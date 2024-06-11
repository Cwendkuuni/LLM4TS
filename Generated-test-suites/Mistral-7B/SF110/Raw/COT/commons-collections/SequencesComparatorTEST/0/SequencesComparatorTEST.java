package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;

public class SequencesComparatorTest<T> {

 private List<T> sequence1;
 private List<T> sequence2;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 List<Integer> list1 = new ArrayList<>();
 list1.add(1);
 list1.add(2);
 list1.add(3);
 this.sequence1 = Collections.unmodifiableList(list1);

 List<Integer> list2 = new ArrayList<>();
 list2.add(1);
 list2.add(3);
 list2.add(2);
 this.sequence2 = Collections.unmodifiableList(list2);
 }

 @Test
 public void testSequencesComparatorConstructorWithLists() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(this.sequence1, this.sequence2);
 assertNotNull(comparator.getScript());
 }

 @Test
 public void testSequencesComparatorConstructorWithListsAndEquator() {
 Equator<Integer> equator = new Equator<Integer>() {
 @Override
 public boolean equate(Object arg0, Object arg1) {
 return ((Comparable<Integer>)arg0).compareTo((Comparable<Integer>)arg1) == 0;
 }
 };
 SequencesComparator<Integer> comparator = new SequencesComparator<>(this.sequence1, this.sequence2, equator);
 assertNotNull(comparator.getScript());
 }

 @Test(expected = NullPointerException.class)
 public void testSequencesComparatorNullList1() {
 List<Integer> nullList1 = null;
 SequencesComparator<Integer> comparator = new SequencesComparator<>(nullList1, this.sequence2);
 fail("Should throw NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testSequencesComparatorNullList2() {
 List<Integer> nullList2 = null;
 SequencesComparator<Integer> comparator = new SequencesComparator<>(this.sequence1, nullList2);
 fail("Should throw NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testSequencesComparatorNullEquator() {
 Equator<Integer> nullEquator = null;
 SequencesComparator<Integer> comparator = new SequencesComparator<>(this.sequence1, this.sequence2, nullEquator);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testGetScript() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(this.sequence1, this.sequence2);
 EditScript<Integer> script = comparator.getScript();
 assertNotNull(script);
 }

 @Test
 public void testGetScriptEmptySequences() {
 List<Integer> emptyList1 = Collections.emptyList();
 List<Integer> emptyList2 = Collections.emptyList();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(emptyList1, emptyList2);
 EditScript<Integer> script = comparator.getScript();
 assertTrue(script.isEmpty());
 }

 @Test
 public void testGetScriptSameSequences() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(this.sequence1, this.sequence1);
 EditScript<Integer> script = comparator.getScript();
 assertTrue(script.isEmpty());
 }

 @Test
 public void testGetScriptDifferentSequences() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(this.sequence1, this.sequence2);
 EditScript<Integer> script = comparator.getScript();
 assertFalse(script.isEmpty());
 }

 @Test
 public void testBuildScriptSimpleCase() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(this.sequence1, this.sequence2);
 EditScript<Integer> script = comparator.getScript();
 KeepCommand<Integer> command = (KeepCommand<Integer>)script.getCommand(0);
 assertEquals(this.sequence1.get(0), command.getObject());
 DeleteCommand<Integer> command1 = (DeleteCommand<Integer>)script.getCommand(1);
 assertEquals(this.sequence1.get(0), command1.getObject());
 InsertCommand<Integer> command2 = (InsertCommand<Integer>)script.getCommand(2);
 assertEquals(this.sequence2.get(0), command2.getObject());
 }

 @Test
 public void testBuildScriptComplexCase() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(this.sequence1, this.sequence2);
 EditScript<Integer> script = comparator.getScript();
 KeepCommand<Integer> command1 = (KeepCommand<Integer>)script.getCommand(0);
 assertEquals(this.sequence1.get(0), command1.getObject());
 KeepCommand<Integer> command2 = (KeepCommand<Integer>)script.getCommand(1);
 assertEquals(this.sequence1.get(1), command2.getObject());
 DeleteCommand<Integer> command3 = (DeleteCommand<Integer>)script.getCommand(2);
 assertEquals(this.sequence1.get(0), command3.getObject());
 InsertCommand<Integer> command4 = (InsertCommand<Integer>)script.getCommand(3);
 assertEquals(this.sequence2.get(1), command4.getObject());
 KeepCommand<Integer> command5 = (KeepCommand<Integer>)script.getCommand(4);
 assertEquals(this.sequence1.get(1), command5.getObject());
 KeepCommand<Integer> command6 = (KeepCommand<Integer>)script.getCommand(5);
 assertEquals(this.sequence2.get(2), command6.getObject());
 DeleteCommand<Integer> command7 = (DeleteCommand<Integer>)script.getCommand(6);
 assertEquals(this.sequence2.get(1), command7.getObject());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetCommandInvalidIndex() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(this.sequence1, this.sequence2);
 EditScript<Integer> script = comparator.getScript();
 script.getCommand(script.size() + 1);
 fail("Should throw IndexOutOfBoundsException");
 }

 @Test
 public void testSnakeBuildSnakeSimpleCase() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(this.sequence1, this.sequence2);
 Snake snake = comparator.buildSnake(0, 0, this.sequence1.size(), this.sequence2.size());
 assertEquals(0, snake.getStart());
 assertEquals(1, snake.getEnd());
 assertEquals(0, snake.getDiag());
 }

 @Test
 public void testSnakeBuildSnakeComplexCase() {
 List<Integer> list3 = new ArrayList<>();
 list3.add(1);
 list3.add(3);
 list3.add(2);
 List<Integer> list4 = new ArrayList<>();
 list4.add(1);
 list4.add(2);
 list4.add(3);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(this.sequence1, this.sequence2, DefaultEquator.defaultEquator());
 Snake snake = comparator.buildSnake(0, 1, this.sequence1.size(), this.sequence2.size());
 assertEquals(0, snake.getStart());
 assertEquals(2, snake.getEnd());
 assertEquals(1, snake.getDiag());
 }

 @Test
 public void testGetMiddleSnakeNullCase() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(this.sequence1, this.sequence2);
 assertNull(comparator.getMiddleSnake(-1, this.sequence1.size(), 0, this.sequence2.size()));
 }

 @Test
 public void testGetMiddleSnakeEmptyCase() {
 List<Integer> emptyList1 = Collections.emptyList();
 List<Integer> emptyList2 = Collections.emptyList();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(emptyList1, emptyList2);
 assertNull(comparator.getMiddleSnake(0, 0, 0, 0));
 }

 @Test
 public void testGetMiddleSnakeSimpleCase() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(this.sequence1, this.sequence2);
 Snake snake = comparator.getMiddleSnake(0, this.sequence1.size(), 0, this.sequence2.size());
 assertNull(snake);
 }

 @Test
 public void testGetMiddleSnakeComplexCase() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(this.sequence1, this.sequence2);
 Snake snake = comparator.getMiddleSnake(0, this.sequence1.size(), 1, this.sequence2.size());
 assertEquals(1, snake.getStart());
 assertEquals(2, snake.getEnd());
 assertEquals(1, snake.getDiag());
 }

 @Test
 public void testBuildScriptWithMiddleSnake() {
 List<Integer> list3 = new ArrayList<>();
 list3.add(1);
 list3.add(3);
 list3.add(2);
 List<Integer> list4 = new ArrayList<>();
 list4.add(1);
 list4.add(2);
 list4.add(3);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(this.sequence1, this.sequence2, DefaultEquator.defaultEquator());
 EditScript<Integer> script = comparator.getScript();
 Snake snake = comparator.getMiddleSnake(0, this.sequence1.size(), 1, this.sequence2.size());
 assertNotNull(snake);
 comparator.buildScript(0, this.sequence1.size(), 0, this.sequence2.size(), script);
 KeepCommand<Integer> command1 = (KeepCommand<Integer>)script.getCommand(0);
 assertEquals(this.sequence1.get(0), command1.getObject());
 KeepCommand<Integer> command2 = (KeepCommand<Integer>)script.getCommand(1);
 assertEquals(this.sequence1.get(1), command2.getObject());
 DeleteCommand<Integer> command3 = (DeleteCommand<Integer>)script.getCommand(2);
 assertEquals(this.sequence1.get(0), command3.getObject());
 InsertCommand<Integer> command4 = (InsertCommand<Integer>)script.getCommand(3);
 assertEquals(this.sequence2.get(1), command4.getObject());
 KeepCommand<Integer> command5 = (KeepCommand<Integer>)script.getCommand(4);
 assertEquals(this.sequence1.get(1), command5.getObject());
 KeepCommand<Integer> command6 = (KeepCommand<Integer>)script.getCommand(5);
 assertEquals(this.sequence2.get(2), command6.getObject());
 DeleteCommand<Integer> command7 = (DeleteCommand<Integer>)script.getCommand(6);
 assertEquals(this.sequence2.get(1), command7.getObject());
 }
}