package day5;

import java.math.BigInteger;

import org.junit.Test;

import common.BaseTest;
import functionalj.list.FuncList;

public class Day5Part1Test extends BaseTest {
    
    record Range(BigInteger first, BigInteger last) {
        boolean inRange(BigInteger value) {
            return theBigInteger.thatIsNotNegative().apply(value.subtract(first))
                && theBigInteger.thatIsNotPositive().apply(value.subtract(last));
        }
    }
    
    Object calculate(FuncList<String> lines) {
        var separationIndex = lines.indexesOf(theString.thatIsEmpty()).first().getAsInt();
        var first    = lines.subList(0, separationIndex);
        var rest     = lines.subList(separationIndex + 1, lines.size());
        
        var ranges
            = first
            .map(grab(regex("[0-9]+")))
            .map(match -> new Range(new BigInteger(match.get(0)), new BigInteger(match.get(1))))
            .toFuncList();
        
        var ids
            = rest
            .map(grab(regex("[0-9]+")))
            .map(match -> new BigInteger(match.get(0)))
            .toFuncList();
        
        ranges.forEach(this::println);
        println("------");
        ids.forEach(this::println);
        
        return ids.filter(id -> ranges.anyMatch(range -> range.inRange(id))).count();
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
        assertAsString("735", result);
    }
    
}
