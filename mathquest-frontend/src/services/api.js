import axios from 'axios';
const BASE = 'http://localhost:8080/api/game';
export const apiCriarPartida    = (nomes)             => axios.post(`${BASE}/start`, nomes);
export const apiRolarDado        = (gameId)            => axios.post(`${BASE}/rolar/${gameId}`);
export const apiResponder        = (dto)               => axios.post(`${BASE}/responder`, dto);
export const apiBuscarTabuleiro  = (gameId)            => axios.get(`${BASE}/buscar-tabuleiro?gameId=${gameId}`);
export const apiUsarDica         = (gameId, questaoId) => axios.post(`${BASE}/usar-dica/${gameId}?questaoId=${questaoId}`);
export const apiProximoTurno     = (gameId)            => axios.post(`${BASE}/proximo-turno/${gameId}`);
