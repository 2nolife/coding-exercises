package uk.gov.dwp.maze.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class MazeMatrix {

  private WallType[][] matrix;
  private int numberOfWalls;
  private int numberOfEmptySpaces;
  private Point startLocation;
  private Set<Point> exitLocations = new HashSet<>();

  public MazeMatrix(List<String> matrixData) {
    matrix = matrixData.stream()
            .map(dataStr -> dataStr.chars().mapToObj(i -> (char)i).map(WallType::fromCharacter))
            .collect(new MatrixCollector());
    for (int y = 0; y < matrix.length; y++)
      for (int x = 0; x < matrix[0].length; x++)
        switch (matrix[y][x]) {
          case WALL: numberOfWalls++; break;
          case EMPTY: numberOfEmptySpaces++; break;
          case START: startLocation = new Point(x ,y); break;
          case EXIT: exitLocations.add(new Point(x, y)); break;
        }
  }

  private class MatrixCollector implements Collector<Stream<WallType>, List<List<WallType>>, WallType[][]> {
    @Override
    public Supplier<List<List<WallType>>> supplier() {
      return ArrayList::new;
    }

    @Override
    public BiConsumer<List<List<WallType>>, Stream<WallType>> accumulator() {
      return (list, stream) -> list.add(stream.collect(toList()));
    }

    @Override
    public BinaryOperator<List<List<WallType>>> combiner() {
      return (list1, list2) -> {
        list1.addAll(list2);
        return list1;
      };
    }

    @Override
    public Function<List<List<WallType>>, WallType[][]> finisher() {
      return list -> {
        var m = new WallType[list.size()][list.get(0).size()];
        for (int y = 0; y < m.length; y++)
          for (int x = 0; x < m[0].length; x++)
            m[y][x] = list.get(y).get(x);
        return m;
      };
    }

    @Override
    public Set<Characteristics> characteristics() {
      return Set.of(Characteristics.UNORDERED);
    }
  }

  public WallType atCoord(int x, int y) {
    return matrix[y][x];
  }

  public int getNumberOfWalls() {
    return numberOfWalls;
  }

  public int getNumberOfEmptySpaces() {
    return numberOfEmptySpaces;
  }

  public Point getStartLocation() {
    return startLocation;
  }

  public Set<Point> getExitLocations() {
    return exitLocations;
  }
}
