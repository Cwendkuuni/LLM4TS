import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmptyString() {
        Caverphone caverphone = new Caverphone();
        String expected = "1111111111";
        String actual = caverphone.caverphone("");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCaverphoneLowercaseString() {
        Caverphone caverphone = new Caverphone();
        String input = "hello";
        String expected = "h3l3l3o";
        String actual = caverphone.caverphone(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCaverphoneNoLettersString() {
        Caverphone caverphone = new Caverphone();
        String input = "12345";
        String expected = "111111";
        String actual = caverphone.caverphone(input);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneNullInput() {
        Caverphone caverphone = new Caverphone();
        String input = null;
        caverphone.encode(input);
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneNonStringInput() {
        Caverphone caverphone = new Caverphone();
        Object input = new Object();
        caverphone.encode(input);
    }
}