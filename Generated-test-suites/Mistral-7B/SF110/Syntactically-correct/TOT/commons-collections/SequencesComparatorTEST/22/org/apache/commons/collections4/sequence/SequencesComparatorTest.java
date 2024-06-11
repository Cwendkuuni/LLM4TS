import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.apache.commons.collections4.iterators.*;
import org.apache.commons.collections4.sequence.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparatorEqual() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<>(sequence1);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        Assert.assertEquals("Keep commands only", script.size(), 0);
    }

    @Test
    public void testSequencesComparatorDifferentLength() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        Assert.assertArrayEquals("Delete and Insert commands", new Object[]{-1, 4}, script.toArray());
    }

    @Test
    public void testSequencesComparatorSameElementDifferentOrder() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 3, 2, 1));

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        Assert.assertArrayEquals("Reverse Insert commands", new Object[]{-1, 0, 1, 2}, script.toArray());
    }

    @Test(expected = RuntimeException.class)
    public void testSequencesComparatorDifferentTypes() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        List<String> sequence2 = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        throw new RuntimeException("Comparing incompatible types");
    }

    @Test
    public void testSequencesComparatorNullLists() {
        List<String> sequence1 = null;
        List<String> sequence2 = null;

        try {
            SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
            EditScript<String> script = comparator.getScript();
        } catch (NullPointerException e) {
            Assert.assertEquals("Expected NullPointerException", e.getMessage(), "The lists must not be null.");
        }
    }

    @Test
    public void testSequencesComparatorNullElements() {
        List<String> sequence1 = Arrays.asList("a", null, "c", "d");
        List<String> sequence2 = Arrays.asList("a", "b", "c", "d");

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = comparator.getScript();

        Assert.assertArrayEquals("Delete and Insert commands for null elements", new Object[]{-1, 2}, script.toArray());
    }

    @Test
    public void testSequencesComparatorEmptyLists() {
        List<Integer> sequence1 = Collections.emptyList();
        List<Integer> sequence2 = Collections.emptyList();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        Assert.assertEquals("Empty scripts", script.size(), 0);
    }
}