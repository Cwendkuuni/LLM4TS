import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import static org.junit.Assert.*;
import java.util.*;

public class IntervalsSetTest {

 private IntervalsSet createIntervalsSet(double lower, double upper) {
 return new IntervalsSet(new BSPTree<Euclidean1D>(IntervalsSet.buildTree(lower, upper)));
 }

 @Test
 public void testIntervalsSetEmptyConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testIntervalsSetConstructorWithLowerAndUpper() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
 List<Interval> expectedIntervals = Arrays.asList(new Interval(0.0, 1.0));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(expectedIntervals, actualIntervals);
 }

 @Test
 public void testIntervalsSetConstructorWithTree() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(), new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(), null, null)));
 List<Interval> expectedIntervals = Arrays.asList(new Interval(0.0, 1.0));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(expectedIntervals, actualIntervals);
 }

 @Test
 public void testIntervalsSetConstructorWithBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0)));
 List<Interval> expectedIntervals = Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(expectedIntervals, actualIntervals);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetInvalidConstructorWithBoundaryNull() {
 new IntervalsSet(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetInvalidConstructorWithBoundaryEmpty() {
 new IntervalsSet(Collections.emptyList());
 }

 @Test
 public void testIntervalsSetBuildNew() {
 IntervalsSet intervalsSet = new IntervalsSet();
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(intervalsSet.getTree());
 assertEquals(intervalsSet, newIntervalsSet);
 }

 @Test
 public void testIntervalsSetGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
 double inf = intervalsSet.getInf();
 assertEquals(Double.NEGATIVE_INFINITY, inf, 0.0);

 intervalsSet = new IntervalsSet();
 inf = intervalsSet.getInf();
 assertEquals(Double.NEGATIVE_INFINITY, inf, 0.0);

 intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(), null, null, null));
 inf = intervalsSet.getInf();
 assertEquals(Double.NEGATIVE_INFINITY, inf, 0.0);

 intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0)));
 inf = intervalsSet.getInf();
 assertEquals(0.0, inf, 0.0);
}

 @Test
 public void testIntervalsSetGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
 double sup = intervalsSet.getSup();
 assertEquals(Double.POSITIVE_INFINITY, sup, 0.0);

 intervalsSet = new IntervalsSet();
 sup = intervalsSet.getSup();
 assertEquals(Double.POSITIVE_INFINITY, sup, 0.0);

 intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(null, null, null, new OrientedPoint(new Vector1D(0.0), true).wholeHyperplane()));
 sup = intervalsSet.getSup();
 assertEquals(Double.POSITIVE_INFINITY, sup, 0.0);

 intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0)));
 sup = intervalsSet.getSup();
 assertEquals(3.0, sup, 0.0);
}

 @Test
 public void testIntervalsSetAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
 List<Interval> expectedIntervals = Arrays.asList(new Interval(0.0, 1.0));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(expectedIntervals, actualIntervals);

 intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(), null, null, null));
 expectedIntervals = Arrays.asList(new Interval(Double.NEGATIVE_INFINITY, 0.0));
 actualIntervals = intervalsSet.asList();
 assertEquals(expectedIntervals, actualIntervals);

 intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0)));
 expectedIntervals = Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
 actualIntervals = intervalsSet.asList();
 assertEquals(expectedIntervals, actualIntervals);
}

 @Test
 public void testIntervalsSetRecurseList() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(), null, null, null));
 List<Interval> expectedIntervals = Arrays.asList(new Interval(Double.NEGATIVE_INFINITY, 0.0));
 List<Interval> actualIntervals = new ArrayList<>();
 intervalsSet.recurseList(((AbstractRegion<Euclidean1D, T>)intervalsSet).getTree(false), actualIntervals, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(expectedIntervals, actualIntervals);

 intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0)));
 expectedIntervals = Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
 actualIntervals = new ArrayList<>();
 intervalsSet.recurseList(((AbstractRegion<Euclidean1D, T>)intervalsSet).getTree(false), actualIntervals, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(expectedIntervals, actualIntervals);
}

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetRecurseListInvalidBounds() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(), null, null, null));
 List<Interval> actualIntervals = new ArrayList<>();
 intervalsSet.recurseList(((AbstractRegion<Euclidean1D, T>)intervalsSet).getTree(false), actualIntervals, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
}

 @Test
 public void testIntervalsSetComputeGeometricalProperties() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane(), null, null, null));
 intervalsSet.computeGeometricalProperties();
 double barycenter = intervalsSet.getBarycenter().getX();
 assertEquals(Double.NaN, barycenter, 0.0);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize());

 intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0)));
 intervalsSet.computeGeometricalProperties();
 double barycenter2 = intervalsSet.getBarycenter().getX();
 double size = intervalsSet.getSize();
 assertEquals(1.5, barycenter2, 0.0);
 assertEquals(3.0, size, 0.0);
}
}