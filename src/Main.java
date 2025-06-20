import java.sql.ClientInfoStatus;
import java.sql.SQLOutput;
import java.util.*;

public class Main {
    public static class Player {
        private String name;
        private int wins;
        private int losses;
        private int draws;

        public Player(String name, int wins, int losses, int draws) {
            this.name = name;
            this.wins = wins;
            this.losses = losses;
            this.draws = draws;
        }

        public String getName() {
            return name;
        }

        public int getWins()
        {
            return wins;
        }
        public int getLosses()
        {
            return losses;
        }
        public int getDraws()
        {
            return draws;
        }
        public void addWin() {
            wins++;
        }

        public void addDraws() {
            draws++;
        }

        public void addLost() {
            losses++;
        }

        public int winRate() {
            int total = wins + draws + losses;
            if (total == 0) {
                return 0;
            } else {
                return (wins / total) * 100;
            }
        }

    }
    public static class ScoreBoard
    {
        private TreeMap<Integer, ArrayList<String>> winTree = new TreeMap<>();
        private HashMap<String , Player> players = new HashMap<>();
        private int playedGames;

        public ScoreBoard() {}

        public void addGameResult(String winnerPlayerName, String losserPlayerName, Boolean draw)
        {
            if(!players.containsKey(winnerPlayerName) || !players.containsKey(losserPlayerName))
            {
                System.out.println("Uno o ambos jugadores no estan registrados");
                return;
            }
            Player winner = players.get(winnerPlayerName);
            Player losser = players.get(losserPlayerName);

            int oldWinnerWins = winner.getWins();
            int oldLosserWins = losser.getWins();

            if(winTree.containsKey(oldWinnerWins))
            {
                winTree.get(oldWinnerWins).remove(winnerPlayerName);
                if(winTree.get(oldWinnerWins).isEmpty())
                {
                    winTree.remove(oldWinnerWins);
                }
            }
            if(winTree.containsKey(oldLosserWins))
            {
                winTree.get(oldLosserWins).remove(losserPlayerName);
                if(winTree.get(oldLosserWins).isEmpty())
                {
                    winTree.remove(oldLosserWins);
                }
            }
            if(!draw)
            {
                winner.addWin();
                losser.addLost();
            }
            else {
                winner.addDraws();
                losser.addDraws();
            }

            int newWinnerWins = winner.getWins();
            int newLosserWins = losser.getWins();
            if(!winTree.containsKey(newWinnerWins))
            {
                winTree.put(newWinnerWins, new ArrayList<>());
            }
            winTree.get(newWinnerWins).add(winnerPlayerName);

            if(!winTree.containsKey(newLosserWins))
            {
                winTree.put(newLosserWins, new ArrayList<>());
            }
            winTree.get(newLosserWins).add(losserPlayerName);

            playedGames++;
        }
        public void registerPlayer(String playerName)
        {
            if(players.containsKey(playerName))
            {
                return;
            }

            Player p1 = new Player(playerName, 0, 0, 0);
            players.put(playerName,p1);
            if(!winTree.containsKey(0))
            {
                winTree.put(0, new ArrayList<>());
            }
            winTree.get(0).add(playerName);
        }
        public boolean checkPlayer(String playerName)
        {
            return players.containsKey(playerName);
        }

        public void mostrarEstadisticas() {
            System.out.println("\n========== ESTADÍSTICAS FINALES ==========");
            System.out.println("Total de partidas jugadas: " + playedGames);
            System.out.println("\nEstadísticas por jugador:");

            for (Player player : players.values()) {
                int total = player.getWins() + player.getLosses() + player.getDraws();
                System.out.println("\n" + player.getName() + ":");
                System.out.println("  Victorias: " + player.getWins());
                System.out.println("  Derrotas: " + player.getLosses());
                System.out.println("  Empates: " + player.getDraws());
                System.out.println("  Total: " + total);
                if (total > 0) {
                    System.out.println("  Porcentaje de victorias: " + player.winRate() + "%");
                }
            }
            System.out.println("==========================================");
        }

        public Player[] winRange(int low, int hi)
        {
            List<Player> resultado = new ArrayList<>();
            for(int i = low; i <= hi; i++)
            {
                if(winTree.containsKey(i))
                {
                    ArrayList<String> names = winTree.get(i);
                    for(int j = 0; j < names.size(); j++)
                    {
                        Player player = players.get(names.get(j));
                        resultado.add(player);
                    }
                }
            }
            return resultado.toArray(new Player[0]);
        }
        public Player[] winSuccessor(int wins) {
            Integer successorKey = null;
            Map.Entry<Integer, ArrayList<String>>[] entries = winTree.entrySet().toArray(new Map.Entry[winTree.size()]);

            for (int i = 0; i < entries.length; i++) {
                int key = entries[i].getKey();
                if (key > wins) {
                    successorKey = key;
                    break;
                }
            }
            if (successorKey == null) {
                return new Player[0];
            }
            ArrayList<String> names = winTree.get(successorKey);
            Player[] resultado = new Player[names.size()];
            for (int i = 0; i < names.size(); i++)
            {
                resultado[i] = players.get(names.get(i));
            }
            return resultado;
        }


    }
    public static class ConnectFour
    {
        private char[][] Tablero;
        private char currentSymbol;

        public ConnectFour()
        {
            currentSymbol = 'X';
            this.Tablero = new char[7][6];
            for(int i = 0; i < 7; i++)
                for(int j = 0; j < 6; j++)
                {
                    this.Tablero[i][j] = ' ';
                }
        }
        public Boolean makeMove(int z)
        {
            if(z < 0 || z >= 7)
            {
                return false;
            }
            for(int i = 0; i < 6; i++)
            {
                if (Tablero[z][i] == ' ')
                {
                    Tablero[z][i] = currentSymbol;
                    // Cambia el símbolo actual
                    if(currentSymbol == 'X')
                    {
                        currentSymbol = 'O';
                    }
                    else
                    {
                        currentSymbol = 'X';
                    }
                    return true;
                }
            }
            return false;
        }

        public char isGameOver()
        {
            for(int columna = 0; columna < 7; columna++)
            {
                for(int fila = 0; fila < 6; fila++)
                {
                    char currentSymbol = Tablero[columna][fila];
                    if(currentSymbol == ' ')
                    {
                        continue;
                    }
                    //Recorre Horizontal columna +3
                    if(columna <=3 &&
                            currentSymbol == Tablero[columna + 1][fila] &&
                            currentSymbol == Tablero[columna + 2][fila] &&
                            currentSymbol == Tablero[columna + 3][fila]){
                        return currentSymbol;
                    }

                    //Recorre vertical fila + 3
                    if(fila <= 2 &&
                            currentSymbol == Tablero[columna][fila + 1] &&
                            currentSymbol == Tablero[columna][fila + 2] &&
                            currentSymbol == Tablero[columna][fila + 3]) {
                        return currentSymbol;
                    }
                    // recorre en diagonal columnas+3 y filas+3
                    if(columna <= 3 && fila <= 2 &&
                            currentSymbol == Tablero[columna + 1][fila + 1] &&
                            currentSymbol == Tablero[columna + 2][fila + 2] &&
                            currentSymbol == Tablero[columna + 3][fila + 3] ){
                        return currentSymbol;
                    }
                    // recorre en diagonal inversa columnas-3 y filas +3
                    if(columna >= 3 && fila <= 2 &&
                            currentSymbol == Tablero[columna - 1][fila + 1] &&
                            currentSymbol == Tablero[columna - 2][fila + 2] &&
                            currentSymbol == Tablero[columna - 3][fila + 3]){
                        return currentSymbol;
                    }

                }
            }
            boolean full = true;
            for(int i = 0; i < 7; i++)
            {
                for(int j = 0; j < 6; j++)
                {
                    if(Tablero[i][j] == ' ')
                    {
                        full = false;
                        break;
                    }

                }
            }
            if(full == true)
            {
                return 'E';
            }
            return ' ';
        }
        public char[][] getTablero()
        {
            return Tablero;
        }
        public char getCurrentSymbol()
        {
            return currentSymbol;
        }
    }
    public static class Game
    {
        private String Status;
        private String WinnerPlayerName;
        private String playerNameA;
        private String playerNameB;
        private ConnectFour connectFour;

        private void prinBoard() {
            System.out.println("Estado Actual del tablero:");
            for(int i = 5; i >= 0; i--)
            {
                for(int j = 0; j < 7; j++)
                {
                    char c = connectFour.getTablero()[j][i];
                    System.out.print("| " + (c == ' ' ? '·' : c) + " ");
                }
                System.out.println("|");
            }
            System.out.println("  0   1   2   3   4   5   6  "); // etiquetas de columnas
            System.out.println();
        }
        public ConnectFour getConnectFour()
        {
            return connectFour;
        }

        public Game(String playerNameA,String playerNameB)
        {
            this.playerNameA = playerNameA;
            this.playerNameB = playerNameB;
            this.Status = "IN_PROGRESS";
            this.WinnerPlayerName = " ";
            this.connectFour = new ConnectFour();
        }
        public String play()
        {
            Scanner teclado = new Scanner(System.in);

            while(Status.equals("IN_PROGRESS"))
            {
                char current = connectFour.getCurrentSymbol();
                String currentPlayer;
                if (current == 'X') {
                    currentPlayer = playerNameA;
                } else {
                    currentPlayer = playerNameB;
                }

                System.out.println("Turno de: " + currentPlayer + "(" + current + ")");
                prinBoard();

                System.out.println("Ingrese columna (0 a 6):");
                int columna = teclado.nextInt();

                boolean moved = getConnectFour().makeMove(columna);
                if (!moved) {
                    System.out.println("Movimiento invalido. Columna llena a fuera de rango");
                    continue;
                }

                char resultado = connectFour.isGameOver();
                if(resultado == 'X' || resultado == 'O')
                {
                    Status = "VICTORY";
                    if(resultado == 'X')
                    {
                        WinnerPlayerName = playerNameA;
                    }
                    else
                    {
                        WinnerPlayerName = playerNameB;
                    }
                    prinBoard();
                    System.out.println("\uD83C\uDF89 VICTORIA MAGISTRAL DEL JUGADOR:  "  + WinnerPlayerName + " \uD83C\uDF89");
                    return WinnerPlayerName;
                }
                else if (resultado == 'E')
                {
                    Status = "DRAW";
                    WinnerPlayerName = "";
                    prinBoard();
                    System.out.println("EMPATARON MALAYAS QLIA");
                    return "";
                }
            }
            return "";

        }

    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ScoreBoard scoreboard = new ScoreBoard();

        System.out.println("¡Bienvenido a Conecta 4!");

        // Pedimos los nombres de los jugadores
        System.out.print("Ingrese el nombre del Jugador X: ");
        String jugadorA = scanner.nextLine();

        System.out.print("Ingrese el nombre del Jugador O: ");
        String jugadorB = scanner.nextLine();

        // Registramos los jugadores en el scoreboard
        scoreboard.registerPlayer(jugadorA);
        scoreboard.registerPlayer(jugadorB);

        boolean seguirJugando = true;

        while (seguirJugando) {
            System.out.println("\n========== NUEVA PARTIDA ==========");

            // Creamos la partida
            Game partida = new Game(jugadorA, jugadorB);

            // Iniciamos la partida
            String ganador = partida.play();

            // Actualizamos las estadísticas
            if (!ganador.isEmpty()) {
                // Hubo un ganador
                String perdedor = ganador.equals(jugadorA) ? jugadorB : jugadorA;
                scoreboard.addGameResult(ganador, perdedor, false);
                System.out.println("¡El ganador es: " + ganador + "!");
            } else {
                // Fue empate
                scoreboard.addGameResult(jugadorA, jugadorB, true);
                System.out.println("La partida terminó en empate.");
            }

            // Preguntamos si quieren jugar otra partida
            System.out.print("\n¿Quieren jugar otra partida? (s/n): ");
            String respuesta = scanner.nextLine().toLowerCase().trim();

            if (!respuesta.equals("s") && !respuesta.equals("si") && !respuesta.equals("sí")) {
                seguirJugando = false;
            }
        }

        // Mostramos las estadísticas finales
        scoreboard.mostrarEstadisticas();

        System.out.println("\n¡Gracias por jugar Conecta 4!");
        scanner.close();
    }
}