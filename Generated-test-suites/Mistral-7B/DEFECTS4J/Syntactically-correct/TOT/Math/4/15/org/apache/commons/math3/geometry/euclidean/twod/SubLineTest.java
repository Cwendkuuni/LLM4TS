package org.apache.commons.math3.geometry.euclidean.twod.SubLineTest;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Point2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.junit.Test;
import org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstructionFromHyperplaneAndRegion() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();

        // create a hyperplane and a region
        Hyperplane<Euclidean2D> hyperplane = new Line(new Point2D(0.0, 1.0), new Point2D(2.0, 3.0)).toHyperplane();
        Region<Euclidean1D> region = new IntervalsSet(new Interval(0.5, 2.5));

        // create a subline from hyperplane and region
        SubLine subLine = new SubLine(hyperplane, region);

        // check that subline contains expected intervals
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());

        Segment segment = segments.get(0);
        Point2D start = segment.getStart();
        Point2D end = segment.getEnd();

        assertEquals(new Point2D(0.5, 1.5), start);
        assertEquals(new Point2D(1.5, 2.5), end);
    }

    @Test
    public void testSubLineConstructionFromTwoEndpoints() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();

        // create two points
        Point2D start = new Point2D(0.0, 0.0);
        Point2D end = new Point2D(2.0, 2.0);

        // create a subline from two endpoints
        SubLine subLine = new SubLine(start, end);

        // check that subline contains expected segments
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());

        Segment segment = segments.get(0);
        Point2D startSegment = segment.getStart();
        Point2D endSegment = segment.getEnd();

        assertEquals(start, startSegment);
        assertEquals(end, endSegment);
    }

    @Test
    public void testSubLineConstructionFromSegment() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();

        // create a point and a vector
        Point2D start = new Point2D(0.0, 0.0);
        Point2D end = new Point2D(2.0, 2.0);

        // create a segment
        Segment segment = new Segment(start, end);

        // create a subline from a segment
        SubLine subLine = new SubLine(segment);

        // check that subline contains expected segments
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());

        Segment segmentTest = segments.get(0);
        Point2D startTest = segmentTest.getStart();
        Point2D endTest = segmentTest.getEnd();

        assertEquals(start, startTest);
        assertEquals(end, endTest);
    }

    @Test
    public void testSubLineIntersectionWithSelf() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();

        // create two points
        Point2D start = new Point2D(0.0, 0.0);
        Point2D end = new Point2D(2.0, 2.0);

        // create a line
        Line line = new Line(start, end);

        // create a subline
        SubLine subLine = new SubLine(start, end);

        // check that subline intersects itself at the end point
        Vector2D intersectionPoint = subLine.intersection(subLine, true);

        assertEquals(end, intersectionPoint);
    }

    @Test
    public void testSubLineIntersectionWithDifferentSubLinesOnSameLine() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();

        // create two points on the same line
        Point2D start1 = new Point2D(0.0, 0.0);
        Point2D end1 = new Point2D(1.0, 1.0);
        Point2D start2 = new Point2D(1.0, 0.0);
        Point2D end2 = new Point2D(2.0, 1.0);

        // create sublines from the points
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        // check that sublines intersect at the intersection point of their lines
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);

        Line line1 = (Line) subLine1.getHyperplane();
        Line line2 = (Line) subLine2.getHyperplane();

        Vector2D lineIntersectionPoint = line1.intersection(line2);

        assertEquals(lineIntersectionPoint, intersectionPoint);
    }

    @Test
    public void testSubLineIntersectionWithDifferentSubLinesNotOnSameLine() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();

        // create two points defining lines that don't intersect
        Point2D start1 = new Point2D(0.0, 0.0);
        Point2D end1 = new Point2D(1.0, 1.0);
        Point2D start2 = new Point2D(1.0, 0.0);
        Point2D end2 = new Point2D(2.0, 2.0);

        // create sublines from the points
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        // check that sublines don't intersect
        assertNull(subLine1.intersection(subLine2, true));
    }

    @Test
    public void testSubLineIntersectionWithEmptySubLine() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();

        // create two points defining lines that don't intersect
        Point2D start1 = new Point2D(0.0, 0.0);
        Point2D end1 = new Point2D(1.0, 1.0);
        Point2D start2 = new Point2D(3.0, 3.0);
        Point2D end2 = new Point2D(4.0, 4.0);

        // create sublines from the points
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        // create an empty subline
        SubLine emptySubLine = new SubLine(new Line(new Point2D(1.0, 1.0), new Point2D(2.0, 2.0)), new IntervalsSet());

        // check that sublines don't intersect with empty subline
        assertNull(subLine1.intersection(emptySubLine, true));
        assertNull(subLine2.intersection(emptySubLine, true));
    }

    @Test
    public void testSubLineIntersectionWithParallelLines() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();

        // create two points defining parallel lines
        Point2D start1 = new Point2D(0.0, 0.0);
        Point2D end1 = new Point2D(1.0, 1.0);
        Point2D start2 = new Point2D(1.0, 0.0);
        Point2D end2 = new Point2D(2.0, 0.0);

        // create sublines from the points
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        // check that sublines don't intersect
        assertNull(subLine1.intersection(subLine2, true));
    }

    @Test
    public void testSubLineIntersectionWithNonIntersectingLines() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();

        // create two points defining lines that don't intersect
        Point2D start1 = new Point2D(0.0, 0.0);
        Point2D end1 = new Point2D(1.0, 1.0);
        Point2D start2 = new Point2D(1.0, 0.0);
        Point2D end2 = new Point2D(2.0, 2.0);

        // create sublines from the points
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        // check that sublines don't intersect
        assertNull(subLine1.intersection(subLine2, true));
    }

    @Test
    public void testSubLineIntersectionWithParallelLinesDifferentSides() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();

        // create two points defining parallel lines on different sides
        Point2D start1 = new Point2D(0.0, 0.0);
        Point2D end1 = new Point2D(2.0, 0.0);
        Point2D start2 = new Point2D(1.0, 1.0);
        Point2D end2 = new Point2D(3.0, 1.0);

        // create sublines from the points
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        // check that sublines don't intersect
        assertNull(subLine1.intersection(subLine2, true));
    }

    @Test
    public void testSubLineIntersectionWithLinesOnSameSide() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();

        // create two points defining lines on the same side
        Point2D start1 = new Point2D(0.0, 0.0);
        Point2D end1 = new Point2D(1.0, 1.0);
        Point2D start2 = new Point2D(1.0, 0.0);
        Point2D end2 = new Point2D(2.0, 1.0);

        // create sublines from the points
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        // check that sublines intersect at the intersection point of their lines
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);

        Line line1 = (Line) subLine1.getHyperplane();
        Line line2 = (Line) subLine2.getHyperplane();

        Vector2D lineIntersectionPoint = line1.intersection(line2);

        assertEquals(lineIntersectionPoint, intersectionPoint);
    }

    @Test
    public void testSubLineIntersectionWithParallelLinesSameSide() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();

        // create two points defining parallel lines on the same side
        Point2D start1 = new Point2D(0.0, 0.0);
        Point2D end1 = new Point2D(2.0, 0.0);
        Point2D start2 = new Point2D(1.0, 0.5);
        Point2D end2 = new Point2D(3.0, 0.5);

        // create sublines from the points
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        // check that sublines intersect at the intersection point of their lines
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);

        Line line1 = (Line) subLine1.getHyperplane();
        Line line2 = (Line) subLine2.getHyperplane();

        // check that intersection point is on both lines
        Vector2D lineIntersectionPoint = line1.intersection(line2);

        assertEquals(lineIntersectionPoint, intersectionPoint);
    }
}