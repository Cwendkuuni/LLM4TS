import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.TestObjects;
import org.junit.Assert;
import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testConstructorWithListArguments() {
        List<Integer> list1 = ListUtils.nCopies(5, 1);
        List<Integer> list2 = ListUtils.nCopies(5, 2);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        Assert.assertNotNull(comparator);
    }
}