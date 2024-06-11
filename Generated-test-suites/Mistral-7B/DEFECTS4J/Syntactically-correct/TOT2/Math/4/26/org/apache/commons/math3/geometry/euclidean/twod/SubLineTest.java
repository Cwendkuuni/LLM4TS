import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubHyperplane;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testEmptyConstructor() {
        SubLine subLine = new SubLine(null, null);
        assertTrue(subLine.getSegments().isEmpty());
    }

    @Test
    public void testLineConstructor() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        SubLine subLine = new SubLine(hyperplane, null);
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        Vector2D start = segment.getStart();
        Vector2D end = segment.getEnd();
        assertEquals(new Vector2D(0.0, 0.0), start);
        assertEquals(new Vector2D(1.0, 1.0), end);
    }

    @Test
    public void testSegmentConstructor() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 1.0);
        Segment segment = new Segment(start, end, new Line(start, end));
        SubLine subLine = new SubLine(segment);
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment testSegment = segments.get(0);
        Vector2D testStart = testSegment.getStart();
        Vector2D testEnd = testSegment.getEnd();
        assertEquals(start, testStart);
        assertEquals(end, testEnd);
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Interval interval1 = new Interval(0.0, 1.0);
        Interval interval2 = new Interval(1.5, 3.0);
        IntervalsSet intervals = new IntervalsSet(interval1, interval2);
        SubLine subLine = new SubLine(line, intervals);
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        Segment segment1 = segments.get(0);
        Vector2D start1 = segment1.getStart();
        Vector2D end1 = segment1.getEnd();
        Vector2D expectedStart1 = new Vector2D(0.0, 0.0);
        Vector2D expectedEnd1 = new Vector2D(1.0, 1.0);
        assertEquals(expectedStart1, start1);
        assertEquals(expectedEnd1, end1);
        Segment segment2 = segments.get(1);
        Vector2D start2 = segment2.getStart();
        Vector2D end2 = segment2.getEnd();
        Vector2D expectedStart2 = new Vector2D(1.5, 1.5);
        Vector2D expectedEnd2 = new Vector2D(3.0, 3.0);
        assertEquals(expectedStart2, start2);
        assertEquals(expectedEnd2, end2);
    }

    @Test
    public void testIntersectionPositiveCase() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        SubLine subLine1 = new SubLine(line1, null);
        SubLine subLine2 = new SubLine(line2, null);
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        Vector2D expectedIntersectionPoint = new Vector2D(1.0, 1.0);
        assertEquals(expectedIntersectionPoint, intersectionPoint);
    }

    @Test
    public void testIntersectionNegativeCase() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(2.0, 2.0), new Vector2D(3.0, 3.0));
        SubLine subLine1 = new SubLine(line1, null);
        SubLine subLine2 = new SubLine(line2, null);
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(null, intersectionPoint);
    }

    @Test
    public void testIntersectionEdgeCases() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(1.0, 2.0));
        SubLine subLine1 = new SubLine(line1, null);
        SubLine subLine2 = new SubLine(line2, null);
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(null, intersectionPoint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionNullSubLine() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        SubLine subLine1 = new SubLine(line1, null);
        SubLine subLine2 = null;
        subLine1.intersection(subLine2, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionNullIncludeEndPoints() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        SubLine subLine1 = new SubLine(line1, null);
        SubLine subLine2 = new SubLine(line1, null);
        subLine1.intersection(subLine2, false);
    }
}