package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.EditScript;
import org.apache.commons.collections4.Functor;
import org.apache.commons.collections4.functors.KeepCommand;
import org.apache.commons.collections4.functors.InsertCommand;
import org.apache.commons.collections4.functors.DeleteCommand;
import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparator_EmptySequences() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        assertNull(script);
    }

    @Test
    public void testSequencesComparator_IdenticalSequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        assertNull(script);
    }

    @Test
    public void testSequencesComparator_DifferentSequences_SimpleCase() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(2, 3, 1);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<Functor<Integer>> expectedCommands = new ArrayList<>();
        expectedCommands.add(new DeleteCommand<>(1));
        expectedCommands.add(new InsertCommand<>(2));
        expectedCommands.add(new InsertCommand<>(3));

        assertSame(EditScript.class, script.getClass());
        assertCommandsEquals(expectedCommands, script);
    }

    @Test
    public void testSequencesComparator_DifferentSequences_ComplexCase() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(3, 2, 1, 5, 4);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<Functor<Integer>> expectedCommands = new ArrayList<>();
        expectedCommands.add(new DeleteCommand<>(1));
        expectedCommands.add(new DeleteCommand<>(2));
        expectedCommands.add(new InsertCommand<>(3));
        expectedCommands.add(new InsertCommand<>(2));
        expectedCommands.add(new InsertCommand<>(1));
        expectedCommands.add(new InsertCommand<>(5));

        assertSame(EditScript.class, script.getClass());
        assertCommandsEquals(expectedCommands, script);
    }

    @Test
    public void testSequencesComparator_DifferentSequences_EmptySequencesAtEnd() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<Functor<Integer>> expectedCommands = new ArrayList<>();
        expectedCommands.add(new DeleteCommand<>(4));

        assertSame(EditScript.class, script.getClass());
        assertCommandsEquals(expectedCommands, script);
    }

    @Test
    public void testSequencesComparator_DifferentSequences_EmptySequencesAtBeginning() {
        List<Integer> sequence1 = Arrays.asList(3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<Functor<Integer>> expectedCommands = new ArrayList<>();
        expectedCommands.add(new InsertCommand<>(1));
        expectedCommands.add(new InsertCommand<>(2));

        assertSame(EditScript.class, script.getClass());
        assertCommandsEquals(expectedCommands, script);
    }

    @Test
    public void testSequencesComparator_DifferentSequences_MixedCase() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(3, 4, 2, 1, 5);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<Functor<Integer>> expectedCommands = new ArrayList<>();
        expectedCommands.add(new DeleteCommand<>(1));
        expectedCommands.add(new DeleteCommand<>(2));
        expectedCommands.add(new InsertCommand<>(3));
        expectedCommands.add(new InsertCommand<>(4));
        expectedCommands.add(new InsertCommand<>(2));

        assertSame(EditScript.class, script.getClass());
        assertCommandsEquals(expectedCommands, script);
    }

    // Add more test cases as needed

    private void assertCommandsEquals(List<Functor<Integer>> expectedCommands, EditScript<Integer> script) {
        List<Functor<Integer>> actualCommands = new ArrayList<>();
        Functor<Integer> currentCommand = script.getNextCommand();
        while (currentCommand != null) {
            actualCommands.add(currentCommand);
            currentCommand = script.getNextCommand();
        }
        Collections.reverse(actualCommands);
        assertEquals(expectedCommands.size(), actualCommands.size());
        for (int i = 0; i < expectedCommands.size(); ++i) {
            assertEquals(expectedCommands.get(i).getClass(), actualCommands.get(i).getClass());
            if (expectedCommands.get(i) instanceof KeepCommand) {
                assertEquals(((KeepCommand<Integer>) expectedCommands.get(i)).getElement(), ((KeepCommand<Integer>) actualCommands.get(i)).getElement());
            }
            else if (expectedCommands.get(i) instanceof InsertCommand) {
                assertEquals(((InsertCommand<Integer>) expectedCommands.get(i)).getElement(), ((InsertCommand<Integer>) actualCommands.get(i)).getElement());
            }
            else if (expectedCommands.get(i) instanceof DeleteCommand) {
                assertEquals(((DeleteCommand<Integer>) expectedCommands.get(i)).getElement(), ((DeleteCommand<Integer>) actualCommands.get(i)).getElement());
            }
        }
    }
}