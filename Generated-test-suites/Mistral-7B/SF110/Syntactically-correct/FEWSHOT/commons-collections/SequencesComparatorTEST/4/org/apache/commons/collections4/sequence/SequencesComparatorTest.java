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
    public void testSequencesComparator_EmptySequences() {
        List<Integer> sequence1 = Collections.emptyList();
        List<Integer> sequence2 = Collections.emptyList();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertNull(script);
    }

    @Test
    public void testSequencesComparator_SameSequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = sequence1;
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertNull(script);
    }

    @Test
    public void testSequencesComparator_IdenticalSequences_DifferentOrders() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(2, 3, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(new KeepCommand<>(1), script.getCommand(0));
        assertEquals(new KeepCommand<>(2), script.getCommand(1));
        assertEquals(new KeepCommand<>(3), script.getCommand(2));
    }

    @Test
    public void testSequencesComparator_SequencesWithDifferences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> sequence2 = Arrays.asList(1, 3, 4, 2);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(new DeleteCommand<>(4), script.getCommand(0));
        assertEquals(new InsertCommand<>(3), script.getCommand(1));
        assertEquals(new KeepCommand<>(1), script.getCommand(2));
        assertEquals(new KeepCommand<>(2), script.getCommand(3));
        assertEquals(new KeepCommand<>(3), script.getCommand(4));
        assertEquals(new KeepCommand<>(4), script.getCommand(5));
    }

    @Test
    public void testSequencesComparator_SequencesWithSwappedElements() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(3, 2, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(new SwapCommand<>(1, 2), script.getCommand(0));
        assertEquals(new SwapCommand<>(2, 1), script.getCommand(1));
    }

    @Test
    public void testSequencesComparator_SequencesWithTranspositions() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> sequence2 = Arrays.asList(3, 4, 2, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(new SwapCommand<>(1, 3), script.getCommand(0));
        assertEquals(new SwapCommand<>(2, 2), script.getCommand(1));
        assertEquals(new SwapCommand<>(3, 4), script.getCommand(2));
    }

    @Test
    public void testSequencesComparator_SequencesWithTranspositions_DifferentLengths() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> sequence2 = Arrays.asList(3, 4, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(new SwapCommand<>(1, 3), script.getCommand(0));
        assertEquals(new InsertCommand<>(5), script.getCommand(1));
        assertEquals(new DeleteCommand<>(4), script.getCommand(2));
    }

    @Test(expected = RuntimeException.class)
    public void testSequencesComparator_SequencesWithInvalidAlignment() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(3, 4, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(new SwapCommand<>(1, 3), script.getCommand(0)); // This should be correct
        assertEquals(new InsertCommand<>(5), script.getCommand(1)); // This should fail
    }
}