import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmptyString() {
        Caverphone caverphone = new Caverphone();
        String expected = "1111111111";
        String actual = caverphone.caverphone("");
        assertEquals(expected, actual);
    }

    @Test
    public void testCaverphoneSingleCharacter() {
        Caverphone caverphone = new Caverphone();
        String expected = "A";
        String actual = caverphone.caverphone("a");
        assertEquals(expected, actual);
    }

    // Add test cases for other methods as needed

    @Test(expected = EncoderException.class)
    public void testEncodeNullObject() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(null);
    }

    @Test
    public void testIsCaverphoneEqualSameStrings() {
        Caverphone caverphone = new Caverphone();
        String str1 = "hello";
        String str2 = "hello";
        boolean expected = true;
        boolean actual = caverphone.isCaverphoneEqual(str1, str2);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsCaverphoneEqualDifferentStrings() {
        Caverphone caverphone = new Caverphone();
        String str1 = "hello";
        String str2 = "world";
        boolean expected = false;
        boolean actual = caverphone.isCaverphoneEqual(str1, str2);
        assertEquals(expected, actual);
    }

    // Add test cases for edge cases and exception handling scenarios

}