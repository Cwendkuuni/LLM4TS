import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        // Test simple constructor
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 2.0), new Vector2D(3.0, 4.0)).toImplicitForm();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(1.5, 2.5));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);

        // Test constructor with two endpoints
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(5.0, 5.0);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(start, end).toImplicitForm();
        remainingRegion = new IntervalsSet(start.subtract(new Vector2D(0.1, 0.1)), end.add(new Vector2D(0.1, 0.1)));
        SubLine subLine2 = new SubLine(start, end);

        // Test constructor with single segment
        Segment segment = new Segment(new Vector2D(1.0, 2.0), new Vector2D(3.0, 4.0), new Line(new Vector2D(0.0, 0.0), new Vector2D(5.0, 5.0)));
        Hyperplane<Euclidean2D> hyperplane3 = segment.getLine().toImplicitForm();
        remainingRegion = new IntervalsSet(segment.getStart().subtract(new Vector2D(0.1, 0.1)), segment.getEnd().add(new Vector2D(0.1, 0.1)));
        SubLine subLine3 = new SubLine(segment);

        // Test getSegments method
        List<Segment> segments = subLine2.getSegments();
        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());

        // Test intersection method with another sub-line on the same line
        SubLine subLine4 = new SubLine(new Line(new Vector2D(0.0, 0.0), new Vector2D(5.0, 5.0)), new IntervalsSet(new Interval(1.0, 4.0), new Interval(6.0, 7.0)));
        Vector2D intersectionPoint = subLine2.intersection(subLine4, true);
        assertEquals(new Vector2D(2.0, 2.0), intersectionPoint);

        // Test intersection method with another sub-line on different lines
        SubLine subLine5 = new SubLine(new Line(new Vector2D(0.0, 0.0), new Vector2D(3.0, 3.0)), new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
        assertNull(subLine2.intersection(subLine5, true));

        // Test intersection method with another sub-line on the same line but endpoints excluded
        assertNull(subLine2.intersection(subLine5, false));
    }

    @Test
    public void testSubLineIntersection() {
        // Test intersection with another sub-line on the same line
        SubLine subLine1 = new SubLine(new Line(new Vector2D(0.0, 0.0), new Vector2D(5.0, 5.0)), new IntervalsSet(new Interval(1.0, 4.0), new Interval(6.0, 7.0)));
        SubLine subLine2 = new SubLine(new Line(new Vector2D(0.0, 0.0), new Vector2D(5.0, 5.0)), new IntervalsSet(new Interval(2.0, 3.0), new Interval(5.5, 6.5)));
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(2.5, 2.5), intersectionPoint);

        // Test intersection with another sub-line on different lines but within the intersection interval
        SubLine subLine3 = new SubLine(new Line(new Vector2D(0.0, 0.0), new Vector2D(3.0, 3.0)), new IntervalsSet(new Interval(1.5, 2.5)));
        SubLine subLine4 = new SubLine(new Line(new Vector2D(1.5, 1.5), new Vector2D(3.5, 3.5)), new IntervalsSet(new Interval(2.0, 3.0)));
        intersectionPoint = subLine3.intersection(subLine4, true);
        assertEquals(new Vector2D(2.25, 2.25), intersectionPoint);

        // Test intersection with another sub-line on different lines but outside the intersection interval
        SubLine subLine5 = new SubLine(new Line(new Vector2D(0.0, 0.0), new Vector2D(3.0, 3.0)), new IntervalsSet(new Interval(1.0, 1.5)));
        SubLine subLine6 = new SubLine(new Line(new Vector2D(3.5, 3.5), new Vector2D(5.0, 5.0)), new IntervalsSet(new Interval(3.5, 4.5)));
        assertNull(subLine5.intersection(subLine6, true));

        // Test intersection with another sub-line on different lines but endpoints excluded
        assertNull(subLine5.intersection(subLine6, false));
    }

    @Test
    public void testSubLineSplit() {
        // Test splitting a sub-line with another line parallel to it
        SubLine subLine1 = new SubLine(new Line(new Vector2D(0.0, 0.0), new Vector2D(5.0, 5.0)), new IntervalsSet(new Interval(1.0, 4.0), new Interval(6.0, 7.0)));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 0.0), new Vector2D(3.0, 0.0)).toImplicitForm();
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine1.split(hyperplane);
        SubLine subLinePlus = (SubLine) splitSubHyperplane.getPlus();
        SubLine subLineMinus = (SubLine) splitSubHyperplane.getMinus();

        assertEquals(new Line(new Vector2D(1.5, 0.0), new Vector2D(2.5, 0.0)), subLinePlus.getHyperplane());
        assertEquals(new IntervalsSet(new Interval(1.0, 1.5), new Interval(2.5, 3.0), new Interval(4.0, 5.0), new Interval(6.0, 7.0)), subLinePlus.getRemainingRegion());
        assertEquals(new Line(new Vector2D(3.5, 0.0), new Vector2D(5.0, 0.0)), subLineMinus.getHyperplane());
        assertEquals(new IntervalsSet(new Interval(1.5, 2.5)), subLineMinus.getRemainingRegion());

        // Test splitting a sub-line with another line intersecting it
        SubLine subLine2 = new SubLine(new Line(new Vector2D(0.0, 0.0), new Vector2D(5.0, 5.0)), new IntervalsSet(new Interval(1.0, 4.0), new Interval(6.0, 7.0)));
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(2.5, 2.5), new Vector2D(2.5, 4.5)).toImplicitForm();
        splitSubHyperplane = subLine2.split(hyperplane2);
        SubLine subLinePlus2 = (SubLine) splitSubHyperplane.getPlus();
        SubLine subLineMinus2 = (SubLine) splitSubHyperplane.getMinus();

        assertEquals(new Line(new Vector2D(1.0, 2.5), new Vector2D(3.5, 2.5)), subLinePlus2.getHyperplane());
        assertEquals(new IntervalsSet(new Interval(1.0, 1.5), new Interval(2.5, 3.0), new Interval(3.5, 4.0)), subLinePlus2.getRemainingRegion());
        assertEquals(new Line(new Vector2D(4.5, 2.5), new Vector2D(5.0, 5.0)), subLineMinus2.getHyperplane());
        assertEquals(new IntervalsSet(new Interval(2.5, 3.5)), subLineMinus2.getRemainingRegion());

        // Test splitting a sub-line with another line not intersecting it
        SubLine subLine3 = new SubLine(new Line(new Vector2D(0.0, 0.0), new Vector2D(5.0, 5.0)), new IntervalsSet(new Interval(1.0, 4.0), new Interval(6.0, 7.0)));
        Hyperplane<Euclidean2D> hyperplane3 = new Line(new Vector2D(6.0, 6.0), new Vector2D(7.0, 7.0)).toImplicitForm();
        splitSubHyperplane = subLine3.split(hyperplane3);
        assertNull(splitSubHyperplane.getPlus());
        assertNull(splitSubHyperplane.getMinus());

        // Test splitting an empty sub-line
        SubLine emptySubLine = new SubLine(new Line(new Vector2D(5.0, 5.0), new Vector2D(5.0, 5.0)), new IntervalsSet(new Interval(5.0, 5.0)));
        Hyperplane<Euclidean2D> hyperplane4 = new Line(new Vector2D(5.0, 5.0), new Vector2D(5.1, 5.1)).toImplicitForm();
        splitSubHyperplane = emptySubLine.split(hyperplane4);
        assertNull(splitSubHyperplane.getPlus());
        assertNull(splitSubHyperplane.getMinus());
    }

}