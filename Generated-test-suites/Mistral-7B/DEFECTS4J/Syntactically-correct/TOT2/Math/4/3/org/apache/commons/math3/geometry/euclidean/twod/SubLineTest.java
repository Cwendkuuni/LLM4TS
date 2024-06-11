import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstructor() {
        // Test the constructor with valid arguments
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end = new Vector2D(3.0, 4.0);
        SubLine subLine = new SubLine(start, end);
        Line expectedLine = new Line(start, end);
        IntervalsSet expectedIntervalSet = SubLine.buildIntervalSet(start, end);
        assertEquals(expectedLine, subLine.getHyperplane());
        assertEquals(expectedIntervalSet, subLine.getRemainingRegion());

        // Test the constructor with valid Segment argument
        Segment segment = new Segment(new Vector2D(1.0, 2.0), new Vector2D(3.0, 4.0), new Line(start, end));
        SubLine subLineFromSegment = new SubLine(segment);
        assertEquals(expectedLine, subLineFromSegment.getHyperplane());
        assertEquals(expectedIntervalSet, subLineFromSegment.getRemainingRegion());

        // Test the constructor with valid Hyperplane and IntervalsSet arguments
        Hyperplane<Euclidean2D> hyperplane = new Line(start, end);
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 5.0));
        SubLine subLineFromHyperplane = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLineFromHyperplane.getHyperplane());
        assertEquals(remainingRegion, subLineFromHyperplane.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end = new Vector2D(3.0, 4.0);
        Line line = new Line(start, end);
        IntervalsSet intervals = new IntervalsSet(new Interval(1.5, 2.5), new Interval(4.5, 5.0));
        SubLine subLine = new SubLine(line, intervals);

        List<Segment> expectedSegments = new ArrayList<>();
        expectedSegments.add(new Segment(new Vector2D(2.0, 3.0), new Vector2D(3.0, 4.0), line));
        expectedSegments.add(new Segment(new Vector2D(1.5, 2.0), new Vector2D(1.5, 3.0), line));

        List<Segment> segments = subLine.getSegments();
        assertEquals(expectedSegments.size(), segments.size());
        for (int i = 0; i < expectedSegments.size(); i++) {
            Segment expectedSegment = expectedSegments.get(i);
            Segment segment = segments.get(i);
            assertEquals(expectedSegment.getStart(), segment.getStart());
            assertEquals(expectedSegment.getEnd(), segment.getEnd());
            assertEquals(expectedSegment.getLine(), segment.getLine());
        }
    }

    @Test
    public void testIntersection() {
        Vector2D p1 = new Vector2D(1.0, 2.0);
        Vector2D p2 = new Vector2D(3.0, 4.0);
        Vector2D p3 = new Vector2D(2.5, 3.5);
        Line line1 = new Line(p1, p2);
        Line line2 = new Line(new Vector2D(0.0, 0.0), new Vector2D(5.0, 5.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.0, 4.0), new Interval(5.0, 6.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2.0, 4.0)));

        Vector2D expectedIntersection = new Vector2D(2.0, 3.0);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertEquals(expectedIntersection, intersection);

        // Test intersection with empty regions
        subLine1 = new SubLine(line1, new IntervalsSet());
        intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);

        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.0, 1.0)));
        intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);

        // Test intersection with non-overlapping regions
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.0, 2.0), new Interval(5.0, 6.0)));
        intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);

        // Test intersection with lines that don't intersect
        line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(5.0, 5.0));
        line2 = new Line(new Vector2D(1.0, 0.0), new Vector2D(1.0, 5.0));
        subLine1 = new SubLine(line1, new IntervalsSet());
        subLine2 = new SubLine(line2, new IntervalsSet());
        intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
    }

    @Test
    public void testIntersectionWithInfiniteLines() {
        Vector2D start1 = new Vector2D(-10.0, 0.0);
        Vector2D end1 = new Vector2D(10.0, 0.0);
        Vector2D start2 = new Vector2D(2.0, 0.0);
        Vector2D end2 = new Vector2D(8.0, 0.0);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet());
        SubLine subLine2 = new SubLine(line2, new IntervalsSet());

        Vector2D expectedIntersection = new Vector2D(0.0, 0.0);
        Vector2D intersection = subLine1.intersection(subLine2, false);
        assertEquals(expectedIntersection, intersection);
    }

    @Test
    public void testSide() {
        Vector2D p1 = new Vector2D(1.0, 2.0);
        Vector2D p2 = new Vector2D(3.0, 4.0);
        Vector2D p3 = new Vector2D(2.5, 3.5);
        Line line1 = new Line(p1, p2);
        Line line2 = new Line(new Vector2D(0.0, 0.0), new Vector2D(5.0, 5.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.0, 4.0), new Interval(5.0, 6.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2.0, 4.0)));

        Side expectedSide = Side.INSIDE;
        Side side = subLine1.side(subLine2);
        assertEquals(expectedSide, side);

        expectedSide = Side.OUTSIDE;
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(-1.0, 0.0)));
        side = subLine1.side(subLine2);
        assertEquals(expectedSide, side);

        expectedSide = Side.HYPER;
        subLine2 = new SubLine(new Line(p1, p3), new IntervalsSet());
        side = subLine1.side(subLine2);
        assertEquals(expectedSide, side);
    }

    @Test
    public void testSplit() {
        Vector2D p1 = new Vector2D(1.0, 2.0);
        Vector2D p2 = new Vector2D(3.0, 4.0);
        Vector2D p3 = new Vector2D(2.5, 3.5);
        Line line1 = new Line(p1, p2);
        Line line2 = new Line(new Vector2D(0.0, 0.0), new Vector2D(5.0, 5.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.0, 4.0), new Interval(5.0, 6.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2.0, 4.0)));

        SubLine expectedSubLine1 = new SubLine(new Line(p1, p3), new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.5, 4.0), new Interval(5.0, 6.0)));
        SubLine expectedSubLine2 = new SubLine(new Line(p3, p2), new IntervalsSet(new Interval(2.0, 3.5)));
        SubLine split = subLine1.split(subLine2);

        assertEquals(expectedSubLine1, split.getSubHyperplane1());
        assertEquals(expectedSubLine2, split.getSubHyperplane2());

        // Test splitting with empty regions
        subLine1 = new SubLine(line1, new IntervalsSet());
        subLine2 = new SubLine(line2, new IntervalsSet());
        assertNull(subLine1.split(subLine2));

        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.0, 2.0)));
        subLine2 = new SubLine(line2, new IntervalsSet());
        assertNull(subLine1.split(subLine2));

        subLine1 = new SubLine(line1, new IntervalsSet());
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2.0, 4.0)));
        assertNull(subLine1.split(subLine2));

        // Test splitting with non-intersecting lines
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.0, 2.0), new Interval(5.0, 6.0)));
        subLine2 = new SubLine(new Line(new Vector2D(0.0, 0.0), new Vector2D(5.0, 5.0)), new IntervalsSet());
        assertNull(subLine1.split(subLine2));

        subLine1 = new SubLine(new Line(p1, p3), new IntervalsSet());
        subLine2 = new SubLine(line2, new IntervalsSet());
        assertNull(subLine1.split(subLine2));

        subLine1 = new SubLine(line1, new IntervalsSet());
        subLine2 = new SubLine(new Line(p3, p2), new IntervalsSet());
        assertNull(subLine1.split(subLine2));
    }

    @Test
    public void testSplitWithZeroLengthInterval() {
        Vector2D p1 = new Vector2D(1.0, 2.0);
        Vector2D p2 = new Vector2D(3.0, 4.0);
        Line line1 = new Line(p1, p2);
        Line line2 = new Line(new Vector2D(2.0, 2.0), new Vector2D(2.0, 3.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.0, 1.0), new Interval(3.0, 3.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.5, 1.5)));

        SubLine expectedSubLine1 = new SubLine(new Line(p1, p1), new IntervalsSet(new Interval(1.0, 1.0)));
        SubLine expectedSubLine2 = new SubLine(new Line(p1, p2), new IntervalsSet(new Interval(1.5, 1.5)));
        SubLine split = subLine1.split(subLine2);

        assertEquals(expectedSubLine1, split.getSubHyperplane1());
        assertEquals(expectedSubLine2, split.getSubHyperplane2());

        // Test splitting with a zero-length intersection interval should return the original subline
        SubLine subLine3 = new SubLine(line1, new IntervalsSet(new Interval(1.0, 1.0), new Interval(3.0, 3.0)));
        SubLine subLine4 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 1.0)));
        SubLine originalSubLine = subLine3;
        SubLine splitResult = subLine3.split(subLine4);
        assertEquals(originalSubLine, splitResult);
    }

    @Test
    public void testBuildNew() {
        Vector2D p1 = new Vector2D(1.0, 2.0);
        Vector2D p2 = new Vector2D(3.0, 4.0);
        Line line1 = new Line(p1, p2);
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(1.0, 4.0), new Interval(5.0, 6.0));
        SubLine subLine1 = new SubLine(line1, remainingRegion);

        Hyperplane<Euclidean2D> hyperplane = new Line(p1, p2);
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1.0, 4.0), new Interval(5.0, 6.0));
        SubLine newSubLine = SubLine.buildNew(hyperplane, remainingRegion2);

        assertEquals(subLine1.getHyperplane(), newSubLine.getHyperplane());
        assertEquals(subLine1.getRemainingRegion(), newSubLine.getRemainingRegion());
    }

    @Test
    public void testLineIntersection() {
        Vector2D p1 = new Vector2D(1.0, 2.0);
        Vector2D p2 = new Vector2D(3.0, 4.0);
        Vector2D p3 = new Vector2D(2.5, 3.5);
        Line line1 = new Line(p1, p2);
        Line line2 = new Line(p3, new Vector2D(5.0, 5.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.0, 4.0), new Interval(5.0, 6.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2.0, 4.0)));

        Vector2D expectedIntersection = new Vector2D(2.5, 3.5);
        Vector2D intersection = line1.intersection(line2);
        assertEquals(expectedIntersection, intersection);

        intersection = subLine1.intersection(subLine2, false);
        assertEquals(expectedIntersection, intersection);

        // Test intersection with empty lines
        line1 = new Line(p1, p1);
        line2 = new Line(p3, p3);
        intersection = line1.intersection(line2);
        assertNull(intersection);

        intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);
    }
}