package tictactoe;

import java.util.Scanner;

// idea: method to check "will this next move win the game?" for medium/hard difficulty levels
// have to account for if there is only 1 move left, don't prevent the play

public class Main {

    static final Scanner scanner = new Scanner(System.in);
    private String startingCells;
    private char[][] board = new char[3][3];
    private char nextPlay = 'X';
    private Difficulty difficulty = Difficulty.EASY;
    private GameState gameState = GameState.NOT_FINISHED;
    private Winner winner = Winner.NONE;
    private Player currentPlayer = Player.HUMAN;

    public static void main(String[] args) {
        // allow optional starting cells as argument
        String startingCells = args.length > 0 ? args[0] : "_________";
        Main game = new Main(startingCells);
        game.readyPlayerOne();
    }

    public Main(String startingCells) {
        this.startingCells = startingCells;
    }

    private void readyPlayerOne() {
        seedBoard();

        printBoard();

        while (gameState == GameState.NOT_FINISHED) {
            tick();
        }
        scanner.close();
    }

    private void tick() {
        if (currentPlayer == Player.HUMAN) {
            requestCoordinates();
            printBoard();
            if (checkForWinner()) return;
            if (!hasEmptySpace(board)) {
                System.out.println("Draw");
                gameState = GameState.DRAW;
                return;
            }

            currentPlayer = Player.COMPUTER;
            nextPlay = 'O';
        } else {
            System.out.println("Making move level \"" + difficulty.name().toLowerCase() + "\"");

            int[] coords = generateRandomValidCoordinates();
            board[coords[0]][coords[1]] = nextPlay;
            printBoard();
            if (checkForWinner()) return;
            if (!hasEmptySpace(board)) {
                System.out.println("Draw");
                gameState = GameState.DRAW;
                return;
            }

            currentPlayer = Player.HUMAN;
            nextPlay = 'X';
        }
    }

    /**
     * Seed the board from the startingCells string
     */
    private void seedBoard() {
        int xCount = 0;
        int oCount = 0;
        for (int i = 0; i < 9; ++i) {
            char charToAdd = startingCells.charAt(i) == '_' ? ' ' : startingCells.charAt(i);
            board[i / 3][i % 3] = charToAdd;
            if (charToAdd == 'X') xCount++;
            if (charToAdd == 'O') oCount++;
        }
        nextPlay = xCount > oCount ? 'O' : 'X';
    }

    /**
     * Print the current state of the board
     */
    private void printBoard() {
        System.out.println("---------");
        for (int i = 0; i < 3; ++i) {
            System.out.print("| ");
            for (int j = 0; j < 3; ++j) {
                System.out.print(board[i][j] + " ");

            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    /**
     * Generate random valid coordinates for the computer's move
     * @return int[] array of size 2 with row and column indices
     */
    private int[] generateRandomValidCoordinates() {
        boolean isValid = false;
        int a;
        int b;
        do {
            a = (int) (Math.random() * 3);
            b = (int) (Math.random() * 3);
            if (board[a][b] == ' ') {
                isValid = true;
            }
        } while (!isValid);
        return new int[]{a, b};
    }

    /**
     * Request coordinates from user and validate input and set the board cell
     */
    private void requestCoordinates() {
        System.out.print("Enter the coordinates: ");
        boolean inputIsValid = false;
        do {
            String input = scanner.nextLine().trim();
            String[] arrCheck = input.split(" ");
            if (arrCheck.length != 2) {
                System.out.println("You should enter numbers!");
                continue;
            }
            int a = -1;
            int b = -1;
            try {
                a = Integer.parseInt(arrCheck[0]) - 1;
                b = Integer.parseInt(arrCheck[1]) - 1;
            } catch (NumberFormatException e) {
                System.out.println("You should enter numbers!");
                continue;
            }
            if (a < 0 || a > 2 || b < 0 || b > 2) {
                System.out.println("Coordinates should be from 1 to 3!");
                continue;
            }
            if (board[a][b] != ' ') {
                System.out.println("This cell is occupied! Choose another one!");
                continue;
            }


            inputIsValid = true;

            board[a][b] = nextPlay;
        } while (!inputIsValid);
    }

    /**
     * Check for three in a row in rows, columns, and diagonals
     * @return boolean true if three in a row found, false otherwise
     */
    private boolean checkForWinner() {
        if (checkRows(board) || checkColumns(board) || checkDiagonals(board)) {
            System.out.printf("%s wins", winner);
            return true;
        }

        return false;
    }

    /**
     * Check rows for three in a row
     * @return boolean true if three in a row found, false otherwise
     */
    private boolean checkRows(char[][] boardToCheck) {
        for (int i = 0; i < 3; ++i) {
            char currentlyChecking = ' ';
            boolean rowStatus = false;

            for (int j = 0; j < 3; ++j) {
                if (boardToCheck[i][j] == ' ') {
                    rowStatus = false;
                    break;
                }
                if (j == 0) {
                    currentlyChecking = boardToCheck[i][j];
                    continue;
                }

                rowStatus = boardToCheck[i][j] == currentlyChecking;
                if (!rowStatus) break;
            }
            if (rowStatus) {
                // found three in a row! <currentlyChecking> is the winner
                winner = currentlyChecking == 'X' ? Winner.X : Winner.O;
                gameState = currentlyChecking == 'X' ? GameState.X_WINS : GameState.O_WINS;
                return true;
            }
        }
        return false;
    }

    /**
     * Check columns for three in a row
     * @return boolean true if three in a row found, false otherwise
     */
    private boolean checkColumns(char[][] boardToCheck) {
        for (int i = 0; i < 3; ++i) {
            char currentlyChecking = ' ';
            boolean columnStatus = false;

            for (int j = 0; j < 3; ++j) {
                if (boardToCheck[j][i] == ' ') {
                    columnStatus = false;
                    break;
                }
                if (j == 0) {
                    currentlyChecking = boardToCheck[j][i];
                    continue;
                }

                columnStatus = boardToCheck[j][i] == currentlyChecking;
                if (!columnStatus) break;
            }
            if (columnStatus) {
                winner = currentlyChecking == 'X' ? Winner.X : Winner.O;
                gameState = currentlyChecking == 'X' ? GameState.X_WINS : GameState.O_WINS;
                return true;
            }
        }

        return false;
    }

    /**
     * Check diagonals for three in a row
     * @return boolean true if three in a row found, false otherwise
     */
    private boolean checkDiagonals(char[][] boardToCheck) {
        if ((boardToCheck[0][0] == boardToCheck[1][1]) && (boardToCheck[1][1] == boardToCheck[2][2]) && (boardToCheck[0][0] != ' ')) {
            winner = boardToCheck[0][0] == 'X' ? Winner.X : Winner.O;
            gameState = boardToCheck[0][0] == 'X' ? GameState.X_WINS : GameState.O_WINS;
            return true;
        }

        if ((boardToCheck[0][2] == boardToCheck[1][1]) && (boardToCheck[1][1] == boardToCheck[2][0]) && (boardToCheck [0][2] != ' ')) {
            winner = boardToCheck[0][2] == 'X' ? Winner.X : Winner.O;
            gameState = boardToCheck[0][2] == 'X' ? GameState.X_WINS : GameState.O_WINS;
            return true;
        }

        return false;
    }

    private boolean hasEmptySpace(char[][] boardToCheck) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (boardToCheck[i][j] == ' ') {
                    return true;
                }
            }
        }
        return false;
    }

    private enum Difficulty {
        EASY, MEDIUM, HARD
    }
    private enum GameState {
        NOT_FINISHED, DRAW, X_WINS, O_WINS
    }
    private enum Winner {
        X, O, NONE
    }
    private enum Player {
        HUMAN, COMPUTER
    }
}
