package player;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import controller.Game;
import controller.GameConstants;
import model.Board;
import model.Color;
import model.Move;
import controller.Communication;

public class ServerPlayer extends Player {
	private Game game;
	private static String IP = "127.0.0.1";
	private static int PORT = 3000;
	private Socket _socket = null;
    private PrintWriter _out = null;
    private BufferedReader _in = null;
    
	
	public ServerPlayer(Color color, Board board, Game game) {
		super(color, board);
		this.game = game;
	}
	
	public ArrayList<Move> makeMove(ArrayList<Move> moves) {
		//�� ����� ����������� ����, ���� ���� �������
		for (Move move : moves){
			game.requestMove(move);
		}
		
		ArrayList<Move> computerMoves = new ArrayList<Move>();
		
		/* ���� ����� */
		Move computerMove = game.makeComputerMove();
		
		if (moves != null) {
			computerMoves.add(computerMove);
		}
		
		/* ���� ���� �������� */
		while (game.getComputer().isInJumpSequence()) {
			computerMove = game.makeComputerMove();
			if (moves != null) {
				computerMoves.add(computerMove);
			}
		}
		
		/* ������� ������ ����, �� ������ ���� */
		return computerMoves;
	}
	
	public void listen(String name, String pass, String opponent) {
		_socket = openSocket();
		ArrayList<Move> moves;
		ArrayList<Move> computerMoves = new ArrayList<Move>();
		try{
			readAndEcho(); //��������� �����������
			readAndEcho(); //������ �����������
			writeMessageAndEcho(name); //³�������� ��� �����������
			readAndEcho(); //������ ������
			writeMessageAndEcho(pass); //³�������� ������
			readAndEcho(); //������ ��������
			writeMessageAndEcho(opponent); //³�������� ��������
			System.out.println("Waiting on opponent: " + opponent);
			String gameID = (readAndEcho().substring(5,9)); //Game ID
			String selectedColor = readAndEcho().substring(6,11); //����
			System.out.println("I am playing as "+selectedColor+" in game number "+ gameID);
			if (selectedColor.equalsIgnoreCase("white")){ //������ �������
				this.setColor(color.BLACK); 
				game.getComputer().setColor(color.WHITE);
				//������� �� ��������� �����
			}
			else{
				this.setColor(color.WHITE);
				game.getComputer().setColor(color.BLACK);
				GameConstants.THUNK_COLOR = Color.BLACK;
				GameConstants.THUNK_COLOR = Color.WHITE;
				computerMoves.add(game.makeComputerMove()); 
				String moveString = Communication.moveToString(computerMoves); 
				readAndEcho(); 
				writeMessageAndEcho(moveString); 
				readAndEcho();
			}
								
			boolean over = false;
			//�������� ���� �����������, ���� ����� �����
			while(!over) { //���� ��� �� ��������
				String moveMsg = readAndEcho(); 
				if (moveMsg.contains("Result")){
					over = true; 
				}
				else {
					moves = Communication.stringToMove(moveMsg); //������� �����
					computerMoves = makeMove(moves);			 //������� ���� � ���� �����
					String moveString = Communication.moveToString(computerMoves);
					readAndEcho(); 
					writeMessageAndEcho(moveString); 
					readAndEcho(); 
				}
					
			}
		}
		catch (Exception e){
			System.out.println("Exception occured:\n" + e);
			System.exit(1);
		}
	}

    public String readAndEcho() throws IOException
    {
    	String readMessage = _in.readLine();
    	System.out.println("read: "+readMessage);
    	return readMessage;
    }

    public void writeMessage(String message) throws IOException
    {
    	_out.print(message+"\r\n");  
    	_out.flush();
    }
 
    public void writeMessageAndEcho(String message) throws IOException
    {
    	_out.print(message+"\r\n");  
    	_out.flush();
    	System.out.println("sent: "+ message);
    }
			       
    public  Socket openSocket(){
	//��������� �����-�'�������
	try{
		_socket = new Socket(IP, PORT);
		_out = new PrintWriter(_socket.getOutputStream(), true);
		_in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
     } catch (UnknownHostException e) {
    	 System.out.println("Unknown host: " + IP);
    	 System.exit(1);
     } catch  (IOException e) {
    	 System.out.println("No I/O");
    	 System.exit(1);
     }
     return _socket;
  }
}
