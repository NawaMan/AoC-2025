package day4;

import java.util.function.IntBinaryOperator;

import org.junit.Test;

import common.BaseTest;
import functionalj.list.FuncList;

/**
 * --- Day 4: Printing Department ---
 * 
 * You ride the escalator down to the printing department. They're clearly getting ready for 1Christmas;
 *   they have lots of large rolls of paper everywhere, and there's even a massive printer in
 *   the corner (to handle the really big print jobs).
 * 
 * Decorating here will be easy: they can make their own decorations.
 * What you really need is a way to get further into the North Pole base while the elevators are offline.
 * 
 * "Actually, maybe we can help with that," one of the Elves replies when you ask for help.
 * "We're pretty sure there's a cafeteria on the other side of the back wall.
 * If we could break through the wall, you'd be able to keep moving.
 * It's too bad all of our forklifts are so busy moving those big rolls of paper around."
 * 
 * If you can optimize the work the forklifts are doing, maybe they would have time to spare to break through the wall.
 * 
 * The rolls of paper (@) are arranged on a large grid; the Elves even have a helpful diagram (your puzzle input)
 *   indicating where everything is located.
 * 
 * For example:
 * 
 * ..@@.@@@@.
 * @@@.@.@.@@
 * @@@@@.@.@@
 * @.@@@@..@.
 * @@.@@@@.@@
 * .@@@@@@@.@
 * .@.@.@.@@@
 * @.@@@.@@@@
 * .@@@@@@@@.
 * @.@.@@@.@.
 * 
 * The forklifts can only access a roll of paper if there are fewer than four rolls of paper in
 *   the eight adjacent positions.
 * If you can figure out which rolls of paper the forklifts can access,
 *   they'll spend less time looking and more time breaking down the wall to the cafeteria.
 * 
 * In this example, there are 13 rolls of paper that can be accessed by a forklift (marked with x):
 * 
 * ..xx.xx@x.
 * x@@.@.@.@@
 * @@@@@.x.@@
 * @.@@@@..@.
 * x@.@@@@.@x
 * .@@@@@@@.@
 * .@.@.@.@@@
 * x.@@@.@@@@
 * .@@@@@@@@.
 * x.x.@@@.x.
 * 
 * Consider your complete diagram of the paper roll locations. How many rolls of paper can be accessed by a forklift?
 * 
 * Your puzzle answer was 1547.
 */
public class Day4Part1Test extends BaseTest {
    
    static record CountResult(int count, Grid newGrid) {}
    
    static int sumLoop(int rows, int cols, IntBinaryOperator body) {
        var sum = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                sum += body.applyAsInt(r, c);
            }
        }
        return sum;
    } 
    
    static record Grid(boolean[][] grid, int rows, int cols) {
        boolean isAccessible(int row, int col, int maxAround) {
            var around = 0;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0)
                        continue;
                    
                    var r = row + i;
                    var c = col + j;
                    if (r < 0 || c < 0 || r >= rows || c >= cols)
                        continue;
                    
                    if (grid[r][c])
                        around++;
                }
            }
            return around <= maxAround;
        }
        
        CountResult countAccessible(int maxAround) {
            var newGrids = new boolean[rows][cols];
            var count    = sumLoop(rows, cols, (r, c) -> {
                newGrids[r][c] = grid[r][c];
                if (!grid[r][c] || !isAccessible(r, c, maxAround))
                    return 0;
                
                newGrids[r][c] = false;
                return 1;
            });
            return new CountResult(count, new Grid(newGrids, rows, cols));
        }
    }
    
    Grid constructGrids(FuncList<String> lines) {
        var rows = lines.size();
        var cols = lines.get(0).length();
        var grids = new boolean[rows][cols];
        sumLoop(rows, cols, (r, c) -> {
            grids[r][c] = lines.get(r).charAt(c) == '@';
            return 0;
        });
        return new Grid(grids, rows, cols);
    }
    
    Object calculate(FuncList<String> lines) {
        var grids = constructGrids(lines);
        var count = grids.countAccessible(4 - 1).count();
        return count;
    }
    
    //== Test ==
    
    @Test
    public void testExample() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("13", result);
    }
    
    @Test
    public void testProd() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("1547", result);
    }
    
}
