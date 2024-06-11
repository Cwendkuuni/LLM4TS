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
 list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 list2 = new ArrayList<>(Arrays.asList(1, 4, 5, 2, 3));
 comparator = new SequencesComparator<>(list1, list2);
 }

 @Test
 public void testConstructorWithTwoLists() {
 Comparator<? super Integer> equator = DefaultEquator.defaultEquator();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
 assertNotNull(comparator.getScript());
 }

 @Test
 public void testConstructorWithTwoListsAndCustomEquator() {
 Comparator<? super Integer> customEquator = new Equator<Integer>() {
 @Override
 public boolean equate(Object o1, Object o2) {
 return ((Integer)o1).intValue() < ((Integer)o2).intValue();
 }
 };
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, customEquator);
 assertNotNull(comparator.getScript());
 }

 @Test(expected = NullPointerException.class)
 public void testNullSequence1() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(null, list2);
 }

 @Test(expected = NullPointerException.class)
 public void testNullSequence2() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, null);
 }

 @Test
 public void testGetScript() {
 EditScript<Integer> script = comparator.getScript();
 assertNotNull(script);
 }

 @Test
 public void testEmptySequences() {
 List<Integer> emptyList1 = new ArrayList<>();
 List<Integer> emptyList2 = new ArrayList<>();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(emptyList1, emptyList2);
 EditScript<Integer> script = comparator.getScript();
 assertTrue(script.isEmpty());
 }

 @Test
 public void testEquivalentSequences() {
 List<Integer> list3 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 List<Integer> list4 = new ArrayList<>(list3);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list3, list4);
 EditScript<Integer> script = comparator.getScript();
 assertTrue(script.isEmpty());
 }

 @Test
 public void testSequencesWithDifferentLengths() {
 List<Integer> shorterList = new ArrayList<>(Arrays.asList(1, 2, 3));
 List<Integer> longerList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 SequencesComparator<Integer> comparator = new SequencesComparator<>(shorterList, longerList);
 EditScript<Integer> script = comparator.getScript();
 List<EditCommand<Integer>> commands = script.getCommands();
 assertEquals(3, commands.size());
 assertEquals(commands.get(0).getClass(), DeleteCommand.class);
 assertEquals(commands.get(1).getClass(), InsertCommand.class);
 assertEquals(commands.get(2).getClass(), InsertCommand.class);
 }

 @Test
 public void testSequencesWithDuplicates() {
 List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 5));
 List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 3, 4, 5, 5));
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 EditScript<Integer> script = comparator.getScript();
 List<EditCommand<Integer>> commands = script.getCommands();
 assertEquals(3, commands.size());
 assertEquals(commands.get(0).getClass(), DeleteCommand.class);
 assertEquals(commands.get(1).getClass(), InsertCommand.class);
 assertEquals(commands.get(2).getClass(), InsertCommand.class);
 }

 @Test
 public void testSequencesWithNegativeNumbers() {
 List<Integer> list1 = new ArrayList<>(Arrays.asList(-1, -2, -3, -4, -5));
 List<Integer> list2 = new ArrayList<>(Arrays.asList(-1, -4, -5, -2, -3));
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 EditScript<Integer> script = comparator.getScript();
 List<EditCommand<Integer>> commands = script.getCommands();
 assertEquals(3, commands.size());
 assertEquals(commands.get(0).getClass(), DeleteCommand.class);
 assertEquals(commands.get(1).getClass(), InsertCommand.class);
 assertEquals(commands.get(2).getClass(), InsertCommand.class);
 }

 @Test
 public void testSequencesWithNullElements() {
 List<Integer> list1 = new ArrayList<>(Arrays.asList(1, null, 3, 4, 5));
 List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 4, 5, 2, 3, null));
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 EditScript<Integer> script = comparator.getScript();
 List<EditCommand<Integer>> commands = script.getCommands();
 assertEquals(5, commands.size());
 assertEquals(commands.get(0).getClass(), DeleteCommand.class);
 assertEquals(commands.get(1).getClass(), DeleteCommand.class);
 assertEquals(commands.get(2).getClass(), InsertCommand.class);
 assertEquals(commands.get(3).getClass(), InsertCommand.class);
 assertEquals(commands.get(4).getClass(), InsertCommand.class);
 }

}