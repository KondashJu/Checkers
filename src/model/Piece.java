package model;

public class Piece {
	public final Color color;
	private Location location;
	private Type type;
	
	public Piece(Color color, int row, int col) {
		this.color = color;
		this.location = new Location(row, col);
		this.type = Type.NORMAL;
	}
	
	public Piece(Piece other) {
		this.color = other.getColor();
		this.location = new Location(other.getLocation());
		this.type = other.getType();
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Color opposite() {
		if(this.color.equals(Color.WHITE)) return Color.BLACK;
		if(this.color.equals(Color.BLACK)) return Color.WHITE;
		return null;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void promote() {
		this.type = Type.KING;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public boolean equals(Piece other) {
		return this.color == other.color &&
				this.location.equals(other.getLocation()) &&
				this.type == other.getType();
	}
	
	@Override
	public String toString() {
		return "Piece(" + this.color + ", " + this.location + ", " + this.type + ")";
	}
}
