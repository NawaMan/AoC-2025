package day2;

import static functionalj.list.FuncList.cycle;
import static functionalj.stream.longstream.LongStreamPlus.range;
import static java.lang.Long.parseLong;

import java.math.BigInteger;

import org.junit.Test;

import common.BaseTest;
import day2.Day2Part1Test.Range;
import functionalj.function.Func1;
import functionalj.list.FuncList;
import functionalj.list.longlist.LongFuncList;

/**
 * --- Part Two ---
 * 
 * The clerk quickly discovers that there are still invalid IDs in the ranges in your list.
 * Maybe the young Elf was doing other silly patterns as well?
 * 
 * Now, an ID is invalid if it is made only of some sequence of digits repeated at least twice.
 * So, 12341234 (1234 two times), 123123123 (123 three times), 1212121212 (12 five times),
 * and 1111111 (1 seven times) are all invalid IDs.
 * 
 * From the same example as before:
 * 
 *     11-22 still has two invalid IDs, 11 and 22.
 *     95-115 now has two invalid IDs, 99 and 111.
 *     998-1012 now has two invalid IDs, 999 and 1010.
 *     1188511880-1188511890 still has one invalid ID, 1188511885.
 *     222220-222224 still has one invalid ID, 222222.
 *     1698522-1698528 still contains no invalid IDs.
 *     446443-446449 still has one invalid ID, 446446.
 *     38593856-38593862 still has one invalid ID, 38593859.
 *     565653-565659 now has one invalid ID, 565656.
 *     824824821-824824827 now has one invalid ID, 824824824.
 *     2121212118-2121212124 now has one invalid ID, 2121212121.
 * 
 * Adding up all the invalid IDs in this example produces 4174379265.
 * 
 * What do you get if you add up all of the invalid IDs using these new rules?
 * 
 * Your puzzle answer was 25663320831.
 */
public class Day2Part2Test extends BaseTest {
    
    Func1<Range, LongFuncList> invalidIds() {
        return range -> {
            return range(range.begin(), range.end() + 1)
                    .filter(id -> isValidId(id))
                    .toFuncList();
        };
    }
    
    private boolean isValidId(long id) {
        var str  = Long.toString(id);
        int half = str.length() / 2;
        return range(1, half + 1)
                .filter  (len -> isStringDivisibleBy (str, len))
                .anyMatch(len -> isStringRepeatingFor(str, len));
    }
    
    boolean isStringDivisibleBy(String str, long i) {
        return str.length() % i == 0;
    }
    
    boolean isStringRepeatingFor(String str, long len) {
        var count  = str.length() / len;
        var part   = "%1$"+len+"s";
        var full   = cycle(part).limit(count).reduce(String::concat).get();
        var first  = str.substring(0, (int)len);
        var expect = full.formatted(first);
        return expect.equals(str);
    }
    
    Object calculate(FuncList<String> lines) {
        var ranges
                = lines
                .map(line -> line.split("-"))
                .map(pair -> new Range(parseLong(pair[0]), parseLong(pair[1])));
        
        var invalidIds
                = ranges
                .filter       (Range::isValid)
                .flatMapToLong(invalidIds());
        
        return invalidIds
                .mapToObj(BigInteger::valueOf)
                .reduce  (BigInteger::add)
                .get();
    }
    
    //== Test ==
    
    @Test
    public void testExample() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("4174379265", result);
    }
    
    @Test
    public void testProd() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("25663320831", result);
    }
    
}
