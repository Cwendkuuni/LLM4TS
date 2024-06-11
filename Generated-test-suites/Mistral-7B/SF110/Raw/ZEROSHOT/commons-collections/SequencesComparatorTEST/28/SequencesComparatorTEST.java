package org.apache.commons.collections4.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.Functor;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.SequenceComparator;
import org.apache.commons.collections4.Sequences;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.EditScript;
import org.apache.commons.collections4.EditOperation;
import org.apache.commons.collections4.Edit;
import org.apache.commons.collections4.factory.CollectionsFactory;

public class SequencesComparatorTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSequencesComparator() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 3, 4, 5, 2));

        // Test default constructor
        SequenceComparator<Integer> comparator1 = new SequenceComparator<>(list1, list2);
        EditScript<Integer> script1 = comparator1.getScript();
        List<Edit<Integer>> operations1 = script1.toList(CollectionsFactory.newList());
        List<Integer> resultList1 = Sequences.reverse(Sequences.editOpsToList(operations1, list1));
        List<Integer> expectedList1 = Arrays.asList(1, 3, 1, 2, 4, 5);
        assertEquals(expectedList1, resultList1);

        // Test constructor with custom Equator
        Equator<Integer> customEquator = new Equator<Integer>() {
            @Override
            public boolean equate(Object o1, Object o2) {
                Integer i1 = (Integer) o1;
                Integer i2 = (Integer) o2;
                return i1 % 2 == i2 % 2;
            }
        };
        SequenceComparator<Integer> comparator2 = new SequenceComparator<>(list1, list2, customEquator);
        EditScript<Integer> script2 = comparator2.getScript();
        List<Edit<Integer>> operations2 = script2.toList(CollectionsFactory.newList());
        List<Integer> resultList2 = Sequences.reverse(Sequences.editOpsToList(operations2, list1));
        List<Integer> expectedList2 = Arrays.asList(1, 3, 1, 3, 4, 5);
        assertEquals(expectedList2, resultList2);

        // Test null input
        assertNull(new SequenceComparator<>(null, list2));
        assertNull(new SequenceComparator<>(list1, null));
    }
}