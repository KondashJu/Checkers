package controller;

import java.util.ArrayList;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import model.Board;
import model.Color;
import model.Location;
import model.Move;
import model.Type;
import player.ComputerPlayer;
import player.HumanPlayer;
import player.Player;
import view.GamePanel;

public class Game {
	private ComputerPlayer computer;
	private Player opponent;
	private Board board;
	private GamePanel gamePanel;

	public Game(Board board) {
		this.board = board;
	}

	/* Або сервер, або користувач запитують переміщення */
	public void requestMove(Move move) {

		board.movePiece(move);
		gamePanel.movePiece(move);
		checkEndGame();
	}
	
	public Move makeComputerMove() {
		
		Move computerMove = null;
		
		if (!opponent.isInJumpSequence()) {

			computerMove = computer.getMove();
			if (computerMove == null) {
				gamePanel.displayMessage("Game over. User has won!");
				gamePanel.disableInteraction();
			} else {
				board.movePiece(computerMove);
				gamePanel.movePiece(computerMove);
			}
		}
		
		return computerMove;
	}
	
	public void checkEndGame() {
		if (board.getMovesSinceCapture() > GameConstants.MAX_PASSIVE_MOVES) {
			gamePanel.displayMessage("It's a tie! Be more aggressive next time.");
			gamePanel.disableInteraction();
		}
	}

	public void notifyClientWin() {
		gamePanel.displayMessage("Game over. Client has won!");
	}

	public void setGamePanel(GamePanel panel) {
		this.gamePanel = panel;
	}

	public void setComputer(ComputerPlayer computer) {
		this.computer = computer;
	}
	
	public void setOpponent(Player opponent) {
		this.opponent = opponent;
	}

	public Player getComputer() {
		return computer;
	}
	
	

}
