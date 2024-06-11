import org.junit.Test;
import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpDec;

public class DfpDecTest {

    @Test
    public void testRound() {
        DfpField factory = new DfpField(64);
        DfpDec dfpDec = new DfpDec(factory, 123.5);
        int expectedRoundResult = 1; // Rounding mode is set to ROUND_UP by default
        int actualRoundResult = dfpDec.round(expectedRoundResult);
        Dfp expectedRoundedDfp = new DfpDec(factory, 124.0);
        Dfp actualRoundedDfp = new DfpDec(dfpDec.getField(), actualRoundResult);
        assert(expectedRoundedDfp.equals(actualRoundedDfp));
    }
}