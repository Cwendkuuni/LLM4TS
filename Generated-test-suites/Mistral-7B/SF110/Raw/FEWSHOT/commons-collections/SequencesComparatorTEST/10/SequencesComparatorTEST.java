package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.EditScript;
import org.apache.commons.collections4.EditScriptOp;
import org.apache.commons.collections4.Functor;
import org.apache.commons.collections4.ListUtils;
import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparator() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(3, 4, 5, 2, 1));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        EditScriptOp<Integer> op;
        Integer o;

        assertEquals(script.size(), 5);
        op = script.getCommand(0);
        assertSame(op, new KeepCommand<>(1));
        op = script.getCommand(1);
        assertSame(op, new DeleteCommand<>(2));
        op = script.getCommand(2);
        assertSame(op, new InsertCommand<>(3));
        op = script.getCommand(3);
        assertSame(op, new KeepCommand<>(4));
        op = script.getCommand(4);
        assertSame(op, new InsertCommand<>(5));

        List<Integer> resultList1 = new ArrayList<>();
        List<Integer> resultList2 = new ArrayList<>();
        script.applyTo(list1, resultList1);
        script.applyTo(list2, resultList2);

        assertArrayEquals(new Integer[]{1, 3, 4, 5}, list1.toArray());
        assertArrayEquals(new Integer[]{3, 4, 5, 2, 1}, list2.toArray());
        assertEquals(list1, resultList1);
        assertEquals(list2, resultList2);

        List<String> stringList1 = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e"));
        List<String> stringList2 = new ArrayList<>(Arrays.asList("d", "c", "b", "a", "e"));
        comparator = new SequencesComparator<>(stringList1, stringList2);
        script = comparator.getScript();
        op = script.getCommand(0);
        assertSame(op, new KeepCommand<>("a"));
        op = script.getCommand(1);
        assertSame(op, new DeleteCommand<>("b"));
        op = script.getCommand(2);
        assertSame(op, new InsertCommand<>("d"));
        op = script.getCommand(3);
        assertSame(op, new KeepCommand<>("c"));
        op = script.getCommand(4);
        assertSame(op, new InsertCommand<>("b"));
        op = script.getCommand(5);
        assertSame(op, new InsertCommand<>("e"));

        resultList1 = new ArrayList<>();
        resultList2 = new ArrayList<>();
        script.applyTo(stringList1, resultList1);
        script.applyTo(stringList2, resultList2);

        assertArrayEquals(new String[]{"a", "d", "c", "b", "e"}, stringList1.toArray(new String[0]));
        assertArrayEquals(new String[]{"d", "c", "b", "a", "e"}, stringList2.toArray(new String[0]));
        assertEquals(stringList1, resultList1);
        assertEquals(stringList2, resultList2);

        List<Integer> list1_1 = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1));
        List<Integer> list2_1 = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1));
        comparator = new SequencesComparator<>(list1_1, list2_1);
        script = comparator.getScript();
        op = script.getCommand(0);
        assertSame(op, new KeepCommand<>(1));
        op = script.getCommand(1);
        assertSame(op, new KeepCommand<>(1));
        op = script.getCommand(2);
        assertSame(op, new KeepCommand<>(1));
        op = script.getCommand(3);
        assertSame(op, new KeepCommand<>(1));
        op = script.getCommand(4);
        assertSame(op, new KeepCommand<>(1));

        resultList1 = new ArrayList<>();
        resultList2 = new ArrayList<>();
        script.applyTo(list1_1, resultList1);
        script.applyTo(list2_1, resultList2);

        assertArrayEquals(new Integer[]{1, 1, 1, 1, 1}, list1_1.toArray());
        assertArrayEquals(new Integer[]{1, 1, 1, 1, 1}, list2_1.toArray());
        assertEquals(list1_1, resultList1);
        assertEquals(list2_1, resultList2);

        List<String> list1_2 = new ArrayList<>(Arrays.asList("a", null, "b", null, "c"));
        List<String> list2_2 = new ArrayList<>(Arrays.asList(null, "b", null, "a", "c"));
        comparator = new SequencesComparator<>(list1_2, list2_2);
        script = comparator.getScript();
        op = script.getCommand(0);
        assertNull(op);
        op = script.getCommand(1);
        assertNull(op);
        op = script.getCommand(2);
        assertSame(op, new InsertCommand<>("b"));
        op = script.getCommand(3);
        assertSame(op, new KeepCommand<>("a"));
        op = script.getCommand(4);
        assertSame(op, new KeepCommand<>("c"));

        resultList1 = new ArrayList<>();
        resultList2 = new ArrayList<>();
        script.applyTo(list1_2, resultList1);
        script.applyTo(list2_2, resultList2);

        assertArrayEquals(new String[]{"", "b", "", "a", "c"}, list1_2.toArray(new String[0]));
        assertArrayEquals(new String[]{"b", "", "a", "", "c"}, list2_2.toArray(new String[0]));
        assertEquals(list1_2, resultList1);
        assertEquals(list2_2, resultList2);

        List<Integer> list1_3 = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1));
        List<Integer> list2_3 = new ArrayList<>(Collections.singletonList(1));
        Functor<Integer, Integer> equator = new IdentityFunctor<>();
        comparator = new SequencesComparator<>(list1_3, list2_3, equator);
        script = comparator.getScript();
        op = script.getCommand(0);
        assertSame(op, new DeleteCommand<>(1));
        op = script.getCommand(1);
        assertNull(op);

        resultList1 = new ArrayList<>();
        resultList2 = new ArrayList<>();
        script.applyTo(list1_3, resultList1);
        script.applyTo(list2_3, resultList2);

        assertArrayEquals(new Integer[]{1, 1, 1, 1}, list1_3.toArray());
        assertArrayEquals(new Integer[]{1}, list2_3.toArray());
        assertEquals(list1_3, resultList1);
        assertEquals(list2_3, resultList2);

        List<String> list1_4 = new ArrayList<>(Arrays.asList("a", null, "b", null, "c"));
        List<String> list2_4 = new ArrayList<>();
        comparator = new SequencesComparator<>(list1_4, list2_4);
        script = comparator.getScript();
        op = script.getCommand(0);
        assertNull(op);
        op = script.getCommand(1);
        assertNull(op);
        op = script.getCommand(2);
        assertNull(op);

        resultList1 = new ArrayList<>();
        script.applyTo(list1_4, resultList1);

        assertArrayEquals(new String[]{"a", "", "b", "", "c"}, list1_4.toArray(new String[0]));
        assertEquals(list1_4, resultList1);
    }
}