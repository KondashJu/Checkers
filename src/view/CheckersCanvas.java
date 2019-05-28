package view;

import java.awt.Dimension;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;

import javax.swing.JPanel;

import model.Location;
import model.Move;
import model.Color;

/*
 * Холст, на якому намальована дошка для шашок
 */
@SuppressWarnings("serial")
public class CheckersCanvas extends JPanel {

	public static final int BOARD_DIM = 8;
	public static final int CANVAS_SIZE = 800;

	private Square[][] board;


	GridBagConstraints layoutConstraints;

	public CheckersCanvas(GameEventListener boardListener) {
		super(new GridLayout(BOARD_DIM, BOARD_DIM));
		this.board = new Square[BOARD_DIM][BOARD_DIM];

		this.setPreferredSize(new Dimension(CANVAS_SIZE, CANVAS_SIZE));
		this.setMaximumSize(new Dimension(CANVAS_SIZE, CANVAS_SIZE));
		this.setMinimumSize(new Dimension(CANVAS_SIZE, CANVAS_SIZE));

		this.initCanvas(boardListener);
	}

	private void initCanvas(GameEventListener boardListener) {
		initSquares(boardListener);
		initCheckers();
	}

	private void initSquares(GameEventListener boardListener) {
		for (int i = 0; i < BOARD_DIM; ++i) {
			if (i % 2 != 0) {
				for (int j = 0; j < BOARD_DIM/2; ++j) {
					/* Чорний квадрат */
					Square blackSquare = new Square(Color.BLACK, new Location(i, j*2));
					board[i][j*2] = blackSquare;
					blackSquare.addMouseListener(boardListener);
					this.add(blackSquare);

					/* Білий квадрат */
					Square whiteSquare = new Square(Color.WHITE, new Location(i, j*2 + 1));
					board[i][j*2 + 1] = whiteSquare;
					whiteSquare.addMouseListener(boardListener);
					this.add(whiteSquare);
				}
			} else {
				for (int j = 0; j < BOARD_DIM/2; ++j) {
					/* Білий квадрат */
					Square whiteSquare = new Square(Color.WHITE, new Location(i, j*2));
					board[i][j*2] = whiteSquare;
					whiteSquare.addMouseListener(boardListener);
					this.add(whiteSquare);

					/* Чорний квадрат */
					Square blackSquare = new Square(Color.BLACK, new Location(i, j*2 + 1));
					board[i][j*2 + 1] = blackSquare;
					blackSquare.addMouseListener(boardListener);
					this.add(blackSquare);
				}
			}
		}
	}

	private void initCheckers() {
		for (int row = 0; row < BOARD_DIM / 2 - 1; ++row) {
			for (int col = 0; col < BOARD_DIM / 2; ++col) {
				board[BOARD_DIM - 1 - row][2*col+ (row % 2)].placePiece(Color.WHITE);
				board[row][2*col + (BOARD_DIM - 1 - row) %2]
						.placePiece(Color.BLACK);
			}
		}
	}

	public void highlightAndValidateSquare(Location location) {
		board[location.row][location.column].highlight();
		board[location.row][location.column].setValid(true);
	}

	public void dehighlightAndInvalidateSquare(Location location) {
		board[location.row][location.column].dehighlight();
		board[location.row][location.column].setValid(false);
	}

	public void invalidateAllSquares() {
		for (int row = 0; row < BOARD_DIM; ++row) {
			for (int col = 0; col < BOARD_DIM; ++col) {		
				dehighlightAndInvalidateSquare(new Location(row, col));
			}
		}
	}

	public void removePiece(Location location) {
		board[location.row][location.column].removePiece();
	}

	public void movePiece(Move move) {
		if (board[move.source.row][move.source.column].isKing()) {
			board[move.source.row][move.source.column].setKing(false);
			board[move.destination.row][move.destination.column].setKing(true);
		}
		Color color = board[move.source.row][move.source.column].removePiece();
		board[move.destination.row][move.destination.column].placePiece(color);
		this.repaint();
	}
	
	public Square getSquare(Location location) {
		return board[location.row][location.column];
	}
}
