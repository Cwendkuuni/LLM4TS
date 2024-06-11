import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testGetScript() {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(4);
        list2.add(1);
        list2.add(3);
        list2.add(2);
        list2.add(4);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        // Add your assertions here
    }
}