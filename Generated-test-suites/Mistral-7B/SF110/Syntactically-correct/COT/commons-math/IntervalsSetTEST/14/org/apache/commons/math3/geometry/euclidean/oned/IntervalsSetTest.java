import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 private IntervalsSet createIntervalsSet(double lower, double upper) {
 return new IntervalsSet(new BSPTree<Euclidean1D>(IntervalsSet.buildTree(lower, upper)));
 }

 @Test
 public void testConstruction() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertTrue(intervalsSet.isEmpty());

 intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(Boolean.TRUE));
 assertTrue(intervalsSet.isEmpty());

 intervalsSet = new IntervalsSet(Collections.emptyList());
 assertTrue(intervalsSet.isEmpty());

 intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(),
 new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE), null));
 intervalsSet.computeGeometricalProperties();
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);

 intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane(),
 new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE), null));
 intervalsSet.computeGeometricalProperties();
 assertEquals(0.0, intervalsSet.getSize(), 0.0);
 }

 @Test
 public void testBuildTree() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);

 assertEquals(4, tree.getNumberOfChildren());

 OrientedPoint lowerCut = (OrientedPoint)tree.getChild(0).getCut().getHyperplane();
 assertEquals(new Vector1D(-1.0), lowerCut.getLocation());

 OrientedPoint upperCut = (OrientedPoint)tree.getChild(2).getCut().getHyperplane();
 assertEquals(new Vector1D(1.0), upperCut.getLocation());
 }

 @Test
 public void testBuildNew() {
 IntervalsSet intervalsSet = new IntervalsSet();
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(),
 new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE), null);
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
 assertTrue(newIntervalsSet.getTree(false).equals(tree));
 }

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 double barycenter = intervalsSet.getBarycenter().getX();
 assertEquals(0.0, barycenter, 0.001);

 intervalsSet = createIntervalsSet(-2.0, 2.0);
 barycenter = intervalsSet.getBarycenter().getX();
 assertEquals(0.0, barycenter, 0.001);

 intervalsSet = createIntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 barycenter = intervalsSet.getBarycenter().getX();
 assertEquals(Double.NaN, barycenter);
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 double inf = intervalsSet.getInf();
 assertEquals(-1.0, inf, 0.001);

 intervalsSet = createIntervalsSet(-2.0, 2.0);
 inf = intervalsSet.getInf();
 assertEquals(Double.NEGATIVE_INFINITY, inf);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 double sup = intervalsSet.getSup();
 assertEquals(1.0, sup, 0.001);

 intervalsSet = createIntervalsSet(-2.0, 2.0);
 sup = intervalsSet.getSup();
 assertEquals(Double.POSITIVE_INFINITY, sup);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 List<Interval> list = intervalsSet.asList();
 assertEquals(1, list.size());
 Interval interval = list.get(0);
 assertEquals(new Interval(-1.0, 1.0), interval);

 intervalsSet = createIntervalsSet(-2.0, 2.0);
 list = intervalsSet.asList();
 assertEquals(0, list.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEdgeCaseBuildTreeWithInvalidArguments() {
 IntervalsSet.buildTree(Double.NaN, Double.POSITIVE_INFINITY);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEdgeCaseBuildTreeWithNegativeLowerAndPositiveUpper() {
 IntervalsSet.buildTree(-1.0, 1.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEdgeCaseBuildTreeWithNegativeUpperAndPositiveLower() {
 IntervalsSet.buildTree(1.0, -1.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEdgeCaseAsListWithNegativeLowerAndPositiveUpperOutsideRegion() {
 IntervalsSet intervalsSet = createIntervalsSet(-2.0, 2.0);
 List<Interval> list = new ArrayList<>();
 this.recurseList(intervalsSet.getTree(false), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(0, list.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEdgeCaseAsListWithNegativeLowerAndPositiveUpperInsideRegion() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 List<Interval> list = new ArrayList<>();
 this.recurseList(intervalsSet.getTree(false), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(1, list.size());
 Interval interval = list.get(0);
 assertTrue(interval.getInf() < 0.0);
 assertTrue(interval.getSup() > 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEdgeCaseAsListWithNegativeLowerInsideRegion() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 List<Interval> list = new ArrayList<>();
 this.recurseList(intervalsSet.getTree(false), list, Double.NEGATIVE_INFINITY, 0.0);
 assertEquals(1, list.size());
 Interval interval = list.get(0);
 assertEquals(Double.NEGATIVE_INFINITY, interval.getInf());
 assertEquals(0.0, interval.getSup(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEdgeCaseAsListWithPositiveUpperInsideRegion() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 List<Interval> list = new ArrayList<>();
 this.recurseList(intervalsSet.getTree(false), list, 1.0, Double.POSITIVE_INFINITY);
 assertEquals(1, list.size());
 Interval interval = list.get(0);
 assertEquals(0.0, interval.getInf(), 0.001);
 assertEquals(Double.POSITIVE_INFINITY, interval.getSup());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEdgeCaseAsListWithNegativeLowerInsideRegionAndNegativeUpperOutsideRegion() {
 IntervalsSet intervalsSet = createIntervalsSet(-2.0, -1.0);
 List<Interval> list = new ArrayList<>();
 this.recurseList(intervalsSet.getTree(false), list, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
 assertEquals(0, list.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEdgeCaseAsListWithNegativeLowerInsideRegionAndNegativeUpperInsideRegion() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 0.0);
 List<Interval> list = new ArrayList<>();
 this.recurseList(intervalsSet.getTree(false), list, Double.NEGATIVE_INFINITY, 0.0);
 assertEquals(1, list.size());
 Interval interval = list.get(0);
 assertEquals(Double.NEGATIVE_INFINITY, interval.getInf());
 assertEquals(0.0, interval.getSup(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEdgeCaseAsListWithNegativeLowerInsideRegionAndPositiveUpperInsideRegion() {
 IntervalsSet intervalsSet = createIntervalsSet(-1.0, 1.0);
 List<Interval> list = new ArrayList<>();
 this.recurseList(intervalsSet.getTree(false), list, Double.NEGATIVE_INFINITY, 1.0);
 assertEquals(2, list.size());
 Interval interval1 = list.get(0);
 Interval interval2 = list.get(1);
 assertEquals(Double.NEGATIVE_INFINITY, interval1.getInf());
 assertEquals(0.0, interval1.getSup(), 0.001);
 assertEquals(1.0, interval2.getInf(), 0.001);
 assertEquals(Double.POSITIVE_INFINITY, interval2.getSup());
 }

 private void recurseList(BSPTree<Euclidean1D> node, List<Interval> list, double lower, double upper) {
 if (node.getCut() == null) {
 if (node.getAttribute()) {
 list.add(new Interval(lower, upper));
 }
 }
 else {
 OrientedPoint op = (OrientedPoint)node.getCut().getHyperplane();
 double x = op.getLocation().getX();
 BSPTree<Euclidean1D> low = op.isDirect() ? node.getMinus() : node.getPlus();
 BSPTree<Euclidean1D> high = op.isDirect() ? node.getPlus() : node.getMinus();
 this.recurseList(low, list, lower, x);
 if (((AbstractRegion<Euclidean1D, T>)this).checkPoint(low, new Vector1D(x)) == Region.Location.INSIDE && ((AbstractRegion<Euclidean1D, T>)this).checkPoint(high, new Vector1D(x)) == Region.Location.INSIDE) {
 x = list.remove(list.size() - 1).getInf();
 }
 this.recurseList(high, list, x, upper);
 }
 }
}