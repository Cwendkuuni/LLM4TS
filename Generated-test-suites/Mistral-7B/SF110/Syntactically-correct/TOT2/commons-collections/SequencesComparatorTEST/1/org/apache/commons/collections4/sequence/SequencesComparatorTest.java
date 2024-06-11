
package org.apache.commons.collections4.sequence;

import org.apache.commons.collections4.ListFactory;
import org.apache.commons.collections4.list.ArrayListList;
import org.apache.commons.collections4.TestObject;
import org.apache.commons.collections4.TestObjectEquator;
import org.apache.commons.collections4.functors.ChainedPredicate;
import org.apache.commons.collections4.functors.PredicateWrapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparator_DefaultEquator() {
        List<TestObject> sequence1 = ListFactory.newList(new TestObject("A"), new TestObject("B"), new TestObject("C"), new TestObject("D"));
        List<TestObject> sequence2 = ListFactory.newList(new TestObject("C"), new TestObject("B"), new TestObject("A"), new TestObject("D"));
        SequencesComparator<TestObject> comparator = new SequencesComparator<>(sequence1, sequence2);
        assertNotNull(comparator.getScript());
    }

    @Test
    public void testSequencesComparator_CustomEquator() {
        List<TestObject> sequence1 = ListFactory.newList(new TestObject("A"), new TestObject("B"), new TestObject("C"), new TestObject("D"));
        List<TestObject> sequence2 = ListFactory.newList(new TestObject("C"), new TestObject("B"), new TestObject("A"), new TestObject("D"));
        Equator<TestObject> customEquator = new TestObjectEquator();
        SequencesComparator<TestObject> comparator = new SequencesComparator<>(sequence1, sequence2, customEquator);
        assertNotNull(comparator.getScript());
    }

    @Test
    public void testGetScript_EmptySequences() {
        List<TestObject> sequence1 = ListFactory.newList();
        List<TestObject> sequence2 = ListFactory.newList();
        SequencesComparator<TestObject> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<TestObject> script = comparator.getScript();
        assertNotNull(script);
        assertTrue(script.isEmpty());
    }

    @Test
    public void testGetScript_SequencesWithSameElements() {
        List<TestObject> sequence1 = ListFactory.newList(new TestObject("A"), new TestObject("B"), new TestObject("C"), new TestObject("D"));
        List<TestObject> sequence2 = ListFactory.newList(new TestObject("A"), new TestObject("B"), new TestObject("C"), new TestObject("D"));
        SequencesComparator<TestObject> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<TestObject> script = comparator.getScript();
        assertTrue(script.isEmpty());
    }

    @Test
    public void testGetScript_SequencesWithSameElementsInDifferentOrders() {
        List<TestObject> sequence1 = ListFactory.newList(new TestObject("A"), new TestObject("B"), new TestObject("C"), new TestObject("D"));
        List<TestObject> sequence2 = ListFactory.newList(new TestObject("D"), new TestObject("C"), new TestObject("B"), new TestObject("A"));
        SequencesComparator<TestObject> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<TestObject> script = comparator.getScript();
        assertEquals(4, script.size());
        // Check the commands in the script
        for (int i = 0; i < script.size(); i++) {
            Command command = script.getCommand(i);
            if (command instanceof KeepCommand) {
                KeepCommand keepCommand = (KeepCommand) command;
                assertEquals(script.size() - i, keepCommand.getObject().hashCode());
            } else if (command instanceof DeleteCommand) {
                DeleteCommand deleteCommand = (DeleteCommand) command;
                assertEquals(script.size() - i, deleteCommand.getObject().hashCode());
            } else if (command instanceof InsertCommand) {
                InsertCommand insertCommand = (InsertCommand) command;
                assertEquals(script.size() - i, insertCommand.getObject().hashCode());
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSequencesComparator_NullLists() {
        List<TestObject> sequence1 = null;
        List<TestObject> sequence2 = null;
        SequencesComparator<TestObject> comparator = new SequencesComparator<>(sequence1, sequence2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSequencesComparator_NullElementsInLists() {
        List<TestObject> sequence1 = ListFactory.newList(new TestObject("A"), null, new TestObject("C"), new TestObject("D"));
        List<TestObject> sequence2 = ListFactory.newList(new TestObject("C"), new TestObject("B"), new TestObject("A"), new TestObject("D"));
        SequencesComparator<TestObject> comparator = new SequencesComparator<>(sequence1, sequence2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSequencesComparator_NullEquator() {
        List<TestObject> sequence1 = ListFactory.newList(new TestObject("A"), new TestObject("B"), new TestObject("C"), new TestObject("D"));
        List<TestObject> sequence2 = ListFactory.newList(new TestObject("C"), new TestObject("B"), new TestObject("A"), new TestObject("D"));
        Equator<TestObject> nullEquator = null;
        SequencesComparator<TestObject> comparator = new SequencesComparator<>(sequence1, sequence2, nullEquator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSequencesComparator_DifferentListSizes() {
        List<TestObject> sequence1 = ListFactory.newList(new TestObject("A"), new TestObject("B"), new TestObject("C"), new TestObject("D"));
        List<TestObject> sequence2 = ListFactory.newList(new TestObject("A"), new TestObject("B"), new TestObject("C"));
        SequencesComparator<TestObject> comparator = new SequencesComparator<>(sequence1, sequence2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSequencesComparator_CustomEquator_NotEquate() {
        List<TestObject> sequence1 = ListFactory.newList(new TestObject("A"), new TestObject("B"), new TestObject("C"), new TestObject("D"));
        List<TestObject> sequence2 = ListFactory.newList(new TestObject("A"), new TestObject("E"), new TestObject("C"), new TestObject("D"));
        Equator<TestObject> customEquator = new TestObjectEquator();
        SequencesComparator<TestObject> comparator = new SequencesComparator<>(sequence1, sequence2, customEquator);
    }
}

class TestObject {
    private final String name;

    public TestObject(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestObject)) {
            return false;
        }
        TestObject testObject = (TestObject) o;
        return name.equals(testObject.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

class TestObjectEquator implements Equator<TestObject> {
    @Override
    public boolean equate(final Object object1, final Object object2) {
        return ((TestObject) object1).equals(object2);
    }
}

interface Command {
}

class KeepCommand<T> implements Command {
    private final T object;

    public KeepCommand(T object) {
        this.object = object;
    }
}

class DeleteCommand<T> implements Command {
    private final T object;

    public DeleteCommand(T object) {
        this.object = object;
    }
}

class InsertCommand<T> implements Command {
    private final T object;
    private final int index;

    public InsertCommand(T object, int index) {
        this.object = object;
        this.index = index;
    }
}

class EditScript<T> {
    private final List<Command> commands;

    public EditScript() {
        this.commands = new ArrayListList<>();
    }

    public void append(Command command) {
        this.commands.add(command);
    }

    public Command getCommand(int index) {
        return this.commands.get(index);
    }

    public int size() {
        return this.commands.size();
    }

    public boolean isEmpty() {
        return this.commands.isEmpty();
    }
}
