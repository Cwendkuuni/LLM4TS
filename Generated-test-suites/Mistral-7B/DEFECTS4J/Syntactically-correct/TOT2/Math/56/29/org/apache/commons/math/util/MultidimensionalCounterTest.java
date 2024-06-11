import org.apache.commons.math.util.MultidimensionalCounter;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test // Test constructor with valid sizes
    public void testMultidimensionalCounterValidSize() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(sizes.length, mdc.getDimension());
    }

    @Test // Test getCounts method with valid index
    public void testGetCountsValidIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        int index = 0;
        int[] counts = mdc.getCounts(index);
        assertEquals(2, counts[0]);
        assertEquals(4, counts[1]);
        assertEquals(3, counts[2]);
    }

    @Test // Test getCount method with valid index
    public void testGetCountValidIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        int index = 0;
        int count = mdc.getCount(index);
        assertEquals(0, count);
    }

    @Test // Test iterator next method with valid counters
    public void testIteratorNextValidCounters() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = mdc.iterator();
        assertTrue(iterator.hasNext());
        Integer next = iterator.next();
        assertEquals(0, next.intValue());
    }

    // Test edge cases and exceptions here

}