import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testGetScript() {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        // Add some elements to the lists
        list1.add(1);
        list1.add(3);
        list1.add(5);

        list2.add(2);
        list2.add(3);
        list2.add(4);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        // Check if the script has the expected commands
        assertEquals(11, script.size());
        assertTrue(script.getCommands().contains(new KeepCommand<>(1)));
        assertTrue(script.getCommands().contains(new DeleteCommand<>(3)));
        assertTrue(script.getCommands().contains(new InsertCommand<>(2)));
        // Add more assertions to check other commands in the script
    }
}