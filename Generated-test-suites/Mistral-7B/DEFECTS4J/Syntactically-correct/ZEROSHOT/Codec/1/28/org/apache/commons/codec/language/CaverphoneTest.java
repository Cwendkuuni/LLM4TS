import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.junit.Test;
import org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphoneNull() {
        Caverphone caverphone = new Caverphone();
        String expected = "1111111111";
        String actual = caverphone.caverphone(null);
        assertEquals(expected, actual);
    }

    @Test
    public void testCaverphoneEmpty() {
        Caverphone caverphone = new Caverphone();
        String expected = "1111111111";
        String actual = caverphone.caverphone("");
        assertEquals(expected, actual);
    }

    @Test
    public void testCaverphoneLowerCase() {
        Caverphone caverphone = new Caverphone();
        String txt = "hello world";
        String expected = "H2l2o2 w2r2d2";
        String actual = caverphone.caverphone(txt);
        assertEquals(expected, actual);
    }

    @Test
    public void testCaverphoneStartOptions() {
        Caverphone caverphone = new Caverphone();
        String txt = "this is a test";
        String expected = "tio5 s2t2 e2s2 t2 a2 t2 e2s2 t2";
        String actual = caverphone.caverphone(txt);
        assertEquals(expected, actual);
    }

    @Test
    public void testCaverphoneReplacements() {
        Caverphone caverphone = new Caverphone();
        String txt = "this is a test";
        String expected = "tio5 s2t2 e2s2 t2 a2 t2 e2s2 t2 cq3 k3 si3 se3 cy3 sy3 tch3 2ch3 c2 k2 q2 k2 v2 dg2 2g2 d3 tio3 sio3 tia3 sia3 d3 p3 ph3 fh3 b3 sh3 s2 z3 w3 Wh3 w3 r3 R3 l3 L3 r2 2 r2 l2 2";
        String actual = caverphone.caverphone(txt);
        assertEquals(expected, actual);
    }

    @Test
    public void testCaverphoneRemovals() {
        Caverphone caverphone = new Caverphone();
        String txt = "this is a test";
        String expected = "3kh3 k3 3kh3 k3 S T P K F M N W3 R3 L3 2";
        String actual = caverphone.caverphone(txt);
        assertEquals(expected, actual);
    }

    @Test
    public void testCaverphoneEncodeDecode() throws DecoderException {
        Caverphone encoder = new Caverphone();
        StringDecoder decoder = new StringDecoder();
        String txt = "this is a test";
        String encoded = encoder.caverphone(txt);
        String decoded = decoder.decode(encoded);
        assertEquals(txt, decoded);
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneNonString() {
        Caverphone caverphone = new Caverphone();
        Object obj = new Object();
        caverphone.encode(obj);
    }

    @Test
    public void testCaverphoneIsEqual() {
        Caverphone caverphone = new Caverphone();
        String str1 = "this is a test";
        String str2 = "this is a test";
        boolean expected = true;
        boolean actual = caverphone.isCaverphoneEqual(str1, str2);
        assertEquals(expected, actual);
    }

    @Test
    public void testCaverphoneNotEqual() {
        Caverphone caverphone = new Caverphone();
        String str1 = "this is a test";
        String str2 = "this is not a test";
        boolean expected = false;
        boolean actual = caverphone.isCaverphoneEqual(str1, str2);
        assertEquals(expected, actual);
    }

}