import org.junit.Test;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Equals;
import static org.junit.Assert.*;

public class SequencesComparatorTest {
    @Test
    public void testSequencesComparatorConstructorWithTwoLists() {
        List<Integer> list1 = ListUtils.niblingList(1, 5);
        List<Integer> list2 = ListUtils.niblingList(2, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        assertEquals(11, comparator.vDown.length);
        assertEquals(11, comparator.vUp.length);
    }
}