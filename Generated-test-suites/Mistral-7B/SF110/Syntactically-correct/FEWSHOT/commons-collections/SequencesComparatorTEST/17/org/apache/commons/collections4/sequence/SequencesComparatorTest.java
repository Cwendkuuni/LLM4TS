package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.EditScript;
import org.apache.commons.collections4.functors.KeepCommand;
import org.apache.commons.collections4.functors.InsertCommand;
import org.apache.commons.collections4.functors.DeleteCommand;

public class SequencesComparatorTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testSequencesComparator() {
        List<Integer> list1 = new LinkedList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new LinkedList<Integer>(Arrays.asList(1, 2, 6, 4, 5));

        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        List<Integer> expected = new LinkedList<Integer>(Arrays.asList(new KeepCommand<>(1), new DeleteCommand<>(3), new InsertCommand<>(6)));
        assertScriptsEqual(expected, script);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSequencesComparator_empty_sequences() {
        List<Integer> list1 = Collections.emptyList();
        List<Integer> list2 = Collections.emptyList();

        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        List<Integer> expected = Collections.emptyList();
        assertScriptsEqual(expected, script);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSequencesComparator_equal_sequences() {
        List<Integer> list1 = new LinkedList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new LinkedList<Integer>(Arrays.asList(1, 2, 3, 4, 5));

        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        List<Integer> expected = Collections.emptyList();
        assertScriptsEqual(expected, script);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSequencesComparator_reverse_sequences() {
        List<Integer> list1 = new LinkedList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new LinkedList<Integer>(Arrays.asList(5, 4, 3, 2, 1));

        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        List<Integer> expected = new LinkedList<Integer>(Arrays.asList(new DeleteCommand<>(1), new DeleteCommand<>(2), new DeleteCommand<>(3), new DeleteCommand<>(4), new InsertCommand<>(5), new InsertCommand<>(4), new InsertCommand<>(3), new InsertCommand<>(2), new InsertCommand<>(1)));
        assertScriptsEqual(expected, script);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = RuntimeException.class)
    public void testSequencesComparator_internal_error() {
        List<Integer> list1 = new LinkedList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new LinkedList<Integer>(Arrays.asList(1, 2, 3, 5, 4));

        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        throw new RuntimeException("Internal Error");
    }

    private void assertScriptsEqual(List<Integer> expected, EditScript<Integer> script) {
        List<Integer> actual = new LinkedList<Integer>();
        for (Object command : script.getCommands()) {
            if (command instanceof KeepCommand) {
                actual.add(((KeepCommand<Integer>) command).getObject());
            } else if (command instanceof InsertCommand) {
                actual.add(((InsertCommand<Integer>) command).getObject());
            } else if (command instanceof DeleteCommand) {
                actual.remove(((DeleteCommand<Integer>) command).getObject());
            }
        }
        assertArrayEquals(expected.toArray(), actual.toArray());
    }
}