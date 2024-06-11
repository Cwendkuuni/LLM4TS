import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Caverphone implementation.
 */
public class CaverphoneTest {

    /**
     * Test for empty string.
     */
    @Test
    public void testEmptyString() {
        StringEncoder encoder = new Caverphone();
        Assert.assertEquals("1111111111", encoder.encode(""));
    }

    /**
     * Test for null string.
     */
    @Test(expected = NullPointerException.class)
    public void testNullString() {
        StringEncoder encoder = new Caverphone();
        encoder.encode(null);
    }

    /**
     * Test for simple string.
     */
    @Test
    public void testSimpleString() {
        StringEncoder encoder = new Caverphone();
        Assert.assertEquals("AETKLK2M2S2T3F11", encoder.encode("hello world"));
    }

    /**
     * Test for edge case: string with only one letter.
     */
    @Test
    public void testSingleLetterString() {
        StringEncoder encoder = new Caverphone();
        Assert.assertEquals("A", encoder.encode("a"));
    }

    /**
     * Test for edge case: string with only digits.
     */
    @Test
    public void testDigitString() {
        StringEncoder encoder = new Caverphone();
        Assert.assertEquals("1111111111", encoder.encode("1234567890"));
    }

    /**
     * Test for edge case: string with non-alphanumeric characters.
     */
    @Test
    public void testNonAlphanumericString() {
        StringEncoder encoder = new Caverphone();
        Assert.assertEquals("1111111111", encoder.encode("!@#$%^&*()_+={}[]|\\:\"<>?"));
    }

    /**
     * Test for edge case: string with long length.
     */
    @Test
    public void testLongString() {
        StringEncoder encoder = new Caverphone();
        String longString = "This is a very long string to test the Caverphone encoder with a string " +
                "that has a longer length than what is usually expected in real-world scenarios.";
        Assert.assertEquals(generateExpectedCaverphoneValue(longString), encoder.encode(longString));
    }

    /**
     * Test for error case: empty string input to isCaverphoneEqual method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsCaverphoneEqualEmptyString() {
        StringEncoder encoder = new Caverphone();
        encoder.isCaverphoneEqual("", "");
    }

    /**
     * Test for error case: null string input to isCaverphoneEqual method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsCaverphoneEqualNullString() {
        StringEncoder encoder = new Caverphone();
        encoder.isCaverphoneEqual(null, "test");
    }

    /**
     * Test for error case: different string lengths input to isCaverphoneEqual method.
     */
    @Test
    public void testIsCaverphoneEqualDifferentStringLengths() {
        StringEncoder encoder = new Caverphone();
        Assert.assertFalse(encoder.isCaverphoneEqual("test", "testlong"));
    }

    /**
     * Test for error case: different caverphone values.
     */
    @Test
    public void testIsCaverphoneEqualDifferentCaverphoneValues() {
        StringEncoder encoder = new Caverphone();
        Assert.assertFalse(encoder.isCaverphoneEqual("test", "anothertest"));
    }

    /**
     * Test for error case: unsupported object type input to encode method.
     */
    @Test(expected = EncoderException.class)
    public void testEncodeUnsupportedObjectType() {
        StringEncoder encoder = new Caverphone();
        encoder.encode(new Object());
    }

    /**
     * Test for error case: unsupported object type input to isCaverphoneEqual method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsCaverphoneEqualUnsupportedObjectType() {
        StringEncoder encoder = new Caverphone();
        encoder.isCaverphoneEqual(new Object(), "test");
    }

    /**
     * Helper method to generate expected caverphone value for a given string.
     *
     * @param str Input string
     * @return Expected caverphone value
     */
    private String generateExpectedCaverphoneValue(String str) {
        String caverphoneValue = str.toLowerCase()
                .replaceAll("[^a-z]", "")
                .replaceAll("(^[aeiou])|([aeiou]$)", "A")
                .replaceAll("(cq|ci|ce|cy|tch|c|q|x|v|dg|tio|tia|d|ph|b|sh|z|j|y|3gh3|gh|g|s+|t+|p+|k+|f+|m+|n+|w3|wh3|w|h|r3|r|l3|l|j|y3|y|3)", m -> m.length() > 0 ? "2" : "")
                .replaceAll("^", "A")
                .replaceAll("", "")
                .replaceAll("(3|$)", "A")
                .substring(0, 10)
                + "111111" + "1111";
        return caverphoneValue;
    }

    /**
     * Test for error case: invalid hexadecimal representation of caverphone value.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeInvalidHexadecimal() {
        StringEncoder encoder = new Caverphone();
        encoder.decodeHex("0xabcd12ef13gh3");
    }

    /**
     * Test for error case: invalid caverphone value.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeInvalidCaverphoneValue() {
        StringEncoder encoder = new Caverphone();
        encoder.decode("invalidCaverphoneValue");
    }

    /**
     * Test for valid caverphone value.
     */
    @Test
    public void testDecodeValidCaverphoneValue() {
        StringEncoder encoder = new Caverphone();
        String expected = "hello world";
        String caverphoneValue = "AETKLK2M2S2T3F11";
        Assert.assertEquals(expected, encoder.decode(Hex.encodeHexString(caverphoneValue.getBytes())));
    }
}