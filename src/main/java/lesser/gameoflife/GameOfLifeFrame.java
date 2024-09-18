package lesser.gameoflife;

import javax.swing.*;
import java.awt.*;

public class GameOfLifeFrame extends JFrame {

    private final GameOfLife game = new GameOfLife(10, 10);

    public GameOfLifeFrame() {
        setSize(800, 600);
        setTitle("Game of Life");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initializeBlinkerPattern(game);

        GameOfLifeComponent gameComponent = new GameOfLifeComponent(game);
        add(gameComponent, BorderLayout.CENTER); // Add to CENTER

        // Control panel with play/pause buttons
        JPanel controlPanel = new JPanel();
        JButton playButton = new JButton("▶");
        playButton.addActionListener(e -> gameComponent.toggleRunning());
        JButton pauseButton = new JButton("⏸");
        pauseButton.addActionListener(e -> gameComponent.toggleRunning());
        controlPanel.add(playButton);
        controlPanel.add(pauseButton);
        add(controlPanel, BorderLayout.SOUTH);

    }

    private void initializeBlinkerPattern(GameOfLife game) {

        int centerX = game.getGrid()[0].length / 2;
        int centerY = game.getGrid().length / 2;


        game.setCell(centerY, centerX - 1, 1);
        game.setCell(centerY, centerX, 1);
        game.setCell(centerY, centerX + 1, 1);
    }

}
