package day6;

import static functionalj.stream.intstream.IntStreamPlus.range;

import java.util.Map;
import java.util.function.LongBinaryOperator;

import org.junit.Test;

import common.BaseTest;
import functionalj.lens.lenses.IntegerToLongAccessPrimitive;
import functionalj.list.FuncList;

/**
 * --- Day 6: Trash Compactor ---
 * 
 * After helping the Elves in the kitchen, you were taking a break and helping them re-enact a movie scene
 *   when you over-enthusiastically jumped into the garbage chute!
 * 
 * A brief fall later, you find yourself in a garbage smasher. Unfortunately, the door's been magnetically sealed.
 * 
 * As you try to find a way out, you are approached by a family of cephalopods!
 * They're pretty sure they can get the door open, but it will take some time.
 * While you wait, they're curious if you can help the youngest cephalopod with her math homework.
 * 
 * Cephalopod math doesn't look that different from normal math.
 * The math worksheet (your puzzle input) consists of a list of problems;
 *   each problem has a group of numbers that need to either be either added (+) or multiplied (*) together.
 * 
 * However, the problems are arranged a little strangely;
 * they seem to be presented next to each other in a very long horizontal list.
 * For example:
 * 
 * 123 328  51 64 
 *  45 64  387 23 
 *   6 98  215 314
 * *   +   *   +  
 * 
 * Each problem's numbers are arranged vertically;
 *   at the bottom of the problem is the symbol for the operation that needs to be performed.
 * Problems are separated by a full column of only spaces.
 * The left/right alignment of numbers within each problem can be ignored.
 * 
 * So, this worksheet contains four problems:
 * 
 *     123 * 45 * 6 = 33210
 *     328 + 64 + 98 = 490
 *     51 * 387 * 215 = 4243455
 *     64 + 23 + 314 = 401
 * 
 * To check their work, cephalopod students are given the grand total of adding together all of the answers to
 *   the individual problems.
 * In this worksheet, the grand total is 33210 + 490 + 4243455 + 401 = 4277556.
 * 
 * Of course, the actual worksheet is much wider.
 * You'll need to make sure to unroll it completely so that you can read the problems clearly.
 * 
 * Solve the problems on the math worksheet.
 * What is the grand total found by adding together all of the answers to the individual problems?
 * 
 * Your puzzle answer was 6957525317641.
 */
public class Day6Part1Test extends BaseTest {
    
    static final Map<String, LongBinaryOperator>
                    operatorFunctions = Map.of(
                        "*", (LongBinaryOperator)((a, b) -> a*b),
                        "+", (LongBinaryOperator)((a, b) -> a+b));
    
    Object calculate(FuncList<String> lines) {
        var lineCount    = lines.size();
        var numberLines  = lines.subList(0, lineCount - 1);
        var operatorLine = lines.get(lineCount - 1);
        
        var operands  = numberLines.map(grab(regex("[0-9]+"))).toFuncList();
        var operators = grab(regex("[^ ]")).apply(operatorLine).toFuncList();
        
        var questionCount = operands.get(0).size();
        return range(0, questionCount)
                .sumToLong(answerColumnQuestion(operands, operators));
    }
    
    IntegerToLongAccessPrimitive answerColumnQuestion(FuncList<FuncList<String>> operands, FuncList<String> operators) {
        return column -> {
            var operator = operatorFunctions.get(operators.get(column));
            return operands
                    .map      (theFuncList.get(column))
                    .map      (Object::toString)
                    .mapToLong(Long::parseLong)
                    .reduce   (operator)
                    .getAsLong();
        };
    }
    
    //== Test ==
    
    @Test
    public void testExample() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("4277556", result);
    }
    
    @Test
    public void testProd() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("6957525317641", result);
    }
    
}
