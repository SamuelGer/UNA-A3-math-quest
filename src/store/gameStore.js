import { create } from 'zustand';

// ═══════════════════════════════════════
// STORE v2 — lógica mínima e previsível
// Regra: atualizarEstado SEMPRE sobrescreve
// questaoAtual controla se o modal aparece
// ═══════════════════════════════════════

const useGameStore = create((set, get) => ({
  // Navegação
  tela: 'menu',   // 'menu' | 'jogo' | 'fim'

  // Partida
  gameId:          null,
  nomeJogador:     '',
  posicaoAtual:    0,
  pontos:          0,
  dicasDisponiveis: 0,
  status:          null,
  mensagemFim:     null,

  // Dado
  dadoResultado:   null,
  rolandoDado:     false,

  // Tabuleiro
  tipoDaCasaAtual: null,

  // Questão — null = modal fechado, objeto = modal aberto
  questaoAtual:    null,
  respostaFeedback: null,   // null | 'acerto' | 'erro'
  explicacaoAtual: null,

  // Dicas
  dicasAcumuladas:  [],
  novaDicaMensagem: null,   // texto da dica recebida no bônus

  // ── Actions ──────────────────────────

  setRolandoDado: (v) => set({ rolandoDado: v }),

  iniciarPartida: (data) => set({
    tela:             'jogo',
    gameId:           data.gameId,
    nomeJogador:      data.nomeJogadorAtual,
    posicaoAtual:     data.posicaoAtual ?? 0,
    pontos:           data.pontos ?? 0,
    dicasDisponiveis: data.dicasDisponiveis ?? 0,
    status:           data.status,
  }),

  // Chamado após ROLAR O DADO — sempre atualiza posição e pode abrir questão
  processarRespostaDado: (data) => {
    const updates = {
      rolandoDado:      false,
      posicaoAtual:     data.posicaoAtual ?? get().posicaoAtual,
      pontos:           data.pontos ?? get().pontos,
      dicasDisponiveis: data.dicasDisponiveis ?? get().dicasDisponiveis,
      dadoResultado:    data.dadoResultado,
      tipoDaCasaAtual:  data.tipoDaCasaAtual,
      status:           data.status,
    };

    // Casa BÔNUS — recebeu dica
    if (data.tipoDaCasaAtual === 'BONUS') {
      updates.dicasAcumuladas  = [...get().dicasAcumuladas, data.dica ?? '💡 Dica adquirida!'];
      updates.novaDicaMensagem = data.dica ?? '💡 Dica adquirida!';
    } else {
      updates.novaDicaMensagem = null;
    }

    // Tem questão → abre modal
    if (data.questaoAtual) {
      updates.questaoAtual     = data.questaoAtual;
      updates.respostaFeedback = null;
      updates.explicacaoAtual  = null;
    }

    // Jogo terminou
    if (data.status === 'FIM') {
      updates.mensagemFim = data.mensagem;
      updates.tela        = 'fim';
    }

    set(updates);
  },

  // Chamado após RESPONDER QUESTÃO — NÃO muda posição nem reabre questão
  processarRespostaQuestao: (data) => {
    set({
      pontos:           data.pontos ?? get().pontos,
      dicasDisponiveis: data.dicasDisponiveis ?? get().dicasDisponiveis,
      respostaFeedback: data.acertou ? 'acerto' : 'erro',
      explicacaoAtual:  data.questaoAtual?.explicacao ?? null,
      status:           data.status,
    });

    // Jogo terminou
    if (data.status === 'FIM') {
      set({ mensagemFim: data.mensagem, tela: 'fim' });
    }
  },

  fecharQuestao: () => set({
    questaoAtual:     null,
    respostaFeedback: null,
    explicacaoAtual:  null,
  }),

  fecharNovaDica: () => set({ novaDicaMensagem: null }),

  usarDicaNaQuestao: () => {
    const { dicasAcumuladas } = get();
    if (dicasAcumuladas.length === 0) return null;
    const dica = dicasAcumuladas[dicasAcumuladas.length - 1];
    set({ dicasAcumuladas: dicasAcumuladas.slice(0, -1) });
    return dica;
  },

  resetar: () => set({
    tela: 'menu', gameId: null, nomeJogador: '', posicaoAtual: 0,
    pontos: 0, dicasDisponiveis: 0, status: null, mensagemFim: null,
    dadoResultado: null, rolandoDado: false, tipoDaCasaAtual: null,
    questaoAtual: null, respostaFeedback: null, explicacaoAtual: null,
    dicasAcumuladas: [], novaDicaMensagem: null,
  }),
}));

export default useGameStore;
