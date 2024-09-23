package lesser.gameoflife;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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

    private void loadRLEPattern() {
        String[] options = {"Load from URL", "Load from File"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose an option to load the RLE pattern",
                "Load RLE Pattern",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if (choice == 0) {
            // Load from URL
            try {
                URL url = new URL("https://conwaylife.com/patterns/glider.rle");
                URLConnection connection = url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                GameOfLifeRLEParser.loadPatternFromReader(game, reader);  // Corrected to use loadPatternFromReader
                repaint(); // Repaint after loading the pattern
                reader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Invalid URL", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading RLE from URL", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (choice == 1) {
            // Load from File
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
    }

    private void initializeBlinkerPattern(GameOfLife game) {
        int centerX = game.getGrid()[0].length / 2;
        int centerY = game.getGrid().length / 2;

        game.setCell(centerY, centerX - 1, 1);
        game.setCell(centerY, centerX, 1);
        game.setCell(centerY, centerX + 1, 1);
    }
}
