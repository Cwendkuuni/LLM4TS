import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class IntervalsSetTest {

 private IntervalsSet intervalsSet;
 private IntervalsSet emptyIntervalsSet;
 private IntervalsSet intervalsSetWithOneInterval;
 private IntervalsSet intervalsSetWithTwoIntervals;
 private IntervalsSet intervalsSetWithInfAndNegInf;

 @Before
 public void setUp() {
 intervalsSet = new IntervalsSet();
 emptyIntervalsSet = new IntervalsSet();
 intervalsSetWithOneInterval = new IntervalsSet(0.0, 1.0);
 intervalsSetWithTwoIntervals = new IntervalsSet(0.0, 1.0, new IntervalsSet(2.0, 3.0));
 intervalsSetWithInfAndNegInf = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 }

 @Test
 public void testEmptyConstructor() {
 assertEquals(0, intervalsSet.size());
 assertTrue(intervalsSet.isEmpty());
 }

 @Test
 public void testFiniteIntervalConstructor() {
 assertEquals(1, intervalsSetWithOneInterval.size());
 assertFalse(intervalsSetWithOneInterval.isEmpty());
 }

 @Test
 public void testBSPTreeConstructor() {
 IntervalsSet intervalsSetFromBSPTree = new IntervalsSet(intervalsSetWithOneInterval.getTree());
 assertEquals(intervalsSetWithOneInterval.size(), intervalsSetFromBSPTree.size());
 assertFalse(intervalsSetFromBSPTree.isEmpty());
 }

 @Test
 public void testCollectionConstructor() {
 List<Interval> intervals = new ArrayList<>();
 intervals.add(new Interval(0.0, 1.0));
 IntervalsSet intervalsSetFromCollection = new IntervalsSet(intervals);
 assertEquals(intervalsSetWithOneInterval.size(), intervalsSetFromCollection.size());
 assertFalse(intervalsSetFromCollection.isEmpty());
 }

 @Test
 public void testBuildNew() {
 IntervalsSet intervalsSetFromBuildNew = intervalsSet.buildNew(intervalsSetWithOneInterval.getTree());
 assertEquals(intervalsSetWithOneInterval.size(), intervalsSetFromBuildNew.size());
 assertFalse(intervalsSetFromBuildNew.isEmpty());
 }

 @Test
 public void testGetInf() {
 assertEquals(Double.POSITIVE_INFINITY, intervalsSetWithInfAndNegInf.getInf(), 0.0);
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSetWithInfAndNegInf.getSup(), 0.0);
 assertEquals(1.0, intervalsSetWithOneInterval.getInf(), Precision.SAFE_EPSILON);
 assertEquals(1.0, intervalsSetWithOneInterval.getSup(), Precision.SAFE_EPSILON);
 }

 @Test
 public void testGetSup() {
 assertEquals(Double.NEGATIVE_INFINITY, intervalsSetWithInfAndNegInf.getInf(), 0.0);
 assertEquals(Double.POSITIVE_INFINITY, intervalsSetWithInfAndNegInf.getSup(), 0.0);
 assertEquals(0.0, intervalsSetWithOneInterval.getInf(), Precision.SAFE_EPSILON);
 assertEquals(1.0, intervalsSetWithOneInterval.getSup(), Precision.SAFE_EPSILON);
 }

 @Test
 public void testSize() {
 assertEquals(0, emptyIntervalsSet.size());
 assertEquals(1, intervalsSetWithOneInterval.size());
 assertEquals(2, intervalsSetWithTwoIntervals.size());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(emptyIntervalsSet.isEmpty());
 assertFalse(intervalsSetWithOneInterval.isEmpty());
 assertFalse(intervalsSetWithTwoIntervals.isEmpty());
 }

 @Test
 public void testAsList() {
 List<Interval> intervalsFromAsListEmpty = intervalsSet.asList();
 assertTrue(intervalsFromAsListEmpty.isEmpty());
 List<Interval> intervalsFromAsListOneInterval = intervalsSetWithOneInterval.asList();
 assertEquals(1, intervalsFromAsListOneInterval.size());
 List<Interval> intervalsFromAsListTwoIntervals = intervalsSetWithTwoIntervals.asList();
 assertEquals(2, intervalsFromAsListTwoIntervals.size());
 }
}