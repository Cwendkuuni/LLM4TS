package org.apache.commons.collections4.sequence;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.assertEquals;
import org.apache.commons.collections4.Functor;
import org.apache.commons.collections4.EditableIterable;
import org.apache.commons.collections4.EditScript;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.functors.Chains;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.iterators.AbstractIterator;
import org.junit.Test;

public class SequencesComparatorTest {

    @SuppressWarnings("unchecked")
    private static final Factory<Equator<?>> DEFAULT_EQUATOR_FACTORY = new Factory<Equator<?>>() {
        @Override
        public Equator<?> makeNew() {
            return DefaultEquator.defaultEquator();
        }
    };

    @Test
    public void testSequencesComparator_emptySequences() {
        List<Integer> sequence1 = Collections.emptyList();
        List<Integer> sequence2 = Collections.emptyList();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(Collections.emptyList(), script.toList());
    }

    @Test
    public void testSequencesComparator_identicalSequences() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(sequence1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(Collections.emptyList(), script.toList());
    }

    @Test
    public void testSequencesComparator_sequencesWithCommonElements() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 3, 4, 5, 6));
        Equator<Integer> equator = Chains.chain(new ConstantTransformer<Integer, Integer>(1), new Equator<Integer>() {
            @Override
            public boolean equate(Integer lhs, Integer rhs) {
                return lhs.intValue() == rhs.intValue();
            }
        });
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> expected = new ArrayList<>(Arrays.asList(
                new DeleteCommand<>(5),
                new InsertCommand<>(6)
        ));
        assertEquals(expected, script.toList());
    }

    @Test
    public void testSequencesComparator_sequencesWithDifferentLengths() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3));
        Equator<Integer> equator = DefaultEquator.defaultEquator();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> expected = new ArrayList<>(Arrays.asList(
                new DeleteCommand<>(4),
                new DeleteCommand<>(5)
        ));
        assertEquals(expected, script.toList());
    }

    @Test
    public void testSequencesComparator_sequencesWithNoCommonElements() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(6, 7, 8, 9, 10));
        Equator<Integer> equator = DefaultEquator.defaultEquator();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> expected = new ArrayList<>(Arrays.asList(
                new DeleteCommand<>(sequence1.subList(0, sequence1.size())),
                new InsertAllCommand<>(sequence2.subList(0, sequence2.size()))
        ));
        assertEquals(expected, script.toList());
    }

    @Test
    public void testSequencesComparator_sequencesWithEmptySequence1() {
        List<Integer> sequence1 = Collections.emptyList();
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> expected = new ArrayList<>(Arrays.asList(
                new InsertAllCommand<>(Collections.singletonList(1)),
                new InsertAllCommand<>(Arrays.asList(2, 3))
        ));
        assertEquals(expected, script.toList());
    }

    @Test(expected = RuntimeException.class)
    public void testSequencesComparator_internalError() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), null);
    }

    @Test
    public void testSequencesComparator_testGetMiddleSnake() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(5, 4, 3, 2, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        Snake middleSnake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
        assertSame(new Snake(3, 4, 1), middleSnake);
    }

    @Test
    public void testSequencesComparator_testGetMiddleSnake_emptySequences() {
        List<Integer> sequence1 = Collections.emptyList();
        List<Integer> sequence2 = Collections.emptyList();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        Snake middleSnake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
        assertNull(middleSnake);
    }

    @Test
    public void testSequencesComparator_testBuildScript_emptyScript() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertArrayEquals(new EditCommand[0], script.toCommands().toArray());
    }

    @Test
    public void testSequencesComparator_testBuildScript_simpleCase() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(3, 2, 1));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> expected = new ArrayList<>(Arrays.asList(
                new DeleteCommand<>(1),
                new InsertCommand<>(3),
                new DeleteCommand<>(2),
                new InsertCommand<>(1)
        ));
        assertEquals(expected, script.toList());
    }

    @Test
    public void testSequencesComparator_testBuildScript_complexCase() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> expected = new ArrayList<>(Arrays.asList(
                new DeleteCommand<>(1),
                new DeleteCommand<>(2),
                new DeleteCommand<>(3),
                new DeleteCommand<>(4),
                new DeleteCommand<>(5),
                new InsertAllCommand<>(Collections.singletonList(5)),
                new InsertAllCommand<>(Arrays.asList(4, 3, 2, 1))
        ));
        assertEquals(expected, script.toList());
    }

    @Test
    public void testSequencesComparator_testBuildScript_inverseOrder() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(3, 2, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> expected = new ArrayList<>(Arrays.asList(
                new InsertCommand<>(1),
                new InsertCommand<>(2),
                new InsertCommand<>(3)
        ));
        assertEquals(expected, script.toList());
    }

    @Test
    public void testSequencesComparator_testBuildScript_sameSequence() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = new ArrayList<>(sequence1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(Collections.emptyList(), script.toList());
    }

    @Test
    public void testSequencesComparator_testBuildScript_edgeCases() {
        List<Integer> sequence1 = Collections.emptyList();
        List<Integer> sequence2 = Arrays.asList(1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> expected = new ArrayList<>(Arrays.asList(
                new InsertCommand<>(1)
        ));
        assertEquals(expected, script.toList());

        sequence1 = Arrays.asList(1);
        sequence2 = Collections.emptyList();
        comparator = new SequencesComparator<>(sequence1, sequence2);
        script = comparator.getScript();
        expected = new ArrayList<>(Arrays.asList(
                new DeleteCommand<>(1)
        ));
        assertEquals(expected, script.toList());
    }
}