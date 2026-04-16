import React from 'react';

const TIPOS = {
  INICIO:    { cor:'#4F46E5', icone:'🏁' },
  DESAFIO:   { cor:'#374151', icone:'❓' },
  BONUS:     { cor:'#15803D', icone:'⭐' },
  ARMADILHA: { cor:'#991B1B', icone:'💀' },
  TESOURO:   { cor:'#92400E', icone:'🏆' },
};

export default function BoardSquare({ numero, tipo, posicaoJogador }) {
  const cfg = TIPOS[tipo] || TIPOS.DESAFIO;
  const temPeao = posicaoJogador === numero;

  return (
    <div style={{
      position:'relative', width:'100%', aspectRatio:'1',
      background: cfg.cor,
      borderRadius: 6,
      border: temPeao
        ? '2px solid var(--gold)'
        : '1px solid rgba(255,255,255,0.07)',
      display:'flex', flexDirection:'column',
      alignItems:'center', justifyContent:'center',
      gap: 1,
      boxShadow: temPeao ? '0 0 16px rgba(212,160,23,0.5)' : 'none',
      transition: 'border 0.15s, box-shadow 0.15s',
      overflow: 'visible',
    }}>
      <span style={{ fontFamily:'var(--font-display)', fontSize:9, color:'rgba(255,255,255,0.55)', lineHeight:1 }}>
        {numero}
      </span>
      <span style={{ fontSize:13, lineHeight:1 }}>{cfg.icone}</span>

      {temPeao && (
        <div style={{
          position:'absolute', top:-16, left:'50%',
          transform:'translateX(-50%)',
          fontSize:20, zIndex:10,
          filter:'drop-shadow(0 2px 5px rgba(0,0,0,0.9))',
          animation:'pawnBounce 1.1s infinite ease-in-out',
        }}>🧙</div>
      )}

      <style>{`
        @keyframes pawnBounce {
          0%,100% { transform: translateX(-50%) translateY(0) scale(1); }
          45%      { transform: translateX(-50%) translateY(-5px) scale(1.08); }
        }
      `}</style>
    </div>
  );
}
