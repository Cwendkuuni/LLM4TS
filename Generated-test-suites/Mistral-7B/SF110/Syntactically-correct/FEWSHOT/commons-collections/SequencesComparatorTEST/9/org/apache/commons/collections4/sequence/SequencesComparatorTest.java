package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.EditScript;
import org.apache.commons.collections4.functors.KeepCommand;
import org.apache.commons.collections4.functors.InsertCommand;
import org.apache.commons.collections4.functors.DeleteCommand;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparator() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(2, 3, 4, 5, 1));

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        List<Command<? super Integer>> commands = new ArrayList<>();
        while (script.hasNext()) {
            commands.add(script.nextCommand());
        }

        List<Integer> expected = new ArrayList<>(Arrays.asList(2, 3, 4, 5, 1));
        List<Integer> result = new ArrayList<>();

        for (Command<? super Integer> command : commands) {
            if (command instanceof KeepCommand) {
                result.add(((KeepCommand<Integer>) command).getElement());
            } else if (command instanceof InsertCommand) {
                result.add(((InsertCommand<Integer>) command).getElement());
            } else {
                result.remove(result.size() - 1);
            }
        }

        assertEquals(expected, result);
    }

    @Test
    public void testSequencesComparatorEmptySequences() {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        List<Command<? super Integer>> commands = new ArrayList<>();
        while (script.hasNext()) {
            commands.add(script.nextCommand());
        }

        assertTrue(commands.isEmpty());
    }

    @Test
    public void testSequencesComparatorNullSequences() {
        List<Integer> list1 = null;
        List<Integer> list2 = Arrays.asList(1, 2, 3, 4, 5);

        try {
            SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        List<Integer> list1_nonNull = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2_empty = new ArrayList<>();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1_nonNull, list2_empty);
        EditScript<Integer> script = comparator.getScript();

        List<Command<? super Integer>> commands = new ArrayList<>();
        while (script.hasNext()) {
            commands.add(script.nextCommand());
        }

        assertTrue(commands.isEmpty());
    }

    @Test
    public void testSequencesComparatorNullEquator() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = new ArrayList<>();

        try {
            SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();

        List<Command<? super Integer>> commands = new ArrayList<>();
        while (script.hasNext()) {
            commands.add(script.nextCommand());
        }

        List<Integer> expected = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> result = new ArrayList<>();

        for (Command<? super Integer> command : commands) {
            if (command instanceof KeepCommand) {
                result.add(((KeepCommand<Integer>) command).getElement());
            } else if (command instanceof InsertCommand) {
                result.add(((InsertCommand<Integer>) command).getElement());
            } else {
                result.remove(result.size() - 1);
            }
        }

        assertEquals(expected, result);
    }
}