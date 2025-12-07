package day7;

import static functionalj.functions.StrFuncs.matches;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import common.BaseTest;
import functionalj.list.FuncList;
import functionalj.tuple.IntIntTuple;

public class Day7Part2Test extends BaseTest {
    
    static class Spliters {
        private final Map<IntIntTuple, Long> visitedCounts = new ConcurrentHashMap<>();
        private final FuncList<FuncList<Integer>> spliters;
        
        Spliters(FuncList<FuncList<Integer>> spliters) { this.spliters = spliters; }
        
        long countPath(int beam) { return countPath(beam, 0); }
        
        long countPath(int beam, int level) {
            var beamLevel = IntIntTuple.of(beam, level);
            var count = visitedCounts.get(beamLevel);
            if (count != null)
                return count;
            
            if (level == spliters.size()) {
                count = 1L;
            } else {
                var hit = spliters.get(level).contains(beam);
                if (hit) count = countPath(beam - 1, level + 1) + countPath(beam + 1, level + 1);
                else     count = countPath(beam, level + 1);
            }
            
            visitedCounts.put(beamLevel, count);
            return count;
        }
    }
    
    Object calculate(FuncList<String> lines) {
        var items
            = lines
            .map    (line -> line.replace('.', ' '))
            .exclude(line -> line.trim().isEmpty())
            .map    (matches(regex("[S^]")))
            .map    (matches -> matches.map(r -> r.start()).toFuncList())
            .toFuncList();
        
        var beam     = items.get(0).get(0);
        var spliters = new Spliters(items.skip(1));
        return spliters.countPath(beam);
    }
    
    //== Test ==
    
    @Test
    public void testExample() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("40", result);
    }
    
    @Test
    public void testProd() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("10357305916520", result);
    }
    
}
