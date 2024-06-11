package org.apache.commons.collections4.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.Functor;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Sequences;
import org.apache.commons.collections4.SequencesComparator;
import org.apache.commons.collections4.bean.Equalator;
import org.apache.commons.collections4.functors.ChainedPredicate;
import org.junit.Test;

public class SequencesComparatorTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testSequencesComparator() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 3, 2, 4));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        List<EditCommand<Integer>> commands = script.getCommands();
        List<Integer> resultList = new ArrayList<>();
        for (EditCommand<Integer> command : commands) {
            if (command instanceof KeepCommand) {
                KeepCommand<Integer> keepCommand = (KeepCommand<Integer>) command;
                resultList.add(keepCommand.getElement());
            } else if (command instanceof InsertCommand) {
                InsertCommand<Integer> insertCommand = (InsertCommand<Integer>) command;
                resultList.add(insertCommand.getElement());
            } else if (command instanceof DeleteCommand) {
                // No need to process delete commands as we are only interested in the resulting list
            }
        }

        List<Integer> expectedList = ListUtils.union(list1, ListUtils.difference(list2, list1));
        assertEquals(expectedList, resultList);

        // Test with custom Equator
        Equator<Integer> customEquator = new Equalator();
        List<Integer> list3 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        List<Integer> list4 = new ArrayList<>(Arrays.asList(1, 3, 2, 4));
        SequencesComparator<Integer> comparatorCustomEquator = new SequencesComparator<>(list3, list4, customEquator);
        EditScript<Integer> scriptCustomEquator = comparatorCustomEquator.getScript();
        List<EditCommand<Integer>> commandsCustomEquator = scriptCustomEquator.getCommands();
        List<Integer> resultListCustomEquator = new ArrayList<>();
        for (EditCommand<Integer> command : commandsCustomEquator) {
            if (command instanceof KeepCommand) {
                KeepCommand<Integer> keepCommand = (KeepCommand<Integer>) command;
                resultListCustomEquator.add(keepCommand.getElement());
            } else if (command instanceof InsertCommand) {
                InsertCommand<Integer> insertCommand = (InsertCommand<Integer>) command;
                resultListCustomEquator.add(insertCommand.getElement());
            } else if (command instanceof DeleteCommand) {
                // No need to process delete commands as we are only interested in the resulting list
            }
        }

        List<Integer> expectedListCustomEquator = ListUtils.union(list3, ListUtils.difference(list4, list3));
        assertEquals(expectedListCustomEquator, resultListCustomEquator);

        // Test null lists
        List<Integer> list5 = null;
        List<Integer> list6 = new ArrayList<>();
        SequencesComparator<Integer> comparatorNullLists = new SequencesComparator<>(list5, list6);
        EditScript<Integer> scriptNullLists = comparatorNullLists.getScript();
        assertNotNull(scriptNullLists);

        // Test empty lists
        List<Integer> list7 = new ArrayList<>();
        List<Integer> list8 = new ArrayList<>();
        SequencesComparator<Integer> comparatorEmptyLists = new SequencesComparator<>(list7, list8);
        EditScript<Integer> scriptEmptyLists = comparatorEmptyLists.getScript();
        assertNotNull(scriptEmptyLists);
        List<EditCommand<Integer>> commandsEmptyLists = scriptEmptyLists.getCommands();
        assertEquals(0, commandsEmptyLists.size());

        // Test same lists
        List<Integer> list9 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        List<Integer> list10 = list9;
        SequencesComparator<Integer> comparatorSameLists = new SequencesComparator<>(list9, list10);
        EditScript<Integer> scriptSameLists = comparatorSameLists.getScript();
        assertNotNull(scriptSameLists);
        List<EditCommand<Integer>> commandsSameLists = scriptSameLists.getCommands();
        assertEquals(0, commandsSameLists.size());

        // Test equal lists with different orders
        List<Integer> list11 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        List<Integer> list12 = new ArrayList<>(Arrays.asList(3, 2, 1, 4));
        SequencesComparator<Integer> comparatorEqualListsWithDifferentOrders
                = new SequencesComparator<>(list11, list12, new ChainedPredicate<>(new Functor[] { new Functor<Object>() {
                        @Override
                        public boolean functorValue(Object obj) {
                            return obj != null;
                        }
                    }, new Functor<Object, Object>() {
                        @Override
                        public Object functorValue(Object obj1, Object obj2) {
                            return Equators.equal((Comparable)obj1, (Comparable)obj2);
                        }
                    } }));
        EditScript<Integer> scriptEqualListsWithDifferentOrders = comparatorEqualListsWithDifferentOrders.getScript();
        List<EditCommand<Integer>> commandsEqualListsWithDifferentOrders = scriptEqualListsWithDifferentOrders.getCommands();
        List<Integer> resultListEqualListsWithDifferentOrders = new ArrayList<>();
        for (EditCommand<Integer> command : commandsEqualListsWithDifferentOrders) {
            if (command instanceof KeepCommand) {
                KeepCommand<Integer> keepCommand = (KeepCommand<Integer>) command;
                resultListEqualListsWithDifferentOrders.add(keepCommand.getElement());
            } else if (command instanceof InsertCommand) {
                InsertCommand<Integer> insertCommand = (InsertCommand<Integer>) command;
                resultListEqualListsWithDifferentOrders.add(insertCommand.getElement());
            } else if (command instanceof DeleteCommand) {
                // No need to process delete commands as we are only interested in the resulting list
            }
        }

        List<Integer> expectedListEqualListsWithDifferentOrders = ListUtils.union(list11, ListUtils.difference(list12, list11));
        assertEquals(expectedListEqualListsWithDifferentOrders, resultListEqualListsWithDifferentOrders);
    }
}