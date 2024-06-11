import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparatorDefault() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);

        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        // Check that the script is empty since the sequences are equal
        assertTrue(script.isEmpty());
    }
}