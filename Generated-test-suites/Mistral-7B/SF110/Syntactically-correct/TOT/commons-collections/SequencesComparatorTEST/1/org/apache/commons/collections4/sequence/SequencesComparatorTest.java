import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Sequences;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.*;

public class SequencesComparatorTest {

    @Test
    public void testEmptySequences() {
        List<Integer> sequence1 = Collections.emptyList();
        List<Integer> sequence2 = Collections.emptyList();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        Assert.assertEquals(0, script.size());
    }

    @Test
    public void testIdenticalSequences() {
        List<String> sequence1 = Arrays.asList("apple", "banana", "cherry");
        List<String> sequence2 = Arrays.asList("apple", "banana", "cherry");

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = comparator.getScript();

        Assert.assertEquals(0, script.size());
    }

    @Test
    public void testSequencesWithDifferences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 2, 6, 4, 5);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<InsertCommand<Integer>> insertCommands = new ArrayList<>();
        List<DeleteCommand<Integer>> deleteCommands = new ArrayList<>();
        List<KeepCommand<Integer>> keepCommands = new ArrayList<>();

        while (!script.isEmpty()) {
            Object cmd = script.poll();
            if (cmd instanceof InsertCommand) {
                insertCommands.add((InsertCommand<Integer>) cmd);
            }
            else if (cmd instanceof DeleteCommand) {
                deleteCommands.add((DeleteCommand<Integer>) cmd);
            }
            else {
                keepCommands.add((KeepCommand<Integer>) cmd);
            }
        }

        List<Integer> expectedResult = ListUtils.union(sequence1, sequence2);
        List<Integer> result = new ArrayList<>();

        for (KeepCommand<Integer> cmd : keepCommands) {
            result.add(cmd.getValue());
        }

        for (int i = 0; i < deleteCommands.size(); ++i) {
            result.remove(i);
        }

        for (int i = 0; i < insertCommands.size(); ++i) {
            result.add(insertCommands.get(i).getValue());
        }

        Assert.assertEquals(expectedResult, result);
    }

    @Test(expected = RuntimeException.class)
    public void testSnakeBuildingException() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 3, 2);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        comparator.getScript();
    }

    @Test
    public void testNullEquator() {
        List<String> sequence1 = Arrays.asList("apple", "banana", "cherry");
        List<String> sequence2 = Arrays.asList("apple", "banana", "cherry");

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, null);
        try {
            comparator.getScript();
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected
        }
    }

    @Test
    public void testSameSizeSequencesWithEqualElements() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(3, 2, 1);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<Integer> expectedResult = Arrays.asList(1, 2, 3);
        List<Integer> result = new ArrayList<>();

        while (!script.isEmpty()) {
            Object cmd = script.poll();
            if (cmd instanceof KeepCommand) {
                result.add(((KeepCommand<Integer>) cmd).getValue());
            }
        }

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testEmptySequence1() {
        List<Integer> sequence1 = Collections.emptyList();
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<Integer> expectedResult = Arrays.asList(1, 2, 3);
        List<Integer> result = new ArrayList<>();

        while (!script.isEmpty()) {
            Object cmd = script.poll();
            if (cmd instanceof InsertCommand) {
                result.add(((InsertCommand<Integer>) cmd).getValue());
            }
        }

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testEmptySequence2() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Collections.emptyList();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<Integer> expectedResult = Arrays.asList(1, 2, 3);
        List<Integer> result = new ArrayList<>();

        while (!script.isEmpty()) {
            Object cmd = script.poll();
            if (cmd instanceof DeleteCommand) {
                result.add(((DeleteCommand<Integer>) cmd).getValue());
            }
        }

        Assert.assertEquals(expectedResult, result.stream().map(i -> (Integer) i).collect(Collectors.toList()));
    }
}