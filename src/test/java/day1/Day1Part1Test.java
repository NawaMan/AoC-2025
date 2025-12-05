package day1;

import static day1.Day1Part1Test.Direction.L;

import java.util.stream.IntStream;

import org.junit.Test;

import common.BaseTest;
import functionalj.list.FuncList;

public class Day1Part1Test extends BaseTest {
    
    enum Direction {
        L, R;
        static Direction valueOf(char ch) {
            return switch (ch) {
                case 'L': { yield L; }
                case 'R': { yield R; }
                default: throw new IllegalArgumentException("Unexpected value: " + ch);
            };
        }
    }
    
    record Rotation(Direction direction, int distance) {
        int displacement() {
            return (direction() == L ? -1 : 1)*distance();
        }
    }
    
    Rotation parseRotation(String str) {
        var direction = Direction.valueOf(str.charAt(0));
        var distance  = Integer.parseInt(str.substring(1));
        return new Rotation(direction, distance);
    }
    
    Object calculate(FuncList<String> lines) {
        return lines.stream()
             .map        (this::parseRotation)
             .mapToInt   (Rotation::displacement)
             .prependWith(IntStream.of(50))
             .accumulate ((position, displacement) -> (position + displacement + 100) % 100)
             .filter     (theInt.thatIsZero())
             .count();
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
