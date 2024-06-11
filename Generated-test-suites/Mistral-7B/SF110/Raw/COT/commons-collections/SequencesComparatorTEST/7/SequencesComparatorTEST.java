package org.apache.commons.collections4.sequence.test;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.equator.DefaultEquator;
import org.apache.commons.collections4.functors.ChainedPredicate;
import org.apache.commons.collections4.functors.PredicateCombinator;
import org.apache.commons.collections4.iterators.EmptyIterator;
import org.apache.commons.collections4.iterators.Iterators;
import org.apache.commons.collections4.sequence.EditScript;
import org.apache.commons.collections4.sequence.SequencesComparator;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparatorEmpty() {
        List<Integer> sequence1 = Collections.emptyList();
        List<Integer> sequence2 = Collections.emptyList();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        Assert.assertEquals(EmptyIterator.instance(), script.iterator());
    }

    @Test
    public void testSequencesComparatorEquals() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        Assert.assertEquals(Collections.emptyList(), script.toList());
    }

    @Test
    public void testSequencesComparatorNotEquals() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 5, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        List<Object> expected = ListUtils.union(ListUtils.difference(sequence1, sequence2), ListUtils.difference(sequence2, sequence1));
        Assert.assertEquals(expected, script.toList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSequencesComparatorNullLists() {
        List<Integer> sequence1 = null;
        List<Integer> sequence2 = null;
        new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
    }

    @Test(expected = NullPointerException.class)
    public void testSequencesComparatorNullElements() {
        List<Integer> sequence1 = Arrays.asList(1, null, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4, null);
        new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSequencesComparatorDifferentSizes() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
    }

    @Test
    public void testSequencesComparatorGetScriptEdgeCases() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4, 5, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        List<Object> expected = ListUtils.difference(ListUtils.difference(sequence1, sequence2), ListUtils.difference(sequence2, sequence1));
        Assert.assertEquals(expected, script.toList());

        sequence1 = Arrays.asList(1, 2, 3, 4, 5, 5);
        sequence2 = Arrays.asList(1, 2, 3, 4, 5);
        comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        script = comparator.getScript();
        expected = ListUtils.difference(ListUtils.difference(sequence1, sequence2), Collections.singletonList(sequence1.get(sequence1.size() - 1)));
        Assert.assertEquals(expected, script.toList());
    }

    @Test
    public void testSequencesComparatorGetScriptExceptionHandling() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, null, 5);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        try {
            comparator.getScript();
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        sequence1 = Arrays.asList(1, 2, null, 3, 4, 5);
        sequence2 = Arrays.asList(1, 2, 3, 4, 5);
        comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        try {
            comparator.getScript();
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        sequence1 = Arrays.asList(1, 2, 3, 4, null, 5);
        sequence2 = Arrays.asList(1, 2, 3, 4, 5);
        comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        try {
            comparator.getScript();
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        sequence1 = Arrays.asList(1, 2, 3, 4, 5, null);
        sequence2 = Arrays.asList(1, 2, 3, 4, 5);
        comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        try {
            comparator.getScript();
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}