package day5;

import java.math.BigInteger;

import org.junit.Test;

import common.BaseTest;
import functionalj.list.FuncList;
import functionalj.list.FuncListBuilder;

public class Day5Part2Test extends BaseTest {
    
    record Range(BigInteger first, BigInteger last) {
        boolean inRange(BigInteger value) {
            return theBigInteger.thatIsNotNegative().apply(value.subtract(first))
                && theBigInteger.thatIsNotPositive().apply(value.subtract(last));
        }
        BigInteger count() {
            return last.subtract(first).add(BigInteger.ONE);
        }
    }
    
    Object calculate(FuncList<String> lines) {
        var separationIndex = lines.indexesOf(theString.thatIsEmpty()).first().getAsInt();
        var ranges
            = lines
            .subList(0, separationIndex)
            .map(grab(regex("[0-9]+")))
            .map(match -> new Range(new BigInteger(match.get(0)), new BigInteger(match.get(1))))
            .sortedBy(Range::first)
            .toFuncList();
        
        var iterator       = ranges.iterator();
        var currentRange   = iterator.pullNext().getValue();
        var combinedRanges = new FuncListBuilder<Range>();
        
        while (iterator.hasNext()) {
            var nextRange = iterator.next();
            if (currentRange.inRange(nextRange.first)) {
                var newFirst = currentRange.first();
                var newLast  = currentRange.last().max(nextRange.last());
                currentRange = new Range(newFirst, newLast);
            } else {
                combinedRanges.add(currentRange);
                currentRange = nextRange;
            }
        }
        combinedRanges.add(currentRange);
        
        return combinedRanges
                .build()
                .peek(r -> println(r + ": " + r.count()))
                .map(Range::count)
                .sumToBigInteger(itself());
    }
    
    //== Test ==
    
    @Test
    public void testExample() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("14", result);
    }
    
    @Test
    public void testProd() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("344306344403172", result);
    }
    
}
