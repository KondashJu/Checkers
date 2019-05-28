package view;

import java.awt.event.KeyEvent;


import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import controller.Game;
import controller.GameConstants;
import player.HumanPlayer;

/*
 * Вікно JFrame, яке містить графічні компоненти гри
 *
 */
@SuppressWarnings("serial")
public class CheckersWindow extends JFrame {
	/* Константи значень розміру вікна */
	public static final int HEIGHT = 868;
	public static final int WIDTH = 800;
	
	/*
	  Екземпляр {@link GamePanel}, який містить всі візуальні компоненти гри.
	 */
	private GamePanel gamePanel;
	
	/*
	 * Екземпляр {@link Game Event Listener), який буде продивлятися всі компоненти ігрової панелі
	 */
	private GameEventListener gameListener;
	
	
	public CheckersWindow(Game game, HumanPlayer user, int mode) {
		super("Checkers");
		this.setSize(WIDTH, HEIGHT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		/*Порядок виконання */
		this.initGameListener();
		this.createMenuBar();
		this.initGamePanel(game, user, mode);
		this.pack();
	}
	
	/*
	 * Відкриває вікно, встановлюючи видимість кадру
	 */
	public void open() {
		this.setVisible(true);
	}

	private void initGameListener() {
		this.gameListener = new GameEventListener();
	}

	/*
	 * Ініціалізує меню і всі його елементи
	 */
	private void createMenuBar() {
		JMenuBar menubar = new JMenuBar(); 
		JMenu file = new JMenu("File");
		/* Нова гра */
		JMenuItem newGame = new JMenuItem("New game");
		newGame.setMnemonic(KeyEvent.VK_N);
		newGame.setToolTipText("Start a new game");
		newGame.addActionListener(gameListener);
		
		/* Вихід */
		JMenuItem quit = new JMenuItem("Quit");
		quit.setMnemonic(KeyEvent.VK_Q);
		quit.setToolTipText("Exit application");
		quit.addActionListener(gameListener);
		
		/* Інструкції */
		JMenuItem instructions = new JMenuItem("Instructions");
		instructions.setMnemonic(KeyEvent.VK_I);
		instructions.setToolTipText("How to play");
		instructions.addActionListener(gameListener);
		
		/* Додати елементи до меню */
		file.add(quit);
		file.add(newGame);
		file.add(instructions);
		
		menubar.add(file);
		menubar.setVisible(true);
		this.setJMenuBar(menubar);	
	}
	

	private void initGamePanel(Game game, HumanPlayer user, int mode) {
		this.gamePanel = new GamePanel(game, user, gameListener);
		if (mode == GameConstants.SERVER_MODE) {
			gamePanel.disableInteraction();
		} else {
			gamePanel.enableInteraction();
		}
		game.setGamePanel(this.gamePanel);
		this.getContentPane().add(this.gamePanel);
		this.gameListener.setGamePanel(gamePanel);
	}
	
}
