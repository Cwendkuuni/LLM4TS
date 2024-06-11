import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.Functors;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparatorWithEqualSequences() {
        List<Integer> list1 = ListUtils.list(1, 2, 3);
        List<Integer> list2 = ListUtils.list(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(3, script.size());
        for (int i = 0; i < script.size(); i++) {
            Command command = script.getCommand(i);
            if (command instanceof KeepCommand) {
                KeepCommand keepCommand = (KeepCommand<Integer>) command;
                assertEquals(ListUtils.get(list1, keepCommand.getElementIndex()), ListUtils.get(list2, keepCommand.getElementIndex()));
            }
        }
    }
}