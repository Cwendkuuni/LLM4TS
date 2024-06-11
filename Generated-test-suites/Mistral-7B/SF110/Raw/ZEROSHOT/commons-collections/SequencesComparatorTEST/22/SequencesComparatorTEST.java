package org.apache.commons.collections4.test.sequence;

import static org.junit.Assert.*;

import java.util.*;

import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.apache.commons.collections4.sequence.*;
import org.apache.commons.collections4.test.equality.*;
import org.junit.*;

public class SequencesComparatorTest {

 private List<Integer> list1;
 private List<Integer> list2;
 private List<Integer> expectedResult;
 private SequencesComparator<Integer> comparator;

 @Before
 public void setup() {
 list1 = new ArrayList<>();
 list1.add(1);
 list1.add(2);
 list1.add(3);
 list1.add(4);

 list2 = new ArrayList<>();
 list2.add(1);
 list2.add(3);
 list2.add(2);
 list2.add(4);

 expectedResult = new ArrayList<>();
 expectedResult.add(1);
 expectedResult.add(2);
 expectedResult.add(3);
 expectedResult.add(4);
 }

 @Test
 public void testSequencesComparatorWithDefaultEquator() {
 comparator = new SequencesComparator<>(list1, list2);
 EditScript<Integer> script = comparator.getScript();
 List<EditCommand<Integer>> commands = script.getCommands();
 List<Integer> actualResult = new ArrayList<>();

 for (EditCommand<Integer> command : commands) {
 if (command instanceof InsertCommand) {
 actualResult.add(command.getObject());
 }
 }

 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSequencesComparatorWithCustomEquator() {
 Equator<Integer> customEquator = new Equator<Integer>() {
 @Override
 public boolean equate(Object o1, Object o2) {
 return new DefaultComparator().compare((Comparable)o1, (Comparable)o2) == 0;
 }
 };
 comparator = new SequencesComparator<>(list1, list2, customEquator);
 EditScript<Integer> script = comparator.getScript();
 List<EditCommand<Integer>> commands = script.getCommands();
 List<Integer> actualResult = new ArrayList<>();

 for (EditCommand<Integer> command : commands) {
 if (command instanceof InsertCommand) {
 actualResult.add(command.getObject());
 }
 }

 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = RuntimeException.class)
 public void testInternalError() {
 comparator = new SequencesComparator<>(Collections.emptyList(), Collections.emptyList());
 comparator.getScript();
 }
}