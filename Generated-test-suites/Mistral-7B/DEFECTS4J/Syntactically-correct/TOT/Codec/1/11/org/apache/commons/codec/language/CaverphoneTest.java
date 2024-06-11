import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmptyString() {
        Caverphone encoder = new Caverphone();
        String expected = "1111111111";
        String result = encoder.encode("");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneLowerCase() {
        Caverphone encoder = new Caverphone();
        String input = "hello world";
        String expected = "h2l2o2w2o2ld";
        String result = encoder.encode(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneSpecialCharacters() {
        Caverphone encoder = new Caverphone();
        String input = "Hello World@123!";
        String expected = "H2l2o2w2o2ld3!";
        String result = encoder.encode(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneStartOptions() {
        Caverphone encoder = new Caverphone();
        String input1 = "Cough";
        String expected1 = "cou2f";
        String result1 = encoder.encode(input1);
        Assert.assertEquals(expected1, result1);

        String input2 = "Rough";
        String expected2 = "rou2f";
        String result2 = encoder.encode(input2);
        Assert.assertEquals(expected2, result2);
    }

    @Test
    public void testCaverphoneReplacements() {
        Caverphone encoder = new Caverphone();
        String input = "cat in the hat";
        String expected = "kat int he hat";
        String result = encoder.encode(input);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneNullInput() {
        Caverphone encoder = new Caverphone();
        String input = null;
        encoder.encode(input);
    }

    @Test
    public void testCaverphoneDecode() {
        Caverphone encoder = new Caverphone();
        String encoded = encoder.encode("hello world");
        StringDecoder decoder = new StringDecoder();
        String decoded = decoder.decode(encoded);
        Assert.assertEquals("hello world", decoded);
    }

    @Test
    public void testCaverphoneIsEqual() {
        Caverphone encoder = new Caverphone();
        String str1 = "hello world";
        String str2 = "hello world";
        boolean expected = true;
        boolean result = encoder.isCaverphoneEqual(str1, str2);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneIsNotEqual() {
        Caverphone encoder = new Caverphone();
        String str1 = "hello world";
        String str2 = "hello world1";
        boolean expected = false;
        boolean result = encoder.isCaverphoneEqual(str1, str2);
        Assert.assertEquals(expected, result);
    }
}