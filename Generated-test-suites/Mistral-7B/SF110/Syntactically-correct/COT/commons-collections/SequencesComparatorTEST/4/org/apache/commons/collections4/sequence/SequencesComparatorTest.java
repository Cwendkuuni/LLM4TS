package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;

public class SequencesComparatorTest {

 private List<Integer> list1;
 private List<Integer> list2;
 private SequencesComparator<Integer> comparator;

 @Test
 public void testSequencesComparatorConstructorWithTwoListsAndDefaultEquator() {
 list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 list2 = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
 comparator = new SequencesComparator<>(list1, list2);
 }

 @Test
 public void testSequencesComparatorConstructorWithTwoListsAndCustomEquator() {
 list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 list2 = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
 Equator<Integer> customEquator = new Equator<Integer>() {
 @Override
 public boolean equate(final Object o1, final Object o2) {
 return o1.equals(o2);
 }
 };
 comparator = new SequencesComparator<>(list1, list2, customEquator);
 }

 @Test
 public void testGetScript_EmptyLists() {
 list1 = new ArrayList<>();
 list2 = new ArrayList<>();
 comparator = new SequencesComparator<>(list1, list2);
 EditScript<Integer> script = comparator.getScript();
 assertTrue(script.isEmpty());
 }

 @Test
 public void testGetScript_IdenticalLists() {
 list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 list2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 comparator = new SequencesComparator<>(list1, list2);
 EditScript<Integer> script = comparator.getScript();
 assertEquals(8, script.size());
 for (int i = 0; i < script.size(); ++i) {
 assertTrue(script.getCommand(i) instanceof KeepCommand);
 }
 }

 @Test
 public void testGetScript_NonIdenticalLists() {
 list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 list2 = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
 comparator = new SequencesComparator<>(list1, list2);
 EditScript<Integer> script = comparator.getScript();
 assertEquals(10, script.size());
 // Test the commands in the script
 // ...
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetScript_NullList1() {
 list1 = null;
 list2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 comparator = new SequencesComparator<>(list1, list2);
 assertNull(comparator.getScript());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetScript_NullList2() {
 list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 list2 = null;
 comparator = new SequencesComparator<>(list1, list2);
 assertNull(comparator.getScript());
 }

 @Test
 public void testBuildSnake_EdgeCases() {
 list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 list2 = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
 comparator = new SequencesComparator<>(list1, list2);
 int[] vDown = comparator.vDown;
 int[] vUp = comparator.vUp;
 Snake snake = comparator.getMiddleSnake(0, list1.size(), 0, list2.size());
 int expectedStart = list1.size() - 1;
 int expectedEnd = 0;
 int expectedDiag = list1.size() - 1;
 assertEquals(new Snake(expectedStart, expectedEnd, expectedDiag), snake);
 // Test other edge cases
 // ...
 }

 @Test(expected = RuntimeException.class)
 public void testBuildSnake_InternalError() {
 list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
 list2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
 comparator = new SequencesComparator<>(list1, list2);
 comparator.buildScript(0, list1.size(), 0, list2.size(), null);
 }

}