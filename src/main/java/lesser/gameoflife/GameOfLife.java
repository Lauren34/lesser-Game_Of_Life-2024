package lesser.gameoflife;


import java.util.Random;

public class GameOfLife {
    private int[][] grid;
    private int width;
    private int height;

    public GameOfLife(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new int[height][width];
    }

    public void initialize() {
        Random random = new Random();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = random.nextBoolean() ? 1 : 0;
            }
        }
    }

    public void initializeWithSize(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new int[height][width]; // Resize the grid
    }

    public void nextGen() {
        int[][] nextGrid = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int aliveNeighbors = countAliveNeighbors(i, j);

                if (grid[i][j] == 1) {
                    nextGrid[i][j] = (aliveNeighbors == 2 || aliveNeighbors == 3) ? 1 : 0;
                } else {
                    nextGrid[i][j] = (aliveNeighbors == 3) ? 1 : 0;
                }
            }
        }

        grid = nextGrid;
    }

    int countAliveNeighbors(int x, int y) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int nx = x + i;
                int ny = y + j;
                if (nx >= 0 && ny >= 0 && nx < height && ny < width) {
                    count += grid[nx][ny];
                }
            }
        }

        return count;
    }

    public void setCell(int x, int y, int value) {
        if (x >= 0 && x < height && y >= 0 && y < width) {
            grid[x][y] = value;
        }
    }

    public int[][] getGrid() {
        return grid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sb.append(grid[i][j] == 1 ? "O " : ". ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
