import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        // Test simple constructor
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine = new SubLine(hyperplane, new IntervalsSet(new Interval(0, 1)));
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(new IntervalsSet(new Interval(0, 1)), subLine.getRemainingRegion());

        // Test constructor from two endpoints
        Vector2D start = new Vector2D(0, 0);
        Vector2D end   = new Vector2D(1, 1);
        SubLine subLineFromEndpoints = new SubLine(start, end);
        Hyperplane<Euclidean2D> expectedHyperplane = new Line(start, end);
        assertEquals(expectedHyperplane.getClass(), subLineFromEndpoints.getHyperplane().getClass());
        assertEquals(expectedHyperplane, subLineFromEndpoints.getHyperplane());
        assertEquals(new IntervalsSet(start, end), subLineFromEndpoints.getRemainingRegion());

        // Test constructor from a segment
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 1), new Line(new Vector2D(0, 0), new Vector2D(1, 1)));
        SubLine subLineFromSegment = new SubLine(segment);
        assertEquals(expectedHyperplane, subLineFromSegment.getHyperplane());
        assertEquals(new IntervalsSet(segment.getStart(), segment.getEnd()), subLineFromSegment.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        // Test a simple line with no intervals
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine = new SubLine(line, new IntervalsSet());
        List<Segment> result = subLine.getSegments();
        assertTrue(result.isEmpty());

        // Test a line with one interval
        Interval interval = new Interval(0.5, 0.6);
        SubLine subLineWithOneInterval = new SubLine(line, new IntervalsSet(interval));
        result = subLineWithOneInterval.getSegments();
        assertEquals(1, result.size());
        Segment segment = result.get(0);
        Vector2D expectedStart = line.toSpace(new Vector1D(interval.getInf()));
        Vector2D expectedEnd   = line.toSpace(new Vector1D(interval.getSup()));
        assertEquals(expectedStart, segment.getStart());
        assertEquals(expectedEnd, segment.getEnd());

        // Test a line with multiple intervals
        interval = new Interval(0.1, 0.2);
        Interval interval2 = new Interval(0.3, 0.4);
        SubLine subLineWithMultipleIntervals = new SubLine(line, new IntervalsSet(interval, interval2));
        result = subLineWithMultipleIntervals.getSegments();
        assertEquals(2, result.size());
        for (int i = 0; i < result.size(); i++) {
            segment = result.get(i);
            Vector2D start = line.toSpace(new Vector1D(interval.getInf() + i * 0.1));
            Vector2D end = line.toSpace(new Vector1D(interval.getInf() + i * 0.1 + interval.getLength()));
            assertEquals(start, segment.getStart());
            assertEquals(end, segment.getEnd());
        }
    }

    @Test
    public void testIntersection() {
        // Test intersection with the same line
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        Vector2D expected = line1.intersection(line1);
        Vector2D result = subLine1.intersection(subLine2, true);
        assertEquals(expected, result);

        // Test intersection with a different line
        Line line2 = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        SubLine subLine3 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine4 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));
        result = subLine3.intersection(subLine4, true);
        assertNull(result);

        // Test intersection with lines that don't intersect
        Line line3 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        SubLine subLine5 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine6 = new SubLine(line3, new IntervalsSet(new Interval(0, 1)));
        result = subLine5.intersection(subLine6, true);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithNullSubLine() {
        SubLine subLine1 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = null;
        Vector2D result = subLine1.intersection(subLine2, true);
    }
}