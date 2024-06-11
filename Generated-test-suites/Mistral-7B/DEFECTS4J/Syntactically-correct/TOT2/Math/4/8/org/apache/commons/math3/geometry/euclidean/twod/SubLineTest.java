import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.two.Line;
import org.apache.commons.math3.geometry.euclidean.two.Segment;
import org.apache.commons.math3.geometry.euclidean.two.Space2D;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    private static Hyperplane<Space2D> createHyperplane(Vector2D p1, Vector2D p2) {
        Line line = new Line(p1, p2);
        IntervalSet iset = SubLine.buildIntervalSet(p1, p2);
        return new SubLine(line, iset);
    }

    @Test
    public void testSubLineCreation() {
        Vector2D p1 = new Vector2D(1.0, 1.0);
        Vector2D p2 = new Vector2D(2.0, 2.0);
        Hyperplane<Space2D> hp = createHyperplane(p1, p2);
        SubLine subLine = (SubLine) hp;
        List<Segment> segments = subLine.getSegments();

        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        Vector2D start = segment.getStart();
        Vector2D end = segment.getEnd();

        assertEquals(p1, start);
        assertEquals(p2, end);
    }

    @Test
    public void testIntersectionNull() {
        Vector2D p1 = new Vector2D(1.0, 1.0);
        Vector2D p2 = new Vector2D(1.0, 2.0);
        Hyperplane<Space2D> hp1 = createHyperplane(p1, p2);
        Vector2D q1 = new Vector2D(2.0, 2.0);
        Hyperplane<Space2D> hp2 = createHyperplane(q1, new Vector2D(3.0, 2.0));

        assertNull(hp1.intersection(hp2, true));
    }

    @Test
    public void testIntersectionPoint() {
        Vector2D p1 = new Vector2D(1.0, 1.0);
        Vector2D p2 = new Vector2D(1.0, 2.0);
        Hyperplane<Space2D> hp1 = createHyperplane(p1, p2);
        Vector2D q1 = new Vector2D(1.5, 1.5);
        Hyperplane<Space2D> hp2 = createHyperplane(new Vector2D(1.5, 1.0), new Vector2D(1.5, 2.0));

        Vector2D p = hp1.intersection(hp2, true);

        assertEquals(Precision.round(p.getX(), 4), 1.5, 0.0);
        assertEquals(Precision.round(p.getY(), 4), 1.5, 0.0);
    }

    @Test
    public void testIntersectionNullSecond() {
        Vector2D p1 = new Vector2D(1.0, 1.0);
        Vector2D p2 = new Vector2D(1.0, 2.0);
        Hyperplane<Space2D> hp1 = createHyperplane(p1, p2);
        Hyperplane<Space2D> hp2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0)).getHyperplane();

        assertNull(hp1.intersection(hp2, true));
    }

    @Test
    public void testIntersectionParallel() {
        Vector2D p1 = new Vector2D(1.0, 1.0);
        Vector2D p2 = new Vector2D(1.0, 2.0);
        Hyperplane<Space2D> hp1 = createHyperplane(p1, p2);
        Vector2D q1 = new Vector2D(2.0, 2.0);
        Hyperplane<Space2D> hp2 = createHyperplane(q1, new Vector2D(3.0, 2.0));

        assertNull(hp1.intersection(hp2, true));
    }

    @Test
    public void testIntersectionOverlap() {
        Vector2D p1 = new Vector2D(1.0, 1.0);
        Vector2D p2 = new Vector2D(1.0, 2.0);
        Hyperplane<Space2D> hp1 = createHyperplane(p1, p2);
        Vector2D q1 = new Vector2D(0.5, 1.5);
        Hyperplane<Space2D> hp2 = createHyperplane(new Vector2D(0.5, 1.0), new Vector2D(0.5, 2.0));

        Vector2D p = hp1.intersection(hp2, true);

        assertEquals(Precision.round(p.getX(), 4), 0.5, 0.0);
        assertEquals(Precision.round(p.getY(), 4), 1.5, 0.0);
    }

    @Test
    public void testIntersectionEndPoints() {
        Vector2D p1 = new Vector2D(1.0, 1.0);
        Vector2D p2 = new Vector2D(1.0, 2.0);
        Hyperplane<Space2D> hp1 = createHyperplane(p1, p2);
        Vector2D q1 = new Vector2D(1.0, 2.0);
        Hyperplane<Space2D> hp2 = createHyperplane(q1, new Vector2D(1.0, 3.0));

        Vector2D p = hp1.intersection(hp2, true);

        assertNull(p);
    }

    @Test
    public void testSplitNull() {
        Line line = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        Hyperplane<Space2D> hp = line.getHyperplane();
        assertNull(hp.split(null));
    }

    @Test
    public void testSplitParallel() {
        Line line = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        Hyperplane<Space2D> hp1 = line.getHyperplane();
        Hyperplane<Space2D> hp2 = new Line(new Vector2D(2.0, 2.0), new Vector2D(3.0, 3.0)).getHyperplane();

        SubHyperplane<SubLine> sh = hp1.split(hp2);

        assertNull(sh.getSubHyperplaneA());
        assertNull(sh.getSubHyperplaneB());
    }

    @Test
    public void testSplitIntersecting() {
        Line line = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        Hyperplane<Space2D> hp1 = line.getHyperplane();
        Hyperplane<Space2D> hp2 = new Line(new Vector2D(1.5, 1.5), new Vector2D(2.5, 2.5)).getHyperplane();

        SubHyperplane<SubLine> sh = hp1.split(hp2);

        SubLine subLineA = sh.getSubHyperplaneA();
        SubLine subLineB = sh.getSubHyperplaneB();

        List<Segment> segmentsA = subLineA.getSegments();
        List<Segment> segmentsB = subLineB.getSegments();

        assertEquals(1, segmentsA.size());
        assertEquals(1, segmentsB.size());

        Segment segmentA = segmentsA.get(0);
        Segment segmentB = segmentsB.get(0);

        Vector2D startA = segmentA.getStart();
        Vector2D endA = segmentA.getEnd();
        Vector2D startB = segmentB.getStart();
        Vector2D endB = segmentB.getEnd();

        assertEquals(Precision.round(startA.getX(), 4), 1.5, 0.0);
        assertEquals(Precision.round(startA.getY(), 4), 1.5, 0.0);
        assertEquals(Precision.round(endA.getX(), 4), 2.5, 0.0);
        assertEquals(Precision.round(endA.getY(), 4), 1.5, 0.0);

        assertEquals(Precision.round(startB.getX(), 4), 1.5, 0.0);
        assertEquals(Precision.round(startB.getY(), 4), 1.5, 0.0);
        assertEquals(Precision.round(endB.getX(), 4), 1.5, 0.0);
        assertEquals(Precision.round(endB.getY(), 4), 2.5, 0.0);
    }

    @Test
    public void testSplitDiverging() {
        Line line = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        Hyperplane<Space2D> hp1 = line.getHyperplane();
        Hyperplane<Space2D> hp2 = new Line(new Vector2D(3.0, 3.0), new Vector2D(4.0, 4.0)).getHyperplane();

        SubHyperplane<SubLine> sh = hp1.split(hp2);

        assertNull(sh.getSubHyperplaneA());
        assertNull(sh.getSubHyperplaneB());
    }
}