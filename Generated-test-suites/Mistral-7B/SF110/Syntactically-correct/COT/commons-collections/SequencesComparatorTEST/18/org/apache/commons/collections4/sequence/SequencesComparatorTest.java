package org.apache.commons.collections4.sequence;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;

public class SequencesComparatorTest {

 private List<Integer> createList(int[] arr) {
 return Arrays.asList(arr);
 }

 private SequencesComparator<Integer> getComparator(List<Integer> seq1, List<Integer> seq2) {
 return new SequencesComparator<>(seq1, seq2);
 }

 @Test
 public void testConstructorWithTwoLists() {
 List<Integer> list1 = createList(new int[] { 1, 2, 3, 4 });
 List<Integer> list2 = createList(new int[] { 1, 3, 4, 2 });
 SequencesComparator<Integer> comparator = getComparator(list1, list2);
 }

 @Test
 public void testConstructorWithTwoListsAndEquator() {
 List<Integer> list1 = createList(new int[] { 1, 2, 3, 4 });
 List<Integer> list2 = createList(new int[] { 1, 3, 4, 2 });
 Equator<Integer> equator = new Equator<Integer>() {
 @Override
 public boolean equate(Object a, Object b) {
 return a.equals(b);
 }
 };
 SequencesComparator<Integer> comparator = getComparator(list1, list2, equator);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullList() {
 List<Integer> list1 = null;
 List<Integer> list2 = createList(new int[] { 1, 2, 3, 4 });
 assertThrows(IllegalArgumentException.class, () -> getComparator(list1, list2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullList2() {
 List<Integer> list1 = createList(new int[] { 1, 2, 3, 4 });
 List<Integer> list2 = null;
 assertThrows(IllegalArgumentException.class, () -> getComparator(list1, list2));
 }

 @Test
 public void testGetScript() {
 List<Integer> list1 = createList(new int[] { 1, 2, 3, 4 });
 List<Integer> list2 = createList(new int[] { 1, 3, 4, 2 });
 SequencesComparator<Integer> comparator = getComparator(list1, list2);
 EditScript<Integer> script = comparator.getScript();
 // Check the script
 }

 @Test
 public void testBuildScript_FirstSequenceShorter() {
 List<Integer> list1 = createList(new int[] { 1, 2, 3 });
 List<Integer> list2 = createList(new int[] { 1, 2, 3, 4 });
 SequencesComparator<Integer> comparator = getComparator(list1, list2);
 EditScript<Integer> script = comparator.getScript();
 // Check the script
 }

 @Test
 public void testBuildScript_SecondSequenceShorter() {
 List<Integer> list1 = createList(new int[] { 1, 2, 3, 4 });
 List<Integer> list2 = createList(new int[] { 1, 2, 3 });
 SequencesComparator<Integer> comparator = getComparator(list1, list2);
 EditScript<Integer> script = comparator.getScript();
 // Check the script
 }

 @Test
 public void testBuildScript_IdenticalSequences() {
 List<Integer> list1 = createList(new int[] { 1, 2, 3, 4 });
 List<Integer> list2 = createList(new int[] { 1, 2, 3, 4 });
 SequencesComparator<Integer> comparator = getComparator(list1, list2);
 EditScript<Integer> script = comparator.getScript();
 // Check the script
 }

 @Test
 public void testBuildScript_DifferentSequences() {
 List<Integer> list1 = createList(new int[] { 1, 2, 3, 4 });
 List<Integer> list2 = createList(new int[] { 1, 3, 4, 2 });
 SequencesComparator<Integer> comparator = getComparator(list1, list2);
 EditScript<Integer> script = comparator.getScript();
 // Check the script
 }

 @Test
 public void testGetMiddleSnake_ZeroSizeSequences() {
 List<Integer> list1 = createList(new int[0]);
 List<Integer> list2 = createList(new int[0]);
 SequencesComparator<Integer> comparator = getComparator(list1, list2);
 assertNull(comparator.getMiddleSnake(0, 0, 0, 0));
 }

 @Test(expected = RuntimeException.class)
 public void testGetMiddleSnake_InternalError() {
 List<Integer> list1 = createList(new int[] { 1, 2, 3 });
 List<Integer> list2 = createList(new int[] { 1, 2, 3 });
 SequencesComparator<Integer> comparator = getComparator(list1, list2);
 assertThrows(RuntimeException.class, () -> comparator.getMiddleSnake(0, 0, 0, 0));
 }

 @Test
 public void testGetMiddleSnake_DifferentSequences_NoSnake() {
 List<Integer> list1 = createList(new int[] { 1, 2, 3, 4, 5 });
 List<Integer> list2 = createList(new int[] { 1, 2, 3, 4 });
 SequencesComparator<Integer> comparator = getComparator(list1, list2);
 assertNull(comparator.getMiddleSnake(0, 5, 0, 4));
 }

 @Test
 public void testGetMiddleSnake_SameSequences_NoSnake() {
 List<Integer> list1 = createList(new int[] { 1, 2, 3, 4 });
 List<Integer> list2 = createList(new int[] { 1, 2, 3, 4 });
 SequencesComparator<Integer> comparator = getComparator(list1, list2);
 assertNull(comparator.getMiddleSnake(0, 4, 0, 4));
 }

 @Test
 public void testGetMiddleSnake_DifferentSequencesWithSnake() {
 List<Integer> list1 = createList(new int[] { 1, 2, 3, 4, 5, 6, 7 });
 List<Integer> list2 = createList(new int[] { 1, 2, 5, 4, 3, 7, 6 });
 SequencesComparator<Integer> comparator = getComparator(list1, list2);
 Snake snake = comparator.getMiddleSnake(0, 7, 0, 7);
 assertNotNull(snake);
 }

 @Test
 public void testBuildScript_WithMiddleSnake() {
 List<Integer> list1 = createList(new int[] { 1, 2, 3, 4, 5, 6, 7 });
 List<Integer> list2 = createList(new int[] { 1, 2, 5, 4, 3, 7, 6 });
 SequencesComparator<Integer> comparator = getComparator(list1, list2);
 EditScript<Integer> script = comparator.getScript();
 // Check the script
 }

}