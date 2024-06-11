package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.EditScript;
import org.apache.commons.collections4.functors.KeepCommand;
import org.apache.commons.collections4.functors.InsertCommand;
import org.apache.commons.collections4.functors.DeleteCommand;

import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparator_nullSequences() {
        try {
            new SequencesComparator<>(null, null);
            fail("Expected IllegalStateException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new SequencesComparator<>(null, new ArrayList<>());
            fail("Expected IllegalStateException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new SequencesComparator<>(new ArrayList<>(), null);
            fail("Expected IllegalStateException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSequencesComparator_emptySequences() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertNull(script);
    }

    @Test
    public void testSequencesComparator_equalSequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(0, script.size());
    }

    @Test
    public void testSequencesComparator_sequencesWithDiffs() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> sequence2 = Arrays.asList(1, 2, 5, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        List<Object> expected = Arrays.asList(new DeleteCommand<>(4), new InsertCommand<>(5), new InsertCommand<>(3));
        assertArrayEquals(expected.toArray(), script.toArray());
    }

    @Test
    public void testSequencesComparator_sequencesWithReversedOrder() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(3, 2, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        List<Object> expected = Arrays.asList(new InsertCommand<>(2), new InsertCommand<>(1));
        assertArrayEquals(expected.toArray(), script.toArray());
    }

    @Test(expected = RuntimeException.class)
    public void testSequencesComparator_internalError() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 3, 2);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        comparator.getScript();
    }
}