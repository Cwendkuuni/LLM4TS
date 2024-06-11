import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.codec.language.Caverphone;

public class CaverphoneTest {

    @Test
    public void testEmptyString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testNullString() {
        Caverphone caverphone = new Caverphone();
        try {
            caverphone.caverphone(null);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testLowerCase() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("hello world");
        assertEquals("h2el3l0w0rld", result);
    }

    @Test
    public void testDigits() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("1234567890");
        assertEquals("2222222222", result);
    }

    @Test
    public void testStartOptions() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("tough cough enough trough");
        assertEquals("tou2fcou2feno2ftrou2f", result);
    }

    @Test
    public void testReplacements() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("tch qu x cy tph b sh z");
        assertEquals("2ch2qxsefhk2s", result);
    }

    @Test
    public void testMiddleCharacters() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("middle");
        assertEquals("m2dle", result);
    }

    @Test
    public void testHandlesRemovals() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("a1b2c33d4e5");
        assertEquals("111111", result);
    }

    @Test
    public void testHandlesLength() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("longstringlongstringlongstringlongstringlongstringlongstringlongstringlongstringlongstringlongstringlongstringlongstringlongstringlongstringlongstringlongstringlongstringlongstringlongstringlongstringlongstring");
        assertEquals("L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1L1", result);
    }

    @Test(expected = EncoderException.class)
    public void testInvalidInput() {
        Caverphone caverphone = new Caverphone();
        caverphone.caverphone("!@#$%^&*");
    }

    @Test
    public void testEquals() {
        Caverphone caverphone = new Caverphone();
        String str1 = "testtesttest";
        String str2 = "testtesttest";
        assertTrue(caverphone.isCaverphoneEqual(str1, str2));
    }

    @Test
    public void testNotEquals() {
        Caverphone caverphone = new Caverphone();
        String str1 = "testtesttest";
        String str2 = "testtesttes";
        assertFalse(caverphone.isCaverphoneEqual(str1, str2));
    }
}