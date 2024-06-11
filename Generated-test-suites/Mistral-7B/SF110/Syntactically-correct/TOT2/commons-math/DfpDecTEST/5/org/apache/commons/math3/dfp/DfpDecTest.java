import static org.junit.Assert.*;
import org.apache.commons.math3.dfp.*;

public class DfpDecTest {
    @Test
    public void testRound() {
        DfpDec dfpDec = new DfpDec(new DfpField(16)); // Initialize DfpDec with a specified radix
        int in = 5;
        int expectedRoundedValue = 5; // Rounding to the nearest
        int actualRoundedValue = dfpDec.round(in);
        assertEquals("Rounded value should be as expected.", expectedRoundedValue, actualRoundedValue);

        in = -5;
        expectedRoundedValue = 5; // Rounding towards zero
        actualRoundedValue = dfpDec.round(in);
        assertEquals("Rounded value should be as expected.", expectedRoundedValue, actualRoundedValue);

        in = 10;
        expectedRoundedValue = 10; // No need to round
        actualRoundedValue = dfpDec.round(in);
        assertEquals("Rounded value should be as expected.", expectedRoundedValue, actualRoundedValue);

        in = 15;
        expectedRoundedValue = 16; // Rounding up
        actualRoundedValue = dfpDec.round(in);
        assertEquals("Rounded value should be as expected.", expectedRoundedValue, actualRoundedValue);
    }
}