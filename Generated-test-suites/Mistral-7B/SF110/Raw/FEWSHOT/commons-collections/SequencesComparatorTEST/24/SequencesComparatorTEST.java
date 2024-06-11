package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Sequences;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.functors.Equator;
import org.apache.commons.collections4.functors.KeepCommand;
import org.apache.commons.collections4.functors.InsertCommand;
import org.apache.commons.collections4.functors.DeleteCommand;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparator() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(2, 3, 4, 5, 1));
        Equator<Integer> equator = DefaultEquator.defaultEquator();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
        EditScript<Integer> script = comparator.getScript();

        List<Object> expected = ListUtils.unionWithOrder(list1, ListUtils.difference(list1, list2));
        List<Object> actual = new ArrayList<>();
        script.applyTo(Lists.newArrayList(list1), actual);

        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testEmptySequencesComparator() {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        Equator<Integer> equator = DefaultEquator.defaultEquator();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
        EditScript<Integer> script = comparator.getScript();

        List<Object> expected = new ArrayList<>();
        List<Object> actual = new ArrayList<>();
        script.applyTo(Lists.newArrayList(list1), actual);

        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testSequencesComparatorWithNullLists() {
        List<Integer> list1 = null;
        List<Integer> list2 = null;
        Equator<Integer> equator = DefaultEquator.defaultEquator();

        try {
            new SequencesComparator<>(list1, list2, equator);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        list1 = new ArrayList<>();
        list2 = null;

        try {
            new SequencesComparator<>(list1, list2, equator);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        list1 = null;
        list2 = new ArrayList<>();

        try {
            new SequencesComparator<>(list1, list2, equator);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        list1 = null;
        list2 = null;

        try {
            new SequencesComparator<>(list1, list2, equator);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSequencesComparatorWithNullEquator() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(2, 3, 4, 5, 1);
        Equator<Integer> equator = null;

        try {
            new SequencesComparator<>(list1, list2, equator);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSequencesComparatorEqualSequences() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(1, 2, 3, 4, 5);
        Equator<Integer> equator = DefaultEquator.defaultEquator();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
        EditScript<Integer> script = comparator.getScript();

        List<Object> expected = new ArrayList<>();
        List<Object> actual = new ArrayList<>();
        script.applyTo(Lists.newArrayList(list1), actual);

        assertArrayEquals(expected.toArray(), actual.toArray());

        assertEquals("Empty script", "", script.toString());
    }

    @Test
    public void testSequencesComparatorKeepCommand() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(1, 4, 3, 4, 5);
        Equator<Integer> equator = DefaultEquator.defaultEquator();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
        EditScript<Integer> script = comparator.getScript();

        List<Object> expected = new ArrayList<>();
        List<Object> actual = new ArrayList<>();
        script.applyTo(Lists.newArrayList(list1), actual);

        assertEquals(1, actual.size());
        assertSame(list1.get(0), actual.get(0));

        assertEquals("KeepCommand[1]", script.toString());
    }

    @Test
    public void testSequencesComparatorDeleteCommand() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(1, 3, 4, 5);
        Equator<Integer> equator = DefaultEquator.defaultEquator();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
        EditScript<Integer> script = comparator.getScript();

        List<Object> expected = new ArrayList<>();
        List<Object> actual = new ArrayList<>();
        script.applyTo(Lists.newArrayList(list1), actual);

        assertEquals(1, actual.size());
        assertNull(actual.get(0));

        assertEquals("DeleteCommand[1]", script.toString());
    }

    @Test
    public void testSequencesComparatorInsertCommand() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(1, 2, 4, 3, 5);
        Equator<Integer> equator = DefaultEquator.defaultEquator();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
        EditScript<Integer> script = comparator.getScript();

        List<Object> expected = new ArrayList<>();
        List<Object> actual = new ArrayList<>();
        script.applyTo(Lists.newArrayList(list1), actual);

        assertEquals(1, actual.size());
        assertEquals(4, ((Integer)actual.get(0)).intValue());

        assertEquals("InsertCommand[4]", script.toString());
    }

    @Test
    public void testSequencesComparatorGetScriptNullLists() {
        List<Integer> list1 = null;
        List<Integer> list2 = null;
        Equator<Integer> equator = DefaultEquator.defaultEquator();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);

        try {
            comparator.getScript();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testSequencesComparatorGetScriptEmptyLists() {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        Equator<Integer> equator = DefaultEquator.defaultEquator();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);

        EditScript<Integer> script = comparator.getScript();

        assertEquals("Empty script", "", script.toString());
    }

}