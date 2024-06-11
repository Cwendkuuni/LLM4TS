package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Sequences;
import org.apache.commons.collections4.seq.EditScript;
import org.junit.Test;

import java.util.*;

public class SequencesComparatorTest {

    @Test
    public void testEmptySequences() {
        List<Integer> sequence1 = Collections.emptyList();
        List<Integer> sequence2 = Collections.emptyList();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertNull(script);
    }

    @Test
    public void testIdenticalSequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        assertEquals(0, script.size());
    }

    @Test
    public void testSequencesWithDifferences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(2, 3, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> commands = script.getCommands();
        assertEquals(3, commands.size());
        DeleteCommand delCmd = (DeleteCommand<Integer>) commands.get(0);
        assertEquals(1, delCmd.getIndex());
        InsertCommand insCmd = (InsertCommand<Integer>) commands.get(1);
        assertEquals(2, insCmd.getIndex());
        assertEquals(2, insCmd.getElement().intValue());
        KeepCommand keepCmd = (KeepCommand<Integer>) commands.get(2);
        assertEquals(3, keepCmd.getIndex());
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidInput() {
        List<Integer> sequence1 = Arrays.asList(1, 2);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidInput2() {
        List<Integer> sequence1 = Arrays.asList(1, 2);
        List<Integer> sequence2 = Arrays.asList(1, 2, null);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidInput3() {
        List<String> sequence1 = Arrays.asList("one", "two");
        List<String> sequence2 = Arrays.asList("one", "two", "three");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
    }

    @Test
    public void testCustomEquator() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> sequence2 = Arrays.asList(4, 3, 2, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, new CustomEquator());
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> commands = script.getCommands();
        assertEquals(7, commands.size());
        DeleteCommand delCmd1 = (DeleteCommand<Integer>) commands.get(0);
        assertEquals(0, delCmd1.getIndex());
        InsertCommand insCmd1 = (InsertCommand<Integer>) commands.get(1);
        assertEquals(1, insCmd1.getIndex());
        assertEquals(4, insCmd1.getElement().intValue());
        DeleteCommand delCmd2 = (DeleteCommand<Integer>) commands.get(2);
        assertEquals(2, delCmd2.getIndex());
        KeepCommand keepCmd1 = (KeepCommand<Integer>) commands.get(3);
        assertEquals(3, keepCmd1.getIndex());
        DeleteCommand delCmd3 = (DeleteCommand<Integer>) commands.get(4);
        assertEquals(4, delCmd3.getIndex());
        InsertCommand insCmd2 = (InsertCommand<Integer>) commands.get(5);
        assertEquals(2, insCmd2.getIndex());
        assertEquals(3, insCmd2.getElement().intValue());
        KeepCommand keepCmd2 = (KeepCommand<Integer>) commands.get(6);
        assertEquals(5, keepCmd2.getIndex());
    }

    private static class CustomEquator implements Equator<Comparable> {

        @Override
        public boolean equate(Comparable o1, Comparable o2) {
            if (o1 instanceof Integer && o2 instanceof Integer) {
                Integer i1 = (Integer) o1;
                Integer i2 = (Integer) o2;
                return i1 % 2 == i2 % 2;
            }
            throw new UnsupportedOperationException();
        }
    }
}