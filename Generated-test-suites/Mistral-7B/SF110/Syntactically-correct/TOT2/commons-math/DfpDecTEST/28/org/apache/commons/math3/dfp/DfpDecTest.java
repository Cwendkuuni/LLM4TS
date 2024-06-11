import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {
    // Test case 1: Test with valid decimal digits
    @Test
    public void testGetDecimalDigits_Valid() {
        DfpDec dfpDec = new DfpDec(new DfpField(16));
        int decimalDigits = dfpDec.getDecimalDigits();
        assertEquals(64, decimalDigits);
    }

    // Test case 2: Test with negative decimal digits
    @Test(expected = IllegalArgumentException.class)
    public void testGetDecimalDigits_Negative() {
        DfpDec dfpDec = new DfpDec(new DfpField(16));
        dfpDec.getDecimalDigits(-1);
    }
}