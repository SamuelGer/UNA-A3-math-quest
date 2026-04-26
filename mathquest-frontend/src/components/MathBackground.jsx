import React from 'react';
const SYMS = [
  {s:'Σ',t:'10%',l:'7%',sz:'72px',d:'0s'},{s:'∫',t:'33%',l:'3%',sz:'56px',d:'3s'},
  {s:'√',t:'8%',l:'54%',sz:'64px',d:'1.5s'},{s:'∞',t:'28%',l:'89%',sz:'52px',d:'4s'},
  {s:'π',t:'74%',l:'5%',sz:'58px',d:'2s'},{s:'Δ',t:'80%',l:'91%',sz:'54px',d:'5s'},
  {s:'∂',t:'58%',l:'51%',sz:'42px',d:'6s'},{s:'∑',t:'13%',l:'79%',sz:'46px',d:'2.5s'},
  {s:'λ',t:'41%',l:'67%',sz:'36px',d:'7s'},{s:'∀',t:'68%',l:'74%',sz:'40px',d:'1s'},
];
export default function MathBackground() {
  return (
    <div className="math-bg">
      {SYMS.map((s,i) => (
        <span key={i} style={{top:s.t,left:s.l,fontSize:s.sz,animationDelay:s.d,animationDuration:`${16+i*2}s`}}>{s.s}</span>
      ))}
    </div>
  );
}
