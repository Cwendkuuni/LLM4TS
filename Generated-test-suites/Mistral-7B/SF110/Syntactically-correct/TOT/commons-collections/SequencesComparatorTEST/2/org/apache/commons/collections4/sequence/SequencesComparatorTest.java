import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.SequenceUtils;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.functors.Equator;
import org.apache.commons.collections4.seq.*;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testEmptySequences() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(0, script.size());
    }

    @Test
    public void testEqualSequences() {
        List<String> sequence1 = new ArrayList<>(List.of("apple", "banana", "cherry"));
        List<String> sequence2 = new ArrayList<>(List.of("apple", "banana", "cherry"));
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<String> script = comparator.getScript();
        assertEquals(0, script.size());
    }

    @Test
    public void testSequencesWithDifferentLengthsAndDifferentElements() {
        List<Integer> sequence1 = new ArrayList<>(List.of(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(List.of(1, 4, 5, 6));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> commands = script.getCommands();

        // Expected: Delete 1 element from sequence1, Insert 1 element into sequence2
        List<Integer> expectedSequence1 = new ArrayList<>(List.of(2));
        List<Integer> expectedSequence2 = new ArrayList<>(List.of(1, 2, 3, 5));
        assertEquals(expectedSequence1, sequence1);
        assertEquals(expectedSequence2, sequence2);

        for (EditCommand<Integer> command : commands) {
            if (command instanceof DeleteCommand<?>) {
                DeleteCommand<Integer> deleteCommand = (DeleteCommand<Integer>) command;
                assertTrue(sequence1.remove(deleteCommand.getObject()));
                assertEquals(expectedSequence1.size(), sequence1.size());
            } else if (command instanceof InsertCommand<?>) {
                InsertCommand<Integer> insertCommand = (InsertCommand<Integer>) command;
                sequence2.add(insertCommand.getObject());
                assertEquals(expectedSequence2.size(), sequence2.size());
            } else {
                fail("Unexpected command found: " + command.getClass().getName());
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSequence() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = null;
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyEquator() {
        List<Integer> sequence1 = new ArrayList<>(List.of(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(List.of(1, 2, 3));
        Equator<Integer> equator = new Equator<Integer>() {}; // Empty equator
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
    }

    @Test(expected = RuntimeException.class)
    public void testInternalError() {
        List<String> sequence1 = new ArrayList<>(List.of("apple", "banana", "cherry", "dog", "elephant"));
        List<String> sequence2 = new ArrayList<>(List.of("apple", "banana", "cherry", "elephant", "dog"));
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = comparator.getScript();
        // This test case is intended to trigger the internal error in SequencesComparator
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
    }
}