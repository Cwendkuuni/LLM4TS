import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEncodesEmptyString() {
        Caverphone caverphone = new Caverphone();
        String encoded = caverphone.encode("");
        Assert.assertEquals("1111111111", encoded);
    }

    @Test
    public void testCaverphoneEncodesNullString() {
        Caverphone caverphone = new Caverphone();
        try {
            String encoded = caverphone.encode(null);
            Assert.fail();
        } catch (EncoderException e) {
            Assert.assertEquals("Parameter supplied to Caverphone encode is not of type java.lang.String", e.getMessage());
        }
    }

    @Test
    public void testCaverphoneEncodesLowerCaseLetters() {
        Caverphone caverphone = new Caverphone();
        String encoded = caverphone.encode("hello");
        String expected = "h2l2o";
        Assert.assertEquals(expected, encoded);
    }

    @Test
    public void testCaverphoneEncodesUpperCaseLetters() {
        Caverphone caverphone = new Caverphone();
        String encoded = caverphone.encode("HELLO");
        String expected = "h2l2o";
        Assert.assertEquals(expected, encoded);
    }

    @Test
    public void testCaverphoneEncodesStringWithDigits() {
        Caverphone caverphone = new Caverphone();
        String encoded = caverphone.encode("hello123");
        String expected = "h2l2o123";
        Assert.assertEquals(expected, encoded);
    }

    @Test
    public void testCaverphoneEncodesStringWithSpecialCharacters() {
        Caverphone caverphone = new Caverphone();
        String encoded = caverphone.encode("hello@world");
        String expected = "h2l2oAw";
        Assert.assertEquals(expected, encoded);
    }

    @Test
    public void testCaverphoneEncodesStartOptions() {
        Caverphone caverphone = new Caverphone();
        String encoded = caverphone.encode("cough");
        String expected = "cou2f";
        Assert.assertEquals(expected, encoded);
    }

    @Test
    public void testCaverphoneEncodesReplacements() {
        Caverphone caverphone = new Caverphone();
        String encoded = caverphone.encode("cyclone");
        String expected = "syklo3n";
        Assert.assertEquals(expected, encoded);
    }

    @Test
    public void testCaverphoneEncodesRemovals() {
        Caverphone caverphone = new Caverphone();
        String encoded = caverphone.encode("thequickbrownfoxjumpsoverthelazydog");
        String expected = "tkbfp3oxp3rd";
        Assert.assertEquals(expected, encoded);
    }

    @Test
    public void testCaverphoneEncodesHandlesMultipleReplacements() {
        Caverphone caverphone = new Caverphone();
        String encoded = caverphone.encode("cyclonejump");
        String expected = "sykjmp";
        Assert.assertEquals(expected, encoded);
    }

    @Test
    public void testCaverphoneDecodesEmptyString() {
        Caverphone caverphone = new Caverphone();
        StringDecoder decoder = new StringDecoder(caverphone.encode(""));
        String decoded = decoder.decode("");
        Assert.assertEquals("1111111111", decoded);
    }

    @Test
    public void testCaverphoneDecodesNullString() {
        StringDecoder decoder = new StringDecoder(new Caverphone().encode(null));
        try {
            decoder.decode(null);
            Assert.fail();
        } catch (DecoderException e) {
            Assert.assertEquals("Null byte not allowed in input", e.getMessage());
        }
    }

    @Test
    public void testCaverphoneDecodesStringWithOnlyOnes() {
        Caverphone caverphone = new Caverphone();
        String encoded = caverphone.encode("1111111111");
        StringDecoder decoder = new StringDecoder(encoded);
        String decoded = decoder.decode("");
        Assert.assertEquals("", decoded);
    }

    @Test
    public void testCaverphoneDecodesStringWithOnlyZeros() {
        Caverphone caverphone = new Caverphone();
        String encoded = caverphone.encode("0000000000");
        StringDecoder decoder = new StringDecoder(encoded);
        String decoded = decoder.decode("");
        Assert.assertEquals("", decoded);
    }

    @Test
    public void testCaverphoneDecodesSimpleString() {
        Caverphone caverphone = new Caverphone();
        String encoded = caverphone.encode("hello");
        StringDecoder decoder = new StringDecoder(encoded);
        String decoded = decoder.decode("");
        Assert.assertEquals("hello", decoded);
    }

    @Test
    public void testCaverphoneDecodesStringWithLongCode() {
        Caverphone caverphone = new Caverphone();
        String encoded = caverphone.encode("thequickbrownfoxjumpsoverthelazydog");
        StringDecoder decoder = new StringDecoder(encoded);
        String decoded = decoder.decode("");
        Assert.assertEquals("thequickbrownfoxjumpsoverthelazydog", decoded);
    }

    @Test
    public void testCaverphoneDecodesStringWithMixedCode() {
        Caverphone caverphone = new Caverphone();
        String encoded = caverphone.encode("thequickbrownfoxjumpsoverthelazydog12345");
        StringDecoder decoder = new StringDecoder(encoded);
        String decoded = decoder.decode("");
        Assert.assertEquals("thequickbrownfoxjumpsoverthelazydog12345", decoded);
    }
}