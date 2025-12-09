package day9;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import org.junit.Ignore;
import org.junit.Test;

import common.BaseTest;
import functionalj.list.FuncList;
import functionalj.list.FuncListBuilder;

public class Day9Part2Test extends BaseTest {
    
    record Point(int x, int y){}
    
    Object calculate(FuncList<String> lines) {
        var points
            = lines
            .map(grab(regex("[0-9]+")))
            .map(nums -> nums.mapToInt(Integer::parseInt))
            .map(nums -> new Point(nums.get(0), nums.get(1)));
        
        points.forEach(this::println);
        println();
        
        long largest = -1L;
        for (var pointA : points) {
            for (var pointB : points) {
                if (pointA.equals(pointB))
                    continue;
                
                var onEdgePoints = new FuncListBuilder<Point>();
                var isValid = true;
                for (var pointC : points) {
                    if (pointA.equals(pointC)) continue;
                    if (pointB.equals(pointC)) continue;
                    
                    var minX = min(pointA.x, pointB.x);
                    var maxX = max(pointA.x, pointB.x);
                    var minY = min(pointA.y, pointB.y);
                    var maxY = max(pointA.y, pointB.y);
                    var pointX = pointC.x;
                    var pointY = pointC.y;
                    
                    var isInside
                        = ((pointX > minX) && (pointX < maxX)
                        && (pointY > minY) && (pointY < maxY));
                    var isOnEdge
                            =  (((pointX == minX) || (pointX == maxX)) && ((pointY >= minY) && (pointY <= maxY)))
                            || (((pointY == minY) || (pointY == maxY)) && ((pointX >= minX) && (pointX <= maxX)));
                    
                    // Check if pointC is within the rectangle
                    if (isInside) isValid = false;
                    if (isOnEdge) onEdgePoints.add(pointC);
                }
                
                if (!isValid)
                    continue;
                
                var onLine = false;
                for (var pointOnEdgeA : onEdgePoints.build()) {
                    for (var pointOnEdgeB : onEdgePoints.build()) {
                        if (pointOnEdgeA.equals(pointOnEdgeB)) continue;
                        if (pointOnEdgeA.x == pointOnEdgeB.x) onLine = true;
                        if (pointOnEdgeA.y == pointOnEdgeB.y) onLine = true;
                    }
                }
                
                var area = (abs(pointA.x - pointB.x) + 1)*(abs(pointA.y - pointB.y) + 1);
                
                if (onLine)
                    continue;
                if (area > largest)
                    largest = area;
            }
        }
        
        return largest;
    }
    
    //== Test ==
    
    @Test
    public void testExample() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("24", result);
    }
    
    @Ignore
    @Test
    public void testProd() {
        var lines  = readAllLines();
        var result = calculate(lines);
        println("result: " + result);
        assertAsString("", result);
    }
    
}
