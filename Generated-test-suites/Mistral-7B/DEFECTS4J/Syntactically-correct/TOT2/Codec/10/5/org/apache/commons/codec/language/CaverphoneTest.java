import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneConstructor() {
        new Caverphone();
    }

    @Test
    public void testCaverphoneEmptyString(Caverphone caverphone) {
        Assert.assertEquals("1111111111", caverphone.caverphone(""));
    }

    @Test
    public void testCaverphoneBasicString(Caverphone caverphone) {
        Assert.assertEquals("52535325353", caverphone.caverphone("hello"));
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneNullString(Caverphone caverphone) {
        caverphone.encode(null);
    }

    @Test
    public void testCaverphoneEncodeString(Caverphone caverphone) {
        Assert.assertEquals("52535325353", caverphone.encode("hello"));
    }

    @Test
    public void testCaverphoneIsCaverphoneEqualTrue() {
        Caverphone caverphone = new Caverphone();
        Assert.assertTrue(caverphone.isCaverphoneEqual("hello", "hello"));
    }

    @Test
    public void testCaverphoneIsCaverphoneEqualFalse() {
        Caverphone caverphone = new Caverphone();
        Assert.assertFalse(caverphone.isCaverphoneEqual("hello", "world"));
    }
}