package lesser.gameoflife;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

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
        JButton loadButton = new JButton("Load RLE");
        loadButton.addActionListener(e -> loadRLEPattern()); // Correct usage here
        controlPanel.add(loadButton);
        controlPanel.add(playButton);
        controlPanel.add(pauseButton);
        add(controlPanel, BorderLayout.SOUTH);
    }

    // Move the loadRLEPattern method outside the constructor
    private void loadRLEPattern() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                GameOfLifeRLEParser.loadPatternFromFile(game, selectedFile.getAbsolutePath());
                repaint(); // Repaint after loading the pattern
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading RLE file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void initializeBlinkerPattern(GameOfLife game) {
        int centerX = game.getGrid()[0].length / 2;
        int centerY = game.getGrid().length / 2;

        game.setCell(centerY, centerX - 1, 1);
        game.setCell(centerY, centerX, 1);
        game.setCell(centerY, centerX + 1, 1);
    }
}
