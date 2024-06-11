package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.functors.Equator;
import org.apache.commons.collections4.impl.list.ArrayListX;
import org.apache.commons.collections4.util.EditScript;
import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testEmptySequences() {
        List<Integer> sequence1 = new ArrayListX<>();
        List<Integer> sequence2 = new ArrayListX<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        assertNull(script);
    }

    @Test
    public void testEqualsSequences() {
        List<Integer> sequence1 = new ArrayListX<>(ListUtils.list(1, 2, 3));
        List<Integer> sequence2 = new ArrayListX<>(ListUtils.list(1, 2, 3));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        EditScript expectedScript = new EditScript<>();
        expectedScript.append(new KeepCommand<>(1));
        expectedScript.append(new KeepCommand<>(2));
        expectedScript.append(new KeepCommand<>(3));
        assertEquals(expectedScript, script);
    }

    @Test
    public void testUnequalSequences() {
        List<Integer> sequence1 = new ArrayListX<>(ListUtils.list(1, 2, 3));
        List<Integer> sequence2 = new ArrayListX<>(ListUtils.list(2, 3, 1));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        EditScript expectedScript = new EditScript<>();
        expectedScript.append(new DeleteCommand<>(1));
        expectedScript.append(new InsertCommand<>(2));
        expectedScript.append(new InsertCommand<>(3));
        expectedScript.append(new KeepCommand<>(1));
        assertEquals(expectedScript, script);
    }

    @Test
    public void testSequencesWithSameLengthButNotEqual() {
        List<Integer> sequence1 = new ArrayListX<>(ListUtils.list(1, 2, 3));
        List<Integer> sequence2 = new ArrayListX<>(ListUtils.list(1, 3, 2));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        EditScript expectedScript = new EditScript<>();
        expectedScript.append(new DeleteCommand<>(2));
        expectedScript.append(new InsertCommand<>(3));
        expectedScript.append(new InsertCommand<>(2));
        assertEquals(expectedScript, script);
    }

    @Test
    public void testSequencesWithDifferentLengthsButEqualTillEnd() {
        List<Integer> sequence1 = new ArrayListX<>(ListUtils.list(1, 2, 3, 4));
        List<Integer> sequence2 = new ArrayListX<>(ListUtils.list(1, 2, 3));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        EditScript expectedScript = new EditScript<>();
        expectedScript.append(new KeepCommand<>(1));
        expectedScript.append(new KeepCommand<>(2));
        expectedScript.append(new KeepCommand<>(3));
        assertEquals(expectedScript, script);
    }

    @Test
    public void testSequencesWithDifferentLengthsAndNotEqualTillEnd() {
        List<Integer> sequence1 = new ArrayListX<>(ListUtils.list(1, 2, 3, 4));
        List<Integer> sequence2 = new ArrayListX<>(ListUtils.list(1, 2, 5));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        EditScript<Integer> script = comparator.getScript();
        EditScript expectedScript = new EditScript<>();
        expectedScript.append(new DeleteCommand<>(4));
        expectedScript.append(new InsertCommand<>(5));
        assertEquals(expectedScript, script);
    }
}