import React from 'react';
import { motion } from 'framer-motion';
import useGameStore from '../store/gameStore';
import MathBackground from '../components/MathBackground';
import { useBreakpoint } from '../hooks/useBreakpoint';

const MEDALHAS = ['🥇', '🥈', '🥉'];

export default function EndScreen() {
  const bp = useBreakpoint();
  const { jogadores, mensagemFim, resetar } = useGameStore();

  // Ordena por pontos desc, desempate por posição desc
  const ranking = [...jogadores]
    .sort((a, b) => b.pontos !== a.pontos ? b.pontos - a.pontos : b.posicaoAtual - a.posicaoAtual);

  const vencedor = ranking[0];

  return (
    <div style={{position:'relative',width:'100vw',height:'100vh',display:'flex',alignItems:'center',justifyContent:'center',background:'radial-gradient(ellipse at center,#1A1408 0%,#0A0A06 70%)',overflow:'hidden'}}>
      <MathBackground />
      <motion.div initial={{opacity:0,scale:0.88}} animate={{opacity:1,scale:1}} transition={{duration:0.55}}
        style={{position:'relative',zIndex:1,textAlign:'center',
          padding: bp === 'compact'? '24px 18px' : '40px 36px',
          background:'rgba(14,14,8,0.97)',border:'1px solid var(--gold-dim)',borderRadius:18,width:'90%',
          maxWidth: bp ==='compact' ? '95vw': 480,
          boxShadow:'0 0 80px rgba(212,160,23,0.14)',display:'flex',flexDirection:'column',alignItems:'center',gap:16}}>

        <motion.div animate={{y:[0,-12,0]}} transition={{duration:2,repeat:Infinity,ease:'easeInOut'}}
          style={{fontSize: bp === 'compact' ? 48 : 64,
            filter:'drop-shadow(0 0 18px rgba(212,160,23,0.55))'}}>🏆</motion.div>

        <div>
          <h1 style={{fontFamily:'var(--font-display)',
            fontSize: bp === 'compact' ? 22: 30,
            fontWeight:900,color:'var(--gold)',letterSpacing:3,textShadow:'0 0 28px rgba(212,160,23,0.4)'}}>Parabéns!</h1>
          <p style={{fontFamily:'var(--font-display)',fontSize:16,color:'#E5D9B6',letterSpacing:2,marginTop:4}}>
            {vencedor?.emoji} {vencedor?.nome} venceu!
          </p>
        </div>

        <div style={{width:200,height:1,background:'linear-gradient(to right,transparent,var(--gold-dim),transparent)'}} />

        {/* Ranking */}
        <div style={{width:'100%',display:'flex',flexDirection:'column',gap:8}}>
          <p style={{fontFamily:'var(--font-display)',fontSize:10,color:'var(--gold-dim)',letterSpacing:2,textTransform:'uppercase',marginBottom:4}}>Classificação Final</p>
          {ranking.map((j, i) => (
            <motion.div key={i} initial={{opacity:0,x:-20}} animate={{opacity:1,x:0}} transition={{delay:i*0.1}}
              style={{display:'flex',alignItems:'center',gap:12,padding:'10px 14px',background: i===0?'rgba(212,160,23,0.12)':'rgba(255,255,255,0.03)',border: i===0?'1px solid var(--gold-dim)':'1px solid #1E1E16',borderRadius:8}}>
              <span style={{fontSize:22,minWidth:28}}>{MEDALHAS[i] || `${i+1}º`}</span>
              <span style={{fontSize: bp === 'compact' ? 18 :24}}>{j.emoji}</span>
              <div style={{flex:1,textAlign:'left'}}>
                <p style={{fontFamily:'var(--font-display)',fontSize:13,fontWeight:700,color: i===0?'var(--gold)':'#C8BBAA'}}>{j.nome}</p>
                <p style={{fontSize:11,color:'#5A5040'}}>Casa {j.posicaoAtual}</p>
              </div>
              <span style={{fontFamily:'var(--font-display)',
                fontSize: bp === 'compact' ? 13 : 16,
                fontWeight:900,color: i===0?'var(--gold)':'#8B7D5A'}}>{j.pontos} pts</span>
            </motion.div>
          ))}
        </div>

        {mensagemFim && <p style={{fontFamily:'var(--font-body)',fontSize:14,color:'#8B7D5A',fontStyle:'italic'}}>{mensagemFim}</p>}

        <div style={{display:'flex',flexDirection:'column',gap:10,width:'100%',marginTop:4}}>
          <motion.button className="btn-primary" onClick={resetar} whileHover={{scale:1.03}} whileTap={{scale:0.97}}>Jogar Novamente</motion.button>
          <motion.button className="btn-ghost"    onClick={resetar} whileHover={{scale:1.03}} whileTap={{scale:0.97}}>Menu Principal</motion.button>
        </div>
      </motion.div>
    </div>
  );
}
