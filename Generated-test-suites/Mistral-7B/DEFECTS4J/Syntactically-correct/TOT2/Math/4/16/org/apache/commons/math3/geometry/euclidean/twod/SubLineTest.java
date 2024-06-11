package org.apache.commons.math3.geometry.euclidean.twod.tests;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstructor() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 2.0), new Vector2D(3.0, 4.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 5.0));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testSubLineSegmentConstructor() {
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end = new Vector2D(3.0, 4.0);
        SubLine subLine = new SubLine(start, end);
        Line line = (Line) subLine.getHyperplane();
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(start, segment.getStart());
        assertEquals(end, segment.getEnd());
        assertEquals(line, segment.getLine());
    }

    @Test
    public void testSubLineEmptyConstructor() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 2.0), new Vector2D(3.0, 4.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet();
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertTrue(subLine.getSegments().isEmpty());
    }

    @Test
    public void testSubLineIntersection() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 2.0)));
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNotNull(intersectionPoint);
        assertEquals(intersectionPoint, new Vector2D(1.0, 1.0));

        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.0, 1.0)));
        intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);

        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 2.0)));
        intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);

        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.0, 2.0)));
        intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);

        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(-1.0, 0.0)));
        intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);
    }

    @Test
    public void testSubLineSide() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 1.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 2.0)));

        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 1.0), new Vector2D(1.0, 1.0));
        Side side = subLine1.side(hyperplane);
        assertEquals(Side.MINUS, side);

        side = subLine2.side(hyperplane);
        assertEquals(Side.PLUS, side);
    }

    @Test
    public void testSubLineSplit() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(1.0, 0.0), new Vector2D(2.0, 1.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 2.0)));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 0.5), new Vector2D(1.5, 0.5));

        SplitSubHyperplane<Euclidean2D> split = subLine1.split(hyperplane);
        SubLine subLinePlus = split.getPlus();
        SubLine subLineMinus = split.getMinus();

        Hyperplane<Euclidean2D> hyperplanePlus = new Line(new Vector2D(0.0, 0.0), new Vector2D(0.5, 0.5));
        Hyperplane<Euclidean2D> hyperplaneMinus = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.0, 1.0));

        assertEquals(hyperplanePlus, subLinePlus.getHyperplane());
        assertEquals(hyperplaneMinus, subLineMinus.getHyperplane());

        List<Segment> segmentsPlus = subLinePlus.getSegments();
        List<Segment> segmentsMinus = subLineMinus.getSegments();

        assertEquals(1, segmentsPlus.size());
        assertEquals(1, segmentsMinus.size());

        Segment segmentPlus = segmentsPlus.get(0);
        Segment segmentMinus = segmentsMinus.get(0);

        assertEquals(new Vector2D(0.0, 0.0), segmentPlus.getStart());
        assertEquals(new Vector2D(0.5, 0.5), segmentPlus.getEnd());
        assertEquals(new Vector2D(0.5, 0.5), segmentMinus.getStart());
        assertEquals(new Vector2D(1.0, 1.0), segmentMinus.getEnd());
    }

}