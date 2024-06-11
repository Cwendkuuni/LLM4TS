package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;

public class SequencesComparatorTest {

 private static final List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 private static final List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 2, 6, 4, 5));
 private static final Equator<Integer> equator = Equator.equalTo(null);

 @Test
 public void testSequencesComparatorEmptySequences() {
 SequencesComparator<Integer> sc = new SequencesComparator<>(Collections.emptyList(), Collections.emptyList());
 assertEquals(Collections.emptyList(), sc.getScript().getCommands());
 }

 @Test
 public void testSequencesComparatorIdenticalSequences() {
 SequencesComparator<Integer> sc = new SequencesComparator<>(list1, list1);
 assertEquals(Collections.emptyList(), sc.getScript().getCommands());
 }

 @Test
 public void testSequencesComparatorSequencesWithSameElements() {
 SequencesComparator<Integer> sc = new SequencesComparator<>(list1, list2);
 List<EditCommand<Integer>> script = sc.getScript().getCommands();

 assertEquals(5, script.size());
 for (int i = 0; i < 5; ++i) {
 KeepCommand<Integer> keepCmd = (KeepCommand<Integer>)script.get(i);
 assertEquals(list1.get(i), keepCmd.getObject());
 }
 }

 @Test
 public void testSequencesComparatorSequencesWithDifferentLengths() {
 SequencesComparator<Integer> sc = new SequencesComparator<>(list1, list2);
 List<EditCommand<Integer>> script = sc.getScript().getCommands();

 assertEquals(7, script.size());
 for (int i = 0; i < 3; ++i) {
 DeleteCommand<Integer> deleteCmd = (DeleteCommand<Integer>)script.get(i);
 assertEquals(list1.get(i), deleteCmd.getObject());
 }
 for (int i = 3; i < 5; ++i) {
 InsertCommand<Integer> insertCmd = (InsertCommand<Integer>)script.get(i);
 assertEquals(list2.get(i - 3), insertCmd.getObject());
 }
 for (int i = 5; i < 7; ++i) {
 KeepCommand<Integer> keepCmd = (KeepCommand<Integer>)script.get(i);
 assertNull(keepCmd.getObject());
 }
 }

 @Test(expected = RuntimeException.class)
 public void testSequencesComparatorInvalidInput() {
 List<Object> list3 = new ArrayList<>();
 list3.add(null);
 SequencesComparator<Object> sc = new SequencesComparator<>(list1, list3);
 }

 @Test
 public void testSequencesComparatorWithCustomEquator() {
 Equator<Integer> customEquator = new Equator<Integer>() {
 @Override
 public boolean equate(Object a, Object b) {
 return a instanceof Integer && b instanceof Integer && a.equals(b);
 }
 };
 SequencesComparator<Integer> sc = new SequencesComparator<>(list1, list2, customEquator);
 List<EditCommand<Integer>> script = sc.getScript().getCommands();

 assertEquals(5, script.size());
 for (int i = 0; i < 5; ++i) {
 KeepCommand<Integer> keepCmd = (KeepCommand<Integer>)script.get(i);
 assertNotSame(list1.get(i), keepCmd.getObject());
 assertNotSame(list2.get(i), keepCmd.getObject());
 assertEquals(list1.get(i).intValue(), keepCmd.getObject().intValue());
 }
 }

}