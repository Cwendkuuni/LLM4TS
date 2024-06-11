import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    private static final String TEST_STRING_1 = "The quick brown fox jumps over the lazy dog";
    private static final String TEST_STRING_2 = "The quick brown fox jumps over the lazy cat";
    private static final String TEST_STRING_3 = "";
    private static final String TEST_STRING_4 = "Caverphone Test";
    private static final String TEST_STRING_5 = "123456789012345678901234567890"; // valid caverphone string with 13 digits, longer than the expected 10 digits

    @Test
    public void testCaverphoneEncodeAndEqual() throws EncoderException, DecoderException {
        StringEncoder caverphone = new Caverphone();

        String caverphoneCode1 = caverphone.encode(TEST_STRING_1);
        String caverphoneCode2 = caverphone.encode(TEST_STRING_2);

        Assert.assertEquals("Encoded strings should be equal", caverphoneCode1, caverphoneCode2);
        Assert.assertFalse("Encoded strings should not be equal to the original strings", caverphoneCode1.equals(TEST_STRING_1));
        Assert.assertFalse("Encoded strings should not be equal to the original strings", caverphoneCode2.equals(TEST_STRING_2));
    }

    @Test
    public void testCaverphoneEncodeNull() {
        StringEncoder caverphone = new Caverphone();

        try {
            caverphone.encode(null);
            Assert.fail("Should have thrown an EncoderException for null input");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testCaverphoneEncodeEmpty() throws EncoderException {
        StringEncoder caverphone = new Caverphone();

        String caverphoneCode = caverphone.encode("");

        Assert.assertEquals("Empty string should be encoded to a specific string", "1111111111", caverphoneCode);
    }

    @Test
    public void testCaverphoneEncodeInvalid() throws EncoderException {
        StringEncoder caverphone = new Caverphone();

        try {
            caverphone.encode("Invalid string");
            Assert.fail("Should have thrown an EncoderException for invalid input");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testCaverphoneIsEqual() {
        StringEncoder caverphone = new Caverphone();

        Assert.assertTrue("Two equal strings should have identical caverphones", caverphone.isCaverphoneEqual(TEST_STRING_1, TEST_STRING_2));
        Assert.assertFalse("Two different strings should not have identical caverphones", caverphone.isCaverphoneEqual(TEST_STRING_1, TEST_STRING_3));
        Assert.assertFalse("Two different strings should not have identical caverphones", caverphone.isCaverphoneEqual(TEST_STRING_1, TEST_STRING_4));
        Assert.assertFalse("Two different strings should not have identical caverphones", caverphone.isCaverphoneEqual(TEST_STRING_1, TEST_STRING_5));
    }

    @Test
    public void testCaverphoneIsEqualNull() {
        StringEncoder caverphone = new Caverphone();

        try {
            caverphone.isCaverphoneEqual(null, TEST_STRING_1);
            Assert.fail("Should have thrown an IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            caverphone.isCaverphoneEqual(TEST_STRING_1, null);
            Assert.fail("Should have thrown an IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCaverphoneIsEqualEmpty() {
        StringEncoder caverphone = new Caverphone();

        Assert.assertFalse("An empty string and a non-empty string should not have identical caverphones", caverphone.isCaverphoneEqual("", TEST_STRING_1));
        Assert.assertFalse("A non-empty string and an empty string should not have identical caverphones", caverphone.isCaverphoneEqual(TEST_STRING_1, ""));
    }
}