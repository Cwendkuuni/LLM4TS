package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.Functor;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Sequences;
import org.apache.commons.collections4.sequence.EditScript;
import org.apache.commons.collections4.sequence.SequencesComparator;

public class SequencesComparatorTest {

    @SuppressWarnings("unchecked")
    private static <T> SequencesComparator<T> createComparator(List<T> sequence1, List<T> sequence2) {
        return new SequencesComparator<T>(sequence1, sequence2);
    }

    @SuppressWarnings("unchecked")
    private static <T> SequencesComparator<T> createComparator(List<T> sequence1, List<T> sequence2, Equator<? super T> equator) {
        return new SequencesComparator<T>(sequence1, sequence2, equator);
    }

    @Test
    public void testGetScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 3, 2, 4, 5);

        SequencesComparator<Integer> comparator = createComparator(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<Functor<Integer, Integer>> commands = Sequences.asList(script.getCommands());
        List<Integer> expectedCommands = Arrays.asList(
                new DeleteCommand<>(1),
                new InsertCommand<>(2),
                new KeepCommand<>(3),
                new KeepCommand<>(4),
                new KeepCommand<>(5)
        );

        assertArrayEquals(expectedCommands.toArray(), commands.toArray());
    }

    @Test
    public void testGetScriptWithEquator() {
        List<String> sequence1 = Arrays.asList("foo", "bar", "baz");
        List<String> sequence2 = Arrays.asList("bar", "qux", "baz", "foo");

        Equator<String> equator = (o1, o2) -> o1.equals(o2);
        SequencesComparator<String> comparator = createComparator(sequence1, sequence2, equator);
        EditScript<String> script = comparator.getScript();

        List<Functor<String, String>> commands = Sequences.asList(script.getCommands());
        List<String> expectedCommands = Arrays.asList(
                new DeleteCommand<String>("foo"),
                new InsertCommand<String>("bar"),
                new KeepCommand<String>("baz"),
                new KeepCommand<String>("baz"),
                new InsertCommand<String>("qux"),
                new InsertCommand<String>("foo")
        );

        assertArrayEquals(expectedCommands.toArray(), commands.toArray());
    }

    @Test
    public void testGetScriptWithNullSequences() {
        List<Integer> sequence1 = null;
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);

        SequencesComparator<Integer> comparator = createComparator(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<Functor<Integer, Integer>> commands = Sequences.asList(script.getCommands());
        List<Functor<Integer, Integer>> emptyCommands = Collections.emptyList();

        assertArrayEquals(emptyCommands.toArray(), commands.toArray());
    }

    @Test
    public void testGetScriptWithEmptySequences() {
        List<Integer> sequence1 = Arrays.asList();
        List<Integer> sequence2 = Arrays.asList();

        SequencesComparator<Integer> comparator = createComparator(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        List<Functor<Integer, Integer>> commands = Sequences.asList(script.getCommands());
        List<Functor<Integer, Integer>> emptyCommands = Collections.emptyList();

        assertArrayEquals(emptyCommands.toArray(), commands.toArray());
    }

    @Test(expected = RuntimeException.class)
    public void testGetScriptWithInternalError() {
        List<Integer> sequence1 = Arrays.asList(1, 2);
        List<Integer> sequence2 = Arrays.asList(2, 1);

        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
    }
}