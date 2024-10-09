package lesser.gameoflife;

import org.apache.commons.io.IOUtils;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameOfLifeRleParser {

    private static final int MIN_GRID_SIZE = 100;
    private GameOfLife game;

    public GameOfLifeRleParser(GameOfLife game) {
        this.game = game;
    }

    public void loadFromClipboard() throws Exception {
        String clipboardContent = (String) Toolkit.getDefaultToolkit()
                .getSystemClipboard().getData(DataFlavor.stringFlavor);
        clipboardContent = clipboardContent.trim();

        if (isValidUrl(clipboardContent)) {
            // Use the modified loadPatternFromUrl method with an InputStream
            try (InputStream in = new URL(clipboardContent).openStream()) {
                loadPatternFromUrl(in);
            }
        } else if (isValidFile(clipboardContent)) {
            loadPatternFromFile(clipboardContent);
        } else {
            loadPatternFromText(clipboardContent);
        }
    }

    private boolean isValidUrl(String input) {
        try {
            new URL(input).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidFile(String input) {
        File file = new File(input);
        return file.exists() && file.isFile();
    }

    public void loadPatternFromUrl(InputStream in) throws IOException {
        String rleContents = IOUtils.toString(in, StandardCharsets.UTF_8);
        loadPatternFromText(rleContents);
    }

    public void loadPatternFromText(String rleText) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(rleText));
        loadPatternFromReader(reader);
    }

    public void loadPatternFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            loadPatternFromReader(reader);
        }
    }

    public void loadPatternFromReader(BufferedReader reader) throws IOException {
        String line;
        int x = 0;
        int y = 0;
        boolean inPattern = false;
        int patternWidth = 0;
        int patternHeight = 0;

        Pattern patternSize = Pattern.compile("x\\s*=\\s*(\\d+),\\s*y\\s*=\\s*(\\d+)");
        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (line.startsWith("#")) {
                continue; // Comment line, skip
            }

            if (!inPattern && line.startsWith("x")) {
                Matcher matcher = patternSize.matcher(line);
                if (matcher.find()) {
                    patternWidth = Integer.parseInt(matcher.group(1));
                    patternHeight = Integer.parseInt(matcher.group(2));

                    int gridWidth = Math.max(patternWidth, MIN_GRID_SIZE);
                    int gridHeight = Math.max(patternHeight, MIN_GRID_SIZE);
                    game.initializeWithSize(gridWidth, gridHeight);

                    x = (gridWidth - patternWidth) / 2;
                    y = (gridHeight - patternHeight) / 2;
                    inPattern = true;
                }
                continue;
            }

            // Parse the pattern data
            if (inPattern) {
                int count = 0;
                for (char c : line.toCharArray()) {
                    if (Character.isDigit(c)) {
                        count = count * 10 + (c - '0');
                    } else {
                        if (count == 0) {
                            count = 1;
                        }

                        switch (c) {
                            case 'b': // Dead cells
                                x += count;
                                break;
                            case 'o': // Live cells
                                for (int i = 0; i < count; i++) {
                                    if (y < game.getGrid().length && x < game.getGrid()[0].length) {
                                        game.setCell(y, x++, 1);
                                    }
                                }
                                break;
                            case '$': // End of row
                                y++;
                                x = (game.getGrid()[0].length - patternWidth) / 2;
                                break;
                            case '!': // End of pattern
                                return;
                            default:
                                System.err.println("Warning: Unexpected character in pattern: " + c);
                                break;
                        }
                        count = 0;
                    }
                }
            }
        }
    }
}
