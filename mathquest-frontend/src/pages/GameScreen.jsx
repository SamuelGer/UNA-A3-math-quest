import React, { useEffect, useState, useRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import useGameStore from '../store/gameStore';
import { apiRolarDado, apiBuscarTabuleiro } from '../services/api';
import BoardSquare from '../components/BoardSquare';
import Dado from '../components/Dado';
import QuestionModal from '../components/QuestionModal';
import MathBackground from '../components/MathBackground';

function buildLayout() {
  const rows = [];
  for (let r = 0; r < 5; r++) {
    const start = r * 6 + 1;
    const row = [start, start+1, start+2, start+3, start+4, start+5];
    rows.push(r % 2 === 0 ? row : [...row].reverse());
  }
  return rows.reverse();
}
const LAYOUT = buildLayout();
const TOTAL  = 30;
const TIPO_PADRAO = (n) => n === 1 ? 'INICIO' : n === TOTAL ? 'TESOURO' : 'DESAFIO';


export default function GameScreen() {
  const {
    gameId, nomeJogador, pontos,
    dicasAcumuladas, dadoResultado, questaoAtual,
    rolandoDado, setRolandoDado,
    processarRespostaDado,
    novaDicaMensagem, fecharNovaDica,
    posicaoAtual: posicaoStore,
  } = useGameStore();

  const [tabuleiro, setTabuleiro] = useState({});
  const [peaoPos,   setPeaoPos]   = useState(1);   // começa na casa 1
  const [movendo,   setMovendo]   = useState(false);

  // Ref que SEMPRE tem o valor mais recente do peão
  const peaoPosRef  = useRef(1);
  const intervalRef = useRef(null);

  // Sincroniza ref com state
  useEffect(() => { peaoPosRef.current = peaoPos; }, [peaoPos]);
  useEffect(() => {
    const pos = useGameStore.getState().posicaoAtual;
    if (pos > 0) { setPeaoPos(pos); peaoPosRef.current = pos; }
  }, []);
  useEffect(() => {
    return () => clearInterval(intervalRef.current);
  }, []);
  useEffect(() => {
    if (!movendo && posicaoStore > 0 && posicaoStore !== peaoPosRef.current) {
      animarPeao(posicaoStore);
    }
  }, [posicaoStore]);
  useEffect(() => { const destino = Number(posicaoStore);
    if (!movendo && Number.isFinite(destino) && destino > 0 && destino !== peaoPosRef.current)
      animarPeao(destino); },
      [posicaoStore, movendo]);
  // Carrega tabuleiro
  useEffect(() => {
    if (!gameId) return;
    apiBuscarTabuleiro(gameId)
      .then(res => {
        const m = {};
        res.data.forEach(c => { m[c.numeroCasa] = c.tipo; });
        setTabuleiro(m);
      })
      .catch(() => {});
  }, [gameId]);

  // Anima peão casa a casa — usa a REF para ler posição atual
  const animarPeao = (destino) => {
    if (destino == null || destino < 1 || destino > TOTAL) return;
    clearInterval(intervalRef.current);
    setMovendo(true);

    intervalRef.current = setInterval(() => {
      const atual = peaoPosRef.current;
      if (atual === destino) {
        clearInterval(intervalRef.current);
        setMovendo(false);
        return;
      }
      const prox = atual + (destino > atual ? 1 : -1);
      peaoPosRef.current = prox;
      setPeaoPos(prox);
    }, 420);
  };

  const handleRolar = async () => {
    if (rolandoDado || movendo || questaoAtual) return;
    setRolandoDado(true);
    try {
      const res  = await apiRolarDado(gameId);
      const data = res.data;
      // Após 900ms (dado animando), processa e inicia animação do peão
      setTimeout(() => {
        processarRespostaDado(data);
        const destino = data.posicaoAtual ?? useGameStore.getState().posicaoAtual;
        if (destino != null && destino !== peaoPosRef.current) {
          animarPeao(destino);
        }
      }, 900);
    } catch {
      setRolandoDado(false);
    }
  };

  const getTipo = (n) => tabuleiro[n] || TIPO_PADRAO(n);

  return (
    <div style={{
      position:'relative', width:'100vw', height:'100vh',
      display:'flex', alignItems:'stretch',
      background:'radial-gradient(ellipse at 30% 50%, #141408 0%, #0A0A06 60%)',
      overflow:'hidden',
    }}>
      <MathBackground />

      {/* ── SIDEBAR ── */}
      <motion.aside
        initial={{ x:-60, opacity:0 }} animate={{ x:0, opacity:1 }} transition={{ delay:0.2 }}
        style={{
          position:'relative', zIndex:1, width:210, flexShrink:0,
          padding:'18px 14px', borderRight:'1px solid #1A1A10',
          display:'flex', flexDirection:'column', gap:10, overflowY:'auto',
        }}
      >
        {/* Perfil */}
        <div style={card}>
          <div style={{ fontSize:34, textAlign:'center', marginBottom:6 }}>🧙</div>
          <h2 style={{ fontFamily:'var(--font-display)', fontSize:14, fontWeight:700, color:'var(--gold)', textAlign:'center', letterSpacing:1 }}>
            {nomeJogador}
          </h2>
          <div style={{ width:'60%', height:1, background:'linear-gradient(to right,transparent,var(--gold-dim),transparent)', margin:'10px auto' }} />
          <Stat label="Pontuação" val={pontos} />
          <Stat label="Posição"   val={`Casa ${peaoPos}`} />
        </div>

        {/* Dado */}
        <div style={card}>
          <Dado
            valor={dadoResultado || 0}
            rolando={rolandoDado}
            onRolar={handleRolar}
            podeLancar={!questaoAtual && !rolandoDado && !movendo}
          />
        </div>

        {/* Dicas */}
        <div style={card}>
          <p style={secTitle}>💡 Dicas</p>
          <div style={{ display:'flex', alignItems:'baseline', gap:6 }}>
            <span style={{ fontFamily:'var(--font-display)', fontSize:28, fontWeight:900, color:'var(--gold)' }}>
              {dicasAcumuladas.length}
            </span>
            <span style={{ fontSize:12, color:'#5A5040' }}>disponíveis</span>
          </div>

          <AnimatePresence>
            {novaDicaMensagem && (
              <motion.div
                initial={{ opacity:0, height:0 }} animate={{ opacity:1, height:'auto' }}
                style={{ marginTop:8, background:'rgba(212,160,23,0.09)', border:'1px solid var(--gold-dim)', borderRadius:7, padding:10, overflow:'hidden' }}
              >
                <p style={{ fontFamily:'var(--font-display)', fontSize:9, color:'var(--gold)', letterSpacing:1, textTransform:'uppercase', marginBottom:4 }}>Nova dica!</p>
                <p style={{ color:'#C8A84A', fontStyle:'italic', fontSize:11 }}>{novaDicaMensagem}</p>
                <button onClick={fecharNovaDica} style={{
                  marginTop:7, width:'100%', padding:'5px 8px',
                  background:'transparent', border:'1px solid #3A3828', borderRadius:5,
                  color:'#7A7060', fontSize:10, fontFamily:'var(--font-display)', letterSpacing:1, cursor:'pointer',
                }}>Guardar dica</button>
              </motion.div>
            )}
          </AnimatePresence>
        </div>

        {/* Legenda */}
        <div style={card}>
          <p style={secTitle}>Legenda</p>
          {[['#4F46E5','Início'],['#374151','Desafio'],['#15803D','Bônus'],['#991B1B','Armadilha'],['#92400E','Tesouro']].map(([cor,lbl]) => (
            <div key={lbl} style={{ display:'flex', alignItems:'center', gap:8, padding:'3px 0', fontSize:12, color:'#8B7D5A' }}>
              <div style={{ width:13, height:13, borderRadius:3, background:cor, flexShrink:0 }} />
              {lbl}
            </div>
          ))}
        </div>
      </motion.aside>

      {/* ── TABULEIRO ── */}
      <main style={{
        position:'relative', zIndex:1, flex:1,
        display:'flex', flexDirection:'column', alignItems:'center', justifyContent:'center',
        padding:24, gap:14,
      }}>
        <motion.div
          initial={{ opacity:0, scale:0.95 }} animate={{ opacity:1, scale:1 }}
          transition={{ delay:0.3, duration:0.5 }}
          style={{
            display:'flex', flexDirection:'column', gap:5,
            padding:18, background:'rgba(10,10,6,0.72)',
            border:'1px solid #1E1E16', borderRadius:14,
            boxShadow:'0 0 60px rgba(0,0,0,0.5)',
          }}
        >
          {LAYOUT.map((linha, li) => (
            <div key={li} style={{ display:'flex', gap:5 }}>
              {linha.map(num => (
                <div key={num} style={{ width:'clamp(52px, 7vw, 78px)' }}>
                  <BoardSquare numero={num} tipo={getTipo(num)} posicaoJogador={peaoPos} />
                </div>
              ))}
            </div>
          ))}
        </motion.div>

        {/* Mensagem do dado */}
        <AnimatePresence>
          {dadoResultado > 0 && !rolandoDado && (
            <motion.div
              initial={{ opacity:0, y:16, scale:0.85 }}
              animate={{ opacity:1, y:0, scale:1 }}
              exit={{ opacity:0 }}
              transition={{ type:'spring', damping:12 }}
              style={{
                background:'rgba(14,14,8,0.97)', border:'1px solid var(--gold)',
                borderRadius:8, padding:'12px 24px',
                fontFamily:'var(--font-display)', fontSize:13, color:'#C8A84A',
                letterSpacing:1, boxShadow:'0 0 24px rgba(212,160,23,0.18)',
              }}
            >
              🎲 Você tirou{' '}
              <strong style={{ color:'var(--gold)', fontSize:17 }}>{dadoResultado}</strong>
              {movendo
                ? ' — movendo...'
                : <> — parou na casa <strong style={{ color:'var(--gold)' }}>{peaoPos}</strong>!</>
              }
            </motion.div>
          )}
        </AnimatePresence>
      </main>

      {/* Modal — só aparece quando peão parou */}
      {questaoAtual && !movendo && <QuestionModal />}
    </div>
  );
}

const card    = { background:'rgba(18,18,12,0.92)', border:'1px solid #22221A', borderRadius:8, padding:14 };
const secTitle = { fontFamily:'var(--font-display)', fontSize:10, fontWeight:700, color:'var(--gold-dim)', letterSpacing:2, textTransform:'uppercase', marginBottom:10 };

function Stat({ label, val }) {
  return (
    <div style={{ display:'flex', justifyContent:'space-between', alignItems:'center', padding:'3px 0' }}>
      <span style={{ fontSize:10, color:'#5A5040', fontFamily:'var(--font-display)', letterSpacing:1, textTransform:'uppercase' }}>{label}</span>
      <span style={{ fontFamily:'var(--font-display)', fontSize:13, fontWeight:700, color:'var(--gold)' }}>{val}</span>
    </div>
  );
}
