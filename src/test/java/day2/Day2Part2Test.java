package day2;

import static java.lang.Long.parseLong;

import java.math.BigInteger;
import java.util.stream.LongStream;

import org.junit.Test;

import common.BaseTest;
import day2.Day2Part1Test.Range;
import functionalj.function.Func1;
import functionalj.list.FuncList;
import functionalj.list.longlist.LongFuncList;

public class Day2Part2Test extends BaseTest {
    
    Func1<Range, LongFuncList> invalidIds() {
        return range -> {
            return LongFuncList
                    .from(LongStream.range(range.begin(), range.end() + 1))
                    .filter(value -> {
                        var str = Long.toString(value);
                        int half = str.length() / 2;
                        for (int len = 1; len <= half; len++) {
                            if (str.length() % len != 0)
                                continue;
                            var count = str.length() / len;
                            
                            var each   = "%1$"+len+"s";
                            var full   = FuncList.cycle(each).limit(count).reduce(String::concat).get();
                            var first  = str.substring(0, len);
                            var expect = full.formatted(first);
                            if (expect.equals(str))
                                return true;
                        }
                        return false;
                    });
        };
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
