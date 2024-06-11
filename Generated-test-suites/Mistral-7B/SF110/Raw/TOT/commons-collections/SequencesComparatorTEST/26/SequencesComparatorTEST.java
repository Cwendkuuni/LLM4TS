import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Sequences;
import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparatorWithDefaultEquator() {
        List<Integer> list1 = List.of(1, 2, 3);
        List<Integer> list2 = List.of(3, 2, 1);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();

        List<Integer> expectedResult = ListUtils.difference(Lists.reverse(list1), Lists.reverse(list2));

        assertEquals(expectedResult, Sequences.editOpsToList(script));
    }
}