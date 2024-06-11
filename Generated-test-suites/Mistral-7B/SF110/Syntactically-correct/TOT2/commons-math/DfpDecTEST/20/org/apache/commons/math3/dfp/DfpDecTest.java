import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.junit.Test;

public class DfpDecTest {
    @Test
    public void testConstructorWithDfpArgument() {
        Dfp dfp = new DfpDec(new DfpField(10), 123.456);
        DfpDec dfpDec = new DfpDec(dfp);
        // Test the functionality of the new instance
        assertEquals("123.456", dfpDec.toString());
        // Test edge cases and exception handling scenarios
        try {
            new DfpDec(new DfpField(10), null);
            fail("Expected IllegalArgumentException when passed a null Dfp object.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            new DfpDec(new DfpField(10), new DfpDec(new DfpField(10), 1));
            fail("Expected IllegalArgumentException when passed a DfpDec object as an argument.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}