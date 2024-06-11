package org.apache.commons.math3.geometry.euclidean.oned;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.*;
import java.util.stream.Collectors;

public class IntervalsSetTest {

 @BeforeClass
 public static void setUpClass() {
 // Set up any static resources or initialize any static variables here
 }

 @AfterClass
 public static void tearDownClass() {
 // Clean up any static resources or reset any static variables here
 }

 @Before
 public void setUp() {
 // Set up any instance variables or initialize any test objects here
 }

 @After
 public void tearDown() {
 // Clean up any test objects or reset any instance variables here
 }

 @Test
 public void testEmptyIntervalsSet() {
 IntervalsSet intervalsSet = new IntervalsSet();
 assertTrue(intervalsSet.isEmpty());
 assertEquals(0, intervalsSet.size(), 0.0);
 assertNull(intervalsSet.getBarycenter());
 }

 @Test
 public void testIntervalsSetWithOneInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0));
 assertFalse(intervalsSet.isEmpty());
 assertEquals(1, intervalsSet.size(), 1.0);
 assertTrue(intervalsSet.contains(new Euclidean1D(1.5)));
 assertFalse(intervalsSet.contains(new Euclidean1D(2.5)));
 }

 @Test
 public void testIntervalsSetWithTwoOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(2.0, 3.0));
 assertFalse(intervalsSet.isEmpty());
 assertEquals(2, intervalsSet.size(), 2.0);
 assertTrue(intervalsSet.contains(new Euclidean1D(1.5)));
 assertTrue(intervalsSet.contains(new Euclidean1D(2.5)));
 }

 @Test
 public void testIntervalsSetWithTwoNonOverlappingIntervals() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
 assertFalse(intervalsSet.isEmpty());
 assertEquals(2, intervalsSet.size(), 2.0);
 assertFalse(intervalsSet.contains(new Euclidean1D(2.5)));
 assertTrue(intervalsSet.contains(new Euclidean1D(1.5)));
 assertTrue(intervalsSet.contains(new Euclidean1D(3.5)));
 assertFalse(intervalsSet.contains(new Euclidean1D(2.5)));
 }

 @Test
 public void testIntervalsSetWithOneSelfOverlappingInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 3.0));
 assertFalse(intervalsSet.isEmpty());
 assertEquals(1, intervalsSet.size(), 1.0);
 assertTrue(intervalsSet.contains(new Euclidean1D(2.0)));
 }

 @Test
 public void testIntervalsSetWithEmptyCollection() {
 IntervalsSet intervalsSet = new IntervalsSet(Collections.emptyList());
 assertTrue(intervalsSet.isEmpty());
 assertEquals(0, intervalsSet.size(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithNullCollection() {
 IntervalsSet intervalsSet = new IntervalsSet(null);
 fail("Expected IllegalArgumentException but didn't get it.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithNullInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, 2.0), null);
 fail("Expected IllegalArgumentException but didn't get it.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithLowerThanZeroInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(-1.0, 2.0));
 fail("Expected IllegalArgumentException but didn't get it.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithUpperThanZeroInterval() {
 IntervalsSet intervalsSet = new IntervalsSet(new Interval(1.0, Double.POSITIVE_INFINITY));
 fail("Expected IllegalArgumentException but didn't get it.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithInfiniteLowerThanZeroTree() {
 IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 fail("Expected IllegalArgumentException but didn't get it.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntervalsSetWithNullBSPTree() {
 IntervalsSet intervalsSet = new IntervalsSet(null);
 fail("Expected IllegalArgumentException but didn't get it.");
 }
}