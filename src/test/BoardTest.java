package test;
import model.Board;

public class BoardTest {
	
	public static void movementTest() {
		Board b = new Board();

	}
	
	public static void printTest() {
		Board b = new Board();
		b.print();
	}
	
	public static void samuelMappingTest() {
		Board b = new Board();
		for(int i = 1; i <= 32; ++i) {
			System.out.println(i + " : " + b.samuelMapping(i));
		}
	}

	public static void main(String[]args) {
		samuelMappingTest();
	}
}

