import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

    @Test
    public void testCaverphone_EmptyString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        String expected = "1111111111";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphone_SingleCharacter() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("a");
        String expected = "a";
        assertEquals(expected, result.substring(0, 1));
        assertEquals("1111111111", result.substring(result.length() - 12));
    }

    @Test
    public void testCaverphone_NormalString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("hello");
        String expected = "h3l2l2o3o11";
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testCaverphone_NullString() {
        Caverphone caverphone = new Caverphone();
        caverphone.caverphone(null);
    }
}