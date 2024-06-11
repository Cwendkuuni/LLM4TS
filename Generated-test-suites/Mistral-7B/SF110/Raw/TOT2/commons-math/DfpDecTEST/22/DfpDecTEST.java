import org.junit.Test;
import org.apache.commons.math3.dfp.DfpDec;

public class DfpDecTest {
    @Test
    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec(new DfpField(2));
        int decimalDigits = dfpDec.getDecimalDigits();
        int expected = 8; // 2 digits * 4 = 8 decimal digits
        assertEquals(expected, decimalDigits);
    }
}