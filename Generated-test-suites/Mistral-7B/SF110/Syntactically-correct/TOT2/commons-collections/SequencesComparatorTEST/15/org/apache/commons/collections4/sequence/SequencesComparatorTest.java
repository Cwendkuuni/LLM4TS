import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Sequences;
import org.apache.commons.collections4.functors.ChainedPredicate;
import org.apache.commons.collections4.functors.PredicateCombiners;
import org.junit.Test;
import org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testGetScript() {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        list1.add(1);
        list1.add(2);
        list1.add(3);

        list2.add(2);
        list2.add(3);
        list2.add(1);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        List<EditCommand<Integer>> expectedCommands = ListUtils.emptyIfNull(Sequences.difference(list1, list2));
        List<EditCommand<Integer>> actualCommands = new ArrayList<>();

        for (EditCommand<Integer> command : script.getCommands()) {
            if (command instanceof KeepCommand) {
                actualCommands.add(command);
            }
        }

        assertTrue(Sequences.equals(expectedCommands, actualCommands));
    }
}