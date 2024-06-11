package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testConstructorWithSequences() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        assertNotNull(comparator.getScript());
    }

    @Test
    public void testConstructorWithSequencesAndEquator() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
        Equator<Integer> equator = new Equator<Integer>() {
            @Override
            public boolean equate(Integer o1, Integer o2) {
                return o1.equals(o2);
            }
        };
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
        assertNotNull(comparator.getScript());
    }

    @Test
    public void testGetScript_EmptySequences() {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(0, script.size());
    }

    @Test
    public void testGetScript_SequencesWithSameElements() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(0, script.size());
    }

    @Test
    public void testGetScript_SequencesWithSameElementsInDifferentOrder() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> commands = script.getCommands();
        assertEquals(5, commands.size());
        for (int i = 0; i < 5; ++i) {
            if (i < 2) {
                assertTrue(commands.get(i) instanceof DeleteCommand);
            } else {
                assertTrue(commands.get(i) instanceof InsertCommand);
            }
        }
    }

    @Test(expected = RuntimeException.class)
    public void testGetScript_SequencesWithDifferentLengths() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        comparator.getScript();
    }

    @Test
    public void testBuildSnake_SimpleCase() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        Snake snake = comparator.getMiddleSnake(0, list1.size(), 0, list2.size());
        assertNotNull(snake);
        assertEquals(4, snake.getStart());
        assertEquals(0, snake.getEnd());
        assertEquals(-1, snake.getDiag());
    }

    @Test
    public void testBuildSnake_CaseWithDiagonalElements() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        Snake middle = comparator.getMiddleSnake(0, list1.size(), 0, list2.size());
        if (middle != null) {
            Snake snake = comparator.buildSnake(middle.getStart(), middle.getDiag(), list1.size(), list2.size());
            assertNotNull(snake);
            assertEquals(0, snake.getStart());
            assertEquals(4, snake.getEnd());
            assertEquals(1, snake.getDiag());
        }
    }

    @Test
    public void testBuildSnake_CaseWithNoSnake() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        Snake middle = comparator.getMiddleSnake(0, list1.size(), 0, list2.size());
        assertNull(middle);
    }

    @Test
    public void testBuildScript_SimpleCase() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> commands = script.getCommands();
        EditScript<Integer> expectedScript = new EditScript<>();
        expectedScript.append(new DeleteCommand<Integer>(1));
        expectedScript.append(new InsertCommand<Integer>(5));
        expectedScript.append(new DeleteCommand<Integer>(2));
        expectedScript.append(new InsertCommand<Integer>(4));
        expectedScript.append(new DeleteCommand<Integer>(3));
        expectedScript.append(new InsertCommand<Integer>(3));
        expectedScript.append(new DeleteCommand<Integer>(4));
        expectedScript.append(new InsertCommand<Integer>(2));
        expectedScript.append(new DeleteCommand<Integer>(5));
        expectedScript.append(new InsertCommand<Integer>(1));
        List<EditCommand<Integer>> expectedCommands = expectedScript.getCommands();
        for (int i = 0; i < commands.size(); ++i) {
            assertEquals(commands.get(i).getClass(), expectedCommands.get(i).getClass());
            if (i < commands.size() - 1 && commands.get(i + 1).getClass() != commands.get(i).getClass()) {
                continue;
            }
            if (commands.get(i) instanceof DeleteCommand) {
                DeleteCommand<Integer> deleteCommand = (DeleteCommand<Integer>) commands.get(i);
                DeleteCommand<Integer> expectedDeleteCommand = (DeleteCommand<Integer>) expectedCommands.get(i);
                assertEquals(deleteCommand.getElement().intValue(), expectedDeleteCommand.getElement().intValue());
            } else if (commands.get(i) instanceof InsertCommand) {
                InsertCommand<Integer> insertCommand = (InsertCommand<Integer>) commands.get(i);
                InsertCommand<Integer> expectedInsertCommand = (InsertCommand<Integer>) expectedCommands.get(i);
                assertEquals(insertCommand.getElement().intValue(), expectedInsertCommand.getElement().intValue());
            }
        }
    }

    @Test
    public void testBuildScript_CaseWithDiagonalElements() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> commands = script.getCommands();
        EditScript<Integer> expectedScript = new EditScript<>();
        expectedScript.append(new DeleteCommand<Integer>(1));
        expectedScript.append(new InsertCommand<Integer>(5));
        expectedScript.append(new DeleteCommand<Integer>(2));
        expectedScript.append(new InsertCommand<Integer>(4));
        expectedScript.append(new DeleteCommand<Integer>(3));
        expectedScript.append(new InsertCommand<Integer>(3));
        expectedScript.append(new DeleteCommand<Integer>(4));
        expectedScript.append(new InsertCommand<Integer>(2));
        expectedScript.append(new DeleteCommand<Integer>(5));
        expectedScript.append(new InsertCommand<Integer>(1));
        List<EditCommand<Integer>> expectedCommands = expectedScript.getCommands();
        for (int i = 0; i < commands.size(); ++i) {
            assertEquals(commands.get(i).getClass(), expectedCommands.get(i).getClass());
            if (i < commands.size() - 1 && commands.get(i + 1).getClass() != commands.get(i).getClass()) {
                continue;
            }
            if (commands.get(i) instanceof DeleteCommand) {
                DeleteCommand<Integer> deleteCommand = (DeleteCommand<Integer>) commands.get(i);
                DeleteCommand<Integer> expectedDeleteCommand = (DeleteCommand<Integer>) expectedCommands.get(i);
                assertEquals(deleteCommand.getElement().intValue(), expectedDeleteCommand.getElement().intValue());
            } else if (commands.get(i) instanceof InsertCommand) {
                InsertCommand<Integer> insertCommand = (InsertCommand<Integer>) commands.get(i);
                InsertCommand<Integer> expectedInsertCommand = (InsertCommand<Integer>) expectedCommands.get(i);
                assertEquals(insertCommand.getElement().intValue(), expectedInsertCommand.getElement().intValue());
            }
        }
    }

    @Test
    public void testBuildScript_CaseWithEmptySequences() {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(0, script.size());
    }

    @Test
    public void testBuildScript_CaseWithSequencesWithSameElements() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(0, script.size());
    }

    @Test
    public void testBuildScript_CaseWithSequencesWithSameElementsInDifferentOrder() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> commands = script.getCommands();
        EditScript<Integer> expectedScript = new EditScript<>();
        expectedScript.append(new DeleteCommand<Integer>(1));
        expectedScript.append(new InsertCommand<Integer>(5));
        expectedScript.append(new DeleteCommand<Integer>(2));
        expectedScript.append(new InsertCommand<Integer>(4));
        expectedScript.append(new DeleteCommand<Integer>(3));
        expectedScript.append(new InsertCommand<Integer>(3));
        expectedScript.append(new DeleteCommand<Integer>(4));
        expectedScript.append(new InsertCommand<Integer>(2));
        expectedScript.append(new DeleteCommand<Integer>(5));
        expectedScript.append(new InsertCommand<Integer>(1));
        List<EditCommand<Integer>> expectedCommands = expectedScript.getCommands();
        for (int i = 0; i < commands.size(); ++i) {
            assertEquals(commands.get(i).getClass(), expectedCommands.get(i).getClass());
            if (i < commands.size() - 1 && commands.get(i + 1).getClass() != commands.get(i).getClass()) {
                continue;
            }
            if (commands.get(i) instanceof DeleteCommand) {
                DeleteCommand<Integer> deleteCommand = (DeleteCommand<Integer>) commands.get(i);
                DeleteCommand<Integer> expectedDeleteCommand = (DeleteCommand<Integer>) expectedCommands.get(i);
                assertEquals(deleteCommand.getElement().intValue(), expectedDeleteCommand.getElement().intValue());
            } else if (commands.get(i) instanceof InsertCommand) {
                InsertCommand<Integer> insertCommand = (InsertCommand<Integer>) commands.get(i);
                InsertCommand<Integer> expectedInsertCommand = (InsertCommand<Integer>) expectedCommands.get(i);
                assertEquals(insertCommand.getElement().intValue(), expectedInsertCommand.getElement().intValue());
            }
        }
    }

    @Test
    public void testBuildScript_CaseWithSequencesWithDifferentLengths() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 2, 3));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        try {
            comparator.getScript();
            fail();
        } catch (RuntimeException e) {
            // Expected
        }
    }
}