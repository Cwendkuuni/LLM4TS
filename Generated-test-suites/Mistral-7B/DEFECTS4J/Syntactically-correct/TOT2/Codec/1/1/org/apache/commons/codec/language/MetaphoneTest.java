import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Metaphone;
import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphoneEncodeBasic() {
        Metaphone metaphoneEncoder = new Metaphone();
        String input = "Apple";
        String expectedOutput = "A0P0L2E0";
        String actualOutput = metaphoneEncoder.encode(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMetaphoneEncodeNullInput() {
        Metaphone metaphoneEncoder = new Metaphone();
        String input = null;
        metaphoneEncoder.encode(input);
    }

    @Test(expected = DecoderException.class)
    public void testMetaphoneEncodeNonStringInput() {
        Metaphone metaphoneEncoder = new Metaphone();
        Object input = new Object();
        metaphoneEncoder.encode(input);
    }
}