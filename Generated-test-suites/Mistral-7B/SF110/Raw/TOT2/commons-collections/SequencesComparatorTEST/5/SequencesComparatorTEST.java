import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.sequence.SequencesComparator;
import org.apache.commons.collections4.ListUtils;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparatorWithDefaultEquator() {
        List<Integer> list1 = ListUtils.list(1, 2, 3);
        List<Integer> list2 = ListUtils.list(2, 1, 3);
        Equator<Integer> equator = DefaultEquator.defaultEquator();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, equator);
        EditScript<Integer> script = comparator.getScript();

        // Check if the keep commands are in the correct order
        List<Integer> expected = ListUtils.list(1, 2);
        List<Integer> actual = new ArrayList<>();
        script.applyCommands(new KeepCommandVisitor<>(), actual);
        Assert.assertEquals(expected, actual);
    }
}