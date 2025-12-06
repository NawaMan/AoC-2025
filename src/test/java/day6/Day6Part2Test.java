package day6;

import static functionalj.stream.intstream.IntStreamPlus.range;

import java.util.Map;
import java.util.function.LongBinaryOperator;

import org.junit.Test;

import common.BaseTest;
import functionalj.list.FuncList;

public class Day6Part2Test extends BaseTest {
    
    static final Map<String, LongBinaryOperator>
                    operatorFunctions = Map.of(
                        "*", (LongBinaryOperator)((a, b) -> a*b),
                        "+", (LongBinaryOperator)((a, b) -> a+b));
    
    Object calculate(FuncList<String> lines) {
        // Ensure there is a space at the end of each line.
        lines = lines.map(line -> line + " ").toFuncList();
        
        var lineCount    = lines.size();
        var numberLines  = lines.subList(0, lineCount - 1);
        var operatorLine = lines.get(lineCount - 1);
        var operators    = grab(regex("[^ ][ ]*")).apply(operatorLine).toFuncList();
        
        int offset = 0;
        long total = 0L;
        for (var operator : operators) {
            total  += answerColumnQuestion(numberLines, offset, operator);
            offset += operator.length();
        }
        
        return total;
    }
    
    long answerColumnQuestion(FuncList<String> numberLines, int offset, String operator) {
        var columnWidth  = operator.length() - 1;
        var operatorFunc = operatorFunctions.get(operator.trim());
        return range(0, columnWidth)
                .map      (theInt.plus(offset))
                .mapToObj (index -> numberLines.map(theString.charAt(index)))
                .mapToObj (chars -> chars.join().trim())
                .mapToLong(Long::parseLong)
                .reduce   (operatorFunc)
                .getAsLong();
    }
    
    //== Test ==
    
    @Test
    public void testExample() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("3263827", result);
    }
    
    @Test
    public void testProd() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("13215665360076", result);
    }
    
}
