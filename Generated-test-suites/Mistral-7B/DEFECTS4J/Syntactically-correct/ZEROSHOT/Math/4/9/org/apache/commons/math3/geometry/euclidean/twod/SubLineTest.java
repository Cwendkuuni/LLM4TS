import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        Euclidean2DSpace euclidean2DSpace = new Euclidean2DSpace();
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end = new Vector2D(3.0, 4.0);
        Line line = new Line(start, end);
        IntervalsSet intervalsSet = new IntervalsSet(new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0)));
        SubLine subLine = new SubLine(line, intervalsSet);
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(start, segment.getStart());
        assertEquals(end, segment.getEnd());
    }

    @Test
    public void testSubLineIntersection() {
        Euclidean2DSpace euclidean2DSpace = new Euclidean2DSpace();
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Line line1 = new Line(start1, end1);
        Vector2D start2 = new Vector2D(1.0, 1.0);
        Vector2D end2 = new Vector2D(2.0, 2.0);
        Line line2 = new Line(start2, end2);
        IntervalsSet intervalsSet1 = new IntervalsSet(new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 0.5)));
        IntervalsSet intervalsSet2 = new IntervalsSet(new Line(new Vector2D(1.5, 1.5), new Vector2D(2.5, 2.5)));
        SubLine subLine1 = new SubLine(line1, intervalsSet1);
        SubLine subLine2 = new SubLine(line2, intervalsSet2);
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(1.0, 0.5), intersectionPoint);
    }

    @Test
    public void testSubLineIntersectionEmpty() {
        Euclidean2DSpace euclidean2DSpace = new Euclidean2DSpace();
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Line line1 = new Line(start1, end1);
        Vector2D start2 = new Vector2D(2.0, 2.0);
        Vector2D end2 = new Vector2D(3.0, 3.0);
        Line line2 = new Line(start2, end2);
        IntervalsSet intervalsSet1 = new IntervalsSet(new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 0.5)));
        IntervalsSet intervalsSet2 = new IntervalsSet(new Line(new Vector2D(1.5, 1.5), new Vector2D(2.5, 2.5)));
        SubLine subLine1 = new SubLine(line1, intervalsSet1);
        SubLine subLine2 = new SubLine(line2, intervalsSet2);
        Vector2D intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);
    }

    @Test
    public void testSubLineSplit() {
        Euclidean2DSpace euclidean2DSpace = new Euclidean2DSpace();
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Line line1 = new Line(start1, end1);
        Vector2D start2 = new Vector2D(1.0, 1.0);
        Vector2D end2 = new Vector2D(2.0, 2.0);
        Line line2 = new Line(start2, end2);
        IntervalsSet intervalsSet1 = new IntervalsSet(new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 0.5)));
        IntervalsSet intervalsSet2 = new IntervalsSet(new Line(new Vector2D(1.5, 1.5), new Vector2D(2.5, 2.5)));
        SubLine subLine1 = new SubLine(line1, intervalsSet1);
        SubLine subLine2 = new SubLine(line2, intervalsSet2);
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine1.split(hyperplane);
        SubLine subLinePlus = (SubLine) splitSubHyperplane.getSubHyperplane1();
        SubLine subLineMinus = (SubLine) splitSubHyperplane.getSubHyperplane2();
        List<Segment> segmentsPlus = subLinePlus.getSegments();
        List<Segment> segmentsMinus = subLineMinus.getSegments();
        Vector2D startSegmentPlus = segmentsPlus.get(0).getStart();
        Vector2D startSegmentMinus = segmentsMinus.get(0).getStart();
        Vector2D endSegmentPlus = segmentsPlus.get(0).getEnd();
        Vector2D endSegmentMinus = segmentsMinus.get(0).getEnd();
        assertEquals(new Vector2D(0.5, 0.5), startSegmentPlus);
        assertEquals(new Vector2D(1.5, 0.5), endSegmentPlus);
        assertEquals(new Vector2D(1.5, 1.5), startSegmentMinus);
        assertEquals(new Vector2D(2.5, 2.5), endSegmentMinus);
        assertFalse(startSegmentPlus.equals(startSegmentMinus));
        assertFalse(endSegmentPlus.equals(endSegmentMinus));
        SubLine intersectionSubLine = new SubLine(line1, new IntervalsSet(new Line(startSegmentMinus, endSegmentMinus)));
        Vector2D intersectionPoint = intersectionSubLine.intersection(subLine2, true);
        assertEquals(new Vector2D(1.0, 1.0), intersectionPoint);
    }

    @Test
    public void testSubLineSide() {
        Euclidean2DSpace euclidean2DSpace = new Euclidean2DSpace();
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end = new Vector2D(3.0, 4.0);
        Line line = new Line(start, end);
        SubLine subLine = new SubLine(line, new IntervalsSet(new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0))));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 2.0), new Vector2D(4.0, 2.0));
        Side side = subLine.side(hyperplane);
        assertEquals(Side.MINUS, side);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(3.0, 0.0), new Vector2D(3.0, 4.0));
        Side side2 = subLine.side(hyperplane2);
        assertEquals(Side.PLUS, side2);
    }

    @Test
    public void testSubLineEquals() {
        Euclidean2DSpace euclidean2DSpace = new Euclidean2DSpace();
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Line line1 = new Line(start1, end1);
        IntervalsSet intervalsSet1 = new IntervalsSet(new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 0.5)));
        SubLine subLine1 = new SubLine(line1, intervalsSet1);
        Vector2D start2 = new Vector2D(1.0, 1.0);
        Vector2D end2 = new Vector2D(2.0, 2.0);
        Line line2 = new Line(start2, end2);
        IntervalsSet intervalsSet2 = new IntervalsSet(new Line(new Vector2D(1.5, 1.5), new Vector2D(2.5, 2.5)));
        SubLine subLine2 = new SubLine(line2, intervalsSet2);
        SubLine subLine3 = new SubLine(line1, intervalsSet1);
        assertTrue(subLine1.equals(subLine3));
        assertFalse(subLine1.equals(subLine2));
    }

    @Test
    public void testSubLineHashCode() {
        Euclidean2DSpace euclidean2DSpace = new Euclidean2DSpace();
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Line line1 = new Line(start1, end1);
        IntervalsSet intervalsSet1 = new IntervalsSet(new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 0.5)));
        SubLine subLine1 = new SubLine(line1, intervalsSet1);
        Vector2D start2 = new Vector2D(1.0, 1.0);
        Vector2D end2 = new Vector2D(2.0, 2.0);
        Line line2 = new Line(start2, end2);
        IntervalsSet intervalsSet2 = new IntervalsSet(new Line(new Vector2D(1.5, 1.5), new Vector2D(2.5, 2.5)));
        SubLine subLine2 = new SubLine(line2, intervalsSet2);
        int hashCode1 = subLine1.hashCode();
        SubLine subLine3 = new SubLine(line1, intervalsSet1);
        int hashCode2 = subLine3.hashCode();
        assertEquals(hashCode1, hashCode2);
        int hashCode3 = subLine2.hashCode();
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testSubLineToString() {
        Euclidean2DSpace euclidean2DSpace = new Euclidean2DSpace();
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end = new Vector2D(3.0, 4.0);
        Line line = new Line(start, end);
        IntervalsSet intervalsSet = new IntervalsSet(new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 0.5)));
        SubLine subLine = new SubLine(line, intervalsSet);
        String expected = "SubLine{" +
                "line=" + line +
                ", intervals=" + intervalsSet +
                '}';
        String string = subLine.toString();
        assertEquals(expected, string);
    }

    @Test
    public void testSubLineGetSegments() {
        Euclidean2DSpace euclidean2DSpace = new Euclidean2DSpace();
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Line line1 = new Line(start1, end1);
        IntervalsSet intervalsSet1 = new IntervalsSet(new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 0.5)));
        SubLine subLine1 = new SubLine(line1, intervalsSet1);
        List<Segment> segments = subLine1.getSegments();
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(new Vector2D(0.5, 0.5), segment.getStart());
        assertEquals(new Vector2D(1.5, 0.5), segment.getEnd());
    }

    @Test
    public void testSubLineGetSegmentsEmpty() {
        Euclidean2DSpace euclidean2DSpace = new Euclidean2DSpace();
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 0.0);
        Line line = new Line(start, end);
        IntervalsSet intervalsSet = new IntervalsSet();
        SubLine subLine = new SubLine(line, intervalsSet);
        List<Segment> segments = subLine.getSegments();
        assertEquals(0, segments.size());
    }

    @Test
    public void testSubLineGetSegmentsInfInit() {
        Euclidean2DSpace euclidean2DSpace = new Euclidean2DSpace();
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        Line line = new Line(start, end);
        IntervalsSet intervalsSet = new IntervalsSet(new Line(new Vector2D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY), end));
        SubLine subLine = new SubLine(line, intervalsSet);
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(new Vector2D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY), segment.getStart());
        assertEquals(end, segment.getEnd());
    }

    @Test
    public void testSubLineGetSegmentsInfEnd() {
        Euclidean2DSpace euclidean2DSpace = new Euclidean2DSpace();
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        Line line = new Line(start, end);
        IntervalsSet intervalsSet = new IntervalsSet(new Line(start, new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)));
        SubLine subLine = new SubLine(line, intervalsSet);
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(start, segment.getStart());
        assertTrue(Precision.isClose(end.getX(), segment.getEnd().getX(), 1e-6));
        assertTrue(Precision.isClose(end.getY(), segment.getEnd().getY(), 1e-6));
    }
}