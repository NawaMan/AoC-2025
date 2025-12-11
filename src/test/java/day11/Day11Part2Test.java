package day11;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import common.BaseTest;
import functionalj.list.FuncList;
import functionalj.map.FuncMap;

public class Day11Part2Test extends BaseTest {
    
    record InPath(String name, boolean dac, boolean fft) {}
    
    long countPath2(InPath inPath, FuncMap<String, FuncList<String>> map, ConcurrentHashMap<InPath, Long> paths) {
        if ("out".equals(inPath.name))
            return (inPath.dac && inPath.fft) ? 1 : 0;
        
        var count = paths.get(inPath);
        if (count != null) {
            return count;
        }
        
        var hasDac = inPath.dac || "dac".equals(inPath.name);
        var hasFft = inPath.fft || "fft".equals(inPath.name);
        
        var nexts = map.get(inPath.name);
        if (nexts == null)
            return 0;
        
        count = nexts.sumToLong(next -> countPath2(new InPath(next, hasDac, hasFft), map, paths));
        paths.put(inPath, count);
        return count;
    }
    
    Object calculate2(FuncList<String> lines) {
        var map
            = lines
            .map(grab(regex("[a-z]+")))
            .toMap(
                items -> items.get(0),
                items -> items.skip(1).toFuncList());
        
        return countPath2(new InPath("svr", false, false), map, new ConcurrentHashMap<>());
    }
    
    //== Test ==
    
    @Test
    public void testExample() {
        var lines  = readAllLines();
        var result = calculate2(lines);
        println("result: " + result);
        assertAsString("2", result);
    }
    
    @Test
    public void testProd() {
        var lines  = readAllLines();
        var result = calculate2(lines);
        println("result: " + result);
        assertAsString("502447498690860", result);
    }
    
}
