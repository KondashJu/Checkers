package player;

import java.util.ArrayList;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import controller.GameConstants;
import model.Board;
import model.Color;
import model.Location;
import model.Move;

public class ComputerPlayer extends Player {

	public ComputerPlayer(Color color, Board board) {
		super(color, board);
	}

	public Move getMove() {
		Move move = getMinimaxMove(GameConstants.MAX_SEARCH_DEPTH, inJumpSequence);
		if (move == null) {
			inJumpSequence = false;
		} else if (board.movePromotesPiece(move)) {
			inJumpSequence = false;
		} else if (move.isJump()) {
			inJumpSequence = true;
		}
		return move;
	}

	private Move getMinimaxMove(int depth, boolean inJumpSequence) {
		ArrayList<Board> boardFrontier = null;
		ArrayList<Move> moveFrontier = null;
		ArrayList<Integer> moveScores = new ArrayList<Integer>();

		if (inJumpSequence) {
			/* Створення контуру для тієї фішки, яка рухається */
			boardFrontier = board.generateJumpFrontierForPiece(board.getLastPieceMoved());
			moveFrontier = board.generateJumpMovesForPiece(board.getLastPieceMoved());

			if (boardFrontier.isEmpty()) {
				return null;
			}

		} else {
			/* Створення кордону для всіх фішок */
			boardFrontier = board.generateFrontier(GameConstants.THUNK_COLOR);
			moveFrontier = board.generateAllMoves(GameConstants.THUNK_COLOR);
		}

		Color nextColor;
		/* Визначення наступного кольору для переміщення */
		if (inJumpSequence) {
			nextColor = GameConstants.THUNK_COLOR;
		} else if (GameConstants.THUNK_COLOR == Color.BLACK) {
			nextColor = Color.WHITE;
		} else {
			nextColor = Color.BLACK;
		}

		/* розрахунок мінімаксної оцінки для кожного квадрату */
		for (Board b : boardFrontier) {
			moveScores.add(this.getMinimaxScore(nextColor, b, depth, inJumpSequence));
		}

		/* Визначається максимальна мінімаксна оцінка і хід, що до цього привів */
		int maxScore = Integer.MIN_VALUE;
		Move bestMove = null;

		for (int i = 0; i < moveScores.size(); ++i) {
			
			if (moveScores.get(i) > maxScore) {
				
				bestMove = moveFrontier.get(i);
				maxScore = moveScores.get(i);
			}

			System.out.println(moveFrontier.get(i) + " --> " + moveScores.get(i));
		}

		/* Всі ходи мають однаковий результат */
		if (!moveScores.isEmpty() && maxScore == Collections.min(moveScores)) {
			return getBestOfSimilarMoves(moveFrontier);
		}
		System.out.println("Choosing: " + bestMove);

		return bestMove;
	}

	private int getMinimaxScore(Color color, Board b, int depth, boolean inJumpSequence) {
		ArrayList<Board> boardFrontier;
		ArrayList<Integer> moveScores = new ArrayList<Integer>();
		Color otherColor = (color == Color.BLACK ? Color.WHITE : Color.BLACK);

		if (depth == 0) {
			return b.getHeuristic(otherColor);
		}
		
		if (inJumpSequence) {
			/* Створення границі для тої фішки, яка тільки що перемістилася */
			boardFrontier = b.generateJumpFrontierForPiece(b.getLastPieceMoved());
			/* Якщо немає прижка, то потрібно вийти */
			if (boardFrontier.isEmpty()) {
				inJumpSequence = false;
				return getMinimaxScore(otherColor, b, depth-1, inJumpSequence);
			}
		} else {
			/* Створення границі для всіх фішок */
			boardFrontier = b.generateFrontier(color);
		}

		/* Якщо досягнута максимальна глибина або кінцевий стан гри */
		if (b.getBlackPieces() == 0 || b.getWhitePieces() == 0
				|| boardFrontier.size() == 0) {
			return b.getHeuristic(otherColor);
		}

		Color nextColor;
		/* Наступний колір для зміщення */
		if (inJumpSequence) {
			nextColor = color;
		} else {
			nextColor = otherColor;
		}

		for (Board board : boardFrontier) {
			int moveScore = getMinimaxScore(nextColor, board, depth - 1, inJumpSequence);
			moveScores.add(moveScore);
		}

		if (color == GameConstants.THUNK_COLOR) {
			// Мінімізувати по рахунку противника
			return Collections.min(moveScores);
		}
		else {
			//Якщо противник максимізує свій рахунок
			return Collections.max(moveScores);
		}

	}

	private Move getBestOfSimilarMoves(ArrayList<Move> moves) {
		ArrayList<Move> regularMoves = new ArrayList<Move>();

		for (Move move : moves) {
			if (!move.isJump()) {
				regularMoves.add(move);
			}
		}

		/* Зміщувати звичайні шашки, а не королів, якщо всі ходи мають 
		 * однаковий результат
		 */
		if (!regularMoves.isEmpty()) {
			return regularMoves.get(ThreadLocalRandom.current().nextInt(regularMoves.size()));
		}

		return moves.get(ThreadLocalRandom.current().nextInt(moves.size()));
	}



}
