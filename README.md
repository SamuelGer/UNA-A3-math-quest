# MathQuest — Caça ao Tesouro

> Jogo educacional de tabuleiro digital multiplayer para o aprendizado de Matemática Computacional.

🎮 **[Acessar o projeto](https://mathquest-nu.vercel.app/)**

---

## Sobre o Projeto

O MathQuest é um jogo de tabuleiro digital desenvolvido como projeto acadêmico para a disciplina de **Matemática Computacional Aplicada** do curso de **Análise e Desenvolvimento de Sistemas** da **UNA — Cristiano Machado**.

O objetivo é estimular o raciocínio lógico e o aprendizado de operações matemáticas de forma interativa e lúdica, transformando conteúdos como álgebra booleana, funções, bases numéricas, potenciação e contagem em desafios dentro de um jogo multiplayer em tempo real.

---

## Regras do Jogo

- Suporta até **4 jogadores** simultâneos
- Cada jogador rola o dado na sua vez e avança no tabuleiro de **30 casas**
- As casas são geradas dinamicamente a cada partida e podem ser:
  - 🏁 **Início** — casa de partida
  - ❓ **Desafio** — responda uma questão matemática em até 45s para ganhar pontos
  - ⭐ **Bônus** — ganhe uma dica para usar em questões futuras
  - 💀 **Armadilha** — se errar a questão, você volta casas
  - 🏆 **Tesouro** — casa final, vence quem chegar com mais pontos

### Pontuação por categoria
| Categoria | Pontos |
|---|---|
| Booleana / Bases | 7 pts |
| Funções / Potenciação | 9 pts |
| Contagem | 11 pts |

---

## Especificações Técnicas

### Back-end
| Tecnologia | Uso |
|---|---|
| Java 21 | Linguagem principal |
| Spring Boot 4 | Framework da API REST |
| Spring WebSocket + STOMP | Comunicação em tempo real entre jogadores |
| Spring Data JPA + Hibernate | ORM para persistência de dados |
| MySQL 8 | Banco de dados relacional |
| Lombok | Redução de boilerplate nas entidades |
| Docker | Conteinerização da aplicação com Dockerfile otimizado para simplificar o processo de build, distribuição e deploy do servidor. |

### Front-end
| Tecnologia | Uso |
|---|---|
| React 18 | Interface do jogo e componentes visuais |
| Zustand | Gerenciamento global de estado do jogo |
| Framer Motion | Animações dos peões e transições de UI |
| Axios | Comunicação HTTP com a API REST |
| Google M3 | Layout adaptativo por proporção de tela |

### Entidades do Banco de Dados
- **partidas** — sessão de jogo (status, turno atual, data)
- **player** — jogador (nome, pontos, posição, dicas disponíveis)
- **tabuleiro** — casas da partida (número, tipo)
- **questoes** — banco de perguntas (pergunta, opções, categoria, dificuldade, dica, explicação)
- **partidas_jogadores** — relação N:N entre partidas e jogadores
- **game_questao_id** — controle de questões já usadas por partida

---
## O que foi aprendido?
Foram adquiridas práticas de desenvolvimento WEB, como: Objetos DTO (Data Transfer Object), Estrutura CORS(Cross-Origin Resource Sharing) e o método CorsFilter, Métodos HTTP (GET, POST, PUT, PATCH, DELETE, OPTIONS), Comunicação de API's reais e endpoints, Conteinerização da aplicação e deploy. 

## Instituição

**UNA — Cristiano Machado**  
Curso: Análise e Desenvolvimento de Sistemas  
Disciplina: Matemática Computacional Aplicada  
Período: 2026/1
