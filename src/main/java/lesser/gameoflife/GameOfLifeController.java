package lesser.gameoflife;

import org.apache.commons.io.IOUtils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener;

public class GameOfLifeController {

    private final GameOfLife model;
    private final GameOfLifeComponent view;
    private final GameOfLifeRleParser rle;

    public GameOfLifeController(GameOfLife model, GameOfLifeComponent view, GameOfLifeRleParser rle) {
        this.rle = rle;
        this.model = model;
        this.view = view;
    }

    public void paste(String clipboardContents) {
        try {
            if (clipboardContents.startsWith("http")) {
                InputStream in = new URL(clipboardContents).openStream();
                String rleContents = IOUtils.toString(in, StandardCharsets.UTF_8);
                rle.loadPatternFromText(rleContents);
            } else if (new File(clipboardContents).exists()) {
                FileInputStream fisTargetFile = new FileInputStream(new File(clipboardContents));
                String rleContents = IOUtils.toString(fisTargetFile, StandardCharsets.UTF_8);
                rle.loadPatternFromText(rleContents);
            } else {
                rle.loadPatternFromText(clipboardContents);
            }
            view.repaint();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void toggleCell(int x, int y) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int xOffset = (x - model.getGrid()[0].length * view.getCellSize()) / 2;
                int yOffset = (y - model.getGrid().length * view.getCellSize()) / 2;
                int cellX = (e.getY() - yOffset) / view.getCellSize();
                int cellY = (e.getX() - xOffset) / view.getCellSize();
                int[][] grid = model.getGrid();
                if (cellX >= 0 && cellX < grid.length && cellY >= 0 && cellY < grid[0].length) {
                    model.setCell(cellX, cellY, grid[cellX][cellY] == 1 ? 0 : 1);
                    view.repaint();
                }
            }
        });
    }
}
