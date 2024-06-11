package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Sequences;
import org.apache.commons.collections4.functors.Equator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SequencesComparatorTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testSequencesComparator() {
        Equator<Integer> equator = new Equator<Integer>() {
            @Override
            public boolean equate(Object o1, Object o2) {
                return ((Comparable<Integer>) o1).compareTo((Comparable<Integer>) o2) == 0;
            }
        };

        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        EditScript<Integer> script = comparator.getScript();

        List<EditCommand<Integer>> expectedCommands = new ArrayList<>();
        expectedCommands.add(new InsertCommand<>(5));
        expectedCommands.add(new DeleteCommand<>(1));
        expectedCommands.add(new KeepCommand<>(2));
        expectedCommands.add(new KeepCommand<>(3));
        expectedCommands.add(new KeepCommand<>(4));
        expectedCommands.add(new KeepCommand<>(5));

        List<EditCommand<Integer>> actualCommands = Sequences.editOperationsToList(script);

        Comparator<EditCommand<Integer>> commandComparator = Comparator.comparingInt(editCommand -> editCommand.getPosition());
        Collections.sort(expectedCommands, commandComparator);
        Collections.sort(actualCommands, commandComparator);

        assertEquals(expectedCommands, actualCommands);
    }

    @Test
    public void testEmptySequencesComparator() {
        List<Integer> sequence1 = Collections.emptyList();
        List<Integer> sequence2 = Collections.emptyList();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        assertEquals(Collections.emptyList(), Sequences.editOperationsToList(script));
    }

    @Test
    public void testSequencesComparatorWithNulls() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, null, 2, null, 3, null));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(3, null, 2, null, 1, null));

        Equator<Integer> equator = new Equator<Integer>() {
            @Override
            public boolean equate(Object o1, Object o2) {
                return ((Comparable<Integer>) o1) == null ? ((Comparable<Integer>) o2) == null : ((Comparable<Integer>) o1).compareTo((Comparable<Integer>) o2) == 0;
            }
        };

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        EditScript<Integer> script = comparator.getScript();

        List<EditCommand<Integer>> expectedCommands = new ArrayList<>();
        expectedCommands.add(new KeepCommand<>(null));
        expectedCommands.add(new KeepCommand<>(2));
        expectedCommands.add(new KeepCommand<>(null));
        expectedCommands.add(new KeepCommand<>(3));
        expectedCommands.add(new KeepCommand<>(null));
        expectedCommands.add(new KeepCommand<>(null));

        List<EditCommand<Integer>> actualCommands = Sequences.editOperationsToList(script);

        Comparator<EditCommand<Integer>> commandComparator = Comparator.comparingInt(editCommand -> editCommand.getPosition());
        Collections.sort(expectedCommands, commandComparator);
        Collections.sort(actualCommands, commandComparator);

        assertEquals(expectedCommands, actualCommands);
    }

    @Test(expected = RuntimeException.class)
    public void testSequencesComparatorWithDifferentLengths() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        comparator.getScript();

        // The test should throw a RuntimeException
    }

    @Test(expected = RuntimeException.class)
    public void testSequencesComparatorWithNullComparator() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3));

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, null);

        // The test should throw a RuntimeException
    }
}