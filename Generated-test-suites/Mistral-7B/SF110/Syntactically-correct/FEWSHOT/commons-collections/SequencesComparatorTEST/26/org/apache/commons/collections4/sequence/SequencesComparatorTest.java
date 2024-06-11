package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.Functor;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.equator.DefaultEquator;
import org.apache.commons.collections4.transformer.ChainTransformer;
import org.apache.commons.collections4.transformer.Transformer;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparator_EmptySequences() {
        List<Integer> sequence1 = Collections.emptyList();
        List<Integer> sequence2 = Collections.emptyList();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> editScript = comparator.getScript();

        Iterator<EditCommand<Integer>> iterator = editScript.iterator();
        assertEquals(0, iterator.next().getOperation().size());
        assertEquals(0, iterator.next().getOperation().size());
        assertNull(iterator.hasNext());
    }

    @Test
    public void testSequencesComparator_EqualSequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> editScript = comparator.getScript();

        Iterator<EditCommand<Integer>> iterator = editScript.iterator();
        assertEquals(0, iterator.next().getOperation().size());
        assertNull(iterator.hasNext());
    }

    @Test
    public void testSequencesComparator_DifferentSequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> sequence2 = Arrays.asList(1, 3, 5, 2, 4);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> editScript = comparator.getScript();

        Iterator<EditCommand<Integer>> iterator = editScript.iterator();
        assertEquals(-1, iterator.next().getOperation().get(0).intValue());
        assertEquals(2, iterator.next().getOperation().size());
        assertEquals(4, iterator.next().getOperation().get(0).intValue());
        assertEquals(2, iterator.next().getOperation().get(1).intValue());
        assertNull(iterator.hasNext());
    }

    @Test
    public void testSequencesComparator_WithCustomEquator() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(3, 2, 1);

        Equator<Integer> customEquator = new Equator<Integer>() {
            @Override
            public boolean equate(Object obj1, Object obj2) {
                return ((Integer) obj1).intValue() == ((Integer) obj2).intValue() * 2;
            }
        };

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, customEquator);
        EditScript<Integer> editScript = comparator.getScript();

        Iterator<EditCommand<Integer>> iterator = editScript.iterator();
        assertEquals(3, iterator.next().getOperation().size());
        assertEquals(-1, iterator.next().getOperation().get(0).intValue());
        assertEquals(1, iterator.next().getOperation().get(0).intValue());
        assertEquals(1, iterator.next().getOperation().get(1).intValue());
        assertEquals(2, iterator.next().getOperation().get(2).intValue());
        assertNull(iterator.hasNext());
    }
}