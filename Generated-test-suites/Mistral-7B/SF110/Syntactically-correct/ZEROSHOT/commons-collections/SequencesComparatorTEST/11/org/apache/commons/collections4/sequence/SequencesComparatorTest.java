package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.apache.commons.collections4.test.EditableSequencesTestUtils;
import org.junit.Test;

public class SequencesComparatorTest {

 private static final List<Integer> list1 = newArrayList(1, 2, 3, 4, 5);
 private static final List<Integer> list2 = newArrayList(1, 2, 4, 5, 3);
 private static final Equator<Integer> EQUATOR = DefaultEquator.defaultEquator();

 @Test
 public void testSequencesComparatorWithEqualSequences() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list1);
 EditScript<Integer> script = comparator.getScript();
 assertEquals("Should return an empty script for equal sequences.", ScriptUtils.EMPTY_SCRIPT, script);
 }

 @Test
 public void testSequencesComparatorWithEmptySequence1() {
 List<Integer> emptyList = Collections.emptyList();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(emptyList, list2);
 EditScript<Integer> script = comparator.getScript();
 List<EditCommand<Integer>> commands = ScriptUtils.asList(script);
 assertEquals("Should return delete all elements from sequence1.", list2.size(), commands.size());
 for (EditCommand<Integer> command : commands) {
 if (command instanceof DeleteCommand<?>) {
 assertTrue("All commands should be DeleteCommand.", command instanceof DeleteCommand<?>);
 }
 }
 }

 @Test
 public void testSequencesComparatorWithEmptySequence2() {
 List<Integer> emptyList = Collections.emptyList();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, emptyList);
 EditScript<Integer> script = comparator.getScript();
 List<EditCommand<Integer>> commands = ScriptUtils.asList(script);
 assertEquals("Should return insert all elements from sequence2.", list1.size(), commands.size());
 for (EditCommand<Integer> command : commands) {
 if (command instanceof InsertCommand<?>) {
 assertTrue("All commands should be InsertCommand.", command instanceof InsertCommand<?>);
 }
 }
 }

 @Test
 public void testSequencesComparatorWithSequencesDifferenceAtStart() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 EditScript<Integer> script = comparator.getScript();
 List<EditCommand<Integer>> commands = ScriptUtils.asList(script);
 assertEquals("Should return insert the first element of list2 and delete the first element of list1.", 2, commands.size());
 for (EditCommand<Integer> command : commands) {
 if (command instanceof InsertCommand<?>) {
 assertEquals("Should insert the first element of list2.", list2.get(0), command.getObject());
 }
 else if (command instanceof DeleteCommand<?>) {
 assertEquals("Should delete the first element of list1.", list1.get(0), command.getObject());
 }
 }
 }

 @Test
 public void testSequencesComparatorWithSequencesDifferenceAtEnd() {
 List<Integer> list3 = newArrayList(1, 2, 3, 4, 5, 6);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list3);
 EditScript<Integer> script = comparator.getScript();
 List<EditCommand<Integer>> commands = ScriptUtils.asList(script);
 assertEquals("Should return insert the last element of list3 and delete the last element of list1.", 2, commands.size());
 for (EditCommand<Integer> command : commands) {
 if (command instanceof InsertCommand<?>) {
 assertEquals("Should insert the last element of list3.", list3.get(list3.size() - 1), command.getObject());
 }
 else if (command instanceof DeleteCommand<?>) {
 assertEquals("Should delete the last element of list1.", list1.get(list1.size() - 1), command.getObject());
 }
 }
 }

 @Test
 public void testSequencesComparatorWithSequencesDifferenceInMiddle() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 EditScript<Integer> script = comparator.getScript();
 List<EditCommand<Integer>> commands = ScriptUtils.asList(script);
 assertEquals("Should return delete the element at index 2 of list1 and insert the element at index 2 of list2.", 2, commands.size());
 for (EditCommand<Integer> command : commands) {
 if (command instanceof DeleteCommand<?>) {
 assertEquals("Should delete the element at index 2 of list1.", list1.get(2), command.getObject());
 }
 else if (command instanceof InsertCommand<?>) {
 assertEquals("Should insert the element at index 2 of list2.", list2.get(2), command.getObject());
 }
 }
 }

 @Test (expected = RuntimeException.class)
 public void testSequencesComparatorInvalidInput() {
 List<Integer> emptyList = Collections.emptyList();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(emptyList, list1);
 }

 @Test (expected = RuntimeException.class)
 public void testSequencesComparatorInvalidInput2() {
 List<Integer> emptyList = Collections.emptyList();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, emptyList);
 }

 @Test
 public void testSequencesComparatorPerformance() {
 final int listSize = 10000;
 List<Integer> list1 = newArrayList();
 List<Integer> list2 = newArrayList();
 for (int i = 0; i < listSize; ++i) {
 list1.add(i);
 list2.add(i + 1);
 }
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 EditScript<Integer> script = comparator.getScript();
 long start = System.currentTimeMillis();
 List<EditCommand<Integer>> commands = ScriptUtils.asList(script);
 long end = System.currentTimeMillis();
 System.out.println("Time to get script: " + (end - start) + "ms");
 assertEquals("Should return " + listSize + " commands.", listSize, commands.size());
 }
}