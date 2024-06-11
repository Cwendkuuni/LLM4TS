package ipac.test;

import static org.junit.Assert.*;
import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        // Test case for addBinary() method
    }

    @Test
    public void testSubBinary() {
        // Test case for subBinary() method
    }

    // Test cases for other methods...

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculateWithInvalidIP() {
        // Test case for IPCalculate() method with invalid IP
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculateWithInvalidPrefix() {
        // Test case for IPCalculate() method with invalid prefix
    }

    @Test
    public void testIPCalculate() {
        // Test case for IPCalculate() method with valid IP and prefix
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        // Test case for prefixInPrefixCalculate() method
    }

    // More test cases...
}