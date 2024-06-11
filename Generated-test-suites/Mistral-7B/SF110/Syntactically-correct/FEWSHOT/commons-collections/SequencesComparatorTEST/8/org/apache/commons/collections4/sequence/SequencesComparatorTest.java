package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.functors.Equator;
import org.apache.commons.collections4.seq.EditScript;
import org.apache.commons.collections4.seq.KeepCommand;
import org.apache.commons.collections4.seq.SequenceComparator;
import org.apache.commons.collections4.seq.SequenceUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparator() {
        List<Integer> list1 = ListUtils.unmodifiableList(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = ListUtils.unmodifiableList(Arrays.asList(4, 3, 2, 1, 5));
        SequenceComparator<Integer> comparator = new SequenceComparator<>(list1, list2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        List<SequenceComparator.Command<Integer>> commands = SequenceUtils.asList(script);

        assertEquals(5, commands.size());
        assertEquals(KeepCommand.class, commands.get(0).getClass());
        assertEquals(1, commands.get(0).getObject().intValue());
        assertEquals(DeleteCommand.class, commands.get(1).getClass());
        assertEquals(2, commands.get(1).getObject().intValue());
        assertEquals(InsertCommand.class, commands.get(2).getClass());
        assertEquals(3, commands.get(2).getObject().intValue());
        assertEquals(KeepCommand.class, commands.get(3).getClass());
        assertEquals(4, commands.get(3).getObject().intValue());
        assertEquals(KeepCommand.class, commands.get(4).getClass());
        assertEquals(5, commands.get(4).getObject().intValue());
    }

    @Test
    public void testSequencesComparatorWithCustomEquator() {
        List<String> list1 = ListUtils.unmodifiableList(Arrays.asList("apples", "oranges", "bananas"));
        List<String> list2 = ListUtils.unmodifiableList(Arrays.asList("oranges", "bananas", "apples"));
        Equator<String> customEquator = new Equator<String>() {
            @Override
            public boolean equate(String s1, String s2) {
                return s1.compareToIgnoreCase(s2) == 0;
            }
        };
        SequenceComparator<String> comparator = new SequenceComparator<>(list1, list2, customEquator);
        EditScript<String> script = comparator.getScript();
        List<SequenceComparator.Command<String>> commands = SequenceUtils.asList(script);

        assertEquals(3, commands.size());
        assertEquals(InsertCommand.class, commands.get(0).getClass());
        assertEquals("oranges", commands.get(0).getObject());
        assertEquals(1, commands.get(1).getObject().intValue());
        assertEquals(DeleteCommand.class, commands.get(1).getClass());
        assertEquals("apples", commands.get(1).getObject());
        assertEquals(2, commands.get(2).getObject().intValue());
    }

    @Test
    public void testSequencesComparatorEmptyLists() {
        List<Integer> list1 = Collections.emptyList();
        List<Integer> list2 = Collections.emptyList();
        SequenceComparator<Integer> comparator = new SequenceComparator<>(list1, list2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        List<SequenceComparator.Command<Integer>> commands = SequenceUtils.asList(script);

        assertNull(commands);
    }

    @Test
    public void testSequencesComparatorNullLists() {
        List<Integer> list1 = null;
        List<Integer> list2 = ListUtils.unmodifiableList(Arrays.asList(1, 2, 3));
        SequenceComparator<Integer> comparator = new SequenceComparator<>(list1, list2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        List<SequenceComparator.Command<Integer>> commands = SequenceUtils.asList(script);

        assertNull(commands);
    }

    @Test(expected = RuntimeException.class)
    public void testSequencesComparatorInternalError() {
        List<Integer> list1 = ListUtils.unmodifiableList(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = ListUtils.unmodifiableList(Arrays.asList(2, 3, 4, 5, 1));
        SequenceComparator<Integer> comparator = new SequenceComparator<>(list1, list2, DefaultEquator.defaultEquator());
        comparator.getScript();
    }
}