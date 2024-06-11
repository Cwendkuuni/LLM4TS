import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        // test construction with hyperplane and remaining region
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 0.5));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());

        // test construction with start and end points
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine2 = new SubLine(start, end);
        assertEquals(start, subLine2.getStart());
        assertEquals(end, subLine2.getEnd());

        // test construction with segment
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 1), new Line(new Vector2D(0, 0), new Vector2D(1, 1)));
        SubLine subLine3 = new SubLine(segment);
        assertEquals(segment.getStart(), subLine3.getStart());
        assertEquals(segment.getEnd(), subLine3.getEnd());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        IntervalsSet intervals = new IntervalsSet(new Interval(0, 0.5), new Interval(0.7, 1));
        SubLine subLine = new SubLine(line, intervals);

        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());

        Segment segment1 = segments.get(0);
        Segment segment2 = segments.get(1);

        assertEquals(new Vector2D(0, 0), segment1.getStart());
        assertEquals(new Vector2D(0.5, 0.5), segment1.getEnd());
        assertEquals(new Vector2D(0.7, 0.7), segment2.getStart());
        assertEquals(new Vector2D(1, 1), segment2.getEnd());
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0, -1), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertEquals(new Vector2D(0.5, 0), intersection);

        intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);

        line2 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);

        intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);

        line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        line2 = new Line(new Vector2D(1, 0), new Vector2D(2, 1));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);

        intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        Side side = subLine1.side(line2.getHyperplane());
        assertEquals(Side.MINUS, side);

        side = subLine2.side(line1.getHyperplane());
        assertEquals(Side.PLUS, side);

        line2 = new Line(new Vector2D(1, 0), new Vector2D(1, 1));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        side = subLine1.side(line2.getHyperplane());
        assertEquals(Side.HYPER, side);

        side = subLine2.side(line1.getHyperplane());
        assertEquals(Side.HYPER, side);
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 0));

        SplitSubHyperplane<Euclidean2D> split = subLine1.split(hyperplane);
        SubLine subLineMinus = (SubLine) split.getSubHyperplane1();
        SubLine subLinePlus = (SubLine) split.getSubHyperplane2();

        assertEquals(new IntervalsSet(new Interval(0, 0.5)), subLineMinus.getRemainingRegion());
        assertEquals(new IntervalsSet(new Interval(0.5, 1)), subLinePlus.getRemainingRegion());

        split = subLine1.split(hyperplane);
        subLineMinus = (SubLine) split.getSubHyperplane1();
        subLinePlus = (SubLine) split.getSubHyperplane2();

        assertEquals(new IntervalsSet(new Interval(0.5, 1)), subLineMinus.getRemainingRegion());
        assertEquals(new IntervalsSet(new Interval(0, 0.5)), subLinePlus.getRemainingRegion());

        line2 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        split = subLine1.split(line2.getHyperplane());
        assertNull(split);

        split = subLine2.split(line1.getHyperplane());
        assertNull(split);
    }
}