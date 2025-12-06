package day6;

import static functionalj.stream.intstream.IntStreamPlus.range;

import java.util.Map;
import java.util.function.LongBinaryOperator;

import org.junit.Test;

import common.BaseTest;
import functionalj.list.FuncList;

/**
 * --- Part Two ---
 * 
 * The big cephalopods come back to check on how things are going.
 * When they see that your grand total doesn't match the one expected by the worksheet,
 *   they realize they forgot to explain how to read cephalopod math.
 * 
 * Cephalopod math is written right-to-left in columns.
 * Each number is given in its own column,
 *   with the most significant digit at the top and the least significant digit at the bottom.
 * (Problems are still separated with a column consisting only of spaces,
 *   and the symbol at the bottom of the problem is still the operator to use.)
 * 
 * Here's the example worksheet again:
 * 
 * 123 328  51 64 
 *  45 64  387 23 
 *   6 98  215 314
 * *   +   *   +  
 * 
 * Reading the problems right-to-left one column at a time, the problems are now quite different:
 * 
 *     The rightmost problem is 4 + 431 + 623 = 1058
 *     The second problem from the right is 175 * 581 * 32 = 3253600
 *     The third problem from the right is 8 + 248 + 369 = 625
 *     Finally, the leftmost problem is 356 * 24 * 1 = 8544
 * 
 * Now, the grand total is 1058 + 3253600 + 625 + 8544 = 3263827.
 * 
 * Solve the problems on the math worksheet again.
 * What is the grand total found by adding together all of the answers to the individual problems?
 * 
 * Your puzzle answer was 13215665360076.
 */
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
                .exclude  (theString.thatIsEmpty())
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
