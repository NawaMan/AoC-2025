package day1;

import static day1.Day1Part1Test.Direction.L;

import java.util.stream.IntStream;

import org.junit.Test;

import common.BaseTest;
import functionalj.function.Func1;
import functionalj.list.FuncList;
import functionalj.types.Struct;

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
    
    @Struct
    static interface RotationSpec {
        Direction direction();
        int       distance();
        
        default int displament() {
            return (direction() == L ? -1 : 1)*distance();
        }
    }
    
    static Func1<String, Rotation> toRotation() {
        return str -> {
            var dir = Direction.valueOf(str.charAt(0));
            var dis = Integer.parseInt(str.substring(1));
            return new Rotation(dir, dis);
        };
    }
    
    Object calculate(FuncList<String> lines) {
//        var rotations  = lines.map(toRotation());
//        int current = 50;
//        int zeros   =  0;
//        for (var rotation : rotations) {
//            var displament = rotation.displament();
//            current = (current+ displament + 100) % 100;
//            if (current == 0) zeros++;
//        }
//        return zeros;
        return lines.stream()
             .map(toRotation())
             .mapToInt(Rotation::displament)
             .prependWith(IntStream.of(50))
             .accumulate((cur, d) -> (cur + d + 100) % 100)
             .filter(c -> c == 0)
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
