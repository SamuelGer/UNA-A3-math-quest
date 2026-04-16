import axios from 'axios';

const BASE = 'http://localhost:8080/api/game';

export const apiCriarPartida   = (nome)    => axios.post(`${BASE}/start?nomeJogador=${encodeURIComponent(nome)}`);
export const apiRolarDado       = (gameId)  => axios.post(`${BASE}/rolar/${gameId}`);
export const apiResponder       = (dto)     => axios.post(`${BASE}/responder`, dto);
export const apiBuscarTabuleiro = (gameId)  => axios.get(`${BASE}/buscar-tabuleiro?gameId=${gameId}`);
export const apiUsarDica        = (gameId, questaoId) =>
  axios.post(`${BASE}/usar-dica/${gameId}?questaoId=${questaoId}`);
