package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;

public class SequencesComparatorTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testSequencesComparator() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> editScript = comparator.getScript();

        assertEquals(2, editScript.size());
        assertEquals(InsertCommand.class, editScript.getCommandClass(0));
        assertEquals(DeleteCommand.class, editScript.getCommandClass(1));
        assertEquals(1, ((InsertCommand<Integer>) editScript.getCommand(0)).getIndex());
        assertEquals(2, ((InsertCommand<Integer>) editScript.getCommand(0)).getElement());
        assertEquals(2, ((DeleteCommand<Integer>) editScript.getCommand(1)).getIndex());

        List<Integer> resultList = new ArrayList<>();
        editScript.applyTo(list1);
        resultList.addAll(list1);
        assertArrayEquals(new Integer[]{1, 2, 3}, resultList.toArray());

        List<Integer> expectedList = new ArrayList<>();
        expectedList.add(1);
        expectedList.add(2);
        expectedList.add(3);
        assertEquals(expectedList, list2);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSequencesComparatorWithDifferentLengths() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(3);
        list2.add(4);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> editScript = comparator.getScript();

        assertEquals(3, editScript.size());
        assertEquals(InsertCommand.class, editScript.getCommandClass(0));
        assertEquals(InsertCommand.class, editScript.getCommandClass(1));
        assertEquals(1, ((InsertCommand<Integer>) editScript.getCommand(0)).getIndex());
        assertEquals(2, ((InsertCommand<Integer>) editScript.getCommand(0)).getElement());
        assertEquals(3, ((InsertCommand<Integer>) editScript.getCommand(1)).getIndex());
        assertEquals(3, ((InsertCommand<Integer>) editScript.getCommand(1)).getElement());
        assertEquals(DeleteCommand.class, editScript.getCommandClass(2));
        assertEquals(3, ((DeleteCommand<Integer>) editScript.getCommand(2)).getIndex());

        List<Integer> resultList = new ArrayList<>();
        editScript.applyTo(list1);
        resultList.addAll(list1);
        assertArrayEquals(new Integer[]{1, 2, 3, 4}, resultList.toArray());

        List<Integer> expectedList = new ArrayList<>();
        expectedList.add(1);
        expectedList.add(2);
        expectedList.add(3);
        expectedList.add(4);
        assertEquals(expectedList, list2);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSequencesComparatorWithNullLists() {
        List<Integer> list1 = null;
        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);

        try {
            new SequencesComparator<>(list1, list2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list2 = null;

        try {
            new SequencesComparator<>(list1, list2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSequencesComparatorWithNullElement() {
        List<Object> list1 = Arrays.asList(1, null);
        List<Object> list2 = Arrays.asList(1, 2, 3);

        try {
            new SequencesComparator<>(list1, list2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSequencesComparatorWithNullComparator() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);

        try {
            new SequencesComparator<>(list1, list2, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetMiddleSnake() {
        int[] expectedDown = {0, 1, 1, 2, 2, 2};
        int[] expectedUp = {0, 1, 2, 2, 2, 3};

        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(2);
        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        Snake snake = comparator.getMiddleSnake(0, list1.size(), 0, list2.size());

        assertNull(snake);

        list1 = new ArrayList<>();
        list1.add(1);
        list1.add(1);
        list1.add(1);
        list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(2);
        list2.add(3);

        snake = comparator.getMiddleSnake(0, list1.size(), 0, list2.size());
        assertArrayEquals(expectedDown, snake.vDown);
        assertArrayEquals(expectedUp, snake.vUp);
    }

    @Test
    public void testBuildScript() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(2);
        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> editScript = comparator.getScript();

        List<Integer> resultList = new ArrayList<>();
        editScript.applyTo(list1);
        resultList.addAll(list1);
        assertArrayEquals(new Integer[]{1, 2, 3}, resultList.toArray());

        List<Integer> expectedList = new ArrayList<>();
        expectedList.add(1);
        expectedList.add(2);
        expectedList.add(3);
        assertEquals(expectedList, list2);
    }
}