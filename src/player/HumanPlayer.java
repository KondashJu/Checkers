package player;

import controller.Game;
import model.Board;
import model.Color;
import model.Move;

public class HumanPlayer extends Player {
	private Game game;
	
	public HumanPlayer(Color color, Board board, Game game) {
		super(color, board);
		this.game = game;
	}
	
	public void makeMove(Move move) {
		/* ���� �� ������ ������ */
		if (move.isJump() && !inJumpSequence) {
			inJumpSequence = true;
		}

		if (board.movePromotesPiece(move)) {
			inJumpSequence = false;
		}
		
		/* �������� ���������� ����� */
		game.requestMove(move);
		
		if(getAvailableMoves(move.destination).isEmpty()) {
			inJumpSequence = false;
		}
		
		/* ճ� ����� */
		game.makeComputerMove();
		
		/* ���� ���� ��������, �� �� ���������� ����� */
		while (game.getComputer().isInJumpSequence()) {
			game.makeComputerMove();
		}
	}
}
