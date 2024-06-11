package org.apache.commons.math3.geometry.euclidean.twod.tests;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link SubLine}.
 */
public class SubLineTest {

    /**
     * Test constructor with hyperplane and remaining region.
     */
    @Test
    public void testConstructorWithHyperplaneAndRemainingRegion() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 2));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        Hyperplane<Euclidean2D> expectedHyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> expectedRemainingRegion = new IntervalsSet(new Interval(0, 2));
        assertEquals(expectedHyperplane, subLine.getHyperplane());
        assertEquals(expectedRemainingRegion, subLine.getRemainingRegion());
    }

    /**
     * Test constructor with two endpoints.
     */
    @Test
    public void testConstructorWithTwoEndpoints() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Vector2D start = new Vector2D(1, 1);
        Vector2D end   = new Vector2D(3, 3);
        SubLine subLine = new SubLine(start, end);
        Line expectedLine = new Line(start, end);
        Region<Euclidean1D> expectedRemainingRegion = new IntervalsSet(expectedLine.intersects(new Line(new Vector2D(0, 0), new Vector2D(4, 4))),
                                                                            expectedLine.intersects(new Line(new Vector2D(4, 4), new Vector2D(0, 0))));
        assertEquals(expectedLine, subLine.getHyperplane());
        assertEquals(expectedRemainingRegion, subLine.getRemainingRegion());
    }

    /**
     * Test constructor with a segment.
     */
    @Test
    public void testConstructorWithSegment() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Vector2D start = new Vector2D(1, 1);
        Vector2D end   = new Vector2D(3, 3);
        Segment segment = new Segment(start, end, new Line(start, end));
        SubLine subLine = new SubLine(segment);
        Line expectedLine = new Line(start, end);
        Region<Euclidean1D> expectedRemainingRegion = new IntervalsSet(expectedLine.intersects(new Line(new Vector2D(0, 0), new Vector2D(4, 4))),
                                                                            expectedLine.intersects(new Line(new Vector2D(4, 4), new Vector2D(0, 0))));
        assertEquals(expectedLine, subLine.getHyperplane());
        assertEquals(expectedRemainingRegion, subLine.getRemainingRegion());
    }

    /**
     * Test method for {@link SubLine#getSegments()}.
     */
    @Test
    public void testGetSegments() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Vector2D start = new Vector2D(1, 1);
        Vector2D end1 = new Vector2D(3, 3);
        Vector2D end2 = new Vector2D(5, 5);
        SubLine subLine = new SubLine(start, end1, end2);
        List<Segment> segments = subLine.getSegments();
        Vector2D[] expectedSegments = {new Segment(new Vector2D(1, 1), new Vector2D(3, 3), new Line(start, end1)).toSpace(space),
                                      new Segment(new Vector2D(3, 3), new Vector2D(5, 5), new Line(end1, end2)).toSpace(space)};
        assertEquals(expectedSegments.length, segments.size());
        for (int i = 0; i < expectedSegments.length; i++) {
            assertEquals(expectedSegments[i].getStart(), segments.get(i).getStart());
            assertEquals(expectedSegments[i].getEnd(), segments.get(i).getEnd());
        }
    }

    /**
     * Test method for {@link SubLine#intersection(SubLine, boolean)}.
     */
    @Test
    public void testIntersection() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Vector2D start1 = new Vector2D(1, 1);
        Vector2D end1   = new Vector2D(3, 3);
        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2   = new Vector2D(4, 4);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        Vector2D expectedIntersection = new Vector2D(2, 2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertEquals(expectedIntersection, intersection);
    }

    /**
     * Test method for {@link SubLine#intersection(SubLine, boolean)}.
     */
    @Test
    public void testIntersectionWithoutEndPoints() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Vector2D start1 = new Vector2D(1, 1);
        Vector2D end1   = new Vector2D(3, 3);
        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2   = new Vector2D(4, 4);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        assertNull(subLine1.intersection(subLine2, false));
    }

}