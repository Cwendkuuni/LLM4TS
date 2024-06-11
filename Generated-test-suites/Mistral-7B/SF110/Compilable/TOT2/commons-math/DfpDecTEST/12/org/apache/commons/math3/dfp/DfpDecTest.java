package org.apache.commons.math3.dfp;
// GetDecimalDigitsTest.java
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {
    @Test
    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec(new DfpField(4));
        int decimalDigits = dfpDec.getDecimalDigits();
        assertEquals(15, decimalDigits); // Test with radixDigits = 4

        dfpDec = new DfpDec(new DfpField(3));
        decimalDigits = dfpDec.getDecimalDigits();
        assertEquals(11, decimalDigits); // Test with radixDigits = 3
    }
}