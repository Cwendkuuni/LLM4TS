package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;

import java.util.*;

public class SequencesComparatorTest<T> {

    @SuppressWarnings("unchecked")
    @Test
    public void testSequencesComparator() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(3);
        list1.add(5);

        List<Integer> list2 = new ArrayList<>();
        list2.add(2);
        list2.add(1);
        list2.add(4);
        list2.add(5);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        assertEquals(1, script.size());
        assertEquals(InsertCommand.class, script.getClass());

        List<Integer> resultList = new ArrayList<>();
        script.applyTo(list1, resultList);

        assertEquals(list2, resultList);

        list1 = new ArrayList<>();
        list1.add(1);
        list1.add(3);
        list1.add(5);

        list2 = new ArrayList<>();
        list2.add(1);
        list2.add(3);
        list2.add(5);

        comparator = new SequencesComparator<>(list1, list2);
        script = comparator.getScript();

        assertEquals(0, script.size());

        list1 = new ArrayList<>();
        list1.add(1);
        list1.add(3);
        list1.add(5);

        list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(5);
        list2.add(5);

        comparator = new SequencesComparator<>(list1, list2);
        script = comparator.getScript();

        assertEquals(1, script.size());
        assertEquals(DeleteCommand.class, script.getClass());

        resultList = new ArrayList<>();
        script.applyTo(list1, resultList);

        assertEquals(list2, resultList);

        list1 = new ArrayList<>();
        list1.add(1);
        list1.add(3);
        list1.add(5);

        list2 = new ArrayList<>();
        list2.add(2);
        list2.add(1);
        list2.add(5);
        list2.add(5);

        comparator = new SequencesComparator<>(list1, list2);
        script = comparator.getScript();

        assertEquals(2, script.size());

        resultList = new ArrayList<>();
        script.applyTo(list1, resultList);

        List<Integer> expectedList = new ArrayList<>();
        expectedList.add(2);
        expectedList.add(5);

        assertEquals(expectedList, resultList);

        list1 = new ArrayList<>();
        list1.add(1);
        list1.add(3);
        list1.add(5);

        list2 = new ArrayList<>();
        list2.add(1);
        list2.add(3);
        list2.add(5);
        list2.add(5);

        comparator = new SequencesComparator<>(list1, list2);
        script = comparator.getScript();

        assertEquals(0, script.size());

        list1 = new ArrayList<>();
        list1.add(1);
        list1.add(3);
        list1.add(5);

        list2 = new ArrayList<>();
        list2.add(1);
        list2.add(3);
        list2.add(5);
        list2.add(5);
        list2.add(6);

        comparator = new SequencesComparator<>(list1, list2);
        script = comparator.getScript();

        assertEquals(1, script.size());
        assertEquals(InsertCommand.class, script.getClass());

        resultList = new ArrayList<>();
        script.applyTo(list1, resultList);

        assertEquals(list2, resultList);

        list1 = new ArrayList<>();
        list1.add(1);
        list1.add(3);
        list1.add(5);

        list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(5);
        list2.add(5);
        list2.add(6);

        comparator = new SequencesComparator<>(list1, list2);
        script = comparator.getScript();

        assertEquals(3, script.size());

        resultList = new ArrayList<>();
        script.applyTo(list1, resultList);

        expectedList = new ArrayList<>();
        expectedList.add(2);
        expectedList.add(5);
        expectedList.add(6);

        assertEquals(expectedList, resultList);

        list1 = new ArrayList<>();
        list1.add(1);
        list1.add(3);
        list1.add(5);

        list2 = new ArrayList<>();
        list2.add(1);
        list2.add(3);
        list2.add(5);
        list2.add(5);
        list2.add(6);
        list2.add(7);

        comparator = new SequencesComparator<>(list1, list2);
        script = comparator.getScript();

        assertEquals(4, script.size());

        resultList = new ArrayList<>();
        script.applyTo(list1, resultList);

        expectedList = new ArrayList<>();
        expectedList.add(7);
        expectedList.add(5);
        expectedList.add(6);
        expectedList.add(3);

        assertEquals(expectedList, resultList);

        list1 = new ArrayList<>();
        list1.add(1);
        list1.add(3);
        list1.add(5);

        list2 = new ArrayList<>();
        list2.add(1);
        list2.add(3);
        list2.add(5);
        list2.add(5);
        list2.add(6);
        list2.add(7);
        list2.add(8);

        comparator = new SequencesComparator<>(list1, list2);
        script = comparator.getScript();

        assertEquals(5, script.size());

        resultList = new ArrayList<>();
        script.applyTo(list1, resultList);

        assertEquals(list2, resultList);

        list1 = new ArrayList<>();
        list1.add(1);
        list1.add(3);
        list1.add(5);

        list2 = new ArrayList<>();
        list2.add(1);
        list2.add(3);
        list2.add(5);
        list2.add(5);
        list2.add(6);
        list2.add(7);
        list2.add(8);
        list2.add(9);

        comparator = new SequencesComparator<>(list1, list2);
        script = comparator.getScript();

        assertEquals(6, script.size());

        resultList = new ArrayList<>();
        script.applyTo(list1, resultList);

        expectedList = new ArrayList<>();
        expectedList.add(9);
        expectedList.add(8);
        expectedList.add(7);
        expectedList.add(6);
        expectedList.add(5);
        expectedList.add(3);

        assertEquals(expectedList, resultList);

        list1 = new ArrayList<>();
        list1.add(1);
        list1.add(3);
        list1.add(5);

        list2 = new ArrayList<>();
        list2.add(1);
        list2.add(3);
        list2.add(5);
        list2.add(5);
        list2.add(6);
        list2.add(7);
        list2.add(8);
        list2.add(9);
        list2.add(10);

        comparator = new SequencesComparator<>(list1, list2);
        script = comparator.getScript();

        assertEquals(7, script.size());

        resultList = new ArrayList<>();
        script.applyTo(list1, resultList);

        expectedList = new ArrayList<>();
        expectedList.add(10);
        expectedList.add(9);
        expectedList.add(8);
        expectedList.add(7);
        expectedList.add(6);
        expectedList.add(5);
        expectedList.add(3);

        assertEquals(expectedList, resultList);

        list1 = new ArrayList<>();
        list1.add(1);
        list1.add(3);
        list1.add(5);

        list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(5);
        list2.add(5);
        list2.add(6);
        list2.add(7);
        list2.add(8);
        list2.add(9);
        list2.add(10);

        comparator = new SequencesComparator<>(list1, list2);
        script = comparator.getScript();

        assertEquals(8, script.size());

        resultList = new ArrayList<>();
        script.applyTo(list1, resultList);

        expectedList = new ArrayList<>();
        expectedList.add(10);
        expectedList.add(9);
        expectedList.add(8);
        expectedList.add(7);
        expectedList.add(6);
        expectedList.add(5);
        expectedList.add(2);
        expectedList.add(3);

        assertEquals(expectedList, resultList);
    }

    @Test
    public void testSequencesComparatorNull() {
        List<Integer> list1 = null;
        List<Integer> list2 = new ArrayList<>();
        list2.add(1);

        try {
            new SequencesComparator<>(list1, list2);
            fail();
        } catch (NullPointerException expected) {
        }

        list1 = new ArrayList<>();
        list1.add(1);
        list1.add(3);
        list1.add(5);

        list2 = null;

        try {
            new SequencesComparator<>(list1, list2);
            fail();
        } catch (NullPointerException expected) {
        }
    }
}