package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;

public class SequencesComparatorTest<T> {

    @Test
    public void testSequencesComparatorConstructorWithTwoLists() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(3, 2, 1));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        assertNotNull(comparator.getScript());
    }

    @Test
    public void testSequencesComparatorConstructorWithTwoListsAndEquator() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(3, 2, 1));
        Equator<Integer> equator = new Equator<Integer>() {
            @Override
            public boolean equate(Object o1, Object o2) {
                if (o1 == null && o2 == null) {
                    return true;
                }
                if (o1 == null || o2 == null) {
                    return false;
                }
                return ((Integer) o1).equals(o2);
            }
        };
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
        assertNotNull(comparator.getScript());
    }

    @Test
    public void testGetScript_SequencesAreIdentical() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 2, 3));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        assertTrue(script.isEmpty());
    }

    @Test
    public void testGetScript_SequencesAreDifferent() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(3, 2, 1));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> commands = new ArrayList<>(script.getCommands());
        assertEquals(3, commands.size());
        assertEquals(commands.get(0), new DeleteCommand<>(1));
        assertEquals(commands.get(1), new InsertCommand<>(3));
        assertEquals(commands.get(2), new InsertCommand<>(2));
    }

    @Test(expected = RuntimeException.class)
    public void testGetScript_InternalError() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(3, 2, 1));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        comparator.buildScript(-1, list1.size(), 0, list2.size(), comparator.getScript());
    }

    @Test
    public void testBuildSnake_SimpleCase() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(3, 2, 1));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        Snake snake = comparator.getMiddleSnake(0, list1.size(), 0, list2.size());
        assertEquals(snake.getStart(), 1);
        assertEquals(snake.getEnd(), 2);
        assertEquals(snake.getDiag(), -1);
    }

    @Test
    public void testBuildSnake_SimpleCase_DiagonalDifferenceIsZero() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        Snake snake = comparator.getMiddleSnake(0, list1.size(), 0, list2.size());
        assertNull(snake);
    }

    @Test
    public void testBuildSnake_EdgeCase_NoCommonElements() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(4, 5, 6));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        Snake snake = comparator.getMiddleSnake(0, list1.size(), 0, list2.size());
        assertNull(snake);
    }

    @Test(expected = RuntimeException.class)
    public void testBuildSnake_InternalError() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(3, 2, 1));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        comparator.buildSnake(-1, 0, 0, 0);
    }

}