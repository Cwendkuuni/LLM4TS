package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;

public class SequencesComparatorTest {

 private List<Integer> sequence1;
 private List<Integer> sequence2;
 private Equator<Integer> equator;
 private SequencesComparator<Integer> comparator;

 @Before
 public void setup() {
 sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 sequence2 = Arrays.asList(1, 3, 4, 2, 5);
 equator = new DefaultEquator<>();
 comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 }

 @Test
 public void testConstructorWithTwoSequences() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 assertEquals(sequence1, comparator.getSequence1());
 assertEquals(sequence2, comparator.getSequence2());
 }

 @Test
 public void testConstructorWithTwoSequencesAndEquator() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 assertEquals(sequence1, comparator.getSequence1());
 assertEquals(sequence2, comparator.getSequence2());
 assertEquals(equator, comparator.getEquator());
 }

 @Test
 public void testGetScript() {
 EditScript<Integer> script = comparator.getScript();
 assertEquals(4, script.size());
 }

 @Test
 public void testGetScript_equalSequences() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4, 5);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> script = comparator.getScript();
 assertEquals(0, script.size());
 }

 @Test(expected = RuntimeException.class)
 public void testGetScript_internalError() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
 List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4, 5);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 comparator.getScript(); //should throw an exception
 }

 @Test
 public void testBuildScript_sameDiagonalSnake_noCommands() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> sequence2 = Arrays.asList(1, 3, 4, 2, 5);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> script = comparator.getScript();
 Snake middle = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
 assertEquals(null, middle);
 }

 @Test
 public void testBuildScript_snakeCrossesEndOfFirstSequence() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> sequence2 = Arrays.asList(1, 3, 4, 2, 5);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> script = comparator.getScript();
 Snake middle = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
 assertNotNull(middle);
 assertEquals(sequence1.get(sequence1.size() - 1), middle.getEnd());
 assertEquals(sequence2.get(sequence2.size() - 1), middle.getStart());
 }

 @Test
 public void testBuildScript_snakeCrossesEndOfSecondSequence() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> sequence2 = Arrays.asList(1, 3, 4, 2, 5);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> script = comparator.getScript();
 Snake middle = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
 assertNotNull(middle);
 assertEquals(sequence1.get(sequence1.size() - 1), middle.getStart());
 assertEquals(sequence2.get(sequence2.size() - 1), middle.getEnd());
 }

 @Test
 public void testBuildScript_snakeCrossesMiddleOfBothSequences() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> sequence2 = Arrays.asList(1, 3, 4, 2, 5);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> script = comparator.getScript();
 Snake middle = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
 assertNotNull(middle);
 assertEquals(sequence1.get(3), middle.getStart());
 assertEquals(sequence2.get(1), middle.getEnd());
 }

 @Test
 public void testBuildScript_snakeCrossesMiddleOfFirstSequence() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> sequence2 = Arrays.asList(1, 3, 4, 2, 5);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> script = comparator.getScript();
 Snake middle = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
 assertNotNull(middle);
 Snake subMiddle = comparator.getMiddleSnake(3, 2, 1, 4);
 assertNotNull(subMiddle);
 assertEquals(sequence1.get(3), subMiddle.getEnd());
 assertEquals(sequence2.get(1), subMiddle.getStart());
 }

 @Test
 public void testBuildScript_snakeCrossesMiddleOfSecondSequence() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> sequence2 = Arrays.asList(1, 3, 4, 2, 5);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> script = comparator.getScript();
 Snake middle = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
 assertNotNull(middle);
 Snake subMiddle = comparator.getMiddleSnake(4, 1, 3, 5);
 assertNotNull(subMiddle);
 assertEquals(sequence1.get(4), subMiddle.getStart());
 assertEquals(sequence2.get(3), subMiddle.getEnd());
 }

 @Test
 public void testBuildScript_snakeDoesNotCrossDiagonal() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> sequence2 = Arrays.asList(1, 3, 4, 2, 5);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> script = comparator.getScript();
 Snake middle = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
 assertNull(middle);
 }

 @Test
 public void testBuildScript_leftDiagonalSnake_commands() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> sequence2 = Arrays.asList(1, 3, 4, 2, 5);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> script = comparator.getScript();
 Snake middle = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
 assertNull(middle);
 EditScript<Integer> expectedScript = new EditScript<>();
 expectedScript.append(new DeleteCommand<>(sequence1.get(0)));
 expectedScript.append(new InsertCommand<>(sequence2.get(0)));
 expectedScript.append(new KeepCommand<>(sequence1.get(1)));
 expectedScript.append(new KeepCommand<>(sequence1.get(2)));
 expectedScript.append(new KeepCommand<>(sequence1.get(3)));
 expectedScript.append(new KeepCommand<>(sequence1.get(4)));
 expectedScript.append(new KeepCommand<>(sequence2.get(1)));
 expectedScript.append(new KeepCommand<>(sequence2.get(2)));
 expectedScript.append(new KeepCommand<>(sequence2.get(3)));
 expectedScript.append(new KeepCommand<>(sequence2.get(4)));
 assertEquals(expectedScript, script);
 }

 @Test
 public void testBuildScript_rightDiagonalSnake_commands() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> sequence2 = Arrays.asList(1, 3, 4, 2, 5);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> script = comparator.getScript();
 Snake middle = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
 assertNull(middle);
 EditScript<Integer> expectedScript = new EditScript<>();
 expectedScript.append(new KeepCommand<>(sequence1.get(0)));
 expectedScript.append(new KeepCommand<>(sequence1.get(1)));
 expectedScript.append(new KeepCommand<>(sequence1.get(2)));
 expectedScript.append(new DeleteCommand<>(sequence1.get(3)));
 expectedScript.append(new InsertCommand<>(sequence2.get(4)));
 expectedScript.append(new KeepCommand<>(sequence2.get(3)));
 expectedScript.append(new KeepCommand<>(sequence2.get(2)));
 expectedScript.append(new KeepCommand<>(sequence2.get(1)));
 expectedScript.append(new KeepCommand<>(sequence2.get(0)));
 assertEquals(expectedScript, script);
 }
}