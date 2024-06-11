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
 list2 = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
 comparator = new SequencesComparator<>(list1, list2);
 }

 @Test
 public void testConstructorWithTwoLists() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 assertNotNull(comparator.getScript());
 }

 @Test
 public void testConstructorWithTwoListsAndEquator() {
 Equator<Integer> equator = new Equator<Integer>() {
 @Override
 public boolean equate(Integer o1, Integer o2) {
 return o1.equals(o2);
 }
 };
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
 assertNotNull(comparator.getScript());
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullList1() {
 List<Integer> nullList1 = null;
 List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 SequencesComparator<Integer> comparator = new SequencesComparator<>(nullList1, list2);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullList2() {
 List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 List<Integer> nullList2 = null;
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, nullList2);
 }

 @Test
 public void testGetScript() {
 EditScript<Integer> script = comparator.getScript();
 assertNotNull(script);
 }

 @Test
 public void testGetScriptWhenListsAreEqual() {
 List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 EditScript<Integer> script = comparator.getScript();
 EqualEditScriptAssert.assertEqualScripts(script, new EditScript<Integer>() {
 @Override
 public void applyTo(List<Integer> sequence) {
 // do nothing
 }
 });
 }

 @Test
 public void testGetScriptWhenListsAreDifferent() {
 Equator<Integer> equator = new Equator<Integer>() {
 @Override
 public boolean equate(Integer o1, Integer o2) {
 return o1.equals(o2);
 }
 };
 List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 List<Integer> list2 = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
 EditScript<Integer> script = comparator.getScript();
 InsertCommand<Integer> firstInsert = (InsertCommand<Integer>)script.getCommand(0);
 DeleteCommand<Integer> firstDelete = (DeleteCommand<Integer>)script.getCommand(1);
 KeepCommand<Integer> firstKeep = (KeepCommand<Integer>)script.getCommand(2);
 KeepCommand<Integer> secondKeep = (KeepCommand<Integer>)script.getCommand(3);
 KeepCommand<Integer> thirdKeep = (KeepCommand<Integer>)script.getCommand(4);
 InsertCommand<Integer> secondInsert = (InsertCommand<Integer>)script.getCommand(5);
 DeleteCommand<Integer> secondDelete = (DeleteCommand<Integer>)script.getCommand(6);
 KeepCommand<Integer> fourthKeep = (KeepCommand<Integer>)script.getCommand(7);
 KeepCommand<Integer> fifthKeep = (KeepCommand<Integer>)script.getCommand(8);

 assertEquals(list1.get(0), firstKeep.getElement());
 assertEquals(list2.get(0), secondInsert.getElement());
 assertEquals(list1.get(1), firstKeep.getElement());
 assertEquals(list2.get(1), secondDelete.getElement());
 assertEquals(list1.get(2), thirdKeep.getElement());
 assertEquals(list2.get(4), secondInsert.getElement());
 assertEquals(list1.get(3), fourthKeep.getElement());
 assertEquals(list1.get(4), fifthKeep.getElement());
 }

 @Test(expected = RuntimeException.class)
 public void testGetScriptWhenInternalError() {
 List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, new Equator<Integer>() {
 @Override
 public boolean equate(Integer o1, Integer o2) {
 throw new RuntimeException();
 }
 });
 }

}