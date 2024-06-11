package org.apache.commons.math3.geometry.euclidean.twod.test;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        // Test construction with a hyperplane and an intervals set
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.5, 1.5));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());

        // Test construction from two endpoints
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine2 = new SubLine(start, end);
        assertEquals(new Line(start, end), subLine2.getHyperplane());
        List<Segment> segments = subLine2.getSegments();
        assertEquals(2, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
        assertEquals(end, segments.get(1).getStart());
        assertNull(segments.get(1).getEnd());

        // Test construction from a segment
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 1), new Line(new Vector2D(0, 0), new Vector2D(1, 1)));
        SubLine subLine3 = new SubLine(segment);
        assertEquals(segment.getLine(), subLine3.getHyperplane());
        segments = subLine3.getSegments();
        assertEquals(1, segments.size());
        assertEquals(segment.getStart(), segments.get(0).getStart());
        assertEquals(segment.getEnd(), segments.get(0).getEnd());
    }

    @Test
    public void testSubLineIntersection() {
        // Test intersection between two unbounded lines
        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0, 1));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(0.5, 1.5));
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Vector2D expectedIntersection = new Vector2D(1, 1);
        Vector2D actualIntersection = subLine1.intersection(subLine2, true);
        assertEquals(expectedIntersection, actualIntersection);

        // Test intersection between two intersecting lines
        Hyperplane<Euclidean2D> hyperplane3 = new Line(new Vector2D(0, 0), new Vector2D(1, 2));
        Region<Euclidean1D> remainingRegion3 = new IntervalsSet(new Interval(0, 1));
        SubLine subLine3 = new SubLine(hyperplane3, remainingRegion3);
        Hyperplane<Euclidean2D> hyperplane4 = new Line(new Vector2D(1, 2), new Vector2D(2, 3));
        Region<Euclidean1D> remainingRegion4 = new IntervalsSet(new Interval(1, 2));
        SubLine subLine4 = new SubLine(hyperplane4, remainingRegion4);
        Vector2D expectedIntersection2 = new Vector2D(1, 2);
        Vector2D actualIntersection2 = subLine3.intersection(subLine4, true);
        assertEquals(expectedIntersection2, actualIntersection2);

        // Test intersection between two parallel lines
        Hyperplane<Euclidean2D> hyperplane5 = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        Region<Euclidean1D> remainingRegion5 = new IntervalsSet(new Interval(0, 1));
        SubLine subLine5 = new SubLine(hyperplane5, remainingRegion5);
        Hyperplane<Euclidean2D> hyperplane6 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        Region<Euclidean1D> remainingRegion6 = new IntervalsSet(new Interval(0, 1));
        SubLine subLine6 = new SubLine(hyperplane6, remainingRegion6);
        assertNull(subLine5.intersection(subLine6, true));
    }

    @Test
    public void testSubLineSplit() {
        // Test split with a hyperplane and an intervals set
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 1));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        Hyperplane<Euclidean2D> hyperplaneSplit = new Line(new Vector2D(0.5, 0), new Vector2D(1, 1));
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.FALSE);
        tree.add(new Hyperplane<Euclidean1D>(new Line(new Vector1D(0), new Vector1D(1)), Side.PLUS));
        Region<Euclidean1D> plusRegion = tree.getPlus();
        Region<Euclidean1D> minusRegion = tree.getMinus();
        SubLine subLinePlus = new SubLine(subLine.getHyperplane().copySelf(), new IntervalsSet(plusRegion));
        SubLine subLineMinus = new SubLine(subLine.getHyperplane().copySelf(), new IntervalsSet(minusRegion));
        SplitSubHyperplane<Euclidean2D> result = subLine.split(hyperplaneSplit);
        assertEquals(subLinePlus, result.getSubHyperplane1());
        assertEquals(subLineMinus, result.getSubHyperplane2());

        // Test split with a sub-line that intersects the hyperplane
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(0.5, 0), new Vector2D(1, 1));
        SubLine subLine2 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet(new Interval(0.5, 1)));
        Hyperplane<Euclidean2D> hyperplaneSplit2 = new Line(new Vector2D(0.75, 0), new Vector2D(0.75, 1));
        tree = new BSPTree<Euclidean1D>(Boolean.FALSE);
        tree.add(new Hyperplane<Euclidean1D>(new Line(new Vector1D(0), new Vector1D(1)), Side.MINUS));
        tree.add(new Hyperplane<Euclidean1D>(new Line(new Vector1D(0.5), new Vector1D(0.5)), Side.PLUS));
        Region<Euclidean1D> plusRegion2 = tree.getPlus();
        Region<Euclidean1D> minusRegion2 = tree.getMinus();
        SubLine subLinePlus2 = new SubLine(subLine2.getHyperplane().copySelf(), new IntervalsSet(plusRegion2));
        SubLine subLineMinus2 = new SubLine(subLine2.getHyperplane().copySelf(), new IntervalsSet(minusRegion2));
        SplitSubHyperplane<Euclidean2D> result2 = subLine2.split(hyperplaneSplit2);
        assertEquals(subLinePlus2, result2.getSubHyperplane1());
        assertEquals(subLineMinus2, result2.getSubHyperplane2());

        // Test split with a sub-line that does not intersect the hyperplane
        Hyperplane<Euclidean2D> hyperplane3 = new Line(new Vector2D(0.5, 0), new Vector2D(1, 1));
        SubLine subLine3 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet(new Interval(0, 1)));
        Hyperplane<Euclidean2D> hyperplaneSplit3 = new Line(new Vector2D(0.25, 0), new Vector2D(0.75, 1));
        tree = new BSPTree<Euclidean1D>(Boolean.FALSE);
        tree.add(new Hyperplane<Euclidean1D>(new Line(new Vector1D(0), new Vector1D(1)), Side.MINUS));
        tree.add(new Hyperplane<Euclidean1D>(new Line(new Vector1D(0.5), new Vector1D(0.5)), Side.PLUS));
        Region<Euclidean1D> plusRegion3 = tree.getPlus();
        Region<Euclidean1D> minusRegion3 = tree.getMinus();
        SubLine subLinePlus3 = new SubLine(subLine3.getHyperplane().copySelf(), new IntervalsSet(plusRegion3));
        SubLine subLineMinus3 = new SubLine(subLine3.getHyperplane().copySelf(), new IntervalsSet(minusRegion3));
        SplitSubHyperplane<Euclidean2D> result3 = subLine3.split(hyperplaneSplit3);
        assertEquals(subLinePlus3, result3.getSubHyperplane1());
        assertNull(result3.getSubHyperplane2());
    }

}