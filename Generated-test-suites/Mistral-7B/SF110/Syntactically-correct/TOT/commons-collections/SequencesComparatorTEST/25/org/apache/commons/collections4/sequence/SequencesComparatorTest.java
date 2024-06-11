package org.apache.commons.collections4.sequence;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.TestConstants;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class SequencesComparatorTest {

 @Test
 public void testEmptySequences() {
 List<Integer> sequence1 = Collections.emptyList();
 List<Integer> sequence2 = Collections.emptyList();
 Equator<Integer> equator = DefaultEquator.defaultEquator();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> script = comparator.getScript();
 assertEquals(0, script.size());
 }

 @Test
 public void testIdenticalSequences() {
 List<String> sequence1 = Arrays.asList("apple", "banana", "cherry");
 List<String> sequence2 = Arrays.asList("apple", "banana", "cherry");
 Equator<String> equator = DefaultEquator.defaultEquator();
 SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<String> script = comparator.getScript();
 assertEquals(0, script.size());
 }

 @Test
 public void testDifferentSizeSequences() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3);
 List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4);
 Equator<Integer> equator = DefaultEquator.defaultEquator();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> script = comparator.getScript();
 List<EditCommand<Integer>> commands = script.getCommands();
 assertEquals(3, commands.size());
 assertEquals(DeleteCommand.class, commands.get(0).getClass());
 assertEquals(InsertCommand.class, commands.get(1).getClass());
 assertEquals(KeepCommand.class, commands.get(2).getClass());
 }

 @Test
 public void testSequencesWithDuplicates() {
 List<String> sequence1 = Arrays.asList("apple", "banana", "cherry", "banana");
 List<String> sequence2 = Arrays.asList("apple", "banana", "cherry", "cherry");
 Equator<String> equator = DefaultEquator.defaultEquator();
 SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<String> script = comparator.getScript();
 List<EditCommand<String>> commands = script.getCommands();
 assertEquals(3, commands.size());
 assertEquals(DeleteCommand.class, commands.get(0).getClass());
 assertEquals(KeepCommand.class, commands.get(1).getClass());
 assertEquals(InsertCommand.class, commands.get(2).getClass());
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidSequenceSizes() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3);
 List<Integer> sequence2 = Arrays.asList(1, 2);
 Equator<Integer> equator = DefaultEquator.defaultEquator();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidEquator() {
 List<String> sequence1 = Arrays.asList("apple", "banana", "cherry");
 List<String> sequence2 = Arrays.asList("apple", "banana", "cherry");
 Equator<String> equator = (x, y) -> x.length() > y.length();
 SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 }

 @Test
 public void testSequencesWithNullValues() {
 List<String> sequence1 = Arrays.asList("apple", null, "cherry");
 List<String> sequence2 = Arrays.asList("apple", "banana", "cherry");
 Equator<String> equator = DefaultEquator.defaultEquator();
 SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<String> script = comparator.getScript();
 List<EditCommand<String>> commands = script.getCommands();
 assertEquals(3, commands.size());
 assertEquals(DeleteCommand.class, commands.get(0).getClass());
 assertEquals(KeepCommand.class, commands.get(1).getClass());
 assertEquals(InsertCommand.class, commands.get(2).getClass());
 }

 @Test
 public void testSequencesWithNullValuesInBoth() {
 List<String> sequence1 = Arrays.asList(null, null, null);
 List<String> sequence2 = Arrays.asList(null, null, null);
 Equator<String> equator = DefaultEquator.defaultEquator();
 SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<String> script = comparator.getScript();
 List<EditCommand<String>> commands = script.getCommands();
 assertEquals(0, commands.size());
 }

 @Test
 public void testSequencesWithEmptyListAsSingleElement() {
 List<Integer> sequence1 = Arrays.asList(Collections.emptyList());
 List<Integer> sequence2 = Arrays.asList(1, 2, 3);
 Equator<Integer> equator = DefaultEquator.defaultEquator();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> script = comparator.getScript();
 List<EditCommand<Integer>> commands = script.getCommands();
 assertEquals(2, commands.size());
 assertEquals(DeleteCommand.class, commands.get(0).getClass());
 assertEquals(InsertCommand.class, commands.get(1).getClass());
 }

 @Test
 public void testSequencesWithSameElementAtStart() {
 List<String> sequence1 = Arrays.asList("apple", "banana", "cherry");
 List<String> sequence2 = Arrays.asList("apple", "cherry", "banana");
 Equator<String> equator = DefaultEquator.defaultEquator();
 SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<String> script = comparator.getScript();
 List<EditCommand<String>> commands = script.getCommands();
 assertEquals(2, commands.size());
 assertEquals(DeleteCommand.class, commands.get(0).getClass());
 assertEquals(InsertCommand.class, commands.get(1).getClass());
 }

 @Test
 public void testSequencesWithSameElementAtEnd() {
 List<String> sequence1 = Arrays.asList("apple", "banana", "cherry");
 List<String> sequence2 = Arrays.asList("apple", "banana", "cherry", "cherry");
 Equator<String> equator = DefaultEquator.defaultEquator();
 SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<String> script = comparator.getScript();
 List<EditCommand<String>> commands = script.getCommands();
 assertEquals(1, commands.size());
 assertEquals(InsertCommand.class, commands.get(0).getClass());
 }

 @Test
 public void testSequencesWithSameElementInBoth() {
 List<String> sequence1 = Arrays.asList("apple", "banana", "cherry", "cherry");
 List<String> sequence2 = Arrays.asList("apple", "banana", "cherry", "cherry");
 Equator<String> equator = DefaultEquator.defaultEquator();
 SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<String> script = comparator.getScript();
 List<EditCommand<String>> commands = script.getCommands();
 assertEquals(0, commands.size());
 }

 @Test
 public void testSequencesWithDifferentElementTypes() {
 List<Object> sequence1 = Arrays.asList("apple", 1, "cherry");
 List<Object> sequence2 = Arrays.asList("banana", 2, "cherry");
 Equator<Object> equator = DefaultEquator.defaultEquator();
 SequencesComparator<Object> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 try {
 comparator.getScript();
 fail("Expected IllegalArgumentException");
 }
 catch (IllegalArgumentException e) {
 // Expected
 }
 }

}