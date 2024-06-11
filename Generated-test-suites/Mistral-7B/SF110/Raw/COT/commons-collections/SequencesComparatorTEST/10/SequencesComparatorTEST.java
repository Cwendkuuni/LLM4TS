package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;

public class SequencesComparatorTest<T> {

    @Test
    public void testSequencesComparatorConstructorWithSequences() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> list2 = Arrays.asList(1, 3, 4, 2);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        assertNotNull(comparator);
    }

    @Test
    public void testSequencesComparatorConstructorWithSequencesAndEquator() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> list2 = Arrays.asList(1, 3, 4, 2);
        Equator<Integer> equator = new Equator<Integer>() {
            @Override
            public boolean equate(Object o1, Object o2) {
                return ((Comparable)o1).compareTo((Comparable)o2) == 0;
            }
        };
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
        assertNotNull(comparator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSequencesComparatorConstructorWithNullSequence1() {
        List<Integer> list1 = null;
        List<Integer> list2 = Arrays.asList(1, 2, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSequencesComparatorConstructorWithNullSequence2() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> list2 = null;
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
    }

    @Test
    public void testGetScript() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> list2 = Arrays.asList(1, 3, 4, 2);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        assertNotNull(script);
    }

    @Test
    public void testBuildScriptWithSameSequences() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> list2 = Arrays.asList(1, 2, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(4, script.size());
        for (int i = 0; i < script.size(); ++i) {
            Command command = script.getCommand(i);
            if (command instanceof KeepCommand) {
                KeepCommand keepCommand = (KeepCommand)command;
                assertEquals(1 + i, keepCommand.getElement().intValue());
            }
        }
    }

    @Test
    public void testBuildScriptWithDifferentSequences() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> list2 = Arrays.asList(1, 3, 4, 2);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(6, script.size());
        for (int i = 0; i < script.size(); ++i) {
            Command command = script.getCommand(i);
            if (command instanceof KeepCommand) {
                KeepCommand keepCommand = (KeepCommand)command;
                int index = i < script.size() / 2 ? i : i - 1;
                assertEquals(1 + index, keepCommand.getElement().intValue());
            }
            else if (command instanceof DeleteCommand) {
                DeleteCommand deleteCommand = (DeleteCommand)command;
                assertEquals(1 + (i < script.size() / 2 ? i : i - 1), deleteCommand.getElement().intValue());
            }
            else if (command instanceof InsertCommand) {
                InsertCommand insertCommand = (InsertCommand)command;
                int index = i < script.size() / 2 ? i : i - 1;
                assertEquals(1 + index, insertCommand.getElement().intValue());
            }
        }
    }

    @Test
    public void testBuildScriptWithDifferentSequencesWithSameElement() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 2, 4);
        List<Integer> list2 = Arrays.asList(1, 2, 3, 2, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(5, script.size());
        for (int i = 0; i < script.size(); ++i) {
            Command command = script.getCommand(i);
            if (i < script.size() - 1 && command instanceof KeepCommand) {
                KeepCommand keepCommand = (KeepCommand)command;
                assertEquals(2, keepCommand.getElement().intValue());
            }
            else if (command instanceof DeleteCommand) {
                DeleteCommand deleteCommand = (DeleteCommand)command;
                if (i == script.size() - 1) {
                    assertEquals(1 + (script.size() - 2), deleteCommand.getElement().intValue());
                }
                else {
                    assertEquals(1 + i, deleteCommand.getElement().intValue());
                }
            }
            else if (command instanceof InsertCommand) {
                InsertCommand insertCommand = (InsertCommand)command;
                if (i == script.size() - 1) {
                    assertEquals(1 + i, insertCommand.getElement().intValue());
                }
                else {
                    assertEquals(1 + i, insertCommand.getElement().intValue());
                }
            }
        }
    }

    @Test(expected = RuntimeException.class)
    public void testGetMiddleSnakeWithEmptySequences() {
        List<Integer> list1 = Collections.emptyList();
        List<Integer> list2 = Collections.emptyList();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        comparator.getMiddleSnake(0, 0, 0, 0);
    }

    @Test(expected = RuntimeException.class)
    public void testGetMiddleSnakeWithOneElementSequences() {
        List<Integer> list1 = Arrays.asList(1);
        List<Integer> list2 = Arrays.asList(1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        comparator.getMiddleSnake(0, 0, 0, 0);
    }

    @Test
    public void testGetMiddleSnakeWithDiagonalSnake() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> list2 = Arrays.asList(1, 4, 3, 2);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        Snake middleSnake = comparator.getMiddleSnake(0, list1.size(), 0, list2.size());
        assertNotNull(middleSnake);
        assertEquals(2, middleSnake.getDiag());
        assertEquals(1, middleSnake.getStart());
        assertEquals(4, middleSnake.getEnd());
    }

    @Test
    public void testGetMiddleSnakeWithVerticalSnake() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> list2 = Arrays.asList(1, 2, 4, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        Snake middleSnake = comparator.getMiddleSnake(0, list1.size(), 0, list2.size());
        assertNotNull(middleSnake);
        assertEquals(0, middleSnake.getDiag());
        assertEquals(1, middleSnake.getStart());
        assertEquals(3, middleSnake.getEnd());
    }

    @Test
    public void testGetMiddleSnakeWithReverseVerticalSnake() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> list2 = Arrays.asList(1, 4, 3, 2);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        Snake middleSnake = comparator.getMiddleSnake(0, list1.size(), 0, list2.size());
        assertNotNull(middleSnake);
        assertEquals(0, middleSnake.getDiag());
        assertEquals(4, middleSnake.getStart());
        assertEquals(1, middleSnake.getEnd());
    }
}