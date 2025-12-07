package day7;

import static functionalj.functions.StrFuncs.matches;

import org.junit.Test;

import common.BaseTest;
import functionalj.list.FuncList;

public class Day7Part1Test extends BaseTest {
    
    Object calculate(FuncList<String> lines) {
        var grid
            = lines
            .map    (matches(regex("[S^]")))
            .map    (matches -> matches.map(r -> r.start()).toFuncList())
            .exclude(matches -> matches.isEmpty())
            .toFuncList();
        
        var beams = grid.get(0);
        var total = 0;
        for (var row : grid.skip(1).toFuncList()) {
            var spliters      = row.filterIn(beams);
            var splitedBeams  = spliters.flatMap(col -> FuncList.of(col - 1, col + 1));
            var throughBeams  = beams.excludeIn(spliters);
            var combinedBeams = splitedBeams.appendAll(throughBeams);
            
            beams  = combinedBeams.sorted().distinct().toFuncList();
            total += spliters.size();
        }
        return total;
    }
    
    //== Test ==
    
    @Test
    public void testExample() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("21", result);
    }
    
    @Test
    public void testProd() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("1622", result);
    }
    
}
