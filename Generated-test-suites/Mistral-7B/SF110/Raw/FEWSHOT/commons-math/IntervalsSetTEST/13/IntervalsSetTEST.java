package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.*;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import java.util.*;
import org.apache.commons.math3.geometry.partitioning.*;

public class IntervalsSetTest {

    @Test
    public void testIntervalsSetEmptyConstruction() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(0, intervalsSet.size());
        assertNull(intervalsSet.getBarycenter());
    }

    @Test
    public void testIntervalsSetRangeConstruction() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
        List<Interval> expectedIntervals = new ArrayList<>();
        expectedIntervals.add(new Interval(0.0, 5.0));
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(1, intervals.size());
        assertEquals(expectedIntervals, intervals);
        assertEquals(5.0, intervalsSet.getBarycenter().getX());
        assertEquals(5.0 * 5.0 / 2.0, intervalsSet.getBarycenter().getX(), 1e-8);
        assertEquals(5.0, intervalsSet.getSize());
    }

    @Test
    public void testIntervalsSetTreeConstruction() {
        IntervalsSet intervalsSet = new IntervalsSet(buildTree(0.0, 5.0));
        List<Interval> expectedIntervals = new ArrayList<>();
        expectedIntervals.add(new Interval(0.0, 5.0));
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(1, intervals.size());
        assertEquals(expectedIntervals, intervals);
        assertEquals(5.0, intervalsSet.getBarycenter().getX());
        assertEquals(5.0 * 5.0 / 2.0, intervalsSet.getBarycenter().getX(), 1e-8);
        assertEquals(5.0, intervalsSet.getSize());
    }

    @Test
    public void testIntervalsSetCollectionConstruction() {
        List<Interval> intervals = new ArrayList<>();
        intervals.add(new Interval(1.0, 2.0));
        intervals.add(new Interval(3.0, 4.0));
        IntervalsSet intervalsSet = new IntervalsSet(intervals.stream().toCollection(ArrayList::new));
        List<Interval> expectedIntervals = new ArrayList<>(intervals);
        List<Interval> intervalsList = intervalsSet.asList();
        assertEquals(2, intervalsList.size());
        assertEquals(expectedIntervals, intervalsList);
        assertEquals(3.0, intervalsSet.getBarycenter().getX(), 1e-8);
        assertEquals(6.0, intervalsSet.getSize());
    }

    @Test
    public void testBuildTree() {
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertTrue(tree.getCut() == null);
        assertFalse(tree.getMinus() == null);
        assertFalse(tree.getPlus() == null);

        BSPTree<Euclidean1D> tree1 = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, 0.0);
        OrientedPoint orientedPoint = new OrientedPoint(new Vector1D(0.0), true);
        SubHyperplane<Euclidean1D> subHyperplane = orientedPoint.wholeHyperplane();
        BSPTree<Euclidean1D> tree1Left = new BSPTree<Euclidean1D>(Boolean.FALSE);
        BSPTree<Euclidean1D> tree1Right = new BSPTree<Euclidean1D>(Boolean.TRUE, tree1Left, null, null);
        assertEquals(tree1, IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, 0.0));
        assertEquals(subHyperplane, tree1.getCut().getHyperplane());

        BSPTree<Euclidean1D> tree2 = IntervalsSet.buildTree(0.0, Double.POSITIVE_INFINITY);
        OrientedPoint orientedPoint2 = new OrientedPoint(new Vector1D(0.0), false);
        SubHyperplane<Euclidean1D> subHyperplane2 = orientedPoint2.wholeHyperplane();
        BSPTree<Euclidean1D> tree2Left = new BSPTree<Euclidean1D>(Boolean.TRUE);
        BSPTree<Euclidean1D> tree2Right = new BSPTree<Euclidean1D>(Boolean.FALSE, tree2Left, null, null);
        assertEquals(tree2, IntervalsSet.buildTree(0.0, Double.POSITIVE_INFINITY));
        assertEquals(subHyperplane2, tree2.getCut().getHyperplane());

        BSPTree<Euclidean1D> tree3 = IntervalsSet.buildTree(0.0, 5.0);
        OrientedPoint orientedPoint3 = new OrientedPoint(new Vector1D(5.0), true);
        SubHyperplane<Euclidean1D> subHyperplane3 = orientedPoint3.wholeHyperplane();
        BSPTree<Euclidean1D> tree3Left = new BSPTree<Euclidean1D>(Boolean.FALSE, null, null, null);
        BSPTree<Euclidean1D> tree3Right = new BSPTree<Euclidean1D>(Boolean.TRUE, null, null, null);
        assertEquals(tree3, IntervalsSet.buildTree(0.0, 5.0));
        assertEquals(subHyperplane3, tree3.getPlus().getCut().getHyperplane());

        BSPTree<Euclidean1D> tree4 = IntervalsSet.buildTree(1.0, 5.0);
        OrientedPoint orientedPoint4 = new OrientedPoint(new Vector1D(1.0), true);
        SubHyperplane<Euclidean1D> subHyperplane4 = orientedPoint4.wholeHyperplane();
        BSPTree<Euclidean1D> tree4Left = new BSPTree<Euclidean1D>(Boolean.FALSE, null, null, null);
        BSPTree<Euclidean1D> tree4Middle = new BSPTree<Euclidean1D>(subHyperplane4, null, null, null);
        BSPTree<Euclidean1D> tree4Right = new BSPTree<Euclidean1D>(Boolean.TRUE, tree4Middle, null, null);
        assertEquals(tree4, IntervalsSet.buildTree(1.0, 5.0));
        assertEquals(subHyperplane4, tree4.getMinus().getCut().getHyperplane());
    }

    @Test
    public void testBuildNew() {
        double lower = 1.0;
        double upper = 5.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        IntervalsSet intervalsSet = new IntervalsSet();
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        List<Interval> intervals = newIntervalsSet.asList();
        List<Interval> expectedIntervals = new ArrayList<>();
        expectedIntervals.add(new Interval(lower, upper));
        assertEquals(1, intervals.size());
        assertEquals(expectedIntervals, intervals);
        assertEquals(lower + (upper - lower) / 2.0, newIntervalsSet.getBarycenter().getX(), 1e-8);
        assertEquals(upper - lower, newIntervalsSet.getSize());
    }

    @Test
    public void testComputeGeometricalProperties() {
        double lower = 1.0;
        double upper = 5.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        intervalsSet.computeGeometricalProperties();
        assertEquals(5.0, intervalsSet.getSize());
        double barycenterX = intervalsSet.getBarycenter().getX();
        double expectedBarycenterX = (lower + upper) / 2.0;
        assertEquals(expectedBarycenterX, barycenterX, 1e-8);
    }

    @Test
    public void testGetInf() {
        double lower = 1.0;
        double upper = 5.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        double inf = intervalsSet.getInf();
        assertEquals(lower, inf, 1e-8);
    }

    @Test
    public void testGetSup() {
        double lower = 1.0;
        double upper = 5.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        double sup = intervalsSet.getSup();
        assertEquals(upper, sup, 1e-8);
    }

    @Test
    public void testAsList() {
        double lower = 1.0;
        double upper = 5.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        List<Interval> intervals = intervalsSet.asList();
        List<Interval> expectedIntervals = new ArrayList<>();
        expectedIntervals.add(new Interval(lower, upper));
        assertEquals(1, intervals.size());
        assertEquals(expectedIntervals, intervals);
    }

    @Test
    public void testRecurseList() {
        double lower = 1.0;
        double upper = 5.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        List<Interval> intervals = new ArrayList<>();
        intervals.add(new Interval(2.0, 3.0));
        IntervalsSet subIntervalsSet = new IntervalsSet(tree.getMinus());
        subIntervalsSet.recurseList(tree.getMinus(), intervals, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(1, intervals.size());
        assertEquals(new Interval(2.0, 3.0), intervals.get(0));

        List<Interval> mergedIntervals = new ArrayList<>();
        mergedIntervals.addAll(intervals);
        mergedIntervals.add(new Interval(lower, 2.0));
        mergedIntervals.add(new Interval(3.0, upper));
        assertEquals(mergedIntervals, intervalsSet.asList());
    }
}