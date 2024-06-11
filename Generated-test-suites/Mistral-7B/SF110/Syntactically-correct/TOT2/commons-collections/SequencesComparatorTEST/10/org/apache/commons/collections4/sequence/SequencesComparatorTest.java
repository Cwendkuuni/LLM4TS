package org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.functors.Equator;
import org.apache.commons.collections4.sequence.EditScript;
import org.apache.commons.collections4.sequence.EditCommand;
import org.apache.commons.collections4.sequence.SequencesComparator;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.*;

@RunWith(JUnit4.class)
public class SequencesComparatorTest {

    private List<Integer> list1;
    private List<Integer> list2;
    private SequencesComparator<Integer> comparator;
    
    @Before
    public void setUp() {
        list1 = Arrays.asList(1, 2, 3, 4);
        list2 = Arrays.asList(2, 3, 4, 1);
        comparator = new SequencesComparator<>(list1, list2);
    }

    @Test
    public void testGetScript() {
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> expectedCommands = Arrays.asList(
                new InsertCommand<>(2),
                new KeepCommand<>(3),
                new KeepCommand<>(4),
                new DeleteCommand<>(1)
        );
        assertEquals(expectedCommands, script.getCommands());
    }

    @Test
    public void testBuildSnakeWithEmptyLists() {
        List<Integer> list1 = Collections.emptyList();
        List<Integer> list2 = Collections.emptyList();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        Snake expectedSnake = null;
        Snake snake = comparator.buildSnake(0, 0, 0, 0);
        assertNull(snake);
        assertEquals(expectedSnake, snake);
    }

    @Test
    public void testGetMiddleSnakeWithDifferentListSizesAndNoCorrespondingElement() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        Snake expectedSnake = null;
        Snake snake = comparator.getMiddleSnake(0, list1.size(), 0, list2.size());
        assertNull(snake);
        assertEquals(expectedSnake, snake);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullList() {
        List<Integer> list1 = null;
        List<Integer> list2 = Arrays.asList(1, 2, 3);
        try {
            new SequencesComparator<>(list1, list2);
        } catch (NullPointerException e) {
            //Expected exception
        }
    }
}