package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;

public class SequencesComparatorTest<T> {

 private List<T> sequence1;
 private List<T> sequence2;
 private Equator<? super T> equator;
 private SequencesComparator<T> sequencer;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 sequence1 = new ArrayList<>();
 sequence2 = new ArrayList<>();
 equator = new EquatorImpl<>();
 }

 @Test
 public void testSequencesComparatorConstructionWithSequences() {
 List<Integer> list1 = Arrays.asList(1, 2, 3);
 List<Integer> list2 = Arrays.asList(1, 2, 3, 4);
 sequencer = new SequencesComparator<>(list1, list2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSequencesComparatorConstructionWithNullSequence() {
 List<Integer> list1 = Arrays.asList(1, 2, 3);
 List<Integer> list2 = null;
 try {
 new SequencesComparator<>(list1, list2);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSequencesComparatorConstructionWithDifferentSizeSequences() {
 List<Integer> list1 = Arrays.asList(1, 2, 3);
 List<Integer> list2 = Arrays.asList(1, 2, 3, 4, 5);
 try {
 new SequencesComparator<>(list1, list2);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = NullPointerException.class)
 public void testSequencesComparatorConstructionWithNullEquator() {
 List<Integer> list1 = Arrays.asList(1, 2, 3);
 List<Integer> list2 = Arrays.asList(1, 2, 3);
 Equator<? super Integer> nullEquator = null;
 try {
 new SequencesComparator<>(list1, list2, nullEquator);
 fail("NullPointerException expected");
 } catch (NullPointerException e) {
 // pass
 }
 }

 @Test
 public void testGetScript() {
 List<Integer> list1 = Arrays.asList(1, 2, 3);
 List<Integer> list2 = Arrays.asList(1, 2, 3, 4);
 sequencer = new SequencesComparator<>(list1, list2);
 EditScript<Integer> script = sequencer.getScript();
 // Expected: Keep, Delete, Insert Commands
 assertTrue(script.isEmpty());
 }

 @Test
 public void testGetScriptWithDifferentSequences() {
 List<Integer> list1 = Arrays.asList(1, 2, 3);
 List<Integer> list2 = Arrays.asList(2, 3, 4);
 sequencer = new SequencesComparator<>(list1, list2);
 EditScript<Integer> script = sequencer.getScript();
 // Expected: Keep, Delete, Insert Commands
 KeepCommand<Integer> keepCommand = new KeepCommand<>(1);
 DeleteCommand<Integer> deleteCommand = new DeleteCommand<>(1);
 InsertCommand<Integer> insertCommand = new InsertCommand<>(2);
 List<EditCommand<Integer>> expectedCommands = Arrays.asList(deleteCommand, insertCommand, keepCommand);
 List<EditCommand<Integer>> actualCommands = new ArrayList<>();
 while (!script.isEmpty()) {
 actualCommands.add(script.pollFirst());
 }
 Collections.reverse(actualCommands);
 assertEquals(expectedCommands, actualCommands);
 }

 @Test
 public void testGetScriptWithIdenticalSequences() {
 List<Integer> list1 = Arrays.asList(1, 2, 3);
 List<Integer> list2 = Arrays.asList(1, 2, 3);
 sequencer = new SequencesComparator<>(list1, list2);
 EditScript<Integer> script = sequencer.getScript();
 // Expected: Empty script
 assertTrue(script.isEmpty());
 }

 @Test
 public void testBuildSnakeWithNonEmptySequencesAndEqualLength() {
 List<Integer> list1 = Arrays.asList(1, 2, 3);
 List<Integer> list2 = Arrays.asList(1, 2, 3);
 equator = new EquatorImpl<>();
 int start1 = 0;
 int end1 = list1.size();
 int start2 = 0;
 int end2 = list2.size();
 Snake middleSnake = sequencer.getMiddleSnake(start1, end1, start2, end2);
 assertNull(middleSnake);
 }

 @Test
 public void testBuildSnakeWithNonEmptySequencesAndDifferentLengthsAndNoMatchingPair() {
 List<Integer> list1 = Arrays.asList(1, 2, 3);
 List<Integer> list2 = Arrays.asList(1, 2, 3, 4);
 equator = new EquatorImpl<>();
 int start1 = 0;
 int end1 = list1.size();
 int start2 = 0;
 int end2 = list2.size();
 Snake middleSnake = sequencer.getMiddleSnake(start1, end1, start2, end2);
 assertNull(middleSnake);
 }

 @Test
 public void testBuildSnakeWithNonEmptySequencesAndDifferentLengthsAndMatchingPairAtBeginning() {
 List<Integer> list1 = Arrays.asList(1, 2, 3);
 List<Integer> list2 = Arrays.asList(1, 2, 3, 4);
 equator = new EquatorImpl<>();
 int start1 = 0;
 int end1 = list1.size();
 int start2 = 0;
 int end2 = list2.size();
 Snake middleSnake = sequencer.getMiddleSnake(start1, end1, start2, end2);
 Snake expectedMiddleSnake = new Snake(0, 0, 0);
 assertEquals(expectedMiddleSnake, middleSnake);
 }

 @Test
 public void testBuildSnakeWithNonEmptySequencesAndDifferentLengthsAndMatchingPairAtEnd() {
 List<Integer> list1 = Arrays.asList(1, 2, 3);
 List<Integer> list2 = Arrays.asList(1, 2, 3, 4);
 equator = new EquatorImpl<>();
 int start1 = 0;
 int end1 = list1.size();
 int start2 = 0;
 int end2 = list2.size();
 Snake middleSnake = sequencer.getMiddleSnake(start1, end1, start2, end2);
 Snake expectedMiddleSnake = new Snake(end1, end2, end1 - end2);
 assertEquals(expectedMiddleSnake, middleSnake);
 }

 @Test
 public void testBuildSnakeWithNonEmptySequencesAndDifferentLengthsAndMatchingPairInMiddle() {
 List<Integer> list1 = Arrays.asList(1, 2, 3);
 List<Integer> list2 = Arrays.asList(1, 2, 3, 4);
 equator = new EquatorImpl<>();
 int start1 = 0;
 int end1 = list1.size();
 int start2 = 0;
 int end2 = list2.size();
 Snake middleSnake = sequencer.getMiddleSnake(start1, end1, start2, end2);
 Snake expectedMiddleSnake = new Snake(1, 2, 1);
 assertEquals(expectedMiddleSnake, middleSnake);
 }

 @Test
 public void testBuildSnakeWithEmptySequences() {
 List<Integer> list1 = new ArrayList<>();
 List<Integer> list2 = new ArrayList<>();
 equator = new EquatorImpl<>();
 int start1 = 0;
 int end1 = list1.size();
 int start2 = 0;
 int end2 = list2.size();
 Snake middleSnake = sequencer.getMiddleSnake(start1, end1, start2, end2);
 assertNull(middleSnake);
 }

 @Test(expected = RuntimeException.class)
 public void testBuildSnakeWithInternalError() {
 List<Integer> list1 = Arrays.asList(1, 2, 3);
 List<Integer> list2 = Arrays.asList(1, 2, 3);
 equator = new EquatorImpl<>();
 int start1 = 0;
 int end1 = list1.size();
 int start2 = 0;
 int end2 = list2.size();
 try {
 sequencer.getMiddleSnake(start1, end1, start2, end2);
 fail("RuntimeException expected");
 } catch (RuntimeException e) {
 // pass
 }
 }

}