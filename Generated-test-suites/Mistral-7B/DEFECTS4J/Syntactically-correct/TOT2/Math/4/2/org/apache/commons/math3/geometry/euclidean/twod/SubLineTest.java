import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Points2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the {@link SubLine} class.
 */
public class SubLineTest {

    /**
     * Tests the constructor with two endpoints.
     */
    @Test
    public void testConstructorTwoEndpoints() {
        // given
        final Vector2D pt1 = new Vector2D(1.0, 2.0);
        final Vector2D pt2 = new Vector2D(3.0, 4.0);
        final SubLine subLine = new SubLine(pt1, pt2);

        // then
        assertEquals("getHyperplane() should return the expected line", new Line(pt1, pt2), subLine.getHyperplane());
        assertEquals("getRemainingRegion() should return the expected interval set", new IntervalsSet(new Interval(pt1.getX(), pt2.getX())), subLine.getRemainingRegion());
    }

    /**
     * Tests the constructor with a single segment.
     */
    @Test
    public void testConstructorSegment() {
        // given
        final Vector2D pt1 = new Vector2D(1.0, 2.0);
        final Vector2D pt2 = new Vector2D(3.0, 4.0);
        final Segment segment = new Segment(pt1, pt2);
        final SubLine subLine = new SubLine(segment);

        // then
        assertEquals("getHyperplane() should return the expected line", new Line(pt1, pt2), subLine.getHyperplane());
        assertEquals("getRemainingRegion() should return the expected interval set", new IntervalsSet(new Interval(pt1.getX(), pt2.getX())), subLine.getRemainingRegion());
    }

    /**
     * Tests the getSegments() method.
     */
    @Test
    public void testGetSegments() {
        // given
        final Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(5.0, 5.0));
        final IntervalsSet intervalSet = new IntervalsSet(new Interval(1.0, 3.0), new Interval(4.0, 5.0));
        final SubLine subLine = new SubLine(line, intervalSet);

        // when
        final List<Segment> segments = subLine.getSegments();

        // then
        assertEquals("getSegments() should return the expected number of segments", 2, segments.size());
        assertEquals("getSegments()[0] should return the expected segment", new Segment(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0), line), segments.get(0));
        assertEquals("getSegments()[1] should return the expected segment", new Segment(new Vector2D(4.0, 4.0), new Vector2D(5.0, 5.0), line), segments.get(1));
    }

    /**
     * Tests the intersection() method with two sub-lines intersecting.
     */
    @Test
    public void testIntersectionTwoSubLinesIntersecting() {
        // given
        final Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(5.0, 5.0));
        final IntervalsSet intervalSet1 = new IntervalsSet(new Interval(1.0, 3.0), new Interval(4.0, 5.0));
        final SubLine subLine1 = new SubLine(line1, intervalSet1);
        final Line line2 = new Line(new Vector2D(3.0, 1.0), new Vector2D(7.0, 3.0));
        final IntervalsSet intervalSet2 = new IntervalsSet(new Interval(2.0, 6.0));
        final SubLine subLine2 = new SubLine(line2, intervalSet2);

        // when
        final Vector2D intersection = subLine1.intersection(subLine2, true);

        // then
        assertNotNull("intersection() should return the expected intersection point", intersection);
        assertEquals("intersection() should return the expected coordinates of the intersection point", new Vector2D(3.0, 1.5), intersection);
    }

    /**
     * Tests the intersection() method with two sub-lines not intersecting.
     */
    @Test
    public void testIntersectionTwoSubLinesNotIntersecting() {
        // given
        final Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(5.0, 5.0));
        final IntervalsSet intervalSet1 = new IntervalsSet(new Interval(1.0, 3.0), new Interval(4.0, 5.0));
        final SubLine subLine1 = new SubLine(line1, intervalSet1);
        final Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0));
        final IntervalsSet intervalSet2 = new IntervalsSet(new Interval(1.1, 2.9), new Interval(3.1, 4.9));
        final SubLine subLine2 = new SubLine(line2, intervalSet2);

        // when
        final Vector2D intersection = subLine1.intersection(subLine2, true);

        // then
        assertNull("intersection() should return null if the sub-lines don't intersect");
    }

    /**
     * Tests the intersection() method with two sub-lines and includeEndPoints set to false.
     */
    @Test
    public void testIntersectionTwoSubLinesNotIntersectingIncludeEndPointsFalse() {
        // given
        final Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(5.0, 5.0));
        final IntervalsSet intervalSet1 = new IntervalsSet(new Interval(1.0, 3.0), new Interval(4.0, 5.0));
        final SubLine subLine1 = new SubLine(line1, intervalSet1);
        final Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0));
        final IntervalsSet intervalSet2 = new IntervalsSet(new Interval(1.1, 2.9), new Interval(3.1, 4.9));
        final SubLine subLine2 = new SubLine(line2, intervalSet2);

        // when
        final Vector2D intersection = subLine1.intersection(subLine2, false);

        // then
        assertNull("intersection() should return null if the sub-lines don't intersect and includeEndPoints is false");
    }

    /**
     * Tests the intersection() method with two sub-lines intersecting at an endpoint.
     */
    @Test
    public void testIntersectionTwoSubLinesIntersectingAtEndpoint() {
        // given
        final Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(5.0, 5.0));
        final IntervalsSet intervalSet1 = new IntervalsSet(new Interval(1.0, 1.0), new Interval(5.0, 5.0));
        final SubLine subLine1 = new SubLine(line1, intervalSet1);
        final Line line2 = new Line(new Vector2D(5.0, 0.0), new Vector2D(5.0, 5.0));
        final IntervalsSet intervalSet2 = new IntervalsSet(new Interval(5.0, 5.0));
        final SubLine subLine2 = new SubLine(line2, intervalSet2);

        // when
        final Vector2D intersection = subLine1.intersection(subLine2, true);

        // then
        assertNotNull("intersection() should return the expected intersection point", intersection);
        assertEquals("intersection() should return the expected coordinates of the intersection point", new Vector2D(5.0, 5.0), intersection);
    }

    /**
     * Tests the intersection() method with two infinite sub-lines.
     */
    @Test
    public void testIntersectionTwoInfiniteSubLines() {
        // given
        final Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        final IntervalsSet intervalSet1 = new IntervalsSet();
        final SubLine subLine1 = new SubLine(line1, intervalSet1);
        final Line line2 = new Line(new Vector2D(0.0, 1.0), new Vector2D(1.0, 0.0));
        final IntervalsSet intervalSet2 = new IntervalsSet();
        final SubLine subLine2 = new SubLine(line2, intervalSet2);

        // when
        final Vector2D intersection = subLine1.intersection(subLine2, true);

        // then
        assertNull("intersection() should return null for two infinite sub-lines");
    }

    /**
     * Tests the intersection() method with two infinite sub-lines and includeEndPoints set to false.
     */
    @Test
    public void testIntersectionTwoInfiniteSubLinesIncludeEndPointsFalse() {
        // given
        final Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        final IntervalsSet intervalSet1 = new IntervalsSet();
        final SubLine subLine1 = new SubLine(line1, intervalSet1);
        final Line line2 = new Line(new Vector2D(0.0, 1.0), new Vector2D(1.0, 0.0));
        final IntervalsSet intervalSet2 = new IntervalsSet();
        final SubLine subLine2 = new SubLine(line2, intervalSet2);

        // when
        final Vector2D intersection = subLine1.intersection(subLine2, false);

        // then
        assertNull("intersection() should return null for two infinite sub-lines and includeEndPoints is false");
    }

}