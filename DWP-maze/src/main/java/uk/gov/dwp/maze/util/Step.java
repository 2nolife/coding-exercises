package uk.gov.dwp.maze.util;

public class Step {

  public final int dx;
  public final int dy;
  public final WallType wallType;

  public Step(int dx, int dy, WallType wallType) {
    this.dx = dx;
    this.dy = dy;
    this.wallType = wallType;
  }
}
