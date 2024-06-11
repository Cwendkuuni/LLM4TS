import org.junit.Test;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.EncoderException;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

    @Test
    public void testCaverphone() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        String input = "ExampleString";
        String expectedOutput = "E2xAmPlE3xAmPl3";
        String output = caverphone.caverphone(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = EncoderException.class)
    public void testNullInput() {
        Caverphone caverphone = new Caverphone();
        String input = null;
        caverphone.caverphone(input);
    }

    @Test(expected = EncoderException.class)
    public void testEmptyInput() {
        Caverphone caverphone = new Caverphone();
        String input = "";
        caverphone.caverphone(input);
    }
}