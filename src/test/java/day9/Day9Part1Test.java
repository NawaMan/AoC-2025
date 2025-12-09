package day9;

import static java.lang.Math.abs;

import org.junit.Test;

import common.BaseTest;
import functionalj.list.FuncList;

public class Day9Part1Test extends BaseTest {
    
    record Point(long x, long y){}
    
    long area(Point a, Point b) {
        return (a.equals(b)) ? -1L : (abs(a.x - b.x) + 1)*(abs(a.y - b.y) + 1);
    }
    
    Object calculate(FuncList<String> lines) {
        var points
            = lines
            .map(grab(regex("[0-9]+")))
            .map(nums -> nums.mapToLong(Long::parseLong))
            .map(nums -> new Point(nums.get(0), nums.get(1)));
        
        return points.flatMap(a -> points.map(b -> area(a, b))).mapToLong(theLong).max().getAsLong();
    }
    
    //== Test ==
    
    @Test
    public void testExample() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("50", result);
    }
    
    @Test
    public void testProd() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("4767418746", result);
    }
    
}
