import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class CaverphoneTest {

    private static final String[] TEST_CASES = {
            "The quick brown fox jumps over the lazy dog",
            "hello world",
            "1A2b3C4D5e6F",
            "12345"
    };

    @Test
    public void testEncodeAndDecode() throws Exception {
        for (String testCase : TEST_CASES) {
            StringEncoder encoder = new Caverphone();
            String encoded = encoder.encode(testCase);
            String decoded = ((StringEncoder) encoder).decode(encoded);
            Assert.assertEquals(testCase, decoded);
        }
    }

    @Test
    public void testEncodeEmptyString() {
        StringEncoder encoder = new Caverphone();
        Assert.assertEquals("1111111111", encoder.encode(""));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeNullString() {
        StringEncoder encoder = new Caverphone();
        encoder.encode(null);
    }

    @Test
    public void testDecodeInvalidHex() {
        String encoded = "5A3J3K4L5M6N7O7P7Q7R7S7T7U7V7W7X7Y7Z7";
        StringEncoder encoder = new Caverphone();
        try {
            encoder.decode(encoded);
            Assert.fail("Expected DecoderException");
        } catch (DecoderException e) {
            // Expected
        }
    }

    @Test
    public void testDecodeEmptyString() {
        String encoded = "";
        StringEncoder encoder = new Caverphone();
        try {
            encoder.decode(encoded);
            Assert.fail("Expected DecoderException");
        } catch (DecoderException e) {
            // Expected
        }
    }

    @Test
    public void testEncodeAndDecodeSpecialCharacters() throws Exception {
        for (String testCase : TEST_CASES) {
            String encoded = new StringEncoder() {
                @Override
                public String encode(String string) {
                    return Hex.encodeHexString(StringUtils.getBytesUtf8(caverphone(string)));
                }
            }.encode(testCase);
            String decoded = ((StringEncoder) new Caverphone()).decode(encoded);
            Assert.assertEquals(testCase, decoded);
        }
    }

    @Test
    public void testEncodeEmptyArray() {
        StringEncoder encoder = new Caverphone();
        String[] testCases = {};
        for (String testCase : testCases) {
            String encoded = encoder.encode(testCase);
            Assert.fail("Expected empty array");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeNullArray() {
        StringEncoder encoder = new Caverphone();
        String[] testCases = null;
        for (String testCase : testCases) {
            encoder.encode(testCase);
        }
    }

    private String caverphone(String txt) {
        Caverphone caverphone = new Caverphone();
        return caverphone.caverphone(txt);
    }

    private void printCaverphone(String txt) {
        StringEncoder encoder = new Caverphone();
        String encoded = encoder.encode(txt);
        String decoded = ((StringEncoder) encoder).decode(encoded);
        System.out.printf("Input: %s%n", txt);
        System.out.printf("Caverphone: %s%n", encoded);
        System.out.printf("Decoded: %s%n", decoded);
        System.out.println();
    }
}