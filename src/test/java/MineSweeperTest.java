import minesweeper.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MineSweeperTest {

    @Test
    public void test_GetTile()
    {
        Grid grid = new Grid(Difficulty.BEGINNER);
        grid.tiles = new ArrayList<>();
        Tile tile = new Tile(0, 0, false);
        grid.tiles.add(tile);
        assertEquals(tile, grid.GetTile(0, 0), "issue finding tile");
    }

    @Test
    public void test_GenerateGrid()
    {
        Grid grid = new Grid(Difficulty.BEGINNER);
        grid.tiles = new ArrayList<>();
        grid.GenerateGrid();
    }

    @Test
    public void test_TakeGuess()
    {

    }
}
