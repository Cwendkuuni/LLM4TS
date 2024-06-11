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
 list2 = new ArrayList<>(Arrays.asList(1, 2, 6, 3, 4, 5));
 comparator = new SequencesComparator<>(list1, list2);
 }

 @Test
 public void testConstructorWithTwoLists() {
 assertNotNull(comparator);
 assertEquals(5, comparator.sequence1.size());
 assertEquals(6, comparator.sequence2.size());
 }

 @Test
 public void testConstructorWithTwoListsAndDefaultEquator() {
 List<Integer> list1Default = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 List<Integer> list2Default = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
 SequencesComparator<Integer> comparatorDefault = new SequencesComparator<>(list1Default, list2Default);
 assertEquals(5, comparatorDefault.sequence1.size());
 assertEquals(6, comparatorDefault.sequence2.size());
 }

 @Test
 public void testGetScript() {
 EditScript<Integer> script = comparator.getScript();
 assertEquals(5, script.size());
 }

 @Test
 public void testBuildScriptNormalCase() {
 List<Integer> expected = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 EditScript<Integer> script = comparator.getScript();
 List<EditCommand<Integer>> commands = script.getCommands();
 List<Integer> result = new ArrayList<>();
 for (EditCommand<Integer> command : commands) {
 if (command instanceof KeepCommand) {
 result.add(((KeepCommand<Integer>)command).getElement());
 }
 }
 assertEquals(expected, result);
 }

 @Test(expected = RuntimeException.class)
 public void testGetMiddleSnakeInternalError() {
 comparator = new SequencesComparator<>(Arrays.asList(1, 2), Arrays.asList(1, 2));
 assertThrows(RuntimeException.class, () -> comparator.getMiddleSnake(0, 1, 0, 1));
 }

 @Test
 public void testGetMiddleSnakeSimpleCase() {
 Snake snake = comparator.getMiddleSnake(0, list1.size(), 0, list2.size());
 assertNull(snake);
 }

 @Test
 public void testGetMiddleSnakeDiagonalCase() {
 Snake snake = comparator.getMiddleSnake(0, list1.size(), 0, list2.size() + 1);
 assertNotNull(snake);
 assertEquals(0, snake.getStart());
 assertEquals(list1.size(), snake.getEnd());
 assertEquals(-1, snake.getDiag());
 }

 @Test
 public void testGetMiddleSnakeDiagonalCase2() {
 Snake snake = comparator.getMiddleSnake(list1.size(), list1.size() + 1, 0, list2.size());
 assertNotNull(snake);
 assertEquals(list1.size(), snake.getStart());
 assertEquals(list1.size() + 1, snake.getEnd());
 assertEquals(1, snake.getDiag());
 }

 @Test
 public void testGetMiddleSnakeEdgeCases() {
 comparator = new SequencesComparator<>(Arrays.asList(1, 2), Arrays.asList(1, 2, 3));
 Snake snake = comparator.getMiddleSnake(0, 1, 0, 2);
 assertNotNull(snake);
 assertEquals(0, snake.getStart());
 assertEquals(1, snake.getEnd());
 assertEquals(-1, snake.getDiag());

 comparator = new SequencesComparator<>(Arrays.asList(1, 2), Arrays.asList(1, 2, 3, 4));
 Snake snake1 = comparator.getMiddleSnake(0, 1, 0, 3);
 assertNull(snake1);

 comparator = new SequencesComparator<>(Arrays.asList(1, 2), Arrays.asList(1, 2, 3, 4, 5));
 Snake snake2 = comparator.getMiddleSnake(0, 1, 0, 4);
 assertNull(snake2);

 comparator = new SequencesComparator<>(Arrays.asList(1, 2), Arrays.asList(1, 2, 3, 4, 5, 6));
 Snake snake3 = comparator.getMiddleSnake(0, 1, 0, 5);
 assertNull(snake3);

 comparator = new SequencesComparator<>(Arrays.asList(1, 2), Arrays.asList(1, 2, 3, 4, 5, 6, 7));
 Snake snake4 = comparator.getMiddleSnake(0, 1, 0, 6);
 assertNull(snake4);
}

 @Test
 public void testBuildScriptEdgeCases() {
 // List1 is shorter than List2
 List<Integer> list1Shorter = Arrays.asList(1, 2);
 List<Integer> list2Longer = Arrays.asList(1, 2, 3, 4, 5, 6);
 SequencesComparator<Integer> comparatorShorterLonger = new SequencesComparator<>(list1Shorter, list2Longer);
 EditScript<Integer> script = comparatorShorterLonger.getScript();
 List<EditCommand<Integer>> commands = script.getCommands();
 List<Integer> result = new ArrayList<>();
 for (EditCommand<Integer> command : commands) {
 if (command instanceof DeleteCommand) {
 result.add(((DeleteCommand<Integer>)command).getElement());
 }
 }
 assertEquals(Arrays.asList(2, 3, 4, 5), result);

 // List1 is longer than List2
 List<Integer> list1Longer = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> list2Shorter = Arrays.asList(1, 2);
 SequencesComparator<Integer> comparatorLongerShorter = new SequencesComparator<>(list1Longer, list2Shorter);
 EditScript<Integer> script1 = comparatorLongerShorter.getScript();
 List<EditCommand<Integer>> commands1 = script1.getCommands();
 List<Integer> result1 = new ArrayList<>();
 for (EditCommand<Integer> command : commands1) {
 if (command instanceof InsertCommand) {
 result1.add(((InsertCommand<Integer>)command).getElement());
 }
 }
 assertEquals(Arrays.asList(3, 4), result1);
}

 @Test
 public void testBuildScriptExceptionHandling() {
 List<Integer> list1Null = null;
 List<Integer> list2 = Arrays.asList(1, 2, 3, 4, 5);
 try {
 new SequencesComparator<>(list1Null, list2);
 fail("Exception expected");
 } catch (NullPointerException e) {
 // Expected
 }

 List<Integer> list1Empty = new ArrayList<>();
 List<Integer> list2 = Arrays.asList(1, 2, 3, 4, 5);
 try {
 new SequencesComparator<>(list1Empty, list2);
 fail("Exception expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 List<Integer> list1 = Arrays.asList(1, 2);
 List<Integer> list2Null = null;
 try {
 new SequencesComparator<>(list1, list2Null);
 fail("Exception expected");
 } catch (NullPointerException e) {
 // Expected
 }
}

}