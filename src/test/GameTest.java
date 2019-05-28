package test;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import controller.Game;
import controller.GameConstants;
import model.Board;
import player.ComputerPlayer;
import player.HumanPlayer;
import view.CheckersWindow;

public class GameTest {

	public static void main(String[] args) {
		
		//������� ����������� ������� ������, ��� �� ���� ����������������� 
		try {
			
			UIManager.setLookAndFeel(
					UIManager.getCrossPlatformLookAndFeelClassName());
		} 
		catch (UnsupportedLookAndFeelException e) {
			/* �������, ���� ����� �� ��, �� ��������� ����� ��������� 
			 * ������� �������� ������ � ������ �����������.
			 */
		}
		catch (ClassNotFoundException e) {
			/* �������, ���� ����� �� ��, �� JVM ����������
			 * ��������� ������ ����, ����� ���� � classpath
			 */
		}
		catch (InstantiationException e) {
			/* ������� ���������� �������� ��������� �����, 
			 * �������������� newInstance ����� � ���� Class, ��� ���������� ��'��� �����
			 * �� ���� ���� ���������
			 */
		}
		catch (IllegalAccessException e) {
			/* �������, ���� ����� �� ��, �� JVM ���������� ��������
			 * ������ �������/�������� ���� ��������
			 */
		}
		Board board = new Board();
		final Game game = new Game(board);
		
		ComputerPlayer computer = new ComputerPlayer(GameConstants.THUNK_COLOR, board);
		final HumanPlayer user = new HumanPlayer(GameConstants.USER_COLOR, board, game);
		
		game.setComputer(computer);
		game.setOpponent(user);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				CheckersWindow window = new CheckersWindow(game, user, GameConstants.USER_MODE);
				window.open();
			}
		  }
		);
		
	}
}
