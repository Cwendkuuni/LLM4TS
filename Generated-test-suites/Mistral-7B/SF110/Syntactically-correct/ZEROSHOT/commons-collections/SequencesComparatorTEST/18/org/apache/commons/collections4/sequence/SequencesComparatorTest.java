package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;

public class SequencesComparatorTest {

 private static final List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 private static final List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 2, 6, 4, 5, 7));
 private static final Equator<Integer> EQUATOR = new Equator<Integer>() {
 @Override
 public boolean equate(Object obj1, Object obj2) {
 return obj1.equals(obj2);
 }
 };

 @Test
 public void testSequencesComparator_equalSequences() {
 List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 EditScript<Integer> script = comparator.getScript();
 assertEquals("KeepCommand, KeepCommand, KeepCommand, KeepCommand, KeepCommand", script.toString());
 }

 @Test
 public void testSequencesComparator_emptySequence1() {
 List<Integer> sequence1 = new ArrayList<>();
 List<Integer> sequence2 = list1;
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 EditScript<Integer> script = comparator.getScript();
 assertEquals("DeleteAllCommand, InsertCommand, InsertCommand, InsertCommand, InsertCommand", script.toString());
 }

 @Test
 public void testSequencesComparator_emptySequence2() {
 List<Integer> sequence1 = list1;
 List<Integer> sequence2 = new ArrayList<>();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 EditScript<Integer> script = comparator.getScript();
 assertEquals("InsertCommand, InsertCommand, InsertCommand, InsertCommand, InsertCommand", script.toString());
 }

 @Test
 public void testSequencesComparator_reverseSequences() {
 List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 List<Integer> sequence2 = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 EditScript<Integer> script = comparator.getScript();
 assertEquals("ReverseCommand, ReverseCommand, ReverseCommand, ReverseCommand, ReverseCommand", script.toString());
 }

 @Test
 public void testSequencesComparator_oneElementDifference() {
 List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 6));
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 EditScript<Integer> script = comparator.getScript();
 assertEquals("DeleteCommand, InsertCommand", script.toString());
 }

 @Test
 public void testSequencesComparator_multipleInsertions() {
 List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 6, 4, 5, 7, 8, 9));
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 EditScript<Integer> script = comparator.getScript();
 assertEquals("DeleteCommand, DeleteCommand, InsertCommand, InsertCommand, InsertCommand, InsertCommand, InsertCommand", script.toString());
 }

 @Test
 public void testSequencesComparator_multipleDeletions() {
 List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
 List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 7, 8));
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 EditScript<Integer> script = comparator.getScript();
 assertEquals("InsertCommand, InsertCommand, DeleteCommand, DeleteCommand, DeleteCommand", script.toString());
 }

 @Test(expected = RuntimeException.class)
 public void testSequencesComparator_errorCase() {
 List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 5));
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 }

}