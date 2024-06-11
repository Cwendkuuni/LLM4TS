import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IntervalsSetTest {

    @Test
    public void testEmptyConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNull(intervalsSet.getTree());
    }

    @Test
    public void testConstructorWithLowerAndUpper() {
        double lower = 0.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        BSPTree<Euclidean1D> tree = intervalsSet.getTree();
        OrientedPoint op = (OrientedPoint) tree.getCut();
        assertEquals(lower, op.getLocation().getX(), 0.0);
        op = (OrientedPoint) tree.getCut(1);
        assertEquals(upper, op.getLocation().getX(), 0.0);
    }

    @Test
    public void testConstructorWithTree() {
        double lower = 0.0;
        double upper = 5.0;
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(lower), false).wholeHyperplane(),
                new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(upper), true).wholeHyperplane(), null, null, null), null, null);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertEquals(tree, intervalsSet.getTree());
    }

    @Test
    public void testConstructorWithBoundary() {
        double lower = 0.0;
        double upper = 2.0;
        double lower2 = 3.0;
        double upper2 = 5.0;
        List<SubHyperplane<Euclidean1D>> boundary = Arrays.asList(
                new OrientedPoint(new Vector1D(lower), false).wholeHyperplane(),
                new OrientedPoint(new Vector1D(upper), true).wholeHyperplane(),
                new OrientedPoint(new Vector1D(lower2), false).wholeHyperplane());
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        BSPTree<Euclidean1D> tree = intervalsSet.getTree();
        OrientedPoint op = (OrientedPoint) tree.getCut();
        assertEquals(lower, op.getLocation().getX(), 0.0);
        op = (OrientedPoint) tree.getCut(1);
        assertEquals(upper, op.getLocation().getX(), 0.0);
        op = (OrientedPoint) tree.getCut(2);
        assertEquals(lower2, op.getLocation().getX(), 0.0);
    }

    @Test
    public void testBuildNew() {
        double lower = 0.0;
        double upper = 5.0;
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(lower), false).wholeHyperplane(),
                new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(upper), true).wholeHyperplane(), null, null, null), null, null);
        IntervalsSet intervalsSet1 = new IntervalsSet(tree);
        IntervalsSet intervalsSet2 = intervalsSet1.buildNew(tree);
        assertEquals(intervalsSet1.getTree(), intervalsSet2.getTree());
    }

    @Test
    public void testComputeGeometricalProperties() {
        double lower = 0.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        intervalsSet.computeGeometricalProperties();
        double inf = intervalsSet.getInf();
        double sup = intervalsSet.getSup();
        List<Interval> list = intervalsSet.asList();
        assertEquals(Double.NEGATIVE_INFINITY, inf, 0.0);
        assertEquals(Double.POSITIVE_INFINITY, sup, 0.0);
        assertEquals(2, list.size());
        Interval interval1 = list.get(0);
        Interval interval2 = list.get(1);
        assertEquals(lower, interval1.getInf(), 0.0);
        assertEquals(upper, interval2.getSup(), 0.0);
    }

    @Test
    public void testGetInf() {
        double lower = 0.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        double inf = intervalsSet.getInf();
        assertEquals(Double.NEGATIVE_INFINITY, inf, 0.0);
        IntervalsSet intervalsSetWithLowerBoundary = new IntervalsSet(Collections.singletonList(new OrientedLine(new Vector1D(0.0), true)));
        inf = intervalsSetWithLowerBoundary.getInf();
        assertEquals(Double.NEGATIVE_INFINITY - 0.001, inf, 0.001);
    }

    @Test
    public void testGetSup() {
        double lower = 0.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        double sup = intervalsSet.getSup();
        assertEquals(Double.POSITIVE_INFINITY, sup, 0.0);
        IntervalsSet intervalsSetWithUpperBoundary = new IntervalsSet(Collections.singletonList(new OrientedLine(new Vector1D(5.0), false)));
        sup = intervalsSetWithUpperBoundary.getSup();
        assertEquals(Double.POSITIVE_INFINITY - 0.001, sup, 0.001);
    }

    @Test
    public void testAsList_Empty() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> list = intervalsSet.asList();
        assertEquals(0, list.size());
    }

    @Test
    public void testAsList() {
        double lower = 0.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        List<Interval> list = intervalsSet.asList();
        assertEquals(2, list.size());
        Interval interval1 = list.get(0);
        Interval interval2 = list.get(1);
        assertEquals(lower, interval1.getInf(), 0.0);
        assertEquals(upper, interval2.getSup(), 0.0);
    }

    @Test
    public void testAsList_WithBoundaries() {
        double lower = 0.0;
        double upper = 5.0;
        double lower2 = 3.0;
        double upper2 = 6.0;
        IntervalsSet intervalsSet = new IntervalsSet(new OrientedLine(new Vector1D(lower), false), new OrientedLine(new Vector1D(upper), true),
                new OrientedLine(new Vector1D(lower2), false));
        List<Interval> list = intervalsSet.asList();
        assertEquals(2, list.size());
        Interval interval1 = list.get(0);
        Interval interval2 = list.get(1);
        assertEquals(lower, interval1.getInf(), 0.0);
        assertEquals(lower2, interval2.getInf(), 0.0);
        assertEquals(upper, interval1.getSup(), 0.0);
        assertEquals(upper2, interval2.getSup(), 0.0);
    }
}