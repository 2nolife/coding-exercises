package uk.gov.dwp.maze;

import org.junit.jupiter.api.Test;
import uk.gov.dwp.maze.util.Location;
import uk.gov.dwp.maze.util.MazeUtil;
import uk.gov.dwp.maze.util.MazeUtilException;
import uk.gov.dwp.maze.util.WallType;

import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.dwp.maze.util.Direction.*;

public class ExplorerTest {

  private Maze createMaze(Explorer explorer) throws MazeUtilException {
    var data = MazeUtil.loadMaze("Maze1.txt");
    return new Maze.Builder().data(data).explorer(explorer).build();
  }

  /** User Story 2
   *  Given a maze the explorer should be able to drop in to the Start location (facing north)
   */
  @Test
  public void explorer_drops_to_maze() throws Exception {
    var explorer = new Explorer();
    createMaze(explorer);
    assertEquals(new Location(3, 3, NORTH), explorer.getLocation());
  }

  /** User Story 2
   *  An explorer on a maze must be able to only turn left or right (changing direction the explorer is facing)
   */
  @Test
  public void explorer_can_turn() throws Exception {
    var explorer = new Explorer();
    createMaze(explorer);
    explorer.turnLeft();
    assertEquals(new Location(3, 3, WEST), explorer.getLocation());
    explorer.turnLeft();
    assertEquals(new Location(3, 3, SOUTH), explorer.getLocation());
    explorer.turnLeft();
    assertEquals(new Location(3, 3, EAST), explorer.getLocation());
    explorer.turnLeft();
    assertEquals(new Location(3, 3, NORTH), explorer.getLocation());
    explorer.turnRight();
    assertEquals(new Location(3, 3, EAST), explorer.getLocation());
    explorer.turnRight();
    assertEquals(new Location(3, 3, SOUTH), explorer.getLocation());
    explorer.turnRight();
    assertEquals(new Location(3, 3, WEST), explorer.getLocation());
    explorer.turnRight();
    assertEquals(new Location(3, 3, NORTH), explorer.getLocation());
  }

  /** User Story 2
   *  An explorer on a maze must be able to move forward
   *  An explorer on a maze must be able to declare what is in front of them
   *  An explorer on a maze must be able to declare all movement options from their given location
   */
  @Test
  public void explorer_can_move_forward() throws Exception {
    var explorer = new Explorer();
    createMaze(explorer);

    explorer.moveForward();
    assertEquals(new Location(3, 3, NORTH), explorer.getLocation());
    assertEquals(WallType.WALL, explorer.lookForward());
    assertEquals(Stream.of(EAST).collect(toSet()), explorer.canMoveInDirections());

    explorer.turnRight();
    explorer.moveForward();
    assertEquals(new Location(4, 3, EAST), explorer.getLocation());
    assertEquals(WallType.EMPTY, explorer.lookForward());
    assertEquals(Stream.of(EAST, WEST).collect(toSet()), explorer.canMoveInDirections());

    range(0, 7).forEach(i -> explorer.moveForward());
    assertEquals(new Location(11, 3, EAST), explorer.getLocation());
    assertEquals(WallType.WALL, explorer.lookForward());
    assertEquals(Stream.of(SOUTH, WEST).collect(toSet()), explorer.canMoveInDirections());

    explorer.turnRight();
    range(0, 3).forEach(i -> explorer.moveForward());
    assertEquals(new Location(11, 6, SOUTH), explorer.getLocation());
    assertEquals(WallType.EMPTY, explorer.lookForward());
    assertEquals(Stream.of(SOUTH, WEST, NORTH).collect(toSet()), explorer.canMoveInDirections());

    explorer.turnRight();
    explorer.moveForward();
    assertEquals(new Location(10, 6, WEST), explorer.getLocation());
    assertEquals(WallType.EMPTY, explorer.lookForward());
    assertEquals(Stream.of(WEST, EAST).collect(toSet()), explorer.canMoveInDirections());
  }

  /** User Story 2
   *  An explorer on a maze must be able to declare the number of times they have moved forward so far
   */
  @Test
  public void explorer_can_count_forward_moves() throws Exception {
    var explorer = new Explorer();
    createMaze(explorer);
    explorer.moveForward();
    assertEquals(0, explorer.getForwardMovesCount());
    explorer.turnRight();
    range(0, 8).forEach(i -> explorer.moveForward());
    assertEquals(8, explorer.getForwardMovesCount());
    explorer.moveForward();
    assertEquals(8, explorer.getForwardMovesCount());
  }

  /** User Story 2
   *  An explorer on a maze must be able to report a record of where they have been in an understandable fashion
   */
  @Test
  public void explorer_can_draw_its_moves_map() throws Exception {
    var explorer = new Explorer();
    createMaze(explorer);
    assertEquals("*\n", MazeUtil.drawPath(explorer.moveHistory()));

    explorer.turnRight();
    range(0, 8).forEach(i -> explorer.moveForward());
    explorer.turnRight();
    range(0, 3).forEach(i -> explorer.moveForward());
    explorer.turnRight();
    range(0, 5).forEach(i -> explorer.moveForward());
    explorer.turnLeft();
    range(0, 3).forEach(i -> explorer.moveForward());
    explorer.turnLeft();
    explorer.turnLeft();
    range(0, 2).forEach(i -> explorer.moveForward());
    assertEquals(21, explorer.getForwardMovesCount());

    var expectedMoveHistory = asList(
      new Location(3, 3, NORTH),
      new Location(4, 3, EAST), new Location(5, 3, EAST), new Location(6, 3, EAST), new Location(7, 3, EAST), new Location(8, 3, EAST), new Location(9, 3, EAST), new Location(10, 3, EAST), new Location(11, 3, EAST),
      new Location(11, 4, SOUTH), new Location(11, 5, SOUTH), new Location(11, 6, SOUTH),
      new Location(10, 6, WEST), new Location(9, 6, WEST), new Location(8, 6, WEST), new Location(7, 6, WEST), new Location(6, 6, WEST),
      new Location(6, 7, SOUTH), new Location(6, 8, SOUTH), new Location(6, 9, SOUTH),
      new Location(6, 8, NORTH), new Location(6, 7, NORTH)
    );
    assertEquals(expectedMoveHistory, explorer.moveHistory());

    String expectedPath =
            "*********\n" +
            "        *\n" +
            "        *\n" +
            "   ******\n" +
            "   *     \n" +
            "   *     \n" +
            "   *     \n";
    assertEquals(expectedPath, MazeUtil.drawPath(explorer.moveHistory()));
  }

}
