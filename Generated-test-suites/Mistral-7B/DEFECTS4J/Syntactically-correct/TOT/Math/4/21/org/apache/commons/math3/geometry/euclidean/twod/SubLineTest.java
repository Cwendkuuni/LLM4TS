package org.apache.commons.math3.geometry.euclidean.twod.test;

import junit.framework.Assert;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.junit.Test;

/**
 * Tests for {@link SubLine} class.
 */
public class SubLineTest {

    /**
     * Test method for {@link SubLine#SubLine(Vector2D, Vector2D)} constructor.
     */
    @Test
    public void testSubLineConstruction() {
        // create two vectors
        final Vector2D v1 = new Vector2D(1.0, 1.0);
        final Vector2D v2 = new Vector2D(2.0, 2.0);

        // create a line and check its endpoints
        final Line line = new Line(v1, v2);
        Assert.assertEquals(v1, line.getStartVector());
        Assert.assertEquals(v2, line.getEndVector());

        // create a sub-line from the line and check its endpoints
        final SubLine subLine = new SubLine(v1, v2);
        Assert.assertEquals(v1, subLine.getStartVector(0));
        Assert.assertEquals(v2, subLine.getEndVector(0));

        // check number of segments
        Assert.assertEquals(1, subLine.getSegments().size());
    }

    /**
     * Test method for {@link SubLine#SubLine(Segment)} constructor.
     */
    @Test
    public void testSubLineSegmentConstruction() {
        // create two vectors
        final Vector2D v1 = new Vector2D(1.0, 1.0);
        final Vector2D v2 = new Vector2D(2.0, 2.0);

        // create a segment
        final Segment segment = new Segment(v1, v2);

        // create a sub-line from the segment and check its endpoints
        final SubLine subLine = new SubLine(segment);
        Assert.assertEquals(v1, subLine.getStartVector(0));
        Assert.assertEquals(v2, subLine.getEndVector(0));

        // check number of segments
        Assert.assertEquals(1, subLine.getSegments().size());
    }

    /**
     * Test method for {@link SubLine#SubLine(Hyperplane, Region)} constructor.
     */
    @Test
    public void testSubLineHyperplaneRegionConstruction() {
        // create a line
        final Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));

        // create a hyperplane and a region
        final Hyperplane<Euclidean2D> hyperplane = line;
        final Region<Euclidean1D> region = new IntervalsSet(new Interval(1.0, 2.0));

        // create a sub-line
        final SubLine subLine = new SubLine(hyperplane, region);

        // check the sub-line's hyperplane and region
        Assert.assertEquals(line, subLine.getHyperplane());
        Assert.assertEquals(region, subLine.getRemainingRegion());

        // check number of segments
        Assert.assertEquals(1, subLine.getSegments().size());
    }

    /**
     * Test method for {@link SubLine#getSegments()} method.
     */
    @Test
    public void testGetSegments() {
        // create two vectors
        final Vector2D v1 = new Vector2D(1.0, 1.0);
        final Vector2D v2 = new Vector2D(2.0, 2.0);

        // create a line and a segment
        final Line line = new Line(v1, v2);
        final Segment segment = new Segment(v1, v2);

        // create a sub-line from the line and check its endpoints
        final SubLine subLine = new SubLine(line, new IntervalsSet(new Interval(1.0, 2.0)));

        // check number and endpoints of segments
        Assert.assertEquals(1, subLine.getSegments().size());
        Assert.assertEquals(v1, subLine.getStartVector(0));
        Assert.assertEquals(v2, subLine.getEndVector(0));

        // create a sub-line from the segment and check its endpoints
        final SubLine subLineSegment = new SubLine(segment);
        Assert.assertEquals(v1, subLineSegment.getStartVector(0));
        Assert.assertEquals(v2, subLineSegment.getEndVector(0));

        // check number and endpoints of segments
        Assert.assertEquals(1, subLineSegment.getSegments().size());
        Assert.assertEquals(v1, subLineSegment.getStartVector(0));
        Assert.assertEquals(v2, subLineSegment.getEndVector(0));
    }

    /**
     * Test method for {@link SubLine#intersection(SubLine, boolean)} method.
     */
    @Test
    public void testIntersection() {
        // create two vectors
        final Vector2D v11 = new Vector2D(0.0, 0.0);
        final Vector2D v12 = new Vector2D(1.0, 1.0);
        final Vector2D v21 = new Vector2D(1.0, 0.0);
        final Vector2D v22 = new Vector2D(2.0, 1.0);

        // create two lines
        final Line line1 = new Line(v11, v12);
        final Line line2 = new Line(v21, v22);

        // create two sub-lines
        final SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1.5)));
        final SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.5, 1.5)));

        // test intersection with endpoints included
        final Vector2D intersection = subLine1.intersection(subLine2, true);
        Assert.assertEquals(v11, intersection);

        // test intersection with endpoints excluded
        intersection = subLine1.intersection(subLine2, false);
        Assert.assertNull(intersection);

        // test intersection without intersection
        final Line line3 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        final SubLine subLine3 = new SubLine(line3, new IntervalsSet(new Interval(0.0, 0.5)));
        final SubLine subLine4 = new SubLine(line3, new IntervalsSet(new Interval(1.5, 2.0)));
        intersection = subLine3.intersection(subLine4, true);
        Assert.assertNull(intersection);
    }

}