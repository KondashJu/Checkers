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
 * ³��� JFrame, ��� ������ ������� ���������� ���
 *
 */
@SuppressWarnings("serial")
public class CheckersWindow extends JFrame {
	/* ��������� ������� ������ ���� */
	public static final int HEIGHT = 868;
	public static final int WIDTH = 800;
	
	/*
	  ��������� {@link GamePanel}, ���� ������ �� ������� ���������� ���.
	 */
	private GamePanel gamePanel;
	
	/*
	 * ��������� {@link Game Event Listener), ���� ���� ������������ �� ���������� ������ �����
	 */
	private GameEventListener gameListener;
	
	
	public CheckersWindow(Game game, HumanPlayer user, int mode) {
		super("Checkers");
		this.setSize(WIDTH, HEIGHT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		/*������� ��������� */
		this.initGameListener();
		this.createMenuBar();
		this.initGamePanel(game, user, mode);
		this.pack();
	}
	
	/*
	 * ³������ ����, ������������ �������� �����
	 */
	public void open() {
		this.setVisible(true);
	}

	private void initGameListener() {
		this.gameListener = new GameEventListener();
	}

	/*
	 * �������� ���� � �� ���� ��������
	 */
	private void createMenuBar() {
		JMenuBar menubar = new JMenuBar(); 
		JMenu file = new JMenu("File");
		/* ���� ��� */
		JMenuItem newGame = new JMenuItem("New game");
		newGame.setMnemonic(KeyEvent.VK_N);
		newGame.setToolTipText("Start a new game");
		newGame.addActionListener(gameListener);
		
		/* ����� */
		JMenuItem quit = new JMenuItem("Quit");
		quit.setMnemonic(KeyEvent.VK_Q);
		quit.setToolTipText("Exit application");
		quit.addActionListener(gameListener);
		
		/* ���������� */
		JMenuItem instructions = new JMenuItem("Instructions");
		instructions.setMnemonic(KeyEvent.VK_I);
		instructions.setToolTipText("How to play");
		instructions.addActionListener(gameListener);
		
		/* ������ �������� �� ���� */
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
