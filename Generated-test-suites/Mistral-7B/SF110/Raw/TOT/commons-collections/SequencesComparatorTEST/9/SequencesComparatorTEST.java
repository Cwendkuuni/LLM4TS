import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Sequences;
import org.apache.commons.collections4.Equalators;
import org.junit.Test;
import org.junit.Assert;

public class SequencesComparatorTest {

 @Test
 public void testSequencesComparatorEqualSizedSequences() {
 List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 List<Integer> sequence2 = new ArrayList<>(sequence1); // same sequence
 Equator<Integer> equator = Equalators.equalTo(Integer.class);
 SequencesComparator<Integer> sequencesComparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> script = sequencesComparator.getScript();

 // Check that the script contains no operations
 Assert.assertEquals(0, script.size());
 }

 @Test
 public void testSequencesComparatorDifferentSizedSequences() {
 List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
 Equator<Integer> equator = Equalors.equalTo(Integer.class);
 SequencesComparator<Integer> sequencesComparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> script = sequencesComparator.getScript();

 // Check that the script contains InsertCommand for the missing element in sequence2
 Assert.assertEquals(1, script.size());
 Assert.assertEquals(InsertCommand.class, script.getCommandClass(0));
 }

 @Test
 public void testSequencesComparatorEmptySequences() {
 List<Integer> sequence1 = new ArrayList<>();
 List<Integer> sequence2 = new ArrayList<>();
 Equator<Integer> equator = Equalors.equalTo(Integer.class);
 SequencesComparator<Integer> sequencesComparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> script = sequencesComparator.getScript();

 // Check that the script is empty
 Assert.assertEquals(0, script.size());
 }

 @Test(expected = RuntimeException.class)
 public void testSequencesComparatorNullSequences() {
 List<Integer> sequence1 = null;
 List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
 Equator<Integer> equator = Equalors.equalTo(Integer.class);
 SequencesComparator<Integer> sequencesComparator = new SequencesComparator<>(sequence1, sequence2, equator);
 }

 @Test(expected = RuntimeException.class)
 public void testSequencesComparatorNullSequenceElement() {
 List<Integer> sequence1 = Arrays.asList(1, null, 3, 4);
 List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4);
 Equator<Integer> equator = Equalors.equalTo(Integer.class);
 SequencesComparator<Integer> sequencesComparator = new SequencesComparator<>(sequence1, sequence2, equator);
 }

 @Test(expected = RuntimeException.class)
 public void testSequencesComparatorNonEqualSequencesWithNullElements() {
 List<Integer> sequence1 = Arrays.asList(1, null, 3, 4);
 List<Integer> sequence2 = Arrays.asList(1, 2, null, 4);
 Equator<Integer> equator = Equalors.equalTo(Integer.class);
 SequencesComparator<Integer> sequencesComparator = new SequencesComparator<>(sequence1, sequence2, equator);
 }
}