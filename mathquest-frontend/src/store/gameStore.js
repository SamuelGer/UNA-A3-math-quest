import { create } from 'zustand';

export const EMOJIS = ['🧙', '🧝', '🙍‍♂️', '🧙‍♂️'];

const mesclarEmojis = (jogadoresBackend, jogadoresLocais) =>
  jogadoresBackend.map((p, i) => ({ ...p, emoji: jogadoresLocais[i]?.emoji || EMOJIS[i] }));

const useGameStore = create((set, get) => ({
  tela: 'menu',
  gameId: null,
  turnoAtual: 0,
  jogadores: [],
  status: null,
  mensagemFim: null,
  dadoResultado: null,
  rolandoDado: false,
  questaoAtual: null,
  respostaFeedback: null,
  explicacaoAtual: null,
  novaDicaMensagem: null,
  animacaoPendente: null,
  proximoTurnoIdx: null,

  limparAnimacao: () => set({ animacaoPendente: null }),
  setRolandoDado: (v) => set({ rolandoDado: v }),

  iniciarPartida: (data, emojisEscolhidos) => {
    const jogadores = (data.jogadores || []).map((p, i) => ({
      ...p, emoji: emojisEscolhidos[i] || EMOJIS[i],
    }));
    set({
      tela: 'jogo',
      gameId: data.gameId,
      turnoAtual: 0,
      jogadores,
      status: data.status,
      dadoResultado: null,
      questaoAtual: null,
      respostaFeedback: null,
      explicacaoAtual: null,
      novaDicaMensagem: null,
      animacaoPendente: null,
      proximoTurnoIdx: null,
    });
  },

  processarRespostaDado: (data, indiceJogador) => {
    const { jogadores } = get();
    const atualizados = data.jogadores?.length > 0
      ? mesclarEmojis(data.jogadores, jogadores)
      : jogadores;

    const updates = {
      rolandoDado: false,
      dadoResultado: data.dadoResultado ?? null,
      jogadores: atualizados,
      status: data.status,
      novaDicaMensagem: null,
      animacaoPendente: data.posicaoAtual != null
        ? { posicao: data.posicaoAtual, indice: indiceJogador }
        : null,
    };

    if (data.tipoDaCasaAtual === 'BONUS') {
      updates.novaDicaMensagem = data.dica || '💡 Dica adquirida!';
    }
    if (data.questaoAtual) {
      updates.questaoAtual     = data.questaoAtual;
      updates.respostaFeedback = null;
      updates.explicacaoAtual  = null;
    }
    if (data.status === 'FIM') {
      updates.mensagemFim = data.mensagem;
      updates.tela        = 'fim';
    }
    set(updates);
  },

  processarRespostaQuestao: (data) => {
    const { turnoAtual, jogadores } = get();
    const atualizados = data.jogadores?.length > 0
      ? mesclarEmojis(data.jogadores, jogadores)
      : jogadores;

    const proximoIdx = data.turnoAtual; // ← ponto e vírgula que estava faltando

    const updates = {
      jogadores: atualizados,
      respostaFeedback: data.acertou ? 'acerto' : 'erro',
      explicacaoAtual: data.questaoAtual?.explicacao ?? null,
      status: data.status,
      proximoTurnoIdx: proximoIdx,
    };

    if (data.posicaoAtual != null) {
      updates.animacaoPendente = { posicao: data.posicaoAtual, indice: turnoAtual };
    }
    if (data.status === 'FIM') {
      updates.mensagemFim = data.mensagem;
      updates.tela        = 'fim';
    }
    set(updates);
  },

  confirmarTurno: () => {
    const { proximoTurnoIdx } = get();
    set({
      turnoAtual:       proximoTurnoIdx ?? 0,
      questaoAtual:     null,
      respostaFeedback: null,
      explicacaoAtual:  null,
      dadoResultado:    null,
      proximoTurnoIdx:  null,
    });
  },

  fecharQuestao:  () => set({ questaoAtual: null, respostaFeedback: null, explicacaoAtual: null }),
  fecharNovaDica: () => set({ novaDicaMensagem: null }),

  usarDicaNaQuestao: () => {
    const { jogadores, turnoAtual } = get();
    set({
      jogadores: jogadores.map((j, i) =>
        i !== turnoAtual ? j : { ...j, dicasDisponiveis: Math.max(0, (j.dicasDisponiveis || 0) - 1) }
      ),
    });
  },

  resetar: () => set({
    tela: 'menu', gameId: null, turnoAtual: 0, jogadores: [], status: null,
    mensagemFim: null, dadoResultado: null, rolandoDado: false,
    questaoAtual: null, respostaFeedback: null, explicacaoAtual: null,
    novaDicaMensagem: null, animacaoPendente: null, proximoTurnoIdx: null,
  }),
}));

export default useGameStore;
