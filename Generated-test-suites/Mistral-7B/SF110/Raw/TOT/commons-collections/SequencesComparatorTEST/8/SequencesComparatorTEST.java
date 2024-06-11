import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Sequences;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.sequence.EditScript;
import org.apache.commons.collections4.sequence.SequencesComparator;
import org.apache.commons.collections4.util.EditScriptUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class SequencesComparatorTest {

 @SuppressWarnings("unchecked")
 @Test
 public void testSequencesComparator_equalSequences() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
 List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());

 EditScript<Integer> script = comparator.getScript();

 List<Integer> expected = Arrays.asList(1, 2, 3, 4);
 List<Integer> result = EditScriptUtils.applyEditScript(expected, script);

 assertEquals(expected, result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testSequencesComparator_reverseSequences() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
 List<Integer> sequence2 = Arrays.asList(4, 3, 2, 1);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());

 EditScript<Integer> script = comparator.getScript();

 List<Integer> expected = Arrays.asList();
 List<Integer> result = EditScriptUtils.applyEditScript(expected, script);

 assertTrue(result.isEmpty());
 }

 @SuppressWarnings("unchecked")
 @Test(expected = RuntimeException.class)
 public void testSequencesComparator_differentSizeSequences() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3);
 List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());

 comparator.getScript(); // This test case should throw a RuntimeException
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testSequencesComparator_nullSequences() {
 List<Integer> sequence1 = null;
 List<Integer> sequence2 = Arrays.asList(1, 2, 3);

 try {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
 } catch (NullPointerException e) {
 // Expected null pointer exception since sequence1 is null
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testSequencesComparator_equalSequencesWithNullElements() {
 List<Object> sequence1 = Arrays.asList(1, 2, null, 4);
 List<Object> sequence2 = Arrays.asList(1, 2, null, 4);
 SequencesComparator<Object> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());

 EditScript<Object> script = comparator.getScript();

 List<Object> expected = Arrays.asList(1, 2, null, 4);
 List<Object> result = EditScriptUtils.applyEditScript(expected, script);

 assertEquals(expected, result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testSequencesComparator_differentSequencesWithNullElements() {
 List<Object> sequence1 = Arrays.asList(1, 2, null, 4);
 List<Object> sequence2 = Arrays.asList(1, 2, 5, 4);
 SequencesComparator<Object> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());

 EditScript<Object> script = comparator.getScript();

 List<Object> expected = Arrays.asList(new DeleteCommand<>(null), new InsertCommand<>(5));
 List<Object> result = EditScriptUtils.applyEditScript(sequence1, script);

 assertEquals(expected, script);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testSequencesComparator_emptySequences() {
 List<Integer> sequence1 = Collections.emptyList();
 List<Integer> sequence2 = Collections.emptyList();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());

 EditScript<Integer> script = comparator.getScript();

 List<Integer> expected = Collections.emptyList();
 List<Integer> result = EditScriptUtils.applyEditScript(expected, script);

 assertEquals(expected, result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testSequencesComparator_sequencesWithSameElement() {
 List<Integer> sequence1 = Arrays.asList(1, 1, 2, 3);
 List<Integer> sequence2 = Arrays.asList(1, 1, 2, 3, 1);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());

 EditScript<Integer> script = comparator.getScript();

 List<Integer> expected = Arrays.asList(new DeleteCommand<>(1), new InsertCommand<>(1), new KeepCommand<>(2), new KeepCommand<>(3), new KeepCommand<>(1));
 List<Object> result = EditScriptUtils.applyEditScript(sequence1, script);

 assertEquals(expected, script);
 }
}