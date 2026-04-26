import React from 'react';
const TIPOS = {
  INICIO:{cor:'#4F46E5',icone:'🏁'},DESAFIO:{cor:'#374151',icone:'❓'},
  BONUS:{cor:'#15803D',icone:'⭐'},ARMADILHA:{cor:'#991B1B',icone:'💀'},TESOURO:{cor:'#92400E',icone:'🏆'},
};
export default function BoardSquare({ numero, tipo, jogadores }) {
  const cfg = TIPOS[tipo] || TIPOS.DESAFIO;
  // Jogadores que estão nesta casa
  const aqui = (jogadores || []).filter(j => j.posicaoAtual === numero);
  const temAlguem = aqui.length > 0;
  return (
    <div
        style={{
        position:'relative',width:'100%',
            aspectRatio:'1',
            background:cfg.cor,
            borderRadius:6,
            border:temAlguem?'2px solid var(--gold)':'1px solid rgba(255,255,255,0.07)',
            display:'flex',flexDirection:'column',
            alignItems:'center',justifyContent:'center',
            gap:1,boxShadow:temAlguem?'0 0 16px rgba(212,160,23,0.5)':'none',
            transition:'border 0.15s,box-shadow 0.15s',
            overflow:'visible'}}>
      <span style={{fontFamily:'var(--font-display)',fontSize:9,color:'rgba(255,255,255,0.55)',lineHeight:1}}>{numero}</span>
      <span style={{fontSize:13,lineHeight:1}}>{cfg.icone}</span>
      {temAlguem && (
        <div style={{position:'absolute',top:-18,left:'50%',transform:'translateX(-50%)',display:'flex',gap:1,zIndex:10}}>
          {aqui.map((j,i) => (
            <span key={i} style={{fontSize:16,filter:'drop-shadow(0 2px 5px rgba(0,0,0,0.9))',animation:'pawnBounce 1.1s infinite ease-in-out',animationDelay:`${i*0.15}s`}}>{j.emoji}</span>
          ))}
        </div>
      )}
      <style>{`@keyframes pawnBounce{0%,100%{transform:translateY(0) scale(1)}45%{transform:translateY(-5px) scale(1.08)}}`}</style>
    </div>
  );
}
