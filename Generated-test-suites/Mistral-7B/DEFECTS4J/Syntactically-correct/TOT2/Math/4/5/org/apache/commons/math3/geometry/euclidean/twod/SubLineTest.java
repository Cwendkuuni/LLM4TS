import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.test.AbstractMathTestCase;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for {@link SubLine}.
 */
public class SubLineTest extends AbstractMathTestCase {

    /**
     * Test simple constructor.
     */
    @Test
    public void testConstructor() {
        final Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(5, 5));
        final Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(3, 7));
        final SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    /**
     * Test constructor from two endpoints.
     */
    @Test
    public void testConstructorFromTwoEndpoints() {
        final Vector2D start = new Vector2D(1, 1);
        final Vector2D end = new Vector2D(4, 4);
        final SubLine subLine = new SubLine(start, end);
        assertEquals(new Line(start, end), subLine.getHyperplane());
        final List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
    }

    /**
     * Test constructor from a segment.
     */
    @Test
    public void testConstructorFromSegment() {
        final Vector2D start = new Vector2D(1, 1);
        final Vector2D end = new Vector2D(4, 4);
        final Segment segment = new Segment(start, end, new Line(start, end));
        final SubLine subLine = new SubLine(segment);
        assertEquals(new Line(start, end), subLine.getHyperplane());
        final List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
    }

    /**
     * Test getSegments method.
     */
    @Test
    public void testGetSegments() {
        final Line line = new Line(new Vector2D(0, 0), new Vector2D(5, 5));
        final IntervalsSet intervalsSet = new IntervalsSet(new Interval(3, 7), new Interval(10, 15), new Interval(20, 25));
        final SubLine subLine = new SubLine(line, intervalsSet);
        final List<Segment> segments = subLine.getSegments();
        assertEquals(3, segments.size());
        assertEquals(new Vector2D(3, 3), segments.get(0).getStart());
        assertEquals(new Vector2D(3, 5), segments.get(0).getEnd());
        assertEquals(new Vector2D(10, 10), segments.get(1).getStart());
        assertEquals(new Vector2D(10, 15), segments.get(1).getEnd());
        assertEquals(new Vector2D(20, 20), segments.get(2).getStart());
        assertEquals(new Vector2D(20, 25), segments.get(2).getEnd());
    }

    /**
     * Test intersection method with no intersection.
     */
    @Test
    public void testIntersectionNoIntersection() {
        final Line line1 = new Line(new Vector2D(0, 0), new Vector2D(5, 5));
        final Line line2 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));
        final SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(3, 7)));
        final SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 5)));
        assertNull(subLine1.intersection(subLine2, false));
        assertNull(subLine1.intersection(subLine2, true));
    }

    /**
     * Test intersection method with intersection on boundary.
     */
    @Test
    public void testIntersectionOnBoundary() {
        final Line line1 = new Line(new Vector2D(0, 0), new Vector2D(5, 5));
        final Line line2 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));
        final SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(3, 7)));
        final SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(3, 3)));
        assertNull(subLine1.intersection(subLine2, false));
        assertNull(subLine1.intersection(subLine2, true));
    }

    /**
     * Test intersection method with intersection inside.
     */
    @Test
    public void testIntersectionInside() {
        final Line line1 = new Line(new Vector2D(0, 0), new Vector2D(5, 5));
        final Line line2 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));
        final SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(3, 7)));
        final SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2, 4)));
        final Vector2D intersection = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(3, 3), intersection);
        assertTrue(subLine1.intersection(subLine2, true) instanceof Vector2D);
        assertEquals(new Vector2D(3, 3), subLine1.intersection(subLine2, true));
    }

    /**
     * Test intersection method with infinite lines.
     */
    @Test
    public void testIntersectionInfiniteLines() {
        final Line line1 = new Line(new Vector2D(0, 0), new Vector2D(5, 5));
        final Line line2 = new Line(new Vector2D(-5, -5), new Vector2D(5, 5));
        final SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(3, 7)));
        final SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 9)));
        final Vector2D intersection = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(3, 3), intersection);
        assertTrue(subLine1.intersection(subLine2, true) instanceof Vector2D);
        assertEquals(new Vector2D(3, 3), subLine1.intersection(subLine2, true));
    }

    /**
     * Test intersection method with parallel lines.
     */
    @Test
    public void testIntersectionParallelLines() {
        final Line line1 = new Line(new Vector2D(0, 0), new Vector2D(5, 5));
        final Line line2 = new Line(new Vector2D(1, 1), new Vector2D(6, 6));
        final SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(3, 7)));
        final SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2, 8)));
        assertNull(subLine1.intersection(subLine2, false));
        assertNull(subLine1.intersection(subLine2, true));
    }

    /**
     * Test split method with no intersection.
     */
    @Test
    public void testSplitNoIntersection() {
        final Line line1 = new Line(new Vector2D(0, 0), new Vector2D(5, 5));
        final Line line2 = new Line(new Vector2D(-5, -5), new Vector2D(5, 5));
        final SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(3, 7)));
        final Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1, 1), new Vector2D(3, 3));
        final SubHyperplane<Euclidean1D> split = subLine1.split(hyperplane);
        assertEquals(null, split.getSubHyperplane1());
        assertEquals(subLine1, split.getSubHyperplane2());
    }

    /**
     * Test split method with intersection on boundary.
     */
    @Test
    public void testSplitOnBoundary() {
        final Line line1 = new Line(new Vector2D(0, 0), new Vector2D(5, 5));
        final Line line2 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));
        final SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(3, 7)));
        final Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(3, 3), new Vector2D(3, 5));
        final SubHyperplane<Euclidean1D> split = subLine1.split(hyperplane);
        assertEquals(new SubLine(new Line(new Vector2D(3, 3), new Vector2D(3, 5)), new IntervalsSet(new Interval(3, 3))), split.getSubHyperplane1());
        assertEquals(subLine1, split.getSubHyperplane2());
    }

    /**
     * Test split method with intersection inside.
     */
    @Test
    public void testSplitInside() {
        final Line line1 = new Line(new Vector2D(0, 0), new Vector2D(5, 5));
        final Line line2 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));
        final SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(3, 7)));
        final Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(2, 2), new Vector2D(4, 4));
        final SubHyperplane<Euclidean1D> split = subLine1.split(hyperplane);
        assertEquals(new SubLine(new Line(new Vector2D(3, 3), new Vector2D(3, 5)), new IntervalsSet(new Interval(3, 3))), split.getSubHyperplane1());
        assertEquals(new SubLine(new Line(new Vector2D(0, 0), new Vector2D(2, 2)), new IntervalsSet(new Interval(0, 3), new Interval(5, 5))), split.getSubHyperplane2());
    }

    /**
     * Test split method with infinite lines.
     */
    @Test
    public void testSplitInfiniteLines() {
        final Line line1 = new Line(new Vector2D(0, 0), new Vector2D(5, 5));
        final Line line2 = new Line(new Vector2D(-5, -5), new Vector2D(5, 5));
        final SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(3, 7)));
        final Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1, 1), new Vector2D(-1, 1));
        final SubHyperplane<Euclidean1D> split = subLine1.split(hyperplane);
        assertEquals(new SubLine(line1, new IntervalsSet(new Interval(3, 7))), split.getSubHyperplane1());
        assertEquals(new SubLine(line2, new IntervalsSet(new Interval(3, 7))), split.getSubHyperplane2());
    }

    /**
     * Test split method with parallel lines.
     */
    @Test
    public void testSplitParallelLines() {
        final Line line1 = new Line(new Vector2D(0, 0), new Vector2D(5, 5));
        final Line line2 = new Line(new Vector2D(1, 1), new Vector2D(6, 6));
        final SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(3, 7)));
        final Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(2, 2), new Vector2D(4, 4));
        final SubHyperplane<Euclidean1D> split = subLine1.split(hyperplane);
        assertEquals(null, split.getSubHyperplane1());
        assertEquals(subLine1, split.getSubHyperplane2());
    }

}