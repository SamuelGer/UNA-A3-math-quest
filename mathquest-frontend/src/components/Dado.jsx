import React, { useEffect, useRef, useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { useBreakpoint } from '../hooks/useBreakpoint';

const FACES = {
  1:[[50,50]],
  2:[[28,28],[72,72]],
  3:[[28,28],[50,50],[72,72]],
  4:[[28,28],[72,28],[28,72],[72,72]],
  5:[[28,28],[72,28],[50,50],[28,72],[72,72]],
  6:[[28,22],[72,22],[28,50],[72,50],[28,78],[72,78]],
};

export default function Dado({ valor, rolando, onRolar, podeLancar }) {
  const bp = useBreakpoint();
  const [face, setFace] = useState(1);
  const ref = useRef(null);
  useEffect(() => {
    if (rolando) { ref.current = setInterval(() => setFace(Math.floor(Math.random()*6)+1), 90); }
    else { clearInterval(ref.current); if (valor >= 1 && valor <= 6) setFace(valor); }
    return () => clearInterval(ref.current);
  }, [rolando, valor]);
  const pts = FACES[face] || FACES[1];
  return (
    <div style={{display:'flex',flexDirection:'column',alignItems:'center',gap:8}}>
      <motion.div
        onClick={podeLancar && !rolando ? onRolar : undefined}
        animate={rolando ? {rotate:[0,-18,18,-12,12,-6,6,0],scale:[1,1.12,0.94,1.08,0.98,1.04,1]} : {}}
        whileHover={podeLancar && !rolando ? {scale:1.1,rotate:4} : {}}
        whileTap={podeLancar && !rolando ? {scale:0.9} : {}}
        transition={{duration:0.85}}
        style={{width: bp === 'compact' ? 56 : 72,
            height: bp === 'compact' ? 56 : 72,
            background:'linear-gradient(135deg,#F5F0E8,#D4C9A8)',borderRadius:14,position:'relative',cursor:podeLancar&&!rolando?'pointer':'default',opacity:!podeLancar?0.5:1,boxShadow:'0 4px 0 #9A8C6A,0 6px 14px rgba(0,0,0,0.55),inset 0 1px 0 rgba(255,255,255,0.6)'}}
      >
        {pts.map(([x,y],i) => (
          <motion.div key={`${face}-${i}`} initial={{scale:0}} animate={{scale:1}} transition={{delay:i*0.03,duration:0.12}}
            style={{position:'absolute',width:10,height:10,background:'#1A1A0E',borderRadius:'50%',left:`${x}%`,top:`${y}%`,transform:'translate(-50%,-50%)'}} />
        ))}
      </motion.div>
      <AnimatePresence mode="wait">
        {!rolando && valor > 0 && (
          <motion.div key={valor} initial={{opacity:0,y:-8,scale:0.7}} animate={{opacity:1,y:0,scale:1}} exit={{opacity:0}} transition={{type:'spring',damping:10,stiffness:220}}
            style={{fontFamily:'var(--font-display)',fontSize:30,fontWeight:900,color:'var(--gold)',textShadow:'0 0 18px rgba(212,160,23,0.55)'}}>{valor}</motion.div>
        )}
      </AnimatePresence>
      <p style={{fontFamily:'var(--font-display)',fontSize:10,color:'var(--gold-dim)',letterSpacing:1,textTransform:'uppercase',textAlign:'center'}}>
        {rolando?'Rolando...':podeLancar?'Clique para rolar':'Aguarde...'}
      </p>
    </div>
  );
}
