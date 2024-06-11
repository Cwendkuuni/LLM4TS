import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubHyperplane;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane.SplitSubHyperplane;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        Vector2D p1 = new Vector2D(1.0, 2.0);
        Vector2D p2 = new Vector2D(3.0, 4.0);
        SubLine subLine1 = new SubLine(p1, p2);
        Line line1 = subLine1.getHyperplane();
        assertEquals(p1, line1.getStartVector());
        assertEquals(p2, line1.getEndVector());

        Segment segment = new Segment(new Vector2D(-1.0, -1.0), new Vector2D(1.0, 1.0), line1);
        SubLine subLine2 = new SubLine(segment);
        Line line2 = subLine2.getHyperplane();
        assertEquals(line1, line2);

        Hyperplane<Euclidean2D> hyperplane = new Hyperplane<Euclidean2D>(Euclidean2DSpace.EUCLIDEAN_2D, new Vector2D(1.0, 0.0), 1.0);
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 2.0), new Interval(3.0, 5.0));
        SubLine subLine3 = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine3.getHyperplane());
        assertEquals(remainingRegion, subLine3.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Vector2D p1 = new Vector2D(1.0, 2.0);
        Vector2D p2 = new Vector2D(3.0, 4.0);
        SubLine subLine = new SubLine(p1, p2);
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(p1, segment.getStartVector());
        assertEquals(p2, segment.getEndVector());

        p1 = new Vector2D(1.0, 0.0);
        p2 = new Vector2D(3.0, 0.0);
        subLine = new SubLine(p1, p2);
        segments = subLine.getSegments();
        assertEquals(1, segments.size());
        segment = segments.get(0);
        assertEquals(p1, segment.getStartVector());
        assertEquals(p2, segment.getEndVector());

        p1 = new Vector2D(1.0, 2.0);
        p2 = new Vector2D(1.0, 4.0);
        subLine = new SubLine(p1, p2);
        segments = subLine.getSegments();
        assertEquals(1, segments.size());
        segment = segments.get(0);
        assertEquals(p1, segment.getStartVector());
        assertEquals(p2, segment.getEndVector());

        p1 = new Vector2D(1.0, 2.0);
        p2 = new Vector2D(1.0, 2.0);
        subLine = new SubLine(p1, p2);
        segments = subLine.getSegments();
        assertEquals(0, segments.size());
    }

    @Test
    public void testIntersection() {
        Vector2D p1 = new Vector2D(1.0, 2.0);
        Vector2D p2 = new Vector2D(3.0, 4.0);
        Vector2D q1 = new Vector2D(0.0, 3.0);
        Vector2D q2 = new Vector2D(4.0, 5.0);
        SubLine subLine1 = new SubLine(p1, p2);
        SubLine subLine2 = new SubLine(q1, q2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);

        p1 = new Vector2D(1.0, 2.0);
        p2 = new Vector2D(3.0, 4.0);
        q1 = new Vector2D(1.0, 1.0);
        q2 = new Vector2D(3.0, 3.0);
        subLine1 = new SubLine(p1, p2);
        subLine2 = new SubLine(q1, q2);
        intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(2.0, 2.0), intersectionPoint);

        p1 = new Vector2D(1.0, 2.0);
        p2 = new Vector2D(3.0, 4.0);
        q1 = new Vector2D(1.0, 1.0);
        q2 = new Vector2D(3.0, 3.0);
        subLine1 = new SubLine(p1, p2);
        subLine2 = new SubLine(q1, q2);
        intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);

        p1 = new Vector2D(1.0, 2.0);
        p2 = new Vector2D(3.0, 4.0);
        q1 = new Vector2D(1.0, 1.0);
        q2 = new Vector2D(3.0, 3.0);
        Vector2D start = new Vector2D(0.5, 2.5);
        Vector2D end = new Vector2D(2.5, 3.5);
        SubLine subLine3 = new SubLine(p1, p2);
        SubLine subLine4 = new SubLine(q1, q2);
        intersectionPoint = subLine3.intersection(subLine4, start, end, true);
        assertNull(intersectionPoint);

        p1 = new Vector2D(1.0, 2.0);
        p2 = new Vector2D(3.0, 4.0);
        q1 = new Vector2D(1.0, 1.0);
        q2 = new Vector2D(3.0, 3.0);
        start = new Vector2D(1.5, 2.5);
        end = new Vector2D(2.5, 3.5);
        subLine3 = new SubLine(p1, p2);
        subLine4 = new SubLine(q1, q2);
        intersectionPoint = subLine3.intersection(subLine4, start, end, true);
        assertEquals(new Vector2D(2.0, 2.0), intersectionPoint);
    }

    @Test
    public void testSplit() {
        Vector2D p1 = new Vector2D(1.0, 2.0);
        Vector2D p2 = new Vector2D(3.0, 4.0);
        Vector2D q1 = new Vector2D(0.0, 3.0);
        Vector2D q2 = new Vector2D(4.0, 5.0);
        SubLine subLine1 = new SubLine(p1, p2);
        SubLine subLine2 = new SubLine(q1, q2);

        Hyperplane<Euclidean2D> hyperplane = new Hyperplane<Euclidean2D>(Euclidean2DSpace.EUCLIDEAN_2D, new Vector2D(1.0, 1.0), 1.0);
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 2.0), new Interval(3.0, 5.0));
        SubLine subLine3 = new SubLine(hyperplane, remainingRegion);
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine1.split(subLine2);
        assertNull(splitSubHyperplane.getLeftSubHyperplane());
        assertNull(splitSubHyperplane.getRightSubHyperplane());

        p1 = new Vector2D(1.0, 2.0);
        p2 = new Vector2D(3.0, 4.0);
        q1 = new Vector2D(1.0, 1.0);
        q2 = new Vector2D(3.0, 3.0);
        Hyperplane<Euclidean2D> hyperplane1 = new Hyperplane<Euclidean2D>(Euclidean2DSpace.EUCLIDEAN_2D, new Vector2D(1.0, 0.0), 1.0);
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0.0, 2.0), new Interval(3.0, 5.0));
        SubLine subLine4 = new SubLine(hyperplane1, remainingRegion1);
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane1 = subLine1.split(subLine2);
        SubHyperplane<Euclidean1D> leftSubHyperplane = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 1.0));
        SubHyperplane<Euclidean1D> rightSubHyperplane = new Line(new Vector2D(3.0, 3.0), new Vector2D(3.0, 2.0));
        assertEquals(leftSubHyperplane, splitSubHyperplane1.getLeftSubHyperplane());
        assertEquals(rightSubHyperplane, splitSubHyperplane1.getRightSubHyperplane());

        p1 = new Vector2D(1.0, 2.0);
        p2 = new Vector2D(3.0, 4.0);
        q1 = new Vector2D(1.0, 1.0);
        q2 = new Vector2D(3.0, 3.0);
        Hyperplane<Euclidean2D> hyperplane2 = new Hyperplane<Euclidean2D>(Euclidean2DSpace.EUCLIDEAN_2D, new Vector2D(2.0, 2.0), 1.0);
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(0.0, 2.0), new Interval(3.0, 5.0));
        SubLine subLine5 = new SubLine(hyperplane2, remainingRegion2);
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane2 = subLine1.split(subLine2);
        SubHyperplane<Euclidean1D> leftSubHyperplane1 = new Line(new Vector2D(1.0, 1.0), new Vector2D(1.0, 2.0));
        SubHyperplane<Euclidean1D> rightSubHyperplane1 = new Line(new Vector2D(3.0, 3.0), new Vector2D(3.0, 2.0));
        SubHyperplane<Euclidean1D> leftSubHyperplane2 = new Line(new Vector2D(1.0, 2.0), new Vector2D(1.5, 2.5));
        SubHyperplane<Euclidean1D> rightSubHyperplane2 = new Line(new Vector2D(2.5, 3.5), new Vector2D(3.0, 2.0));
        assertEquals(leftSubHyperplane1, splitSubHyperplane2.getLeftSubHyperplane());
        assertEquals(rightSubHyperplane2, splitSubHyperplane2.getRightSubHyperplane());

        p1 = new Vector2D(1.0, 2.0);
        p2 = new Vector2D(3.0, 4.0);
        q1 = new Vector2D(1.0, 1.0);
        q2 = new Vector2D(3.0, 3.0);
        Hyperplane<Euclidean2D> hyperplane3 = new Hyperplane<Euclidean2D>(Euclidean2DSpace.EUCLIDEAN_2D, new Vector2D(1.5, 2.5), 0.0);
        Region<Euclidean1D> remainingRegion3 = new IntervalsSet(new Interval(0.0, 2.0), new Interval(3.0, 5.0));
        SubLine subLine6 = new SubLine(hyperplane3, remainingRegion3);
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane3 = subLine1.split(subLine2);
        SubHyperplane<Euclidean1D> leftSubHyperplane3 = new Line(new Vector2D(1.0, 1.0), new Vector2D(1.5, 2.5));
        SubHyperplane<Euclidean1D> rightSubHyperplane3 = new Line(new Vector2D(1.5, 2.5), new Vector2D(3.0, 2.0));
        assertEquals(leftSubHyperplane3, splitSubHyperplane3.getLeftSubHyperplane());
        assertEquals(rightSubHyperplane1, splitSubHyperplane3.getRightSubHyperplane());
    }

}