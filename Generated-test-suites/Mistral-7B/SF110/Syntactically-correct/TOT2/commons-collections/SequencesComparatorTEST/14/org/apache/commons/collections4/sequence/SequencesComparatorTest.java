import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.iterators.EmptyIterator;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparatorConstruction() {
        // Test with valid input lists
        List<Integer> inputList1 = List.of(1, 2, 3);
        List<Integer> inputList2 = List.of(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(inputList1, inputList2);
        assertNotNull(comparator);

        // Test with null input lists
        List<Integer> nullList1 = null;
        List<Integer> nullList2 = null;
        assertThrows(NullPointerException.class, () -> new SequencesComparator<>(nullList1, nullList2));
        assertThrows(NullPointerException.class, () -> new SequencesComparator<>(inputList1, nullList2));
        assertThrows(NullPointerException.class, () -> new SequencesComparator<>(nullList1, inputList2));
    }

    // ... Continue with tests for other methods ...
}