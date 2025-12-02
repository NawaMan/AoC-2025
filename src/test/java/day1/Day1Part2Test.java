package day1;

import static functionalj.list.FuncList.zipOf;

import org.junit.Test;

import common.BaseTest;
import day1.Day1Part1Test.Direction;
import functionalj.list.FuncList;

public class Day1Part2Test extends BaseTest {
    
    Object calculate(FuncList<String> lines) {
        var directions = lines.flatMap(grab(regex("L|R")))   .map(Direction::valueOf);
        var distances  = lines.flatMap(grab(regex("[0-9]+"))).map(Integer::valueOf);
        var rotations  = zipOf(directions, distances, Rotation::new);
        int current = 50;
        int zeros   =  0;
        for (var rotation : rotations) {
            var previous   = current;
            var displament = rotation.displament();
            var combined   = previous + displament;
            
            while (combined >= 100) {
                combined -= 100;
                zeros++;
            }
            while (combined < 0) {
                combined += 100;
                zeros++;
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
