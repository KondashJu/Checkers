package view;

import java.awt.Font;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import model.Color;
import model.Location;

/*
 * Представляє квадрат або клітинку на шаховій дошці. 
 * Квадрат може містити шашку.
 */
@SuppressWarnings("serial")
public class Square extends JPanel {

	
	private final Location location;

	private boolean hasPiece;

	private Color pieceColor;


	private boolean selected;

	private boolean valid;
	
	private boolean king;



	public Square(Color color, Location location) {
		super();
		this.location = location;
		this.selected = false;
		this.king = false;
		this.valid = false;
		initSquare(color);
	}



	private void initSquare(Color color) {
		if (color == Color.BLACK) {
			this.setBackground(java.awt.Color.BLACK);
		} else if (color == Color.WHITE) {
			// Колір коричнових квадратів
			this.setBackground(new java.awt.Color(132, 88, 45));
		}
	}


	
	public Location getCellLocation() {
		return location;
	}



	public boolean isSelected() {
		return this.selected;
	}


	public void setSelected(boolean val) {
		if (val) {
			this.setBorder(BorderFactory.createLineBorder(java.awt.Color.GREEN));
		} else if (valid){
			this.setBorder(BorderFactory.createLineBorder(java.awt.Color.YELLOW));
		} else {
			this.setBorder(null);
		}
		this.selected = val;
	}

	public void setValid(boolean state) {
		this.valid = state;
	}

	public boolean isValid() {
		return valid;
	}


	public boolean hasPiece() {
		return hasPiece;
	}
	
	public void promotePiece() {
		this.king = true;
	}
	
	public boolean isKing() {
		return king;
	}


	public void highlight() {
		this.setBorder(BorderFactory.createLineBorder(java.awt.Color.RED));

	}

	 
	@Override
	protected void paintComponent(Graphics g) {
		
		
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		if (hasPiece) {
			
			RenderingHints hints = new RenderingHints(null);
			hints.put(RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON);
			hints.put(RenderingHints.KEY_INTERPOLATION, 
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			hints.put(RenderingHints.KEY_RENDERING, 
					RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHints(hints);

			// Кольори
			if (this.pieceColor == Color.WHITE) {
				// Колір фішок компа
				g2.setColor(new java.awt.Color(0xD7D7D8));
				g2.fillOval(5, 5, getSize().width-10,getSize().height-10);
				g2.setColor(java.awt.Color.BLACK);
				if (king) {
					g2.setFont(new Font("Courier", Font.PLAIN, 24));
					g2.drawString("KING", this.getWidth()/2 - 26, this.getHeight()/2 + 6);
				}
			} else if (this.pieceColor == Color.BLACK) {
				// Колір фішок користувача
				g2.setColor(new java.awt.Color(200, 0, 0));
				g2.fillOval(5, 5, getSize().width-10,getSize().height-10);
				g2.setColor(java.awt.Color.WHITE);
				if (king) {
					// Фішка-дамка
					g2.setFont(new Font("Courier", Font.PLAIN, 24));
					g2.drawString("KING", this.getWidth()/2 - 26, this.getHeight()/2 + 6);
				}
			}
			
		}
	}

	public void dehighlight() {
		this.setBorder(null);
	}

	public Color getPieceColor() {
		return pieceColor;
	}

	public void placePiece(Color color) {
		this.hasPiece = true;
		this.pieceColor = color;
	}

	public Color removePiece() {
		this.hasPiece = false;
		Color color = this.pieceColor;
		this.pieceColor = null;
		this.king = false;
		return color;
	}


	public void setKing(boolean king) {
		this.king = king;
	}


}
