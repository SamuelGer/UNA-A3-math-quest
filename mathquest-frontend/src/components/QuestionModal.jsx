import React, { useState, useEffect, useRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import useGameStore from '../store/gameStore';
import { apiResponder, apiUsarDica } from '../services/api';
import { useBreakpoint } from '../hooks/useBreakpoint';

const CAT = {
  BOOLEANA:    { label:'Casa Booleana',   cor:'#3B82F6', timer:30 },
  FUNCOES:     { label:'Casa de Funções', cor:'#22C55E', timer:45 },
  BASES:       { label:'Casa de Bases',   cor:'#F97316', timer:40 },
  POTENCIACAO: { label:'Potenciação',     cor:'#EAB308', timer:45 },
  CONTAGEM:    { label:'Contagem',        cor:'#EF4444', timer:45 },
};

export default function QuestionModal() {
  const bp = useBreakpoint();
  const {
    questaoAtual, gameId,
    processarRespostaQuestao, confirmarTurno,  // ← confirmarTurno no lugar de fecharQuestao
    respostaFeedback, explicacaoAtual,
    jogadores, turnoAtual,                     // ← para pegar dicasDisponiveis do jogador ativo
  } = useGameStore();

  const [opcaoSelecionada, setOpcaoSelecionada] = useState(null);
  const [enviando,         setEnviando]         = useState(false);
  const [timer,            setTimer]            = useState(45);
  const [dicaVisivel,      setDicaVisivel]      = useState(null);
  const [carregandoDica,   setCarregandoDica]   = useState(false);
  const [podeContinuar,    setPodeContinuar]    = useState(false);
  const timerRef = useRef(null);

  const jogadorAtivo = jogadores[turnoAtual];
  const dicasDisp    = jogadorAtivo?.dicasDisponiveis || 0;  // ← substituiu dicasAcumuladas
  const cat          = CAT[questaoAtual?.categoria] || { label:'Desafio', cor:'#6B7280', timer:45 };
  const timerMax     = cat.timer;

  // Reset a cada nova questão
  useEffect(() => {
    if (!questaoAtual) return;
    setOpcaoSelecionada(null);
    setEnviando(false);
    setTimer(timerMax);
    setDicaVisivel(null);
    setCarregandoDica(false);
    setPodeContinuar(false);
  }, [questaoAtual?.id]);

  // Countdown
  useEffect(() => {
    if (!questaoAtual || respostaFeedback) { clearInterval(timerRef.current); return; }
    timerRef.current = setInterval(() => {
      setTimer(t => {
        if (t <= 1) { clearInterval(timerRef.current); enviar(null); return 0; }
        return t - 1;
      });
    }, 1000);
    return () => clearInterval(timerRef.current);
  }, [questaoAtual?.id, respostaFeedback]);

  // Responder questão
  const enviar = async (textoOpcao) => {
    if (enviando || respostaFeedback) return;
    clearInterval(timerRef.current);
    setEnviando(true);
    setOpcaoSelecionada(textoOpcao);
    try {
      const res = await apiResponder({
        gameId,
        questaoId:           questaoAtual.id,
        indiceResposta:      -1,
        respostaSelecionada: textoOpcao ?? '',
      });
      processarRespostaQuestao(res.data);
      setTimeout(() => setPodeContinuar(true), 900);
    } catch {
      setEnviando(false);
    }
  };

  // Usar dica
  const handleUsarDica = async () => {
    if (dicaVisivel || carregandoDica || dicasDisp === 0) return;
    setCarregandoDica(true);
    try {
      const res = await apiUsarDica(gameId, questaoAtual.id);
      useGameStore.getState().usarDicaNaQuestao();
      setDicaVisivel(res.data.dica || questaoAtual.dica || '💡 Dica não disponível.');
    } catch {
      setDicaVisivel(questaoAtual.dica || '💡 Dica não disponível.');
      useGameStore.getState().usarDicaNaQuestao();
    } finally {
      setCarregandoDica(false);
    }
  };

  if (!questaoAtual) return null;

  const pct      = (timer / timerMax) * 100;
  const timerCor = pct > 45 ? '#22C55E' : pct > 22 ? '#EAB308' : '#EF4444';

  return (
    <div style={{
      position:'fixed', inset:0,
      background:'rgba(0,0,0,0.86)', backdropFilter:'blur(6px)',
      display:'flex', alignItems:'center', justifyContent:'center',
      zIndex:200, padding:20,
    }}>
      <motion.div
        initial={{ y:40, opacity:0, scale:0.94 }}
        animate={{ y:0, opacity:1, scale:1 }}
        transition={{ type:'spring', damping:18 }}
        style={{
          background:'#0E0E08', border:'1px solid #2A2820',
          borderRadius:18, padding: bp === 'compact' ? 18 : 32,
          width:'100%', maxWidth: bp === 'compact' ? '95vw' : 540,
          maxHeight:'90vh', overflowY:'auto',
        }}
      >
        {/* Header: categoria + timer */}
        <div style={{ display:'flex', justifyContent:'space-between', alignItems:'center', marginBottom:18, gap:12 }}>
          <span style={{
            fontFamily:'var(--font-display)', fontSize: bp === 'compact' ? 15 : 19, fontWeight:700,
            letterSpacing:2, textTransform:'uppercase',
            color:cat.cor, padding:'3px 12px',
            border:`1px solid ${cat.cor}`, borderRadius:20, whiteSpace:'nowrap',
          }}>{cat.label}</span>

          {!respostaFeedback && (
            <div style={{ flex:1, display:'flex', alignItems:'center', gap:8 }}>
              <div style={{ flex:1, height:4, background:'#1E1E14', borderRadius:2, overflow:'hidden' }}>
                <div style={{ height:'100%', width:`${pct}%`, background:timerCor, transition:'width 1s linear, background 0.4s' }} />
              </div>
              <span style={{ fontFamily:'var(--font-display)', fontSize:12, fontWeight:700, color:timerCor, minWidth:28 }}>
                {timer}s
              </span>
            </div>
          )}
        </div>

        {/* Pergunta */}
        <p style={{ fontFamily:'var(--font-body)', fontSize:19, color:'#E5D9B6', lineHeight:1.65, marginBottom:22 }}>
          {questaoAtual.pergunta}
        </p>

        {/* Dica revelada */}
        <AnimatePresence>
          {dicaVisivel && (
            <motion.div
              initial={{ opacity:0, height:0 }} animate={{ opacity:1, height:'auto' }}
              style={{
                background:'rgba(212,160,23,0.09)', border:'1px solid var(--gold-dim)',
                borderRadius:8, padding:'10px 14px', marginBottom:14,
                fontSize:13, color:'var(--gold)', fontStyle:'italic',
              }}
            >
              💡 {dicaVisivel}
            </motion.div>
          )}
        </AnimatePresence>

        {/* Opções */}
        <div style={{ display:'flex', flexDirection:'column', gap:9, marginBottom:14 }}>
          {questaoAtual.opcoes?.map((opcao, i) => {
            const isSel     = opcaoSelecionada === opcao;
            const isCorrect = respostaFeedback === 'erro' && i === questaoAtual.indiceCorreto;
            let bg = 'rgba(255,255,255,0.03)', border = '1px solid #2A2820', color = '#C8BBAA';
            if (isSel && respostaFeedback === 'acerto') { bg='rgba(34,197,94,0.14)';  border='1px solid #22C55E'; color='#86EFAC'; }
            if (isSel && respostaFeedback === 'erro')   { bg='rgba(239,68,68,0.14)';  border='1px solid #EF4444'; color='#FCA5A5'; }
            if (isCorrect)                               { bg='rgba(34,197,94,0.14)'; border='1px solid #22C55E'; color='#86EFAC'; }

            return (
              <motion.button
                key={i}
                onClick={() => !enviando && !respostaFeedback && enviar(opcao)}
                whileHover={!enviando && !respostaFeedback ? { x:4 } : {}}
                whileTap={!enviando && !respostaFeedback ? { scale:0.98 } : {}}
                disabled={enviando || !!respostaFeedback}
                style={{
                  display:'flex', alignItems:'center', gap:14,
                  padding:'13px 16px', background:bg, border, borderRadius:8,
                  color, fontFamily:'var(--font-body)', fontSize:16,
                  textAlign:'left', cursor: enviando || respostaFeedback ? 'default' : 'pointer',
                  transition:'all 0.15s',
                }}
              >
                <span style={{
                  width:26, height:26, borderRadius:'50%',
                  background:'rgba(255,255,255,0.05)', border:'1px solid #3A3828',
                  display:'flex', alignItems:'center', justifyContent:'center',
                  fontFamily:'var(--font-display)', fontSize:10, fontWeight:700,
                  color:'var(--gold-dim)', flexShrink:0,
                }}>{String.fromCharCode(65+i)}</span>
                {opcao}
              </motion.button>
            );
          })}
        </div>

        {/* Feedback */}
        <AnimatePresence>
          {respostaFeedback && (
            <motion.div
              initial={{ opacity:0, y:8 }} animate={{ opacity:1, y:0 }}
              style={{
                textAlign:'center', padding:'11px 16px', borderRadius:8, marginBottom:10,
                fontFamily:'var(--font-display)', fontSize:14, fontWeight:700, letterSpacing:1,
                background: respostaFeedback==='acerto' ? 'rgba(34,197,94,0.12)' : 'rgba(239,68,68,0.12)',
                color:      respostaFeedback==='acerto' ? '#86EFAC' : '#FCA5A5',
              }}
            >
              {respostaFeedback==='acerto' ? '✅ Correto!' : '❌ Incorreto!'}
            </motion.div>
          )}
        </AnimatePresence>

        {/* Explicação */}
        <AnimatePresence>
          {explicacaoAtual && (
            <motion.div
              initial={{ opacity:0, height:0 }} animate={{ opacity:1, height:'auto' }}
              transition={{ delay:0.25 }}
              style={{
                background:'rgba(212,160,23,0.07)', border:'1px solid var(--gold-dim)',
                borderRadius:8, padding:'12px 14px', marginBottom:12, overflow:'hidden',
              }}
            >
              <span style={{
                display:'block', fontFamily:'var(--font-display)', fontSize:9,
                color:'var(--gold-dim)', letterSpacing:2, textTransform:'uppercase', marginBottom:5,
              }}>Explicação</span>
              <p style={{ fontFamily:'var(--font-body)', fontSize:14, color:'#C8A84A', lineHeight:1.6, fontStyle:'italic' }}>
                {explicacaoAtual}
              </p>
            </motion.div>
          )}
        </AnimatePresence>

        {/* Botão Continuar — aparece após 900ms do feedback */}
        <AnimatePresence>
          {podeContinuar && (
            <motion.button
              initial={{ opacity:0, y:8 }} animate={{ opacity:1, y:0 }}
              whileHover={{ scale:1.02 }} whileTap={{ scale:0.97 }}
              onClick={confirmarTurno}
              style={{
                display:'block', width:'100%', padding:14, marginBottom:10,
                background:'linear-gradient(135deg, var(--gold), var(--gold-dim))',
                border:'none', borderRadius:8,
                color:'#0A0A06', fontFamily:'var(--font-display)',
                fontSize:13, fontWeight:700, letterSpacing:2,
                textTransform:'uppercase', cursor:'pointer',
              }}
            >
              Continuar
            </motion.button>
          )}
        </AnimatePresence>

        {/* Botão Usar Dica */}
        {!respostaFeedback && !dicaVisivel && dicasDisp > 0 && (
          <motion.button
            whileHover={{ scale:1.01 }}
            whileTap={{ scale:0.97 }}
            onClick={handleUsarDica}
            disabled={carregandoDica}
            style={{
              display:'block', width:'100%', padding:'11px 16px',
              background:'rgba(212,160,23,0.07)',
              border:'1px solid var(--gold-dim)', borderRadius:8,
              color:'var(--gold)', fontFamily:'var(--font-display)',
              fontSize:11, fontWeight:600, letterSpacing:1.5,
              textTransform:'uppercase', cursor: carregandoDica ? 'wait' : 'pointer',
              transition:'all 0.2s',
            }}
          >
            {carregandoDica ? 'Buscando dica...' : `💡 Usar Dica (${dicasDisp} disponível)`}
          </motion.button>
        )}
      </motion.div>
    </div>
  );
}
