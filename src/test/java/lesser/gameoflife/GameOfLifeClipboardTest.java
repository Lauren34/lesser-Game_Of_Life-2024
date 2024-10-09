package lesser.gameoflife;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class GameOfLifeClipboardTest {

    private GameOfLife game;
    private GameOfLifeRleParser parser;
    private int[][] grid;

    @BeforeEach
    void setUp() {
        // Given
        game = new GameOfLife(100, 100);
        parser = new GameOfLifeRleParser(game);
        grid = new int[100][100];
        game.initializeWithSize(100, 100);
    }

    @Test
    void fromText() throws Exception {
        // Given
        String rleText = "x = 2, y = 2\n2o$2o!";

        // When
        parser.loadPatternFromText(rleText);

        // Then
        grid = game.getGrid();

        // Then
        int centerX = 50;
        int centerY = 50;

        assertEquals(1, grid[centerX - 1][centerY - 1]);
        assertEquals(1, grid[centerX - 1][centerY]);
        assertEquals(1, grid[centerX][centerY - 1]);
        assertEquals(1, grid[centerX][centerY]);
    }

    @Test
    void fromUrl() throws Exception {
        // Given
        InputStream mockInputStream = new ByteArrayInputStream("x = 2, y = 2\n2o$2o!".getBytes());

        // When
        parser.loadPatternFromUrl(mockInputStream);

        // Then
        grid = game.getGrid();

        // Then
        int centerX = 50;
        int centerY = 50;

        assertEquals(1, grid[centerX - 1][centerY - 1]);
        assertEquals(1, grid[centerX - 1][centerY]);
        assertEquals(1, grid[centerX][centerY - 1]);
        assertEquals(1, grid[centerX][centerY]);
    }

    @Test
    void fromFile() throws Exception {
        // Given
        String rleContent = "x = 2, y = 2\n2o$2o!";
        InputStream mockInputStream = new ByteArrayInputStream(rleContent.getBytes());

        // When
        parser.loadPatternFromUrl(mockInputStream);

        // Then
        grid = game.getGrid();
        int centerX = 50;
        int centerY = 50;

        assertEquals(1, grid[centerX - 1][centerY - 1]);
        assertEquals(1, grid[centerX - 1][centerY]);
        assertEquals(1, grid[centerX][centerY - 1]);
        assertEquals(1, grid[centerX][centerY]);
    }
}
