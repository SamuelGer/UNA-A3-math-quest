import axios from 'axios';
const BASE = 'https://mathquest-24qk.onrender.com/api/game';
export const apiCriarPartida    = (nomes)             => axios.post(`${BASE}/start`, nomes);
export const apiRolarDado        = (gameId)            => axios.put(`${BASE}/rolar/${gameId}`);
export const apiResponder        = (dto)               => axios.put(`${BASE}/responder`, dto);
export const apiBuscarTabuleiro  = (gameId)            => axios.get(`${BASE}/buscar-tabuleiro?gameId=${gameId}`);
export const apiUsarDica         = (gameId, questaoId) => axios.put(`${BASE}/usar-dica/${gameId}?questaoId=${questaoId}`);

