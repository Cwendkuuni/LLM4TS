package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.functors.ChainedPredicate;
import org.apache.commons.collections4.functors.PredicateCompositor;
import org.apache.commons.collections4.functors.PredicateFactory;
import org.apache.commons.collections4.iterator.AbstractIterator;
import org.apache.commons.collections4.iterators.TransformIterator;
import org.apache.commons.collections4.sequence.EditScript;
import org.apache.commons.collections4.sequence.Edits;
import org.apache.commons.collections4.sequence.SequenceComparator;

public class SequencesComparatorTest {
    
    @Test
    public void testSequencesComparator() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(1, 4, 3, 2, 5);

        SequenceComparator<Integer> comparator = new SequenceComparator<>(list1, list2, new Equator<Integer>() {
            @Override
            public boolean equate(Object o1, Object o2) {
                return ((Comparable) o1).compareTo((Comparable) o2) == 0;
            }
        });

        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> edits = Edits.toList(script);

        List<Integer> resultList = new ArrayList<>();
        for (EditCommand<Integer> editCommand : edits) {
            if (editCommand instanceof InsertCommand) {
                resultList.add(null);
            } else {
                resultList.add(editCommand.getObject());
            }
        }

        List<Integer> expectedList = Arrays.asList(1, 2, 3, 4, 5);
        ListUtils.reverse(expectedList);
        ListUtils.addAll(expectedList, Collections.singletonList(null));

        assertEquals(expectedList, resultList);
    }

    @Test
    public void testSequencesComparatorWithNullLists() {
        SequenceComparator<String> comparator = new SequenceComparator<>(null, null);
        assertNull(comparator.getScript());
    }

    @Test
    public void testSequencesComparatorWithEmptyLists() {
        List<String> list1 = Collections.emptyList();
        List<String> list2 = Collections.emptyList();

        SequenceComparator<String> comparator = new SequenceComparator<>(list1, list2);
        EditScript<String> script = comparator.getScript();

        assertTrue(script.isEmpty());
    }

    @Test
    public void testSequencesComparatorWithIdenticalLists() {
        List<String> list1 = Arrays.asList("a", "b", "c");
        List<String> list2 = Arrays.asList("a", "b", "c");

        SequenceComparator<String> comparator = new SequenceComparator<>(list1, list2);
        EditScript<String> script = comparator.getScript();

        assertTrue(script.isEmpty());
    }

    @Test
    public void testSequencesComparatorWithMismatchedListLengths() {
        List<String> list1 = Arrays.asList("a", "b", "c");
        List<String> list2 = Arrays.asList("a", "b");

        SequenceComparator<String> comparator = new SequenceComparator<>(list1, list2);
        EditScript<String> script = comparator.getScript();

        List<EditCommand<String>> edits = Edits.toList(script);
        List<String> resultList = new ArrayList<>();

        for (EditCommand<String> editCommand : edits) {
            if (editCommand instanceof InsertCommand) {
                resultList.add("c");
            } else if (editCommand instanceof DeleteCommand) {
                resultList.add(null);
            } else {
                resultList.add(editCommand.getObject());
            }
        }

        List<String> expectedList = Arrays.asList("a", "b", "c");

        assertEquals(expectedList, resultList);
    }

    @Test
    public void testSequencesComparatorWithMismatchedListOrder() {
        List<String> list1 = Arrays.asList("a", "b", "c");
        List<String> list2 = Arrays.asList("c", "b", "a");

        SequenceComparator<String> comparator = new SequenceComparator<>(list1, list2);
        EditScript<String> script = comparator.getScript();

        List<EditCommand<String>> edits = Edits.toList(script);
        List<String> resultList = new ArrayList<>();

        for (EditCommand<String> editCommand : edits) {
            if (editCommand instanceof InsertCommand) {
                resultList.add(null);
            } else {
                resultList.add(editCommand.getObject());
            }
        }

        List<String> expectedList = Arrays.asList("a", null, null, "b", "c");

        assertEquals(expectedList, resultList);
    }

    @Test
    public void testSequencesComparatorWithOneDeleteAndOneInsert() {
        List<String> list1 = Arrays.asList("a", "b", "c");
        List<String> list2 = Arrays.asList("b", "c", "a");

        SequenceComparator<String> comparator = new SequenceComparator<>(list1, list2);
        EditScript<String> script = comparator.getScript();

        List<EditCommand<String>> edits = Edits.toList(script);
        List<String> resultList = new ArrayList<>();

        for (EditCommand<String> editCommand : edits) {
            if (editCommand instanceof InsertCommand) {
                resultList.add("a");
            } else if (editCommand instanceof DeleteCommand) {
                resultList.add(null);
            } else {
                resultList.add(editCommand.getObject());
            }
        }

        List<String> expectedList = Arrays.asList("b", "c", "a");

        assertEquals(expectedList, resultList);
    }

    @Test
    public void testSequencesComparatorWithMultipleChanges() {
        List<String> list1 = Arrays.asList("a", "b", "c", "d");
        List<String> list2 = Arrays.asList("e", "f", "c", "b", "g", "h");

        SequenceComparator<String> comparator = new SequenceComparator<>(list1, list2);
        EditScript<String> script = comparator.getScript();

        List<EditCommand<String>> edits = Edits.toList(script);
        List<String> resultList = new ArrayList<>();

        for (EditCommand<String> editCommand : edits) {
            if (editCommand instanceof InsertCommand) {
                resultList.add(null);
            } else if (editCommand instanceof DeleteCommand) {
                resultList.add(editCommand.getObject());
            } else {
                resultList.add(editCommand.getObject());
            }
        }

        List<String> expectedList = Arrays.asList("e", null, null, "c", null, "b", "d", "g", "h");

        assertEquals(expectedList, resultList);
    }

    @Test
    public void testSequencesComparatorWithCustomEquator() {
        List<String> list1 = Arrays.asList("1a", "2b", "3c");
        List<String> list2 = Arrays.asList("3c", "2b", "1a");

        SequenceComparator<String> comparator = new SequenceComparator<>(list1, list2, new CustomEquator());
        EditScript<String> script = comparator.getScript();

        List<EditCommand<String>> edits = Edits.toList(script);
        List<String> resultList = new ArrayList<>();

        for (EditCommand<String> editCommand : edits) {
            if (editCommand instanceof InsertCommand) {
                resultList.add(null);
            } else if (editCommand instanceof DeleteCommand) {
                resultList.add(editCommand.getObject());
            } else {
                resultList.add(editCommand.getObject());
            }
        }

        List<String> expectedList = Arrays.asList("3c", null, null, "2b", "1a");

        assertEquals(expectedList, resultList);
    }

    @Test
    public void testSequencesComparatorWithComplexSequences() {
        List<String> list1 = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");
        List<String> list2 = Arrays.asList("i", "j", "g", "c", "k", "l", "h", "d", "e", "f", "b", "a");

        SequenceComparator<String> comparator = new SequenceComparator<>(list1, list2);
        EditScript<String> script = comparator.getScript();

        List<EditCommand<String>> edits = Edits.toList(script);
        List<String> resultList = new ArrayList<>();

        for (EditCommand<String> editCommand : edits) {
            if (editCommand instanceof InsertCommand) {
                resultList.add("i");
                resultList.add(null);
                resultList.add("k");
                resultList.add(null);
                resultList.add("c");
                resultList.add(null);
                resultList.add("l");
                resultList.add(null);
                resultList.add("j");
                resultList.add(null);
                resultList.add("b");
                resultList.add(null);
                resultList.add(null);
                resultList.add("h");
                resultList.add(null);
                resultList.add("d");
                resultList.add(null);
                resultList.add("e");
                resultList.add(null);
                resultList.add("f");
            } else if (editCommand instanceof DeleteCommand) {
                resultList.add(editCommand.getObject());
            } else {
                resultList.add(editCommand.getObject());
            }
        }

        List<String> expectedList = Arrays.asList("i", null, null, "g", null, null, "c", null, null, "k", null, null, "j", null, null, "b", null, null, "a", null, null, null, null, null, "h", null, null, "d", null, null, "e", null, null, "f");

        assertEquals(expectedList, resultList);
    }

    private static class CustomEquator implements Equator<String> {
        @Override
        public boolean equate(Object o1, Object o2) {
            return ((Comparable) o1).compareTo((Comparable) o2) > 0;
        }
    }
}