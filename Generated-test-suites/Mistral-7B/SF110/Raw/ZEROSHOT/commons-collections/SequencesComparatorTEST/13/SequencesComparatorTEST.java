package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.Functor;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.EditableList;
import org.apache.commons.collections4.EditScript;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.Functors;
import org.apache.commons.collections4.Transformers;
import org.junit.Test;

public class SequencesComparatorTest
{
    @SuppressWarnings("unchecked")
    @Test
    public void testSequencesComparator()
    {
        // Test SequencesComparator construction
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 4, 5, 2, 3));
        Equator<Integer> equator = Functors.identityEquator();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);

        // Test getScript method
        EditScript<Integer> script = comparator.getScript();
        List<EditCommand<Integer>> commands = new ArrayList<>();
        while (!script.isEmpty())
        {
            commands.add(script.nextCommand());
        }

        // Test sequence transformation using KeepCommand, DeleteCommand, and InsertCommand
        List<Integer> expectedList1 = Arrays.asList(1, 1, 2, 3, 4, 5);
        List<Integer> expectedList2 = Arrays.asList(1, 4, 5, 2, 3, 1, 2, 3);
        List<Integer> list1Transformed = new ArrayList<>();
        List<Integer> list2Transformed = new ArrayList<>();

        for (EditCommand<Integer> command : commands)
        {
            if (command instanceof KeepCommand)
            {
                list1Transformed.add(((KeepCommand<Integer>) command).getObject());
            }
            else if (command instanceof DeleteCommand)
            {
                list2Transformed.add(null);
            }
            else if (command instanceof InsertCommand)
            {
                list1Transformed.add(null);
                list2Transformed.add(((InsertCommand<Integer>) command).getObject());
            }
        }

        for (int i = 0; i < list1Transformed.size(); ++i)
        {
            assertEquals("Mismatch at index " + i, list1Transformed.get(i), list2Transformed.get(i));
        }
        assertFalse("List 1 should have fewer elements than list 2 after transformation", list1Transformed.size() > list2Transformed.size());

        // Test SequencesComparator with custom Equator
        Equator<Integer> customEquator = Transformers.instanceOf(Integer.class, new Functor<Object, Integer>()
        {
            @Override
            public Integer transform(Object object)
            {
                return ((Number) object).intValue() * 2;
            }
        });
        List<Integer> list3 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> list4 = new ArrayList<>(Arrays.asList(2, 4, 6));
        SequencesComparator<Integer> comparator2 = new SequencesComparator<>(list3, list4, customEquator);
        EditScript<Integer> script2 = comparator2.getScript();

        List<Integer> expectedList3 = Arrays.asList(1, 1, 2, 3, 3, 3);
        List<Integer> expectedList4 = Arrays.asList(2, 3, 3, 3, 3, 2);
        List<Integer> list3Transformed = new ArrayList<>();
        List<Integer> list4Transformed = new ArrayList<>();

        while (!script2.isEmpty())
        {
            EditCommand<Integer> command = script2.nextCommand();
            if (command instanceof KeepCommand)
            {
                list3Transformed.add(((KeepCommand<Integer>) command).getObject());
            }
            else if (command instanceof DeleteCommand)
            {
                list4Transformed.add(null);
            }
            else if (command instanceof InsertCommand)
            {
                list3Transformed.add(null);
                list4Transformed.add(((InsertCommand<Integer>) command).getObject());
            }
        }

        for (int i = 0; i < list3Transformed.size(); ++i)
        {
            assertEquals("Mismatch at index " + i, list3Transformed.get(i) * 2, list4Transformed.get(i) * 2);
        }
        assertFalse("List 3 should have fewer elements than list 4 after transformation", list3Transformed.size() > list4Transformed.size());
    }
}