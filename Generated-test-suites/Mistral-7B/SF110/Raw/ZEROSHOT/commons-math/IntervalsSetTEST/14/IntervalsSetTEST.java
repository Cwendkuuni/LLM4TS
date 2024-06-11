import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(Collections.emptyList(), intervalsSet.asList());
 }

 @Test
 public void testConstructorWithBounds() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
 List<Interval> expectedIntervals = Arrays.asList(new Interval(0.0, 5.0));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(expectedIntervals, actualIntervals);
 }

 @Test
 public void testConstructorWithTree() {
 IntervalsSet intervalsSet = new IntervalsSet(buildTree(0.0, 5.0));
 List<Interval> expectedIntervals = Arrays.asList(new Interval(0.0, 5.0));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(expectedIntervals, actualIntervals);
 }

 @Test
 public void testConstructorWithBoundary() {
 List<SubHyperplane<Euclidean1D>> boundary = Arrays.asList(new OrientedHyperplane(new Vector1D(3.0), true), new OrientedHyperplane(new Vector1D(1.0), false));
 IntervalsSet intervalsSet = new IntervalsSet(boundary);
 List<Interval> expectedIntervals = Arrays.asList(new Interval(1.0, 3.0));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(expectedIntervals, actualIntervals);
 }

 @Test
 public void testBuildTreeWithLowerThanZeroAndUpperThanZero() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 IntervalsSet intervalsSet = new IntervalsSet(tree);
 List<Interval> expectedIntervals = Arrays.asList(new Interval(Double.NEGATIVE_INFINITY, 0.0), new Interval(0.0, Double.POSITIVE_INFINITY));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(expectedIntervals, actualIntervals);
 }

 @Test
 public void testBuildTreeWithLowerThanZeroAndUpperEqualsZero() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, 0.0);
 IntervalsSet intervalsSet = new IntervalsSet(tree);
 List<Interval> expectedIntervals = Arrays.asList(new Interval(Double.NEGATIVE_INFINITY, 0.0));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(expectedIntervals, actualIntervals);
 }

 @Test
 public void testBuildTreeWithLowerEqualsZeroAndUpperThanZero() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(0.0, Double.POSITIVE_INFINITY);
 IntervalsSet intervalsSet = new IntervalsSet(tree);
 List<Interval> expectedIntervals = Arrays.asList(new Interval(0.0, Double.POSITIVE_INFINITY));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(expectedIntervals, actualIntervals);
 }

 @Test
 public void testBuildTreeWithBothBoundsInfinite() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
 IntervalsSet intervalsSet = new IntervalsSet(tree);
 List<Interval> expectedIntervals = Collections.emptyList();
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(expectedIntervals, actualIntervals);
 }

 @Test
 public void testBuildNew() {
 IntervalsSet originalIntervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0), new Interval(4.0, 5.0)));
 IntervalsSet newIntervalsSet = originalIntervalsSet.buildNew(originalIntervalsSet.getTree());
 List<Interval> expectedIntervals = originalIntervalsSet.asList();
 List<Interval> actualIntervals = newIntervalsSet.asList();
 assertEquals(expectedIntervals, actualIntervals);
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0), new Interval(4.0, 5.0)));
 intervalsSet.computeGeometricalProperties();
 double expectedBarycenter = 2.666666666666667;
 double actualBarycenter = intervalsSet.getBarycenter().getX();
 assertEquals(expectedBarycenter, actualBarycenter, 1e-10);
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0), new Interval(4.0, 5.0)));
 double expectedInf = -Double.MAX_VALUE;
 double actualInf = intervalsSet.getInf();
 assertEquals(expectedInf, actualInf, 1e-10);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0), new Interval(4.0, 5.0)));
 double expectedSup = Double.MAX_VALUE;
 double actualSup = intervalsSet.getSup();
 assertEquals(expectedSup, actualSup, 1e-10);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0), new Interval(4.0, 5.0)));
 List<Interval> expectedIntervals = Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0), new Interval(4.0, 5.0));
 List<Interval> actualIntervals = intervalsSet.asList();
 assertEquals(expectedIntervals, actualIntervals);
 }

 @Test
 public void testRecursiveList() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(2.0, 3.0), new Interval(4.0, 5.0)));
 List<Interval> expectedIntervals = Arrays.asList(new Interval(0.0, 1.0), new Interval(1.0, 2.0), new Interval(2.0, 3.0), new Interval(3.0, 4.0), new Interval(4.0, 5.0));
 List<Interval> actualIntervals = new ArrayList<>();
 intervalsSet.recurseList(((AbstractRegion<Euclidean1D, T>)intervalsSet).getTree(false), actualIntervals, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(expectedIntervals, actualIntervals);
 }
}