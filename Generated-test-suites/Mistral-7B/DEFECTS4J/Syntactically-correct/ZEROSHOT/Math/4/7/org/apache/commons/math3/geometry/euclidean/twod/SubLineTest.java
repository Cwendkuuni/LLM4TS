import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Point2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubHyperplane;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        // test simple constructor
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 0.5));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());

        // test constructor from two endpoints
        Point2D start = new Vector2D(0, 0);
        Point2D end   = new Vector2D(1, 1);
        SubLine subLine2 = new SubLine(start, end);
        Line line = (Line) subLine2.getHyperplane();
        assertFalse(line.isParallel(new Line(start, end)));
        assertEquals(start, line.toSpace(new Vector1D(line.getParameter(0))));
        assertEquals(end, line.toSpace(new Vector1D(line.getParameter(1))));

        // test constructor from a segment
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 1), new Line(new Vector2D(0, 0), new Vector2D(1, 1)));
        SubLine subLine3 = new SubLine(segment);
        assertEquals(segment.getLine(), subLine3.getHyperplane());
        assertEquals(segment.getStart(), subLine3.getEndpoints().get(0).getStart());
        assertEquals(segment.getEnd(), subLine3.getEndpoints().get(0).getEnd());

        // test getSegments method
        List<Segment> segments = subLine3.getSegments();
        assertEquals(1, segments.size());
        assertEquals(segment.getStart(), segments.get(0).getStart());
        assertEquals(segment.getEnd(), segments.get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        // test intersection with an infinite line
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(0, 2)),
                                        new IntervalsSet(new Interval(0, 1)));
        Vector2D point = subLine1.intersection(line1, false);
        assertNull(point);

        // test intersection with a finite line
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine2 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)),
                                        new IntervalsSet(new Interval(0, 1), new Interval(1.5, 2)));
        point = subLine2.intersection(line2, false);
        assertEquals(new Vector2D(1.5, 1.5), point);

        // test intersection with a sub-line that does not intersect
        SubLine subLine3 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(0, 1)),
                                        new IntervalsSet(new Interval(0, 0.5)));
        SubLine subLine4 = new SubLine(new Line(new Vector2D(1, 1), new Vector2D(2, 2)),
                                        new IntervalsSet(new Interval(0, 0.5)));
        point = subLine3.intersection(subLine4, false);
        assertNull(point);

        // test intersection with sub-lines with endpoints on them
        SubLine subLine5 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)),
                                        new IntervalsSet(new Interval(0, 1), new Interval(1, 2)));
        SubLine subLine6 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)),
                                        new IntervalsSet(new Interval(0.5, 1), new Interval(0.5, 1.5)));
        point = subLine5.intersection(subLine6, true);
        assertEquals(new Vector2D(0.5, 0.5), point);
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        assertEquals(Side.MINUS, subLine1.side(hyperplane));
        assertEquals(Side.MINUS, subLine2.side(hyperplane));

        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        assertEquals(Side.PLUS, subLine1.side(hyperplane2));
        assertEquals(Side.MINUS, subLine2.side(hyperplane2));

        Hyperplane<Euclidean2D> hyperplane3 = new Line(new Vector2D(1, 0), new Vector2D(2, 1));
        assertEquals(Side.HYPER, subLine1.side(hyperplane3));
        assertEquals(Side.HYPER, subLine2.side(hyperplane3));
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.5, 0), Vector1D.UNIT_VECTOR);
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(hyperplane);
        assertEquals(Side.PLUS, split.getSubHyperplane1().side(hyperplane));
        assertEquals(Side.MINUS, split.getSubHyperplane2().side(hyperplane));

        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(1, 0.5), Vector1D.UNIT_VECTOR);
        split = subLine2.split(hyperplane2);
        assertEquals(Side.MINUS, split.getSubHyperplane1().side(hyperplane2));
        assertEquals(Side.PLUS, split.getSubHyperplane2().side(hyperplane2));

        Hyperplane<Euclidean2D> hyperplane3 = new Line(new Vector2D(0.5, 0.5), Vector1D.UNIT_VECTOR);
        split = subLine1.split(hyperplane3);
        assertNull(split.getSubHyperplane1());
        assertNull(split.getSubHyperplane2());

        Hyperplane<Euclidean2D> hyperplane4 = new Line(new Vector2D(0.5, 0.5), Vector1D.NEGATIVE_UNIT_VECTOR);
        split = subLine1.split(hyperplane4);
        assertNull(split.getSubHyperplane1());
        assertNull(split.getSubHyperplane2());
    }

    @Test
    public void testBuildNew() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> region = new IntervalsSet(new Interval(0, 1));
        SubLine subLine1 = new SubLine(line1, region);

        Region<Euclidean1D> region2 = new IntervalsSet(new Interval(0.5, 1), new Interval(0, 0.5));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.5, 0), Vector1D.UNIT_VECTOR);
        SubLine newSubLine = SubLine.buildNew(hyperplane, region2);

        assertEquals(hyperplane, newSubLine.getHyperplane());
        assertEquals(region2, newSubLine.getRemainingRegion());
    }

}