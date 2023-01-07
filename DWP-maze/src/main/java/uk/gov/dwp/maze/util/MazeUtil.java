package uk.gov.dwp.maze.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MazeUtil {

    public static Stream<String> loadMaze(String filename) throws MazeUtilException {
        try {
            return Files.lines(Paths.get(ClassLoader.getSystemResource(filename).toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new MazeUtilException(e);
        }
    }

    public static String drawPath(List<Location> path) {
        if (path == null || path.isEmpty()) throw new IllegalArgumentException("no moves provided");
        Function<Function<Location, Integer>, IntStream> mapper = m -> path.stream().map(m).mapToInt(i -> i);
        var topX = mapper.apply(Location::getX).min().getAsInt();
        var topY = mapper.apply(Location::getY).min().getAsInt();
        var botX = mapper.apply(Location::getX).max().getAsInt();
        var botY = mapper.apply(Location::getY).max().getAsInt();

        var matrix = new boolean[botY-topY+1][botX-topX+1];
        path.forEach(location -> {
            matrix[location.y-topY][location.x-topX] = true;
        });

        var sb = new StringBuilder();
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++)
                sb.append(matrix[y][x] ? "*" : " ");
            sb.append("\n");
        }
        return sb.toString();
    }
}
