package day1;

import static day1.Day1Part1Test.Direction.L;
import static functionalj.list.FuncList.zipOf;

import org.junit.Test;

import common.BaseTest;
import functionalj.list.FuncList;
import functionalj.types.Struct;

public class Day1Part1Test extends BaseTest {
    
    enum Direction { L, R }
    
    @Struct
    static interface RotationSpec {
        Direction direction();
        int       distance();
        
        default int displament() {
            return (direction() == L ? -1 : 1)*distance();
        }
    }
    
    Object calculate(FuncList<String> lines) {
        var directions = lines.flatMap(grab(regex("L|R")))   .map(Direction::valueOf);
        var distances  = lines.flatMap(grab(regex("[0-9]+"))).map(Integer::valueOf);
        var rotations  = zipOf(directions, distances, Rotation::new);
        int current = 50;
        int zeros   =  0;
        for (var rotation : rotations) {
            var displament = rotation.displament();
            current = (current+ displament + 100) % 100;
            if (current == 0) zeros++;
        }
        return zeros;
    }
    
    //== Test ==
    
    @Test
    public void testExample() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("3", result);
    }
    
    @Test
    public void testProd() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("1154", result);
    }
    
}
