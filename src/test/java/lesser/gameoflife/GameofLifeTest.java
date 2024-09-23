package lesser.gameoflife;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import static org.junit.jupiter.api.Assertions.*;

public class GameofLifeTest {

    @Test
    public void initialize() {
        GameOfLife game = new GameOfLife(5, 5);
        game.initialize();
        int[][] grid = game.getGrid();
        boolean hasAliveCells = false;
        boolean hasDeadCells = false;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (grid[i][j] == 1) {
                    hasAliveCells = true;
                } else {
                    hasDeadCells = true;
                }
            }
        }

        assertTrue(hasAliveCells, "Grid should have some alive cells after initialization");
        assertTrue(hasDeadCells, "Grid should have some dead cells after initialization");
    }

    @Test
    public void nextGen() {
        GameOfLife game = new GameOfLife(3, 3);
        game.setCell(1, 0, 1);
        game.setCell(1, 1, 1);
        game.setCell(1, 2, 1);

        game.nextGen();
        int[][] grid = game.getGrid();

        assertEquals(1, grid[0][1], "Cell (0,1) should be alive");
        assertEquals(1, grid[1][1], "Cell (1,1) should be alive");
        assertEquals(1, grid[2][1], "Cell (2,1) should be alive");
        assertEquals(0, grid[1][0], "Cell (1,0) should be dead");
        assertEquals(0, grid[1][2], "Cell (1,2) should be dead");
    }

    @Test
    public void setCell() {
        GameOfLife game = new GameOfLife(3, 3);
        game.setCell(1, 1, 1);
        int[][] grid = game.getGrid();

        assertEquals(1, grid[1][1], "Cell (1,1) should be set to alive");
    }

    @Test
    void loadGliderPatternFromUrl() throws IOException {
        // Given
        GameOfLife game = new GameOfLife(10, 10);
        URL url = new URL("https://conwaylife.com/patterns/glider.rle");
        URLConnection connection = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        // When
        GameOfLifeRleParser.loadPatternFromReader(game, reader);
        reader.close();

        // Then
        int[][] expectedGrid = new int[][]{
                {0, 1, 0},
                {0, 0, 1},
                {1, 1, 1}
        };

        assertEquals(3, game.getGrid().length, "Grid height should be 3");
        assertEquals(3, game.getGrid()[0].length, "Grid width should be 3");

        for (int i = 0; i < expectedGrid.length; i++) {
            for (int j = 0; j < expectedGrid[i].length; j++) {
                assertEquals(expectedGrid[i][j], game.getGrid()[i][j],
                        "Cell (" + i + "," + j + ") does not match the expected value.");
            }
        }
    }
}
