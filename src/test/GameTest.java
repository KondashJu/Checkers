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
		
		//Потрібно налаштувати зовнішній вигляд, щоб це було кроссплатформеним 
		try {
			
			UIManager.setLookAndFeel(
					UIManager.getCrossPlatformLookAndFeelClassName());
		} 
		catch (UnsupportedLookAndFeelException e) {
			/* Виняток, який вказує на те, що запитувані класи управління 
			 * зовнішнім виглядом відсутні в системі користувача.
			 */
		}
		catch (ClassNotFoundException e) {
			/* Виняток, який вказує на те, що JVM намагається
			 * загрузити певний клас, якого немає в classpath
			 */
		}
		catch (InstantiationException e) {
			/* Додаток намагається створити екземпляр класу, 
			 * використовуючи newInstance метод в класі Class, але зазначений об'єкт класу
			 * не може бути створений
			 */
		}
		catch (IllegalAccessException e) {
			/* Виняток, який вказує на те, що JVM намагається передати
			 * методу невірний/невірного типу аргумент
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
