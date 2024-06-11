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
    public void testSequencesComparatorEmpty() {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(0, script.size());
        assertNull(script.getCommands());
    }

    @Test
    public void testSequencesComparatorIdentity() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = list1;

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        assertNull(script.getCommands());
    }

    @Test
    public void testSequencesComparatorDifferentSizes() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2, 3, 4);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        assertArrayEquals(new Object[]{"InsertCommand{element=4}", "KeepCommand{element=1}", "KeepCommand{element=2}", "KeepCommand{element=3}", "KeepCommand{element=3}", "KeepCommand{element=3}"}, script.getCommands());
    }

    @Test
    public void testSequencesComparatorDifferentElements() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(4, 5, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        assertArrayEquals(new Object[]{"DeleteCommand{element=1}", "InsertCommand{element=4}", "InsertCommand{element=5}", "KeepCommand{element=3}"}, script.getCommands());
    }

    @Test
    public void testSequencesComparatorEqualSequences() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        assertNull(script.getCommands());
    }

    @Test(expected = RuntimeException.class)
    public void testSequencesComparatorInternalError() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2, 3, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
    }
}