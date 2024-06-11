package org.junit.jupiter.api;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.TestCollections;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.functors.Equator;
import org.apache.commons.collections4.sequences.EditScript;
import org.apache.commons.collections4.sequences.Snake;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SequencesComparatorTest {
    @Test
    void testSequencesComparatorEmptyLists() {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(0, script.size());
    }

    @Test
    void testSequencesComparatorEqualLists() {
        List<Integer> list1 = ListUtils.nCopies(3, 1);
        List<Integer> list2 = list1;
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        assertTrue(script.isEmpty());
    }

    @Test
    void testSequencesComparatorDifferentLists() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(2, 3, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> commands = script.getCommands();
        assertEquals(3, commands.size());
        assertEquals(Type.DELETE, commands.get(0).getType());
        assertEquals(1, commands.get(0).getIndex());
        assertEquals(Type.INSERT, commands.get(1).getType());
        assertEquals(1, commands.get(1).getIndex());
        assertEquals(Type.INSERT, commands.get(2).getType());
        assertEquals(3, commands.get(2).getIndex());
    }

    @Test
    void testSequencesComparatorNullLists() {
        List<Integer> list1 = null;
        List<Integer> list2 = null;
        try {
            SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    void testSequencesComparatorNullElementInList1() {
        List<Integer> list1 = Arrays.asList(1, null, 3);
        List<Integer> list2 = Arrays.asList(1, 2, 3);
        try {
            SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    void testSequencesComparatorNullElementInList2() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(1, null, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> commands = script.getCommands();
        assertEquals(2, commands.size());
        assertEquals(Type.DELETE, commands.get(0).getType());
        assertEquals(1, commands.get(0).getIndex());
        assertEquals(Type.INSERT, commands.get(1).getType());
        assertEquals(2, commands.get(1).getIndex());
    }

    @Test
    void testSequencesComparatorMismatchedSizes() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> commands = script.getCommands();
        assertEquals(2, commands.size());
        assertEquals(Type.DELETE, commands.get(0).getType());
        assertEquals(3, commands.get(0).getIndex());
        assertEquals(Type.INSERT, commands.get(1).getType());
        assertEquals(2, commands.get(1).getIndex());
    }

    @Test
    void testSequencesComparatorEqualElementsDifferentOrder() {
        List<String> list1 = TestCollections.list("apple", "banana", "cherry");
        List<String> list2 = TestCollections.list("banana", "cherry", "apple");
        SequencesComparator<String> comparator = new SequencesComparator<>(list1, list2);
        EditScript<String> script = comparator.getScript();
        List<EditCommand<String>> commands = script.getCommands();
        assertEquals(3, commands.size());
        assertEquals(Type.DELETE, commands.get(0).getType());
        assertEquals(0, commands.get(0).getIndex());
        assertEquals(Type.INSERT, commands.get(1).getType());
        assertEquals(1, commands.get(1).getIndex());
        assertEquals(Type.INSERT, commands.get(2).getType());
        assertEquals(3, commands.get(2).getIndex());
    }

    @Test
    void testSequencesComparatorCustomEquator() {
        List<String> list1 = TestCollections.list("apple", "banana", "cherry");
        List<String> list2 = TestCollections.list("banana", "cherry", "apple");
        SequencesComparator<String> comparator = new SequencesComparator<>(list1, list2, new ComparatorStringWithLengthFirst());
        EditScript<String> script = comparator.getScript();
        List<EditCommand<String>> commands = script.getCommands();
        assertEquals(3, commands.size());
        assertEquals(Type.DELETE, commands.get(0).getType());
        assertEquals(0, commands.get(0).getIndex());
        assertEquals(Type.INSERT, commands.get(1).getType());
        assertEquals(1, commands.get(1).getIndex());
        assertEquals(Type.INSERT, commands.get(2).getType());
        assertEquals(3, commands.get(2).getIndex());
    }
}

class ComparatorStringWithLengthFirst implements Equator<String> {
    @Override
    public boolean equate(String a, String b) {
        int aLength = a.length();
        int bLength = b.length();
        if (aLength != bLength) {
            return aLength < bLength;
        }
        return a.compareTo(b) < 0;
    }
}