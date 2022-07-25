package minesweeper;

public class Tile {
    public int x;
    public int y;
    public boolean hasBomb;
    public int numNeighborBombs;
    public boolean active;
    public boolean flagged;
    public String displayValue;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_MAGENTA = "\u001b[35m";

    public Tile(int x, int y, boolean hasBomb)
    {
        this.x = x;
        this.y = y;
        this.hasBomb = hasBomb;
        active = true;
        flagged = false;
        displayValue = "?";
    }

    public void UpdateDisplayValue(boolean gameOver)
    {
        if (!gameOver)
        {
            if (active)
            {
                if (flagged)
                {
                    displayValue = ANSI_MAGENTA + "F" + ANSI_RESET;
                }
                else
                {
                    displayValue = ANSI_YELLOW + "?" + ANSI_RESET;
                }
            }
            else
            {
                if (numNeighborBombs > 0)
                {
                    switch (numNeighborBombs)
                    {
                        case 1 -> displayValue = ANSI_BLUE + numNeighborBombs + ANSI_RESET;
                        case 2 -> displayValue = ANSI_GREEN + numNeighborBombs + ANSI_RESET;
                        case 3 -> displayValue = ANSI_RED + numNeighborBombs + ANSI_RESET;
                        case 4 -> displayValue = ANSI_PURPLE + numNeighborBombs + ANSI_RESET;
                    }

                }
                else
                {
                    displayValue = " ";
                }
            }
        }
        else
        {
            // show bombs and neighbors when game is over
            if (hasBomb)
            {
                displayValue = ANSI_RED + "B" + ANSI_RESET;
            }
            else
            {
                if (numNeighborBombs > 0)
                {
                    switch (numNeighborBombs)
                    {
                        case 1 -> displayValue = ANSI_BLUE + numNeighborBombs + ANSI_RESET;
                        case 2 -> displayValue = ANSI_GREEN + numNeighborBombs + ANSI_RESET;
                        case 3 -> displayValue = ANSI_RED + numNeighborBombs + ANSI_RESET;
                        case 4 -> displayValue = ANSI_PURPLE + numNeighborBombs + ANSI_RESET;
                    }
                }
                else
                {
                    displayValue = " ";
                }
            }
        }
    }
}
