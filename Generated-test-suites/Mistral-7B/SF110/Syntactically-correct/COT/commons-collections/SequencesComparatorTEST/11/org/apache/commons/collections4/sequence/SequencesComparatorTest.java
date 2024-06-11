package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.functors.Equator;
import org.apache.commons.collections4.util.EditScript;
import org.apache.commons.collections4.util.EditScriptIterator;
import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparatorWithDefaultEquator() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>(getList1(), getList2());
        EditScript<Integer> script = comparator.getScript();
        List<Integer> expectedResult = getExpectedResult1();

        EditScriptIterator<Integer> iterator = script.getIterator();
        List<Integer> result = new ArrayList<>();
        while (iterator.hasNext()) {
            result.add(iterator.next().getObject());
        }

        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSequencesComparatorNullLists() {
        List<Integer> nullList1 = null;
        List<Integer> nullList2 = null;
        SequencesComparator<Integer> comparator = new SequencesComparator<>(nullList1, nullList2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSequencesComparatorDifferentSizes() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSequencesComparatorNullElementInLists() {
        List<Integer> list1 = Arrays.asList(1, null, 3);
        List<Integer> list2 = Arrays.asList(1, 2);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSequencesComparatorCustomEquatorNull() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2, 3);
        Equator<? super Integer> nullEquator = null;
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, nullEquator);
    }

    // Add test cases for other methods and potential edge cases/exceptions here

    private List<Integer> getList1() {
        return Arrays.asList(1, 2, 3, 4, 5);
    }

    private List<Integer> getList2() {
        return Arrays.asList(1, 2, 4, 5, 3);
    }

    private List<Integer> getExpectedResult1() {
        return Arrays.asList(1, 2, 4, 3, 5);
    }
}