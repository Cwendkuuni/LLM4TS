import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphoneNull() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone(null);
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphoneEmpty() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphoneLowerCase() {
        Caverphone caverphone = new Caverphone();
        String txt = "hello";
        String result = caverphone.caverphone(txt);
        String expected = "h3l2l2o2o";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneReplaceC() {
        Caverphone caverphone = new Caverphone();
        String txt = "cat";
        String result = caverphone.caverphone(txt);
        String expected = "ctk";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneReplaceQ() {
        Caverphone caverphone = new Caverphone();
        String txt = "quilt";
        String result = caverphone.caverphone(txt);
        String expected = "2qul2t";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneReplaceX() {
        Caverphone caverphone = new Caverphone();
        String txt = "xylophone";
        String result = caverphone.caverphone(txt);
        String expected = "kphnkf2n";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneReplaceDg() {
        Caverphone caverphone = new Caverphone();
        String txt = "dog";
        String result = caverphone.caverphone(txt);
        String expected = "2g";
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneNonString() {
        Caverphone caverphone = new Caverphone();
        Integer num = 123;
        caverphone.encode(num); // This will throw an EncoderException
    }
}