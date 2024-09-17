package lesser.gameoflife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameOfLifeComponent extends JComponent {

    private final GameOfLife game;
    private final int cellSize = 20; // Size for each cell

    public GameOfLifeComponent(GameOfLife game) {
        this.game = game;

        // Timer to repaint the component every second
        Timer timer = new Timer(1000, e -> {
            if (isRunning()) {
                game.nextGen();
                repaint();
            }
        });
        timer.start();

        // Mouse listener for clicking on cells
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int xOffset = (getWidth() - game.getGrid()[0].length * cellSize) / 2;
                int yOffset = (getHeight() - game.getGrid().length * cellSize) / 2;

                int x = (e.getY() - yOffset) / cellSize;
                int y = (e.getX() - xOffset) / cellSize;
                int[][] grid = game.getGrid();

                if (x >= 0 && x < grid.length && y >= 0 && y < grid[0].length) {
                    game.setCell(x, y, grid[x][y] == 1 ? 0 : 1);
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int[][] grid = game.getGrid();
        int gridWidth = grid[0].length * cellSize;
        int gridHeight = grid.length * cellSize;

        // Calculate the offset to center the grid
        int xOffset = (getWidth() - gridWidth) / 2;
        int yOffset = (getHeight() - gridHeight) / 2;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                int x = j * cellSize + xOffset;
                int y = i * cellSize + yOffset;

                if (grid[i][j] == 1) {
                    g.fillRect(x, y, cellSize, cellSize);
                } else {
                    g.drawRect(x, y, cellSize, cellSize);
                }
            }
        }
    }

    // Start or stop the simulation
    private boolean running = false;

    public void toggleRunning() {
        running = !running;
    }

    public boolean isRunning() {
        return running;
    }
}
