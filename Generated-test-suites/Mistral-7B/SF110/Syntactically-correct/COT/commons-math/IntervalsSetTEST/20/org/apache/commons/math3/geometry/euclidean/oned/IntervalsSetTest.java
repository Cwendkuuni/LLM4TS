package org.apache.commons.math3.geometry.euclidean.oned;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import java.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 private IntervalsSet is;

 @Before
 public void setUp() {
 is = new IntervalsSet();
 }

 @Test
 public void testIntervalsSetEmptyConstructor() {
 assertEquals(0, is.size());
 assertNull(is.getBarycenter());
 }

 @Test
 public void testIntervalsSetConstructorWithBounds() {
 is = new IntervalsSet(0.0, 5.0);
 assertEquals(1, is.size());
 Interval interval = is.asList().get(0);
 assertEquals(0.0, interval.getInf(), 0.0);
 assertEquals(5.0, interval.getSup(), 0.0);
 }

 @Test
 public void testIntervalsSetConstructorWithTree() {
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane());
 tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane(), tree, new BSPTree<Euclidean1D>(Boolean.TRUE), null);
 is = new IntervalsSet(tree);
 assertEquals(1, is.size());
 Interval interval = is.asList().get(0);
 assertEquals(0.0, interval.getInf(), 0.0);
 assertEquals(5.0, interval.getSup(), 0.0);
 }

 @Test
 public void testIntervalsSetConstructorWithBoundary() {
 List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
 boundary.add(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane());
 boundary.add(new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane());
 is = new IntervalsSet(boundary);
 assertEquals(1, is.size());
 Interval interval = is.asList().get(0);
 assertEquals(0.0, interval.getInf(), 0.0);
 assertEquals(5.0, interval.getSup(), 0.0);
 }

 @Test
 public void testBuildTreeWithFiniteLowerAndUpper() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(0.0, 5.0);
 assertInstanceOf(BSPTree.class, tree);
 }

 @Test
 public void testBuildTreeWithInfLowerAndFiniteUpper() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, 5.0);
 assertInstanceOf(BSPTree.class, tree);
 }

 @Test
 public void testBuildTreeWithInfLowerAndInfUpper() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertInstanceOf(BSPTree.class, tree);
 }

 @Test
 public void testBuildTreeWithFiniteLowerAndInfUpper() {
 BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(0.0, Double.POSITIVE_INFINITY);
 assertInstanceOf(BSPTree.class, tree);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildTreeWithInvalidArguments() {
 IntervalsSet.buildTree(null, 5.0);
 }

 @Test
 public void testBuildNew() {
 IntervalsSet is1 = new IntervalsSet();
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane());
 tree = new BSPTree<Euclidean1D>(new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane(), tree, new BSPTree<Euclidean1D>(Boolean.TRUE), null);
 IntervalsSet is2 = is1.buildNew(tree);
 assertEquals(is, is2);
 }

 @Test
 public void testComputeGeometricalProperties() {
 is = new IntervalsSet(new ArrayList<SubHyperplane<Euclidean1D>>() {{
 add(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane());
 add(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane());
 add(new OrientedPoint(new Vector1D(4.0), false).wholeHyperplane());
 }});
 is.computeGeometricalProperties();
 assertEquals(3, is.size());
 assertNull(is.getBarycenter());

 is = new IntervalsSet(new ArrayList<SubHyperplane<Euclidean1D>>() {{
 add(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane());
 add(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane());
 add(new OrientedPoint(new Vector1D(4.0), false).wholeHyperplane());
 }});
 is.computeGeometricalProperties();
 Interval interval = is.asList().get(0);
 Vector1D barycenter = new Vector1D(1.5);
 assertEquals(3.0, is.size(), 0.0);
 assertEquals(barycenter.getX(), is.getBarycenter().getX(), 0.0);
 }

 @Test
 public void testGetInf() {
 is = new IntervalsSet(new ArrayList<SubHyperplane<Euclidean1D>>() {{
 add(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane());
 add(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane());
 add(new OrientedPoint(new Vector1D(4.0), false).wholeHyperplane());
 }});
 double inf = is.getInf();
 assertEquals(Double.NEGATIVE_INFINITY, inf, 0.0);

 is = new IntervalsSet(new ArrayList<SubHyperplane<Euclidean1D>>() {{
 add(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane());
 add(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane());
 add(new OrientedPoint(new Vector1D(4.0), false).wholeHyperplane());
 add(new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane());
 }});
 inf = is.getInf();
 assertEquals(-5.0, inf, 0.0);
 }

 @Test
 public void testGetSup() {
 is = new IntervalsSet(new ArrayList<SubHyperplane<Euclidean1D>>() {{
 add(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane());
 add(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane());
 add(new OrientedPoint(new Vector1D(4.0), false).wholeHyperplane());
 }});
 double sup = is.getSup();
 assertEquals(5.0, sup, 0.0);

 is = new IntervalsSet(new ArrayList<SubHyperplane<Euclidean1D>>() {{
 add(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane());
 add(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane());
 add(new OrientedPoint(new Vector1D(4.0), false).wholeHyperplane());
 add(new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane());
 add(new OrientedPoint(new Vector1D(6.0), true).wholeHyperplane());
 }});
 sup = is.getSup();
 assertEquals(6.0, sup, 0.0);
 }

 @Test
 public void testAsList() {
 is = new IntervalsSet(new ArrayList<SubHyperplane<Euclidean1D>>() {{
 add(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane());
 add(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane());
 add(new OrientedPoint(new Vector1D(4.0), false).wholeHyperplane());
 }});
 List<Interval> list = is.asList();
 assertEquals(3, list.size());
 Interval interval = list.get(0);
 assertEquals(1.0, interval.getInf(), 0.0);
 assertEquals(2.0, interval.getSup(), 0.0);
 interval = list.get(1);
 assertEquals(4.0, interval.getInf(), 0.0);
 assertEquals(Double.POSITIVE_INFINITY, interval.getSup(), 0.0);
 interval = list.get(2);
 assertEquals(Double.NEGATIVE_INFINITY, interval.getInf(), 0.0);
 assertEquals(4.0, interval.getSup(), 0.0);
 }

}