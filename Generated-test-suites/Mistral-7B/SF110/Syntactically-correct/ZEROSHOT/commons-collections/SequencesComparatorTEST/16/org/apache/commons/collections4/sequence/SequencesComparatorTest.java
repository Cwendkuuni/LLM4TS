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

 @Before
 public void setup() {
 list1 = Arrays.asList(1, 2, 3, 4, 5);
 list2 = Arrays.asList(1, 2, 6, 4, 5);
 comparator = new SequencesComparator<>(list1, list2);
 }

 @Test
 public void testEmptySequences() {
 List<Integer> emptyList1 = Collections.emptyList();
 List<Integer> emptyList2 = Collections.emptyList();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(emptyList1, emptyList2);
 assertEquals(new EditScript<Integer>(), comparator.getScript());
 }

 @Test
 public void testIdenticalSequences() {
 List<Integer> identicalList1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> identicalList2 = Arrays.asList(1, 2, 3, 4, 5);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(identicalList1, identicalList2);
 assertEquals(new EditScript<Integer>(), comparator.getScript());
 }

 @Test
 public void testDifferentSequences() {
 List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> list2 = Arrays.asList(1, 2, 6, 4, 5);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);

 EditScript<Integer> script = comparator.getScript();
 assertEquals(5, script.size());

 KeepCommand<Integer> keepCommand1 = (KeepCommand<Integer>) script.getCommand(0);
 assertEquals(1, keepCommand1.getElement().intValue());

 DeleteCommand<Integer> deleteCommand1 = (DeleteCommand<Integer>) script.getCommand(1);
 assertEquals(2, deleteCommand1.getElement().intValue());

 InsertCommand<Integer> insertCommand1 = (InsertCommand<Integer>) script.getCommand(2);
 assertEquals(6, insertCommand1.getElement().intValue());

 KeepCommand<Integer> keepCommand2 = (KeepCommand<Integer>) script.getCommand(3);
 assertEquals(4, keepCommand2.getElement().intValue());

 KeepCommand<Integer> keepCommand3 = (KeepCommand<Integer>) script.getCommand(4);
 assertEquals(5, keepCommand3.getElement().intValue());
 }

 @Test(expected = RuntimeException.class)
 public void testFailedComparison() {
 List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> list2 = Arrays.asList(1, 2, 3, 4, 6);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 }

}