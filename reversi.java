// Team First Place: Michael Crouch (113581236), Jin Liu (114479952), Chris Iwaskiw (113576881)

import java.awt.Point;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;


/**
 * computer player for reversi on a custom board
 **/
public class reversi {
	/*------------------------------STATIC FIELDS------------------------------*/
	protected static int[] offsets = {9,8,7,6,5,4,3,2,1,0};
	protected static int[] rowLengths = {2,4,6,8,10,12,14,16,18,20};
	/* Note: notice that positionValue[y][x] is the position value of point(x,y) */
	protected static int[][] positionValueTable = {
			{0, 0, 0, 120, -20, 20, 5, 5, 20, -20, 120, 0, 0, 0},
			{0, 0, 120, -40, -40, 0, 0, 0, 0, -40, -40, 120, 0, 0,},
			{0, 120, -40, -40, -5, 15, 18, 18, 15, -5, -40, -40, 120,0},
			{120, -20, -20, 0, 10, 13, 1, 1, 13, 10, 0, -20, -20, 120},
			{120, -20, -20, 0, 10, 13, 1, 1, 13, 10, 0, -20, -20, 120},
			{0, 120, -40, -40, -5, 15, 18, 18, 15, -5, -40, -40, 120,0},
			{0, 0, 120, -40, -40, 0, 0, 0, 0, -40, -40, 120, 0, 0,},
			{0, 0, 0, 120, -20, 20, 5, 5, 20, -20, 120, 0, 0, 0}
			};

	protected static int color(boolean p1) {
		return p1 ? 1 : 2;
	}
/*	                  120   -20   20   5   5   20   -20   120
                     120  -40   -40   0    0   0   0    -40   -40   120	
                120  -40  -40   -5    15   18  18  15   -5    -40   -40   120
          120  -20   -20    0   10    13   1   1   13   10     0    -20   -20   120
          120  -20   -20    0   10    13   1   1   13   10     0    -20   -20   120
               120   -40  -40   -5    15   18  18  15   -5    -40   -40   120
                     120  -40   -40   0    0   0   0    -40   -40   120	
                          120   -20   20   5   5   20   -20   120
*/
	
	/*------------------------------PUBLIC FIELDS------------------------------*/
	
	/**The current game board.**/
	public HashMap<Point, Integer> board = new HashMap<Point, Integer>();
	/**The move taken to get to this current board.  Set to null for the initial board.**/
	public Point prev;
	/**Whoever's turn it is: true for player 1, false for player 2**/
	public boolean p1;
	/**The set of legal moves and their respective evalutation values.**/
	public HashSet<reversi> gameTree = new HashSet<reversi>();
	/**The evaluation value of the current board**/
	public int value;
	
	/*------------------------------CLASS CONSTRUCTOR------------------------------*/
	
	/** A reversi object contains a game board, a record of whatever the previous**/
	public reversi(HashMap<Point, Integer> board, Point prev, boolean p1) {
		this.board = board;
		this.prev = prev;
		this.p1 = p1;
	}

	/**
	 * Creates a reversi object by reading the standard input.
	 **/
	static reversi processInput() {
		Scanner input = new Scanner(System.in);
		HashMap<Point, Integer> newBoard = new HashMap<Point, Integer>();
		int j = 0;
		while(j < 10) {
			int i = 0;
			while(i < rowLengths[j]) {
				int value = Integer.parseInt(input.next());
				int offset = offsets[j];
				newBoard.put(new Point(i+offset,j) , value);
				i++;
			}
			j++;
		}
		input.close();
		return new reversi(newBoard, null, true);
	}


	/**
	 * Generates the gameTree field.
	 * 
	 * gets the legal moves and tokens taken
	 * 'disk' - Our piece on the board at one end of our move
	 * 'moves' - map of possible moves and the number of pieces taken
	 *  
	 **/
	private void getLegalMoves(Point disk) {
		//System.out.println("check (" + disk.x + "," + disk.y + ")getLegalMoves called");
		int cur_x = disk.x;
		int cur_y = disk.y;
		int deltaX = 20, deltaY = 20;
		int direction = 0;
		
		// check each direction
		for(int i=0;i<8;i++){
			switch(i){
			case 0: deltaX = -1; //left
					deltaY = 0;
					direction = 2; // reverse disk from right to right to left
					break;
			case 1: deltaX = 0; //up
					deltaY = -1;
					direction = 3;
					break;
			case 2: deltaX = 1; //right
					deltaY = 0;
					direction = 0;
					break;
			case 3: deltaX = 0; //down
					deltaY = 1;
					direction = 1;
					break;
			case 4: deltaX = -1;
					deltaY = -1; //Upper left
					direction = 6;
					break;
			case 5: deltaX = -1;
					deltaY = 1; //Bottom left
					direction = 7;
					break;
			case 6: deltaX = 1;
					deltaY = 1; //Bottom right
					direction = 4;
					break;
			case 7: deltaX = 1;
					deltaY = -1; //Upper right
					direction = 5;
					break;
			default:break;		//shouldn't happen 
			}
			
			Point next = new Point(cur_x+deltaX, cur_y+deltaY);
			
			while(board.containsKey(next) && board.get(next) == color(!p1)) {
				Point possible_move = new Point(next.x+deltaX, next.y+deltaY);
			
			
				if(board.containsKey(possible_move)&& board.get(possible_move) == 0) {
					//System.out.println("legal move at (" + possible_move.x + "," + possible_move.y + ")");
					HashMap<Point, Integer> nextBoard = makeMove(board, possible_move, direction);
					reversi nextGame = new reversi(nextBoard, possible_move, !p1);
					gameTree.add(nextGame); 
				}
				next = possible_move;
			}
		}
		
	}
	
	/**
	 * Quick setup for getLegalMoves()
	 **/
	private void legalMoves(){
		Set<Point> disks = board.keySet();
		for(Point disk: disks) {
			if(board.get(disk) == color(p1)) {
				getLegalMoves(disk);
			}
		}
	}

	/**
	 * Make move at point p. **The method will be called only if the move is legal**
	 * @param direction : corresponding to direction value setting in getLegalMoves()
	 **/
	private HashMap<Point,Integer> makeMove(HashMap<Point,Integer> originalBoard, Point move, int direction) {
		int playerColor = p1 ? 1 : 2;
		int cur_x = move.x;
		int cur_y = move.y;
		int deltaX = 20, deltaY = 20;
		HashMap<Point,Integer> newBoard = new HashMap<Point, Integer>(originalBoard);
		newBoard.put(move, playerColor);
		
		switch(direction){
			case 0: deltaX = -1; //to left
					deltaY = 0;
					break;
			case 1: deltaX = 0; //to up
					deltaY = -1;
					break;
			case 2: deltaX = 1; //to right
					deltaY = 0;
					break;
			case 3: deltaX = 0; //to down
					deltaY = 1;
					break;
			case 4: deltaX = -1;
					deltaY = -1; //to Upper left
					break;
			case 5: deltaX = -1;
					deltaY = 1; //to Bottom left
					break;
			case 6: deltaX = 1;
					deltaY = 1; //to Bottom right
					break;
			case 7: deltaX = 1;
					deltaY = -1; //to Upper right
					break;
			default:break;		 
			}
			
			Point next = new Point(cur_x + deltaX, cur_y + deltaY);
			while(newBoard.get(next) != playerColor) {
				Point reverse = new Point(next.x, next.y);
				newBoard.put(reverse, playerColor);
				next = new Point(next.x + deltaX, next.y + deltaY);
			}
		return newBoard;
	}
	
	/**
	 * Takes a hashmap representing a game board and returns its evaluation.
	 */
	private static int evaluateBoard(HashMap<Point, Integer> currentBoard) {
		return diffInMobility(currentBoard) + diffInPositionValue(currentBoard);
	}
	
	/**
 	 *	Evaluate the winner of the game
	 **/
	private static int evaluateTerminalBoard(HashMap<Point, Integer> currentBoard) {
		int numOfPlayer = 0;
		int numOfOpponent = 0;
		for(Point disk: currentBoard.keySet()) {
			if(currentBoard.get(disk) == 1) {
				numOfPlayer ++;
			}else if(currentBoard.get(disk) == 2){
				numOfOpponent ++;
			}
		}
		if (numOfPlayer > numOfOpponent) return 5000;
		else return -5000;		
	}
	
	/**
	 * Mobility Evaluation: Calculate the difference between number of possible moves
	 **/
	private static int diffInMobility(HashMap<Point, Integer> currentBoard) {
		int numOfPlayer = 0;
		int numOfOpponent = 0;
		for(Point disk: currentBoard.keySet()) {
			if(currentBoard.get(disk) == 1) {
				numOfPlayer ++;
			}else if(currentBoard.get(disk) == 2){
				numOfOpponent ++;
			}
		}
		
		return numOfPlayer - numOfOpponent;
	}
	
	/**
	 * According to preset position value table, calculate the difference between the position values.
	 **/
	private static int diffInPositionValue(HashMap<Point, Integer> currentBoard) {
		int myValue = 0;
		int opponentValue = 0;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 14; j++) {
				Point position = new Point(j,i);
				if(currentBoard.containsKey(position) && currentBoard.get(position) == 1) {
					myValue += positionValueTable[i][j];
				}else if(currentBoard.containsKey(position) && currentBoard.get(position) == 2) {
					opponentValue += positionValueTable[i][j];
				}
			}
		}
		
		return myValue - opponentValue;
	}
	
	/**
	*	Returns the correct move to make.  Currently returns the max-valued turn in the level-1 game tree,
	*	But hopefully will use the minimax algorithm in the future.
	**/
	public Point respond(int depth) {
		miniMax(depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
		int bestValue = -5000;
		Point bestMove = null;
		for(reversi r : gameTree) {
			if(r.value > bestValue) {
				bestValue = r.value;
				bestMove = r.prev;
			}
		}
		return bestMove;
	}
	
	/** Perform the minimax algorithm on the search tree**/
	public int miniMax(int depth, int alpha, int beta) {
		int minimaxValue = 0;
		//Check if search depth is 0.
		if(depth > 0) {
			//Generate children for current player
			this.legalMoves();
			//Check if children exist
			if(gameTree.size() > 0) {
				if(p1) {
					minimaxValue = maxChild(gameTree, depth, alpha, beta);
				} else minimaxValue = minChild(gameTree, depth, alpha, beta);
				
			} 
			//If no children exist for current player, pass turn on to the next.
			else { 
				p1 = !p1;
				this.legalMoves();
				if(gameTree.size() > 0) {
					if(p1) {
						minimaxValue = maxChild(gameTree, depth, alpha, beta);
					} else minimaxValue = minChild(gameTree, depth, alpha, beta);
				} else minimaxValue = evaluateTerminalBoard(board);
			}
		} else minimaxValue = evaluateBoard(board);
		this.value = minimaxValue;
		return minimaxValue;
	}
	
	private int maxChild(HashSet<reversi> gameTree, int depth, int alpha, int beta) {
		int max = -5000;
		for (reversi r : gameTree) {
			int minimaxed = r.miniMax(depth-1, alpha, beta);
			if (minimaxed > max) {
		        max = minimaxed;
		    }
			if(max > alpha) {
				alpha = max;
			}
			if(beta <= alpha) break;
		}
		//System.out.println("Maximizing: Beta: " + beta + " Alpha: " + alpha);
		return max;
	}
	
	private int minChild(HashSet<reversi> gameTree, int depth, int alpha, int beta) {
		int min = 5000;
		for (reversi r : gameTree) {
			int minimaxed = r.miniMax(depth-1, alpha, beta);
			if (minimaxed < min) {
		        min = minimaxed;
		    }
			if(min < beta) {
				beta = min;
			}
			if(beta <= alpha) break;
		}
		//System.out.println("Minimizing: Beta: " + beta + " Alpha: " + alpha);
		return min;
	}
	
	/** Test method to make sure minimax is working correctly **/
	public void printTreeValues(int previous, int round) {
		System.out.print(previous + " => ");
		for(reversi r: gameTree) {
			System.out.print(r.value + " ");
		}
		System.out.println();
		for(reversi r: gameTree) {
			r.printTreeValues(r.value, round + 1);
		}
	}
	
	/*
	 * Main
	 */
	public static void main(String[] args) {
		
		reversi game = processInput();
		Point output = game.respond(4);
		//game.printTreeValues(0, 1);
		System.out.println((output.y + 1) + " " + (output.x - offsets[output.y] + 1) );
		
				
	
	}

}