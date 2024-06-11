package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;

public class SequencesComparatorTest<T> {

    @Test
    public void testSequencesComparator_EmptySequences() {
        List<T> sequence1 = new ArrayList<>();
        List<T> sequence2 = new ArrayList<>();
        SequencesComparator<T> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<T> script = comparator.getScript();
        assertTrue(script.isEmpty());
    }

    @Test
    public void testSequencesComparator_IdentitySequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertTrue(script.isEmpty());
    }

    @Test
    public void testSequencesComparator_DifferentSequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> sequence2 = Arrays.asList(1, 2, 5, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> commands = script.getCommands();
        assertEquals(3, commands.size());
        DeleteCommand<Integer> deleteCommand = (DeleteCommand<Integer>) commands.get(0);
        assertEquals(3, deleteCommand.getIndex());
        InsertCommand<Integer> insertCommand = (InsertCommand<Integer>) commands.get(2);
        assertEquals(4, insertCommand.getIndex());
        assertEquals(5, insertCommand.getElement().intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSequencesComparator_NullSequence1() {
        List<Integer> nullSequence1 = null;
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(nullSequence1, sequence2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSequencesComparator_NullSequence2() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> nullSequence2 = null;
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, nullSequence2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSequencesComparator_DifferentSizes() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSequencesComparator_NullEquator() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, null);
    }

    @Test
    public void testSequencesComparator_GetScript_EdgeCases() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(new Integer[0], new Integer[0]);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> commands = script.getCommands();
        assertTrue(commands.isEmpty());

        sequence1 = Arrays.asList(1, 2, 3, 4);
        sequence2 = Arrays.asList(new Integer[0]);
        comparator = new SequencesComparator<>(sequence1, sequence2);
        script = comparator.getScript();
        commands = script.getCommands();
        assertEquals(4, commands.size());
        for (EditCommand<Integer> command : commands) {
            if (command instanceof DeleteCommand<?>) {
                DeleteCommand<Integer> deleteCommand = (DeleteCommand<Integer>) command;
                assertEquals(0, deleteCommand.getIndex());
            }
        }

        sequence1 = Arrays.asList(new Integer[0], 2, 3, 4);
        sequence2 = Arrays.asList(1, 2, 3, 4);
        comparator = new SequencesComparator<>(sequence1, sequence2);
        script = comparator.getScript();
        commands = script.getCommands();
        assertEquals(3, commands.size());
        for (EditCommand<Integer> command : commands) {
            if (command instanceof InsertCommand<?>) {
                InsertCommand<Integer> insertCommand = (InsertCommand<Integer>) command;
                assertEquals(0, insertCommand.getIndex());
                assertEquals(1, insertCommand.getElement().intValue());
            }
        }

        sequence1 = Arrays.asList(1, 2, 3, 4);
        sequence2 = Arrays.asList(1, 2, 3, 4, 5);
        comparator = new SequencesComparator<>(sequence1, sequence2);
        script = comparator.getScript();
        commands = script.getCommands();
        assertEquals(1, commands.size());
        assertTrue(commands.isEmpty());
    }
}