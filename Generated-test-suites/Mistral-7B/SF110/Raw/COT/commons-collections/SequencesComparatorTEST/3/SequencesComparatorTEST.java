package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.apache.commons.collections4.test.equality.EqualsTester;
import org.junit.Before;
import org.junit.Test;

public class SequencesComparatorTest {

 private List<Integer> list1;
 private List<Integer> list2;
 private List<Integer> list3;
 private SequencesComparator<Integer> comparator;

 @Before
 public void setup() {
 list1 = Arrays.asList(1, 2, 3, 4, 5);
 list2 = Arrays.asList(1, 2, 6, 4, 5, 7);
 list3 = Arrays.asList(1, 2, 3, 4, 5, 5);
 comparator = new SequencesComparator<>(list1, list2);
 }

 @Test
 public void testSequencesComparatorConstructorWithLists() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 assertNotNull(comparator.getScript());
 }

 @Test
 public void testSequencesComparatorConstructorWithListsAndEquator() {
 Equator<Integer> equator = new Equator<Integer>() {
 @Override
 public boolean equate(Object arg0, Object arg1) {
 return arg0.equals(arg1);
 }
 };
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
 assertNotNull(comparator.getScript());
 }

 @Test
 public void testGetScript() {
 EditScript<Integer> script = comparator.getScript();
 assertNotNull(script);
 }

 @Test(expected = RuntimeException.class)
 public void testGetScriptWithInternalError() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1), Arrays.asList(1));
 EditScript<Integer> script = comparator.getScript();
 }

 @Test
 public void testBuildScript() {
 EditScript<Integer> script = comparator.getScript();
 int index = 0;
 for (EditCommand editCommand : script.getCommands()) {
 switch (editCommand.getClass().getSimpleName()) {
 case "KeepCommand":
 assertEquals(list1.get(index), editCommand.getObject());
 index++;
 break;
 case "DeleteCommand":
 assertEquals(list1.get(index - 1), editCommand.getObject());
 index--;
 break;
 case "InsertCommand":
 assertEquals(list2.get(index), editCommand.getObject());
 index++;
 break;
 default:
 fail("Unexpected command: " + editCommand.getClass().getSimpleName());
 }
 }
 }

 @Test
 public void testGetMiddleSnake() {
 Snake snake = comparator.getMiddleSnake(0, list1.size(), 0, list2.size());
 assertNotNull(snake);
 }

 @Test(expected = RuntimeException.class)
 public void testGetMiddleSnakeWithInternalError() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1), Arrays.asList(1));
 Snake snake = comparator.getMiddleSnake(0, 1, 0, 1);
 }

 @Test
 public void testEquals() {
 EqualsTester.forClass(SequencesComparator.class).addEqualityMethod("equalsNull").testEquals();
 EqualsTester.forClass(SequencesComparator.class).addEqualityMethod("areSameSequence").testEquals();
 }

}