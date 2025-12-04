package day4;

import org.junit.Test;

import functionalj.list.FuncList;

public class Day4Part2Test extends Day4Part1Test {
    
    Object calculate(FuncList<String> lines) {
        var grids = constructGrids(lines);
        var total = 0;
        while (true) {
            var result = grids.countAccessible(4 - 1);
            var count = result.count();
            if (count == 0)
                break;
            
            total += count;
            grids = result.newGrid();
        }
        
        return total;
    }
    
    //== Test ==
    
    @Test
    public void testExample() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("43", result);
    }
    
    @Test
    public void testProd() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("8948", result);
    }
    
}
