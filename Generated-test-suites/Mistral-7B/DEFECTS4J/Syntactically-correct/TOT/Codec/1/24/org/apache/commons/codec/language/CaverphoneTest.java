import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testEncodeAndDecode() throws Exception {
        Caverphone encoder = new Caverphone();
        String original = "the quick brown fox jumps over the lazy dog";
        String encoded = encoder.encode(original);
        StringDecoder decoder = new StringDecoder();
        String decoded = decoder.decode(encoded, Caverphone.ALGORITHM_NAME);
        Assert.assertEquals(original, decoded);
    }

    @Test
    public void testEncodeEmptyString() {
        Caverphone encoder = new Caverphone();
        String original = "";
        String encoded = encoder.encode(original);
        Assert.assertEquals("1111111111", encoded);
    }

    @Test
    public void testEncodeNull() {
        Caverphone encoder = new Caverphone();
        String original = null;
        try {
            String encoded = encoder.encode(original);
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testEncodeSpecialCharacters() {
        Caverphone encoder = new Caverphone();
        String original = "!@#$%^&*()_+={}[];:\"'<>,.?/~`";
        String encoded = encoder.encode(original);
        Assert.assertEquals("22222222222222222222222222", encoded);
    }

    @Test
    public void testEncodeUpperCase() {
        Caverphone encoder = new Caverphone();
        String original = "HELLO WORLD";
        String encoded = encoder.encode(original);
        Assert.assertEquals("H2L2O3 W2R2L2D2", encoded);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidInput() {
        Caverphone encoder = new Caverphone();
        String original = "12345";
        encoder.encode(original);
        // the test should throw an EncoderException
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone encoder = new Caverphone();
        String str1 = "the quick brown fox jumps over the lazy dog";
        String str2 = "The quick brown fox jumps over the lazy dog";
        boolean result = encoder.isCaverphoneEqual(str1, str2);
        Assert.assertTrue(result);

        String str3 = "the quick brown fox jumps over the lazy cat";
        boolean result2 = encoder.isCaverphoneEqual(str1, str3);
        Assert.assertFalse(result2);

        String str4 = "the quick brown fox jumps over the lazy dog";
        String str5 = "";
        boolean result3 = encoder.isCaverphoneEqual(str4, str5);
        Assert.assertFalse(result3);
    }
}