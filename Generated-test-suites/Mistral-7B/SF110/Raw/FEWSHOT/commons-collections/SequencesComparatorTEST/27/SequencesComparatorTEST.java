package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.EditScript;
import org.apache.commons.collections4.functors.KeepCommand;
import org.apache.commons.collections4.functors.InsertCommand;
import org.apache.commons.collections4.functors.DeleteCommand;
import org.apache.commons.collections4.SequenceComparator;
import org.apache.commons.collections4.Transformation;
import org.apache.commons.collections4.iterators.TransformIterator;

public class SequencesComparatorTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testSequenceComparison() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 3, 2, 1));

        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        Transformation<Integer, Integer> keepCommand = new Transformation<Integer, Integer>() {
            @Override
            public Integer transform(Integer input) {
                return input;
            }
        };

        List<Integer> expectedResult = new ArrayList<>();
        Iterator<KeepCommand<Integer>> keepIterator = new TransformationIterator<Integer, KeepCommand<Integer>>(script, keepCommand);
        while (keepIterator.hasNext()) {
            KeepCommand<Integer> keepCommand = keepIterator.next();
            expectedResult.add(keepCommand.getObject());
        }

        List<Integer> result = new ArrayList<>();
        Iterator<Integer> resultIterator = script.iterator();
        while (resultIterator.hasNext()) {
            result.add((Integer) resultIterator.next());
        }

        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }

    @Test
    public void testEmptyListsComparison() {
        List<String> sequence1 = new ArrayList<>();
        List<String> sequence2 = new ArrayList<>();

        SequencesComparator<String> comparator = new SequencesComparator<String>(sequence1, sequence2);
        EditScript<String> script = comparator.getScript();

        assertNull(script.iterator().next());
    }

    @Test
    public void testSequencesWithEqualElementsComparison() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 1, 1, 1));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 1, 1, 1));

        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        assertNull(script.iterator().next());
    }

    @Test
    public void testSequencesWithDifferentLengthComparison() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));

        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        Transformation<Integer, Integer> deleteCommand = new Transformation<Integer, Integer>() {
            @Override
            public Integer transform(Integer input) {
                return null;
            }
        };

        List<Integer> expectedResult = new ArrayList<>();
        Iterator<DeleteCommand<Integer>> deleteIterator = new TransformationIterator<Integer, DeleteCommand<Integer>>(script, deleteCommand);
        while (deleteIterator.hasNext()) {
            DeleteCommand<Integer> deleteCommand = deleteIterator.next();
            expectedResult.add(deleteCommand.getObject());
        }

        List<Integer> result = new ArrayList<>();
        Iterator<Integer> resultIterator = script.iterator();
        while (resultIterator.hasNext()) {
            result.add((Integer) resultIterator.next());
        }

        // The first element of sequence1 should be deleted
        assertEquals(2, result.size());
        assertArrayEquals(new Integer[] {1, 4}, result.toArray());

        // The first element of sequence2 should be kept
        assertEquals(4, expectedResult.size());
        assertArrayEquals(new Integer[] {1, 1, 3, 4}, expectedResult.toArray());
    }

    @Test
    public void testSequencesWithDifferentElementsComparison() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 3));

        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        Transformation<Integer, Integer> insertCommand = new Transformation<Integer, Integer>() {
            @Override
            public Integer transform(Integer input) {
                return input;
            }
        };

        List<Integer> expectedResult = new ArrayList<>();
        Iterator<InsertCommand<Integer>> insertIterator = new TransformationIterator<Integer, InsertCommand<Integer>>(script, insertCommand);
        while (insertIterator.hasNext()) {
            InsertCommand<Integer> insertCommand = insertIterator.next();
            expectedResult.add(insertCommand.getObject());
        }

        List<Integer> result = new ArrayList<>();
        Iterator<Integer> resultIterator = script.iterator();
        while (resultIterator.hasNext()) {
            result.add((Integer) resultIterator.next());
        }

        // The first element of sequence1 should be deleted
        assertEquals(3, result.size());
        assertArrayEquals(new Integer[] {2, 5, 3}, result.toArray());

        // The first element of sequence2 should be kept
        assertEquals(3, expectedResult.size());
        assertArrayEquals(new Integer[] {1, 2, 3}, expectedResult.toArray());

        // The third element of sequence2 should be inserted
        assertEquals(3, result.size());
        assertEquals(5, result.get(1));
    }
}