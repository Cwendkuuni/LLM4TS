import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SubSpace;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstructor() {
        Vector2D start = new Vector2D(1.0, 1.0);
        Vector2D end   = new Vector2D(3.0, 1.0);
        SubLine subLine = new SubLine(start, end);
        Line line = (Line) subLine.getHyperplane();
        assertEquals(line.getSlope(), 0.5, 0.01);
        assertEquals(subLine.getRemainingRegion().getMinimum().getInf(), 1.0, 0.01);
        assertEquals(subLine.getRemainingRegion().getMinimum().getSup(), 3.0, 0.01);
        assertEquals(subLine.getRemainingRegion().getMaximum().getInf(), 3.0, 0.01);
        assertEquals(subLine.getRemainingRegion().getMaximum().getSup(), 5.0, 0.01);
    }

    @Test
    public void testSubLineIntersection() {
        Vector2D start1 = new Vector2D(1.0, 1.0);
        Vector2D end1   = new Vector2D(3.0, 1.0);
        Vector2D start2 = new Vector2D(2.0, 2.0);
        Vector2D end2   = new Vector2D(4.0, 2.0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);

        start1 = new Vector2D(1.0, 1.0);
        start2 = new Vector2D(1.0, 3.0);
        end1   = new Vector2D(3.0, 1.0);
        end2   = new Vector2D(3.0, 3.0);
        subLine1 = new SubLine(start1, end1);
        subLine2 = new SubLine(start2, end2);
        intersection = subLine1.intersection(subLine2, true);
        Vector2D expected = new Vector2D(2.0, 2.0);
        assertEquals(expected.getX(), intersection.getX(), 0.01);
        assertEquals(expected.getY(), intersection.getY(), 0.01);

        start1 = new Vector2D(1.0, 1.0);
        start2 = new Vector2D(1.0, 3.0);
        end1   = new Vector2D(3.0, 1.0);
        end2   = new Vector2D(3.0, 3.0);
        subLine1 = new SubLine(start1, end1);
        subLine2 = new SubLine(start2, end2);
        intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);

        start1 = new Vector2D(1.0, 1.0);
        start2 = new Vector2D(1.0, 3.0);
        end1 = new Vector2D(3.0, 1.0);
        end2 = new Vector2D(3.0, 3.0);
        subLine1 = new SubLine(start1, end1);
        subLine2 = new SubLine(start2, end2);
        Segment segment = subLine1.getSegments().get(0);
        Line line = segment.getLine();
        Vector2D point = line.intersection(line.perpendicular().rotate(-Math.PI / 2.0));
        SubLine subLine3 = new SubLine(point, point);
        intersection = subLine1.intersection(subLine3, true);
        assertNull(intersection);
    }

    @Test
    public void testSubLineSplit() {
        Vector2D start = new Vector2D(1.0, 1.0);
        Vector2D end   = new Vector2D(3.0, 1.0);
        SubLine subLine = new SubLine(start, end);
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 1.0), 1.0).getPerpendicular();
        SplitSubHyperplane<Euclidean2D> split = subLine.split(hyperplane);
        SubLine subLinePlus = (SubLine) split.getSubHyperplane1();
        SubLine subLineMinus = (SubLine) split.getSubHyperplane2();
        assertTrue(subLinePlus.getRemainingRegion().intersects(subLineMinus.getRemainingRegion()));
        assertFalse(subLinePlus.getRemainingRegion().intersects(subLine.getRemainingRegion()));
        assertFalse(subLineMinus.getRemainingRegion().intersects(subLine.getRemainingRegion()));

        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(1.0, 0.0), 1.0).getPerpendicular();
        split = subLine.split(hyperplane2);
        subLinePlus = (SubLine) split.getSubHyperplane1();
        subLineMinus = (SubLine) split.getSubHyperplane2();
        assertTrue(subLinePlus.getRemainingRegion().isEmpty());
        assertTrue(subLineMinus.getRemainingRegion().isEmpty());
    }

    @Test
    public void testSubLineSide() {
        Vector2D start = new Vector2D(1.0, 1.0);
        Vector2D end   = new Vector2D(3.0, 1.0);
        SubLine subLine = new SubLine(start, end);
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 1.0), 1.0).getPerpendicular();
        Side side = subLine.side(hyperplane);
        assertEquals(Side.MINUS, side);

        start = new Vector2D(1.0, 1.0);
        end = new Vector2D(3.0, 1.0);
        SubLine subLine2 = new SubLine(start, end);
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(1.0, 0.0), 1.0).getPerpendicular();
        Side side2 = subLine2.side(hyperplane2);
        assertEquals(Side.PLUS, side2);
    }

}