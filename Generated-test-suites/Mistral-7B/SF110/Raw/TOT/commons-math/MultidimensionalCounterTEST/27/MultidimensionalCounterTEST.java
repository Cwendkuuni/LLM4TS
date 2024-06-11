package org.apache.commons.math3.util.test;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.junit.jupiter.api.*;

public class MultidimensionalCounterTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void constructorNegativeSizeTest() {
        assertThrows(NotStrictlyPositiveException.class, () -> new MultidimensionalCounter(-1, 1));
    }

    @Test
    public void constructorPositiveSizeTest() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(2 * 3 * 4, counter.getSize());
    }

    // Repeat tests for getDimension(), getSizes(), getCounts(), getCount(), and iterator() methods with different names.

    @Test
    public void testToString() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        String expected = "[0][1][1]";
        String actual = counter.toString();
        assertEquals(expected, actual);
    }
}