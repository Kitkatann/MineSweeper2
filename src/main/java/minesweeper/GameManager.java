package minesweeper;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameManager {

    private Grid grid;
    Scanner scanner;
    private boolean gameOver;
    private long startTime;
    private long currentTime;
    private int minesRemaining;



    public GameManager(Scanner scanner)
    {
        startTime = System.currentTimeMillis();
        currentTime = 0;
        grid = new Grid(Difficulty.BEGINNER);
        minesRemaining = grid.totalMines;
        this.scanner = scanner;
        gameOver = false;
        Start();
    }

    private void Start()
    {
        while (!gameOver)
        {
            DisplayGrid();
            TakeGuess();
        }
        DisplayGrid();
    }

    private void TakeGuess()
    {
        System.out.println("\nEnter the coordinates and then f to place a flag, or t to traverse (x,y,t/f): ");
        String guess = scanner.nextLine();
        int x;
        int y;
        String operation;
        Pattern pattern = Pattern.compile("^([0-9]+),([0-9]+),([tfTF])$");
        Matcher matcher = pattern.matcher(guess);
        if (matcher.find( ))
        {
            String[] splitGuess = matcher.group(0).split(",", 0);
            x = Integer.parseInt(splitGuess[0]);
            y = Integer.parseInt(splitGuess[1]);
            operation = splitGuess[2];
            Tile targetTile = grid.GetTile(x, y);
            if (targetTile != null)
            {

                if (targetTile.active)
                {
                    if (operation.equals("f"))
                    {
                        if (!targetTile.flagged)
                        {
                            targetTile.flagged = true;
                            minesRemaining--;
                        }
                        else
                        {
                            targetTile.flagged = false;
                            minesRemaining++;
                        }
                    }
                    if (operation.equals("t"))
                    {
                        targetTile.active = false;
                        if (targetTile.hasBomb)
                        {
                            gameOver = true;
                            System.out.println("\nWhoops, that was a mine! Game over.");
                        }
                        else
                        {
                            if (targetTile.flagged)
                            {
                                targetTile.flagged = false;
                                minesRemaining++;
                            }
                            grid.FloodFillSafeSpaces(targetTile);
                            if (CheckIfWon())
                            {
                                gameOver = true;
                                System.out.println("\nCongrats, you won!");
                            }
                        }
                    }
                }
                else
                {
                    System.out.println("This tile is already revealed.");
                }
            }
            else
            {
                System.out.println("Out of range.");
            }
        }
        else
        {
            System.out.println("Invalid input.");
        }

    }

    private void DisplayGrid()
    {
        grid.UpdateTileDisplayValues(gameOver);
        System.out.println("-----------------------------------------------------------------------------------------");
        currentTime = System.currentTimeMillis() - startTime;
        System.out.format("%10s %10s %30s %5d\n", "Time elapsed: ", currentTime / 1000 + " seconds", "Mines: ", minesRemaining);
        String cellStr = "";
        for (int y = grid.height - 1; y >= 0; y--)
        {
            System.out.println();

            cellStr += String.format("%-3s", y);

            for (int x = 0; x < grid.width; x++)
            {
                Tile t = grid.GetTile(x, y);
                if (t != null)
                {
                    cellStr += t.displayValue;
                }

                System.out.printf("  %s ", cellStr);


                cellStr = "";
            }
        }
        cellStr = "\n     ";
        for (int x = 0; x < grid.width; x++)
        {
            cellStr += x;
            System.out.printf("%4s", cellStr);
            cellStr = "";
        }
        System.out.println();
    }

    private boolean CheckIfWon()
    {
        for (Tile tile : grid.tiles)
        {
            if (tile.active && !tile.hasBomb)
            {
                return false;
            }
        }
        return true;
    }
}
