import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import ipac.BinaryCalculate;

class BinaryCalculateTest {

    @Test
    public void testAddBinary_ValidInput() {
        // ...
    }

    @Test
    public void testAddBinary_BothInputsAreZero() {
        // ...
    }

    @Test
    public void testAddBinary_FirstInputIsLonger() {
        // ...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinary_InvalidInput1() {
        // ...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinary_InvalidInput2() {
        // ...
    }

    @Test
    public void testSubBinary_ValidInput() {
        // ...
    }

    @Test
    public void testSubBinary_FirstInputIsLongerAndBothInputsAreZero() {
        // ...
    }

    @Test
    public void testSubBinary_BothInputsAreZero() {
        // ...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubBinary_InvalidInput1() {
        // ...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubBinary_InvalidInput2() {
        // ...
    }

    @Test
    public void testIPCalculate_ValidInput() {
        // ...
    }

    @Test
    public void testIPCalculate_InvalidIPAddress() {
        // ...
    }

    @Test
    public void testIPCalculate_IPPrefixLongerThan32() {
        // ...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculate_InvalidIPPrefix() {
        // ...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculate_InvalidIPAddressLength() {
        // ...
    }
}