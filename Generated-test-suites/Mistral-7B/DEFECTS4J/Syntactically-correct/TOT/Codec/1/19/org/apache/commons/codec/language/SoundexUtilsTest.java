import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the SoundexUtils class.
 */
public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Typical use case
        String input = "The quick Brown fox jumps over the lazy dog";
        String expected = "The quick Brown fox jumps over the lazy dog";
        String result = SoundexUtils.clean(input);
        Assert.assertEquals(expected, result);

        // Edge case: empty string
        String empty = "";
        expected = "";
        result = SoundexUtils.clean(empty);
        Assert.assertEquals(expected, result);

        // Edge case: null input
        String nullInput = null;
        Assert.assertNull(SoundexUtils.clean(nullInput));

        // Error case: null character in input
        String errorInput = "The quick Brown fox jumps over the λazy dog";
        Assert.assertThrows(NullPointerException.class, () -> SoundexUtils.clean(errorInput));
    }

    @Test
    public void testDifference() throws EncoderException {
        // Typical use case
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = "apple";
        String s2 = "appel";
        int expected = 3;
        int result = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expected, result);

        // Edge case: same string
        s1 = "apple";
        s2 = "apple";
        expected = 4;
        result = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expected, result);

        // Edge case: null input for one or both strings
        String s3 = null;
        Assert.assertThrows(NullPointerException.class, () -> SoundexUtils.difference(encoder, s1, s3));
        Assert.assertThrows(NullPointerException.class, () -> SoundexUtils.difference(encoder, s3, s1));

        // Error case: incorrect encoder
        StringEncoder invalidEncoder = new org.apache.commons.codec.StringEncoder() {
            @Override
            public String encode(String string) throws EncoderException {
                throw new EncoderException("Invalid encoder");
            }
        };
        String s4 = "test";
        String s5 = "test";
        Assert.assertThrows(EncoderException.class, () -> SoundexUtils.difference(invalidEncoder, s4, s5));
    }

    @Test
    public void testDifferenceEncoded() {
        // Typical use case
        String es1 = "122000122000";
        String es2 = "122000132000";
        int expected = 3;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expected, result);

        // Edge case: same encoded strings
        es1 = "122000122000";
        es2 = "122000122000";
        expected = 4;
        result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expected, result);

        // Edge case: null input for one or both encoded strings
        String es3 = null;
        Assert.assertNull(SoundexUtils.differenceEncoded(es1, es3));
        Assert.assertNull(SoundexUtils.differenceEncoded(es3, es1));

        // Error case: incorrect encoded strings
        String es4 = "122000X22000";
        String es5 = "122000122000";
        Assert.assertThrows(IllegalArgumentException.class, () -> SoundexUtils.differenceEncoded(es4, es5));
    }
}