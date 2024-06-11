package org.apache.commons.math3.geometry.euclidean.oned;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.*;

public class IntervalsSetTest {

 private IntervalsSet intervalsSet;

 @Before
 public void setUp() {
 intervalsSet = new IntervalsSet();
 }

 @Test
 public void testIntervalsSetConstructor() {
 IntervalsSet intervalsSet1 = new IntervalsSet();
 assertNotNull(intervalsSet1.getTree());

 IntervalsSet intervalsSet2 = new IntervalsSet(0.0, 5.0);
 assertNotNull(intervalsSet2.getTree());
 assertEquals(5.0, intervalsSet2.getSup(), 0.01);
 }

 @Test
 public void testIntervalsSetBuildNew() {
 BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
 IntervalsSet intervalsSet1 = new IntervalsSet();
 IntervalsSet intervalsSet2 = intervalsSet1.buildNew(tree);
 assertSame(tree, intervalsSet2.getTree());
 }

 @Test
 public void testIntervalsSetBuildTree() {
 BSPTree<Euclidean1D> expectedTree = new BSPTree<Euclidean1D>(Boolean.TRUE);
 IntervalsSet intervalsSet = new IntervalsSet(-2.0, 6.0);
 BSPTree<Euclidean1D> actualTree = intervalsSet.getTree();
 assertEquals(expectedTree, actualTree);
 }

 @Test
 public void testIntervalsSetAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(new ArrayList<Interval>() {{
 add(new Interval(1.0, 3.0));
 add(new Interval(5.0, 7.0));
 }});
 List<Interval> expectedList = new ArrayList<Interval>() {{
 add(new Interval(1.0, 3.0));
 add(new Interval(5.0, 7.0));
 }};
 List<Interval> actualList = intervalsSet.asList();
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testIntervalsSetGetInf() {
 IntervalsSet intervalsSet1 = new IntervalsSet();
 double inf1 = intervalsSet1.getInf();
 assertEquals(Double.NEGATIVE_INFINITY, inf1, 0.01);

 IntervalsSet intervalsSet2 = new IntervalsSet(1.0, 5.0);
 double inf2 = intervalsSet2.getInf();
 assertEquals(1.0, inf2, 0.01);
 }

 @Test
 public void testIntervalsSetGetSup() {
 IntervalsSet intervalsSet1 = new IntervalsSet();
 double sup1 = intervalsSet1.getSup();
 assertEquals(Double.POSITIVE_INFINITY, sup1, 0.01);

 IntervalsSet intervalsSet2 = new IntervalsSet(1.0, 5.0);
 double sup2 = intervalsSet2.getSup();
 assertEquals(5.0, sup2, 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetBuildTreeWithInvalidArguments() {
 try {
 IntervalsSet.buildTree(Double.NaN, Double.NaN);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testIntervalsSetCheckPoint() {
 IntervalsSet intervalsSet = new IntervalsSet(new ArrayList<Interval>() {{
 add(new Interval(1.0, 3.0));
 add(new Interval(5.0, 7.0));
 add(new Interval(9.0, 11.0));
 }});
 Region location = intervalsSet.checkPoint(intervalsSet.getTree(), new Vector1D(2.0));
 assertEquals(Region.Location.OUTSIDE, location);

 location = intervalsSet.checkPoint(intervalsSet.getTree(), new Vector1D(4.0));
 assertEquals(Region.Location.INSIDE, location);
}

 @Test
 public void testIntervalsSetComputeGeometricalProperties() {
 IntervalsSet intervalsSet = new IntervalsSet(new ArrayList<Interval>() {{
 add(new Interval(1.0, 3.0));
 add(new Interval(5.0, 7.0));
 }});
 intervalsSet.computeGeometricalProperties();

 double barycenter = intervalsSet.getBarycenter().getX();
 assertEquals(3.5, barycenter, 0.01);

 double size = intervalsSet.getSize();
 assertEquals(6.0, size, 0.01);
}

 @Test
 public void testIntervalsSetEdgeCases() {
 IntervalsSet intervalsSet1 = new IntervalsSet(new ArrayList<Interval>() {{
 add(new Interval(Double.NEGATIVE_INFINITY, 0.0));
 add(new Interval(1.0, 3.0));
 add(new Interval(5.0, Double.POSITIVE_INFINITY));
 }});
 List<Interval> list1 = intervalsSet1.asList();
 List<Interval> expectedList1 = new ArrayList<Interval>() {{
 add(new Interval(Double.NEGATIVE_INFINITY, 0.0));
 add(new Interval(1.0, 3.0));
 add(new Interval(5.0, Double.POSITIVE_INFINITY));
 }};
 assertEquals(expectedList1, list1);

 IntervalsSet intervalsSet2 = new IntervalsSet(new ArrayList<Interval>() {{
 add(new Interval(Double.NEGATIVE_INFINITY, 0.0));
 add(new Interval(1.0, 3.0));
 add(new Interval(5.0, Double.POSITIVE_INFINITY));
 add(new Interval(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
 }});
 List<Interval> list2 = intervalsSet2.asList();
 List<Interval> expectedList2 = new ArrayList<Interval>() {{
 add(new Interval(Double.NEGATIVE_INFINITY, 0.0));
 add(new Interval(1.0, 3.0));
 add(new Interval(5.0, Double.POSITIVE_INFINITY));
 add(new Interval(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
 }};
 assertEquals(expectedList2, list2);
}
}