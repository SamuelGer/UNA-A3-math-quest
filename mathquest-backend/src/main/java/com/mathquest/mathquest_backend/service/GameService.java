package com.mathquest.mathquest_backend.service;

import com.mathquest.mathquest_backend.domain.*;
import com.mathquest.mathquest_backend.dto.*;
import com.mathquest.mathquest_backend.repository.BoardSquareRepository;
import com.mathquest.mathquest_backend.repository.GameRepository;
import com.mathquest.mathquest_backend.repository.PlayerRepository;
import com.mathquest.mathquest_backend.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import static com.mathquest.mathquest_backend.domain.SquareType.DESAFIO;

@Service
@Transactional
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final BoardSquareRepository boardSquareRepository;
    private final QuestionRepository questionRepository;
    private static final int TOTAL_CASAS = 30;
    private static final int MIN_BONUS = 3;
    private static final int MAX_BONUS = 6;
    private static final int MIN_ARMADILHAS = 2;
    private static final int MAX_ARMADILHAS = 5;
    @Autowired
    public GameService(GameRepository gameRepository, PlayerRepository playerRepository, BoardSquareRepository boardSquareRepository, QuestionRepository questionRepository) {
        this.gameRepository = gameRepository;
        this.questionRepository = questionRepository;
        this.boardSquareRepository = boardSquareRepository;
        this.playerRepository = playerRepository;
    }

    public GameStateDTO criarPartida(String nomeJogador){
    // A partida se inicia quando o player escreve o nome dele.
    Player player = new Player();
    player.setNome(nomeJogador);
    player.setPontos(0);
    player.setPosicaoAtual(1);
    player.setDicasDisponiveis(0);
    //Hábito -> depois do save o objeto recebe o id gerado pelo banco, e ira precisar do id mais pra frente.
    player = playerRepository.save(player);
    //Partida iniciada
    Game game = new Game();
    game.setJogadores(List.of(player));
    game.setStatus(GameStatus.AGUARDANDO);
    game.setQuestaoId(new ArrayList<>());
    //Hábito -> depois do save o objeto recebe o id gerado pelo banco, e ira precisar do id mais pra frente.
    game = gameRepository.save(game);
    //Sorteando quantas casas bonus e armadilhas terá a instância atual do jogo.
        Random random = new Random();
        // MAX_BONUS - MIN_BONUS + 1 = 8 - 4 + 1 = 5
        // O random gera 0,1,2,3 ou 4, com o + MIN_BONUS(4), vira 4, 5 ,6, 7, 8.
        int quantidadeBonus = random.nextInt(MAX_BONUS - MIN_BONUS + 1)+ MIN_BONUS;

        // MAX_ARMADILHAS - MIN_ARMADILHAS + 1 = 8 - 4 + 1 = 5
        // O random gera 0,1,2 ou 3, com o + MIN_ARMADILHAS(3), vira 3, 4 ,5, 6, 7.
        int quantidadeArmadilhas = random.nextInt(MAX_ARMADILHAS - MIN_ARMADILHAS + 1) + MIN_ARMADILHAS;

        //A classe "Set" não permite que os números sejam duplicados, isso previne em todas as ocasiões, que ocorra uma armadilha na mesma casa que um bonus
        Set<Integer> posicoesBonus = new HashSet<>();
        Set<Integer> posicoesArmadilhas = new HashSet<>();
        //enquanto o set de bonus não tiver quantidadeBonus exata escolhida pela classe random:
        // vai sortear uma posição entre 2 e 29 (1 = inicio, 30 = fim)
        //adiciona no set
        while(posicoesBonus.size() < quantidadeBonus){
            posicoesBonus.add(random.nextInt(2,30));
        }

        //enquanto o set de armadilhas não tiver quantidadeArmadilhas suficiente definida pelo random:
        // vai sortear uma posição entre 2 e 29
        // SE não estiver no set de bonus, adiciona a posicao sorteada no set de armadilhas
        while(posicoesArmadilhas.size() < quantidadeArmadilhas){
            int posicaoSorteada = random.nextInt(2, 30);
            if(!posicoesBonus.contains(posicaoSorteada)){
                posicoesArmadilhas.add(posicaoSorteada);
            }
        }
        //Logica que vai gerar um quadrado do tabuleiro de cada tipo por índice
        for(int i = 1; i <= TOTAL_CASAS; i++){
            BoardSquare numeroCasa = new BoardSquare();
            SquareType tipo;
            if(i == 1){
                tipo = SquareType.INICIO;
            } else if (i == TOTAL_CASAS){
                tipo = SquareType.TESOURO;
            } else if (posicoesBonus.contains(i)){
                tipo = SquareType.BONUS;
            } else if (posicoesArmadilhas.contains(i)){
                tipo = SquareType.ARMADILHA;
            } else{
                tipo = DESAFIO;
            }
            numeroCasa.setTipo(tipo);
            numeroCasa.setGame(game);
            numeroCasa.setNumeroDeCasas(i);
            boardSquareRepository.save(numeroCasa);
        }
        // Atualiza o status do jogo para rodando (jogo em andamento)
        game.setStatus(GameStatus.ANDAMENTO);
        game.setUltimaAcao(LocalDateTime.now());
        //Hábito
        game = gameRepository.save(game);
        //Atualizar o DTO, que é o responsável pela comunicação de dados entre o back e o frontend
        GameStateDTO gameDTO = new GameStateDTO();
        gameDTO.setGameId(game.getId());
        gameDTO.setStatus(game.getStatus());
        gameDTO.setNomeJogadorAtual(nomeJogador);
        gameDTO.setPontos(player.getPontos());
        gameDTO.setPosicaoAtual(player.getPosicaoAtual());
        gameDTO.setDicasDisponiveis(player.getDicasDisponiveis());
        return gameDTO;
    }

    public GameStateDTO rolarDado (Long gameId){
        Game game = gameRepository.findById(gameId).
                orElseThrow(() -> new RuntimeException("Partida não encontrada!"));
        Random random = new Random();
        //Se o jogo está em andamento
        if(game.getStatus() == GameStatus.ANDAMENTO){
            //Se estiver, gera um número de 1 a 6 simulando um dado
            int numeroDeCasasAMover = random.nextInt(1,7);
            //Move o jogador utilizando o método moverJogador()
            return moverJogador(game.getId(), numeroDeCasasAMover);
        } else {
            throw new RuntimeException("Partida não está em andamento");
        }
    }

    public GameStateDTO moverJogador(Long gameId, int casasAMover){
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Partida não encontrada!"));
        //Verifica qual jogador está jogando
        Player playerAtual = game.getJogadores().get(game.getTurnoAtual());
        //Atualiza a posição dele no tabuleiro, utilizando o math.max para garantir-
        //que o jogador nunca volte a uma casa negativa. Ex: no case ARMADILHA o casasAMover recebe-
        //Um valor negativo, e isso poderia resultar em um valor negativo da posição do jogador no tabuleiro
        playerAtual.setPosicaoAtual(Math.max(1, playerAtual.getPosicaoAtual() + casasAMover));
        //Salva no banco de dados
        playerRepository.save(playerAtual);
        game.setUltimaAcao(LocalDateTime.now());
        game = gameRepository.save(game);
        //Retorna o processamento das casas pra verificar qual casa ele parou, passando o id da partida atual
        return processarCasa(game.getId());
    }

    public GameStateDTO processarCasa(Long gameId){
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Partida não encontrada!"));
        //Verifica o jogador que esta a jogar
        Player playerAtual = game.getJogadores().get(game.getTurnoAtual());
        //Pega o número/índice da casa para verificar o tipo de casa que ele caiu
        BoardSquare tabela = boardSquareRepository.findByNumeroDeCasasAndGame(playerAtual.getPosicaoAtual(), game)
                .orElseThrow(() -> new RuntimeException("Casa não encontrada!"));
        GameStateDTO gameDTO = new GameStateDTO();
        Random random = new Random();
        //Switch case pra cada tipo de casa.
        switch (tabela.getTipo()){
            case INICIO -> {
                gameDTO.setGameId(game.getId());
                gameDTO.setPontos(playerAtual.getPontos());
                gameDTO.setNomeJogadorAtual(playerAtual.getNome());
                gameDTO.setPosicaoAtual(playerAtual.getPosicaoAtual());
                gameDTO.setStatus(game.getStatus());
            }
            case DESAFIO -> {
                //Acha todas as questões e sorteia uma para cada casa que o jogador estiver
                List<Question> questoes = questionRepository.findByIdNotIn(game.getQuestaoId());
                int desafioSorteado = random.nextInt(0, questoes.size());
                Question question = questoes.get(desafioSorteado);
                game.getQuestaoId().add(question.getId());
                game = gameRepository.save(game);
                QuestionDTO questionDTO = embaralharQuestao(question);
                gameDTO.setGameId(game.getId());
                gameDTO.setPontos(playerAtual.getPontos());
                gameDTO.setNomeJogadorAtual(playerAtual.getNome());
                gameDTO.setPosicaoAtual(playerAtual.getPosicaoAtual());
                gameDTO.setStatus(game.getStatus());
                gameDTO.setDicasDisponiveis(playerAtual.getDicasDisponiveis());
                gameDTO.setTipoDaCasaAtual(tabela.getTipo()); //Automaticamente será do tipo DESAFIO
                gameDTO.setQuestaoAtual(questionDTO);
            }
            case BONUS -> {
                //Acumula uma dica para ser usada quando o jogador sentir que for necessário
                playerAtual.setDicasDisponiveis(playerAtual.getDicasDisponiveis() + 1);
                playerRepository.save(playerAtual);
                gameDTO.setGameId(game.getId());
                gameDTO.setPontos(playerAtual.getPontos());
                gameDTO.setNomeJogadorAtual(playerAtual.getNome());
                gameDTO.setPosicaoAtual(playerAtual.getPosicaoAtual());
                gameDTO.setStatus(game.getStatus());
                gameDTO.setDicasDisponiveis(playerAtual.getDicasDisponiveis());
                gameDTO.setTipoDaCasaAtual(tabela.getTipo());
            }
            case ARMADILHA -> {
                // Tem que receber uma pergunta mais difícil do que as convencionais do jogo
                // se ele errar, o dado roda e ele volta o número de casas que cair no dado
                List<Question> questaoDificil = questionRepository.findByDifficultyAndIdNotIn(QuestionDificulty.DIFICIL, game.getQuestaoId());
                int questaoSorteada = random.nextInt(0, questaoDificil.size());
                Question question = questaoDificil.get(questaoSorteada);
                game.getQuestaoId().add(question.getId());
                game = gameRepository.save(game);
                QuestionDTO questionDTO = embaralharQuestao(question);
                gameDTO.setGameId(game.getId());
                gameDTO.setPontos(playerAtual.getPontos());
                gameDTO.setNomeJogadorAtual(playerAtual.getNome());
                gameDTO.setPosicaoAtual(playerAtual.getPosicaoAtual());
                gameDTO.setStatus(game.getStatus());
                gameDTO.setDicasDisponiveis(playerAtual.getDicasDisponiveis());
                gameDTO.setTipoDaCasaAtual(tabela.getTipo()); //Automaticamente será do tipo ARMADILHA
                gameDTO.setQuestaoAtual(questionDTO);
            }
            case TESOURO -> {
                //Fim do jogo
                game.setStatus(GameStatus.FIM);
                gameDTO.setMensagem(playerAtual.getNome() + " venceu! Com: " + playerAtual.getPontos() + " Pontos.");
                game = gameRepository.save(game);
                gameDTO.setGameId(game.getId());
                gameDTO.setPontos(playerAtual.getPontos());
                gameDTO.setNomeJogadorAtual(playerAtual.getNome());
                gameDTO.setPosicaoAtual(playerAtual.getPosicaoAtual());
                gameDTO.setStatus(game.getStatus());
                finalizarPartida(game.getId());
            }
        }
        return gameDTO;
    }

    public GameStateDTO responderQuestao(AnswerDTO answerDTO){
        Game game = gameRepository.findById(answerDTO.getGameId())
                .orElseThrow(() -> new RuntimeException("Partida não encontrada!"));
        Random random = new Random();
        GameStateDTO gameDTO = new GameStateDTO();
        Player playerAtual = game.getJogadores().get(game.getTurnoAtual());
        Question question = questionRepository.findById(answerDTO.getQuestaoId()).
                orElseThrow(() -> new RuntimeException("Questão não encontrada!"));
        BoardSquare tabela = boardSquareRepository.findByNumeroDeCasasAndGame(playerAtual.getPosicaoAtual(), game)
                .orElseThrow(() -> new RuntimeException("Casa não encontrada!"));

                String respostaCorreta = question.getOpcoes().get(question.getIndiceCorreto());
                //Se a resposta for a mesma que o indice correto da questão
                if(respostaCorreta.equals(answerDTO.getRespostaSelecionada())){
                    //Se estiver certa, veririfica o tipo da questão
                    switch (question.getCategoria()){
                        case BOOLEANA, BASES -> {
                            playerAtual.setPontos(playerAtual.getPontos() + 7);
                        }
                        case FUNCOES, POTENCIACAO -> {
                            playerAtual.setPontos(playerAtual.getPontos() + 9);
                        }
                        case CONTAGEM -> {
                            playerAtual.setPontos(playerAtual.getPontos() + 11);
                        }
                    }
                    gameDTO.setAcertou(true);
                    // Salva o status do player no BD
                    playerRepository.save(playerAtual);
                    //Troca o turno da jogada
                    proximoTurno(answerDTO.getGameId());
                    //Se errar, verifica se ele caiu numa armadilha ou num desafio comum, para diferenciar o tipo de punição
                } else{
                    switch (tabela.getTipo()){
                        case DESAFIO -> {
                            //Subtrai 3 pontos por erro. E impede que o jogador fique com pontos negativos
                            //Nos primeiros testes do jogo, a pontuação acumulava abaixo de 0
                            playerAtual.setPontos(Math.max(0, playerAtual.getPontos() - 3));
                            playerRepository.save(playerAtual);
                            gameDTO.setAcertou(false);
                            proximoTurno(answerDTO.getGameId());
                        }
                        case ARMADILHA -> {
                            //Numero aleatório simulando o dado
                            int movimento = random.nextInt(1,7);
                            //Mais uma garantia de que o player esteja casas acima de 1 para subtrair as casas
                            if(playerAtual.getPosicaoAtual() > 1){
                                //movimenta o jogador para trás conforme o número que cair no dado. DICA MUNDIAL para negativar um número (movimento * -1)
                                GameStateDTO resultado = moverJogador(answerDTO.getGameId(),  movimento * -1);
                                resultado.setAcertou(false);
                                //Subtrai 3 pontos por erro. E impede que o jogador fique com pontos negativos
                                playerAtual.setPontos(Math.max(0, playerAtual.getPontos() - 3));
                                playerRepository.save(playerAtual);
                                resultado.setPontos(playerAtual.getPontos());

                                QuestionDTO questionDTO = new QuestionDTO();
                                questionDTO.setExplicacao(question.getExplicacao());
                                resultado.setQuestaoAtual(questionDTO);
                                proximoTurno(answerDTO.getGameId());
                                return resultado;
                            }
                        }
                    }
                    gameDTO.setAcertou(false);
                }
            QuestionDTO questionDTO = new QuestionDTO();
            questionDTO.setExplicacao(question.getExplicacao());
            gameDTO.setQuestaoAtual(questionDTO);
            gameDTO.setGameId(answerDTO.getGameId());
            gameDTO.setStatus(game.getStatus());
            gameDTO.setNomeJogadorAtual(playerAtual.getNome());
            gameDTO.setPontos(playerAtual.getPontos());
            gameDTO.setPosicaoAtual(playerAtual.getPosicaoAtual());
            gameDTO.setTipoDaCasaAtual(tabela.getTipo());
        return gameDTO;
    }

    public GameStateDTO proximoTurno(Long gameId){
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Partida não encontrada!"));
        //Pega a lista de jogadores
        List<Player> totalJogadores = game.getJogadores();
        //Passa para o proximo jogador usando o resto da Divisão
        //Se chegar ao limite de jogadores ele volta para o primeiro indice
        game.setTurnoAtual((game.getTurnoAtual() + 1) % totalJogadores.size());
        //variavel pra receber o proximo jogador
        Player proximoJogador = game.getJogadores().get(game.getTurnoAtual());
        game = gameRepository.save(game);
        GameStateDTO gameDTO = new GameStateDTO();
        gameDTO.setGameId(gameId);
        gameDTO.setStatus(game.getStatus());
        gameDTO.setNomeJogadorAtual(proximoJogador.getNome());
        gameDTO.setPontos(proximoJogador.getPontos());
        gameDTO.setPosicaoAtual(proximoJogador.getPosicaoAtual());
        gameDTO.setDicasDisponiveis(proximoJogador.getDicasDisponiveis());

        return gameDTO;
    }

    public List<BoardSquareDTO> buscarTabuleiro(Long gameId){
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Partida não encontrada!"));
        return boardSquareRepository.findByGame(game)
                .stream()
                .map(casa -> {
                    BoardSquareDTO dto = new BoardSquareDTO();
                    dto.setNumeroCasa(casa.getNumeroDeCasas());
                    dto.setTipo(casa.getTipo());
                    return dto;
                }).toList();
    }

    private QuestionDTO embaralharQuestao(Question question){
        //Lista pegando as opções
        List<String> opcoes = new ArrayList<>(question.getOpcoes());
        //Retirando o índice correto ANTES do embaralhamento
        String respostaCorreta = opcoes.get(question.getIndiceCorreto());
        //Embaralhando as questões
        Collections.shuffle(opcoes);
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setPergunta(question.getPergunta());
        dto.setOpcoes(opcoes);
        dto.setIndiceCorreto(opcoes.indexOf(respostaCorreta));
        dto.setCategoria(question.getCategoria());
        dto.setDica(question.getDica());
        return dto;
    }

    public void finalizarPartida(Long gameId){
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Partida não encontrada!"));
        boardSquareRepository.deleteAll(boardSquareRepository.findByGame(game));
        if(game.getJogadores() != null){
            for(Player player : game.getJogadores()){
                playerRepository.delete(player);
            }
        }
        gameRepository.deleteById(game.getId());
    }

    public List<Game> buscarPartidasAtivas(){
        return gameRepository.findByStatus(GameStatus.ANDAMENTO);
    }

    public GameStateDTO usarDica(Long gameId, Long questaoId){
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Partida não encontrada!"));

        Player playerAtual = game.getJogadores().get(game.getTurnoAtual());
        Question questao = questionRepository.findById(questaoId)
                .orElseThrow(() -> new RuntimeException("Questão não encontrada!"));
        GameStateDTO gameDTO = new GameStateDTO();
        gameDTO.setGameId(gameId);
        gameDTO.setStatus(game.getStatus());
        gameDTO.setNomeJogadorAtual(playerAtual.getNome());
        gameDTO.setPosicaoAtual(playerAtual.getPosicaoAtual());
        gameDTO.setPontos(playerAtual.getPontos());

        if(playerAtual.getDicasDisponiveis() >= 1){
            playerAtual.setDicasDisponiveis(playerAtual.getDicasDisponiveis() - 1);
            playerRepository.save(playerAtual);
            gameDTO.setDicasDisponiveis(playerAtual.getDicasDisponiveis());
        } else {
            gameDTO.setDicasDisponiveis(0);
        }
        gameDTO.setDica(questao.getDica());
        return gameDTO;
    }
}
