import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.euclidean.twod.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end = new Vector2D(3.0, 4.0);
        Line line = new Line(start, end);

        IntervalSet intervalSet = new IntervalsSet(new Interval(0.0, 2.0), new Interval(3.0, 5.0));
        SubLine subLine = new SubLine(line, intervalSet);

        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());

        Segment segment1 = segments.get(0);
        Vector2D segment1Start = segment1.getStart();
        Vector2D segment1End = segment1.getEnd();
        assertEquals(start.getX(), segment1Start.getX(), 0.0);
        assertEquals(start.getY(), segment1Start.getY(), 0.0);
        assertEquals(1.5, segment1End.getX(), 0.01);
        assertEquals(2.0, segment1End.getY(), 0.01);

        Segment segment2 = segments.get(1);
        Vector2D segment2Start = segment2.getStart();
        Vector2D segment2End = segment2.getEnd();
        assertEquals(3.0, segment2Start.getX(), 0.0);
        assertEquals(4.0, segment2Start.getY(), 0.0);
        assertEquals(3.5, segment2End.getX(), 0.01);
        assertEquals(5.0, segment2End.getY(), 0.01);
    }

    @Test
    public void testIntersection() {
        Vector2D p1 = new Vector2D(1.0, 2.0);
        Vector2D p2 = new Vector2D(3.0, 4.0);
        Vector2D q1 = new Vector2D(0.5, 3.5);
        Vector2D q2 = new Vector2D(5.5, 2.5);

        Line line1 = new Line(p1, p2);
        Line line2 = new Line(q1, q2);

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.5, 2.5), new Interval(3.5, 4.5)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2.5, 3.5), new Interval(4.5, 5.5)));

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(p1, intersectionPoint);
    }

    @Test
    public void testIntersectionEmpty() {
        Vector2D p1 = new Vector2D(1.0, 2.0);
        Vector2D p2 = new Vector2D(3.0, 4.0);
        Vector2D q1 = new Vector2D(0.5, 3.5);
        Vector2D q2 = new Vector2D(5.5, 2.5);

        Line line1 = new Line(p1, p2);
        Line line2 = new Line(q1, q2);

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.5, 2.5), new Interval(3.5, 4.5)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2.5, 3.5), new Interval(4.5, 5.5)));

        Vector2D intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);
    }

    @Test
    public void testIntersectionParallel() {
        Vector2D p1 = new Vector2D(1.0, 2.0);
        Vector2D p2 = new Vector2D(3.0, 2.0);
        Vector2D q1 = new Vector2D(0.5, 3.5);
        Vector2D q2 = new Vector2D(5.5, 2.0);

        Line line1 = new Line(p1, p2);
        Line line2 = new Line(q1, q2);

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.5, 2.5), new Interval(3.5, 4.5)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2.5, 3.5), new Interval(4.5, 5.5)));

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);
    }

    @Test
    public void testSplit() {
        Vector2D p1 = new Vector2D(1.0, 2.0);
        Vector2D p2 = new Vector2D(3.0, 4.0);
        Vector2D q1 = new Vector2D(0.5, 3.5);
        Vector2D q2 = new Vector2D(5.5, 2.5);

        Line line1 = new Line(p1, p2);
        Line line2 = new Line(q1, q2);

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.5, 2.5), new Interval(3.5, 4.5)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2.5, 3.5), new Interval(4.5, 5.5)));

        Hyperplane<Euclidean2D> splitHyperplane = new Hyperplane<Euclidean2D>(new Line(new Vector2D(2.0, 2.5), new Vector2D(2.0, 3.5)));

        SubHyperplane<Euclidean1D> splitSubHyperplane1 = subLine1.split(splitHyperplane);
        assertNotNull(splitSubHyperplane1);

        SubHyperplane<Euclidean1D> splitSubHyperplane2 = subLine2.split(splitHyperplane);
        assertNotNull(splitSubHyperplane2);

        SubLine leftSubLine1 = (SubLine) splitSubHyperplane1.getSubHyperplane();
        SubLine rightSubLine1 = (SubLine) splitSubHyperplane1.getSubHyperplane2();

        SubLine leftSubLine2 = (SubLine) splitSubHyperplane2.getSubHyperplane();
        SubLine rightSubLine2 = (SubLine) splitSubHyperplane2.getSubHyperplane2();

        List<Segment> segmentsLeft1 = leftSubLine1.getSegments();
        List<Segment> segmentsRight1 = rightSubLine1.getSegments();

        List<Segment> segmentsLeft2 = leftSubLine2.getSegments();
        List<Segment> segmentsRight2 = rightSubLine2.getSegments();

        assertEquals(1, segmentsLeft1.size());
        assertEquals(1, segmentsRight1.size());
        assertEquals(1, segmentsLeft2.size());
        assertEquals(1, segmentsRight2.size());

        Segment segmentLeft1 = segmentsLeft1.get(0);
        Segment segmentRight1 = segmentsRight1.get(0);
        Segment segmentLeft2 = segmentsLeft2.get(0);
        Segment segmentRight2 = segmentsRight2.get(0);

        assertEquals(p1, segmentLeft1.getStart());
        assertEquals(p1, segmentLeft1.getEnd());
        assertEquals(p2, segmentRight1.getStart());
        assertEquals(p2, segmentRight1.getEnd());

        assertEquals(q1, segmentLeft2.getStart());
        assertEquals(q1, segmentLeft2.getEnd());
        assertEquals(q2, segmentRight2.getStart());
        assertEquals(q2, segmentRight2.getEnd());

        SubLine mergedSubLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.5, 2.5), new Interval(3.5, 4.5)));
        SubLine mergedSubLine2 = new SubLine(line2, new IntervalsSet(new Interval(2.5, 3.5), new Interval(4.5, 5.5)));

        SubLine mergedSubLineLeft = (SubLine) leftSubLine1.getHyperplane();
        SubLine mergedSubLineRight = (SubLine) rightSubLine1.getHyperplane();

        assertEquals(mergedSubLine1, mergedSubLineLeft);
        assertEquals(mergedSubLine2, mergedSubLineRight);
    }

}