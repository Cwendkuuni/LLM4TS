import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link SubLine} class.
 */
public class SubLineTest {

    /**
     * Test constructor with hyperplane and remaining region.
     */
    @Test
    public void testConstructorWithHyperplaneAndRemainingRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.5, 1.5));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        Assert.assertEquals(new Vector2D(0.5, 0.0), segment.getStart());
        Assert.assertEquals(new Vector2D(1.0, 0.0), segment.getEnd());
    }

    /**
     * Test constructor with two endpoints.
     */
    @Test
    public void testConstructorWithTwoEndpoints() {
        Vector2D start = new Vector2D(1.0, 1.0);
        Vector2D end = new Vector2D(3.0, 3.0);
        SubLine subLine = new SubLine(start, end);
        List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        Assert.assertEquals(start, segment.getStart());
        Assert.assertEquals(end, segment.getEnd());
    }

    /**
     * Test constructor with a segment.
     */
    @Test
    public void testConstructorWithSegment() {
        Segment segment = new Segment(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0), new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0)));
        SubLine subLine = new SubLine(segment);
        List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(1, segments.size());
        Segment testSegment = segments.get(0);
        Assert.assertEquals(segment.getStart(), testSegment.getStart());
        Assert.assertEquals(segment.getEnd(), testSegment.getEnd());
    }

    /**
     * Test getSegments method.
     */
    @Test
    public void testGetSegmentsMethod() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 0.0));
        IntervalsSet intervalSet = new IntervalsSet(new Interval(0.5, 1.5), new Interval(3.0, 4.0), new Interval(5.0, 6.0));
        SubLine subLine = new SubLine(line, intervalSet);
        List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(3, segments.size());
        Segment testSegment1 = segments.get(0);
        Assert.assertEquals(new Vector2D(0.5, 0.0), testSegment1.getStart());
        Assert.assertEquals(new Vector2D(1.0, 0.0), testSegment1.getEnd());
        Segment testSegment2 = segments.get(1);
        Assert.assertEquals(new Vector2D(3.0, 0.0), testSegment2.getStart());
        Assert.assertEquals(new Vector2D(4.0, 0.0), testSegment2.getEnd());
        Segment testSegment3 = segments.get(2);
        Assert.assertEquals(new Vector2D(5.0, 0.0), testSegment3.getStart());
        Assert.assertEquals(new Vector2D(6.0, 0.0), testSegment3.getEnd());
    }

    /**
     * Test intersection method with two disjoint lines.
     */
    @Test
    public void testIntersectionWithTwoDisjointLines() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(2.0, 0.0), new Vector2D(3.0, 1.0));
        SubLine subLine1 = new SubLine(line1);
        SubLine subLine2 = new SubLine(line2);
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        Assert.assertNull(intersectionPoint);
    }

    /**
     * Test intersection method with two intersecting lines.
     */
    @Test
    public void testIntersectionWithTwoIntersectingLines() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(1.0, 0.0), new Vector2D(2.0, 1.0));
        SubLine subLine1 = new SubLine(line1);
        SubLine subLine2 = new SubLine(line2);
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        Assert.assertEquals(new Vector2D(1.0, 0.5), intersectionPoint);
    }

    /**
     * Test intersection method with two parallel lines.
     */
    @Test
    public void testIntersectionWithTwoParallelLines() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Line line2 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 0.0));
        SubLine subLine1 = new SubLine(line1);
        SubLine subLine2 = new SubLine(line2);
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        Assert.assertNull(intersectionPoint);
    }

    /**
     * Test intersection method with an infinite line and a bounded line.
     */
    @Test
    public void testIntersectionWithAnInfiniteLineAndABoundedLine() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        Line line2 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        SubLine subLine1 = new SubLine(line1);
        SubLine subLine2 = new SubLine(line2);
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        Assert.assertEquals(new Vector2D(0.0, 0.0), intersectionPoint);
    }

}