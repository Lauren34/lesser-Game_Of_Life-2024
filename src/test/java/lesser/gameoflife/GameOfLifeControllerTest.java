package lesser.gameoflife;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class GameOfLifeControllerTest {

    String text = "#N Glider\n"
            + "#O Richard K. Guy\n"
            + "#C The smallest, most common, and first discovered spaceship. Diagonal, has period 4 and speed c/4.\n"
            + "#C www.conwaylife.com/wiki/index.php?title=Glider\n"
            + "x = 3, y = 3, rule = B3/S23\n"
            + "bob$2bo$3o!";

    @Test
    void toggleCell() {
        // given
        GameOfLife model = mock(GameOfLife.class);
        GameOfLifeComponent view = mock(GameOfLifeComponent.class);
        GameOfLifeRleParser rle = mock(GameOfLifeRleParser.class);

        GameOfLifeController controller = new GameOfLifeController(model, view, rle);
        doReturn(10).when(view).getCellSize();
        doReturn(10).when(view).getWidth();
        doReturn(10).when(view).getHeight();

        // when
        controller.toggleCell(50, 100);

        // then
        verify(model).setCell(5, 10, 1);
        verify(view).repaint();
    }

    @Test
    void pasteRle() throws IOException {
        // given
        GameOfLife model = mock(GameOfLife.class);
        GameOfLifeComponent view = mock(GameOfLifeComponent.class);
        GameOfLifeRleParser rle = mock(GameOfLifeRleParser.class);

        GameOfLifeController controller = new GameOfLifeController(model, view, rle);
        String word = "";

        // when
        controller.paste(word);

        // then
        verify(rle).loadPatternFromText("");
        verify(view).repaint();
    }

    @Test
    public void pasteUrl() throws IOException {
        // given
        GameOfLife model = mock(GameOfLife.class);
        GameOfLifeComponent view = mock(GameOfLifeComponent.class);
        GameOfLifeRleParser rle = spy(new GameOfLifeRleParser(model));

        GameOfLifeController controller = new GameOfLifeController(model, view, rle);
        String url = "https://conwaylife.com/patterns/glider.rle";

        doNothing().when(rle).loadPatternFromUrl(url);

        // when
        controller.paste(url);

        // then
        verify(rle).loadPatternFromUrl(url);
        verify(view).repaint();
    }
}
