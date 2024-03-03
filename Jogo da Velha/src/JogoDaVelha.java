import java.util.Scanner;

public class JogoDaVelha {
    // Instância do mapa do jogo
    private JogoDaVelha_Mapa jogoMapa = new JogoDaVelha_Mapa(); 

    // Instância do jogador PC
    private JogoDaVelha_PC jogoPC = new JogoDaVelha_PC(jogoMapa); 

    // Instância do jogador humano
    private JogoDaVelha_Jogador jogoJogador = new JogoDaVelha_Jogador(jogoMapa); 

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in); // Instância do Scanner para entrada de dados do teclado
        JogoDaVelha jogo = new JogoDaVelha(); // Instância do jogo da velha
        jogo.jogar(teclado); // Chamada para iniciar o jogo
        teclado.close(); // Fechar o Scanner após a finalização do jogo
    }

    // Método para controlar o fluxo do jogo
    private void jogar(Scanner teclado) {
        char jogarNovamente; // Variável para controlar se o jogador deseja jogar novamente
        do {
            jogoMapa.limpaMapa(); // Limpar o mapa para iniciar uma nova partida
            int jogadas = 0; // Contador de jogadas
            char quemComeca = sortearPrimeiraJogada(); // Sortear quem começa a partida

            // Loop principal do jogo
            while (jogadas < 9) {
                jogoMapa.desenha(jogadas); // Desenhar o estado atual do mapa
                if (quemComeca == 'X') { // Se é a vez do jogador humano
                    if (jogoJogador.joga(teclado)) { // Jogada do jogador humano
                        System.out.println("... Jogador GANHOU!");
                        break; // Se o jogador ganhou, sair do loop
                    }
                    quemComeca = 'O'; // Trocar para a vez do jogador PC
                } else { // Se é a vez do jogador PC
                    if (jogoPC.joga()) { // Jogada do jogador PC
                        System.out.println("... PC GANHOU!");
                        break; // Se o PC ganhou, sair do loop
                    }
                    quemComeca = 'X'; // Trocar para a vez do jogador humano
                }
                jogadas++; // Incrementar o contador de jogadas
            }

            // Verificar se houve empate
            if (jogadas == 9) {
                System.out.println("... EMPATOU!");
            }

            // Exibir o tabuleiro após a última jogada
            jogoMapa.desenha(jogadas);

            // Perguntar se o jogador deseja jogar novamente
            System.out.println("________________________");
            System.out.print("Deseja jogar novamente (s/n)? ");
            jogarNovamente = teclado.next().charAt(0);
        } while (jogarNovamente == 's' || jogarNovamente == 'S');

        // Mensagem de fim do jogo
        System.out.println("--- FIM ---");
    }

    // Método para sortear quem começa o jogo
    private char sortearPrimeiraJogada() {
        return Math.random() < 0.5 ? 'X' : 'O'; // Retorna 'X' ou 'O' aleatoriamente
    }
}

// Classe que representa o mapa do jogo
class JogoDaVelha_Mapa {
    char[][] mapa = new char[3][3]; // Representação do mapa do jogo (tabuleiro)

    // Método para limpar o mapa
    public void limpaMapa() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mapa[i][j] = ' '; // Preencher o mapa com espaços em branco
            }
        }
    }

    // Método para desenhar o mapa
    public void desenha(int jogada) {
        System.out.println("------------- . . jogada: " + jogada);
        for (int i = 0; i < 3; i++) {
            System.out.print("|");
            for (int j = 0; j < 3; j++) {
                System.out.print(mapa[i][j] + "|"); // Imprimir o conteúdo do mapa
            }
            System.out.println();
        }
        System.out.println("-------------");
    }

    // Método para registrar uma jogada no mapa
    public boolean jogar(int l, int c, char jogador) {
        if (mapa[l][c] == ' ') { // Se a célula estiver vazia
            mapa[l][c] = jogador; // Registrar a jogada do jogador
            return true; // Indicar que a jogada foi bem-sucedida
        }
        System.out.println("Posição já preenchida. Escolha outra.");
        return false; // Indicar que a jogada não foi possível (célula ocupada)
    }

    // Método para verificar se alguém ganhou o jogo
    public boolean ganhou(char jogador) {
        // Verificar se alguma linha ou coluna está completamente preenchida pelo jogador
        for (int i = 0; i < 3; i++) {
            if ((mapa[i][0] == jogador && mapa[i][1] == jogador && mapa[i][2] == jogador) ||
                (mapa[0][i] == jogador && mapa[1][i] == jogador && mapa[2][i] == jogador)) {
                return true; // Se houver uma linha ou coluna completa, o jogador venceu
            }
        }
        // Verificar se alguma diagonal está completamente preenchida pelo jogador
        if ((mapa[0][0] == jogador && mapa[1][1] == jogador && mapa[2][2] == jogador) ||
            (mapa[0][2] == jogador && mapa[1][1] == jogador && mapa[2][0] == jogador)) {
            return true; // Se houver uma diagonal completa, o jogador venceu
        }
        return false; // Se nenhuma das condições acima for atendida, o jogador não venceu
    }

    // Método para sortear um número aleatório dentro de um intervalo especificado
    public int sortear(int inicio, int fim) {
        return (int) (Math.random() * (fim - inicio + 1) + inicio);
    }
}

// Classe que representa o jogador PC
class JogoDaVelha_PC {
    private JogoDaVelha_Mapa mapa;
    private char letra = 'O'; // Letra representativa do jogador PC

    public JogoDaVelha_PC(JogoDaVelha_Mapa mapa) {
        this.mapa = mapa; // Inicializar o mapa do jogo
    }

    // Método para que o PC realize uma jogada
    public boolean joga() {
        int linha, coluna;
        do {
            linha = mapa.sortear(0, 2); // Sortear uma linha aleatória
            coluna = mapa.sortear(0, 2); // Sortear uma coluna aleatória
        } while (!mapa.jogar(linha, coluna, letra)); // Repetir até encontrar uma posição vazia
        System.out.println("PC[" + linha + "," + coluna + "]"); // Imprimir a jogada do PC
        return mapa.ganhou(letra); // Verificar se o PC venceu após a jogada
    }
}

// Classe que representa o jogador humano
class JogoDaVelha_Jogador {
    private JogoDaVelha_Mapa mapa;
    private char letra = 'X'; // Letra representativa do jogador humano

    public JogoDaVelha_Jogador(JogoDaVelha_Mapa mapa) {
        this.mapa = mapa; // Inicializar o mapa do jogo
    }

    // Método para que o jogador realize uma jogada
    public boolean joga(Scanner teclado) {
        int linha, coluna;
        do {
            System.out.print("Digite a linha (0-2): ");
            linha = teclado.nextInt(); // Ler a linha digitada pelo jogador
            System.out.print("Digite a coluna (0-2): ");
            coluna = teclado.nextInt(); // Ler a coluna digitada pelo jogador
        } while (!mapa.jogar(linha, coluna, letra)); // Repetir até encontrar uma posição válida
        return mapa.ganhou(letra); // Verificar se o jogador venceu após a jogada
    }
}
