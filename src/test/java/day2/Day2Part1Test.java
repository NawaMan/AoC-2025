package day2;

import static java.lang.Long.parseLong;

import java.math.BigInteger;
import java.util.stream.LongStream;

import org.junit.Test;

import common.BaseTest;
import functionalj.list.FuncList;
import functionalj.list.longlist.LongFuncList;

public class Day2Part1Test extends BaseTest {
    
    record Range(long begin, long end) {
        
        boolean isValid() {
            return begin() < end();
        }
        
        LongFuncList invalidIds() {
            return LongFuncList
                    .from(LongStream.range(begin(), end() + 1))
                    .filter(value -> {
                        var str = Long.toString(value);
                        if (str.length() % 2 != 0)
                            return false;
                        
                        int half = str.length() / 2;
                        for (int i = 0; i < half; i++) {
                            if (str.charAt(i) != str.charAt(i + half))
                                return false;
                        }
                        return true;
                    });
        }
    }
    
    Object calculate(FuncList<String> lines) {
        var ranges
                = lines
                .map(line -> line.split("-"))
                .map(pair -> new Range(parseLong(pair[0]), parseLong(pair[1])));
        
        var invalidIds
                = ranges
                .filter       (Range::isValid)
                .flatMapToLong(Range::invalidIds);
        
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
        assertAsString("1227775554", result);
    }
    
    @Test
    public void testProd() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("8576933996", result);
    }
    
}
