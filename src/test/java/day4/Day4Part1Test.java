package day4;

import java.util.function.IntBinaryOperator;

import org.junit.Test;

import common.BaseTest;
import functionalj.list.FuncList;

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
