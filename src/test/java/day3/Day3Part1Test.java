package day3;

import org.junit.Test;

import common.BaseTest;
import functionalj.list.FuncList;
import functionalj.stream.intstream.IntStreamPlus;

public class Day3Part1Test extends BaseTest {
    
    record Found(long value, int position) {}
    
    Found findLargestOf(int[] digits, int start, int end) {
        var max = -1L;
        var pos = -1;
        for (int i = start; i < end; i++) {
            var digit = digits[i];
            if (digit > max) {
                max = digit;
                pos = i;
            }
        }
        return new Found(max, pos);
    }
    
    long largestOf(int[] digits, int start, int wantDigits) {
        if (wantDigits == 0)
            return 0;
        
        var found = findLargestOf(digits, start, digits.length - (wantDigits - 1));
        var pow  = ((long)Math.pow(10, wantDigits - 1));
        var next = largestOf(digits, found.position + 1, wantDigits - 1);
        return found.value*pow + next;
    }
    
    long largestOf(String line, int wantDigits) {
        var digits = IntStreamPlus.from(line.chars()).map(c -> c - '0').toArray();
        return largestOf(digits, 0, wantDigits);
    }
    
    Object calculate(FuncList<String> lines) {
        return lines
                .mapToLong(line -> largestOf(line, 2))
                .sum();
    }
    
    //== Test ==
    
    @Test
    public void testExample() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("357", result);
    }
    
    @Test
    public void testProd() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("17113", result);
    }
    
}
