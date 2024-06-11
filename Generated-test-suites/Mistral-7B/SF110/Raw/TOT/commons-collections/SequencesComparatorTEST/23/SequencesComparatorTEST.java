import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Sequences;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.seq.EditScript;
import org.apache.commons.collections4.seq.SequenceComparator;
import org.apache.commons.collections4.seq.Snake;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class SequencesComparatorTest {

    @Test
    public void testEmptySequences() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequenceComparator<Integer> comparator = new SequenceComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        assertEquals(0, script.size());
    }

    @Test
    public void testIdenticalSequences() {
        List<String> sequence1 = new ArrayList<>(List.of("apple", "banana", "cherry"));
        List<String> sequence2 = new ArrayList<>(List.of("apple", "banana", "cherry"));
        SequenceComparator<String> comparator = new SequenceComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<String> script = comparator.getScript();
        List<EditCommand<String>> commands = script.getCommands();
        for (EditCommand<String> command : commands) {
            if (command instanceof KeepCommand<?>) {
                KeepCommand<String> keepCommand = (KeepCommand<String>) command;
                assertEquals(sequence1.get(keepCommand.getIndex()), keepCommand.getElement());
            }
        }
    }

    @Test
    public void testDifferentSequences() {
        List<Integer> sequence1 = new ArrayList<>(List.of(1, 2, 3, 4));
        List<Integer> sequence2 = new ArrayList<>(List.of(4, 3, 2, 1));
        SequenceComparator<Integer> comparator = new SequenceComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> commands = script.getCommands();
        int index1 = 0;
        int index2 = 0;
        for (EditCommand<Integer> command : commands) {
            if (command instanceof KeepCommand<?>) {
                KeepCommand<Integer> keepCommand = (KeepCommand<Integer>) command;
                assertEquals(sequence1.get(index1++), keepCommand.getElement());
            } else if (command instanceof DeleteCommand<?>) {
                DeleteCommand<Integer> deleteCommand = (DeleteCommand<Integer>) command;
                assertEquals(sequence1.get(index1 - 1), deleteCommand.getElement());
                assertEquals(index1, sequence1.size());
                index1 = index1 - 1;
            } else if (command instanceof InsertCommand<?>) {
                InsertCommand<Integer> insertCommand = (InsertCommand<Integer>) command;
                assertEquals(sequence2.get(index2), insertCommand.getElement());
                ++index2;
            }
        }
        assertEquals(index2, sequence2.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSequence1() {
        List<String> sequence1 = null;
        List<String> sequence2 = new ArrayList<>(List.of("apple", "banana", "cherry"));
        SequenceComparator<String> comparator = new SequenceComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSequence2() {
        List<String> sequence1 = new ArrayList<>(List.of("apple", "banana", "cherry"));
        List<String> sequence2 = null;
        SequenceComparator<String> comparator = new SequenceComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDifferentSizes() {
        List<Integer> sequence1 = new ArrayList<>(List.of(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(List.of(1, 2, 3, 4));
        SequenceComparator<Integer> comparator = new SequenceComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullEquator() {
        List<String> sequence1 = new ArrayList<>(List.of("apple", "banana", "cherry"));
        List<String> sequence2 = new ArrayList<>(List.of("apple", "banana", "cherry"));
        SequenceComparator<String> comparator = new SequenceComparator<>(sequence1, sequence2, null);
    }
}