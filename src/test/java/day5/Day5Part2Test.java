package day5;

import java.math.BigInteger;

import org.junit.Test;

import common.BaseTest;
import functionalj.list.FuncList;
import functionalj.list.FuncListBuilder;

/**
 * --- Part Two ---
 * 
 * The Elves start bringing their spoiled inventory to the trash chute at the back of the kitchen.
 * 
 * So that they can stop bugging you when they get new inventory,
 *   the Elves would like to know all of the IDs that the fresh ingredient ID ranges consider to be fresh.
 * An ingredient ID is still considered fresh if it is in any range.
 * 
 * Now, the second section of the database (the available ingredient IDs) is irrelevant.
 * Here are the fresh ingredient ID ranges from the above example:
 * 
 * 3-5
 * 10-14
 * 16-20
 * 12-18
 * 
 * The ingredient IDs that these ranges consider to be fresh are 3, 4, 5, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, and 20.
 * So, in this example, the fresh ingredient ID ranges consider a total of 14 ingredient IDs to be fresh.
 * 
 * Process the database file again.
 * How many ingredient IDs are considered to be fresh according to the fresh ingredient ID ranges?
 * 
 * Your puzzle answer was 344306344403172.
 */
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
