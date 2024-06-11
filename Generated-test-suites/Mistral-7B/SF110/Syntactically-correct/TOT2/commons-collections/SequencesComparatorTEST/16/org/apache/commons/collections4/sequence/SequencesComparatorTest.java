import org.apache.commons.collections4.ListFactory;
import org.apache.commons.collections4.SequencesComparator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparatorConstructor() {
        List<Integer> list1 = ListFactory.list(1, 2, 3, 4);
        List<Integer> list2 = ListFactory.list(4, 3, 2, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        assertNotNull(comparator);
    }
}