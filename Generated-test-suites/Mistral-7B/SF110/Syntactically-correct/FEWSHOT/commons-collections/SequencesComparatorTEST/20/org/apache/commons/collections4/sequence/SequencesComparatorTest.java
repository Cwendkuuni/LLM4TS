package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;

import java.util.*;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparator() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);

        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(1);
        sequence2.add(3);
        sequence2.add(2);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<SequenceCommand<Integer>> commands = script.getCommands();
        List<Integer> expectedCommands = new ArrayList<>();
        expectedCommands.add(new KeepCommand<>(1));
        expectedCommands.add(new DeleteCommand<>(2));
        expectedCommands.add(new InsertCommand<>(3));

        assertEquals(expectedCommands.size(), commands.size());
        for (int i = 0; i < commands.size(); i++) {
            assertEquals(expectedCommands.get(i).getClass(), commands.get(i).getClass());
            if (commands.get(i) instanceof KeepCommand) {
                KeepCommand<Integer> keepCommand = (KeepCommand<Integer>) commands.get(i);
                Integer keepValue = keepCommand.getValue();
                Integer expectedValue = expectedCommands.get(i).getValue();
                assertEquals(expectedValue, keepValue);
            } else if (commands.get(i) instanceof DeleteCommand) {
                DeleteCommand<Integer> deleteCommand = (DeleteCommand<Integer>) commands.get(i);
                Integer deleteValue = deleteCommand.getValue();
                Integer expectedValue = expectedCommands.get(i).getValue();
                assertEquals(expectedValue, deleteValue);
            } else if (commands.get(i) instanceof InsertCommand) {
                InsertCommand<Integer> insertCommand = (InsertCommand<Integer>) commands.get(i);
                Integer insertValue = insertCommand.getValue();
                Integer expectedValue = expectedCommands.get(i).getValue();
                assertEquals(expectedValue, insertValue);
            }
        }
    }

    @Test
    public void testSequencesComparatorNullSequence() {
        List<Integer> sequence1 = null;
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(1);
        sequence2.add(2);
        sequence2.add(3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        assertNull(comparator.getScript());
    }

    @Test
    public void testSequencesComparatorNullSequence2() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);

        List<Integer> sequence2 = null;

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        assertNull(comparator.getScript());
    }

    @Test
    public void testSequencesComparatorEmptySequences() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        List<SequenceCommand<Integer>> commands = script.getCommands();
        assertArrayEquals(new KeepCommand[0], commands.toArray(new KeepCommand[0]));
    }

    @Test
    public void testSequencesComparatorIdenticalSequences() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);

        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(1);
        sequence2.add(2);
        sequence2.add(3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        List<SequenceCommand<Integer>> commands = script.getCommands();
        assertArrayEquals(new KeepCommand[sequence1.size()][], commands.toArray(new KeepCommand[sequence1.size()][]));
    }

    @Test
    public void testSequencesComparatorSequencesWithSameElementsInDifferentOrder() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(3);
        sequence1.add(2);

        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(2);
        sequence2.add(1);
        sequence2.add(3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        List<SequenceCommand<Integer>> commands = script.getCommands();
        List<Integer> expectedCommands = new ArrayList<>();
        expectedCommands.add(new DeleteCommand<>(2));
        expectedCommands.add(new InsertCommand<>(1));
        expectedCommands.add(new KeepCommand<>(3));

        assertEquals(expectedCommands.size(), commands.size());
        for (int i = 0; i < commands.size(); i++) {
            assertEquals(expectedCommands.get(i).getClass(), commands.get(i).getClass());
            if (commands.get(i) instanceof KeepCommand) {
                KeepCommand<Integer> keepCommand = (KeepCommand<Integer>) commands.get(i);
                Integer keepValue = keepCommand.getValue();
                Integer expectedValue = expectedCommands.get(i).getValue();
                assertEquals(expectedValue, keepValue);
            } else if (commands.get(i) instanceof DeleteCommand) {
                DeleteCommand<Integer> deleteCommand = (DeleteCommand<Integer>) commands.get(i);
                Integer deleteValue = deleteCommand.getValue();
                Integer expectedValue = expectedCommands.get(i).getValue();
                assertEquals(expectedValue, deleteValue);
            } else if (commands.get(i) instanceof InsertCommand) {
                InsertCommand<Integer> insertCommand = (InsertCommand<Integer>) commands.get(i);
                Integer insertValue = insertCommand.getValue();
                Integer expectedValue = expectedCommands.get(i).getValue();
                assertEquals(expectedValue, insertValue);
            }
        }
    }

    @Test
    public void testSequencesComparatorSequencesWithDifferentElements() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);

        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(4);
        sequence2.add(5);
        sequence2.add(3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        List<SequenceCommand<Integer>> commands = script.getCommands();
        List<Integer> expectedCommands = new ArrayList<>();
        expectedCommands.add(new DeleteCommand<>(1));
        expectedCommands.add(new DeleteCommand<>(2));
        expectedCommands.add(new InsertCommand<>(3));
        expectedCommands.add(new InsertCommand<>(4));
        expectedCommands.add(new InsertCommand<>(5));

        assertEquals(expectedCommands.size(), commands.size());
        for (int i = 0; i < commands.size(); i++) {
            assertEquals(expectedCommands.get(i).getClass(), commands.get(i).getClass());
            if (commands.get(i) instanceof KeepCommand) {
                KeepCommand<Integer> keepCommand = (KeepCommand<Integer>) commands.get(i);
                Integer keepValue = keepCommand.getValue();
                Integer expectedValue = expectedCommands.get(i).getValue();
                assertEquals(expectedValue, keepValue);
            } else if (commands.get(i) instanceof DeleteCommand) {
                DeleteCommand<Integer> deleteCommand = (DeleteCommand<Integer>) commands.get(i);
                Integer deleteValue = deleteCommand.getValue();
                Integer expectedValue = expectedCommands.get(i).getValue();
                assertEquals(expectedValue, deleteValue);
            } else if (commands.get(i) instanceof InsertCommand) {
                InsertCommand<Integer> insertCommand = (InsertCommand<Integer>) commands.get(i);
                Integer insertValue = insertCommand.getValue();
                Integer expectedValue = expectedCommands.get(i).getValue();
                assertEquals(expectedValue, insertValue);
            }
        }
    }
}