package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparator() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(3);
        sequence1.add(5);

        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(2);
        sequence2.add(3);
        sequence2.add(4);
        sequence2.add(5);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        assertEquals(3, script.size());
        assertEquals(DeleteCommand.class, script.getCommandClass(0));
        assertEquals(InsertCommand.class, script.getCommandClass(1));
        assertEquals(KeepCommand.class, script.getCommandClass(2));

        List<Integer> result = new ArrayList<>();
        script.applyTo(sequence1, result);

        List<Integer> expected = new ArrayList<>();
        expected.add(2);
        expected.add(3);
        expected.add(5);

        assertEquals(expected, sequence1);
        assertEquals(sequence2, result);
    }

    @Test
    public void testSequencesComparatorWithEqualSequences() {
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

        assertEquals(0, script.size());
    }

    @Test(expected = RuntimeException.class)
    public void testSequencesComparatorInternalError() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);

        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(1);
        sequence2.add(2);
        sequence2.add(2); // duplicate element

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        comparator.getScript();
    }

    @Test
    public void testSequencesComparatorWithNullLists() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>(null, null);
        assertNull(comparator.getScript());
    }

    @Test
    public void testSequencesComparatorWithNullEquator() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);

        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(2);
        sequence2.add(3);
        sequence2.add(4);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, null);
        assertNull(comparator.getScript());
    }
}