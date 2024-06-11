import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.junit.Assert;
import org.junit.Test;

public class DfpDecTest {
    @Test
    public void testDfpDecConstructor() {
        DfpField factory = new DfpField(32, 18, java.util.RoundingMode.HALF_UP);
        DfpDec dfpDec = new DfpDec(factory);
        Assert.assertEquals(Dfp.ONE, dfpDec.newInstance());
    }
}