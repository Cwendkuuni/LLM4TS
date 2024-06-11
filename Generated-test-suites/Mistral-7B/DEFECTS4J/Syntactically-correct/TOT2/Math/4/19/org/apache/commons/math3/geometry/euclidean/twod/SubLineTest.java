import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.vector.Vector2D;
import org.junit.Assert;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testSubLineCreation() {
        // Test creation from hyperplane and remaining region
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Vector1D(0.0), new Vector1D(0.5));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        Assert.assertNotNull(subLine);

        // Test creation from two endpoints
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 1.0);
        SubLine subLineFromEndpoints = new SubLine(start, end);
        Assert.assertEquals(start, subLineFromEndpoints.getStartSegment().getStart());
        Assert.assertEquals(end, subLineFromEndpoints.getStartSegment().getEnd());

        // Test creation from a segment
        Segment segment = new Segment(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0), new Line(start, end));
        SubLine subLineFromSegment = new SubLine(segment);
        Assert.assertEquals(start, subLineFromSegment.getStartSegment().getStart());
        Assert.assertEquals(end, subLineFromSegment.getStartSegment().getEnd());
    }

    @Test
    public void testGetSegments() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Vector1D(0.0), new Vector1D(1.0));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        List<Segment> segments = subLine.getSegments();

        Assert.assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        Assert.assertEquals(new Vector2D(0.0, 0.0), segment.getStart());
        Assert.assertEquals(new Vector2D(1.0, 1.0), segment.getEnd());
    }

    @Test
    public void testIntersectionUnboundedSubLines() {
        // Create two unbounded lines in the positive infinity directions
        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));

        SubLine subLine1 = new SubLine(hyperplane1, new IntervalsSet(new Vector1D(0.0)));
        SubLine subLine2 = new SubLine(hyperplane2, new IntervalsSet(new Vector1D(0.0)));

        // Test intersection returns null since lines don't intersect
        Assert.assertNull(subLine1.intersection(subLine2, true));
    }

    @Test
    public void testIntersectionBoundedSubLines() {
        // Create two lines with overlapping bounding intervals
        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(3.0, 3.0));
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(5.0, 5.0));

        SubLine subLine1 = new SubLine(hyperplane1, new IntervalsSet(new Vector1D(1.5), new Vector1D(3.0)));
        SubLine subLine2 = new SubLine(hyperplane2, new IntervalsSet(new Vector1D(2.0), new Vector1D(4.0)));

        // Test intersection returns the intersection point
        Vector2D intersectionPoint = new Vector2D(2.5, 2.5);
        Assert.assertEquals(intersectionPoint, subLine1.intersection(subLine2, true));
    }

    @Test
    public void testIntersectionEmptySubLines() {
        // Create two empty lines
        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));

        SubLine subLine1 = new SubLine(hyperplane1, new IntervalsSet(new Vector1D(0.0)));
        SubLine subLine2 = new SubLine(hyperplane2, new IntervalsSet(new Vector1D(0.0)));

        // Test intersection returns null since lines don't intersect
        Assert.assertNull(subLine1.intersection(subLine2, true));
    }

    @Test
    public void testIntersectionParallelSubLines() {
        // Create two parallel lines
        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 1.0));

        SubLine subLine1 = new SubLine(hyperplane1, new IntervalsSet(new Vector1D(0.0)));
        SubLine subLine2 = new SubLine(hyperplane2, new IntervalsSet(new Vector1D(0.0)));

        // Test intersection returns null since lines don't intersect
        Assert.assertNull(subLine1.intersection(subLine2, true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionInvalidIncludeEndPoints() {
        // Create two intersecting lines
        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));

        SubLine subLine1 = new SubLine(hyperplane1, new IntervalsSet(new Vector1D(0.0)));
        SubLine subLine2 = new SubLine(hyperplane2, new IntervalsSet(new Vector1D(0.0)));

        // Test IllegalArgumentException is thrown when includeEndPoints is false and intersection occurs on start or end points
        Assert.assertNull(subLine1.intersection(subLine2, false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionInvalidIncludeEndPoints2() {
        // Create two intersecting lines
        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));

        SubLine subLine1 = new SubLine(hyperplane1, new IntervalsSet(new Vector1D(0.0), new Vector1D(0.5)));
        SubLine subLine2 = new SubLine(hyperplane2, new IntervalsSet(new Vector1D(0.5), new Vector1D(1.0)));

        // Test IllegalArgumentException is thrown when includeEndPoints is false and intersection occurs on start or end points
        Assert.assertNull(subLine1.intersection(subLine2, false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionNullSubLine() {
        // Test IllegalArgumentException is thrown when passing a null SubLine to the intersection method
        SubLine subLine1 = new SubLine(new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0)), new IntervalsSet(new Vector1D(0.0)));
        SubLine subLine2 = null;

        Assert.assertNull(subLine1.intersection(subLine2, true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionNullOtherSubLine() {
        // Test IllegalArgumentException is thrown when passing a null Hyperplane to the constructor
        Hyperplane<Euclidean2D> hyperplane = null;
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Vector1D(0.0), new Vector1D(1.0));

        try {
            new SubLine(hyperplane, remainingRegion);
        } catch (IllegalArgumentException e) {
            //Expected
            return;
        }

        Assert.fail("IllegalArgumentException not thrown");
    }
}