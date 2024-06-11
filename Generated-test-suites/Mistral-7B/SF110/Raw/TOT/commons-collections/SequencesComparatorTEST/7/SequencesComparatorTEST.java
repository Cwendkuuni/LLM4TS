import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SequencesComparatorTest {

 @Test
 public void testSequencesComparator_sameSequences() {
 List<Integer> sequence1 = new ArrayList<>();
 List<Integer> sequence2 = new ArrayList<>(sequence1);
 sequence1.add(1);
 sequence1.add(2);
 sequence1.add(3);

 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 EditScript<Integer> script = comparator.getScript();

 EditScript<Integer> expectedScript = new EditScript<>();
 expectedScript.append(new KeepCommand<>(1));
 expectedScript.append(new KeepCommand<>(2));
 expectedScript.append(new KeepCommand<>(3));

 assertEquals(expectedScript, script);
 }

 @Test
 public void testSequencesComparator_reverseSequences() {
 List<Integer> sequence1 = new ArrayList<>();
 List<Integer> sequence2 = new ArrayList<>();

 sequence1.add(1);
 sequence1.add(2);
 sequence1.add(3);
 sequence2.add(3);
 sequence2.add(2);
 sequence2.add(1);

 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 EditScript<Integer> script = comparator.getScript();

 EditScript<Integer> expectedScript = new EditScript<>();
 expectedScript.append(new DeleteCommand<>(1));
 expectedScript.append(new DeleteCommand<>(2));
 expectedScript.append(new DeleteCommand<>(3));
 expectedScript.append(new InsertCommand<>(3));
 expectedScript.append(new InsertCommand<>(2));
 expectedScript.append(new InsertCommand<>(1));

 assertEquals(expectedScript, script);
 }

 @Test(expected = RuntimeException.class)
 public void testSequencesComparator_error_invalidInput() {
 List<Object> sequence1 = new ArrayList<>();
 List<Object> sequence2 = new ArrayList<>();

 SequencesComparator<Object> comparator = new SequencesComparator<>(sequence1, sequence2);
 }

 @Test
 public void testSequencesComparator_error_nullElements() {
 List<String> sequence1 = new ArrayList<>();
 List<String> sequence2 = new ArrayList<>();

 sequence1.add(null);
 sequence2.add("test");

 SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
 }

 @Test(expected = RuntimeException.class)
 public void testSequencesComparator_error_sequencesSizeDifference() {
 List<Integer> sequence1 = new ArrayList<>();
 List<Integer> sequence2 = new ArrayList<>();

 sequence1.add(1);
 sequence2.add(2);
 sequence1.add(3);
 sequence2.add(4);

 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 }
}