import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;

public class DfpDecTest {

    @Test
    public void testDfpDecConstructorWithDfp() {
        Dfp d = new DfpDec(2.0);
        DfpDec dfpDec = new DfpDec(d);
        assertEquals(d, dfpDec);
    }
}