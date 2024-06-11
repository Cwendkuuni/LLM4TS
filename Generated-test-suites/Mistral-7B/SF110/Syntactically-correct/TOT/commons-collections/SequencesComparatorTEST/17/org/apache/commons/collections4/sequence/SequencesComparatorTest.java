import org.apache.commons.collections4.*;
import org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

public class SequencesComparatorTest {

    @Test
    public void testEmptySequences() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        Assert.assertEquals(0, script.size());
    }

    @Test
    public void testEqualSequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        Assert.assertEquals(0, script.size());
    }

    @Test
    public void testDifferentSequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> sequence2 = Arrays.asList(1, 2, 5, 4);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<Command<Integer>> commands = script.getCommands();
        Assert.assertEquals(4, commands.size());
        Assert.assertEquals(CommandType.DELETE, commands.get(0).getType());
        Assert.assertEquals(CommandType.INSERT, commands.get(1).getType());
        Assert.assertEquals(CommandType.INSERT, commands.get(2).getType());
        Assert.assertEquals(CommandType.DELETE, commands.get(3).getType());
        Assert.assertEquals(4, commands.get(1).getIndex());
        Assert.assertEquals(5, commands.get(2).getIndex());
    }

    @Test(expected = RuntimeException.class)
    public void testInternalError() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 3, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        comparator.getScript();
    }

    @Test
    public void testNullSequence() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = null;

        try {
            SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
            comparator.getScript();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}