import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class CaverphoneTest {

    private final StringEncoder encoder = new Caverphone();

    @Test
    public void testEmptyString() {
        String encoded = encoder.encode("");
        assertEquals("1111111111", encoded);
    }

    @Test
    public void testNullString() {
        try {
            encoder.encode(null);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testLowerCaseString() {
        String input = "hello world";
        String expected = "h3l2o2w2d2";
        String encoded = encoder.encode(input);
        assertEquals(expected, encoded);
    }

    @Test
    public void testStringWithDigits() {
        String input = "abc123def";
        String expected = "abcm2d2ef";
        String encoded = encoder.encode(input);
        assertEquals(expected, encoded);
    }

    @Test
    public void testStringWithSpecialChars() {
        String input = "ab@cd$ef!";
        String expected = "ab2cd2ef3";
        String encoded = encoder.encode(input);
        assertEquals(expected, encoded);
    }

    @Test
    public void testStringWithMultipleSpecialChars() {
        String input = "ab@cd$ef!@#";
        String expected = "ab2cd2ef3#";
        String encoded = encoder.encode(input);
        assertEquals(expected, encoded);
    }

    @Test
    public void testStringWithMultipleReplacements() {
        String input = "ceci tchatech";
        String expected = "sisi se2f2e2";
        String encoded = encoder.encode(input);
        assertEquals(expected, encoded);
    }

    @Test
    public void testStringWithMultipleReplacementsAndSpecialChars() {
        String input = "ceci tchatech@gmail.com";
        String expected = "sisi se2f2e2@gmail.m3";
        String encoded = encoder.encode(input);
        assertEquals(expected, encoded);
    }

    @Test
    public void testEncodeWithError() {
        try {
            encoder.encode(null);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testDecodeEmptyString() {
        try {
            String decoded = encoder.decode(encoder.encode(""));
            fail("Expected DecoderException");
        } catch (DecoderException e) {
            // Expected
        }
    }

    @Test
    public void testDecodeInvalidString() {
        try {
            String decoded = encoder.decode("invalidString");
            fail("Expected DecoderException");
        } catch (DecoderException e) {
            // Expected
        }
    }

    @Test
    public void testDecodeValidString() {
        String encoded = encoder.encode("hello world");
        String decoded = encoder.decode(encoded);
        assertEquals("hello world", decoded);
    }

    @Test
    public void testDecodeValidStringWithError() {
        try {
            String decoded = encoder.decode("1111111111111");
            fail("Expected DecoderException");
        } catch (DecoderException e) {
            // Expected
        }
    }

    @Test
    public void testIsCaverphoneEqualEmptyStrings() {
        assertTrue(encoder.isCaverphoneEqual("", ""));
    }

    @Test
    public void testIsCaverphoneEqualStringsWithDifferentLengths() {
        String str1 = "hello world";
        String str2 = "hello world!";
        assertFalse(encoder.isCaverphoneEqual(str1, str2));
    }

    @Test
    public void testIsCaverphoneEqualStringsWithSameEncoding() {
        String str1 = "hello world";
        String str2 = "hello world";
        assertTrue(encoder.isCaverphoneEqual(str1, str2));
    }

    @Test
    public void testIsCaverphoneEqualStringsWithDifferentEncodings() {
        String str1 = "hello world";
        String str2 = "hello world!";
        assertFalse(encoder.isCaverphoneEqual(str1, str2));
    }
}