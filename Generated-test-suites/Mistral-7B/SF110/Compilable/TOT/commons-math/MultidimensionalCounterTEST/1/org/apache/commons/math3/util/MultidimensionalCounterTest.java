package org.apache.commons.math3.util;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3 * 4 * 2, counter.getSize());
    }
}