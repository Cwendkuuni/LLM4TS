package org.apache.commons.collections4.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.Functor;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.SeqUtils;
import org.apache.commons.collections4.sequence.EditScript;
import org.apache.commons.collections4.sequence.SequencesComparator;

public class SequencesComparatorTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSequencesComparator() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 3, 4, 2));

        // Test default constructor
        SequencesComparator<Integer> comparator1 = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script1 = comparator1.getScript();
        List<Integer> result1 = SeqUtils.sequenceRollback(script1, list1);
        List<Integer> expected1 = Arrays.asList(1, 2, 3, 4);
        assertEquals(expected1, result1);

        // Test constructor with custom Equator
        Equator<Integer> customEquator = new Equator<Integer>() {
            @Override
            public boolean equate(Object o1, Object o2) {
                return ((Integer)o1).intValue() > ((Integer)o2).intValue();
            }
        };
        SequencesComparator<Integer> comparator2 = new SequencesComparator<>(list1, list2, customEquator);
        EditScript<Integer> script2 = comparator2.getScript();
        List<Integer> result2 = SeqUtils.sequenceRollback(script2, list1);
        List<Integer> expected2 = Arrays.asList(3, 2, 1, 4);
        assertEquals(expected2, result2);

        // Test null input
        assertNull(new SequencesComparator<>(null, list2));
        assertNull(new SequencesComparator<>(list1, null));

        // Test empty sequences
        List<Integer> emptyList1 = Collections.emptyList();
        List<Integer> emptyList2 = Collections.emptyList();
        SequencesComparator<Integer> comparator3 = new SequencesComparator<>(emptyList1, emptyList2);
        EditScript<Integer> script3 = comparator3.getScript();
        List<Integer> result3 = SeqUtils.sequenceRollback(script3, emptyList1);
        List<Integer> expected3 = Collections.emptyList();
        assertEquals(expected3, result3);

        // Test sequences with same elements
        List<String> list3 = new ArrayList<>(Arrays.asList("a", "a", "b"));
        List<String> list4 = new ArrayList<>(Arrays.asList("a", "a", "b"));
        SequencesComparator<String> comparator4 = new SequencesComparator<>(list3, list4);
        EditScript<String> script4 = comparator4.getScript();
        List<String> result4 = SeqUtils.sequenceRollback(script4, list3);
        List<String> expected4 = list3;
        assertEquals(expected4, result4);

        // Test sequences with different sizes
        List<Character> list5 = new ArrayList<>(Arrays.asList('a', 'b', 'c'));
        List<Character> list6 = new ArrayList<>(Arrays.asList('a', 'd', 'e'));
        SequencesComparator<Character> comparator5 = new SequencesComparator<>(list5, list6);
        EditScript<Character> script5 = comparator5.getScript();
        List<Character> result5 = SeqUtils.sequenceRollback(script5, list5);
        List<Character> expected5 = Arrays.asList('a', 'b', 'c');
        assertEquals(expected5, result5);

        // Test sequences with different elements
        List<Integer> list7 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> list8 = new ArrayList<>(Arrays.asList(4, 5, 6));
        SequencesComparator<Integer> comparator6 = new SequencesComparator<>(list7, list8);
        EditScript<Integer> script6 = comparator6.getScript();
        List<Integer> result6 = SeqUtils.sequenceRollback(script6, list7);
        List<Integer> expected6 = Arrays.asList();
        assertEquals(expected6, result6);

        // Test sequences with different orders but same elements
        List<Integer> list9 = new ArrayList<>(Arrays.asList(1, 3, 2, 4));
        List<Integer> list10 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        SequencesComparator<Integer> comparator7 = new SequencesComparator<>(list9, list10);
        EditScript<Integer> script7 = comparator7.getScript();
        List<Integer> result7 = SeqUtils.sequenceRollback(script7, list9);
        List<Integer> expected7 = Arrays.asList();
        assertEquals(expected7, result7);
    }
}