import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        // Test construction with a hyperplane and remaining region
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 1));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertNotNull(subLine);

        // Test construction with two endpoints
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 0);
        SubLine subLine1 = new SubLine(start, end);
        assertNotNull(subLine1);
        assertEquals(start, subLine1.getStart());
        assertEquals(end, subLine1.getEnd());

        // Test construction with a Segment
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 0), new Line(new Vector2D(0, 0), new Vector2D(1, 0)));
        SubLine subLine2 = new SubLine(segment);
        assertNotNull(subLine2);
        assertEquals(segment.getStart(), subLine2.getStart());
        assertEquals(segment.getEnd(), subLine2.getEnd());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Interval interval1 = new Interval(0, 0.5);
        Interval interval2 = new Interval(0.5, 1);
        IntervalsSet remainingRegion = new IntervalsSet(interval1, interval2);
        SubLine subLine = new SubLine(line, remainingRegion);
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());

        Segment segment1 = segments.get(0);
        assertEquals(new Vector2D(0, 0), segment1.getStart());
        assertEquals(new Vector2D(0.5, 0.5), segment1.getEnd());

        Segment segment2 = segments.get(1);
        assertEquals(new Vector2D(0.5, 0.5), segment2.getStart());
        assertEquals(new Vector2D(1, 1), segment2.getEnd());
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        Line line2 = new Line(new Vector2D(1, 0), new Vector2D(2, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));

        Vector2D point = subLine1.intersection(subLine2, true);
        assertNotNull(point);
        assertEquals(new Vector2D(1, 0), point);

        point = subLine1.intersection(subLine2, false);
        assertNull(point);
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        Line line2 = new Line(new Vector2D(1, 0), new Vector2D(2, 1));

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));

        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 1), new Vector2D(1, 1));

        Side side = subLine1.side(hyperplane);
        assertEquals(Side.PLUS, side);

        side = subLine2.side(hyperplane);
        assertEquals(Side.MINUS, side);
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        Line line2 = new Line(new Vector2D(1, 0), new Vector2D(2, 1));

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));

        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 1), new Vector2D(1, 1));

        SplitSubHyperplane<Euclidean2D> split = subLine1.split(hyperplane);
        assertNotNull(split);
        assertEquals(subLine1, split.getFirst());
        assertNull(split.getSecond());

        split = subLine2.split(hyperplane);
        assertNotNull(split);
        assertNull(split.getFirst());
        assertEquals(subLine2, split.getSecond());
    }

    @Test
    public void testBuildNew() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 1));

        SubLine subLine1 = new SubLine(line, remainingRegion);
        AbstractSubHyperplane<Euclidean2D, Euclidean1D> newSubLine = subLine1.buildNew(new Line(new Vector2D(0, 0), new Vector2D(1, 1)),
                new IntervalsSet(new Interval(0, 1)));
        assertEquals(subLine1, newSubLine);
    }

}