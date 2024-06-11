package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;

import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparatorEmpty() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        assertTrue(script.isEmpty());
    }

    @Test
    public void testSequencesComparatorIdentical() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        assertTrue(script.isEmpty());
    }

    @Test
    public void testSequencesComparatorDifferentLengths() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<EditCommand<Integer>> commands = script.getCommands();
        assertEquals(3, commands.size());
        assertTrue(commands.get(0) instanceof InsertCommand);
        assertEquals(4, ((InsertCommand<Integer>) commands.get(0)).getObject().intValue());
        assertTrue(commands.get(1) instanceof KeepCommand);
        assertEquals(2, ((KeepCommand<Integer>) commands.get(1)).getObject().intValue());
        assertTrue(commands.get(2) instanceof DeleteCommand);
        assertEquals(3, ((DeleteCommand<Integer>) commands.get(2)).getObject().intValue());
    }

    @Test
    public void testSequencesComparatorEqualElementsDifferentPositions() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(2, 1, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<EditCommand<Integer>> commands = script.getCommands();
        assertEquals(3, commands.size());
        assertTrue(commands.get(0) instanceof SwapCommand);
        assertEquals(2, ((SwapCommand<Integer>) commands.get(0)).getObject1().intValue());
        assertEquals(2, ((SwapCommand<Integer>) commands.get(0)).getObject2().intValue());
        assertTrue(commands.get(1) instanceof KeepCommand);
        assertEquals(1, ((KeepCommand<Integer>) commands.get(1)).getObject().intValue());
        assertTrue(commands.get(2) instanceof KeepCommand);
        assertEquals(3, ((KeepCommand<Integer>) commands.get(2)).getObject().intValue());
    }

    @Test
    public void testSequencesComparatorDifferentElements() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 4, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<EditCommand<Integer>> commands = script.getCommands();
        assertEquals(3, commands.size());
        assertTrue(commands.get(0) instanceof DeleteCommand);
        assertEquals(2, ((DeleteCommand<Integer>) commands.get(0)).getObject().intValue());
        assertTrue(commands.get(1) instanceof InsertCommand);
        assertEquals(4, ((InsertCommand<Integer>) commands.get(1)).getObject().intValue());
        assertTrue(commands.get(2) instanceof KeepCommand);
        assertEquals(3, ((KeepCommand<Integer>) commands.get(2)).getObject().intValue());
    }

    @Test(expected = RuntimeException.class)
    public void testSequencesComparatorInternalError() {
        List<Integer> sequence1 = Arrays.asList(1, 2);
        List<Integer> sequence2 = Arrays.asList(1, 2);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, new ChainEquator<>(new ReverseEquator<>(), new IdentityEquator<>()));
        EditScript<Integer> script = comparator.getScript();
    }
}