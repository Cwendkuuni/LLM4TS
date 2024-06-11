import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.euclidean.twod.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end = new Vector2D(3.0, 4.0);
        SubLine subLine = new SubLine(start, end);
        Line line = (Line) subLine.getHyperplane();
        IntervalsSet intervalsSet = subLine.getRemainingRegion();

        assertEquals(line.getStart(), start);
        assertEquals(line.getEnd(), end);
        assertEquals(intervalsSet.size(), 1);
        Interval interval = intervalsSet.get(0);
        assertEquals(interval.getInf().getX(), start.getX());
        assertEquals(interval.getSup().getX(), end.getX());
    }

    @Test
    public void testSubLineGetSegments() {
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end = new Vector2D(3.0, 4.0);
        SubLine subLine = new SubLine(start, end);
        List<Segment> segments = subLine.getSegments();

        assertEquals(segments.size(), 1);
        Segment segment = segments.get(0);
        assertEquals(segment.getStart(), start);
        assertEquals(segment.getEnd(), end);
    }

    @Test
    public void testSubLineIntersection() {
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1 = new Vector2D(3.0, 4.0);
        Vector2D start2 = new Vector2D(4.0, 5.0);
        Vector2D end2 = new Vector2D(6.0, 7.0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);

        start2 = new Vector2D(1.5, 2.5);
        end2 = new Vector2D(4.5, 5.5);
        subLine2 = new SubLine(start2, end2);

        intersectionPoint = subLine1.intersection(subLine2, true);
        Vector2D expectedIntersectionPoint = new Vector2D(2.0, 3.0);
        assertEquals(intersectionPoint, expectedIntersectionPoint);
    }

    @Test
    public void testSubLineIntersectionWithEmptySubLine() {
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1 = new Vector2D(3.0, 4.0);
        Vector2D start2 = new Vector2D(5.0, 6.0);
        Vector2D end2 = new Vector2D(5.0, 6.0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);
    }

    @Test
    public void testSubLineIntersectionWithOneSegmentSubLine() {
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1 = new Vector2D(3.0, 4.0);
        Vector2D start2 = new Vector2D(3.5, 4.5);
        Vector2D end2 = new Vector2D(4.5, 5.5);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);
    }

    @Test
    public void testSubLineIntersectionWithParallelLines() {
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1 = new Vector2D(3.0, 4.0);
        Vector2D start2 = new Vector2D(3.0, 5.0);
        Vector2D end2 = new Vector2D(5.0, 5.0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);
    }

    @Test
    public void testSubLineIntersectionWithLinesWithDifferentAngles() {
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1 = new Vector2D(3.0, 4.0);
        Vector2D start2 = new Vector2D(3.0, 5.0);
        Vector2D end2 = new Vector2D(5.0, 2.0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);
    }

    @Test
    public void testSubLineIntersectionWithLinesWithSameAngleButDifferentOffsets() {
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1 = new Vector2D(3.0, 4.0);
        Vector2D start2 = new Vector2D(3.0, 4.0);
        Vector2D end2 = new Vector2D(5.0, 6.0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        Vector2D expectedIntersectionPoint = new Vector2D(2.5, 4.5);
        assertEquals(intersectionPoint, expectedIntersectionPoint);
    }

}