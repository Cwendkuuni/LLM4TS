package org.apache.commons.math3.dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.junit.Test;

public class DfpDecTest {
    // 

    @Test
    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec(new DfpField(4));
        int decimalDigits = dfpDec.getDecimalDigits();
        int expected = 15; // 4 radix digits * 4 = 16, but subtract 3 since getDecimalDigits() returns the number of decimal digits
        assert(decimalDigits == expected);
    }

    // 
}