package day11;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import common.BaseTest;
import functionalj.list.FuncList;
import functionalj.map.FuncMap;

public class Day11Part1Test extends BaseTest {
    
    long countPath1(String name, FuncMap<String, FuncList<String>> map, ConcurrentHashMap<String, Long> paths) {
        if ("out".equals(name))
            return 1;
        
        var nexts = map.get(name);
        return nexts
                .sumToLong(next -> {
                    var pathCount = paths.get(next);
                    if (pathCount == null) {
                        pathCount = countPath1(next, map, paths);
                        paths.put(next, pathCount);
                    }
                    return pathCount;
                });
    }
    
    Object calculate1(FuncList<String> lines) {
        var map
            = lines
            .map(grab(regex("[a-z]+")))
            .toMap(items -> items.get(0), items -> items.skip(1).toFuncList());
        println();
        
        map
        .entries()
        .forEach(println);
        println();
        
        return countPath1("you", map, new ConcurrentHashMap<String, Long>());
    }
    
    //== Test ==
    
    @Test
    public void testExample() {
        var lines  = readAllLines();
        var result = calculate1(lines);
        println("result: " + result);
        assertAsString("5", result);
    }
    
    @Test
    public void testProd() {
        var lines  = readAllLines();
        var result = calculate1(lines);
        println("result: " + result);
        assertAsString("", result);
    }
    
}
