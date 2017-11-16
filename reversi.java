// (placeholder team name): Michael Crouch (113581236), Jin Liu(114479952), Chris Iwaskiw

import java.awt.Point;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;


/**
 * computer player for reversi on a custom board
 **/
public class reversi {
	/*------------------------------STATIC FIELDS------------------------------*/
	protected static int[] offsets = {3,2,1,0,0,1,2,3};
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
	public HashMap<reversi, Integer> gameTree = new HashMap<reversi, Integer>();
	
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
		Scanner input = new Scanner(System.in).useDelimiter("");
		HashMap<Point, Integer> newBoard = new HashMap<Point, Integer>();
		int j = 0;
		while(j < 8) {
			int i = 0;
			while(input.hasNextInt()) {
				
				int value = input.nextInt();
				int offset = offsets[j];
				newBoard.put(new Point(i+offset,j) , value);
				i++;
			}
			input.nextLine();
			j++;
		}
		input.close();
		return new reversi(newBoard, null, true);
	}


	/**
	 *00000000
0000000000
000002100000
00000022000000
00000122200000
000002222000
0000001000
00000000 Generates the gameTree field.
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
			while(board.containsKey(next) && board.get(next) == 2) {
				Point possible_move = new Point(next.x+deltaX, next.y+deltaY);
			
			
				if(board.containsKey(possible_move)&& board.get(possible_move) == 0) {
					//System.out.println("legal move at (" + possible_move.x + "," + possible_move.y + ")");
					HashMap<Point, Integer> nextBoard = makeMove(board, possible_move, direction);
					Integer nextValue = evaluateBoard(nextBoard);
					reversi nextGame = new reversi(nextBoard, possible_move, !p1);
					gameTree.put(nextGame, nextValue); 
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
			if(board.get(disk) == 1) {
				getLegalMoves(disk);
			}
		}
	}

	/**
	 * Make move at point p. **The method will be called only if the move is legal**
	 * @param direction : corresponding to direction value setting in getLegalMoves()
	 **/
	private static HashMap<Point,Integer> makeMove(HashMap<Point,Integer> originalBoard, Point move, int direction) {
		
		int cur_x = move.x;
		int cur_y = move.y;
		int deltaX = 20, deltaY = 20;
		HashMap<Point,Integer> newBoard = new HashMap<Point, Integer>(originalBoard);
		newBoard.put(move, 1);
		
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
			while(newBoard.get(next) != 1) {
				Point reverse = new Point(next.x, next.y);
				newBoard.put(reverse, 1);
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
	public Point respond() {
		HashMap.Entry<reversi, Integer> maxEntry = null;
		for (HashMap.Entry<reversi, Integer> entry : gameTree.entrySet()) {
			if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
		        maxEntry = entry;
		    }
		}
		return maxEntry.getKey().prev;
	}
	
	/*
	 * Main
	 */
	public static void main(String[] args) {
		
		reversi game = processInput();
		game.legalMoves();
		Point output = game.respond();
		System.out.println(output.x + " " + output.y);
		
				
	
	}

}
