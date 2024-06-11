package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.functors.Equator;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.iterators.EmptyIterator;
import org.apache.commons.collections4.iterators.SingletonIterator;
import org.apache.commons.collections4.list.ArrayListMultimap;
import org.apache.commons.collections4.list.ListUtils;
import org.junit.Test;

public class SequencesComparatorTest<T> {

    @SuppressWarnings("unchecked")
    private static <T> List<T> listOf(T... elements) {
        return new ArrayList<T>(ListUtils.asList(elements));
    }

    @Test
    public void testConstructor_SequencesWithEqualElements() {
        List<Integer> sequence1 = listOf(1, 2, 3);
        List<Integer> sequence2 = listOf(1, 2, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();

        assertEquals(new ArrayList<EditCommand<Integer>>(), script.getCommands());
    }

    @Test
    public void testConstructor_SequencesWithDifferentSizes() {
        List<Integer> sequence1 = listOf(1, 2, 3);
        List<Integer> sequence2 = listOf(1, 2);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();

        assertEquals(new ArrayList<EditCommand<Integer>>() {{
            add(new InsertCommand<>(3));
            add(new DeleteCommand<>(sequence2.get(1)));
        }}, script.getCommands());
    }

    @Test
    public void testConstructor_SequencesWithSameSizeButDifferentElements() {
        List<Integer> sequence1 = listOf(1, 2, 3);
        List<Integer> sequence2 = listOf(2, 1, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();

        assertEquals(new ArrayList<EditCommand<Integer>>() {{
            add(new SwapCommand<>(sequence1.get(1), sequence2.get(1)));
        }}, script.getCommands());
    }

    @Test
    public void testConstructor_SequencesWithNullElements() {
        List<String> sequence1 = listOf("a", null, "c");
        List<String> sequence2 = listOf("a", "b", "c");

        try {
            new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        List<String> sequence1Null = new ArrayList<String>();
        List<String> sequence2 = listOf("a", "b", "c");

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1Null, sequence2, DefaultEquator.defaultEquator());
        EditScript<String> script = comparator.getScript();

        assertEquals(new ArrayList<EditCommand<String>>() {{
            add(new InsertCommand<String>(null));
            add(new DeleteCommand<String>(sequence2.get(0)));
            add(new DeleteCommand<String>(sequence2.get(1)));
        }}, script.getCommands());
    }

    @Test
    public void testConstructor_SequencesWithNullSequences() {
        List<String> sequence1 = null;
        List<String> sequence2 = listOf("a", "b", "c");

        try {
            new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        List<String> sequence1 = listOf("a", "b", "c");
        List<String> sequence2 = null;

        try {
            new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testGetScript_EmptySequences() {
        List<String> sequence1 = EmptyIterator.emptyList();
        List<String> sequence2 = EmptyIterator.emptyList();

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<String> script = comparator.getScript();

        assertEquals(EmptyIterator.emptyList(), script.getCommands());
    }

    @Test
    public void testGetScript_SequencesWithEqualElementsOrdered() {
        List<Integer> sequence1 = listOf(1, 2, 3);
        List<Integer> sequence2 = listOf(3, 2, 1);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();

        assertEquals(EmptyIterator.emptyList(), script.getCommands());
    }

    @Test
    public void testGetScript_SequencesWithEqualElementsRandomOrder() {
        List<Integer> sequence1 = listOf(3, 1, 2);
        List<Integer> sequence2 = listOf(1, 2, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();

        assertEquals(EmptyIterator.emptyList(), script.getCommands());
    }

    @Test
    public void testGetScript_SequencesWithDifferentElements() {
        List<Integer> sequence1 = listOf(1, 2, 3);
        List<Integer> sequence2 = listOf(4, 5, 6);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();

        assertEquals(new ArrayList<EditCommand<Integer>>() {{
            add(new InsertCommand<Integer>(4));
            add(new InsertCommand<Integer>(5));
            add(new InsertCommand<Integer>(6));
            add(new DeleteCommand<Integer>(sequence1.get(0)));
            add(new DeleteCommand<Integer>(sequence1.get(1)));
        }}, script.getCommands());
    }

    @Test
    public void testGetScript_SequencesWithIdenticalSubSequences() {
        List<Integer> sequence1 = listOf(1, 2, 1, 3, 1, 2, 1);
        List<Integer> sequence2 = listOf(1, 2, 1, 3, 1, 2, 1, 1, 1);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();

        assertEquals(new ArrayList<EditCommand<Integer>>() {{
            add(new DeleteCommand<Integer>(sequence2.get(sequence2.size() - 1)));
            add(new DeleteCommand<Integer>(sequence2.get(sequence2.size() - 1)));
            add(new DeleteCommand<Integer>(sequence2.get(sequence2.size() - 1)));
        }}, script.getCommands());
    }

    @Test
    public void testGetScript_SequencesWithIdenticalSubSequencesInReverseOrder() {
        List<Integer> sequence1 = listOf(1, 2, 1, 3, 1, 2, 1);
        List<Integer> sequence2 = listOf(1, 2, 1, 3, 1, 2, SingletonIterator.singleton(1).iterator().next(), SingletonIterator.singleton(1).iterator().next());

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();

        assertEquals(new ArrayList<EditCommand<Integer>>() {{
            add(new DeleteCommand<Integer>(sequence2.get(0)));
            add(new DeleteCommand<Integer>(sequence2.get(0)));
            add(new DeleteCommand<Integer>(sequence2.get(0)));
        }}, script.getCommands());
    }

    @Test
    public void testGetScript_SequencesWithDifferentLengthsButIdenticalSubSequencesAtStart() {
        List<Integer> sequence1 = listOf(1, 2, 1, 3, 1, 2, 1);
        List<Integer> sequence2 = listOf(1, 2, 1, 3, 1, 2);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();

        assertEquals(EmptyIterator.emptyList(), script.getCommands());
    }

    @Test
    public void testGetScript_SequencesWithDifferentLengthsButIdenticalSubSequencesAtEnd() {
        List<Integer> sequence1 = listOf(1, 2, 1, 3, 1, 2, 1);
        List<Integer> sequence2 = listOf(1, 2, 1, 3, SingletonIterator.singleton(1).iterator().next(), SingletonIterator.singleton(1).iterator().next(), SingletonIterator.singleton(1).iterator().next());

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();

        assertEquals(EmptyIterator.emptyList(), script.getCommands());
    }

    @Test
    public void testGetScript_SequencesWithIdenticalSubSequencesButDifferentOrder() {
        List<Integer> sequence1 = listOf(1, 2, 1, 3, 1, 2, 1);
        List<Integer> sequence2 = listOf(1, 1, 1, 3, 2, 1, 2);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();

        assertEquals(new ArrayList<EditCommand<Integer>>() {{
            add(new SwapCommand<>(sequence1.get(2), sequence1.get(4)));
            add(new SwapCommand<>(sequence1.get(3), sequence1.get(5)));
            add(new SwapCommand<>(sequence1.get(5), sequence1.get(3)));
            add(new SwapCommand<>(sequence1.get(4), sequence1.get(2)));
            add(new SwapCommand<>(sequence2.get(1), sequence2.get(2)));
            add(new SwapCommand<>(sequence2.get(2), sequence2.get(1)));
            add(new SwapCommand<>(sequence2.get(2), sequence2.get(3)));
            add(new SwapCommand<>(sequence2.get(3), sequence2.get(2)));
        }}, script.getCommands());
    }

    @Test
    public void testGetScript_SequencesWithIdenticalSubSequencesButDifferentLengths() {
        List<Integer> sequence1 = listOf(1, 2, 1, 3, 1, 2, 1);
        List<Integer> sequence2 = listOf(1, 2, 1, 3, 1, 2, 1, 1);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();

        assertEquals(new ArrayList<EditCommand<Integer>>() {{
            add(new DeleteCommand<Integer>(sequence2.get(sequence2.size() - 1)));
        }}, script.getCommands());
    }

    @Test
    public void testGetScript_SequencesWithIdenticalSubSequencesButDifferentStartPosition() {
        List<Integer> sequence1 = listOf(1, 2, 1, 3, 1, 2, 1);
        List<Integer> sequence2 = listOf(2, 1, 1, 3, 1, 2, 1);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();

        assertEquals(new ArrayList<EditCommand<Integer>>() {{
            add(new SwapCommand<>(sequence1.get(0), sequence2.get(0)));
        }}, script.getCommands());
    }

    @Test
    public void testGetScript_SequencesWithIdenticalSubSequencesButDifferentEndPosition() {
        List<Integer> sequence1 = listOf(1, 2, 1, 3, 1, 2, 1);
        List<Integer> sequence2 = listOf(1, 2, 1, 3, 1, 2);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();

        assertEquals(EmptyIterator.emptyList(), script.getCommands());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetScript_SequencesWithNullElements() {
        List<String> sequence1 = listOf("a", null, "c");
        List<String> sequence2 = listOf("a", "b", "c");

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        comparator.getScript();
    }

    @Test(expected = NullPointerException.class)
    public void testGetScript_SequencesWithNullSequences() {
        List<String> sequence1 = null;
        List<String> sequence2 = listOf("a", "b", "c");

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        comparator.getScript();
    }
}