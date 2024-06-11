import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Point2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segments2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Planes;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SubHyperplaneSet;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstructor() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Vector2D start = space.createVector(1.0, 0.0);
        Vector2D end   = space.createVector(0.0, 1.0);

        SubLine line = new SubLine(start, end);
        Line expected = new Line(start, end);
        assertEquals(expected.toString(), line.getHyperplane().toString());

        IntervalsSet intervals = line.getRemainingRegion();
        List<Interval> list = intervals.asList();
        assertEquals(1, list.size());
        Interval interval = list.get(0);
        assertEquals(start.getX(), interval.getInf().getComponent(0), 1e-14);
        assertEquals(end.getX(), interval.getSup().getComponent(0), 1e-14);

        assertEquals(line.getSegments().size(), 1);
        Segment segment = line.getSegments().get(0);
        assertEquals(start, segment.getStart());
        assertEquals(end, segment.getEnd());
    }

    @Test
    public void testSubLineIntersection() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Vector2D start1 = space.createVector(0.0, 0.0);
        Vector2D end1   = space.createVector(2.0, 0.0);
        Vector2D start2 = space.createVector(2.0, 2.0);
        Vector2D end2   = space.createVector(0.0, 2.0);

        SubLine line1 = new SubLine(start1, end1);
        SubLine line2 = new SubLine(start2, end2);
        Vector2D expected = start1.add(end1).divide(2.0);

        assertNull(line1.intersection(line2, true));
        assertNull(line1.intersection(line2, false));

        start2 = space.createVector(1.0, 1.0);
        end2 = space.createVector(1.0, 3.0);

        expected = line1.intersection(line2, true);
        assertNotNull(expected);
        assertEquals(expected.getX(), (start1.getX() + end1.getX()) / 2.0, 1e-14);
        assertEquals(expected.getY(), (start1.getY() + end1.getY()) / 2.0, 1e-14);

        expected = line1.intersection(line2, false);
        assertNotNull(expected);
        assertEquals(expected.getX(), (start1.getX() + end1.getX()) / 2.0, 1e-14);
        assertEquals(expected.getY(), (start1.getY() + end1.getY()) / 2.0, 1e-14);
    }

    @Test
    public void testSubLineSplit() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Vector2D start1 = space.createVector(0.0, 0.0);
        Vector2D end1   = space.createVector(2.0, 0.0);
        Vector2D start2 = space.createVector(2.0, 2.0);
        Vector2D end2   = space.createVector(0.0, 2.0);

        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        Hyperplane<Euclidean2D> hyperplane = Planes.linePlane(line1, space.getDimension());

        SubLine line = new SubLine(start1, end1);
        SubLine split1 = line.split(hyperplane);
        SubLine split2 = line.split(hyperplane);

        assertNull(split1.getRemainingRegion().isEmpty(split1.getHyperplane().getRemainingRegion()));
        assertNull(split2.getRemainingRegion().isEmpty(split2.getHyperplane().getRemainingRegion()));
        assertNotNull(split1.getHyperplane());
        assertNotNull(split2.getHyperplane());
        assertFalse(split1.equals(split2));

        List<Segment> segments1 = split1.getSegments();
        List<Segment> segments2 = split2.getSegments();
        assertEquals(segments1.size(), 1);
        assertEquals(segments2.size(), 1);

        Segment segment1 = segments1.get(0);
        Segment segment2 = segments2.get(0);

        assertNotSame(segment1, segment2);
        assertEquals(segment1.getStart().getX(), segment2.getStart().getX(), 1e-14);
        assertEquals(segment1.getStart().getY(), segment2.getStart().getY(), 1e-14);
        assertEquals(segment1.getEnd().getX(), segment2.getEnd().getX(), 1e-14);
        assertEquals(segment1.getEnd().getY(), segment2.getEnd().getY(), 1e-14);
    }

    @Test
    public void testSubLineSplitZero() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Segments2D segments = new Segments2D(space);
        Segment segment = segments.createSegment(space.createVector(0.0, 0.0), space.createVector(1.0, 1.0));
        SubLine subLine = new SubLine(segment);

        Hyperplane<Euclidean2D> hyperplane = Planes.linePlane(segment.getLine(), space.getDimension());
        SubHyperplane<Euclidean2D> left = subLine.split(hyperplane);
        SubHyperplane<Euclidean2D> right = subLine.split(hyperplane);

        assertNotNull(left);
        assertNotNull(right);
        assertFalse(left.equals(right));
        assertEquals(left.getHyperplane().getOffset(subLine.getHyperplane()), 0.0, 1e-14);
        assertEquals(right.getHyperplane().getOffset(subLine.getHyperplane()), 0.0, 1e-14);

        List<Segment> segmentsLeft = left.getSegments();
        List<Segment> segmentsRight = right.getSegments();

        assertEquals(segmentsLeft.size(), 1);
        assertEquals(segmentsRight.size(), 1);

        Segment segmentLeft = segmentsLeft.get(0);
        Segment segmentRight = segmentsRight.get(0);

        assertEquals(segmentLeft.getStart().getX(), 0.0, 1e-14);
        assertEquals(segmentLeft.getStart().getY(), 0.0, 1e-14);
        assertEquals(segmentLeft.getEnd().getX(), 1.0, 1e-14);
        assertEquals(segmentLeft.getEnd().getY(), 1.0, 1e-14);

        assertEquals(segmentRight.getStart().getX(), 1.0, 1e-14);
        assertEquals(segmentRight.getStart().getY(), 0.0, 1e-14);
        assertEquals(segmentRight.getEnd().getX(), 2.0, 1e-14);
        assertEquals(segmentRight.getEnd().getY(), 1.0, 1e-14);
    }

    @Test
    public void testSubLineSplitParallel() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Segments2D segments = new Segments2D(space);
        Segment segment = segments.createSegment(space.createVector(0.0, 0.0), space.createVector(1.0, 1.0));
        SubLine subLine = new SubLine(segment);

        Line line = new Line(space.createVector(0.0, 1.0), space.createVector(1.0, 1.0));
        Hyperplane<Euclidean2D> hyperplane = line;

        SubHyperplane<Euclidean2D> left = subLine.split(hyperplane);
        SubHyperplane<Euclidean2D> right = subLine.split(hyperplane);

        assertNotNull(left);
        assertNotNull(right);
        assertFalse(left.equals(right));
        assertEquals(left.getHyperplane().getOffset(subLine.getHyperplane()), -1.0, 1e-14);
        assertEquals(right.getHyperplane().getOffset(subLine.getHyperplane()), 1.0, 1e-14);

        List<Segment> segmentsLeft = left.getSegments();
        List<Segment> segmentsRight = right.getSegments();

        assertEquals(segmentsLeft.size(), 1);
        assertEquals(segmentsRight.size(), 1);

        Segment segmentLeft = segmentsLeft.get(0);
        Segment segmentRight = segmentsRight.get(0);

        assertEquals(segmentLeft.getStart().getX(), 0.0, 1e-14);
        assertEquals(segmentLeft.getStart().getY(), 0.0, 1e-14);
        assertEquals(segmentLeft.getEnd().getX(), 1.0, 1e-14);
        assertEquals(segmentLeft.getEnd().getY(), 1.0, 1e-14);

        assertEquals(segmentRight.getStart().getX(), 0.0, 1e-14);
        assertEquals(segmentRight.getStart().getY(), 1.0, 1e-14);
        assertEquals(segmentRight.getEnd().getX(), 1.0, 1e-14);
        assertEquals(segmentRight.getEnd().getY(), 2.0, 1e-14);
    }
}