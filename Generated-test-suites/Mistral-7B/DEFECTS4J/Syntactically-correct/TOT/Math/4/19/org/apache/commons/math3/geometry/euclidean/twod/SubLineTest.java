package org.apache.commons.math3.geometry.euclidean.twod.test;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link SubLine}.
 */
public class SubLineTest {

    /**
     * Test construction from hyperplane and remaining region.
     */
    @Test
    public void testConstructionFromHyperplaneAndRemainingRegion() {
        // create a line and intervals set
        final Vector2D start = new Vector2D(1.0, 1.0);
        final Vector2D end   = new Vector2D(2.0, 2.0);
        final Line line = new Line(start, end);
        final IntervalsSet intervals = new IntervalsSet(new Interval(0.5, 0.8), new Interval(1.2, 1.5));

        // create a sub-line
        final SubLine subLine = new SubLine(line, intervals);

        // check the sub-line was constructed correctly
        assertEquals(line, subLine.getHyperplane());
        assertEquals(intervals, subLine.getRemainingRegion());
    }

    /**
     * Test creation from two endpoints.
     */
    @Test
    public void testConstructionFromTwoEndpoints() {
        // create two points
        final Vector2D start = new Vector2D(1.0, 1.0);
        final Vector2D end   = new Vector2D(2.0, 2.0);

        // create a sub-line
        final SubLine subLine = new SubLine(start, end);

        // check the sub-line was constructed correctly
        assertEquals(new Line(start, end), subLine.getHyperplane());
        assertEquals(buildIntervalSet(start, end), subLine.getRemainingRegion());
    }

    /**
     * Test creation from a segment.
     */
    @Test
    public void testConstructionFromSegment() {
        // create a segment
        final Vector2D start = new Vector2D(1.0, 1.0);
        final Vector2D end   = new Vector2D(2.0, 2.0);

        // create a sub-line
        final Segment segment = new Segment(start, end);
        final SubLine subLine = new SubLine(segment);

        // check the sub-line was constructed correctly
        assertEquals(segment.getLine(), subLine.getHyperplane());
        assertEquals(buildIntervalSet(start, end), subLine.getRemainingRegion());
    }

    /**
     * Test getting endpoints.
     */
    @Test
    public void testGettingEndpoints() {
        // create a sub-line with 2 segments
        final Vector2D start1 = new Vector2D(1.0, 1.0);
        final Vector2D end1   = new Vector2D(2.0, 2.0);
        final Vector2D start2 = new Vector2D(3.0, 3.0);
        final Vector2D end2   = new Vector2D(4.0, 4.0);
        final SubLine subLine = new SubLine(start1, end1, start2, end2);

        // check the endpoints are returned correctly
        assertEquals(2, subLine.getSegments().size());
        assertEquals(start1, subLine.getSegments().get(0).getStart());
        assertEquals(end1,   subLine.getSegments().get(0).getEnd());
        assertEquals(start2, subLine.getSegments().get(1).getStart());
        assertEquals(end2,   subLine.getSegments().get(1).getEnd());
    }

    /**
     * Test intersection with another sub-line.
     */
    @Test
    public void testIntersectionWithSubLine() {
        // create two sub-lines
        final Vector2D start1 = new Vector2D(0.0, 0.0);
        final Vector2D end1   = new Vector2D(1.0, 1.0);
        final Vector2D start2 = new Vector2D(1.0, 1.0);
        final Vector2D end2   = new Vector2D(2.0, 2.0);
        final SubLine subLine1 = new SubLine(start1, end1);
        final SubLine subLine2 = new SubLine(start2, end2);

        // check the sub-lines don't intersect
        assertNull(subLine1.intersection(subLine2, false));

        // create a sub-line with an intersection point
        final Vector2D intersection = new Vector2D(0.5, 0.5);
        final SubLine subLine3 = new SubLine(start1, end1, start2, end2);
        assertNotNull(subLine3.intersection(subLine2, false));
        assertEquals(intersection, subLine3.intersection(subLine2, false));

        // check the sub-lines don't intersect when endpoints are not considered to belong to sub-line
        assertNull(subLine1.intersection(subLine2, true));
    }

    /**
     * Test get intersection of infinite lines.
     */
    @Test
    public void testGetIntersectionOfInfiniteLines() {
        // create two lines
        final Vector2D start1 = new Vector2D(1.0, 1.0);
        final Vector2D end1   = new Vector2D(1.0, 3.0);
        final Vector2D start2 = new Vector2D(2.0, 2.0);
        final Vector2D end2   = new Vector2D(2.0, 4.0);
        final Line line1 = new Line(start1, end1);
        final Line line2 = new Line(start2, end2);

        // create a sub-line from line1
        final SubLine subLine1 = new SubLine(line1);

        // create a sub-line from line2
        final SubLine subLine2 = new SubLine(line2);

        // check the lines don't intersect
        assertNull(subLine1.intersection(subLine2, false));

        // check the intersection of infinite lines
        final Vector2D intersection = new Vector2D(2.0, 2.0);
        assertEquals(intersection, subLine1.intersection(subLine2, false));
    }

    /**
     * Test side method.
     */
    @Test
    public void testSideMethod() {
        // create a line
        final Vector2D start = new Vector2D(1.0, 1.0);
        final Vector2D end   = new Vector2D(2.0, 2.0);
        final Line line = new Line(start, end);

        // create a sub-line
        final SubLine subLine = new SubLine(start, end);

        // check the side of the sub-line with respect to the line
        assertEquals(Side.POSITIVE, subLine.side(line));

        // create a line which is parallel to the sub-line
        final Line otherLine = new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 1.0));
        assertEquals(Side.ON_BOUNDARY, subLine.side(otherLine));

        // create a line which intersects the sub-line
        final Vector2D intersection = line.intersection(otherLine);
        assertEquals(Side.NEGATIVE, subLine.side(otherLine));
    }

    /**
     * Test split method.
     */
    @Test
    public void testSplitMethod() {
        // create a line
        final Vector2D start = new Vector2D(1.0, 1.0);
        final Vector2D end   = new Vector2D(2.0, 2.0);
        final Line line = new Line(start, end);

        // create a sub-line
        final SubLine subLine = new SubLine(start, end);

        // create a hyperplane which intersects the sub-line
        final Vector2D hyperplanePoint = new Vector2D(1.5, 1.5);
        final Hyperplane<Euclidean2D> hyperplane = new Line(hyperplanePoint, line.toSpace(hyperplanePoint));

        // split the sub-line by the hyperplane
        final SplitSubHyperplane<Euclidean2D> split = subLine.split(hyperplane);

        // check the split result
        assertEquals(subLine.getHyperplane(), split.getFirst());
        assertEquals(new SubLine(new Line(start, new Vector2D(1.5, 1.5)), new IntervalsSet(new Interval(1.5, 1.5))), split.getSecond());
    }

    /**
     * Test split method with empty remaining region.
     */
    @Test
    public void testSplitMethodWithEmptyRemainingRegion() {
        // create a line
        final Vector2D start = new Vector2D(1.0, 1.0);
        final Vector2D end   = new Vector2D(2.0, 2.0);
        final Line line = new Line(start, end);

        // create a sub-line with empty remaining region
        final SubLine subLine = new SubLine(start, new IntervalsSet());

        // create a hyperplane which intersects the sub-line
        final Vector2D hyperplanePoint = new Vector2D(1.5, 1.5);
        final Hyperplane<Euclidean2D> hyperplane = new Line(hyperplanePoint, line.toSpace(hyperplanePoint));

        // split the sub-line by the hyperplane
        final SplitSubHyperplane<Euclidean2D> split = subLine.split(hyperplane);

        // check the split result
        assertEquals(subLine, split.getFirst());
        assertNull(split.getSecond());
    }

    /**
     * Test split method with hyperplane outside the sub-line.
     */
    @Test
    public void testSplitMethodWithHyperplaneOutside() {
        // create a line
        final Vector2D start = new Vector2D(1.0, 1.0);
        final Vector2D end   = new Vector2D(2.0, 2.0);
        final Line line = new Line(start, end);

        // create a sub-line
        final SubLine subLine = new SubLine(start, end);

        // create a hyperplane which is outside the sub-line
        final Vector2D hyperplanePoint = new Vector2D(0.5, 0.5);
        final Hyperplane<Euclidean2D> hyperplane = new Line(hyperplanePoint, line.toSpace(hyperplanePoint));

        // split the sub-line by the hyperplane
        final SplitSubHyperplane<Euclidean2D> split = subLine.split(hyperplane);

        // check the split result
        assertNull(split.getFirst());
        assertNull(split.getSecond());
    }

    /**
     * Helper method for building an intervals set from two points.
     * @param start start point
     * @param end end point
     * @return an intervals set
     */
    private static IntervalsSet buildIntervalSet(final Vector2D start, final Vector2D end) {
        final Line line = new Line(start, end);
        return new IntervalsSet(line.toSubSpace(start).getX(), line.toSubSpace(end).getX());
    }

}