package day3;

import org.junit.Test;

import common.BaseTest;
import functionalj.list.FuncList;
import functionalj.stream.intstream.IntStreamPlus;

/**
 * --- Part Two ---
 * 
 * The escalator doesn't move.
 * The Elf explains that it probably needs more joltage to overcome the static friction of the system
 *   and hits the big red "joltage limit safety override" button.
 * You lose count of the number of times she needs to confirm "yes, I'm sure"
 *   and decorate the lobby a bit while you wait.
 * 
 * Now, you need to make the largest joltage by turning on exactly twelve batteries within each bank.
 * 
 * The joltage output for the bank is still the number formed by the digits of the batteries you've turned on;
 * the only difference is that now there will be 12 digits in each bank's joltage output instead of two.
 * 
 * Consider again the example from before:
 * 
 * 987654321111111
 * 811111111111119
 * 234234234234278
 * 818181911112111
 * 
 * Now, the joltages are much larger:
 * 
 *     In 987654321111111, the largest joltage can be found by turning on everything except some 1s at the end
 *       to produce 987654321111.
 *     In the digit sequence 811111111111119, the largest joltage can be found by turning on everything except some 1s,
 *       producing 811111111119.
 *     In 234234234234278, the largest joltage can be found by turning on everything except a 2 battery, a 3 battery,
 *       and another 2 battery near the start to produce 434234234278.
 *     In 818181911112111, the joltage 888911112111 is produced by turning on everything except some 1s near the front.
 * 
 * The total output joltage is now much larger: 987654321111 + 811111111119 + 434234234278 + 888911112111 = 3121910778619.
 * 
 * What is the new total output joltage?
 * 
 * Your puzzle answer was 169709990062889.
 */
public class Day3Part2Test extends BaseTest {
    
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
                .mapToLong(line -> largestOf(line, 12))
                .sum();
    }
    
    //== Test ==
    
    @Test
    public void testExample() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("3121910778619", result);
    }
    
    @Test
    public void testProd() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("169709990062889", result);
    }
    
}
