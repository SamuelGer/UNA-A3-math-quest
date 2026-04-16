import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import MathBackground from '../components/MathBackground';
import useGameStore from '../store/gameStore';
import { apiCriarPartida } from '../services/api';

export default function MenuScreen() {
  const [modal, setModal]   = useState(null); // null | 'jogar' | 'regras' | 'sobre'
  const [nome, setNome]     = useState('');
  const [loading, setLoading] = useState(false);
  const [erro, setErro]     = useState('');
  const { iniciarPartida }  = useGameStore();

  const handleJogar = async () => {
    if (!nome.trim()) { setErro('Insira seu nome para continuar.'); return; }
    setLoading(true); setErro('');
    try {
      const res = await apiCriarPartida(nome.trim());
      iniciarPartida(res.data);
    } catch {
      setErro('Erro ao conectar. Verifique se o backend está rodando.');
      setLoading(false);
    }
  };

  return (
    <div style={{
      position:'relative', width:'100vw', height:'100vh',
      display:'flex', alignItems:'center', justifyContent:'center',
      background:'radial-gradient(ellipse at center, #1A1A0E 0%, #0A0A06 70%)',
      overflow:'hidden',
    }}>
      <MathBackground />

      <motion.div
        initial={{ opacity:0, y:30 }} animate={{ opacity:1, y:0 }}
        transition={{ duration:0.75, ease:'easeOut' }}
        style={{ position:'relative', zIndex:1, width:'100%', maxWidth:380, padding:'0 24px', display:'flex', flexDirection:'column', alignItems:'center', gap:38 }}
      >
        {/* Logo */}
        <div style={{ textAlign:'center' }}>
          <h1 style={{
            fontFamily:'var(--font-display)', fontSize:'clamp(46px,8vw,78px)',
            fontWeight:900, color:'var(--gold)', letterSpacing:4, lineHeight:1,
            textShadow:'0 0 40px rgba(212,160,23,0.4)',
          }}>MathQuest</h1>
          <p style={{ fontFamily:'var(--font-display)', fontSize:13, color:'#8B6914', letterSpacing:6, textTransform:'uppercase', marginTop:6 }}>
            Caça ao Tesouro
          </p>
          <div style={{ width:180, height:1, background:'linear-gradient(to right, transparent, var(--gold-dim), transparent)', margin:'18px auto 0' }} />
        </div>

        {/* Botões */}
        <div style={{ display:'flex', flexDirection:'column', gap:11, width:'100%' }}>
          <motion.button className="btn-primary" onClick={() => setModal('jogar')} whileHover={{ scale:1.02 }} whileTap={{ scale:0.98 }}>
            Novo Jogo
          </motion.button>
          <motion.button className="btn-secondary" onClick={() => setModal('regras')} whileHover={{ scale:1.02 }} whileTap={{ scale:0.98 }}>
            Regras do Jogo
          </motion.button>
          <motion.button className="btn-ghost" onClick={() => setModal('sobre')} whileHover={{ scale:1.02 }} whileTap={{ scale:0.98 }}>
            Sobre
          </motion.button>
        </div>
      </motion.div>

      {/* Modais */}
      <AnimatePresence>
        {modal === 'jogar' && (
          <ModalOverlay onClose={() => setModal(null)}>
            <h2 style={mTitle}>Insira seu Nome</h2>
            <p style={mDesc}>Como deseja ser chamado, aventureiro?</p>
            <input
              className="input-field"
              placeholder="Seu nome aqui..."
              value={nome}
              onChange={e => setNome(e.target.value)}
              onKeyDown={e => e.key === 'Enter' && handleJogar()}
              maxLength={20} autoFocus
            />
            {erro && <p style={{ color:'#EF4444', fontSize:12, marginTop:8, textAlign:'center' }}>{erro}</p>}
            <div style={{ display:'flex', flexDirection:'column', gap:9, marginTop:22 }}>
              <button className="btn-primary" onClick={handleJogar} disabled={loading}>
                {loading ? 'Iniciando...' : 'Começar Aventura'}
              </button>
              <button className="btn-ghost" onClick={() => setModal(null)}>Voltar</button>
            </div>
          </ModalOverlay>
        )}

        {modal === 'regras' && (
          <ModalOverlay onClose={() => setModal(null)} wide>
            <h2 style={mTitle}>Regras do Jogo</h2>
            <div style={{ display:'flex', flexDirection:'column', gap:12, margin:'18px 0' }}>
              {[
                ['🎲','Tabuleiro','30 casas com bônus e armadilhas sorteados a cada partida.'],
                ['❓','Desafio','Responda questões de matemática dentro do tempo para ganhar pontos.'],
                ['⭐','Bônus','Receba uma dica que pode ser usada em qualquer questão futura.'],
                ['💀','Armadilha','Questão difícil! Se errar, o dado rola e você volta casas.'],
              ].map(([ic,tit,desc]) => (
                <div key={tit} style={{ display:'flex', gap:14, alignItems:'flex-start', padding:12, background:'rgba(255,255,255,0.03)', borderRadius:7 }}>
                  <span style={{ fontSize:22 }}>{ic}</span>
                  <div>
                    <strong style={{ fontFamily:'var(--font-display)', fontSize:12, color:'var(--gold)', letterSpacing:1, display:'block', marginBottom:3 }}>{tit}</strong>
                    <p style={{ fontSize:13, color:'#8B7D5A' }}>{desc}</p>
                  </div>
                </div>
              ))}
              <div style={{ background:'rgba(255,255,255,0.02)', border:'1px solid #1E1E16', borderRadius:7, padding:14 }}>
                <p style={{ fontFamily:'var(--font-display)', fontSize:10, color:'var(--gold-dim)', letterSpacing:2, textTransform:'uppercase', marginBottom:10 }}>Pontuação</p>
                {[['Booleana / Bases','7 pts'],['Funções / Potenciação','9 pts'],['Contagem','11 pts']].map(([cat,pts]) => (
                  <div key={cat} style={{ display:'flex', justifyContent:'space-between', padding:'5px 0', borderBottom:'1px solid #1A1A10', fontSize:13, color:'#8B7D5A' }}>
                    <span>{cat}</span><span style={{ color:'var(--gold)' }}>{pts}</span>
                  </div>
                ))}
              </div>
            </div>
            <button className="btn-secondary" onClick={() => setModal(null)}>Fechar</button>
          </ModalOverlay>
        )}

        {modal === 'sobre' && (
          <ModalOverlay onClose={() => setModal(null)}>
            <h2 style={mTitle}>Sobre</h2>
            <p style={{ ...mDesc, textAlign:'center', lineHeight:2, marginBottom:22 }}>
              MathQuest é um jogo educacional de tabuleiro digital desenvolvido para
              <strong> Matemática Computacional Aplicada</strong>.<br/>
              Backend: <strong>Java + Spring Boot</strong><br/>
              Frontend: <strong>React + Framer Motion</strong>
            </p>
            <button className="btn-ghost" onClick={() => setModal(null)}>Fechar</button>
          </ModalOverlay>
        )}
      </AnimatePresence>
    </div>
  );
}

function ModalOverlay({ children, onClose, wide }) {
  return (
    <motion.div
      className="modal-overlay"
      initial={{ opacity:0 }} animate={{ opacity:1 }} exit={{ opacity:0 }}
      onClick={e => e.target === e.currentTarget && onClose()}
    >
      <motion.div
        className="modal-box"
        style={{ maxWidth: wide ? 560 : 480, maxHeight:'85vh', overflowY:'auto' }}
        initial={{ y:36, opacity:0 }} animate={{ y:0, opacity:1 }} exit={{ y:18, opacity:0 }}
      >
        {children}
      </motion.div>
    </motion.div>
  );
}

const mTitle = { fontFamily:'var(--font-display)', fontSize:21, fontWeight:700, color:'var(--gold)', letterSpacing:2, textAlign:'center', marginBottom:6 };
const mDesc  = { fontFamily:'var(--font-body)', fontSize:15, color:'#8B7D5A', textAlign:'center', marginBottom:20 };
