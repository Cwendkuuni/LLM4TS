package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.EditScript;
import org.apache.commons.collections4.functors.KeepCommand;
import org.apache.commons.collections4.functors.InsertCommand;
import org.apache.commons.collections4.functors.DeleteCommand;

import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testEmptySequences() {
        List<Integer> sequence1 = Collections.emptyList();
        List<Integer> sequence2 = Collections.emptyList();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(Arrays.asList(new DeleteCommand<>(null)), script.getCommands());
        assertArrayEquals(new int[0], comparator.vDown);
        assertArrayEquals(new int[0], comparator.vUp);
    }

    @Test
    public void testIdentitySequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(Collections.emptyList(), script.getCommands());
        assertArrayEquals(new int[]{1, 1, 2, 2, 3, 3}, comparator.vDown);
        assertArrayEquals(new int[]{1, 1, 2, 2, 3, 3}, comparator.vUp);
    }

    @Test
    public void testSequencesWithDifferences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> sequence2 = Arrays.asList(1, 2, 5, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        List<EditScript.Command<Integer>> commands = script.getCommands();
        assertEquals(3, commands.size());
        assertEquals(new DeleteCommand<>(4), commands.get(0));
        assertEquals(new InsertCommand<>(5), commands.get(1));
        assertEquals(new KeepCommand<>(3), commands.get(2));

        List<Integer> result1 = new ArrayList<>();
        List<Integer> result2 = new ArrayList<>();
        comparator.applyScript(result1, result2);
        assertEquals(Arrays.asList(1, 2, 5, 3, 4), result2);
        assertEquals(Arrays.asList(1, 2, 3, 4), result1);

        assertArrayEquals(new int[]{1, 1, 2, 2, 4, 4, 5, 5, 5, 6, 6}, comparator.vDown);
        assertArrayEquals(new int[]{1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6}, comparator.vUp);
    }
}