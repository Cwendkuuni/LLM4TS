package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.apache.commons.collections4.test.EditablePair;

public class SequencesComparatorTest {

 private List<Integer> sequence1;
 private List<Integer> sequence2;

 @Before
 public void setUp() {
 sequence1 = Arrays.asList(1, 2, 3, 4);
 sequence2 = Arrays.asList(1, 3, 4, 2);
 }

 @Test
 public void testSequencesComparatorConstructionWithSequences() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 assertNotNull(comparator);
 }

 @Test
 public void testSequencesComparatorConstructionWithSequencesAndEquator() {
 Equator<Integer> equator = new Equator<Integer>() {
 @Override
 public boolean equate(Object o1, Object o2) {
 return ((Comparable<?>)o1).compareTo((Comparable<?>)o2) == 0;
 }
 };
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 assertNotNull(comparator);
 }

 @Test
 public void testGetScriptWithSequencesEqual() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 EditScript<Integer> script = comparator.getScript();
 assertEquals(0, script.size());
 }

 @Test
 public void testGetScriptWithSequencesNotEqual() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 EditScript<Integer> script = comparator.getScript();
 EditablePair<List<Integer>, List<Integer>> pair = new EditablePair<>(sequence1, sequence2);
 pair.applyScript(script);
 assertArrayEquals(new Integer[]{3, 4}, sequence2.subList(0, 2).toArray());
 assertArrayEquals(new Integer[]{1, 2}, sequence1.subList(2, 4).toArray());
 }

 @Test(expected = RuntimeException.class)
 public void testGetMiddleSnakeInternalError() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 comparator.getMiddleSnake(-1, sequence1.size(), 0, sequence2.size());
 }

 @Test
 public void testGetMiddleSnake() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 int[] vDown = comparator.vDown;
 int[] vUp = comparator.vUp;
 Snake middleSnake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
 assertEquals(1, middleSnake.getStart());
 assertEquals(3, middleSnake.getEnd());
 assertEquals(-1, middleSnake.getDiag());

 assertArrayEquals(new int[]{1, 2, 3, 4}, vDown);
 assertArrayEquals(new int[]{1, 2, 3, 4}, vUp);
 }

 @Test
 public void testGetMiddleSnakeWithEmptySequences() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(Collections.emptyList(), Collections.emptyList());
 try {
 comparator.getMiddleSnake(0, 0, 0, 0);
 fail();
 } catch (RuntimeException e) {
 // Expected
 }
 }

 @Test
 public void testGetMiddleSnakeWithOneElementDifference() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1), Arrays.asList(1, 2));
 Snake middleSnake = comparator.getMiddleSnake(0, 1, 0, 1);
 assertNull(middleSnake);
 }

 @Test
 public void testGetMiddleSnakeWithDifferentLengths() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 2));
 Snake middleSnake = comparator.getMiddleSnake(0, 3, 0, 1);
 assertNull(middleSnake);
 }

 @Test
 public void testGetMiddleSnakeWithIdenticalElements() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 2, 3), Arrays.asList(1, 2, 2, 3));
 Snake middleSnake = comparator.getMiddleSnake(0, 4, 0, 4);
 assertNull(middleSnake);
 }

 @Test
 public void testGetMiddleSnakeWithDifferentElements() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3, 4), Arrays.asList(1, 3, 4, 2));
 Snake middleSnake = comparator.getMiddleSnake(0, 4, 0, 4);
 assertNotNull(middleSnake);
 }

 @Test
 public void testBuildScriptWithEqualSequences() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 EditScript<Integer> script = comparator.getScript();
 assertEquals(0, script.size());
 }

 @Test
 public void testBuildScriptWithInsertions() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 4, 3, 2));
 EditScript<Integer> script = comparator.getScript();
 EditablePair<List<Integer>, List<Integer>> pair = new EditablePair<>(sequence1, sequence2);
 pair.applyScript(script);
 assertArrayEquals(new Integer[]{1, 4, 3}, sequence2.subList(0, 3).toArray());
 assertArrayEquals(new Integer[]{1, 2, 3}, sequence1.subList(0, 3).toArray());
 }

 @Test
 public void testBuildScriptWithDeletions() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3, 4), Arrays.asList(1, 2, 3));
 EditScript<Integer> script = comparator.getScript();
 EditablePair<List<Integer>, List<Integer>> pair = new EditablePair<>(sequence1, sequence2);
 pair.applyScript(script);
 assertArrayEquals(new Integer[]{1, 2, 3}, sequence1.subList(0, 3).toArray());
 assertArrayEquals(new Integer[]{1, 2}, sequence2.subList(0, 2).toArray());
 }

 @Test
 public void testBuildScriptWithSwaps() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3, 4), Arrays.asList(1, 3, 2, 4));
 EditScript<Integer> script = comparator.getScript();
 EditablePair<List<Integer>, List<Integer>> pair = new EditablePair<>(sequence1, sequence2);
 pair.applyScript(script);
 assertArrayEquals(new Integer[]{1, 3, 2, 4}, sequence1.subList(0, 4).toArray());
 assertArrayEquals(new Integer[]{1, 2, 3, 4}, sequence2.subList(0, 4).toArray());
 }
}