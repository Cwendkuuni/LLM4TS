import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import java.util.*;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @Test
 public void testEmptyConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testConstructorWithLowerAndUpper() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
 assertFalse(intervalsSet.isEmpty());
 assertEquals(1, intervalsSet.asList().size());
 Interval interval = intervalsSet.asList().get(0);
 assertEquals(0.0, interval.getInf(), 0.0);
 assertEquals(5.0, interval.getSup(), 0.0);
 }

 @Test
 public void testConstructorWithTree() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane()));
 assertFalse(intervalsSet.isEmpty());
 assertEquals(1, intervalsSet.asList().size());
 Interval interval = intervalsSet.asList().get(0);
 assertEquals(Double.NEGATIVE_INFINITY, interval.getInf(), 0.0);
 assertEquals(5.0, interval.getSup(), 0.0);
 }

 @Test
 public void testConstructorWithBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(3.0, 4.0)));
 assertFalse(intervalsSet.isEmpty());
 assertEquals(2, intervalsSet.asList().size());
 }

 @Test
 public void testBuildNew() {
 IntervalsSet intervalsSet = new IntervalsSet();
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane());
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
 assertFalse(newIntervalsSet.isEmpty());
 assertEquals(1, newIntervalsSet.asList().size());
 Interval interval = newIntervalsSet.asList().get(0);
 assertEquals(Double.NEGATIVE_INFINITY, interval.getInf(), 0.0);
 assertEquals(5.0, interval.getSup(), 0.0);
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(3.0, 4.0)));
 intervalsSet.computeGeometricalProperties();
 assertEquals(2.5, intervalsSet.getBarycenter().getX(), 0.01);
 assertEquals(2.0, intervalsSet.getSize(), 0.01);
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(3.0, 4.0)));
 assertEquals(-Infinity.DOUBLE, intervalsSet.getInf(), 0.0);

 intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane()));
 assertEquals(5.0, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(3.0, 4.0)));
 assertEquals(Infinity.DOUBLE, intervalsSet.getSup(), 0.0);

 intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(-5.0), false).wholeHyperplane()));
 assertEquals(-5.0, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(new Interval(0.0, 1.0), new Interval(3.0, 4.0)));
 List<Interval> list = intervalsSet.asList();
 assertEquals(2, list.size());
 }
}