package day8;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import common.BaseTest;
import functionalj.list.FuncList;
import lombok.Value;
import lombok.experimental.Accessors;

public class Day8Part1Test extends BaseTest {
    
    static final Point origin = new Point(0, 0, 0);
    
    static record Point(long x, long y, long z) implements Comparable<Point> {
        long distanceSquarTo(Point anotherPoint) {
            var deltaX = (x-anotherPoint.x);
            var deltaY = (y-anotherPoint.y);
            var deltaZ = (z-anotherPoint.z);
            return deltaX * deltaX
                 + deltaY * deltaY
                 + deltaZ * deltaZ;
        }
        long distanceSquarToOrigin() {
            return distanceSquarTo(origin);
        }
        
        @Override
        public int compareTo(Point that) {
            var thisDistanceToOrigin = this.distanceSquarToOrigin();
            var thatDistanceToOrigin = that.distanceSquarToOrigin();
            return Long.compare(thisDistanceToOrigin, thatDistanceToOrigin);
        }
    }
    
    @Value
    @Accessors(fluent = true)
    static class Connection {
        private final Point a;
        private final Point b;
        private final long distanceSquar;
        Connection(Point p1, Point p2) {
            if (p1.compareTo(p2) <= 0) {
                this.a = p1;
                this.b = p2;
            } else {
                this.a = p2;
                this.b = p1;
            }
            this.distanceSquar = this.a.distanceSquarTo(this.b);
        }
        FuncList<Point> points() { return FuncList.of(a, b).sorted(); }
        
        @Override
        public int hashCode() {
            return Objects.hash(a) + Objects.hash(b);
        }
        @Override
        public boolean equals(Object o) {
            return (o instanceof Connection that)
                    ? (a.equals(that.a()) && b.equals(that.b())) || (a.equals(that.b()) && b.equals(that.a()))
                    : false;
        }
    }
    @Value
    @Accessors(fluent = true)
    static class Curcuit {
        final FuncList<Point> points;
        Curcuit(FuncList<Point> points) {
            this.points = points.sorted().distinct().toFuncList();
        }
        FuncList<Point> points() { return points; }
    }
    
    static class Curcuits {
        private final Map<Point, Curcuit> circuits = new ConcurrentHashMap<>();
        
        FuncList<Curcuit> circuits() {
            return FuncList.from(circuits.values());
        }
        
        void merge(Connection connection) {
            var circuitA = FuncList.from(circuits.values()).findFirst(circuit -> circuit.points.contains(connection.points().get(0))).orElse(null);
            var circuitB = FuncList.from(circuits.values()).findFirst(circuit -> circuit.points.contains(connection.points().get(1))).orElse(null);
            
            // Remove them out first -- just in case it will change.
            if (circuitA != null) circuits.remove(circuitA.points.get(0));
            if (circuitB != null) circuits.remove(circuitB.points.get(0));
            
            // The connection is new ... not part of any circuit
            if ((circuitA == null) && (circuitB == null)) {
                var newCircuit = new Curcuit(connection.points());
                circuits.put(newCircuit.points().get(0), newCircuit);
            } else if ((circuitA == null) || (circuitB == null)) {
                var newCircuit = (circuitA != null) ? circuitA : circuitB;
                newCircuit = new Curcuit(newCircuit.points().appendAll(connection.points()));
                circuits.put(newCircuit.points().get(0), newCircuit);
            } else if (circuitA.equals(circuitB)) {
                // Same circuit .... so nothing to combine .. add it back
                circuits.put(circuitA.points().get(0), circuitA);
            } else {
                var newCircuit = new Curcuit(circuitA.points().appendAll(circuitB.points()));
                circuits.put(newCircuit.points().get(0), newCircuit);
            }
        }
        
        public int size() {
            return circuits.size();
        }
        
    }
    
    Object calculate(FuncList<String> lines, int limit) {
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
        
        // Get the `limit` nearest connections 
        var nearestConnections
            = allConnections
            .sortedBy(Connection::distanceSquar)
            .distinct()
            .limit(limit)
            .toFuncList();
        
        var circuits = new Curcuits();
        for (var connection : nearestConnections) {
            circuits.merge(connection);
        }
        
        return FuncList.from(circuits.circuits.values())
                .mapToLong(c -> c.points().size())
                .sorted()
                .reverse()
                .limit(3)
                .product()
                .getAsLong();
    }
    
    //== Test ==
    
    @Test
    public void testExample() {
        var lines  = readAllLines();
        var result = calculate(lines, 10);
        println("result: " + result);
        assertAsString("40", result);
    }
    
    @Test
    public void testProd() {
        var lines  = readAllLines();
        var result = calculate(lines, 1000);
        println("result: " + result);
        assertAsString("131150", result);
    }
    
}
