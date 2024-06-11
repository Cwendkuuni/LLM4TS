package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;

public class SequencesComparatorTest<T> {

 private List<T> sequence1;
 private List<T> sequence2;
 private Equator<? super T> equator;
 private SequencesComparator<T> comparator;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 sequence1 = new ArrayList<>();
 sequence2 = new ArrayList<>();
 equator = DefaultEquator.defaultEquator();
 }

 @Test
 public void testSequencesComparatorWithSameLists() {
 sequence1.addAll(sequence2);
 comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<T> script = comparator.getScript();
 assertEquals(0, script.size());
 }

 @Test
 public void testSequencesComparatorWithEqualElementsButDifferentOrder() {
 sequence1.add(1);
 sequence1.add(2);
 sequence2.add(2);
 sequence2.add(1);
 comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<T> script = comparator.getScript();
 assertEquals(2, script.size());
 assertInstanceOf(InsertCommand.class, script.getCommand(0));
 assertInstanceOf(DeleteCommand.class, script.getCommand(1));
 }

 @Test
 public void testSequencesComparatorWithDifferentElements() {
 sequence1.add(1);
 sequence1.add(2);
 sequence2.add(3);
 sequence2.add(2);
 comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<T> script = comparator.getScript();
 assertEquals(3, script.size());
 assertInstanceOf(InsertCommand.class, script.getCommand(0));
 assertInstanceOf(DeleteCommand.class, script.getCommand(1));
 assertInstanceOf(InsertCommand.class, script.getCommand(2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSequencesComparatorWithNullLists() {
 comparator = new SequencesComparator<>(null, sequence2, equator);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSequencesComparatorWithNullEquator() {
 comparator = new SequencesComparator<>(sequence1, null, equator);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSequencesComparatorWithEmptyLists() {
 sequence1.clear();
 sequence2.clear();
 comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 }

 @Test
 public void testBuildSnakeEdgeCases() {
 comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(3, 2, 1), equator);
 Snake snake = comparator.getMiddleSnake(0, 3, 0, 3);
 assertNotNull(snake);
 assertEquals(1, snake.getStart());
 assertEquals(2, snake.getEnd());
 assertEquals(1, snake.getDiag());

 snake = comparator.getMiddleSnake(0, 3, 0, 2);
 assertNull(snake);

 snake = comparator.getMiddleSnake(0, 3, 0, 4);
 assertNull(snake);

 snake = comparator.getMiddleSnake(0, 1, 0, 1);
 assertNull(snake);

 snake = comparator.getMiddleSnake(0, 3, 2, 3);
 assertNull(snake);
 }

 @Test
 public void testBuildScriptEdgeCases() {
 sequence1.clear();
 sequence1.add(1);
 sequence2.add(null);
 comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<T> script = comparator.getScript();
 assertEquals(1, script.size());
 assertInstanceOf(DeleteCommand.class, script.getCommand(0));

 sequence1.clear();
 sequence2.add(null);
 comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 script = comparator.getScript();
 assertEquals(0, script.size());

 sequence1.addAll(Arrays.asList(1, 2, 3));
 sequence2.addAll(Arrays.asList(1, 2, 3, 3, 3));
 comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 script = comparator.getScript();
 assertEquals(3, script.size());
 assertInstanceOf(InsertCommand.class, script.getCommand(0));
 assertInstanceOf(DeleteCommand.class, script.getCommand(1));
 assertInstanceOf(InsertCommand.class, script.getCommand(2));
 }

 @Test
 public void testBuildScriptExceptionHandling() {
 sequence1.addAll(Arrays.asList(1, 2, 3));
 sequence2.addAll(Arrays.asList(1, 2, null));
 comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 try {
 EditScript<T> script = comparator.getScript();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }

 sequence1.clear();
 sequence2.addAll(Arrays.asList(1, 2, null));
 comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 try {
 EditScript<T> script = comparator.getScript();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
}

}