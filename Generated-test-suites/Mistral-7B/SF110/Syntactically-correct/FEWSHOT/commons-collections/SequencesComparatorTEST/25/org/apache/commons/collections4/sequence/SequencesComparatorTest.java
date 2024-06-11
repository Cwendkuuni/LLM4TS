package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.apache.commons.collections4.IterableUtils.toList;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.equator.Equator;
import org.apache.commons.collections4.functors.ReplaceAllFunctor;
import org.apache.commons.collections4.iterators.Iterators;
import org.junit.Test;

import java.util.*;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparator() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 3, 2, 1));

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();

        List<Integer> expected = new ArrayList<>(Arrays.asList(1, 1, 3, 3, 5));
        List<Integer> actual = new ArrayList<>();
        Iterator<EditCommand<Integer>> iterator = Iterators.iterate(script);
        while (iterator.hasNext()) {
            EditCommand<Integer> command = iterator.next();
            if (command instanceof KeepCommand) {
                actual.add(((KeepCommand<Integer>) command).getElement());
            }
            if (command instanceof InsertCommand) {
                actual.add(((InsertCommand<Integer>) command).getElement());
            }
            if (command instanceof DeleteCommand) {
                actual.remove(Iterators.peek(iterator));
                iterator.next();
            }
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testSequencesComparatorWithCustomEquator() {
        List<String> sequence1 = new ArrayList<>(Arrays.asList("apple", "banana", "orange"));
        List<String> sequence2 = new ArrayList<>(Arrays.asList("orange", "banana", "apple"));

        Equator<String> customEquator = new Equator<>() {
            @Override
            public boolean equate(Object o1, Object o2) {
                return ((String) o1).equalsIgnoreCase((String) o2);
            }
        };

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, customEquator);
        EditScript<String> script = comparator.getScript();

        List<String> expected = new ArrayList<>(Arrays.asList("apple", "", "banana", "banana", "orange"));
        List<String> actual = new ArrayList<>();
        Iterator<EditCommand<String>> iterator = Iterators.iterate(script);
        while (iterator.hasNext()) {
            EditCommand<String> command = iterator.next();
            if (command instanceof KeepCommand) {
                actual.add(((KeepCommand<String>) command).getElement());
            }
            if (command instanceof InsertCommand) {
                actual.add(0, ((InsertCommand<String>) command).getElement());
            }
            if (command instanceof DeleteCommand) {
                actual.remove(((DeleteCommand<String>) command).getElementIndex());
            }
        }

        ListUtils.reverse(expected);
        ListUtils.reverse(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testSequencesComparatorEmptySequences() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();

        assertTrue(script.isEmpty());
    }

    @Test
    public void testSequencesComparatorNullSequences() {
        List<Integer> sequence1 = null;
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));

        try {
            new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        List<Integer> sequence2Null = null;
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));

        try {
            new SequencesComparator<>(sequence1, sequence2Null, DefaultEquator.defaultEquator());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSequencesComparatorNullSequenceElements() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, null, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, null, 5));

        try {
            new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}