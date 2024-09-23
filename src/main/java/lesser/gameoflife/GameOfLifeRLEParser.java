package lesser.gameoflife;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameOfLifeRLEParser {


    public static void loadPatternFromFile(GameOfLife game, String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            loadPatternFromReader(game, reader);  // Delegate to the new method
        }
    }

    public static void loadPatternFromReader(GameOfLife game, BufferedReader reader) throws IOException {
        String line;
        int x = 0;
        int y = 0;
        boolean inPattern = false;

        Pattern patternSize = Pattern.compile("x\\s*=\\s*(\\d+),\\s*y\\s*=\\s*(\\d+)");
        while ((line = reader.readLine()) != null) {
            line = line.trim();

            // Ignore comments
            if (line.startsWith("#")) {
                continue;
            }

            if (!inPattern && line.startsWith("x")) {
                Matcher matcher = patternSize.matcher(line);
                if (matcher.find()) {
                    int width = Integer.parseInt(matcher.group(1));
                    int height = Integer.parseInt(matcher.group(2));
                    game.initializeWithSize(width, height);
                    inPattern = true; // Start parsing pattern
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
                                x += count; // Move x forward by 'count' dead cells
                                break;
                            case 'o': // Live cells
                                for (int i = 0; i < count; i++) {
                                    if (y < game.getGrid().length && x < game.getGrid()[0].length) {
                                        game.setCell(y, x++, 1); // Set live cells in the game grid
                                    }
                                }
                                break;
                            case '$': // End of row
                                y++; // Move to the next row
                                x = 0; // Reset column position
                                break;
                            case '!': // End of pattern
                                return;
                            default: // Default case for switch
                                throw new IllegalArgumentException("Unexpected character in pattern: " + c);
                        }
                        count = 0; // Reset count after processing each character
                    }
                }
            }
        }
    }
}
