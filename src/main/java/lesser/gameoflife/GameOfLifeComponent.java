package lesser.gameoflife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameOfLifeComponent extends JComponent {

    private GameOfLife game;
    private int cellSize = 20;

    private boolean running = false;

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

        int xOffset = (getWidth() - gridWidth) / 2;
        int yOffset = (getHeight() - gridHeight) / 2;

        // Draw cells
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                int x = j * cellSize + xOffset;
                int y = i * cellSize + yOffset;

                if (grid[i][j] == 1) {
                    g.fillRect(x, y, cellSize, cellSize);
                }
            }
        }

        // Draw grid lines
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i <= grid.length; i++) {
            int y = i * cellSize + yOffset;
            g.drawLine(xOffset, y, xOffset + gridWidth, y);
        }
        for (int j = 0; j <= grid[0].length; j++) {
            int x = j * cellSize + xOffset;
            g.drawLine(x, yOffset, x, yOffset + gridHeight);
        }
    }

    public void toggleRunning() {
        running = !running;
    }

    public boolean isRunning() {
        return running;
    }
}