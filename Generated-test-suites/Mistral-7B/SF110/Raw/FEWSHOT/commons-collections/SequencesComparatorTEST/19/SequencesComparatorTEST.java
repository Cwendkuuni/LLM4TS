package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.EditScript;
import org.apache.commons.collections4.functors.KeepCommand;
import org.apache.commons.collections4.functors.InsertCommand;
import org.apache.commons.collections4.functors.DeleteCommand;

public class SequencesComparatorTest {

    @Test
    public void testEmptySequences() {
        List<Integer> sequence1 = Collections.emptyList();
        List<Integer> sequence2 = Collections.emptyList();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> editScript = comparator.getScript();

        assertNull(editScript);
    }

    @Test
    public void testIdenticalSequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> editScript = comparator.getScript();

        assertEquals(0, editScript.size());
    }

    @Test
    public void testSequencesWithDifferences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> sequence2 = Arrays.asList(1, 2, 5, 4);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> editScript = comparator.getScript();

        List<Object> expectedCommands = Arrays.asList(new DeleteCommand<>(4), new InsertCommand<>(5));
        List<Object> actualCommands = new ArrayList<>();

        for (Object command : editScript) {
            actualCommands.add(command);
        }

        assertEquals(expectedCommands, actualCommands);
    }

    @Test
    public void testSequencesWithMultipleDifferences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 2, 6, 4, 5, 7);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> editScript = comparator.getScript();

        List<Object> expectedCommands = Arrays.asList(new DeleteCommand<>(5), new InsertCommand<>(6), new InsertCommand<>(7));
        List<Object> actualCommands = new ArrayList<>();

        for (Object command : editScript) {
            actualCommands.add(command);
        }

        assertEquals(expectedCommands, actualCommands);
    }

    @Test(expected = RuntimeException.class)
    public void testSequencesWithInternalError() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 1, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        comparator.getScript();
    }
}