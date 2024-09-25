package lesser.gameoflife;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameOfLifeRleParserTest {
    @Test
    void loadGliderPatternFromReader() throws IOException {
        // Given
        GameOfLife game = new GameOfLife(10, 10);
        URL url = new URL("https://conwaylife.com/patterns/glider.rle");
        URLConnection connection = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        GameOfLifeRleParser parser = new GameOfLifeRleParser(game);

        // When
        parser.loadPatternFromReader(reader);
        reader.close();

        // Expected glider pattern (3x3)
        int[][] expectedGrid = new int[][]{
                {0, 1, 0},
                {0, 0, 1},
                {1, 1, 1}
        };

        // Then
        assertEquals(3, game.getGrid().length, "Grid height should be 3");  // Adjust grid height check
        assertEquals(3, game.getGrid()[0].length, "Grid width should be 3");  // Adjust grid width check

        // Check the expected pattern
        assertArrayEquals(expectedGrid, game.getGrid());
        }
    }
