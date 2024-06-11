import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Sequences;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.seq.EditScript;
import org.apache.commons.collections4.seq.EditScriptEditor;
import org.apache.commons.collections4.seq.SequencesComparator;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class SequencesComparatorTest {

 @Test
 public void testEmptySequences() {
 List<Integer> sequence1 = new ArrayList<>();
 List<Integer> sequence2 = new ArrayList<>();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 EditScript<Integer> script = comparator.getScript();
 List<Integer> expectedResult = new ArrayList<>();
 EditScriptEditor<Integer> editor = new EditScriptEditor<>();
 editor.applyScript(expectedResult, script);
 assertTrue(expectedResult.isEmpty());
 }

 @Test
 public void testIdenticalSequences() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
 List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 EditScript<Integer> script = comparator.getScript();
 List<Integer> expectedResult = Arrays.asList(1, 2, 3, 4);
 EditScriptEditor<Integer> editor = new EditScriptEditor<>();
 editor.applyScript(expectedResult, script);
 assertEquals(expectedResult, sequence1);
 }

 @Test
 public void testDifferentSequences() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> sequence2 = Arrays.asList(1, 2, 3, 6, 4);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
 EditScript<Integer> script = comparator.getScript();
 List<Integer> expectedResult = Arrays.asList(1, 2, 3, 4, 5, DeleteCommand.DELETE, InsertCommand.INSERT, 6, InsertCommand.INSERT, 4);
 EditScriptEditor<Integer> editor = new EditScriptEditor<>();
 editor.applyScript(expectedResult, script);
 assertEquals(expectedResult, Arrays.asList(1, 2, 3, 4, 5, null, 6, null, 4));
 }

 @Test(expected = RuntimeException.class)
 public void testDifferentSequencesWithDifferentSize() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 comparator.getScript();
 }

 @Test(expected = RuntimeException.class)
 public void testNullSequences() {
 List<Integer> sequence1 = null;
 List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 }

 @Test(expected = RuntimeException.class)
 public void testNullEquator() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
 List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, null);
 }

 @Test
 public void testSequencesWithDuplicateElements() {
 List<String> sequence1 = Arrays.asList("apple", "banana", "apple", "orange");
 List<String> sequence2 = Arrays.asList("banana", "apple", "orange", "apple");
 SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
 EditScript<String> script = comparator.getScript();
 List<String> expectedResult = Arrays.asList("apple", KeepCommand.KEEP, "banana", InsertCommand.INSERT, "orange", KeepCommand.KEEP, "apple", DeleteCommand.DELETE, "apple");
 EditScriptEditor<String> editor = new EditScriptEditor<>();
 editor.applyScript(expectedResult, script);
 assertEquals(expectedResult, Arrays.asList("apple", "banana", "orange", "apple"));
 }

 @Test
 public void testSequencesWithEmptySequenceAsFirstArgument() {
 List<Integer> sequence1 = new ArrayList<>();
 List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 EditScript<Integer> script = comparator.getScript();
 List<Integer> expectedResult = Arrays.asList(DeleteCommand.DELETE, 1, InsertCommand.INSERT, 1, 2, InsertCommand.INSERT, 3, InsertCommand.INSERT, 4);
 EditScriptEditor<Integer> editor = new EditScriptEditor<>();
 editor.applyScript(expectedResult, script);
 assertEquals(expectedResult, Arrays.asList(null, 1, 2, 3, 4));
 }

 @Test
 public void testSequencesWithEmptySequenceAsSecondArgument() {List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
 List<Integer> sequence2 = new ArrayList<>();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 EditScript<Integer> script = comparator.getScript();
 List<Integer> expectedResult = Arrays.asList(1, 2, 3, 4, DeleteCommand.DELETE);
 EditScriptEditor<Integer> editor = new EditScriptEditor<>();
 editor.applyScript(expectedResult, script);
 assertEquals(expectedResult, Arrays.asList(1, 2, 3, 4, null));
 }

 @Test
 public void testSequencesWithNullElement() {
 List<String> sequence1 = Arrays.asList("apple", "banana", null, "orange");
 List<String> sequence2 = Arrays.asList("banana", "apple", "orange", "apple");
 SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
 EditScript<String> script = comparator.getScript();
 List<String> expectedResult = Arrays.asList("apple", KeepCommand.KEEP, "banana", InsertCommand.INSERT, "orange", KeepCommand.KEEP, null, DeleteCommand.DELETE, null);
 EditScriptEditor<String> editor = new EditScriptEditor<>();
 editor.applyScript(expectedResult, script);
 assertEquals(expectedResult, Arrays.asList("apple", "banana", "orange", null));
 }
}