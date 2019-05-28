package controller;

import java.util.ArrayList;


import model.Location;
import model.Move;

/* ³������ �� ����������� �������� ����� �
 * �������� ��������� ��� �� ��������
 *
 */
public class Communication {

	/* �������� �������� ������������� ����������� ���������
	 * � ��'��� Move 
	 * 
	 * @param stringMove �������� ������������� ����������� ���������
	 * @return ����� ��'���� Move, ��������� � ���������� �������������
	 */
	public static ArrayList<Move> stringToMove(String stringMoves) {
		String[] splitMoves = stringMoves.split(":");
		
		ArrayList<Integer> moveIndexes = new ArrayList<Integer>(); 
		
		for (String move : splitMoves) {
			String index = move.replaceAll("[^0-9]", "");
			if (!index.equals("")) {
				moveIndexes.add(Integer.parseInt(index));
			}
		}
		
		ArrayList<Move> moves = new ArrayList<Move>();
		
		for (int i = 0; i < moveIndexes.size() - 3; i+=2) {
			Location from = new Location(GameConstants.BOARD_SIZE - 1 - moveIndexes.get(i), moveIndexes.get(i+1));
			Location to = new Location(GameConstants.BOARD_SIZE - 1 - moveIndexes.get(i+2), moveIndexes.get(i+3));
			Move move = new Move(from, to);
			moves.add(move);
		}
		
		return moves;
		
	}
	
	/* �������� ����� ��'���� Move � �������� �������������, �������� �������
	 * 
	 * @param moves ������ ��'���� Move
	 * @return �������� ������������� Move
	 */
	public static String moveToString(ArrayList<Move> moves) {
		StringBuilder sb = new StringBuilder();
		
		Move firstmove = moves.get(0);
		sb.append("(");
		sb.append(GameConstants.BOARD_SIZE - 1 - firstmove.source.row);
		sb.append(":");
		sb.append(firstmove.source.column);
		sb.append("):(");
		sb.append(GameConstants.BOARD_SIZE - 1 - firstmove.destination.row);
		sb.append(":");
		sb.append(firstmove.destination.column);
		sb.append(")");
		for (int i = 1; i < moves.size(); ++i) {
			Move move = moves.get(i);
			sb.append(":(");
			sb.append(GameConstants.BOARD_SIZE - 1 - move.destination.row);
			sb.append(":");
			sb.append(move.destination.column);
			sb.append(")");
		}
		return sb.toString();
	}
	
	public Communication() {
		// TODO Auto-generated constructor stub
	}

}
