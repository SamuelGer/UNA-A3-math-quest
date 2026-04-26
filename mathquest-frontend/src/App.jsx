import React from 'react';
import useGameStore from './store/gameStore';
import MenuScreen from './pages/MenuScreen';
import GameScreen from './pages/GameScreen';
import EndScreen  from './pages/EndScreen';
import './index.css';
export default function App() {
  const tela = useGameStore(s => s.tela);
  if (tela === 'jogo') return <GameScreen />;
  if (tela === 'fim')  return <EndScreen />;
  return <MenuScreen />;
}
