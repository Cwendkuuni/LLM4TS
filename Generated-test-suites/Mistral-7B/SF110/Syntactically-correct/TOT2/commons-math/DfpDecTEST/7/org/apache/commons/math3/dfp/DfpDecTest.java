import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {
    @Test
    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec(new DfpField(10));
        int decimalDigits = dfpDec.getDecimalDigits();
        assertEquals(40, decimalDigits);

        DfpDec dfpDec2 = new DfpDec(new DfpField(5));
        decimalDigits = dfpDec2.getDecimalDigits();
        assertEquals(20, decimalDigits);
    }
}