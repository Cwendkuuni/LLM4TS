import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Assert;
import org.junit.Test;

/**
 * Caverphone test cases
 */
public class CaverphoneTest {

    /**
     * Test encodes empty string
     */
    @Test
    public void testEncodeEmptyString() {
        Caverphone caverphone = new Caverphone();
        String caverphoneCode = caverphone.caverphone("");
        String expected = "1111111111";
        Assert.assertEquals(expected, caverphoneCode);
    }

    /**
     * Test encodes lowercase letter
     */
    @Test
    public void testEncodeLowercaseLetter() {
        Caverphone caverphone = new Caverphone();
        String caverphoneCode = caverphone.caverphone("a");
        String expected = "A";
        Assert.assertEquals(expected, caverphoneCode.substring(0, 1));
    }

    /**
     * Test encodes multi-letter word
     */
    @Test
    public void testEncodeMultiLetterWord() {
        Caverphone caverphone = new Caverphone();
        String caverphoneCode = caverphone.caverphone("hello");
        String expected = "h2l2o";
        Assert.assertEquals(expected, caverphoneCode);
    }

    /**
     * Test encodes special characters
     */
    @Test(expected = EncoderException.class)
    public void testEncodeSpecialCharacter() {
        Caverphone caverphone = new Caverphone();
        String caverphoneCode = caverphone.caverphone("!@#$%^&*()_+={}[]|\\:\"<>,.?/~`");
    }

    /**
     * Test encodes uppercase letter
     */
    @Test
    public void testEncodeUppercaseLetter() {
        Caverphone caverphone = new Caverphone();
        String caverphoneCode = caverphone.caverphone("B");
        String expected = "2";
        Assert.assertEquals(expected, caverphoneCode.substring(0, 1));
    }

    /**
     * Test encodes word with special start options
     */
    @Test
    public void testEncodeWordWithSpecialStartOptions() {
        Caverphone caverphone = new Caverphone();
        String caverphoneCode = caverphone.caverphone("coughenough");
        String expected = "cou2fenou2f";
        Assert.assertEquals(expected, caverphoneCode);
    }

    /**
     * Test decodes empty string
     */
    @Test
    public void testDecodeEmptyString() {
        Caverphone caverphone = new Caverphone();
        StringDecoder decoder = new StringDecoder(caverphone.encode(""));
        String decoded = decoder.decode("");
        String expected = "";
        Assert.assertEquals(expected, decoded);
    }

    /**
     * Test decodes invalid string
     */
    @Test(expected = DecoderException.class)
    public void testDecodeInvalidString() {
        Caverphone caverphone = new Caverphone();
        StringDecoder decoder = new StringDecoder(caverphone.encode("xyz"));
        String decoded = decoder.decode("");
    }

    /**
     * Test decodes string with single letter
     */
    @Test
    public void testDecodeSingleLetter() {
        Caverphone caverphone = new Caverphone();
        StringEncoder encoder = new Caverphone();
        String encoded = encoder.encode("a");
        StringDecoder decoder = new StringDecoder(encoded);
        String decoded = decoder.decode("");
        String expected = "a";
        Assert.assertEquals(expected, decoded);
    }

    /**
     * Test decodes string with multiple letters
     */
    @Test
    public void testDecodeMultiLetterString() {
        Caverphone caverphone = new Caverphone();
        String encoded = caverphone.encode("hello");
        StringDecoder decoder = new StringDecoder(encoded);
        String decoded = decoder.decode("");
        String expected = "hello";
        Assert.assertEquals(expected, decoded);
    }

    /**
     * Test decodes string with errors
     */
    @Test(expected = DecoderException.class)
    public void testDecodeStringWithErrors() {
        Caverphone caverphone = new Caverphone();
        String encoded = caverphone.encode("xyz2gh3");
        StringDecoder decoder = new StringDecoder(encoded);
        decoder.decode("");
    }

    /**
     * Test equals method
     */
    @Test
    public void testEqualStrings() {
        Caverphone caverphone = new Caverphone();
        String str1 = "hello";
        String str2 = "hello";
        boolean result = caverphone.isCaverphoneEqual(str1, str2);
        boolean expected = true;
        Assert.assertEquals(expected, result);
    }

    /**
     * Test not equals method
     */
    @Test
    public void testNotEqualStrings() {
        Caverphone caverphone = new Caverphone();
        String str1 = "hello";
        String str2 = "world";
        boolean result = caverphone.isCaverphoneEqual(str1, str2);
        boolean expected = false;
        Assert.assertEquals(expected, result);
    }
}