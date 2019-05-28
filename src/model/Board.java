package model;

import java.util.ArrayList;

import controller.GameConstants;

/*Сітка 8х8, де [рядок][стовпець] позначає положення елементу */
public class Board {
	// Властивості і представлення дошки
	private static final int BOARD_SIZE = 8;
	private Piece[][] representation;
	
	private Piece lastPieceMoved;
	private Move lastMove;
	
	private int blackPieces;
	private int whitePieces;
	private int blackKings;
	private int whiteKings;

	// Властивості кроку
	private int movesSinceCapture;
	public enum Direction {UP, DOWN, LEFT, RIGHT};
	

	public Board() {
		representation = new Piece[BOARD_SIZE][BOARD_SIZE];
		movesSinceCapture = 0;
		init();
		lastPieceMoved = null;
		lastMove = null;
		blackPieces = 12;
		whitePieces = 12;
	}
	
	
	public Board(Board other) {
		this.representation = new Piece[BOARD_SIZE][BOARD_SIZE];
		Piece[][] other_representation = other.getRepresentation();
		for (int i = 0; i < other_representation.length; ++i) {
			for (int j = 0; j < other_representation[0].length; ++j) {
				if (other_representation[i][j] != null) {
					this.representation[i][j] = new Piece(other_representation[i][j]);
				}
			}
		}
		movesSinceCapture = other.getMovesSinceCapture();
		lastPieceMoved = other.getLastPieceMoved();
		lastMove = other.getLastMove();
		blackPieces = other.getBlackPieces();
		whitePieces = other.getWhitePieces();
	}

	/* Ініціалізація дошки з шашками, які знаходяться
	 * на своїх стартових позиціях
	 */
	private void init() {
		for (int row = 0; row < 3; row++){
			for (int col = 0; col < 4; col++) {
				Piece white_piece = new Piece(Color.WHITE, BOARD_SIZE - row - 1, 2*col + (row % 2));
				Piece black_piece = new Piece(Color.BLACK, row, 2*col + (BOARD_SIZE - 1 - row) %2);
				representation[BOARD_SIZE - row - 1][2*col+ (row % 2)] = white_piece;
				representation[row][2*col + (BOARD_SIZE - 1 - row) %2] = black_piece;
			}
		}
	}
	
	/* Порівняння дошок */
	public boolean equals(Board other) {
		for (int i = 0; i < BOARD_SIZE; ++i) {
			for (int j = 0; j < BOARD_SIZE; ++j) {
				if(!(this.representation[i][j]).equals(other.getRepresentation()[i][j])) 
					return false;
			}
		}
		return true;
	}
	
	public void movePiece(Move move) {
		int sourceRow = move.source.row;
		int sourceCol = move.source.column;
		int destRow = move.destination.row;
		int destCol = move.destination.column;
	
		if (move.isJump()) {
			int monkeyRow = (destRow + sourceRow)/2;
			int monkeyCol = (destCol + sourceCol)/2;
	
			Piece removed = representation[monkeyRow][monkeyCol];
			
			if (removed.getColor() == Color.BLACK) {
				--blackPieces; 
				if (removed.getType() == Type.KING) --blackKings;
			}
			else {
				--whitePieces;
				if (removed.getType() == Type.KING) --whiteKings;
			}
			
			/* Видалити фрагмент, який "стрибає" */
			representation[monkeyRow][monkeyCol] = null;
			
			this.movesSinceCapture = 0;
		} 
		else {
			++this.movesSinceCapture;
		}
	
		/* Помістити в потрібну ячейку */
		representation[destRow][destCol] = representation[sourceRow][sourceCol];
		
		/* Видалити з попередньої ячейки */
		representation[sourceRow][sourceCol] = null;
		
		/* Оновити місцеположення фігури */
		representation[destRow][destCol].setLocation(new Location(destRow, destCol));
		
		Piece moved = representation[destRow][destCol];
		
		if (canPromote(moved)) {
			moved.promote();
			if (moved.color == Color.BLACK) {
				++blackKings;
			} else {
				++whiteKings;
			}
		}
	
		/* Оновити останню фішку, яка була зміщена */
		this.lastPieceMoved = moved;
	}

	/*
	 * Створення кордону
	 * @param color Колір фішок, для яких створюється кордон.
	 * @return Список можливих наступних ходів на дошці.
	 */
	public ArrayList<Board> generateFrontier(Color color) {
		ArrayList<Board> from_jumps = generateFrontierFromJumps(color);
		if (from_jumps.isEmpty()) {
			return generateFrontierFromRegularMoves(color);
		}
		return from_jumps;
	}

	/*
	 * Створення кордону для всіх фішок.
	 * @param color
	 */
	public ArrayList<Board> generateFrontierFromRegularMoves(Color color) {
		ArrayList<Board> frontier = new ArrayList<Board>();
		ArrayList<Move> moves = generateAllRegularMoves(color);
		for (Move move : moves) {
			Board board = new Board(this);
			board.movePiece(move);
			frontier.add(board);
		}
		return frontier;	
	}

	public ArrayList<Board> generateFrontierFromJumps(Color color) {
		ArrayList<Board> frontier = new ArrayList<Board>();
		ArrayList<Move> moves = generateAllJumpMoves(color);
		for (Move move : moves) {
			Board board = new Board(this);
			board.movePiece(move);
			frontier.add(board);
		}
		return frontier;	
	}

	public ArrayList<Move> generateMovesForPiece(Piece p) {
		ArrayList<Move> jumps = generateJumpMovesForPiece(p);
		if (jumps.isEmpty()) {
			return generateRegularMovesForPiece(p);
		}
		return jumps;
	}
	
	/*Створює набір Move для певної фішки*/
	public ArrayList<Move> generateRegularMovesForPiece(Piece p) {
		ArrayList<Move> avail_moves = new ArrayList<Move>();
		int row = p.getLocation().row;
		int col = p.getLocation().column;
		boolean up, down;
		up = p.getColor() == Color.WHITE || p.getType() == Type.KING;
		down = p.getColor() == Color.BLACK || p.getType() == Type.KING;
		if (up) {
			// верх зліва
			Move upLeft = new Move(p.getLocation(), new Location(row - 1, col - 1));
			if (isValidMove(upLeft)) {
				avail_moves.add(upLeft);
			}
			
			// верх праворуч	
			Move upRight = new Move(p.getLocation(), new Location(row - 1, col + 1));
			if (isValidMove(upRight)) {
				avail_moves.add(upRight);
			}
		}
		if (down) {
			// низ ліво
			Move downLeft = new Move(p.getLocation(), new Location(row + 1, col - 1));
			if (isValidMove(downLeft)) {
				avail_moves.add(downLeft);
			}
			
			// низ право
			Move downRight = new Move(p.getLocation(), new Location(row + 1, col + 1));
			if (isValidMove(downRight)) {
				avail_moves.add(downRight);
			}
		}
		return avail_moves;
	}
	
	public ArrayList<Board> generateJumpFrontierForPiece(Piece p) {
		ArrayList<Board> frontier = new ArrayList<Board>();
		ArrayList<Move> moves = generateJumpMovesForPiece(p);
		for (Move move : moves) {
			Board board = new Board(this);
			board.movePiece(move);
			frontier.add(board);
		}
		return frontier;
	}
	
	/*повертає можливі "стрибки", які може зробити фішка.*/
	public ArrayList<Move> generateJumpMovesForPiece(Piece p) {
		ArrayList<Move> jumps = new ArrayList<Move>();
		int row = p.getLocation().row, 
				col = p.getLocation().column;
		boolean up = p.getColor() == Color.WHITE || p.getType() == Type.KING;
		boolean down = p.getColor() == Color.BLACK || p.getType() == Type.KING;
		if (up) {
			// зверху зліва
			Move upleft = new Move(new Location(row, col), new Location(row - 2, col - 2));
			if (isValidJump(upleft)) {
				jumps.add(upleft);
			}
			// зверху праворуч
			Move upright = new Move(new Location(row, col), new Location(row - 2, col + 2));
			if (isValidJump(upright)) {
				jumps.add(upright);
			}
		}
		if (down) {
			// знизу зліва
			Move downleft = new Move(new Location(row, col), new Location(row + 2, col - 2));
			if (isValidJump(downleft)) {
				jumps.add(downleft);
			}
			// знизу праворуч
			Move downright = new Move(new Location(row, col), new Location(row + 2, col + 2));
			if (isValidJump(downright)) {
				jumps.add(downright);
			}
		}
		return jumps;
	}

	public ArrayList<Move> generateAllMoves(Color color) {
		ArrayList<Move> jumps = generateAllJumpMoves(color);
		if (jumps.isEmpty()) {
			return generateAllRegularMoves(color);
		}
		return jumps;
	}

	public ArrayList<Move> generateAllJumpMoves(Color color) {
		ArrayList<Move> frontier = new ArrayList<Move>();
		for (int i = 0; i < BOARD_SIZE; ++i) {
			for (int j = 0; j < BOARD_SIZE; ++j) {
				Piece p = this.representation[i][j];
				if (null != p && p.getColor() == color) {
					ArrayList<Move> jump_moves = generateJumpMovesForPiece(this.representation[i][j]);
					frontier.addAll(jump_moves);
				}
			}
		}
		return frontier;		
	}
	
	public ArrayList<Move> generateAllRegularMoves(Color color) {
		ArrayList<Move> frontier = new ArrayList<Move>();
		for (int i = 0; i < BOARD_SIZE; ++i) {
			for (int j = 0; j < BOARD_SIZE; ++j) {
				Piece p = this.representation[i][j];
				if(null != p && p.getColor() == color) {
					ArrayList<Move> moves = generateRegularMovesForPiece(this.representation[i][j]);
					frontier.addAll(moves);
				}
			}
		}
		return frontier;
	}
	
	/*Вивести поточну дошку */
	public void print() {
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				if (!isOccupied(new Location(row, col)))
					System.out.print("| ");
				else if (representation[row][col].getColor() == Color.BLACK) {
					if (representation[row][col].getType() == Type.NORMAL)
						System.out.print("|b");
					else	
						System.out.print("|B");
				}
				else {
					if (representation[row][col].getType() == Type.NORMAL)
						System.out.print("|w");
					else
						System.out.print("|W");
				}
			}
			System.out.println("|");
		}
	}
	
	public boolean isValidSquare(Location location) {
		return 	0 <= location.row && location.row < BOARD_SIZE && 
				0 <= location.column && location.column < BOARD_SIZE;
	}

	/*Визначає, чи є перехід допустимим стрибком */
	public boolean isValidJump(Move move) {
		if (isValidSquare(move.destination)) {
			Piece monkey = representation[(move.destination.row + move.source.row)/2][(move.destination.column + move.source.column)/2];
			Piece toMove = representation[move.source.row][move.source.column];
			return !isOccupied(move.destination)
					&& monkey != null
					&& monkey.getColor() == toMove.opposite();

		} else {
			return false;
		}
	}

	public boolean isValidMove(Move move) {
		return isValidSquare(move.destination) && !isOccupied(move.destination);
	}
	
	/*
	 * return true якщо у квадраті є фішка
	 * return false якщо помилка
	 */
	public boolean isOccupied(Location location) {
		return representation[location.row][location.column] != null;
	}
	
	public boolean canPromote(Piece p) {
		
		return p.getType() != Type.KING && 
				isPromotionLocation(p.getLocation());
	}
	
	public boolean movePromotesPiece(Move move) {
		return getPiece(move.source).getType() != Type.KING &&
				isPromotionLocation(move.destination);
	}
	
	public boolean isPromotionLocation(Location location) {
		return (location.row == 0 || 
				location.row == BOARD_SIZE - 1 );
	}
	
	public int pieceDifferentialHeuristic(Color color) {
		int blackHeuristic = blackPieces + blackKings;
		int whiteHeuristic = whitePieces + whiteKings;
		return (color == Color.BLACK ? 
				(blackHeuristic) : 
					(whiteHeuristic));
	}
	
	public Location samuelMapping(int k) {
		int augmented = (32 - k) * 2;
		int row = augmented / GameConstants.BOARD_SIZE;
		int col = augmented % GameConstants.BOARD_SIZE;
		if (row % 2 == 0) ++col;
		return new Location(row, col);
	}
	
	public int emptySquaresSurrounding(Piece piece) {
		int num = 0;
		int row = piece.getLocation().row;
		int col = piece.getLocation().column;
		boolean up = row > 0,
				down = row < GameConstants.BOARD_SIZE - 1,
				left = col > 0,
				right = col < GameConstants.BOARD_SIZE - 1;
		if (up && left && this.representation[row-1][col-1] == null) ++num;
		if (up && right && this.representation[row-1][col+1] == null) ++num;
		if (down && left && this.representation[row+1][col-1] == null) ++num;
		if (down && right && this.representation[row+1][col+1] == null) ++num;
		
		return num;
	}
	
	public boolean isActive(Piece piece) {
		if (piece == null) return false;
		ArrayList<Move> jumpFrontier = this.generateJumpMovesForPiece(piece);
		return jumpFrontier.size() != 0;
	}
	
	/* Параметр 1, якщо немає королів
	 * Якщо квадрат 7 або 26 занятий "активною" людиною
	 * Якщо ні один з цих квадратів не занятий "пасивною" людиною
	 */
	public int apexHeuristic(Color color) {
		boolean noKings = false,
				eitherSquaresOccupiedByActiveMan = false,
				neitherSquaresOccupiedByPassiveMan = true;
		Location square7 = this.samuelMapping(7);
		Location square26 = this.samuelMapping(26);
		Piece piece7 = this.getPiece(square7);
		Piece piece26 = this.getPiece(square26);
		
		boolean active7 = isActive(piece7);
		boolean active26 = isActive(piece26);
		
		noKings = (color == Color.BLACK && this.blackKings == 0) ||
					(color == Color.WHITE && this.whiteKings == 0);
		if (piece7 != null && piece7.getColor() == color) {
			eitherSquaresOccupiedByActiveMan |= (active7 && piece7.getType() == Type.NORMAL);
			neitherSquaresOccupiedByPassiveMan &= (active7 && piece7.getType() == Type.NORMAL);
		}
		if (piece26 != null && piece26.getColor() == color) {
			eitherSquaresOccupiedByActiveMan |= (active26 && piece26.getType() == Type.NORMAL);
			neitherSquaresOccupiedByPassiveMan &= (active26 && piece26.getType() == Type.NORMAL);
		}
		
		if (noKings && eitherSquaresOccupiedByActiveMan && neitherSquaresOccupiedByPassiveMan)
			return -1;
		return 0;
	}
	
	/*
	 Параметру зараховується 1, якщо на дошці немає активних королів 
	 і якщо два квадрати (1 і 3, або 30 і 32) в задньому ряду зайняті пасивними фішками.
	 */
	public int backHeuristic(Color color) {
		for (int i = 0; i < GameConstants.BOARD_SIZE; i++){ 
			for (int j = 0; j < GameConstants.BOARD_SIZE; j++){
				Piece piece = this.representation[i][j];
				if (piece != null
						&& piece.getType() == Type.KING 
						&& piece.getColor() == color
						&& isActive(piece)) {
					return 0;
				}
			}
		}
		
		
		if (color == Color.BLACK){
			Piece p30 = getPiece(samuelMapping(30));
			Piece p32 = getPiece(samuelMapping(32));
			
			if (!isActive(p30) && !isActive(p32)) {
				return 1;
			}
		}
		else { // Білий колір
			Piece p1 = getPiece(samuelMapping(1));
			Piece p3 = getPiece(samuelMapping(3));
			
			if (!isActive(p1) && !isActive(p3)) {
				return 1;
			}
		}
		
		return 0;
	}
	
	
	/*
	  Параметр приписується 1 для кожного з наступних квадратів: 
	  11,12,15,16,20,21,24 і 25, 
	  який займає "пасивна" людина.
	 */
	public int centHeuristic(Color color) {
		int sum = 0;
		int[] locations = {11, 12, 15, 16, 20, 21, 24, 25};
		for (int k : locations) {
			Piece p = this.getPiece(this.samuelMapping(k));
			if (p != null && p.getColor() == color && 
					p.getType() == Type.NORMAL && !isActive(p))
				++sum;
		}
		return sum;
	}
	
	/*
	 Параметр зараховується 1 для кожного квадрата, до якого 
	 "активна" людина може перемістити одну або більше фішок у звичайному режимі.
	 
	 */
	public int mobHeuristic(Color color) {
		ArrayList<Move> moves = this.generateAllMoves(color);
		return moves.size();
	}
	
	/*
	Параметру зараховується 1 для кожного з наступних квадратів: 
	11, 12, 15, 16, 20, 21, 24 і 25, 
	які зайняті пасивним королем."
	 */
	public int kcentHeuristic(Color color) {
		int sum = 0;
		int[] locations = {11, 12, 15, 16, 20, 21, 24, 25};
		for (int k : locations) {
			Piece p = this.getPiece(this.samuelMapping(k));
			if (p != null && p.getColor() == color 
					&& p.getType() == Type.KING && !isActive(p))
				++sum;
		}
		return sum;
	}
	
	/*
	 Параметр приписується 1 для кожної пасивної людини, яка повністю оточена порожніми квадратами."
	 */
	public int poleHeuristic(Color color) {
		int sum = 0;
		for (int i = 0; i < GameConstants.BOARD_SIZE; ++i) {
			for (int j = 0; j < GameConstants.BOARD_SIZE; ++j) {
				Piece p = this.representation[i][j];
				if (p != null) {
					if (p.getType() == Type.NORMAL &&
							!isActive(p) &&
							emptySquaresSurrounding(p) == 4) {
						++sum;
					}
				}
			}
		}
		return sum;
	}
	
	public int getHeuristic(Color color) {
		/* Королі важать більше, тому розраховуємо на них двічі */
		int heuristic = 0;
		if (GameConstants.PIECE_DIFFERENTIAL) {
			heuristic += pieceDifferentialHeuristic(color)*GameConstants.PIECE_DIFFERENTIAL_WEIGHT;
		}
		if (GameConstants.APEX) {
			heuristic += apexHeuristic(color)*GameConstants.APEX_WEIGHT;
		}
		if (GameConstants.BACK) {
			heuristic += backHeuristic(color)*GameConstants.BACK_WEIGHT;
		}
		if (GameConstants.CENT) {
			heuristic += centHeuristic(color)*GameConstants.CENT_WEIGHT;
		}
		if (GameConstants.MOB) {
			heuristic += mobHeuristic(color)*GameConstants.MOB_WEIGHT;
		}
		if (GameConstants.POLE) {
			heuristic += poleHeuristic(color)*GameConstants.POLE_WEIGHT;
		}
		if (GameConstants.KCENT) {
			heuristic += kcentHeuristic(color)*GameConstants.KCENT_WEIGHT;
		}
		
		return heuristic;
	}
	
	public Piece getPiece(Location location) {
		return representation[location.row][location.column];
	}
	
	public Piece[][] getRepresentation() {
		return this.representation;
	}
	
	public int getMovesSinceCapture() {
		return this.movesSinceCapture;
	}
	
	public Piece getLastPieceMoved() {
		return this.lastPieceMoved;
	}
	public Move getLastMove() {
		return this.lastMove;
	}
	
	public int getWhitePieces() {
		return this.whitePieces;
	}
	
	public int getBlackPieces() {
		return this.blackPieces;
	}
}

