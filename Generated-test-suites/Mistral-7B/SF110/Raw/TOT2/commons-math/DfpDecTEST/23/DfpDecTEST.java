import org.apache.commons.math3.dfp.DfpDec;
import org.junit.Test;

public class DfpDecTest {
    // Test starts here

    @Test
    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec(new DfpField(32));
        int decimalDigits = dfpDec.getDecimalDigits();
        int expectedDecimalDigits = 3 * 4 - 3;
        assertEquals("Decimal digits are not correct.", expectedDecimalDigits, decimalDigits);
    }

    // Test ends here
}