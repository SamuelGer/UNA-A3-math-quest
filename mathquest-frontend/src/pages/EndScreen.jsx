import React from 'react';
import { motion } from 'framer-motion';
import useGameStore from '../store/gameStore';
import MathBackground from '../components/MathBackground';

export default function EndScreen() {
  const { nomeJogador, pontos, posicaoAtual, mensagemFim, resetar } = useGameStore();
  return (
    <div style={{
      position:'relative', width:'100vw', height:'100vh',
      display:'flex', alignItems:'center', justifyContent:'center',
      background:'radial-gradient(ellipse at center, #1A1408 0%, #0A0A06 70%)',
      overflow:'hidden',
    }}>
      <MathBackground />
      <motion.div
        initial={{ opacity:0, scale:0.88 }} animate={{ opacity:1, scale:1 }}
        transition={{ duration:0.55 }}
        style={{
          position:'relative', zIndex:1, textAlign:'center',
          padding:'46px 40px', background:'rgba(14,14,8,0.97)',
          border:'1px solid var(--gold-dim)', borderRadius:18,
          minWidth:340, boxShadow:'0 0 80px rgba(212,160,23,0.14)',
          display:'flex', flexDirection:'column', alignItems:'center', gap:18,
        }}
      >
        <motion.div
          animate={{ y:[0,-12,0] }} transition={{ duration:2, repeat:Infinity, ease:'easeInOut' }}
          style={{ fontSize:68, filter:'drop-shadow(0 0 18px rgba(212,160,23,0.55))' }}
        >🏆</motion.div>

        <h1 style={{ fontFamily:'var(--font-display)', fontSize:34, fontWeight:900, color:'var(--gold)', letterSpacing:4, textShadow:'0 0 28px rgba(212,160,23,0.4)' }}>
          Parabéns!
        </h1>
        <p style={{ fontFamily:'var(--font-display)', fontSize:18, color:'#E5D9B6', letterSpacing:2 }}>{nomeJogador}</p>

        <div style={{ width:200, height:1, background:'linear-gradient(to right, transparent, var(--gold-dim), transparent)' }} />

        <div style={{ display:'flex', gap:48 }}>
          {[['Pontos', pontos],['Casa Final', posicaoAtual]].map(([lbl, val]) => (
            <div key={lbl} style={{ display:'flex', flexDirection:'column', alignItems:'center', gap:3 }}>
              <span style={{ fontFamily:'var(--font-display)', fontSize:38, fontWeight:900, color:'var(--gold)', lineHeight:1 }}>{val}</span>
              <span style={{ fontFamily:'var(--font-display)', fontSize:10, color:'#5A5040', letterSpacing:2, textTransform:'uppercase' }}>{lbl}</span>
            </div>
          ))}
        </div>

        {mensagemFim && (
          <p style={{ fontFamily:'var(--font-body)', fontSize:15, color:'#8B7D5A', fontStyle:'italic', maxWidth:300 }}>{mensagemFim}</p>
        )}

        <div style={{ display:'flex', flexDirection:'column', gap:10, width:'100%', marginTop:6 }}>
          <motion.button className="btn-primary" onClick={resetar} whileHover={{ scale:1.03 }} whileTap={{ scale:0.97 }}>
            Jogar Novamente
          </motion.button>
          <motion.button className="btn-ghost" onClick={resetar} whileHover={{ scale:1.03 }} whileTap={{ scale:0.97 }}>
            Menu Principal
          </motion.button>
        </div>
      </motion.div>
    </div>
  );
}
