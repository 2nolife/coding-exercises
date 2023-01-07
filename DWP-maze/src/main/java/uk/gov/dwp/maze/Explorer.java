package uk.gov.dwp.maze;

import uk.gov.dwp.maze.util.Direction;
import uk.gov.dwp.maze.util.Location;
import uk.gov.dwp.maze.util.Step;
import uk.gov.dwp.maze.util.WallType;

import java.util.*;
import java.util.function.Consumer;

public class Explorer {

  private Maze maze;
  private Location location;
  private int forwardMovesCount;
  private List<Location> moveHistory;

  public void setMaze(Maze maze) {
    this.maze = maze;
    var point = maze.getStartLocation();
    location = new Location(point.x, point.y, Direction.NORTH);
    moveHistory = new ArrayList<>();
    moveHistory.add(location);
  }

  public Location getLocation() {
    return location;
  }

  public void turnLeft() {
    turnLeftOrRight(true);
  }

  public void turnRight() {
    turnLeftOrRight(false);
  }

  private void turnLeftOrRight(boolean left) {
    Direction newDir;
    switch (location.direction) {
      case NORTH: newDir = left ? Direction.WEST : Direction.EAST; break;
      case WEST: newDir = left ? Direction.SOUTH : Direction.NORTH; break;
      case SOUTH: newDir = left ? Direction.EAST : Direction.WEST; break;
      default: newDir = left ? Direction.NORTH : Direction.SOUTH;
    }
    location = new Location(location.x, location.y, newDir);
  }

  private Step stepInDirection(Direction direction) {
    int dx = 0, dy = 0;
    switch (direction) {
      case NORTH: dy = -1; break;
      case WEST: dx = -1; break;
      case SOUTH: dy = 1; break;
      default: dx = 1;
    }
    return new Step(dx, dy, maze.atCoord(location.x+dx, location.y+dy));
  }

  public void moveForward() {
    var step = stepInDirection(location.direction);
    if (step.wallType != WallType.WALL) {
      location = new Location(location.x + step.dx, location.y + step.dy, location.direction);
      forwardMovesCount++;
      moveHistory.add(location);
    }
  }

  public WallType lookForward() {
    return stepInDirection(location.direction).wallType;
  }

  public Set<Direction> canMoveInDirections() {
    Set<Direction> movers = new HashSet<>();
    Consumer<Direction> canMove = direction -> {
      if (stepInDirection(direction).wallType != WallType.WALL) movers.add(direction);
    };
    Arrays.stream(Direction.values()).forEach(canMove);
    return movers;
  }

  public int getForwardMovesCount() {
    return forwardMovesCount;
  }

  public List<Location> moveHistory() {
    return List.copyOf(moveHistory);
  }
}
