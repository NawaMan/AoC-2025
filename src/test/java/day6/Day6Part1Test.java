package day6;

import static functionalj.stream.intstream.IntStreamPlus.range;

import java.util.Map;
import java.util.function.LongBinaryOperator;

import org.junit.Test;

import common.BaseTest;
import functionalj.lens.lenses.IntegerToLongAccessPrimitive;
import functionalj.list.FuncList;

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
