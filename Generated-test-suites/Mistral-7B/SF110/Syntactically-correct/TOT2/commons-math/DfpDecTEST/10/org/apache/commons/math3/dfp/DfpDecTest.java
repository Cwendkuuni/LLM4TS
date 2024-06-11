import org.apache.commons.math3.dfp.DfpDec;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DfpDecTest {
    @Test
    public void testEmptyConstructor() {
        DfpDec dfpDec = new DfpDec(DfpField.getInstance(10));
        assertEquals(Dfp.ZERO, dfpDec);
    }
}