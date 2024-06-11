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
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.functors.KeepCommand;
import org.apache.commons.collections4.functors.ReplaceCommand;
import org.apache.commons.collections4.functors.SwapCommand;
import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparator_emptySequences() {
        List<Integer> sequence1 = Collections.emptyList();
        List<Integer> sequence2 = Collections.emptyList();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        assertNull(script);
    }

    @Test
    public void testSequencesComparator_identicalSequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        assertNull(script);
    }

    @Test
    public void testSequencesComparator_differentSequences_equalLength() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(3, 2, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        List<Object> commands = Arrays.asList(new SwapCommand<Integer>(1, 1), new SwapCommand<Integer>(2, 2));
        assertCommandsEqual(script.getCommands(), commands);
    }

    @Test
    public void testSequencesComparator_differentSequences_differingLengths() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(3, 2, 1, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        List<Object> commands = Arrays.asList(new DeleteCommand<Integer>(sequence1.get(2)), new InsertCommand<Integer>(sequence2.get(3)),
                new InsertCommand<Integer>(sequence2.get(4)));
        assertCommandsEqual(script.getCommands(), commands);
    }

    @Test
    public void testSequencesComparator_differentSequences_multipleCommands() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> sequence2 = Arrays.asList(4, 3, 2, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        List<Object> commands = Arrays.asList(new SwapCommand<Integer>(1, 3), new SwapCommand<Integer>(2, 2), new DeleteCommand<Integer>(sequence1.get(3)),
                new InsertCommand<Integer>(sequence2.get(0)), new InsertCommand<Integer>(sequence2.get(1)), new InsertCommand<Integer>(sequence2.get(2)));
        assertCommandsEqual(script.getCommands(), commands);
    }

    @Test
    public void testSequencesComparator_keepCommandsAtEnd() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> sequence2 = Arrays.asList(4, 3, 2, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        List<Object> commands = Arrays.asList(new SwapCommand<Integer>(1, 3), new SwapCommand<Integer>(2, 2), new KeepCommand<Integer>(sequence1.get(3)),
                new KeepCommand<Integer>(sequence2.get(0)), new KeepCommand<Integer>(sequence2.get(1)), new KeepCommand<Integer>(sequence2.get(2)));
        assertCommandsEqual(script.getCommands(), commands);
    }

    @Test
    public void testSequencesComparator_multipleSnakes() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> sequence2 = Arrays.asList(9, 8, 7, 6, 5, 4, 3, 2, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        List<Object> commands = Arrays.asList(new SwapCommand<Integer>(1, 9), new SwapCommand<Integer>(2, 8), new SwapCommand<Integer>(3, 7),
                new SwapCommand<Integer>(4, 6), new SwapCommand<Integer>(5, 5), new DeleteCommand<Integer>(sequence1.get(6)),
                new InsertCommand<Integer>(sequence2.get(1)), new InsertCommand<Integer>(sequence2.get(2)), new InsertCommand<Integer>(sequence2.get(3)),
                new InsertCommand<Integer>(sequence2.get(4)), new KeepCommand<Integer>(sequence1.get(7)), new KeepCommand<Integer>(sequence1.get(8)),
                new KeepCommand<Integer>(sequence1.get(9)));
        assertCommandsEqual(script.getCommands(), commands);
    }

    @Test
    public void testSequencesComparator_reverseSnake() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<Integer> sequence2 = Arrays.asList(6, 5, 4, 3, 2, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        List<Object> commands = Arrays.asList(new SwapCommand<Integer>(1, 6), new SwapCommand<Integer>(2, 5), new SwapCommand<Integer>(3, 4),
                new SwapCommand<Integer>(4, 3), new SwapCommand<Integer>(5, 2), new SwapCommand<Integer>(6, 1));
        assertCommandsEqual(script.getCommands(), commands);
    }

    @Test
    public void testSequencesComparator_multipleEqualSequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        assertNull(script);
    }

    @Test
    public void testSequencesComparator_multipleIdenticalSnakes() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(3, 2, 1, 1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        List<Object> commands = Arrays.asList(new SwapCommand<Integer>(1, 3), new SwapCommand<Integer>(2, 2), new SwapCommand<Integer>(2, 3),
                new SwapCommand<Integer>(1, 3));
        assertCommandsEqual(script.getCommands(), commands);
    }

    @Test(expected = RuntimeException.class)
    public void testSequencesComparator_internalError() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 1, 2, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        throw new RuntimeException("Unexpected null script.");
    }

    private void assertCommandsEqual(List<Object> actualCommands, List<Object> expectedCommands) {
        List<Object> actualCommandsCopy = new ArrayList<>(actualCommands);
        List<Object> expectedCommandsCopy = new ArrayList<>(expectedCommands);
        while (!actualCommandsCopy.isEmpty() && !expectedCommandsCopy.isEmpty()) {
            Object actualCommand = actualCommandsCopy.remove(0);
            Object expectedCommand = expectedCommandsCopy.remove(0);
            assertEquals(actualCommand, expectedCommand);
        }
        assertSame(Collections.emptyList(), actualCommandsCopy);
        assertSame(Collections.emptyList(), expectedCommandsCopy);
    }
}