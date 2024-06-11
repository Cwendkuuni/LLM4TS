package org.apache.commons.math3.geometry.euclidean.twod.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testLineThroughTwoPoints() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end   = new Vector2D(4.0, 6.0);
        SubLine subLine = new SubLine(start, end);
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(new Vector2D(3.0, 4.5), segments.get(0).getMidpoint());
        assertEquals(end, segments.get(1).getEnd());
        assertEquals(new Vector2D(3.0, 5.5), segments.get(1).getMidpoint());
    }

    @Test
    public void testLineThroughSegment() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end   = new Vector2D(3.0, 4.0);
        Segment segment = new Segment(new Vector2D(2.0, 3.0), new Vector2D(4.0, 5.0), new Line(start, end));
        SubLine subLine = new SubLine(segment);
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(segment.getStart(), segments.get(0).getStart());
        assertEquals(segment.getEnd(), segments.get(0).getEnd());
    }

    @Test
    public void testEmptyLine() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Vector2D start = new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        Vector2D end   = new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        SubLine subLine = new SubLine(start, end);
        List<Segment> segments = subLine.getSegments();
        assertEquals(0, segments.size());
    }

    @Test
    public void testIntersectionWithSameLine() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end   = new Vector2D(4.0, 6.0);
        SubLine subLine1 = new SubLine(start, end);
        Vector2D intersectionPoint = new Vector2D(3.0, 4.5);
        SubLine subLine2 = new SubLine(new Vector2D(3.0, 4.5), new Vector2D(5.0, 7.0));
        Vector2D result = subLine1.intersection(subLine2, true);
        assertEquals(intersectionPoint, result);
    }

    @Test
    public void testIntersectionWithParallelLinesDifferentOffsets() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1   = new Vector2D(4.0, 6.0);
        Vector2D start2 = new Vector2D(1.0, 1.0);
        Vector2D end2   = new Vector2D(5.0, 9.0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        Vector1D direction1 = subLine1.getHyperplane().getNormal();
        Vector1D direction2 = subLine2.getHyperplane().getNormal();
        Vector1D intersectionVector = direction1.crossProduct(direction2);
        double offset1 = subLine1.getHyperplane().getOffset(subLine2.getHyperplane());
        double offset2 = subLine2.getHyperplane().getOffset(subLine1.getHyperplane());
        if (offset1 < 0) {
            assertEquals(new Vector2D(2.0, 1.0), subLine1.intersection(subLine2, false));
        } else {
            assertNull(subLine1.intersection(subLine2, false));
        }
        if (offset2 < 0) {
            assertEquals(new Vector2D(2.0, 1.0), subLine2.intersection(subLine1, false));
        } else {
            assertNull(subLine2.intersection(subLine1, false));
        }
    }

    @Test
    public void testIntersectionWithParallelLinesSameOffset() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1   = new Vector2D(4.0, 6.0);
        Vector2D start2 = new Vector2D(1.0, 1.0);
        Vector2D end2   = new Vector2D(5.0, 9.0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        Vector1D direction1 = subLine1.getHyperplane().getNormal();
        Vector1D direction2 = subLine2.getHyperplane().getNormal();
        Vector1D intersectionVector = direction1.crossProduct(direction2);
        double offset1 = subLine1.getHyperplane().getOffset(subLine2.getHyperplane());
        Vector2D result = subLine1.intersection(subLine2, true);
        if (offset1 > 0) {
            assertEquals(new Vector2D(3.0, 3.0), result);
        } else {
            assertEquals(new Vector2D(3.0, 7.0), result);
        }
    }

    @Test
    public void testIntersectionWithDifferentLines() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1   = new Vector2D(4.0, 6.0);
        Vector2D start2 = new Vector2D(0.0, 0.0);
        Vector2D end2   = new Vector2D(5.0, 7.0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        Vector1D direction1 = subLine1.getHyperplane().getNormal();
        Vector1D direction2 = subLine2.getHyperplane().getNormal();
        Vector1D intersectionVector = direction1.crossProduct(direction2);
        double offset1 = subLine1.getHyperplane().getOffset(subLine2.getHyperplane());
        if (offset1 < 0) {
            assertEquals(new Vector2D(2.0, 1.0), subLine1.intersection(subLine2, true));
        } else {
            assertNull(subLine1.intersection(subLine2, true));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithSelf() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end   = new Vector2D(4.0, 6.0);
        SubLine subLine = new SubLine(start, end);
        subLine.intersection(subLine, true);
    }

}