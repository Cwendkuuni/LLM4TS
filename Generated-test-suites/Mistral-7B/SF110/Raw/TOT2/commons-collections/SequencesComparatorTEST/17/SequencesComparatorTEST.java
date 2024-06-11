import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.equator.EqualsEquator;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparator() {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        assertNotNull(comparator.getScript());
    }
}