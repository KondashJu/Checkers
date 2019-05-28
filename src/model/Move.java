package model;

public class Move {
	public final Location source;
	public final Location destination;
	
	public Move(Location source, Location destination) {
		this.source = source;
		this.destination = destination;
	}

	public boolean isJump() {
		return Math.abs(source.column - destination.column) == 2
				&& Math.abs(source.row - destination.row) == 2;
	}
	
	public boolean equals(Move other) {
		return  this.destination.row 	== other.destination.row &&
				this.destination.column == other.destination.column &&
				this.source.row			== other.source.row &&
				this.source.column		== other.source.column;
	}
	
	@Override
	public String toString() {
		return "From (" + this.source.row + ", " + this.source.column + ")" +
				" to (" + this.destination.row + ", " + this.destination.column + ")";
	}

}
