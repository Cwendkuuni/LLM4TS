package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;

public class SequencesComparatorTest {

 private List<Integer> createList(Integer[] array) {
 CollectionUtils.addAll(new ArrayList<>(), array);
 return (List<Integer>) new ArrayList<>(array.length);
 }

 @Test
 public void testConstructorWithTwoLists() {
 List<Integer> list1 = createList(new Integer[] { 1, 2, 3, 4, 5 });
 List<Integer> list2 = createList(new Integer[] { 1, 2, 3, 5, 4, 6 });
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 }

 @Test
 public void testConstructorWithTwoListsAndEquator() {
 List<Integer> list1 = createList(new Integer[] { 1, 2, 3, 4, 5 });
 List<Integer> list2 = createList(new Integer[] { 1, 2, 3, 5, 4, 6 });
 Equator<Integer> equator = new Equator<Integer>() {
 @Override
 public boolean equate(Object o1, Object o2) {
 return ((Integer)o1).equals((Integer)o2);
 }
 };
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
 }

 @Test(expected = RuntimeException.class)
 public void testConstructorWithNullLists() {
 List<Integer> list1 = null;
 List<Integer> list2 = null;
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 }

 @Test(expected = RuntimeException.class)
 public void testConstructorWithNullList1() {
 List<Integer> list1 = null;
 List<Integer> list2 = createList(new Integer[] { 1, 2, 3, 4, 5 });
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 }

 @Test(expected = RuntimeException.class)
 public void testConstructorWithNullList2() {
 List<Integer> list1 = createList(new Integer[] { 1, 2, 3, 4, 5 });
 List<Integer> list2 = null;
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 }

 @Test
 public void testGetScript() {
 List<Integer> list1 = createList(new Integer[] { 1, 2, 3, 4, 5 });
 List<Integer> list2 = createList(new Integer[] { 1, 2, 3, 5, 4, 6 });
 Equator<Integer> equator = new Equator<Integer>() {
 @Override
 public boolean equate(Object o1, Object o2) {
 return ((Integer)o1).equals((Integer)o2);
 }
 };
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
 EditScript<Integer> script = comparator.getScript();
 assertEquals(11, script.size());
 }

 @Test
 public void testBuildScriptWithEmptyLists() {
 List<Integer> list1 = new ArrayList<>();
 List<Integer> list2 = new ArrayList<>();
 Equator<Integer> equator = new Equator<Integer>() {
 @Override
 public boolean equate(Object o1, Object o2) {
 return ((Integer)o1).equals((Integer)o2);
 }
 };
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
 EditScript<Integer> script = comparator.getScript();
 assertTrue(script.isEmpty());
 }

 @Test
 public void testBuildScriptWithEqualLists() {
 List<Integer> list1 = createList(new Integer[] { 1, 2, 3, 4, 5 });
 List<Integer> list2 = createList(new Integer[] { 1, 2, 3, 4, 5 });
 Equator<Integer> equator = new Equator<Integer>() {
 @Override
 public boolean equate(Object o1, Object o2) {
 return ((Integer)o1).equals((Integer)o2);
 }
 };
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
 EditScript<Integer> script = comparator.getScript();
 assertTrue(script.isEmpty());
 }

 @Test
 public void testBuildScriptWithIdenticalSequences() {
 List<Integer> list1 = createList(new Integer[] { 1, 2, 3, 4, 5 });
 List<Integer> list2 = list1;
 Equator<Integer> equator = new Equator<Integer>() {
 @Override
 public boolean equate(Object o1, Object o2) {
 return ((Integer)o1).equals((Integer)o2);
 }
 };
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
 EditScript<Integer> script = comparator.getScript();
 assertTrue(script.isEmpty());
 }

 @Test
 public void testBuildScriptWithDifferentLengths() {
 List<Integer> list1 = createList(new Integer[] { 1, 2, 3, 4, 5 });
 List<Integer> list2 = createList(new Integer[] { 1, 2, 3, 5, 4, 6, 7 });
 Equator<Integer> equator = new Equator<Integer>() {
 @Override
 public boolean equate(Object o1, Object o2) {
 return ((Integer)o1).equals((Integer)o2);
 }
 };
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
 EditScript<Integer> script = comparator.getScript();
 assertEquals(5, script.size());
 assertEquals(KeepCommand.class, script.get(0).getClass());
 assertEquals(KeepCommand.class, script.get(1).getClass());
 assertEquals(DeleteCommand.class, script.get(2).getClass());
 assertEquals(InsertCommand.class, script.get(3).getClass());
 assertEquals(KeepCommand.class, script.get(4).getClass());
 }

 @Test
 public void testBuildScriptWithEmptyListAtEnd() {
 List<Integer> list1 = createList(new Integer[] { 1, 2, 3, 4, 5 });
 List<Integer> list2 = createList(new Integer[] { 1, 2, 3, 5, 4 });
 Equator<Integer> equator = new Equator<Integer>() {
 @Override
 public boolean equate(Object o1, Object o2) {
 return ((Integer)o1).equals((Integer)o2);
 }
 };
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
 EditScript<Integer> script = comparator.getScript();
 assertEquals(5, script.size());
 assertEquals(KeepCommand.class, script.get(0).getClass());
 assertEquals(KeepCommand.class, script.get(1).getClass());
 assertEquals(DeleteCommand.class, script.get(2).getClass());
 assertEquals(InsertCommand.class, script.get(3).getClass());
 assertEquals(KeepCommand.class, script.get(4).getClass());
 }

 @Test
 public void testBuildScriptWithEmptyListAtBeginning() {
 List<Integer> list1 = createList(new Integer[] { 2, 3, 4, 5 });
 List<Integer> list2 = createList(new Integer[] { 1, 2, 3, 4, 5 });
 Equator<Integer> equator = new Equator<Integer>() {
 @Override
 public boolean equate(Object o1, Object o2) {
 return ((Integer)o1).equals((Integer)o2);
 }
 };
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
 EditScript<Integer> script = comparator.getScript();
 assertEquals(5, script.size());
 assertEquals(InsertCommand.class, script.get(0).getClass());
 assertEquals(KeepCommand.class, script.get(1).getClass());
 assertEquals(KeepCommand.class, script.get(2).getClass());
 assertEquals(KeepCommand.class, script.get(3).getClass());
 assertEquals(KeepCommand.class, script.get(4).getClass());
 }

 @Test
 public void testBuildScriptWithIdenticalSubsequences() {
 List<Integer> list1 = createList(new Integer[] { 1, 2, 3, 4, 5, 6 });
 List<Integer> list2 = createList(new Integer[] { 1, 2, 3, 5, 4, 6, 7, 5, 4, 5, 6 });
 Equator<Integer> equator = new Equator<Integer>() {
 @Override
 public boolean equate(Object o1, Object o2) {
 return ((Integer)o1).equals((Integer)o2);
 }
 };
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
 EditScript<Integer> script = comparator.getScript();
 assertEquals(10, script.size());
 }

 @Test(expected = RuntimeException.class)
 public void testBuildScriptWithNullList1() {
 List<Integer> list1 = null;
 List<Integer> list2 = createList(new Integer[] { 1, 2, 3, 4, 5 });
 Equator<Integer> equator = new Equator<Integer>() {
 @Override
 public boolean equate(Object o1, Object o2) {
 return ((Integer)o1).equals((Integer)o2);
 }
 };
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
 }

 @Test(expected = RuntimeException.class)
 public void testBuildScriptWithNullList2() {
 List<Integer> list1 = createList(new Integer[] { 1, 2, 3, 4, 5 });
 List<Integer> list2 = null;
 Equator<Integer> equator = new Equator<Integer>() {
 @Override
 public boolean equate(Object o1, Object o2) {
 return ((Integer)o1).equals((Integer)o2);
 }
 };
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
 }

 @Test(expected = RuntimeException.class)
 public void testBuildScriptWithNullEquator() {
 List<Integer> list1 = createList(new Integer[] { 1, 2, 3, 4, 5 });
 List<Integer> list2 = createList(new Integer[] { 1, 2, 3, 5, 4, 6 });
 Equator<Integer> equator = null;
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
 }

}