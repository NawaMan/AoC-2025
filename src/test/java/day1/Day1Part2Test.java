package day1;

import org.junit.Test;

import functionalj.list.FuncList;

public class Day1Part2Test extends Day1Part1Test {
    
    Object calculate(FuncList<String> lines) {
        var rotations = lines.stream().map(this::parseRotation).toList();
        int current = 50;
        int zeros   =  0;
        for (var rotation : rotations) {
            var previous   = current;
            var displament = rotation.displacement();
            var combined   = previous + displament;
            
            if (combined >= 100) {
                zeros    += combined / 100;
                combined  = combined % 100;
            }
            if (combined < 0) {
                int k = (-combined + 99) / 100;
                zeros    += k;
                combined += 100 * k;
            }
            if (combined == 0) {
                zeros++;
            }
            if (previous == 0) {
                zeros--;
            }
            current = combined;
        }
        return zeros;
    }
    
    //== Test ==
    
    @Test
    public void testExample() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("6", result);
    }
    
    @Test
    public void testProd() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("6819", result);
    }
    
}
