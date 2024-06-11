package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.Functor;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.EditableIterable;
import org.apache.commons.collections4.EditScript;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.Transformers;
import org.junit.Test;

public class SequencesComparatorTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testSequencesComparator() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(4, 5, 1, 2, 3));

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        // Test keep commands
        List<Integer> expectedList = Arrays.asList(1, 4, 5);
        List<Integer> actualList = new ArrayList<>();
        EditableIterable<Integer> editList = new ChainedTransformer<>(Transformers.reverseTransformWith(new ConstantTransformer<Integer, Integer>(null)), Collections.emptyList(Integer.class), script);
        for (Integer i : editList) {
            actualList.add(i);
        }
        assertEquals(expectedList, actualList);

        // Test delete commands
        List<Integer> list1Delete = new ArrayList<>(Arrays.asList(1, 3, 4, 5));
        assertEquals(list1Delete, script.inverse().transform(list1));

        // Test insert commands
        List<Integer> list2Insert = new ArrayList<>(Arrays.asList(2, 1, 2, 3, 4));
        assertEquals(list2Insert, script.transform(list2));

        // Test different list sizes
        List<String> list3 = new ArrayList<>(Arrays.asList("a", "b", "c"));
        List<String> list4 = new ArrayList<>(Arrays.asList("b", "a"));
        SequencesComparator<String> comparatorString = new SequencesComparator<>(list3, list4);
        EditScript<String> scriptString = comparatorString.getScript();

        // Test empty lists
        List<String> emptyList1 = Collections.emptyList();
        List<String> emptyList2 = Collections.emptyList();
        SequencesComparator<String> comparatorEmpty = new SequencesComparator<>(emptyList1, emptyList2);
        EditScript<String> scriptEmpty = comparatorEmpty.getScript();
        assertEquals(Collections.emptyList(), scriptEmpty.transform(emptyList1));
        assertEquals(Collections.emptyList(), scriptEmpty.inverse().transform(emptyList1));

        // Test null inputs
        List<Object> listNull1 = null;
        List<Object> listNull2 = null;
        SequencesComparator<Object> comparatorNull = new SequencesComparator<>(listNull1, listNull2);
        assertNull(comparatorNull.getScript());

        // Test different types
        List<Integer> listInt1 = Arrays.asList(1, 2, 3);
        List<String> listStr2 = Arrays.asList("a", "b", "c");
        SequencesComparator<Integer, String> comparatorTypeMismatch = new SequencesComparator<>(listInt1, listStr2);
        EditScript<String> scriptTypeMismatch = comparatorTypeMismatch.getScript();
        assertNull(scriptTypeMismatch);

        // Test equator
        Equator<Integer> equator = new ComparatorEquator<>();
        SequencesComparator<Integer> comparatorEquator = new SequencesComparator<>(list1, list2, equator);
        EditScript<Integer> scriptEquator = comparatorEquator.getScript();
        List<Integer> actualListEquator = new ArrayList<>();
        EditableIterable<Integer> editListEquator = new ChainedTransformer<>(Transformers.reverseTransformWith(new ConstantTransformer<Integer, Integer>(null)), Collections.emptyList(Integer.class), scriptEquator);
        for (Integer i : editListEquator) {
            actualListEquator.add(i);
        }
        assertEquals(expectedList, actualListEquator);
    }

    static class ComparatorEquator<T> implements Equator<T> {

        @Override
        public boolean equate(T obj1, T obj2) {
            Comparator<T> comparator = new Comparator<T>() {
                @Override
                public int compare(T o1, T o2) {
                    return o1.toString().compareTo(o2.toString());
                }
            };
            return comparator.compare(obj1, obj2) == 0;
        }
    }
}