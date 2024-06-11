import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {
    @Test
    public void testGetDecimalDigits() {
        DfpField field = new DfpField(16); // 4 decimal digits
        DfpDec dfp = new DfpDec(field);
        int decimalDigits = dfp.getDecimalDigits();
        assertEquals(4, decimalDigits);
    }
}