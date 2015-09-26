import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Date: 20/9/2015
 * Time: 11:33 PM
 */
public class ConwayGame {

    private static final int REMAIN = 2;
    private static final int REPRODUCE = 3;
    private static final Random RANDOM = new Random();

    private boolean[][] currentGeneration;
    private Point range;
    private int generation;
    private boolean finished;

    public ConwayGame(final Point range) {
        this.range = range;
    }

    public boolean[][] next() {
        if (finished) return currentGeneration.clone();

        if (generation == 0) {
            currentGeneration = createGeneration(range.x, range.y);
            generation++;
            return currentGeneration;
        }

        int[][] liveCells = calculateLiveCells(currentGeneration);
        boolean[][] nextGeneration = new boolean[range.x][range.y];

        retainCells(currentGeneration, nextGeneration, liveCells);
        reproduceCells(nextGeneration, liveCells);

        if (isEqual(currentGeneration, nextGeneration)) {
            finished = true;
            return nextGeneration;
        }

        generation++;
        currentGeneration = nextGeneration;
        return nextGeneration;
    }

    public int getGeneration() {
        return generation;
    }

    public boolean isFinished() {
        return finished;
    }

    private boolean[][] createGeneration(int x, int y) {
        boolean[][] map = new boolean[x][y];

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                map[i][j] = RANDOM.nextBoolean();
            }
        }

        return map;
    }

    int[][] calculateLiveCells(boolean[][] cells) {
        final int x = cells.length;
        final int y = cells[0].length;
        final Point range = new Point(x, y);
        int[][] liveCells = new int[x][y];

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                List<Point> points = getNearbyPoints(range, new Point(i, j));
                int totalAlive = 0;

                for (Point point : points) {
                    if (cells[point.x][point.y]) totalAlive++;
                }

                liveCells[i][j] = totalAlive;
            }
        }

        return liveCells;
    }

    List<Point> getNearbyPoints(Point range, Point current) {
        ArrayList<Point> points = new ArrayList<>(8);

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int x = current.x + i;
                int y = current.y + j;
                if (x >= 0 && x < range.x && y >= 0 && y < range.y) {
                    points.add(new Point(x, y));
                }
            }
        }

        return points;
    }

    void retainCells(boolean[][] currentGeneration, boolean[][] nextGeneration, int[][] liveCells) {
        final int x = liveCells.length;
        final int y = liveCells[0].length;

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (liveCells[i][j] == REMAIN) {
                    nextGeneration[i][j] = currentGeneration[i][j];
                }
            }
        }
    }

    void reproduceCells(boolean[][] nextGeneration, int[][] liveCells) {
        final int x = liveCells.length;
        final int y = liveCells[0].length;

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (liveCells[i][j] == REPRODUCE) {
                    nextGeneration[i][j] = true;
                }
            }
        }
    }

    boolean isEqual(boolean[][] array1, boolean[][] array2) {
        int length = array1.length;
        for (int i = 0; i < length; i++) {
            if (!Arrays.equals(array1[i], array2[i])) return false;
        }
        return true;
    }

    public static void debug(boolean[][] generation, String header) {
        System.out.println(header);

        for (boolean[] row : generation) {
            for (boolean alive : row) {
                System.out.print(alive ? "1 " : "0 ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        ConwayGame game = new ConwayGame(new Point(25, 25));

        for (int i = 0; i < 100; i++) {
            if (game.isFinished()) break;
            boolean[][] map = game.next();
            debug(map, "Generation " + game.getGeneration());
        }
    }
}
