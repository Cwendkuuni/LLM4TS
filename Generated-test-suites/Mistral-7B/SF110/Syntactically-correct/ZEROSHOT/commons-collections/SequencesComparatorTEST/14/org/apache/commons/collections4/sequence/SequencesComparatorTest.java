package org.apache.commons.collections4.sequence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.Functor;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.EditScript;
import org.apache.commons.collections4.iterator.ListIteratorAdapter;
import org.apache.commons.collections4.iterators.EmptyIterator;
import org.apache.commons.collections4.test.EditableTestClass;
import org.junit.Test;

public class SequencesComparatorTest<T> {

    @SuppressWarnings("unchecked")
    private static Equator<Object> defaultEquator() {
        return (Equator<Object>) DefaultEquator.defaultEquator();
    }

    @SuppressWarnings("unchecked")
    private static Functor<Object, Object> identityFunctor() {
        return (Functor<Object, Object>) (Function) o -> o;
    }

    @Test
    public void testEmptySequences() {
        List<Integer> sequence1 = Collections.emptyList();
        List<Integer> sequence2 = Collections.emptyList();
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertNotNull(script);
        assertEquals(EmptyIterator.class, script.getClass());
    }

    @Test
    public void testIdentitySequences() {
        List<String> sequence1 = new ArrayList<String>(Collections.singletonList("abc"));
        List<String> sequence2 = sequence1;
        SequencesComparator<String> comparator = new SequencesComparator<String>(sequence1, sequence2);
        EditScript<String> script = comparator.getScript();
        ListIteratorAdapter<String> listIterator = script.iterator();
        assertEquals(new KeepCommand<String>("abc"), listIterator.next());
        assertEquals(EmptyIterator.class, listIterator.hasNext() ? listIterator.next().getClass() : null);
    }

    @Test
    public void testSequencesWithDifferences() {
        List<Integer> sequence1 = new ArrayList<Integer>(Collections.singletonList(1));
        List<Integer> sequence2 = new ArrayList<Integer>(Collections.singletonList(2));
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        ListIteratorAdapter<EditCommand<Integer>> listIterator = script.iterator();
        assertEquals(new DeleteCommand<Integer>(1), listIterator.next());
        assertEquals(new InsertCommand<Integer>(2), listIterator.next());
        assertEquals(EmptyIterator.class, listIterator.hasNext() ? listIterator.next().getClass() : null);
    }

    @Test
    public void testSequencesWithLongerFirstSequence() {
        List<Integer> sequence1 = new ArrayList<Integer>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<Integer>(Arrays.asList(1, 2));
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        ListIteratorAdapter<EditCommand<Integer>> listIterator = script.iterator();
        assertEquals(new DeleteCommand<Integer>(3), listIterator.next());
        assertEquals(EmptyIterator.class, listIterator.hasNext() ? listIterator.next().getClass() : null);
    }

    @Test
    public void testSequencesWithLongerSecondSequence() {
        List<Integer> sequence1 = new ArrayList<Integer>(Arrays.asList(1, 2));
        List<Integer> sequence2 = new ArrayList<Integer>(Arrays.asList(1, 2, 3));
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        ListIteratorAdapter<EditCommand<Integer>> listIterator = script.iterator();
        assertEquals(new InsertCommand<Integer>(3), listIterator.next());
        assertEquals(EmptyIterator.class, listIterator.hasNext() ? listIterator.next().getClass() : null);
    }

    @Test
    public void testSequencesWithDiagonalDifferences() {
        List<String> sequence1 = new ArrayList<String>(Arrays.asList("ab", "cd"));
        List<String> sequence2 = new ArrayList<String>(Arrays.asList("ac", "db"));
        SequencesComparator<String> comparator = new SequencesComparator<String>(sequence1, sequence2, identityFunctor());
        EditScript<String> script = comparator.getScript();
        ListIteratorAdapter<EditCommand<String>> listIterator = script.iterator();
        assertEquals(new DeleteCommand<String>("d"), listIterator.next());
        assertEquals(new InsertCommand<String>("c"), listIterator.next());
        assertEquals(new DeleteCommand<String>("b"), listIterator.next());
        assertEquals(new InsertCommand<String>("b"), listIterator.next());
        assertEquals(EmptyIterator.class, listIterator.hasNext() ? listIterator.next().getClass() : null);
    }

    @Test(expected = RuntimeException.class)
    public void testSequencesWithError() {
        List<Integer> sequence1 = new ArrayList<Integer>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
    }
}