import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.Sequences;
import org.apache.commons.collections4.iterator.IteratorUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SequencesComparatorTest {

    @Test
    public void testEmptySequences() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        Iterator<EditCommand<Integer>> iterator = script.iterator();
        assertEquals(0, iterator.next().getNumberOfReplacements());
        assertEquals(0, iterator.next().getNumberOfDeletions());
        assertEquals(0, iterator.next().getNumberOfInsertions());
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.isEmpty());
    }

    @Test
    public void testIdenticalSequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        SequencesComparator comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        Iterator<EditCommand<Integer>> iterator = script.iterator();
        assertEquals(0, iterator.next().getNumberOfReplacements());
        assertEquals(0, iterator.next().getNumberOfDeletions());
        assertEquals(0, iterator.next().getNumberOfInsertions());
        assertTrue(iterator.isEmpty());
    }

    @Test
    public void testSequencesWithDuplicates() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 3, 2);
        SequencesComparator comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        Iterator<EditCommand<Integer>> iterator = script.iterator();
        assertEquals(1, iterator.next().getNumberOfDeletions());
        assertEquals(1, iterator.next().getNumberOfInsertions());
        assertEquals(3, iterator.next().getNumberOfReplacements());
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.isEmpty());
    }

    @Test
    public void testSequencesWithDuplicatesAndDifferentOrder() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 2, 3);
        List<Integer> sequence2 = Arrays.asList(2, 3, 3, 1, 2);
        SequencesComparator comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        Iterator<EditCommand<Integer>> iterator = script.iterator();
        assertEquals(3, iterator.next().getNumberOfDeletions());
        assertEquals(3, iterator.next().getNumberOfInsertions());
        assertEquals(3, iterator.next().getNumberOfReplacements());
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.isEmpty());
    }

    @Test
    public void testSequencesWithDifferentLengths() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4);
        SequencesComparator comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        Iterator<EditCommand<Integer>> iterator = script.iterator();
        assertEquals(1, iterator.next().getNumberOfInsertions());
        assertTrue(iterator.next().getNumberOfDeletions() == 0 && iterator.next().getNumberOfReplacements() == 0);
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.isEmpty());
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidSequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator comparator = new SequencesComparator<>(sequence1, sequence2);
        SequencesComparator.Snake middle = comparator.getComparator().getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
        assertNull(middle);
    }
}