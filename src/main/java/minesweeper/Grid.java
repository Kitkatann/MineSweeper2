package minesweeper;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Grid {
    public ArrayList<Tile> tiles;
    public int width;
    public int height;
    public int totalMines;
    public Difficulty difficulty;

    public Grid(Difficulty difficulty)
    {
        this.difficulty = difficulty;
        switch(difficulty)
        {
            case BEGINNER:
            {
                width = 10;
                height = 10;
                totalMines = 10;
                break;
            }
            case INTERMEDIATE:
            {
                width = 16;
                height = 16;
                totalMines = 40;
                break;
            }
            case EXPERT:
            {
                width = 16;
                height = 30;
                totalMines = 99;
                break;
            }
        }

        tiles = new ArrayList<>();
        GenerateGrid();
        SetTileValues();
    }

    public void GenerateGrid()
    {
        int[] mineSpawnIndexes = GenerateMineSpawnIndexes();

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                tiles.add(new Tile(x, y, false));
            }
        }
        for (int i = 0; i < totalMines; i++)
        {
            tiles.get(mineSpawnIndexes[i]).hasBomb = true;
        }
    }

    public int[] GenerateMineSpawnIndexes()
    {
        Random random = new Random();
        int[] mineSpawns = new int[totalMines];
        ArrayList<Integer> takenIndexes = new ArrayList<>();
        boolean indexFound;
        for (int i = 0; i < totalMines; i++)
        {
            indexFound = false;
            while (!indexFound)
            {
                int randInt = random.nextInt(width * height);
                if (!takenIndexes.contains(randInt))
                {
                    mineSpawns[i] = randInt;
                    takenIndexes.add(randInt);
                    indexFound = true;
                }
            }
        }
        return mineSpawns;
    }

    public void SetTileValues()
    {
        for (Tile tile : tiles)
        {
            if (tile.hasBomb)
            {
                Tile LTile = GetTile(tile.x - 1, tile.y);
                Tile TLTile = GetTile(tile.x - 1, tile.y + 1);
                Tile TTile = GetTile(tile.x, tile.y + 1);
                Tile TRTile = GetTile(tile.x + 1, tile.y + 1);
                Tile RTile = GetTile(tile.x + 1, tile.y);
                Tile BRTile = GetTile(tile.x + 1, tile.y - 1);
                Tile BTile = GetTile(tile.x, tile.y - 1);
                Tile BLTile = GetTile(tile.x - 1, tile.y - 1);

                AddNeighborBomb(LTile);
                AddNeighborBomb(TLTile);
                AddNeighborBomb(TTile);
                AddNeighborBomb(TRTile);
                AddNeighborBomb(RTile);
                AddNeighborBomb(BRTile);
                AddNeighborBomb(BTile);
                AddNeighborBomb(BLTile);

            }
        }
    }

    private void AddNeighborBomb(Tile tile)
    {
        if (tile != null)
        {
            tile.numNeighborBombs++;
        }
    }

    public Tile GetTile(int x, int y)
    {
        try
        {
            int index = x + width * y;
            if (tiles.contains(tiles.get(index)))
            {
                if (tiles.get(index).x == x && tiles.get(index).y == y)
                {
                    return tiles.get(index);
                }
            }
        }
        catch (IndexOutOfBoundsException e)
        {
            return null;
        }
        return null;
    }

    public void UpdateTileDisplayValues(boolean gameOver)
    {
        for (Tile t : tiles)
        {
            t.UpdateDisplayValue(gameOver);
        }
    }

    public void FloodFillSafeSpaces(Tile tile)
    {
        // set all connecting tiles to inactive
        ArrayList<Tile> connectedActiveTiles = IdentifyConnectedActiveTiles(tile);
        for (Tile t : connectedActiveTiles)
        {
            t.active = false;
        }
    }

    /**
     * Find and return the connected active tiles up until the first adjoining tiles with neighboring bombs
     *
     * @param startingTile the tile to start the flood fill from
     * @return a list of all safe tiles connected to the starting tile
     */
    public ArrayList<Tile> IdentifyConnectedActiveTiles(Tile startingTile)
    {
        // list of tiles to flood
        Stack<Tile> floodTiles = new Stack<>();
        floodTiles.add(startingTile);

        // list of tiles visited
        ArrayList<Tile> tilesVisited = new ArrayList<>();
        tilesVisited.add(startingTile);

        // list of adjacent tiles to a given tile
        ArrayList<Tile> adjacent = new ArrayList<>();

        while (floodTiles.size() > 0)
        {
            // take a tile off the list of tiles to flood
            Tile tile = floodTiles.peek();
            floodTiles.pop();

            // if the tile has no neighboring bombs
            if (tile.numNeighborBombs == 0)
            {
                adjacent.add(GetTile(tile.x + 1, tile.y));
                adjacent.add(GetTile(tile.x - 1, tile.y));
                adjacent.add(GetTile(tile.x, tile.y + 1));
                adjacent.add(GetTile(tile.x, tile.y - 1));
                adjacent.add(GetTile(tile.x - 1, tile.y - 1));
                adjacent.add(GetTile(tile.x + 1, tile.y + 1));
                adjacent.add(GetTile(tile.x + 1, tile.y - 1));
                adjacent.add(GetTile(tile.x - 1, tile.y + 1));

                // look at adjacent tiles
                for (Tile adjTile : adjacent)
                {
                    if (adjTile != null && adjTile.active && !adjTile.hasBomb && !tilesVisited.contains(adjTile))
                    {
                        // add the tile to be flooded around, and set it as visited
                        floodTiles.push(adjTile);
                        tilesVisited.add(adjTile);
                    }
                }
            }
            adjacent.clear();
        }
        return tilesVisited;
    }
}
