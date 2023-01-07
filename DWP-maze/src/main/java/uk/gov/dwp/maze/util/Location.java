package uk.gov.dwp.maze.util;

public class Location {

  public final int x;
  public final int y;
  public final Direction direction;

  public Location(int x, int y, Direction direction) {
    this.x = x;
    this.y = y;
    this.direction = direction;
  }

  @Override
  public boolean equals(Object obj) {
    var other = (Location) obj;
    return x == other.x && y == other.y && direction == other.direction;
  }

  @Override
  public String toString() {
    return String.format("(%s,%s) facing %s", x, y, direction);
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }
}
