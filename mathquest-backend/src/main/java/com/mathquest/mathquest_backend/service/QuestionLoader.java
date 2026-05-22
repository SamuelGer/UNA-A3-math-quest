package com.mathquest.mathquest_backend.service;

import com.mathquest.mathquest_backend.domain.Question;
import com.mathquest.mathquest_backend.domain.QuestionCategory;
import com.mathquest.mathquest_backend.domain.QuestionDifficulty;
import com.mathquest.mathquest_backend.repository.QuestionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DependsOn("entityManagerFactory")
public class QuestionLoader {

    QuestionRepository questionRepository;

    public QuestionLoader(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @PostConstruct
    public void carregarQuestoes() {
        if (questionRepository.count() == 0) {

            // ═══════════════════════════════════════════
            // ÁLGEBRA BOOLEANA — 15 questões
            // ═══════════════════════════════════════════

            Question q1 = new Question();
            q1.setPergunta("Qual o resultado de: (1 AND 0) OR (NOT 1)?");
            q1.setOpcoes(List.of("0", "1", "2", "Indefinido"));
            q1.setIndiceCorreto(0);
            q1.setCategoria(QuestionCategory.BOOLEANA);
            q1.setDifficulty(QuestionDifficulty.FACIL);
            q1.setDica("Resolva cada parte separadamente: primeiro AND, depois NOT, por fim OR.");
            q1.setExplicacao("1 AND 0 = 0; NOT 1 = 0; 0 OR 0 = 0.");
            questionRepository.save(q1);

            Question q2 = new Question();
            q2.setPergunta("Complete a tabela verdade de A XOR B para A=1, B=1");
            q2.setOpcoes(List.of("0", "1", "A", "B"));
            q2.setIndiceCorreto(0);
            q2.setCategoria(QuestionCategory.BOOLEANA);
            q2.setDifficulty(QuestionDifficulty.FACIL);
            q2.setDica("XOR retorna 1 somente quando as entradas são DIFERENTES.");
            q2.setExplicacao("XOR retorna 1 somente quando as entradas são diferentes. 1 XOR 1 = 0.");
            questionRepository.save(q2);

            Question q3 = new Question();
            q3.setPergunta("Simplifique: NOT(A AND B)");
            q3.setOpcoes(List.of("NOT A OR NOT B", "NOT A AND NOT B", "A OR B", "A AND B"));
            q3.setIndiceCorreto(0);
            q3.setCategoria(QuestionCategory.BOOLEANA);
            q3.setDifficulty(QuestionDifficulty.MEDIA);
            q3.setDica("Use a Lei de De Morgan: a negação de AND vira OR com cada termo negado.");
            q3.setExplicacao("Pela Lei de De Morgan: NOT(A AND B) = NOT A OR NOT B.");
            questionRepository.save(q3);

            Question q4 = new Question();
            q4.setPergunta("Qual porta lógica retorna 1 apenas quando ambas as entradas são 0?");
            q4.setOpcoes(List.of("NOR", "NAND", "AND", "XOR"));
            q4.setIndiceCorreto(0);
            q4.setCategoria(QuestionCategory.BOOLEANA);
            q4.setDifficulty(QuestionDifficulty.MEDIA);
            q4.setDica("Pense em qual porta é o inverso de OR — ela nega o resultado do OR.");
            q4.setExplicacao("A porta NOR retorna 1 somente quando todas as entradas são 0.");
            questionRepository.save(q4);

            Question q5 = new Question();
            q5.setPergunta("Qual o resultado de: (1 OR 0) AND (NOT 0)?");
            q5.setOpcoes(List.of("1", "0", "Indefinido", "2"));
            q5.setIndiceCorreto(0);
            q5.setCategoria(QuestionCategory.BOOLEANA);
            q5.setDifficulty(QuestionDifficulty.FACIL);
            q5.setDica("NOT 0 = 1. OR retorna 1 se pelo menos uma entrada for 1.");
            q5.setExplicacao("1 OR 0 = 1; NOT 0 = 1; 1 AND 1 = 1.");
            questionRepository.save(q5);

            Question q6 = new Question();
            q6.setPergunta("A expressão A AND (A OR B) é equivalente a?");
            q6.setOpcoes(List.of("A", "B", "A OR B", "A AND B"));
            q6.setIndiceCorreto(0);
            q6.setCategoria(QuestionCategory.BOOLEANA);
            q6.setDifficulty(QuestionDifficulty.MEDIA);
            q6.setDica("Use a Lei de Absorção: quando A aparece em ambos os lados, o OR é absorvido.");
            q6.setExplicacao("Pela Lei de Absorção: A AND (A OR B) = A.");
            questionRepository.save(q6);

            Question q7 = new Question();
            q7.setPergunta("Qual o resultado de NOT(0 OR 0)?");
            q7.setOpcoes(List.of("1", "0", "Indefinido", "NOT 0"));
            q7.setIndiceCorreto(0);
            q7.setCategoria(QuestionCategory.BOOLEANA);
            q7.setDifficulty(QuestionDifficulty.FACIL);
            q7.setDica("Primeiro resolva o OR dentro dos parênteses, depois aplique o NOT.");
            q7.setExplicacao("0 OR 0 = 0; NOT 0 = 1.");
            questionRepository.save(q7);

            Question q8 = new Question();
            q8.setPergunta("Qual o resultado de 1 NAND 1?");
            q8.setOpcoes(List.of("0", "1", "Indefinido", "2"));
            q8.setIndiceCorreto(0);
            q8.setCategoria(QuestionCategory.BOOLEANA);
            q8.setDifficulty(QuestionDifficulty.MEDIA);
            q8.setDica("NAND é NOT(AND). Calcule AND primeiro, depois aplique NOT.");
            q8.setExplicacao("NAND é NOT(AND). 1 AND 1 = 1; NOT 1 = 0.");
            questionRepository.save(q8);

            Question q9 = new Question();
            q9.setPergunta("Qual o valor de NOT(NOT A)?");
            q9.setOpcoes(List.of("A", "NOT A", "0", "1"));
            q9.setIndiceCorreto(0);
            q9.setCategoria(QuestionCategory.BOOLEANA);
            q9.setDifficulty(QuestionDifficulty.FACIL);
            q9.setDica("Aplicar NOT duas vezes é como não aplicar nenhuma negação.");
            q9.setExplicacao("Dupla negação: NOT(NOT A) = A.");
            questionRepository.save(q9);

            Question q10 = new Question();
            q10.setPergunta("Qual o resultado de (A OR NOT A)?");
            q10.setOpcoes(List.of("1", "0", "A", "NOT A"));
            q10.setIndiceCorreto(0);
            q10.setCategoria(QuestionCategory.BOOLEANA);
            q10.setDifficulty(QuestionDifficulty.FACIL);
            q10.setDica("Um valor OR com seu próprio complemento sempre resulta em verdadeiro.");
            q10.setExplicacao("Pela Lei do Complemento: A OR NOT A = 1 sempre.");
            questionRepository.save(q10);

            Question q11 = new Question();
            q11.setPergunta("Qual o resultado de (A AND NOT A)?");
            q11.setOpcoes(List.of("0", "1", "A", "NOT A"));
            q11.setIndiceCorreto(0);
            q11.setCategoria(QuestionCategory.BOOLEANA);
            q11.setDifficulty(QuestionDifficulty.FACIL);
            q11.setDica("Um valor AND com seu próprio complemento sempre resulta em falso.");
            q11.setExplicacao("Pela Lei do Complemento: A AND NOT A = 0 sempre.");
            questionRepository.save(q11);

            Question q12 = new Question();
            q12.setPergunta("Simplifique: NOT(A OR B)");
            q12.setOpcoes(List.of("NOT A AND NOT B", "NOT A OR NOT B", "A AND B", "A OR B"));
            q12.setIndiceCorreto(0);
            q12.setCategoria(QuestionCategory.BOOLEANA);
            q12.setDifficulty(QuestionDifficulty.MEDIA);
            q12.setDica("Use a Lei de De Morgan: a negação de OR vira AND com cada termo negado.");
            q12.setExplicacao("Pela Lei de De Morgan: NOT(A OR B) = NOT A AND NOT B.");
            questionRepository.save(q12);

            Question q13 = new Question();
            q13.setPergunta("Qual o resultado de 0 XOR 1?");
            q13.setOpcoes(List.of("1", "0", "Indefinido", "2"));
            q13.setIndiceCorreto(0);
            q13.setCategoria(QuestionCategory.BOOLEANA);
            q13.setDifficulty(QuestionDifficulty.FACIL);
            q13.setDica("XOR retorna 1 quando as entradas são diferentes entre si.");
            q13.setExplicacao("XOR retorna 1 quando as entradas são diferentes. 0 XOR 1 = 1.");
            questionRepository.save(q13);

            Question q14 = new Question();
            q14.setPergunta("A expressão A OR (A AND B) é equivalente a?");
            q14.setOpcoes(List.of("A", "B", "A AND B", "A OR B"));
            q14.setIndiceCorreto(0);
            q14.setCategoria(QuestionCategory.BOOLEANA);
            q14.setDifficulty(QuestionDifficulty.MEDIA);
            q14.setDica("Use a Lei de Absorção: quando A aparece em ambos os lados, o AND é absorvido.");
            q14.setExplicacao("Pela Lei de Absorção: A OR (A AND B) = A.");
            questionRepository.save(q14);

            Question q15 = new Question();
            q15.setPergunta("Qual porta lógica retorna 1 apenas quando todas as entradas são 1?");
            q15.setOpcoes(List.of("AND", "OR", "NAND", "XOR"));
            q15.setIndiceCorreto(0);
            q15.setCategoria(QuestionCategory.BOOLEANA);
            q15.setDifficulty(QuestionDifficulty.DIFICIL);
            q15.setDica("Pense na porta que exige que TODAS as entradas sejam verdadeiras.");
            q15.setExplicacao("A porta AND retorna 1 somente quando todas as entradas são 1.");
            questionRepository.save(q15);

            // ═══════════════════════════════════════════
            // FUNÇÕES MATEMÁTICAS — 15 questões
            // ═══════════════════════════════════════════

            Question q16 = new Question();
            q16.setPergunta("Se f(x) = 2x + 3, qual é f(4)?");
            q16.setOpcoes(List.of("11", "8", "14", "10"));
            q16.setIndiceCorreto(0);
            q16.setCategoria(QuestionCategory.FUNCOES);
            q16.setDifficulty(QuestionDifficulty.FACIL);
            q16.setDica("Substitua x por 4 na expressão e calcule passo a passo.");
            q16.setExplicacao("f(4) = 2(4) + 3 = 8 + 3 = 11.");
            questionRepository.save(q16);

            Question q17 = new Question();
            q17.setPergunta("Dada f(x) = x² e g(x) = x + 1, qual é f(g(2))?");
            q17.setOpcoes(List.of("9", "5", "8", "4"));
            q17.setIndiceCorreto(0);
            q17.setCategoria(QuestionCategory.FUNCOES);
            q17.setDifficulty(QuestionDifficulty.MEDIA);
            q17.setDica("Composição: calcule g(2) primeiro, depois aplique f ao resultado.");
            q17.setExplicacao("g(2) = 2 + 1 = 3; f(3) = 3² = 9.");
            questionRepository.save(q17);

            Question q18 = new Question();
            q18.setPergunta("Qual é o domínio de f(x) = √(x - 2)?");
            q18.setOpcoes(List.of("x ≥ 2", "x > 2", "x ≥ 0", "Todos os reais"));
            q18.setIndiceCorreto(0);
            q18.setCategoria(QuestionCategory.FUNCOES);
            q18.setDifficulty(QuestionDifficulty.MEDIA);
            q18.setDica("Para a raiz quadrada ser definida, o valor dentro dela não pode ser negativo.");
            q18.setExplicacao("Para a raiz ser definida: x - 2 ≥ 0, ou seja, x ≥ 2.");
            questionRepository.save(q18);

            Question q19 = new Question();
            q19.setPergunta("f(x) = 3x - 6. Para qual valor de x temos f(x) = 0?");
            q19.setOpcoes(List.of("x = 2", "x = 3", "x = -2", "x = 6"));
            q19.setIndiceCorreto(0);
            q19.setCategoria(QuestionCategory.FUNCOES);
            q19.setDifficulty(QuestionDifficulty.FACIL);
            q19.setDica("Iguale a função a zero e isole o x: 3x - 6 = 0.");
            q19.setExplicacao("3x - 6 = 0 → 3x = 6 → x = 2.");
            questionRepository.save(q19);

            Question q20 = new Question();
            q20.setPergunta("Se f(x) = x² - 4, qual é f(-3)?");
            q20.setOpcoes(List.of("5", "13", "-13", "-5"));
            q20.setIndiceCorreto(0);
            q20.setCategoria(QuestionCategory.FUNCOES);
            q20.setDifficulty(QuestionDifficulty.FACIL);
            q20.setDica("(-3)² = 9. Cuidado com o sinal ao elevar número negativo ao quadrado.");
            q20.setExplicacao("f(-3) = (-3)² - 4 = 9 - 4 = 5.");
            questionRepository.save(q20);

            Question q21 = new Question();
            q21.setPergunta("f(x) = 2x e g(x) = x + 3. Qual é (f ∘ g)(1)?");
            q21.setOpcoes(List.of("8", "5", "6", "10"));
            q21.setIndiceCorreto(0);
            q21.setCategoria(QuestionCategory.FUNCOES);
            q21.setDifficulty(QuestionDifficulty.MEDIA);
            q21.setDica("(f ∘ g)(1) significa f(g(1)). Calcule g(1) primeiro.");
            q21.setExplicacao("g(1) = 1 + 3 = 4; f(4) = 2(4) = 8.");
            questionRepository.save(q21);

            Question q22 = new Question();
            q22.setPergunta("Se f(x) = |x - 5|, qual é f(2)?");
            q22.setOpcoes(List.of("3", "-3", "7", "2"));
            q22.setIndiceCorreto(0);
            q22.setCategoria(QuestionCategory.FUNCOES);
            q22.setDifficulty(QuestionDifficulty.FACIL);
            q22.setDica("O valor absoluto |x| sempre retorna um número positivo ou zero.");
            q22.setExplicacao("f(2) = |2 - 5| = |-3| = 3.");
            questionRepository.save(q22);

            Question q23 = new Question();
            q23.setPergunta("Qual é a inversa de f(x) = 2x + 4?");
            q23.setOpcoes(List.of("f⁻¹(x) = (x - 4)/2", "f⁻¹(x) = 2x - 4", "f⁻¹(x) = x/2 + 4", "f⁻¹(x) = (x + 4)/2"));
            q23.setIndiceCorreto(0);
            q23.setCategoria(QuestionCategory.FUNCOES);
            q23.setDifficulty(QuestionDifficulty.DIFICIL);
            q23.setDica("Para encontrar a inversa, substitua f(x) por y, troque x e y, e isole y.");
            q23.setExplicacao("y = 2x + 4 → x = (y - 4)/2. Logo f⁻¹(x) = (x - 4)/2.");
            questionRepository.save(q23);

            Question q24 = new Question();
            q24.setPergunta("Se f(x) = x³, qual é f(2)?");
            q24.setOpcoes(List.of("8", "6", "4", "16"));
            q24.setIndiceCorreto(0);
            q24.setCategoria(QuestionCategory.FUNCOES);
            q24.setDifficulty(QuestionDifficulty.FACIL);
            q24.setDica("x³ significa x multiplicado por si mesmo três vezes.");
            q24.setExplicacao("f(2) = 2³ = 2 × 2 × 2 = 8.");
            questionRepository.save(q24);

            Question q25 = new Question();
            q25.setPergunta("Dada f(x) = 1/x, qual é o domínio de f?");
            q25.setOpcoes(List.of("x ≠ 0", "x > 0", "x ≥ 0", "Todos os reais"));
            q25.setIndiceCorreto(0);
            q25.setCategoria(QuestionCategory.FUNCOES);
            q25.setDifficulty(QuestionDifficulty.MEDIA);
            q25.setDica("Pense: qual valor de x tornaria a função indefinida? Divisão por zero!");
            q25.setExplicacao("Divisão por zero é indefinida, então x ≠ 0.");
            questionRepository.save(q25);

            Question q26 = new Question();
            q26.setPergunta("Se f(x) = 2x² - 3x + 1, qual é f(1)?");
            q26.setOpcoes(List.of("0", "1", "2", "-1"));
            q26.setIndiceCorreto(0);
            q26.setCategoria(QuestionCategory.FUNCOES);
            q26.setDifficulty(QuestionDifficulty.MEDIA);
            q26.setDica("Substitua x = 1 e calcule cada termo separadamente antes de somar.");
            q26.setExplicacao("f(1) = 2(1)² - 3(1) + 1 = 2 - 3 + 1 = 0.");
            questionRepository.save(q26);

            Question q27 = new Question();
            q27.setPergunta("Qual é f(0) se f(x) = 5x + 7?");
            q27.setOpcoes(List.of("7", "5", "0", "12"));
            q27.setIndiceCorreto(0);
            q27.setCategoria(QuestionCategory.FUNCOES);
            q27.setDifficulty(QuestionDifficulty.FACIL);
            q27.setDica("Qualquer número multiplicado por 0 é 0. Reste apenas a constante.");
            q27.setExplicacao("f(0) = 5(0) + 7 = 0 + 7 = 7.");
            questionRepository.save(q27);

            Question q28 = new Question();
            q28.setPergunta("Se f(x) = x² + 2x, qual é f(-1)?");
            q28.setOpcoes(List.of("-1", "1", "0", "3"));
            q28.setIndiceCorreto(0);
            q28.setCategoria(QuestionCategory.FUNCOES);
            q28.setDifficulty(QuestionDifficulty.FACIL);
            q28.setDica("(-1)² = 1 e 2(-1) = -2. Some os resultados.");
            q28.setExplicacao("f(-1) = (-1)² + 2(-1) = 1 - 2 = -1.");
            questionRepository.save(q28);

            Question q29 = new Question();
            q29.setPergunta("Dada f(x) = 3x e g(x) = x², qual é g(f(2))?");
            q29.setOpcoes(List.of("36", "12", "18", "9"));
            q29.setIndiceCorreto(0);
            q29.setCategoria(QuestionCategory.FUNCOES);
            q29.setDifficulty(QuestionDifficulty.MEDIA);
            q29.setDica("Calcule f(2) primeiro, depois aplique g ao resultado obtido.");
            q29.setExplicacao("f(2) = 3×2 = 6; g(6) = 6² = 36.");
            questionRepository.save(q29);

            Question q30 = new Question();
            q30.setPergunta("Se f(x) = log₂(x), qual é f(8)?");
            q30.setOpcoes(List.of("3", "2", "4", "8"));
            q30.setIndiceCorreto(0);
            q30.setCategoria(QuestionCategory.FUNCOES);
            q30.setDifficulty(QuestionDifficulty.DIFICIL);
            q30.setDica("log₂(x) pergunta: 2 elevado a qual potência resulta em x?");
            q30.setExplicacao("log₂(8) = 3, pois 2³ = 8.");
            questionRepository.save(q30);

            // ═══════════════════════════════════════════
            // BASES NUMÉRICAS — 15 questões
            // ═══════════════════════════════════════════

            Question q31 = new Question();
            q31.setPergunta("Converta 1010₂ para decimal");
            q31.setOpcoes(List.of("10", "12", "8", "5"));
            q31.setIndiceCorreto(0);
            q31.setCategoria(QuestionCategory.BASES);
            q31.setDifficulty(QuestionDifficulty.FACIL);
            q31.setDica("Multiplique cada bit pela potência de 2 correspondente à sua posição (da direita para esquerda: 2⁰, 2¹, 2², 2³).");
            q31.setExplicacao("1×2³ + 0×2² + 1×2¹ + 0×2⁰ = 8 + 0 + 2 + 0 = 10.");
            questionRepository.save(q31);

            Question q32 = new Question();
            q32.setPergunta("Qual é 255 em hexadecimal?");
            q32.setOpcoes(List.of("FF", "FE", "1F", "F0"));
            q32.setIndiceCorreto(0);
            q32.setCategoria(QuestionCategory.BASES);
            q32.setDifficulty(QuestionDifficulty.MEDIA);
            q32.setDica("F em hexadecimal vale 15. Pense: qual combinação de dois dígitos hex resulta em 255?");
            q32.setExplicacao("255 ÷ 16 = 15 resto 15. F = 15, logo 255 = FF₁₆.");
            questionRepository.save(q32);

            Question q33 = new Question();
            q33.setPergunta("Some em binário: 1011 + 0101");
            q33.setOpcoes(List.of("10000", "1110", "10110", "1100"));
            q33.setIndiceCorreto(0);
            q33.setCategoria(QuestionCategory.BASES);
            q33.setDifficulty(QuestionDifficulty.MEDIA);
            q33.setDica("Some coluna por coluna da direita para esquerda. Em binário: 1+1=10 (zero e vai 1).");
            q33.setExplicacao("1011 (11) + 0101 (5) = 10000 (16).");
            questionRepository.save(q33);

            Question q34 = new Question();
            q34.setPergunta("Converta 8A₁₆ para decimal");
            q34.setOpcoes(List.of("138", "128", "140", "142"));
            q34.setIndiceCorreto(0);
            q34.setCategoria(QuestionCategory.BASES);
            q34.setDifficulty(QuestionDifficulty.MEDIA);
            q34.setDica("Em hexadecimal, A = 10. Multiplique cada dígito pela potência de 16 correspondente.");
            q34.setExplicacao("8×16¹ + A(10)×16⁰ = 128 + 10 = 138.");
            questionRepository.save(q34);

            Question q35 = new Question();
            q35.setPergunta("Qual é 47 em binário?");
            q35.setOpcoes(List.of("101111", "110001", "101110", "100111"));
            q35.setIndiceCorreto(0);
            q35.setCategoria(QuestionCategory.BASES);
            q35.setDifficulty(QuestionDifficulty.MEDIA);
            q35.setDica("Divida 47 por 2 repetidamente e leia os restos de baixo para cima.");
            q35.setExplicacao("47 = 32+8+4+2+1 = 101111₂.");
            questionRepository.save(q35);

            Question q36 = new Question();
            q36.setPergunta("Converta 37₈ para decimal");
            q36.setOpcoes(List.of("31", "37", "28", "33"));
            q36.setIndiceCorreto(0);
            q36.setCategoria(QuestionCategory.BASES);
            q36.setDifficulty(QuestionDifficulty.FACIL);
            q36.setDica("Multiplique cada dígito pela potência de 8 correspondente à sua posição.");
            q36.setExplicacao("3×8¹ + 7×8⁰ = 24 + 7 = 31.");
            questionRepository.save(q36);

            Question q37 = new Question();
            q37.setPergunta("Qual é 1100₂ em decimal?");
            q37.setOpcoes(List.of("12", "10", "14", "8"));
            q37.setIndiceCorreto(0);
            q37.setCategoria(QuestionCategory.BASES);
            q37.setDifficulty(QuestionDifficulty.FACIL);
            q37.setDica("Identifique as posições com bit 1 e some as potências de 2 correspondentes.");
            q37.setExplicacao("1×2³ + 1×2² + 0×2¹ + 0×2⁰ = 8 + 4 = 12.");
            questionRepository.save(q37);

            Question q38 = new Question();
            q38.setPergunta("Converta 15 para binário");
            q38.setOpcoes(List.of("1111", "1110", "10000", "1101"));
            q38.setIndiceCorreto(0);
            q38.setCategoria(QuestionCategory.BASES);
            q38.setDifficulty(QuestionDifficulty.FACIL);
            q38.setDica("15 = 8 + 4 + 2 + 1. Quais potências de 2 somam 15?");
            q38.setExplicacao("15 = 8+4+2+1 = 1111₂.");
            questionRepository.save(q38);

            Question q39 = new Question();
            q39.setPergunta("Qual é 2F₁₆ em decimal?");
            q39.setOpcoes(List.of("47", "31", "45", "50"));
            q39.setIndiceCorreto(0);
            q39.setCategoria(QuestionCategory.BASES);
            q39.setDifficulty(QuestionDifficulty.MEDIA);
            q39.setDica("F = 15 em hexadecimal. Multiplique cada dígito pela potência de 16.");
            q39.setExplicacao("2×16¹ + F(15)×16⁰ = 32 + 15 = 47.");
            questionRepository.save(q39);

            Question q40 = new Question();
            q40.setPergunta("Converta 100₂ para decimal");
            q40.setOpcoes(List.of("4", "2", "8", "6"));
            q40.setIndiceCorreto(0);
            q40.setCategoria(QuestionCategory.BASES);
            q40.setDifficulty(QuestionDifficulty.FACIL);
            q40.setDica("Apenas o bit mais à esquerda é 1. Qual potência de 2 ele representa?");
            q40.setExplicacao("1×2² + 0×2¹ + 0×2⁰ = 4.");
            questionRepository.save(q40);

            Question q41 = new Question();
            q41.setPergunta("Qual é 64 em octal?");
            q41.setOpcoes(List.of("100", "80", "77", "64"));
            q41.setIndiceCorreto(0);
            q41.setCategoria(QuestionCategory.BASES);
            q41.setDifficulty(QuestionDifficulty.MEDIA);
            q41.setDica("Divida 64 por 8 repetidamente e leia os restos de baixo para cima.");
            q41.setExplicacao("64 ÷ 8 = 8 resto 0; 8 ÷ 8 = 1 resto 0. Logo 64 = 100₈.");
            questionRepository.save(q41);

            Question q42 = new Question();
            q42.setPergunta("Converta 11001₂ para decimal");
            q42.setOpcoes(List.of("25", "19", "22", "27"));
            q42.setIndiceCorreto(0);
            q42.setCategoria(QuestionCategory.BASES);
            q42.setDifficulty(QuestionDifficulty.MEDIA);
            q42.setDica("Some as potências de 2 onde o bit é 1: posições 0, 3 e 4 da direita.");
            q42.setExplicacao("1×2⁴ + 1×2³ + 0×2² + 0×2¹ + 1×2⁰ = 16 + 8 + 1 = 25.");
            questionRepository.save(q42);

            Question q43 = new Question();
            q43.setPergunta("Qual é 200 em hexadecimal?");
            q43.setOpcoes(List.of("C8", "D0", "CA", "B8"));
            q43.setIndiceCorreto(0);
            q43.setCategoria(QuestionCategory.BASES);
            q43.setDifficulty(QuestionDifficulty.DIFICIL);
            q43.setDica("Divida 200 por 16. O quociente e o resto formam os dois dígitos hex. C = 12.");
            q43.setExplicacao("200 ÷ 16 = 12 resto 8. 12 = C, logo 200 = C8₁₆.");
            questionRepository.save(q43);

            Question q44 = new Question();
            q44.setPergunta("Some em binário: 111 + 1");
            q44.setOpcoes(List.of("1000", "110", "1010", "1001"));
            q44.setIndiceCorreto(0);
            q44.setCategoria(QuestionCategory.BASES);
            q44.setDifficulty(QuestionDifficulty.MEDIA);
            q44.setDica("111 em decimal é 7. Some 7 + 1 = 8. Qual é 8 em binário?");
            q44.setExplicacao("111 (7) + 1 (1) = 1000 (8).");
            questionRepository.save(q44);

            Question q45 = new Question();
            q45.setPergunta("Converta 52₈ para binário");
            q45.setOpcoes(List.of("101010", "110010", "101100", "100110"));
            q45.setIndiceCorreto(0);
            q45.setCategoria(QuestionCategory.BASES);
            q45.setDifficulty(QuestionDifficulty.DIFICIL);
            q45.setDica("Converta cada dígito octal separadamente para 3 bits binários: 5₈ = 101₂, 2₈ = 010₂.");
            q45.setExplicacao("5₈ = 101₂, 2₈ = 010₂. Logo 52₈ = 101010₂.");
            questionRepository.save(q45);

            // ═══════════════════════════════════════════
            // POTENCIAÇÃO — 10 questões
            // ═══════════════════════════════════════════

            Question q46 = new Question();
            q46.setPergunta("Qual é o valor de 2⁸?");
            q46.setOpcoes(List.of("256", "128", "512", "64"));
            q46.setIndiceCorreto(0);
            q46.setCategoria(QuestionCategory.POTENCIACAO);
            q46.setDifficulty(QuestionDifficulty.FACIL);
            q46.setDica("2⁸ representa 1 byte em computação — quantos valores diferentes 8 bits podem representar?");
            q46.setExplicacao("2⁸ = 2×2×2×2×2×2×2×2 = 256. Todo byte tem 256 combinações possíveis.");
            questionRepository.save(q46);

            Question q47 = new Question();
            q47.setPergunta("Qual é o resultado de log₂(64)?");
            q47.setOpcoes(List.of("6", "5", "4", "8"));
            q47.setIndiceCorreto(0);
            q47.setCategoria(QuestionCategory.POTENCIACAO);
            q47.setDifficulty(QuestionDifficulty.MEDIA);
            q47.setDica("log₂(x) pergunta: 2 elevado a qual potência dá x? Tente: 2¹, 2², 2³, 2⁴, 2⁵, 2⁶...");
            q47.setExplicacao("2⁶ = 64, logo log₂(64) = 6.");
            questionRepository.save(q47);

            Question q48 = new Question();
            q48.setPergunta("Simplifique: (2³ × 2⁴) / 2⁵");
            q48.setOpcoes(List.of("2²", "2⁶", "2³", "2¹²"));
            q48.setIndiceCorreto(0);
            q48.setCategoria(QuestionCategory.POTENCIACAO);
            q48.setDifficulty(QuestionDifficulty.MEDIA);
            q48.setDica("Multiplicação de mesma base: some os expoentes. Divisão de mesma base: subtraia os expoentes.");
            q48.setExplicacao("2³×2⁴ = 2⁷. Depois 2⁷/2⁵ = 2⁷⁻⁵ = 2².");
            questionRepository.save(q48);

            Question q49 = new Question();
            q49.setPergunta("Qual é o valor de 3⁰?");
            q49.setOpcoes(List.of("1", "0", "3", "Indefinido"));
            q49.setIndiceCorreto(0);
            q49.setCategoria(QuestionCategory.POTENCIACAO);
            q49.setDifficulty(QuestionDifficulty.FACIL);
            q49.setDica("Qualquer número (exceto zero) elevado a 0 segue uma regra especial.");
            q49.setExplicacao("Qualquer número elevado a 0 é igual a 1. Logo 3⁰ = 1.");
            questionRepository.save(q49);

            Question q50 = new Question();
            q50.setPergunta("Qual é o resultado de (2²)³?");
            q50.setOpcoes(List.of("64", "32", "16", "12"));
            q50.setIndiceCorreto(0);
            q50.setCategoria(QuestionCategory.POTENCIACAO);
            q50.setDifficulty(QuestionDifficulty.MEDIA);
            q50.setDica("Potência de potência: multiplique os expoentes. (aᵐ)ⁿ = aᵐˣⁿ");
            q50.setExplicacao("(2²)³ = 2²ˣ³ = 2⁶ = 64.");
            questionRepository.save(q50);

            Question q51 = new Question();
            q51.setPergunta("Qual é o valor de 2⁻³?");
            q51.setOpcoes(List.of("1/8", "-8", "-6", "1/6"));
            q51.setIndiceCorreto(0);
            q51.setCategoria(QuestionCategory.POTENCIACAO);
            q51.setDifficulty(QuestionDifficulty.MEDIA);
            q51.setDica("Expoente negativo significa o inverso: a⁻ⁿ = 1/aⁿ.");
            q51.setExplicacao("2⁻³ = 1/2³ = 1/8.");
            questionRepository.save(q51);

            Question q52 = new Question();
            q52.setPergunta("Quantos bits são necessários para representar 256 valores distintos?");
            q52.setOpcoes(List.of("8", "7", "9", "16"));
            q52.setIndiceCorreto(0);
            q52.setCategoria(QuestionCategory.POTENCIACAO);
            q52.setDifficulty(QuestionDifficulty.MEDIA);
            q52.setDica("Com n bits temos 2ⁿ combinações. Qual n faz 2ⁿ = 256?");
            q52.setExplicacao("2⁸ = 256, portanto são necessários 8 bits (1 byte).");
            questionRepository.save(q52);

            Question q53 = new Question();
            q53.setPergunta("Qual é o resultado de 4^(1/2)?");
            q53.setOpcoes(List.of("2", "1", "8", "16"));
            q53.setIndiceCorreto(0);
            q53.setCategoria(QuestionCategory.POTENCIACAO);
            q53.setDifficulty(QuestionDifficulty.DIFICIL);
            q53.setDica("Expoente 1/2 equivale à raiz quadrada do número.");
            q53.setExplicacao("4^(1/2) = √4 = 2.");
            questionRepository.save(q53);

            Question q54 = new Question();
            q54.setPergunta("Se 2ⁿ = 1024, qual é o valor de n?");
            q54.setOpcoes(List.of("10", "8", "12", "16"));
            q54.setIndiceCorreto(0);
            q54.setCategoria(QuestionCategory.POTENCIACAO);
            q54.setDifficulty(QuestionDifficulty.DIFICIL);
            q54.setDica("1024 é um número muito comum em computação — pense em kilobytes.");
            q54.setExplicacao("2¹⁰ = 1024. Logo n = 10. Por isso 1 KB = 1024 bytes.");
            questionRepository.save(q54);

            Question q55 = new Question();
            q55.setPergunta("Simplifique: (a³ × b²) / (a × b²)");
            q55.setOpcoes(List.of("a²", "a³", "a²b", "ab²"));
            q55.setIndiceCorreto(0);
            q55.setCategoria(QuestionCategory.POTENCIACAO);
            q55.setDifficulty(QuestionDifficulty.DIFICIL);
            q55.setDica("Cancele os termos iguais: b²/b² = 1. Depois simplifique a³/a.");
            q55.setExplicacao("b²/b² = 1; a³/a = a². Resultado: a².");
            questionRepository.save(q55);

            // ═══════════════════════════════════════════
            // CONTAGEM E ANÁLISE COMBINATÓRIA — 10 questões
            // ═══════════════════════════════════════════

            Question q56 = new Question();
            q56.setPergunta("De quantas formas diferentes podemos organizar 3 livros em uma prateleira?");
            q56.setOpcoes(List.of("6", "3", "9", "12"));
            q56.setIndiceCorreto(0);
            q56.setCategoria(QuestionCategory.CONTAGEM);
            q56.setDifficulty(QuestionDifficulty.FACIL);
            q56.setDica("Isso é uma permutação de 3 elementos: 3! = 3 × 2 × 1.");
            q56.setExplicacao("3! = 3×2×1 = 6. Existem 6 formas de organizar 3 livros.");
            questionRepository.save(q56);

            Question q57 = new Question();
            q57.setPergunta("Quantas combinações de 2 elementos podemos formar com um conjunto de 5 elementos?");
            q57.setOpcoes(List.of("10", "20", "15", "25"));
            q57.setIndiceCorreto(0);
            q57.setCategoria(QuestionCategory.CONTAGEM);
            q57.setDifficulty(QuestionDifficulty.MEDIA);
            q57.setDica("Use a fórmula C(n,k) = n! / (k! × (n-k)!). Aqui n=5, k=2.");
            q57.setExplicacao("C(5,2) = 5! / (2! × 3!) = 120 / (2×6) = 10.");
            questionRepository.save(q57);

            Question q58 = new Question();
            q58.setPergunta("Um algoritmo tem 4 etapas, cada uma com 3 opções. Quantos caminhos existem?");
            q58.setOpcoes(List.of("81", "12", "64", "24"));
            q58.setIndiceCorreto(0);
            q58.setCategoria(QuestionCategory.CONTAGEM);
            q58.setDifficulty(QuestionDifficulty.MEDIA);
            q58.setDica("Use o Princípio Multiplicativo: multiplique as opções de cada etapa independente.");
            q58.setExplicacao("3×3×3×3 = 3⁴ = 81 caminhos possíveis.");
            questionRepository.save(q58);

            Question q59 = new Question();
            q59.setPergunta("Qual é o valor de 5!?");
            q59.setOpcoes(List.of("120", "60", "24", "720"));
            q59.setIndiceCorreto(0);
            q59.setCategoria(QuestionCategory.CONTAGEM);
            q59.setDifficulty(QuestionDifficulty.FACIL);
            q59.setDica("n! = n × (n-1) × (n-2) × ... × 2 × 1. Multiplique de 5 até 1.");
            q59.setExplicacao("5! = 5×4×3×2×1 = 120.");
            questionRepository.save(q59);

            Question q60 = new Question();
            q60.setPergunta("Quantos arranjos de 2 elementos existem em um conjunto de 4?");
            q60.setOpcoes(List.of("12", "6", "8", "4"));
            q60.setIndiceCorreto(0);
            q60.setCategoria(QuestionCategory.CONTAGEM);
            q60.setDifficulty(QuestionDifficulty.MEDIA);
            q60.setDica("Arranjo: A(n,k) = n!/(n-k)!. A ordem importa! A(4,2) = 4!/2!");
            q60.setExplicacao("A(4,2) = 4!/(4-2)! = 24/2 = 12.");
            questionRepository.save(q60);

            Question q61 = new Question();
            q61.setPergunta("Uma senha tem 3 dígitos distintos de 0-9. Quantas senhas são possíveis?");
            q61.setOpcoes(List.of("720", "1000", "504", "900"));
            q61.setIndiceCorreto(0);
            q61.setCategoria(QuestionCategory.CONTAGEM);
            q61.setDifficulty(QuestionDifficulty.MEDIA);
            q61.setDica("Como os dígitos são distintos, use arranjo: 10 opções para o 1°, 9 para o 2°, 8 para o 3°.");
            q61.setExplicacao("10 × 9 × 8 = 720 senhas possíveis.");
            questionRepository.save(q61);

            Question q62 = new Question();
            q62.setPergunta("De quantas formas podemos escolher 3 pessoas de um grupo de 6 para formar uma comissão?");
            q62.setOpcoes(List.of("20", "120", "30", "18"));
            q62.setIndiceCorreto(0);
            q62.setCategoria(QuestionCategory.CONTAGEM);
            q62.setDifficulty(QuestionDifficulty.MEDIA);
            q62.setDica("Comissão = combinação (ordem não importa). C(6,3) = 6!/(3!×3!).");
            q62.setExplicacao("C(6,3) = 6!/(3!×3!) = 720/36 = 20.");
            questionRepository.save(q62);

            Question q63 = new Question();
            q63.setPergunta("Quantos subconjuntos tem um conjunto com 4 elementos?");
            q63.setOpcoes(List.of("16", "8", "12", "24"));
            q63.setIndiceCorreto(0);
            q63.setCategoria(QuestionCategory.CONTAGEM);
            q63.setDifficulty(QuestionDifficulty.DIFICIL);
            q63.setDica("Para cada elemento há 2 opções: incluir ou não. Use potenciação: 2ⁿ.");
            q63.setExplicacao("Um conjunto com n elementos tem 2ⁿ subconjuntos. 2⁴ = 16.");
            questionRepository.save(q63);

            Question q64 = new Question();
            q64.setPergunta("Quantas permutações existem da palavra 'ANA'?");
            q64.setOpcoes(List.of("3", "6", "2", "12"));
            q64.setIndiceCorreto(0);
            q64.setCategoria(QuestionCategory.CONTAGEM);
            q64.setDifficulty(QuestionDifficulty.DIFICIL);
            q64.setDica("Permutação com repetição: n!/r! onde r é a quantidade de letras repetidas. 'A' repete 2 vezes.");
            q64.setExplicacao("3!/2! = 6/2 = 3. As letras A se repetem, dividimos por 2!.");
            questionRepository.save(q64);

            Question q65 = new Question();
            q65.setPergunta("Em um torneio de 5 times, quantos jogos acontecem se todos enfrentam todos uma vez?");
            q65.setOpcoes(List.of("10", "20", "15", "25"));
            q65.setIndiceCorreto(0);
            q65.setCategoria(QuestionCategory.CONTAGEM);
            q65.setDifficulty(QuestionDifficulty.DIFICIL);
            q65.setDica("Cada jogo envolve 2 times. Use combinação C(5,2) pois A vs B = B vs A.");
            q65.setExplicacao("C(5,2) = 5!/(2!×3!) = 10 jogos no total.");
            questionRepository.save(q65);
        }
    }
}
