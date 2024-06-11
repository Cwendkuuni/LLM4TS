package org.apache.commons.collections4.sequence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.functors.Equator;
import org.apache.commons.collections4.functors.KeepCommand;
import org.apache.commons.collections4.functors.InsertCommand;
import org.apache.commons.collections4.functors.DeleteCommand;
import org.apache.commons.collections4.functors.EditScript;
import org.junit.Test;

public class SequencesComparatorTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testSequencesComparator() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 3, 4, 5, 2));

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();

        List<Object> expected = Arrays.asList(new KeepCommand<>(2), new DeleteCommand<>(5), new InsertCommand<>(3), new KeepCommand<>(4), new KeepCommand<>(5), new InsertCommand<>(2));
        assertEquals(expected, script.toList());

        list1 = new ArrayList<>(Arrays.asList(1, 2, 2, 2, 3, 4, 4, 4, 4, 5));
        list2 = new ArrayList<>(Arrays.asList(1, 1, 2, 2, 3, 3, 4, 4, 5, 5));

        comparator = new SequencesComparator<>(list1, list2, DefaultEquator.defaultEquator());
        script = comparator.getScript();

        expected = Arrays.asList(new DeleteCommand<>(2), new DeleteCommand<>(2), new DeleteCommand<>(2), new InsertCommand<>(1), new KeepCommand<>(3), new KeepCommand<>(3), new KeepCommand<>(3), new KeepCommand<>(4), new KeepCommand<>(4), new KeepCommand<>(4), new KeepCommand<>(5), new KeepCommand<>(5), new KeepCommand<>(5));
        assertEquals(expected, script.toList());

        list1 = new ArrayList<>(Collections.singletonList(1));
        list2 = new ArrayList<>(Collections.singletonList(1));

        comparator = new SequencesComparator<>(list1, list2, DefaultEquator.defaultEquator());
        script = comparator.getScript();

        assertNull(script);

        list1 = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1));
        list2 = new ArrayList<>(Collections.singletonList(1));

        comparator = new SequencesComparator<>(list1, list2, DefaultEquator.defaultEquator());
        script = comparator.getScript();

        expected = Arrays.asList(new DeleteCommand<>(1), new DeleteCommand<>(1), new DeleteCommand<>(1), new DeleteCommand<>(1), new DeleteCommand<>(1), new InsertCommand<>(1));
        assertEquals(expected, script.toList());
    }
}