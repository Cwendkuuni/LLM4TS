import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end   = new Vector2D(3.0, 4.0);
        Line line = new Line(start, end);
        List<Interval> intervalList = new ArrayList<>();
        intervalList.add(new Interval(1.5, 2.5));
        intervalList.add(new Interval(3.5, 4.5));
        IntervalsSet intervals = new IntervalsSet(intervalList);
        SubLine subLine = new SubLine(line, intervals);

        assertEquals(line, subLine.getHyperplane());
        assertEquals(intervals, subLine.getRemainingRegion());
    }

    @Test
    public void testSubLineConstructorsFromSegments() {
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end   = new Vector2D(3.0, 4.0);
        Line line = new Line(start, end);
        Segment segment = new Segment(new Vector2D(1.5, 2.5), new Vector2D(2.5, 3.5), line);
        IntervalsSet intervals = new IntervalsSet(new Interval(1.5, 2.5), new Interval(2.5, 3.5));
        SubLine subLine = new SubLine(segment);

        assertEquals(line, subLine.getHyperplane());
        assertEquals(intervals, subLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1   = new Vector2D(3.0, 4.0);
        Vector2D start2 = new Vector2D(5.0, 6.0);
        Vector2D end2   = new Vector2D(7.0, 8.0);
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(10.0, 10.0));

        List<Interval> intervalList = new ArrayList<>();
        intervalList.add(new Interval(1.5, 2.5));
        intervalList.add(new Interval(3.5, 4.5));
        intervalList.add(new Interval(5.5, 6.5));
        intervalList.add(new Interval(7.5, 8.5));

        IntervalsSet intervals = new IntervalsSet(intervalList);
        SubLine subLine = new SubLine(line, intervals);

        List<Segment> segments = subLine.getSegments();
        assertEquals(4, segments.size());

        Segment segment1 = segments.get(0);
        Segment expectedSegment1 = new Segment(new Vector2D(1.5, 2.5), new Vector2D(2.5, 3.5), line);
        assertEquals(expectedSegment1, segment1);

        Segment segment2 = segments.get(1);
        Segment expectedSegment2 = new Segment(new Vector2D(3.5, 4.5), new Vector2D(4.5, 5.5), line);
        assertEquals(expectedSegment2, segment2);

        Segment segment3 = segments.get(2);
        Segment expectedSegment3 = new Segment(new Vector2D(5.5, 6.5), new Vector2D(6.5, 7.5), line);
        assertEquals(expectedSegment3, segment3);

        Segment segment4 = segments.get(3);
        Segment expectedSegment4 = new Segment(new Vector2D(7.5, 8.5), new Vector2D(8.5, 10.0), line);
        assertEquals(expectedSegment4, segment4);
    }

    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1   = new Vector2D(3.0, 4.0);
        Vector2D start2 = new Vector2D(3.5, 4.5);
        Vector2D end2   = new Vector2D(5.5, 6.5);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);

        IntervalsSet intervals1 = new IntervalsSet(new Interval(2.0, 3.0));
        SubLine subLine1 = new SubLine(line1, intervals1);
        IntervalsSet intervals2 = new IntervalsSet(new Interval(3.5, 4.5), new Interval(5.5, 6.5));
        SubLine subLine2 = new SubLine(line2, intervals2);

        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(3.0, 4.0), intersection);

        intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);

        // Test case where lines don't intersect
        IntervalsSet intervals1_1 = new IntervalsSet(new Interval(1.0, 2.0));
        SubLine subLine1_1 = new SubLine(line1, intervals1_1);
        IntervalsSet intervals2_1 = new IntervalsSet(new Interval(5.0, 6.0));
        SubLine subLine2_1 = new SubLine(line2, intervals2_1);

        intersection = subLine1_1.intersection(subLine2_1, true);
        assertNull(intersection);
    }
}