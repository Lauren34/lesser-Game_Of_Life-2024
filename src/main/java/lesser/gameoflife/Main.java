package lesser.gameoflife;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameOfLifeFrame frame = new GameOfLifeFrame();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}