import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;

public class DfpDecTest {

    @Test
    public void testRound() {
        DfpField factory = new DfpField(16);
        DfpDec dfpDec = new DfpDec(factory, 123.456789);
        int result = dfpDec.round(2);
        assertEquals(0, result);

        DfpDec dfpDec2 = new DfpDec(factory, 123.556789);
        int result2 = dfpDec2.round(2);
        assertEquals(1, result2);
    }
}