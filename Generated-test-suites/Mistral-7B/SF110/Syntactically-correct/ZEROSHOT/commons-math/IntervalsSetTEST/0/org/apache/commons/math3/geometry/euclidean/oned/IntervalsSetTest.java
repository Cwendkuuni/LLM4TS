import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 private IntervalsSet intervalsSet;
 private final double EPSILON = 1e-9;

 @Before
 public void setUp() {
 IntervalsSet intervalsSet = new IntervalsSet();
 this.intervalsSet = intervalsSet;
 }

 @Test
 public void testIntervalsSetEmptyConstructor() {
 assertTrue(intervalsSet.isEmpty());
 assertEquals(0, intervalsSet.size(), EPSILON);
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testIntervalsSetWithBoundsConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
 assertFalse(intervalsSet.isEmpty());
 assertEquals(5.0, intervalsSet.size(), EPSILON);
 assertArrayEquals(new double[] { 0.0, 5.0 }, intervalsSet.getBarycenter().toArray(), EPSILON);
 }

 @Test
 public void testIntervalsSetWithTreeConstructor() {
 IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane()));
 assertFalse(intervalsSet.isEmpty());
 assertEquals(5.0, intervalsSet.size(), EPSILON);
 assertArrayEquals(new double[] { 0.0, 5.0 }, intervalsSet.getBarycenter().toArray(), EPSILON);
 }

 @Test
 public void testIntervalsSetWithBoundaryConstructor() {
 List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
 boundary.add(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane());
 boundary.add(new OrientedPoint(new Vector1D(5.0), false).wholeHyperplane());
 IntervalsSet intervalsSet = new IntervalsSet(boundary);
 assertFalse(intervalsSet.isEmpty());
 assertEquals(5.0, intervalsSet.size(), EPSILON);
 assertArrayEquals(new double[] { 0.0, 5.0 }, intervalsSet.getBarycenter().toArray(), EPSILON);
 }

 @Test
 public void testBuildTree() {
 double lower = -1.0;
 double upper = 6.0;
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
 assertFalse(tree.isLeaf());

 if (Double.isInfinite(lower) && lower < 0.0) {
 if (Double.isInfinite(upper) && upper > 0.0) {
 BSPTree<Euclidean1D> expectedTree = new BSPTree<Euclidean1D>(Boolean.TRUE);
 assertEquals(expectedTree, tree);
 }
 else {
 BSPTree<Euclidean1D> expectedTree1 = new OrientedPoint(new Vector1D(lower), true).wholeHyperplane();
 BSPTree<Euclidean1D> expectedTree2 = new BSPTree<Euclidean1D>(Boolean.FALSE, new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE, null));
 assertEquals(expectedTree1, tree.getLeftChild());
 assertEquals(expectedTree2, tree.getRightChild());
 }
 }
 else {
 BSPTree<Euclidean1D> expectedTree1 = new OrientedPoint(new Vector1D(lower), false).wholeHyperplane();
 BSPTree<Euclidean1D> expectedTree2 = new BSPTree<Euclidean1D>(Boolean.FALSE);
 BSPTree<Euclidean1D> expectedTree3 = new BSPTree<Euclidean1D>(Boolean.TRUE);
 assertEquals(expectedTree1, tree.getLeftChild());
 assertEquals(expectedTree2, tree.getMiddleChild());
 assertEquals(expectedTree3, tree.getRightChild());
 }
}

 @Test
 public void testBuildNew() {
 IntervalsSet intervalsSet = new IntervalsSet();
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint( new Vector1D(0.0), false ).wholeHyperplane());
 IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
 assertFalse(newIntervalsSet.isEmpty());
 assertEquals(5.0, newIntervalsSet.size(), EPSILON);
 assertArrayEquals(new double[] { 0.0, 5.0 }, newIntervalsSet.getBarycenter().toArray(), EPSILON);
}

 @Test
 public void testComputeGeometricalProperties() {
 IntervalsSet intervalsSet = new IntervalsSet(new IntervalsSet(0.0, 1.0).union(new IntervalsSet(2.0, 4.0).union(new IntervalsSet(5.0, 6.0))));
 intervalsSet.computeGeometricalProperties();
 assertEquals(11.0, intervalsSet.size(), EPSILON);
 assertArrayEquals(new double[] { 0.0, 1.0, 2.0, 4.0, 5.0, 6.0 }, intervalsSet.getBarycenter().toArray(), EPSILON);
}

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), EPSILON);

 intervalsSet = new IntervalsSet(new IntervalsSet(0.0, 1.0).union(new IntervalsSet(2.0, 4.0).union(new IntervalsSet(5.0, 6.0))));
 assertEquals(0.0, intervalsSet.getInf(), EPSILON);
}

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), EPSILON);

 intervalsSet = new IntervalsSet(new IntervalsSet(0.0, 1.0).union(new IntervalsSet(2.0, 4.0).union(new IntervalsSet(5.0, 6.0))));
 assertEquals(6.0, intervalsSet.getSup(), EPSILON);
}

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(new IntervalsSet(0.0, 1.0).union(new IntervalsSet(2.0, 4.0).union(new IntervalsSet(5.0, 6.0))));
 List<Interval> list = intervalsSet.asList();
 assertEquals(5, list.size());
 for (int i = 0; i < list.size(); i++) {
 Interval expected = new Interval(i * 1.0, (i + 1) * 1.0);
 Interval actual = list.get(i);
 assertEquals(expected, actual);
 }
}
}