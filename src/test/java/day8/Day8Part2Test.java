package day8;

import static org.junit.Assert.fail;

import org.junit.Test;

import functionalj.list.FuncList;

public class Day8Part2Test extends Day8Part1Test {
    
    Object calculate(FuncList<String> lines) {
        var points
            = lines
            .map(grab(regex("[0-9]+")))
            .map(nums -> nums.mapToLong(Long::parseLong))
            .map(nums -> new Point(nums.get(0), nums.get(1), nums.get(2)));
        
        var allConnections
            = points
            .flatMap(a -> {
                return points
                        .exclude(a::equals)                 // Exclude self
                        .map    (b ->  new Connection(a, b));
            });
        
        var nearestConnections
            = allConnections
            .sortedBy(Connection::distanceSquar)
            .distinct()
            .toFuncList();
        
        var circuits = new Curcuits();
        var iterator   = nearestConnections.iterator();
        var pointCount = points.size();
        while (iterator.hasNext()) {
            var connection = iterator.next();
            circuits.merge(connection);
            
          if (circuits.size() == 1 && circuits.circuits().findFirst().get().points().size() == pointCount) {
              return connection.a().x()*connection.b().x();
          }
        }
        
        fail("Should find something!");
        return null;
    }
    
    //== Test ==
    
    @Test
    public void testExample() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("25272", result);
    }
    
    @Test
    public void testProd() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("2497445", result);
    }
    
}
