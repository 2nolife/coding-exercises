package uk.gov.dwp.maze;

import org.junit.jupiter.api.Test;
import uk.gov.dwp.maze.util.MazeUtil;
import uk.gov.dwp.maze.util.MazeUtilException;
import uk.gov.dwp.maze.util.Point;
import uk.gov.dwp.maze.util.WallType;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class MazeTest {

  private Explorer mockExplorer = mock(Explorer.class);
  private Maze.Builder mazeBuilder = new Maze.Builder();

  private Stream<String> loadMazeData() throws MazeUtilException {
    return MazeUtil.loadMaze("Maze1.txt");
  }

  @Test
  public void maze_is_created_from_builder() throws Exception {
    var exception = assertThrows(IllegalArgumentException.class, () -> mazeBuilder.build());
    assertEquals("data or explorer cannot be null", exception.getMessage());
    var maze = mazeBuilder.data(loadMazeData()).explorer(mockExplorer).build();
    assertInstanceOf(Maze.class, maze);
    assertNotNull(maze);
  }

  @Test
  public void maze_contains_valid_symbols() throws Exception {
    var emptyDataException = assertThrows(IllegalArgumentException.class, () -> mazeBuilder.explorer(mockExplorer).data(Stream.empty()).build());
    assertEquals("data is empty or contains illegal character", emptyDataException.getMessage());
    var illegalDataException = assertThrows(IllegalArgumentException.class, () -> mazeBuilder.explorer(mockExplorer).data(Stream.concat(loadMazeData(), Stream.of("O"))).build());
    assertEquals("data is empty or contains illegal character", illegalDataException.getMessage());
  }

  /** User Story 1:
   *  A Maze (as defined in Maze1.txt) consists of walls 'X', empty spaces ' ', one and only one start location 'S', and at least one exit location 'E'
   */
  @Test
  public void maze_contains_start_end_exit() throws Exception {
    var noStartException = assertThrows(IllegalArgumentException.class, () -> mazeBuilder.explorer(mockExplorer).data(loadMazeData().filter(str -> !str.contains("S"))).build());
    assertEquals("data does not have start location", noStartException.getMessage());
    var twoStartException = assertThrows(IllegalArgumentException.class, () -> mazeBuilder.explorer(mockExplorer).data(Stream.concat(loadMazeData(), Stream.of("S"))).build());
    assertEquals("data has more than one start locations", twoStartException.getMessage());
    var noExitException = assertThrows(IllegalArgumentException.class, () -> mazeBuilder.explorer(mockExplorer).data(loadMazeData().filter(str -> !str.contains("E"))).build());
    assertEquals("data does not have exit location", noExitException.getMessage());
    mazeBuilder.explorer(mockExplorer).data(loadMazeData()).build(); // more than one exit
  }

  /** User Story 1:
   *  After a maze has been created the number of walls, empty spaces and exit locations should be available to me
   */
  @Test
  public void maze_statistics_should_be_as_expected() throws Exception {
    var maze = mazeBuilder.data(loadMazeData()).explorer(mockExplorer).build();
    assertEquals(149, maze.getNumberOfWalls(), "number of walls mismatch");
    assertEquals(74, maze.getNumberOfEmptySpaces(), "number of empty spaces mismatch");
    assertEquals(new Point(3, 3), maze.getStartLocation(), "start location mismatch");
    assertEquals(Stream.of(new Point(1, 14)).collect(toSet()), maze.getExitLocations(), "exit location mismatch");
  }

  /** User Story 1:
   *  After a maze has been created I should be able to put in a co-ordinate and know what exists at that location
   */
  @Test
  public void maze_should_return_type_for_coord() throws Exception {
    var maze = mazeBuilder.data(loadMazeData()).explorer(mockExplorer).build();
    assertEquals(WallType.WALL, maze.atCoord(0, 0));
    assertEquals(WallType.WALL, maze.atCoord(10, 7));
    assertEquals(WallType.EMPTY, maze.atCoord(1, 5));
    assertEquals(WallType.EMPTY, maze.atCoord(9, 6));
    assertEquals(WallType.START, maze.atCoord(3, 3));
    assertEquals(WallType.EXIT, maze.atCoord(1, 14));
  }

}
