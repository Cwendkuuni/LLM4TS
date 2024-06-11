package org.apache.commons.math3.util;
import org.junit.Test;
import org.apache.commons.math3.util.MultidimensionalCounter;
import static org.junit.Assert.assertEquals;

public class MultidimensionalCounterTest {
    
    @Test
    public void testConstructorValidDimensions() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(144, counter.getSize());
    }
    
    // Other test cases for the methods and edge cases will follow
}