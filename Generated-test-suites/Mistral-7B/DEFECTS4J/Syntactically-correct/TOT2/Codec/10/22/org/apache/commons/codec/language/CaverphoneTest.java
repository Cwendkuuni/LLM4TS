import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphoneConstructor() {
        Caverphone caverphone = new Caverphone();
        assertEquals("11111111111111", caverphone.caverphone(""));
    }

    @Test
    public void testCaverphoneBasic() {
        Caverphone caverphone = new Caverphone();
        String input = "HelloWorld";
        String expectedOutput = "H2l2o2w2r2d2l2o2l2o2A";
        String output = caverphone.caverphone(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        Object input = "HelloWorld";
        String expectedOutput = "H2l2o2w2r2d2l2o2l2o2A";
        String output = (String) caverphone.encode(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeNullInput() {
        Caverphone caverphone = new Caverphone();
        Object input = null;
        caverphone.encode(input);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeNonStringInput() {
        Caverphone caverphone = new Caverphone();
        Object input = new Object();
        caverphone.encode(input);
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        String str1 = "HelloWorld";
        String str2 = "HelloWorld";
        boolean expectedOutput = true;
        boolean output = caverphone.isCaverphoneEqual(str1, str2);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testIsCaverphoneEqualDifferentStrings() {
        Caverphone caverphone = new Caverphone();
        String str1 = "HelloWorld";
        String str2 = "WorldHello";
        boolean expectedOutput = false;
        boolean output = caverphone.isCaverphoneEqual(str1, str2);
        assertEquals(expectedOutput, output);
    }

}