package org.junit.test;

import org.junit.Test;
import org.junit.Assert;
import java.util.ArrayList;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparator() {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        Assert.assertNotNull(comparator);
    }

    @Test
    public void testSequencesComparatorWithEquator() {
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        Equator<String> equator = new AlwaysTrueEquator<>();
        SequencesComparator<String> comparator = new SequencesComparator<>(list1, list2, equator);
        Assert.assertNotNull(comparator);
    }

    // Test edge cases and exception handling scenarios will follow here...

    // Test edge cases for getScript() method
    @Test
    public void testGetScriptWithListsWithIdenticalElements() {
        List<Integer> list1 = Arrays.asList(1, 1, 1);
        List<Integer> list2 = Arrays.asList(1, 1, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        // Check the script contains only KeepCommands
        Assert.assertTrue(script.isEmpty() || script.getClass() == EditScript.KeepCommand.class);
    }

    @Test
    public void testGetScriptWithEmptyLists() {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        // Check the script is empty
        Assert.assertTrue(script.isEmpty());
    }

    @Test(expected = RuntimeException.class)
    public void testGetScriptWithDifferentTypes() {
        List<String> list1 = new ArrayList<>();
        list1.add("A");
        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        SequencesComparator<Object> comparator = new SequencesComparator<>(list1, list2);
        comparator.getScript();
    }

    // Test edge cases for buildScript() method
    @Test
    public void testBuildScriptWithIdenticalElements() {
        List<Integer> list1 = Arrays.asList(1, 1, 1);
        List<Integer> list2 = Arrays.asList(1, 1, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        // Check the script contains only KeepCommands
        Assert.assertTrue(script.isEmpty() || script.getClass() == EditScript.KeepCommand.class);
    }

    @Test
    public void testBuildScriptWithDifferentSizes() {
        List<Integer> list1 = Arrays.asList(1, 1, 1);
        List<Integer> list2 = Arrays.asList(1, 1, 1, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        // Check the script contains a DeleteCommand and KeepCommands
        Assert.assertEquals(1, script.size());
        Assert.assertTrue(script.getCommands().get(0) instanceof EditScript.DeleteCommand);
    }

    // Test edge cases for getMiddleSnake() method
    @Test
    public void testGetMiddleSnakeWithEmptyLists() {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        Snake snake = comparator.getMiddleSnake(1, 1, 1, 1);
        Assert.assertNull(snake);
    }

    @Test
    public void testGetMiddleSnakeWithIdenticalElements() {
        List<Integer> list1 = Arrays.asList(1, 1, 1);
        List<Integer> list2 = Arrays.asList(1, 1, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        Snake snake = comparator.getMiddleSnake(1, 1, 1, 1);
        Assert.assertNull(snake);
    }

    @Test
    public void testGetMiddleSnakeWithDifferentSizes() {
        List<Integer> list1 = Arrays.asList(1, 1, 1);
        List<Integer> list2 = Arrays.asList(1, 1, 1, 1, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        Snake snake = comparator.getMiddleSnake(1, 1, 1, 1);
        Assert.assertNull(snake);
    }

    @Test
    public void testGetMiddleSnakeWithIdenticalDiagonals() {
        List<Integer> list1 = Arrays.asList(1, 1, 1, 1);
        List<Integer> list2 = Arrays.asList(1, 1, 1, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        Snake snake = comparator.getMiddleSnake(2, 3, 2, 3);
        Assert.assertNull(snake);
    }

    @Test
    public void testGetMiddleSnakeWithDiagonalCycle() {
        List<Integer> list1 = Arrays.asList(1, 1, 1, 1, 1, 1);
        List<Integer> list2 = Arrays.asList(1, 1, 1, 1, 1, 1, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        Snake snake = comparator.getMiddleSnake(1, 6, 1, 6);
        Assert.assertNotNull(snake);
        Assert.assertEquals(1, snake.getStart());
        Assert.assertEquals(1, snake.getEnd());
        Assert.assertEquals(5, snake.getDiag());
    }

    // Test edge cases for buildSnake() method
    @Test
    public void testBuildSnakeWithDiagonalCycle() {
        List<Integer> list1 = Arrays.asList(1, 1, 1, 1, 1, 1);
        List<Integer> list2 = Arrays.asList(1, 1, 1, 1, 1, 1, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        Snake snake = comparator.buildSnake(1, 5, 6, 6);
        Assert.assertNotNull(snake);
        Assert.assertEquals(1, snake.getStart());
        Assert.assertEquals(5, snake.getEnd());
        Assert.assertEquals(5, snake.getDiag());
    }

    @Test(expected = RuntimeException.class)
    public void testBuildSnakeWithInvalidArguments() {
        List<Integer> list1 = Arrays.asList(1, 1, 1);
        List<Integer> list2 = Arrays.asList(1, 1, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        comparator.buildSnake(-1, 0, 1, 1);
    }

    // Test edge cases for vDown and vUp arrays
    @Test
    public void testBuildScriptWithEmptyLists() {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        int[] vDown = script.getVDown();
        int[] vUp = script.getVUp();
        Assert.assertArrayEquals(new int[0], vDown);
        Assert.assertArrayEquals(new int[0], vUp);
    }

    @Test
    public void testBuildScriptWithDifferentSizes() {
        List<Integer> list1 = Arrays.asList(1, 1, 1);
        List<Integer> list2 = Arrays.asList(1, 1, 1, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        int[] vDown = script.getVDown();
        int[] vUp = script.getVUp();
        Assert.assertEquals(13, vDown.length);
        Assert.assertEquals(13, vUp.length);
        Assert.assertArrayEquals(new int[]{0, 1, 1, 2, 2, 3, 3, 3, 3, 4, 5, 5}, vDown);
        Assert.assertArrayEquals(new int[]{1, 2, 2, 3, 4, 5, 5, 5, 6, 7, 7, 8}, vUp);
    }

    @Test
    public void testBuildScriptWithIdenticalElements() {
        List<Integer> list1 = Arrays.asList(1, 1, 1);
        List<Integer> list2 = Arrays.asList(1, 1, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        int[] vDown = script.getVDown();
        int[] vUp = script.getVUp();
        Assert.assertEquals(12, vDown.length);
        Assert.assertEquals(12, vUp.length);
        Assert.assertArrayEquals(new int[]{0, 0, 1, 1, 2, 2, 3, 3, 3, 3, 3, 3}, vDown);
        Assert.assertArrayEquals(new int[]{1, 1, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3}, vUp);
    }

    @Test(expected = RuntimeException.class)
    public void testBuildScriptWithDifferentTypes() {
        List<String> list1 = new ArrayList<>();
        list1.add("A");
        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        SequencesComparator<Object> comparator = new SequencesComparator<>(list1, list2);
        comparator.getScript();
    }
}