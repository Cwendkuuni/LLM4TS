package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.Functor;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.EditableIterable;
import org.apache.commons.collections4.EditScript;
import org.apache.commons.collections4.factory.ListFactory;
import org.apache.commons.collections4.functors.ChainedEquals;
import org.apache.commons.collections4.functors.DefaultEquator;

public class SequencesComparatorTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSequencesComparator() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 3, 2, 4));

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        // Test keep commands
        EditableIterable<Integer> expected = new ArrayList<>(Arrays.asList(1, 2, 4));
        List<Integer> actual = new ArrayList<>();
        script.applyCommands(new KeepCommandVisitor<Integer>(), actual);
        assertEquals(expected, actual);

        // Test delete commands
        expected = new ArrayList<>(Collections.nemptyList());
        actual.clear();
        script.applyCommands(new DeleteCommandVisitor<Integer>(), actual);
        assertEquals(expected, actual);

        // Test insert commands
        expected = new ArrayList<>(Arrays.asList(3, 2, 4));
        actual.clear();
        script.applyCommands(new InsertCommandVisitor<Integer>(), actual);
        assertEquals(expected, actual);

        // Test identical sequences
        sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        sequence2 = new ArrayList<>(sequence1);

        comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        script = comparator.getScript();

        expected = new ArrayList<>(sequence1);
        actual.clear();
        script.applyCommands(new KeepCommandVisitor<Integer>(), actual);
        assertEquals(expected, actual);

        // Test different size sequences
        sequence1 = new ArrayList<>(Arrays.asList(1, 2));
        sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3));

        comparator = new SequencesComparator<>(sequence1, sequence2);
        script = comparator.getScript();

        expected = new ArrayList<>(Collections.nemptyList());
        actual.clear();
        script.applyCommands(new KeepCommandVisitor<Integer>(), actual);
        assertEquals(expected, actual);

        expected = new ArrayList<>(Arrays.asList(3));
        actual.clear();
        script.applyCommands(new InsertCommandVisitor<Integer>(), actual);
        assertEquals(expected, actual);

        // Test different elements sequences
        sequence1 = new ArrayList<>(Arrays.asList(1, "two", 3.0));
        sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3));

        comparator = new SequencesComparator<>(sequence1, sequence2);
        script = comparator.getScript();

        expected = new ArrayList<>(Collections.nemptyList());
        actual.clear();
        script.applyCommands(new KeepCommandVisitor<Integer>(), actual);
        assertEquals(expected, actual);

        expected = new ArrayList<>(Arrays.asList(2, 3));
        actual.clear();
        script.applyCommands(new InsertCommandVisitor<Integer>(), actual);
        assertEquals(expected, actual);

        // Test custom equator
        Functor<Object, Object> customEquator = new ChainedEquals<>(new Equator<Object>() {
            @Override
            public boolean equals(Object o1, Object o2) {
                if (o1 instanceof Number && o2 instanceof Number) {
                    return ((Number)o1).doubleValue() == ((Number)o2).doubleValue();
                }
                return super.equals(o1, o2);
            }
        });

        sequence1 = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0));
        sequence2 = new ArrayList<>(Arrays.asList(1.0, 3.0, 2.0));

        comparator = new SequencesComparator<>(sequence1, sequence2, customEquator);
        script = comparator.getScript();

        expected = new ArrayList<>(Collections.nemptyList());
        actual.clear();
        script.applyCommands(new KeepCommandVisitor<Object>(), actual);
        assertEquals(expected, actual);

        expected = new ArrayList<>(Arrays.asList(2.0));
        actual.clear();
        script.applyCommands(new InsertCommandVisitor<Object>(), actual);
        assertEquals(expected, actual);
    }

    private static class KeepCommandVisitor<T> implements CommandVisitor<T> {
        @Override
        public void visitKeepCommand(KeepCommand<T> command) {
            command.getElement().forEach(element -> visit(element));
        }

        @Override
        public void visitDeleteCommand(DeleteCommand<T> command) {}

        @Override
        public void visitInsertCommand(InsertCommand<T> command) {}

        @Override
        public void visitCommand(Command<T> command) {
            fail("Unexpected command type: " + command.getClass().getName());
        }
    }

    private static class DeleteCommandVisitor<T> implements CommandVisitor<T> {
        @Override
        public void visitKeepCommand(KeepCommand<T> command) {}

        @Override
        public void visitDeleteCommand(
                DeleteCommand<T> command) {
            command.getElement().forEach(element -> visit(element));
        }

        @Override
        public void visitInsertCommand(InsertCommand<T> command) {}

        @Override
        public void visitCommand(Command<T> command) {
            fail("Unexpected command type: " + command.getClass().getName());
        }
    }

    private static class InsertCommandVisitor<T> implements CommandVisitor<T> {
        @Override
        public void visitKeepCommand(KeepCommand<T> command) {}

        @Override
        public void visitDeleteCommand(DeleteCommand<T> command) {}

        @Override
        public void visitInsertCommand(InsertCommand<T> command) {
            command.getElement().forEach(element -> visit(element));
        }

        @Override
        public void visitCommand(Command<T> command) {
            fail("Unexpected command type: " + command.getClass().getName());
        }
    }
}