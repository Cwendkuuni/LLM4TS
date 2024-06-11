package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.EditableIterable;
import org.apache.commons.collections4.EditScript;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparatorWithDefaultEquator() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 3, 2));

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        assertEquals(3, script.size());
        List<EditCommand<Integer>> commands = script.getCommands();
        assertEquals(Commands.DELETE, commands.get(0).getCommandType());
        assertEquals(1, commands.get(0).getIndex());
        assertEquals(2, commands.get(1).getIndex());
        assertEquals(Commands.INSERT, commands.get(2).getCommandType());
        assertEquals(1, commands.get(2).getIndex());
        assertEquals((Integer) 3, commands.get(2).getElement());
    }

    @Test
    public void testSequencesComparatorWithCustomEquator() {
        List<String> list1 = new ArrayList<>(Arrays.asList("a", "b", "c"));
        List<String> list2 = new ArrayList<>(Arrays.asList("c", "b", "a"));

        SequencesComparator<String> comparator = new SequencesComparator<>(list1, list2, new CustomEquator());
        EditScript<String> script = comparator.getScript();

        assertEquals(3, script.size());
        List<EditCommand<String>> commands = script.getCommands();
        assertEquals(Commands.DELETE, commands.get(0).getCommandType());
        assertEquals(1, commands.get(0).getIndex());
        assertEquals(2, commands.get(1).getIndex());
        assertEquals(Commands.INSERT, commands.get(2).getCommandType());
        assertEquals(1, commands.get(2).getIndex());
        assertEquals((String) "c", commands.get(2).getElement());
    }

    @Test
    public void testEmptySequencesComparator() {
        List<String> list1 = Collections.emptyList();
        List<String> list2 = Collections.emptyList();

        SequencesComparator<String> comparator = new SequencesComparator<>(list1, list2);
        EditScript<String> script = comparator.getScript();

        assertEquals(0, script.size());
    }

    @Test(expected = RuntimeException.class)
    public void testSequencesComparatorInternalError() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 3, 4));

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        comparator.buildScript(0, list1.size(), 0, list2.size(), null);
    }

    @Test
    public void testSequencesComparatorWithSameSequences() {
        List<String> list1 = new ArrayList<>(Arrays.asList("a", "b", "c"));
        List<String> list2 = new ArrayList<>(Arrays.asList("a", "b", "c"));

        SequencesComparator<String> comparator = new SequencesComparator<>(list1, list2);
        EditScript<String> script = comparator.getScript();

        assertEquals(0, script.size());
    }

    @Test
    public void testSequencesComparatorWithIdenticalSequences() {
        List<String> list1 = new ArrayList<>(Collections.singletonList("foo"));
        List<String> list2 = new ArrayList<>(Collections.singletonList("foo"));

        SequencesComparator<String> comparator = new SequencesComparator<>(list1, list2);
        EditScript<String> script = comparator.getScript();

        assertEquals(0, script.size());
    }

    @Test
    public void testSequencesComparatorWithNullSequences() {
        List<String> list1 = null;
        List<String> list2 = new ArrayList<>(Arrays.asList("foo"));

        try {
            SequencesComparator<String> comparator = new SequencesComparator<>(list1, list2);
            comparator.getScript();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        List<String> list1Null = new ArrayList<>();
        list1Null.add(null);

        List<String> list2 = new ArrayList<>(Arrays.asList("foo"));

        try {
            SequencesComparator<String> comparator = new SequencesComparator<>(list1Null, list2);
            comparator.getScript();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        List<String> list1 = new ArrayList<>(Arrays.asList("foo"));
        List<String> list2Null = null;

        try {
            SequencesComparator<String> comparator = new SequencesComparator<>(list1, list2Null);
            comparator.getScript();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        List<String> list1Null = null;
        List<String> list2Null = null;

        try {
            SequencesComparator<String> comparator = new SequencesComparator<>(list1Null, list2Null);
            comparator.getScript();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSequencesComparatorWithDifferentLengthSequences() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 2));

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        assertEquals(2, script.size());
        List<EditCommand<Integer>> commands = script.getCommands();
        assertEquals(Commands.DELETE, commands.get(0).getCommandType());
        assertEquals(2, commands.get(0).getIndex());
        assertEquals(Commands.INSERT, commands.get(1).getCommandType());
        assertEquals(1, commands.get(1).getIndex());
        assertEquals((Integer) 3, commands.get(1).getElement());
    }

    @Test
    public void testSequencesComparatorWithIdenticalSubSequences() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 3, 2, 1));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 3, 2, 3, 2, 1));

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        assertEquals(3, script.size());
        List<EditCommand<Integer>> commands = script.getCommands();
        assertEquals(Commands.DELETE, commands.get(0).getCommandType());
        assertEquals(3, commands.get(0).getIndex());
        assertEquals(Commands.DELETE, commands.get(1).getCommandType());
        assertEquals(4, commands.get(1).getIndex());
        assertEquals(Commands.INSERT, commands.get(2).getCommandType());
        assertEquals(3, commands.get(2).getIndex());
        assertEquals((Integer) 3, commands.get(2).getElement());
    }

    @Test
    public void testSequencesComparatorWithDisjunctSequences() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(4, 5, 6));

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        assertEquals(6, script.size());
        List<EditCommand<Integer>> commands = script.getCommands();
        assertEquals(Commands.DELETE, commands.get(0).getCommandType());
        assertEquals(0, commands.get(0).getIndex());
        assertEquals(Commands.INSERT, commands.get(1).getCommandType());
        assertEquals(0, commands.get(1).getIndex());
        assertEquals((Integer) 4, commands.get(1).getElement());
        assertEquals(Commands.INSERT, commands.get(2).getCommandType());
        assertEquals(3, commands.get(2).getIndex());
        assertEquals((Integer) 5, commands.get(2).getElement());
        assertEquals(Commands.INSERT, commands.get(3).getCommandType());
        assertEquals(6, commands.get(3).getIndex());
        assertEquals((Integer) 6, commands.get(3).getElement());
        assertEquals(Commands.DELETE, commands.get(4).getCommandType());
        assertEquals(1, commands.get(4).getIndex());
        assertEquals(Commands.DELETE, commands.get(5).getCommandType());
        assertEquals(2, commands.get(5).getIndex());
    }

    private static final class CustomEquator implements Equator<String> {
        @Override
        public boolean equate(final String a, final String b) {
            return a.equalsIgnoreCase(b);
        }
    }

    private interface EditCommand<T> {
        CommandType getCommandType();

        int getIndex();

        T getElement();
    }

    private static final class KeepCommand<T> implements EditCommand<T> {
        private final T element;

        public KeepCommand(final T element) {
            this.element = element;
        }

        @Override
        public CommandType getCommandType() {
            return Commands.KEEP;
        }

        @Override
        public int getIndex() {
            return -1;
        }

        @Override
        public T getElement() {
            return this.element;
        }
    }

    private static final class DeleteCommand<T> implements EditCommand<T> {
        private final int index;

        public DeleteCommand(final int index) {
            this.index = index;
        }

        @Override
        public CommandType getCommandType() {
            return Commands.DELETE;
        }

        @Override
        public int getIndex() {
            return this.index;
        }

        @Override
        public T getElement() {
            return null;
        }
    }

    private static final class InsertCommand<T> implements EditCommand<T> {
        private final int index;
        private final T element;

        public InsertCommand(final T element, final int index) {
            this.index = index;
            this.element = element;
        }

        @Override
        public CommandType getCommandType() {
            return Commands.INSERT;
        }

        @Override
        public int getIndex() {
            return this.index;
        }

        @Override
        public T getElement() {
            return this.element;
        }
    }

    private static enum Commands {
        KEEP,
        DELETE,
        INSERT;
    }

}