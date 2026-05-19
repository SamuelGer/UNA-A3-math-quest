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
const TIPO_P = (n) => n === 1 ? 'INICIO' : n === TOTAL ? 'TESOURO' : 'DESAFIO';

export default function GameScreen() {
  const {
    gameId, jogadores, turnoAtual,
    dadoResultado, questaoAtual,
    rolandoDado, setRolandoDado, processarRespostaDado,
    novaDicaMensagem, fecharNovaDica,
    animacaoPendente, limparAnimacao,
    respostaFeedback
  } = useGameStore();

  const [tabuleiro, setTabuleiro] = useState({});
  const [peaoPos, setPeaoPos]     = useState([]);   // posição visual de cada peão
  const [movendo, setMovendo]     = useState(false);
  const peaoPosRef  = useRef([]);  // ref síncrona para uso dentro do setInterval
  const intervalRef = useRef(null);

  // Inicializa peaoPos quando os jogadores chegam pela primeira vez
  useEffect(() => {
    if (jogadores.length > 0 && peaoPos.length === 0) {
      const inicial = jogadores.map(j => j.posicaoAtual || 1);
      setPeaoPos(inicial);
      peaoPosRef.current = inicial;
    }
  }, [jogadores.length]);

  // Carrega o layout do tabuleiro do backend
  useEffect(() => {
    if (!gameId) return;
    apiBuscarTabuleiro(gameId).then(res => {
      const m = {};
      res.data.forEach(c => { m[c.numeroCasa] = c.tipo; });
      setTabuleiro(m);
    }).catch(() => {});
  }, [gameId]);

  // Anima o peão do jogador `indice` casa a casa até `destino`
  const animarPeao = (destino, indice) => {
    clearInterval(intervalRef.current);
    setMovendo(true);
    intervalRef.current = setInterval(() => {
      const atual = peaoPosRef.current[indice];
      if (atual === undefined || atual === destino) {
        clearInterval(intervalRef.current);
        setMovendo(false);
        return;
      }
      const prox = atual + (destino > atual ? 1 : -1);
      const nova = [...peaoPosRef.current];
      nova[indice] = prox;
      peaoPosRef.current = nova;
      setPeaoPos([...nova]);
    }, 420);
  };

  // Observa animacaoPendente da store e dispara a animação
  useEffect(() => {
    if (!animacaoPendente) return;
    const { posicao, indice } = animacaoPendente;
    limparAnimacao();          // limpa imediatamente para não disparar duas vezes
    animarPeao(posicao, indice);
  }, [animacaoPendente]);

  const handleRolar = async () => {
    if (rolandoDado || movendo || questaoAtual) return;
    setRolandoDado(true);
    // Captura o turno AGORA antes do await — depois do await pode ter mudado
    const indiceAtual = turnoAtual;
    try {
      const res  = await apiRolarDado(gameId);
      const data = res.data;
      // Aguarda a animação visual do dado (900ms) antes de processar o estado
      setTimeout(() => {
        processarRespostaDado(data, indiceAtual);
      }, 900);
    } catch {
      setRolandoDado(false);
    }
  };

  const jogadorAtivo = jogadores[turnoAtual];
  const getTipo = (n) => tabuleiro[n] || TIPO_P(n);

  // Passa posição animada (visual) para o BoardSquare, não a do backend
  const jogadoresComPos = jogadores.map((j, i) => ({
    ...j,
    posicaoAtual: peaoPos[i] ?? j.posicaoAtual ?? 1,
  }));

  return (
    <div style={{position:'relative',width:'100vw',height:'100vh',display:'flex',alignItems:'stretch',background:'radial-gradient(ellipse at 30% 50%,#141408 0%,#0A0A06 60%)',overflow:'hidden'}}>
      <MathBackground />

      {/* ── SIDEBAR ── */}
      <motion.aside
        initial={{x:-60,opacity:0}} animate={{x:0,opacity:1}} transition={{delay:0.2}}
        style={{position:'relative',zIndex:1,width:240,flexShrink:0,padding:'18px 14px',borderRight:'1px solid #1A1A10',display:'flex',flexDirection:'column',gap:10,overflowY:'auto'}}>

        {/* Jogador ativo */}
        <div style={{...card, border:'1px solid var(--gold-dim)', textAlign:'center'}}>
          <div style={{fontSize:36,marginBottom:4}}>{jogadorAtivo?.emoji || '🧙'}</div>
          <h2 style={{fontFamily:'var(--font-display)',fontSize:14,fontWeight:700,color:'var(--gold)',letterSpacing:1}}>{jogadorAtivo?.nome}</h2>
          <p style={{fontFamily:'var(--font-display)',fontSize:9,color:'var(--gold-dim)',letterSpacing:2,textTransform:'uppercase',marginTop:2}}>Vez de jogar</p>
          <div style={{width:'60%',height:1,background:'linear-gradient(to right,transparent,var(--gold-dim),transparent)',margin:'10px auto'}} />
          <Stat label="Pontuação" val={jogadorAtivo?.pontos ?? 0} />
          <Stat label="Posição"   val={`Casa ${peaoPos[turnoAtual] ?? 1}`} />
          <Stat label="Dicas"     val={jogadorAtivo?.dicasDisponiveis ?? 0} />
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

        {/* Placar */}
        {jogadores.length > 1 && (
          <div style={card}>
            <p style={secTitle}>Placar</p>
            {jogadores.map((j, i) => (
              <div key={i} style={{display:'flex',alignItems:'center',gap:8,padding:'5px 0',borderBottom:'1px solid #1A1A10',opacity:i===turnoAtual?1:0.6}}>
                <span style={{fontSize:18}}>{j.emoji}</span>
                <div style={{flex:1,minWidth:0}}>
                  <p style={{fontFamily:'var(--font-display)',fontSize:11,color:i===turnoAtual?'var(--gold)':'#8B7D5A',fontWeight:700,overflow:'hidden',textOverflow:'ellipsis',whiteSpace:'nowrap'}}>{j.nome}</p>
                  <p style={{fontSize:10,color:'#5A5040'}}>Casa {peaoPos[i]??1} · {j.pontos} pts</p>
                </div>
                {i === turnoAtual && <span style={{fontSize:10,color:'var(--gold)',fontFamily:'var(--font-display)',letterSpacing:1}}>▶</span>}
              </div>
            ))}
          </div>
        )}

        {/* Nova dica */}
        <AnimatePresence>
          {novaDicaMensagem && (
            <motion.div initial={{opacity:0,height:0}} animate={{opacity:1,height:'auto'}} style={{...card,overflow:'hidden'}}>
              <p style={{fontFamily:'var(--font-display)',fontSize:9,color:'var(--gold)',letterSpacing:1,textTransform:'uppercase',marginBottom:4}}>💡 Nova dica!</p>
              <p style={{color:'#C8A84A',fontStyle:'italic',fontSize:11,marginBottom:8}}>{novaDicaMensagem}</p>
              <button onClick={fecharNovaDica} style={{width:'100%',padding:'5px 8px',background:'transparent',border:'1px solid #3A3828',borderRadius:5,color:'#7A7060',fontSize:10,fontFamily:'var(--font-display)',letterSpacing:1,cursor:'pointer'}}>Guardar dica</button>
            </motion.div>
          )}
        </AnimatePresence>

        {/* Legenda */}
        <div style={card}>
          <p style={secTitle}>Legenda</p>
          {[['#4F46E5','Início'],['#374151','Desafio'],['#15803D','Bônus'],['#991B1B','Armadilha'],['#92400E','Tesouro']].map(([cor,lbl]) => (
            <div key={lbl} style={{display:'flex',alignItems:'center',gap:8,padding:'3px 0',fontSize:12,color:'#8B7D5A'}}>
              <div style={{width:13,height:13,borderRadius:3,background:cor,flexShrink:0}} />{lbl}
            </div>
          ))}
        </div>
      </motion.aside>

      {/* ── TABULEIRO ── */}
      <main style={{position:'relative',zIndex:1,flex:1,display:'flex',flexDirection:'column',alignItems:'center',justifyContent:'center',padding:24,gap:14}}>
        <motion.div
          initial={{opacity:0,scale:0.95}} animate={{opacity:1,scale:1}} transition={{delay:0.3,duration:0.5}}
          style={{display:'flex',flexDirection:'column',gap:5,padding:22,background:'rgba(10,10,6,0.72)',border:'1px solid #1E1E16',borderRadius:14,boxShadow:'0 0 60px rgba(0,0,0,0.5)'}}>
          {LAYOUT.map((linha, li) => (
            <div key={li} style={{display:'flex',gap:6}}>
              {linha.map(num => (
                <div key={num} style={{width:'clamp(68px,8vw,96px)'}}>
                  <BoardSquare numero={num} tipo={getTipo(num)} jogadores={jogadoresComPos} />
                </div>
              ))}
            </div>
          ))}
        </motion.div>

        {/* Banner do dado */}
        <AnimatePresence>
          {dadoResultado > 0 && !rolandoDado && (
            <motion.div
              initial={{opacity:0,y:16,scale:0.85}} animate={{opacity:1,y:0,scale:1}} exit={{opacity:0}} transition={{type:'spring',damping:12}}
              style={{background:'rgba(14,14,8,0.97)',border:'1px solid var(--gold)',borderRadius:8,padding:'12px 24px',fontFamily:'var(--font-display)',fontSize:13,color:'#C8A84A',letterSpacing:1,boxShadow:'0 0 24px rgba(212,160,23,0.18)'}}>
              {jogadorAtivo?.emoji} {jogadorAtivo?.nome} tirou{' '}
              <strong style={{color:'var(--gold)',fontSize:17}}>{dadoResultado}</strong>
              {movendo
                ? ' — movendo...'
                : <> — parou na casa <strong style={{color:'var(--gold)'}}>{peaoPos[turnoAtual] ?? 1}</strong>!</>}
            </motion.div>
          )}
        </AnimatePresence>
      </main>

      {questaoAtual && (!movendo || respostaFeedback) && <QuestionModal />}
    </div>
  );
}

const card     = {background:'rgba(18,18,12,0.92)',border:'1px solid #22221A',borderRadius:8,padding:14};
const secTitle = {fontFamily:'var(--font-display)',fontSize:10,fontWeight:700,color:'var(--gold-dim)',letterSpacing:2,textTransform:'uppercase',marginBottom:10};

function Stat({ label, val }) {
  return (
    <div style={{display:'flex',justifyContent:'space-between',alignItems:'center',padding:'3px 0'}}>
      <span style={{fontSize:10,color:'#5A5040',fontFamily:'var(--font-display)',letterSpacing:1,textTransform:'uppercase'}}>{label}</span>
      <span style={{fontFamily:'var(--font-display)',fontSize:13,fontWeight:700,color:'var(--gold)'}}>{val}</span>
    </div>
  );
}
