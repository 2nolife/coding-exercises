package uk.gov.dwp.maze;

import uk.gov.dwp.maze.util.MazeMatrix;
import uk.gov.dwp.maze.util.Point;
import uk.gov.dwp.maze.util.WallType;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Maze {

  private MazeMatrix matrix;

  private Maze(List<String> matrixData, Explorer explorer) {
    matrix = new MazeMatrix(matrixData);
  }

  public int getNumberOfWalls() {
    return matrix.getNumberOfWalls();
  }

  public int getNumberOfEmptySpaces() {
    return matrix.getNumberOfEmptySpaces();
  }

  public Point getStartLocation() {
    return matrix.getStartLocation();
  }

  public Set<Point> getExitLocations() {
    return matrix.getExitLocations();
  }

  public WallType atCoord(int x, int y) {
    return matrix.atCoord(x, y);
  }

  public static class Builder {
    private Stream<String> data = null;
    private Explorer explorer = null;

    public Builder data(Stream<String> data) {
      this.data = data;
      return this;
    }

    public Builder explorer(Explorer explorer) {
      this.explorer = explorer;
      return this;
    }

    private long countWallType(List<String> matrixData, WallType wallType) {
      return matrixData.stream().flatMapToInt(String::chars).mapToObj(i -> (char)i).map(WallType::fromCharacter).filter(type -> type == wallType).count();
    }

    public Maze build() {
      if (data == null || explorer == null) throw new IllegalArgumentException("data or explorer cannot be null");
      var matrixData = data.collect(toList());
      var regEx = Pattern.compile(Arrays.stream(WallType.values()).map(WallType::getCharacter).map(String::valueOf).collect(joining("", "[", "]+")));
      if (matrixData.size() == 0 || !matrixData.stream().map(regEx::matcher).allMatch(Matcher::matches)) throw new IllegalArgumentException("data is empty or contains illegal character");
      var startLocations = countWallType(matrixData, WallType.START);
      var exitLocations = countWallType(matrixData, WallType.EXIT);
      if (startLocations == 0) throw new IllegalArgumentException("data does not have start location");
      if (startLocations > 1) throw new IllegalArgumentException("data has more than one start locations");
      if (exitLocations == 0) throw new IllegalArgumentException("data does not have exit location");

      Maze maze = new Maze(matrixData, explorer);
      explorer.setMaze(maze);
      return maze;
    }
  }
}
