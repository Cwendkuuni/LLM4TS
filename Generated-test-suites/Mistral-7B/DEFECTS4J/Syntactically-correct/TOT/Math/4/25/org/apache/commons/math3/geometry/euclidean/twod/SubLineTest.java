import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link SubLine}.
 */
public class SubLineTest {

    /**
     * Test construction from hyperplane and region.
     */
    @Test
    public void testConstructionHyperplaneAndRegion() {
        final Euclidean2DSpace space = new Euclidean2DSpace();
        final Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        final Interval interval = new Interval(0.0, 1.0);
        final Region<Euclidean1D> remainingRegion = new IntervalsSet(interval).getRegions(space)[0];

        final SubLine subLine = new SubLine(hyperplane, remainingRegion);

        Assert.assertEquals(hyperplane, subLine.getHyperplane());
        Assert.assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    /**
     * Test construction from two endpoints.
     */
    @Test
    public void testConstructionTwoEndpoints() {
        final Euclidean2DSpace space = new Euclidean2DSpace();
        final Vector2D start = new Vector2D(0.0, 0.0);
        final Vector2D end   = new Vector2D(1.0, 1.0);
        final SubLine subLine = new SubLine(start, end);

        final Line line = (Line) subLine.getHyperplane();
        Assert.assertEquals(line.getEndPoint(), end);
        Assert.assertEquals(line.getStartPoint(), start);

        final List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(segments.size(), 1);
        Assert.assertEquals(segments.get(0).getEnd(), end);
        Assert.assertEquals(segments.get(0).getStart(), start);
    }

    /**
     * Test construction from a segment.
     */
    @Test
    public void testConstructionSegment() {
        final Euclidean2DSpace space = new Euclidean2DSpace();
        final Vector2D start = new Vector2D(0.0, 0.0);
        final Vector2D end   = new Vector2D(1.0, 1.0);
        final Segment segment = new Segment(start, end);
        final SubLine subLine = new SubLine(segment);

        final Line line = (Line) subLine.getHyperplane();
        Assert.assertEquals(line.getEndPoint(), end);
        Assert.assertEquals(line.getStartPoint(), start);

        final List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(segments.size(), 1);
        Assert.assertEquals(segments.get(0).getEnd(), end);
        Assert.assertEquals(segments.get(0).getStart(), start);
    }

    /**
     * Test {@link SubLine#getSegments()}.
     */
    @Test
    public void testGetSegments() {
        final Euclidean2DSpace space = new Euclidean2DSpace();
        final Vector2D start1 = new Vector2D(0.0, 0.0);
        final Vector2D end1   = new Vector2D(1.0, 0.0);
        final Vector2D start2 = new Vector2D(2.0, 1.0);
        final Vector2D end2   = new Vector2D(3.0, 2.0);
        final Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(3.0, 0.0));

        final Interval interval1 = new Interval(0.0, 1.0);
        final Interval interval2 = new Interval(2.0, 3.0);
        final Region<Euclidean1D> remainingRegion = new IntervalsSet(interval1, interval2).getRegions(space)[0];

        final SubLine subLine = new SubLine(line, remainingRegion);
        final List<Segment> segments = subLine.getSegments();

        Assert.assertEquals(segments.size(), 2);
        Assert.assertEquals(segments.get(0).getEnd(), new Vector2D(1.0, 0.0));
        Assert.assertEquals(segments.get(0).getStart(), new Vector2D(0.0, 0.0));
        Assert.assertEquals(segments.get(1).getEnd(), new Vector2D(3.0, 0.0));
        Assert.assertEquals(segments.get(1).getStart(), new Vector2D(2.0, 1.0));
    }

    /**
     * Test {@link SubLine#intersection(SubLine, boolean)}.
     */
    @Test
    public void testIntersection() {
        final Euclidean2DSpace space = new Euclidean2DSpace();

        final Vector2D start1 = new Vector2D(0.0, 0.0);
        final Vector2D end1   = new Vector2D(1.0, 0.0);
        final Vector2D start2 = new Vector2D(1.0, 1.0);
        final Vector2D end2   = new Vector2D(2.0, 2.0);
        final Line line1 = new Line(start1, end1);
        final Line line2 = new Line(start2, end2);

        final Interval interval1 = new Interval(0.0, 1.0);
        final Interval interval2 = new Interval(1.0, 2.0);
        final Region<Euclidean1D> remainingRegion1 = new IntervalsSet(interval1).getRegions(space)[0];
        final Region<Euclidean1D> remainingRegion2 = new IntervalsSet(interval2).getRegions(space)[0];

        final SubLine subLine1 = new SubLine(line1, remainingRegion1);
        final SubLine subLine2 = new SubLine(line2, remainingRegion2);

        // intersection occurs on an endpoint
        Assert.assertEquals(subLine1.intersection(subLine2, true).getX(), 1.0);

        // intersection occurs on a segment
        final Vector2D intersectionPoint = new Vector2D(0.5, 0.5);
        line1.setEndPoint(intersectionPoint);
        line2.setStartPoint(intersectionPoint);
        remainingRegion1 = new IntervalsSet(new Interval(0.0, 0.5), new Interval(1.0, 2.0)).getRegions(space)[0];
        remainingRegion2 = new IntervalsSet(new Interval(1.5, 2.0)).getRegions(space)[0];
        subLine1 = new SubLine(line1, remainingRegion1);
        subLine2 = new SubLine(line2, remainingRegion2);
        Assert.assertEquals(subLine1.intersection(subLine2, true).getX(), 1.0);

        // intersection doesn't occur
        line1.setStartPoint(new Vector2D(-1.0, -1.0));
        line2.setEndPoint(new Vector2D(1.0, 1.0));
        remainingRegion1 = new IntervalsSet(interval1).getRegions(space)[0];
        remainingRegion2 = new IntervalsSet(interval2).getRegions(space)[0];
        subLine1 = new SubLine(line1, remainingRegion1);
        subLine2 = new SubLine(line2, remainingRegion2);
        Assert.assertNull(subLine1.intersection(subLine2, true));
    }

    /**
     * Test {@link SubLine#toString()}.
     */
    @Test
    public void testToString() {
        final Euclidean2DSpace space = new Euclidean2DSpace();

        final Vector2D start = new Vector2D(0.0, 0.0);
        final Vector2D end   = new Vector2D(1.0, 0.0);
        final Line line = new Line(start, end);
        final Interval interval = new Interval(0.0, 1.0);
        final Region<Euclidean1D> remainingRegion = new IntervalsSet(interval).getRegions(space)[0];
        final SubLine subLine = new SubLine(line, remainingRegion);

        final String output = subLine.toString();
        Assert.assertTrue(output.contains("SubLine{"));
        Assert.assertTrue(output.contains("start=" + start.toString()));
        Assert.assertTrue(output.contains("end=" + end.toString()));
        Assert.assertTrue(output.contains("intervals=" + interval.toString()));
    }

}