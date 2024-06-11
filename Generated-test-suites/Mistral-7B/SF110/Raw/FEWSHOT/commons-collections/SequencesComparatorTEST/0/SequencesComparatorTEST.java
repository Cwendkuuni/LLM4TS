package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;

public class SequencesComparatorTest<T> {

    @Test
    public void testSequencesComparator_SameSequences() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>(sequence1);
        Equator<Integer> equator = DefaultEquator.defaultEquator();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(0, script.size());
    }

    @Test
    public void testSequencesComparator_EmptySequences() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        Equator<Integer> equator = DefaultEquator.defaultEquator();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(0, script.size());
    }

    @Test
    public void testSequencesComparator_IdenticalSequences() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        List<Integer> sequence2 = new ArrayList<>(sequence1);
        Equator<Integer> equator = DefaultEquator.defaultEquator();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(0, script.size());
    }

    @Test
    public void testSequencesComparator_DifferentLengthSequencesWithEquivalentElements() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(1);
        sequence2.add(2);
        Equator<Integer> equator = DefaultEquator.defaultEquator();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(1, script.size());
        assertTrue(script.getCommand(0) instanceof InsertCommand<?>);
        InsertCommand<Integer> insertCommand = (InsertCommand<Integer>) script.getCommand(0);
        assertEquals(2, insertCommand.getObject().intValue());
    }

    @Test
    public void testSequencesComparator_DifferentLengthSequencesWithDifferentElements() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(2);
        sequence2.add(3);
        Equator<Integer> equator = DefaultEquator.defaultEquator();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(2, script.size());
        assertTrue(script.getCommand(0) instanceof DeleteCommand<?>);
        assertTrue(script.getCommand(1) instanceof InsertCommand<?>);
        DeleteCommand<Integer> deleteCommand = (DeleteCommand<Integer>) script.getCommand(0);
        assertEquals(1, deleteCommand.getObject().intValue());
        InsertCommand<Integer> insertCommand = (InsertCommand<Integer>) script.getCommand(1);
        assertEquals(3, insertCommand.getObject().intValue());
    }

    @Test
    public void testSequencesComparator_SequencesWithSameElementButDifferentPositions() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(2);
        sequence2.add(1);
        Equator<Integer> equator = DefaultEquator.defaultEquator();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(1, script.size());
        assertTrue(script.getCommand(0) instanceof TranspositionCommand<?>);
        TranspositionCommand<Integer> transpositionCommand = (TranspositionCommand<Integer>) script.getCommand(0);
        assertEquals(1, transpositionCommand.getFirstObject().intValue());
        assertEquals(1, transpositionCommand.getSecondObject().intValue());
    }

    @Test
    public void testSequencesComparator_SequencesWithDifferentElements() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(3);
        sequence2.add(4);
        Equator<Integer> equator = DefaultEquator.defaultEquator();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(3, script.size());
        assertTrue(script.getCommand(0) instanceof DeleteCommand<?>);
        assertTrue(script.getCommand(1) instanceof InsertCommand<?>);
        assertTrue(script.getCommand(2) instanceof InsertCommand<?>);
        DeleteCommand<Integer> deleteCommand = (DeleteCommand<Integer>) script.getCommand(0);
        assertEquals(1, deleteCommand.getObject().intValue());
        InsertCommand<Integer> insertCommand1 = (InsertCommand<Integer>) script.getCommand(1);
        assertEquals(3, insertCommand1.getObject().intValue());
        InsertCommand<Integer> insertCommand2 = (InsertCommand<Integer>) script.getCommand(2);
        assertEquals(4, insertCommand2.getObject().intValue());
    }

    @Test(expected = RuntimeException.class)
    public void testSequencesComparator_InvalidComparison() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(2);
        sequence2.add(1);
        Equator<Integer> equator = new Equator<Integer>() {
            @Override
            public boolean equate(Object o1, Object o2) {
                throw new RuntimeException("Invalid comparison");
            }
        };
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
    }
}