package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;

import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.apache.commons.collections4.test.EditScriptTestCase;

public class SequencesComparatorTest {

 private List<Integer> list1;
 private List<Integer> list2;
 private List<Integer> expectedResult;

 @Before
 public void setUp() {
 list1 = Arrays.asList(1, 2, 3, 4, 5);
 list2 = Arrays.asList(4, 5, 3, 2, 1);
 expectedResult = Arrays.asList(1, 2, 3, 5, 4);
 }

 @Test
 public void testSequencesComparator() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 EditScript<Integer> script = comparator.getScript();
 EditScriptTestCase.assertScriptEquals(expectedResult, script);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSequencesComparatorNullLists() {
 List<Integer> nullList1 = null;
 List<Integer> nullList2 = null;
 assertNull(new SequencesComparator<>(nullList1, nullList2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSequencesComparatorNullSequence1() {
 List<Integer> nullSequence1 = null;
 List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4, 5);
 assertNull(new SequencesComparator<>(nullSequence1, sequence2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSequencesComparatorNullSequence2() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> nullSequence2 = null;
 assertNull(new SequencesComparator<>(sequence1, nullSequence2));
 }

 @Test
 public void testSequencesComparatorDefaultEquator() {
 List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> list2 = Arrays.asList(4, 5, 3, 2, 1);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 EditScript<Integer> script = comparator.getScript();
 EditScriptTestCase.assertScriptEquals(expectedResult, script);
 }

 @Test
 public void testSequencesComparatorCustomEquator() {
 List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> list2 = Arrays.asList(4, 5, 3, 2, 1);
 Equator<Integer> customEquator = new Equator<Integer>() {
 @Override
 public boolean equate(Object o1, Object o2) {
 return ((Integer)o1).intValue() > ((Integer)o2).intValue();
 }
 };
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, customEquator);
 EditScript<Integer> script = comparator.getScript();
 List<Integer> result = new ArrayList<>();
 for (Object cmd : script.getCommands()) {
 if (cmd instanceof InsertCommand) {
 result.add(((InsertCommand<Integer>)cmd).getElement());
 } else if (cmd instanceof DeleteCommand) {
 // no need to process delete commands
 } else if (cmd instanceof KeepCommand) {
 // no need to process keep commands
 }
 }
 Collections.reverse(result);
 assertArrayEquals(expectedResult.toArray(), result.toArray());
 }
}