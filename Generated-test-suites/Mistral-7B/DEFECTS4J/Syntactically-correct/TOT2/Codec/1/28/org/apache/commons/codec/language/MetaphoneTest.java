import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Metaphone;
import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphoneBasic() throws EncoderException, DecoderException {
        Metaphone metaphone = new Metaphone();
        String original = "apple";
        String metaphoneCode = metaphone.metaphone(original);
        String expected = "A0P0L1E0";
        assertEquals(expected, metaphoneCode);
    }

    @Test
    public void testMetaphoneEdgeCases() throws EncoderException, DecoderException {
        Metaphone metaphone = new Metaphone();

        String testCases[] = {
                new String[]{"kat", "K1T0"},
                new String[]{"banana", "A0P1N1N1A0"},
                new String[]{"schedule", "S0X3C1H1D2L2E2"},
                new String[]{"pseudopseudopseudopseudo", "P0H0W0S0X0D0P0S0P0S0P0S0X0D0P0S0"}
        };

        for (String[] testCase : testCases) {
            String original = testCase[0];
            String expected = testCase[1];
            String metaphoneCode = metaphone.metaphone(original);
            assertEquals(expected, metaphoneCode);
        }
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneException() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        String input = "123";
        metaphone.metaphone(input);
    }
}