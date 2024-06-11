import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.EditScript;
import org.apache.commons.collections4.functors.ComparatorEquator;
import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparatorWithDefaultEquator() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(2, 3, 4, 5, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        int[] expectedCommands = {DeleteCommand.ID, 0, 1, KeepCommand.ID, 2, KeepCommand.ID, 3, KeepCommand.ID, 4, KeepCommand.ID, 5, InsertCommand.ID, 1, 4, KeepCommand.ID, 5};
        int[] actualCommands = new int[script.size()];
        for (int i = 0; i < script.size(); i++) {
            actualCommands[i] = script.getCommand(i).getID();
        }

        assertArrayEquals(expectedCommands, actualCommands);
    }

    @Test
    public void testSequencesComparatorWithCustomEquator() {
        List<String> list1 = Arrays.asList("apple", "banana", "cherry", "date");
        List<String> list2 = Arrays.asList("cherry", "apple", "banana", "date", "apple");
        Equator<String> customEquator = new ComparatorEquator<>();
        SequencesComparator<String> comparator = new SequencesComparator<>(list1, list2, customEquator);
        EditScript<String> script = comparator.getScript();

        String[] expectedCommands = {"Delete", "Keep", "Keep", "Keep", "Keep", "Insert", "Keep"};
        String[] actualCommands = new String[script.size()];
        for (int i = 0; i < script.size(); i++) {
            actualCommands[i] = script.getCommand(i).getClass().getSimpleName();
        }

        assertArrayEquals(expectedCommands, actualCommands);
    }

    @Test
    public void testSequencesComparatorWithEmptyLists() {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        assertTrue(script.isEmpty());
    }

    @Test
    public void testSequencesComparatorWithOneEmptyList() {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = Arrays.asList(1, 2, 3, 4, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        int[] expectedCommands = {DeleteCommand.ID, 0, 1, DeleteCommand.ID, 2, DeleteCommand.ID, DeleteCommand.ID, DeleteCommand.ID, DeleteCommand.ID, DeleteCommand.ID};
        int[] actualCommands = new int[script.size()];
        for (int i = 0; i < script.size(); i++) {
            actualCommands[i] = script.getCommand(i).getID();
        }

        assertArrayEquals(expectedCommands, actualCommands);
    }

    @Test
    public void testSequencesComparatorWithIdenticalLists() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(5, 4, 3, 2, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        int[] expectedCommands = {KeepCommand.ID, 1, KeepCommand.ID, 2, KeepCommand.ID, 3, KeepCommand.ID, 4, KeepCommand.ID, 5};
        int[] actualCommands = new int[script.size()];
        for (int i = 0; i < script.size(); i++) {
            actualCommands[i] = script.getCommand(i).getID();
        }

        assertArrayEquals(expectedCommands, actualCommands);
    }

    @Test(expected = RuntimeException.class)
    public void testSequencesComparatorWithDifferentListSizes() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(1, 2, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        // Assert that an exception is thrown
    }
}