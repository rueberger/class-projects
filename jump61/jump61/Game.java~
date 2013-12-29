package jump61;

import java.io.Reader;
import java.io.Writer;
import java.io.PrintWriter;

import java.util.Scanner;
import java.util.Random;

import java.util.NoSuchElementException;

import static jump61.Color.*;
import static jump61.GameException.error;

/** Main logic for playing (a) game(s) of Jump61.
 *  @author Andrew Berger
 */
class Game {

    /** Name of resource containing help message. */
    private static final String HELP = "jump61/Help.txt";

    /** A new Game that takes command/move input from INPUT, prints
     *  normal output on OUTPUT, prints prompts for input on PROMPTS,
     *  and prints error messages on ERROROUTPUT. The Game now "owns"
     *  INPUT, PROMPTS, OUTPUT, and ERROROUTPUT, and is responsible for
     *  closing them when its play method returns. */
    Game(Reader input, Writer prompts, Writer output, Writer errorOutput) {
        _board = new MutableBoard(Defaults.BOARD_SIZE);
        _readonlyBoard = new ConstantBoard(_board);
        _prompter = new PrintWriter(prompts, true);
        _inp = new Scanner(input);
        _inp.useDelimiter("(?m)$|^|\\p{Blank}");
        _out = new PrintWriter(output, true);
        _err = new PrintWriter(errorOutput, true);
        _continue = true;
        _playing = false;

    }

    /** Returns a readonly view of the game board.  This board remains valid
     *  throughout the session. */
    Board getBoard() {
        return _readonlyBoard;
    }

    /** Returns a mutable copy of the current board.
     *  Necessary for the AI to work */
    Board getMutableBoard() {
        return _board.internalCopy();
    }

    /** Play a session of Jump61.  This may include multiple games,
     *  and proceeds until the user exits.  Returns an exit code: 0 is
     *  normal; any positive quantity indicates an error.  */
    int play() {
        _out.println("Welcome to " + Defaults.VERSION);
        _out.flush();
        _red = new HumanPlayer(this, RED);
        _blue = new AI(this, BLUE);
        _players = new Player[] {_red, _blue};
        while (_continue) {
            if (_playing) {
                try {
                    _players[_board.selectMove()].makeMove();
                    checkForWin();
                } catch (GameException e) {
                    if (e.getMessage() != null) {
                        reportError(e.getMessage());
                    } else {
                        reportError("Got some kind of anonymous error: " + e);
                    }
                    return 1;
                }
            } else if (promptForNext()) {
                readExecuteCommand();
            } else {
                _continue = false;
            }
        }
        _prompter.close();
        _out.close();
        _err.close();
        return 0;
    }

    /** Get a move from my input and place its row and column in
     *  MOVE.  Returns true if this is successful, false if game stops
     *  or ends first. */
    boolean getMove(int[] move) {
        while (_playing && _move[0] == 0 && promptForNext()) {
            readExecuteCommand();
        }
        if (_move[0] > 0) {
            move[0] = _move[0];
            move[1] = _move[1];
            _move[0] = 0;
            return true;
        } else {
            return false;
        }
    }

    /** Add a spot to R C, if legal to do so. */
    void makeMove(int r, int c) {
        Color curr = _board.whoseMove();
        if (_board.isLegal(curr, r, c)) {
            _board.addSpot(curr, r, c);
        }
    }

    /** Add a spot to square #N, if legal to do so. */
    void makeMove(int n) {
        Color curr = _board.whoseMove();
        if (_board.isLegal(curr, n)) {
            _board.addSpot(curr, n);
        }
    }

    /** Return a random integer in the range [0 .. N), uniformly
     *  distributed.  Requires N > 0. */
    int randInt(int n) {
        return _random.nextInt(n);
    }

    /** Send a message to the user as determined by FORMAT and ARGS, which
     *  are interpreted as for String.format or PrintWriter.printf. */
    void message(String format, Object... args) {
        _out.printf(format, args);
    }

    /** Reverts one board state. */
    void undo() {
        _board.undo();
    }

    /** Check whether we are playing and there is an unannounced winner.
     *  If so, announce and stop play. */
    private void checkForWin() {
        if (_board.getWinner() != null && _playing) {
            announceWinner();
            stop();
        }
    }

    /** Send announcement of winner to my user output. */
    private void announceWinner() {
        Color winner = _board.getWinner();
        if (winner == RED) {
            _out.println("Red wins.");
        } else {
            _out.println("Blue wins.");
        }
        _out.flush();
    }

    /** Make PLAYER an AI for subsequent moves. */
    private void setAuto(Color player) {
        if (player == RED) {
            _red = new AI(this, RED);
            _players[0] = _red;
        } else {
            _blue = new AI(this, BLUE);
            _players[1] = _blue;
        }
        stop();
    }

    /** Make PLAYER take manual input from the user for subsequent moves. */
    private void setManual(Color player) {
        if (player == RED) {
            _red = new HumanPlayer(this, RED);
            _players[0] = _red;
        } else {
            _blue = new HumanPlayer(this, BLUE);
            _players[1] = _blue;
        }
        stop();
    }

    /** Stop any current game and clear the board to its initial
     *  state. */
    private void clear() {
        _board.clear(_board.size());
        stop();
    }

    /** Print the current board using standard board-dump format. */
    private void dump() {
        _out.println(_board);
        _out.flush();
    }

    /** Print a help message. */
    private void help() {
        Main.printHelpResource(HELP, _out);
    }

    /** Stop any current game and set the move number to N. */
    private void setMoveNumber(int n) {
        stop();
        _board.setMoves(n);
    }

    /** Seed the random-number generator with SEED. */
    private void setSeed(long seed) {
        _random.setSeed(seed);
    }

    /** Place SPOTS spots on square R:C and color the square red or
     *  blue depending on whether COLOR is "r" or "b".  If SPOTS is
     *  0, clears the square, ignoring COLOR.  SPOTS must be less than
     *  the number of neighbors of square R, C. */
    private void setSpots(int r, int c, int spots, String color) {
        Color col = null;
        if (color.equals("r")) {
            col = RED;
        } else if (color.equals("b")) {
            col = BLUE;
        }
        if (col != null) {
            _board.set(r, c, spots, col);
        }
    }

    /** Stop any current game and set the board to an empty N x N board
     *  with numMoves() == 0.  */
    private void setSize(int n) {
        stop();
        _board.clear(n);
    }

    /** Begin accepting moves for game.  If the game is won,
     *  immediately print a win message and end the game. */
    private void restartGame() {
        if (_board.getWinner() != null) {
            announceWinner();
            _playing = false;
        } else {
            _playing = true;
        }
        _move[0] = 0;
    }

    /** Save move R C in _move.  Error if R and C do not indicate an
     *  existing square on the current board. */
    private void saveMove(int r, int c) {
        if (!_board.exists(r, c)) {
            throw error("move %d %d out of bounds", r, c);
        }
        _move[0] = r;
        _move[1] = c;
    }

    /** Returns a color (player) name from _inp: either RED or BLUE.
     *  Throws an exception if not present. */
    private Color readColor() {
        return Color.parseColor(_inp.next("[rR][eE][dD]|[Bb][Ll][Uu][Ee]"));
    }

    /** Read and execute one command.  Leave the input at the start of
     *  a line, if there is more input. */
    private void readExecuteCommand() {
        if (_inp.hasNextInt()) {
            processMove();
        } else if (!_inp.hasNext("[\\n\\r]")) {
            executeCommand(_inp.next().toLowerCase());
        }
        if (_inp.hasNextLine()) {
            _inp.nextLine();
        }
    }

    /** Gather arguments and execute command CMND.  Throws GameException
     *  on errors. */
    private void executeCommand(String cmnd) {
        try {
            switch (cmnd) {
            case "\n": case "\r\n":
                return;
            case "#":
                break;
            case "clear":
                clear();
                break;
            case "auto":
                setAuto(parseColor(_inp.next()));
                break;
            case "manual":
                setManual(parseColor(_inp.next()));
                break;
            case "size":
                setSize(_inp.nextInt());
                break;
            case "move":
                setMoveNumber(_inp.nextInt());
                break;
            case "set":
                setSpots(_inp.nextInt(), _inp.nextInt(),
                         _inp.nextInt(), _inp.next("[BbRr]"));
                break;
            case "dump":
                dump();
                break;
            case "seed":
                String unused = _inp.next();
                break;
            case "help":
                help();
                break;
            case "start":
                restartGame();
                break;
            case "quit":
                _continue = false;
                break;
            case "undo":
                undo();
                break;
            default:
                throw error("bad command: '%s'", cmnd);
            }
        } catch (NoSuchElementException e) {
            reportError("Arguments formatted incorrectly");
        } catch (GameException e) {
            reportError(e.getMessage());
        }
    }

    /** Take the next tokens of int input and move. */
    private void processMove() {
        try {
            if (!_playing) {
                throw error("No game currently accepting moves");
            }
            int first = _inp.nextInt();
            int second = _inp.nextInt();
            if (_board.isLegal(_board.whoseMove(), first, second)) {
                saveMove(first, second);
            } else {
                throw error("Illegal move");
            }
        } catch (NoSuchElementException e) {
            reportError("Move is formatted incorrectly");
        } catch (GameException e) {
            reportError(e.getMessage());
        }
    }

    /** Print a prompt and wait for input. Returns true iff there is another
     *  token. */
    private boolean promptForNext() {
        if (_playing) {
            _prompter.print(_board.whoseMove());
        }
        _prompter.print(" $:");
        _prompter.flush();
        return _inp.hasNext();
    }


    /** Send an error message to the user formed from arguments FORMAT
     *  and ARGS, whose meanings are as for printf. */
    void reportError(String format, Object... args) {
        _err.print("Error: ");
        _err.printf(format, args);
        _err.println();
    }


    /** Takes INP and returns RED or BLUE.
     *  IFF inp = "red" or "blue"
     *  Returns null if inp fails.*/
    private Color parseColor(String inp) {
        inp = inp.toLowerCase();
        if (inp.equals("red")) {
            return RED;
        } else if (inp.equals("blue")) {
            return BLUE;
        } else {
            throw new NoSuchElementException(inp
                 +  " is not a well formatted color");
        }
    }

    /** Common operation.
     *  Needs own method to abstract and
     *  allow tinkering with implementation. */
    private void stop() {
        _playing = false;
    }

    /** Writer on which to print prompts for input. */
    private final PrintWriter _prompter;
    /** Scanner from current game input.  Initialized to return
     *  newlines as tokens. */
    private final Scanner _inp;
    /** Outlet for responses to the user. */
    private final PrintWriter _out;
    /** Outlet for error responses to the user. */
    private final PrintWriter _err;

    /** The board on which I record all moves. */
    private final Board _board;
    /** A readonly view of _board. */
    private final Board _readonlyBoard;

    /** A pseudo-random number generator used by players as needed. */
    private final Random _random = new Random();

    /** True iff a game is currently in progress. */
    private boolean _playing;

    /** The player in charge of the red squares. */
    private Player _red;

    /** The player in charge of the blue squares. */
    private Player _blue;

    /** Holds the players for calls to makeMove. */
    private Player[] _players;


    /** While true play loop executes. */
    private boolean _continue;

   /** Used to return a move entered from the console.  Allocated
     *  here to avoid allocations. */
    private final int[] _move = new int[2];
}
