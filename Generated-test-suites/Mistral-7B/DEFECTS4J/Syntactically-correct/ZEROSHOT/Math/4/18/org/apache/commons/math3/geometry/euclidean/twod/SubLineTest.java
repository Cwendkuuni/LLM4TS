import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end   = new Vector2D(3.0, 4.0);
        Line line = new Line(start, end);

        SubLine subLine1 = new SubLine(line, new IntervalsSet(new Interval(0.0, 1.0)));
        SubLine subLine2 = new SubLine(line, new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
        SubLine subLine3 = new SubLine(new Segment(start, end, line));

        assertEquals(line, subLine1.getHyperplane());
        assertEquals(new IntervalsSet(new Interval(0.0, 1.0)), subLine1.getRemainingRegion());
        assertEquals(line, subLine2.getHyperplane());
        assertEquals(new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0)), subLine2.getRemainingRegion());
        assertEquals(line, subLine3.getHyperplane());
        assertEquals(subLine2.getRemainingRegion(), subLine3.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end   = new Vector2D(3.0, 4.0);
        Line line = new Line(start, end);

        SubLine subLine = new SubLine(line, new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0)));

        List<Segment> segments = subLine.getSegments();

        assertEquals(2, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(new Vector2D(1.0, 2.0), segments.get(0).getStartSpace());
        assertEquals(end, segments.get(1).getEnd());
        assertEquals(new Vector2D(2.0, 3.0), segments.get(1).getEndSpace());
        assertEquals(line, segments.get(0).getLine());
        assertEquals(line, segments.get(1).getLine());
    }

    @Test
    public void testIntersection() {
        Vector2D p1 = new Vector2D(0.0, 0.0);
        Vector2D p2 = new Vector2D(3.0, 3.0);
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(3.0, 3.0));

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.0, 2.0)));
        SubLine subLine2 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0)));

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(1.0, 1.0), intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);

        Vector2D pointOnSubLine1 = new Vector2D(1.5, 1.5);
        Vector2D pointOnSubLine2 = new Vector2D(2.5, 2.5);
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0), new Interval(3.0, 4.0)));
        subLine2 = new SubLine(line1, new IntervalsSet(new Interval(1.5, 2.5)));

        intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(pointOnSubLine1, intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);
        assertEquals(pointOnSubLine1, intersectionPoint);

        line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        subLine2 = new SubLine(line1, new IntervalsSet(new Interval(1.0, 2.0)));

        intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);
    }

    @Test
    public void testSide() {
        Vector2D p1 = new Vector2D(0.0, 0.0);
        Vector2D p2 = new Vector2D(3.0, 3.0);
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(3.0, 3.0));

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        SubLine subLine2 = new SubLine(line1, new IntervalsSet(new Interval(2.0, 3.0)));

        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        Side side = subLine1.side(hyperplane);
        assertEquals(Side.MINUS, side);

        side = subLine2.side(hyperplane);
        assertEquals(Side.PLUS, side);
    }

    @Test
    public void testSplit() {
        Vector2D p1 = new Vector2D(0.0, 0.0);
        Vector2D p2 = new Vector2D(3.0, 3.0);
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(3.0, 3.0));

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        SubLine subLine2 = new SubLine(line1, new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));

        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(hyperplane);

        SubHyperplane<Euclidean1D> subPlus = split.getSubPlus();
        SubHyperplane<Euclidean1D> subMinus = split.getSubMinus();

        SubLine subLinePlus = (SubLine) subPlus.getHyperplane();
        SubLine subLineMinus = (SubLine) subMinus.getHyperplane();

        assertEquals(line1, subLinePlus.getHyperplane());
        assertEquals(new IntervalsSet(new Interval(0.0, 1.0)), subLinePlus.getRemainingRegion());
        assertEquals(line1, subLineMinus.getHyperplane());
        assertEquals(new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0)), subLineMinus.getRemainingRegion());

        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(2.0, 2.0), new Vector2D(4.0, 4.0));
        split = subLine1.split(hyperplane2);

        subPlus = split.getSubPlus();
        subMinus = split.getSubMinus();

        subLinePlus = (SubLine) subPlus.getHyperplane();
        subLineMinus = (SubLine) subMinus.getHyperplane();

        assertEquals(line1, subLinePlus.getHyperplane());
        assertEquals(new IntervalsSet(new Interval(0.0, 1.0)), subLinePlus.getRemainingRegion());
        assertEquals(line1, subLineMinus.getHyperplane());
        assertEquals(new IntervalsSet(new Interval(2.0, 3.0)), subLineMinus.getRemainingRegion());

        Hyperplane<Euclidean2D> hyperplane3 = new Line(new Vector2D(1.5, 1.5), new Vector2D(2.5, 2.5));
        split = subLine2.split(hyperplane3);

        subPlus = split.getSubPlus();
        subMinus = split.getSubMinus();

        SubLine subLinePlus2 = (SubLine) subPlus.getHyperplane();
        SubLine subLineMinus2 = (SubLine) subMinus.getHyperplane();

        assertEquals(line1, subLinePlus2.getHyperplane());
        assertEquals(new IntervalsSet(new Interval(1.0, 1.5), new Interval(2.0, 3.0)), subLinePlus2.getRemainingRegion());
        assertEquals(line1, subLineMinus2.getHyperplane());
        assertEquals(new IntervalsSet(new Interval(new Vector2D(1.5, 1.5), new Vector2D(2.0, 2.0)), new Interval(3.0, 4.0)), subLineMinus2.getRemainingRegion());
    }

}