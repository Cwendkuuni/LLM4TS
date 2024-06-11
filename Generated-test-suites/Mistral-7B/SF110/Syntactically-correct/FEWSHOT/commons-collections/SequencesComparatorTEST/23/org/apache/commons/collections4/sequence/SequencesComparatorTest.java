package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.functors.NullEqualator;
import org.apache.commons.collections4.iterators.ArrayIterator;
import org.apache.commons.collections4.iterators.ListIterator;
import org.apache.commons.collections4.list.ArrayListMultimap;
import org.apache.commons.collections4.list.ArrayListSentinalList;
import org.apache.commons.collections4.list.ListUtils;
import org.apache.commons.collections4.map.Multimap;
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
        List<Integer> sequence2 = sequence1;

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        assertNull(script);
    }

    @Test
    public void testNullSequences() {
        List<Integer> sequence1 = null;
        List<Integer> sequence2 = null;

        try {
            SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
            EditScript<Integer> script = comparator.getScript();
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testEmptySequence1() {
        List<Integer> sequence1 = Collections.emptyList();
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<EditCommand<Integer>> commands = new ArrayList<>();
        commands.add(new InsertCommand<>(1));
        commands.add(new InsertCommand<>(2));
        commands.add(new InsertCommand<>(3));

        assertEquals(commands, ListUtils.cast(script.getCommands()));
    }

    @Test
    public void testEmptySequence2() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Collections.emptyList();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<EditCommand<Integer>> commands = new ArrayList<>();
        commands.add(new DeleteCommand<>(1));
        commands.add(new DeleteCommand<>(2));
        commands.add(new DeleteCommand<>(3));

        assertEquals(commands, ListUtils.cast(script.getCommands()));
    }

    @Test
    public void testSequencesWithSameElementsButDifferentOrder() {
        List<Integer> sequence1 = Arrays.asList(2, 1, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<EditCommand<Integer>> commands = new ArrayList<>();
        commands.add(new SwapCommand<>(1, 0));
        commands.add(new SwapCommand<>(1, 1));

        assertEquals(commands, ListUtils.cast(script.getCommands()));
    }

    @Test
    public void testSequencesWithIdenticalSubsequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        List<Integer> sequence2 = Arrays.asList(1, 3, 2, 4, 5, 6, 7);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<EditCommand<Integer>> commands = new ArrayList<>();
        commands.add(new RearrangeCommand<>(Arrays.asList(1, 2), Arrays.asList(3, 2)));

        assertEquals(commands, ListUtils.cast(script.getCommands()));
    }

    @Test
    public void testSequencesWithDifferentLengths() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4, 5);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<EditCommand<Integer>> commands = new ArrayList<>();
        commands.add(new InsertCommand<>(5));

        assertEquals(commands, ListUtils.cast(script.getCommands()));
    }

    @Test
    public void testSequencesWithCommonPrefixAndSuffix() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<Integer> sequence2 = Arrays.asList(1, 4, 5, 6, 7, 8);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<EditCommand<Integer>> commands = new ArrayList<>();
        commands.add(new DeleteCommand<>(2));
        commands.add(new DeleteCommand<>(3));
        commands.add(new InsertCommand<>(4, 2));
        commands.add(new InsertCommand<>(5, 3));
        commands.add(new InsertCommand<>(7));
        commands.add(new InsertCommand<>(8));

        assertEquals(commands, ListUtils.cast(script.getCommands()));
    }

    @Test
    public void testSequencesWithNoCommonPrefixOrSuffix() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(6, 7, 8, 9, 10);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<EditCommand<Integer>> commands = new ArrayList<>();
        commands.add(new DeleteCommand<>(0, 5));
        commands.addAll(ListUtils.cast(script.getCommands()));
        commands.addAll(ListUtils.cast(Arrays.asList(
                new InsertCommand<>(6), new InsertCommand<>(7), new InsertCommand<>(8), new InsertCommand<>(9), new InsertCommand<>(10)
        )));

        assertEquals(commands, ListUtils.cast(script.getCommands()));
    }

    @Test
    public void testSequencesWithCommonPrefixAndSuffixButDifferentMiddleElements() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<Integer> sequence2 = Arrays.asList(1, 2, 7, 4, 5, 6);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<EditCommand<Integer>> commands = new ArrayList<>();
        commands.add(new DeleteCommand<>(2));
        commands.add(new DeleteCommand<>(3));
        commands.add(new InsertCommand<>(7));
        commands.addAll(ListUtils.cast(script.getCommands()));

        assertEquals(commands, ListUtils.cast(script.getCommands()));
    }

    @Test
    public void testSequencesWithMultipleSnakes() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> sequence2 = Arrays.asList(1, 4, 5, 6, 11, 7, 8, 12, 9, 10);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<EditCommand<Integer>> commands = new ArrayList<>();
        commands.add(new DeleteCommand<>(0, 3));
        commands.add(new InsertCommand<>(4, 2));
        commands.add(new DeleteCommand<>(5));
        commands.add(new InsertCommand<>(11, 5));
        commands.addAll(ListUtils.cast(script.getCommands()));

        assertEquals(commands, ListUtils.cast(script.getCommands()));
    }

    @Test
    public void testSequencesWithEqualLengthButDifferentElements() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);

        try {
            SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
            EditScript<Integer> script = comparator.getScript();
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSequencesWithDifferentLengthsButSameElements() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2);

        try {
            SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
            EditScript<Integer> script = comparator.getScript();
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSequencesComparatorWithNullEqualator() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(3, 2, 1);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, NullEqualator.INSTANCE);
        EditScript<Integer> script = comparator.getScript();

        List<EditCommand<Integer>> commands = new ArrayList<>();
        commands.add(new ReverseCommand<>(Arrays.asList(1, 2, 3)));

        assertEquals(commands, ListUtils.cast(script.getCommands()));
    }
}