package org.apache.commons.math3.geometry.euclidean.oned;

import org.apache.commons.math3.geometry.AbstractRegion;
import org.apache.commons.math3.geometry.point.Vector1D;
import org.apache.commons.math3.geometry.util.BSPTree;
import org.apache.commons.math3.geometry.util.Region;
import org.apache.commons.math3.geometry.util.SubHyperplane;
import org.apache.commons.math3.geometry.util.Vector1DBasis;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

 @Test
 public void testEmptySet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(0, intervalsSet.size(), 0.0);
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testFiniteSet() {
 IntervalsSet intervalsSet = new IntervalsSet(new Vector1D(1.0), new Vector1D(5.0));
 assertEquals(4, intervalsSet.size(), 0.0);
 Vector1D barycenter = intervalsSet.getBarycenter();
 assertNotNull(barycenter);
 assertTrue(barycenter.getX() >= 1.0 && barycenter.getX() <= 5.0);
 }

 @Test
 public void testInfiniteSet() {
 IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.size(), 0.0);
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testSetWithBoundary() {
 IntervalsSet intervalsSet = new IntervalsSet(new IntervalsSet(new Vector1D(0.0), new Vector1D(2.0)).asList(), new Vector1D(1.0));
 assertEquals(2, intervalsSet.size(), 0.0);
 Vector1D barycenter = intervalsSet.getBarycenter();
 assertNotNull(barycenter);
 assertEquals(1.0, barycenter.getX(), Precision.SAFE_EPSILON);
 }

 @Test
 public void testIntersectionWithEmptySet() {
 IntervalsSet intervalsSet1 = new IntervalsSet(new Vector1D(1.0), new Vector1D(3.0));
 IntervalsSet intervalsSet2 = new IntervalsSet();
 IntervalsSet intersection = intervalsSet1.intersection(intervalsSet2);
 assertEquals(0, intersection.size(), 0.0);
 }

 @Test
 public void testIntersectionWithSelf() {
 IntervalsSet intervalsSet = new IntervalsSet(new Vector1D(1.0), new Vector1D(3.0));
 IntervalsSet intersection = intervalsSet.intersection(intervalsSet);
 assertEquals(2, intersection.size(), 0.0);
 }

 @Test
 public void testIntersectionWithDisjointSets() {
 IntervalsSet intervalsSet1 = new IntervalsSet(new Vector1D(1.0), new Vector1D(3.0));
 IntervalsSet intervalsSet2 = new IntervalsSet(new Vector1D(5.0), new Vector1D(7.0));
 IntervalsSet intersection = intervalsSet1.intersection(intervalsSet2);
 assertEquals(0, intersection.size(), 0.0);
 }

 @Test
 public void testIntersectionWithOverlappingSets() {
 IntervalsSet intervalsSet1 = new IntervalsSet(new Vector1D(1.0), new Vector1D(4.0));
 IntervalsSet intervalsSet2 = new IntervalsSet(new Vector1D(2.0), new Vector1D(5.0));
 IntervalsSet intersection = intervalsSet1.intersection(intervalsSet2);
 IntervalsSet expectedIntersection = new IntervalsSet(new Interval(2.0, 4.0));
 assertEquals(1, intersection.size(), 0.0);
 List<Interval> intersectionList = intersection.asList();
 assertEquals(expectedIntersection.asList().get(0).getInf(), intersectionList.get(0).getInf(), Precision.SAFE_EPSILON);
 assertEquals(expectedIntersection.asList().get(0).getSup(), intersectionList.get(0).getSup(), Precision.SAFE_EPSILON);
 }

 @Test
 public void testUnionWithEmptySet() {
 IntervalsSet intervalsSet1 = new IntervalsSet(new Vector1D(1.0), new Vector1D(3.0));
 IntervalsSet intervalsSet2 = new IntervalsSet();
 IntervalsSet union = intervalsSet1.union(intervalsSet2);
 assertEquals(2, union.size(), 0.0);
 }

 @Test
 public void testUnionWithSelf() {
 IntervalsSet intervalsSet = new IntervalsSet(new Vector1D(1.0), new Vector1D(3.0));
 IntervalsSet union = intervalsSet.union(intervalsSet);
 assertEquals(2, union.size(), 0.0);
 }

 @Test
 public void testUnionWithDisjointSets() {
 IntervalsSet intervalsSet1 = new IntervalsSet(new Vector1D(1.0), new Vector1D(3.0));
 IntervalsSet intervalsSet2 = new IntervalsSet(new Vector1D(5.0), new Vector1D(7.0));
 IntervalsSet union = intervalsSet1.union(intervalsSet2);
 assertEquals(4, union.size(), 0.0);
 }

 @Test
 public void testUnionWithOverlappingSets() {
 IntervalsSet intervalsSet1 = new IntervalsSet(new Vector1D(1.0), new Vector1D(4.0));
 IntervalsSet intervalsSet2 = new IntervalsSet(new Vector1D(2.0), new Vector1D(5.0));
 IntervalsSet union = intervalsSet1.union(intervalsSet2);
 IntervalsSet expectedUnion = new IntervalsSet(new Interval(1.0, 5.0));
 assertEquals(1, union.size(), 0.0);
 List<Interval> unionList = union.asList();
 assertEquals(expectedUnion.asList().get(0).getInf(), unionList.get(0).getInf(), Precision.SAFE_EPSILON);
 assertEquals(expectedUnion.asList().get(0).getSup(), unionList.get(0).getSup(), Precision.SAFE_EPSILON);
 }

 @Test
 public void testDifferenceWithEmptySet() {
 IntervalsSet intervalsSet = new IntervalsSet(new Vector1D(1.0), new Vector11D(3.0));
 IntervalsSet difference = intervalsSet.difference(new IntervalsSet());
 assertEquals(2, difference.size(), 0.0);
 }

 @Test
 public void testDifferenceWithSelf() {
 IntervalsSet intervalsSet = new IntervalsSet(new Vector1D(1.0), new Vector1D(3.0));
 IntervalsSet difference = intervalsSet.difference(intervalsSet);
 assertEquals(0, difference.size(), 0.0);
 }

 @Test
 public void testDifferenceWithDisjointSets() {
 IntervalsSet intervalsSet1 = new IntervalsSet(new Vector1D(1.0), new Vector1D(3.0));
 IntervalsSet intervalsSet2 = new IntervalsSet(new Vector1D(5.0), new Vector1D(7.0));
 IntervalsSet difference = intervalsSet1.difference(intervalsSet2);
 assertEquals(2, difference.size(), 0.0);
 }

 @Test
 public void testDifferenceWithOverlappingSets() {
 IntervalsSet intervalsSet1 = new IntervalsSet(new Vector1D(1.0), new Vector1D(4.0));
 IntervalsSet intervalsSet2 = new IntervalsSet(new Vector1D(2.0), new Vector1D(5.0));
 IntervalsSet difference = intervalsSet1.difference(intervalsSet2);
 IntervalsSet expectedDifference = new IntervalsSet(new Interval(1.0, 2.0), new Interval(4.0, 5.0));
 assertEquals(2, difference.size(), 0.0);
 List<Interval> differenceList = difference.asList();
 assertEquals(expectedDifference.asList().get(0).getInf(), differenceList.get(0).getInf(), Precision.SAFE_EPSILON);
 assertEquals(expectedDifference.asList().get(0).getSup(), differenceList.get(0).getSup(), Precision.SAFE_EPSILON);
 assertEquals(expectedDifference.asList().get(1).getInf(), differenceList.get(1).getInf(), Precision.SAFE_EPSILON);
 assertEquals(expectedDifference.asList().get(1).getSup(), differenceList.get(1).getSup(), Precision.SAFE_EPSILON);
 }

 @Test
 public void testProject() {
 IntervalsSet intervalsSet = new IntervalsSet(new Vector1D(1.0), new Vector1D(3.0));
 Vector1DBasis basis = new Vector1DBasis();
 IntervalsSet projectedSet = intervalsSet.project(basis);
 assertEquals(2, projectedSet.size(), 0.0);
 }

 @Test
 public void testProjectWithIdentityBasis() {
 IntervalsSet intervalsSet = new IntervalsSet(new Vector1D(1.0), new Vector1D(3.0));
 Vector1DBasis basis = new Vector1DBasis(new Vector1D[]{new Vector1D(1.0)});
 IntervalsSet projectedSet = intervalsSet.project(basis);
 assertEquals(2, projectedSet.size(), 0.0);
 }

 @Test
 public void testProjectWithNullBasis() {
 IntervalsSet intervalsSet = new IntervalsSet(new Vector1D(1.0), new Vector1D(3.0));
 Vector1DBasis basis = null;
 try {
 IntervalsSet projectedSet = intervalsSet.project(basis);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testProjectWithMismatchedBasis() {
 IntervalsSet intervalsSet = new IntervalsSet(new Vector1D(1.0), new Vector1D(3.0));
 Vector1DBasis basis = new Vector1DBasis(new Vector1D[]{new Vector1D(1.0), new Vector1D(2.0)});
 try {
 IntervalsSet projectedSet = intervalsSet.project(basis);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetInf() {
 IntervalsSet intervalsSet = new IntervalsSet(new Vector1D(1.0), new Vector1D(3.0));
 assertEquals(3.0, intervalsSet.getInf(), Precision.SAFE_EPSILON);
 }

 @Test
 public void testGetSup() {
 IntervalsSet intervalsSet = new IntervalsSet(new Vector1D(1.0), new Vector1D(3.0));
 assertEquals(1.0, intervalsSet.getSup(), Precision.SAFE_EPSILON);
 }

 @Test
 public void testGetInfEmptySet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0);
 }

 @Test
 public void testGetSupEmptySet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0);
 }

 @Test
 public void testAsList() {
 IntervalsSet intervalsSet = new IntervalsSet(new Vector1D(1.0), new Vector1D(3.0));
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(2, intervalsList.size());
 }

 @Test
 public void testAsListEmptySet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 List<Interval> intervalsList = intervalsSet.asList();
 assertEquals(0, intervalsList.size());
 }
}