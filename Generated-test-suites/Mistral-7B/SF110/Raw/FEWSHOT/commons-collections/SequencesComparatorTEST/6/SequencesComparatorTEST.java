package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.Functor;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.equator.DefaultEquator;
import org.apache.commons.collections4.functors.ChainedEquator;
import org.apache.commons.collections4.functors.EquatorPredicate;
import org.apache.commons.collections4.functors.EquatorTransformer;
import org.apache.commons.collections4.iterators.TransformIterator;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparator() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 3, 3, 4, 5, 5);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();

        List<Integer> expectedResult = Arrays.asList(1, 3, 4, 5);
        List<Integer> result = new ArrayList<>();
        TransformIterator<Integer> transformer = new TransformIterator<>(new EquatorTransformer<>(), new EquatorPredicate<>());
        List<Integer> keys = ListUtils.unionWith(sequence1, sequence2);
        for (Object cmd : script.getCommands()) {
            if (cmd instanceof KeepCommand<?>) {
                transformer.setNext(Collections.singletonList((Integer) ((KeepCommand<Integer>) cmd).getKey()));
                result.addAll(transformer.next());
            }
        }
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSequencesComparatorWithCustomEquator() {
        List<String> sequence1 = Arrays.asList("apple", "banana", "cherry", "date");
        List<String> sequence2 = Arrays.asList("date", "cherry", "banana", "apple");

        Equator<String> customEquator = new Equator<String>() {
            @Override
            public boolean equate(Object a, Object b) {
                String strA = (String) a;
                String strB = (String) b;
                return strA.length() == strB.length() && strA.regionMatches(false, 0, strB, 0, strA.length());
            }
        };

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, customEquator);
        EditScript<String> script = comparator.getScript();

        List<String> expectedResult = Arrays.asList("date", "cherry");
        List<String> result = new ArrayList<>();
        TransformIterator<String> transformer = new TransformIterator<>(new EquatorTransformer<>(), new EquatorPredicate<>());
        List<String> keys = ListUtils.unionWith(sequence1, sequence2);
        for (Object cmd : script.getCommands()) {
            if (cmd instanceof KeepCommand<?>) {
                transformer.setNext(Collections.singletonList((String) ((KeepCommand<String>) cmd).getKey()));
                result.addAll(transformer.next());
            }
        }
        assertEquals(expectedResult, result);
    }

    @Test(expected = RuntimeException.class)
    public void testSequencesComparatorWithInvalidInput() {
        List<Object> sequence1 = Arrays.asList(1, "two", 3.0);
        List<Object> sequence2 = Arrays.asList("one", 2, 3.0, 4);

        SequencesComparator<Object> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
    }
}