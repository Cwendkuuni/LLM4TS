import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Locale;

public class CaverphoneTest {

    @Test
    public void testEmptyString() {
        StringEncoder encoder = new Caverphone();
        String encoded = encoder.encode("");
        String decoded = encoder.decode(encoded);
        assertEquals("", decoded);
    }

    @Test
    public void testSingleCharacter() {
        StringEncoder encoder = new Caverphone();
        String encoded = encoder.encode("a");
        String decoded = encoder.decode(encoded);
        assertEquals("a", decoded);
    }

    @Test
    public void testLowercaseString() {
        StringEncoder encoder = new Caverphone();
        String encoded = encoder.encode("apple");
        String decoded = encoder.decode(encoded);
        assertEquals("apple", decoded);
    }

    @Test
    public void testStringWithDigits() {
        StringEncoder encoder = new Caverphone();
        String encoded = encoder.encode("2apple3");
        String decoded = encoder.decode(encoded);
        assertEquals("2apple3", decoded);
    }

    @Test
    public void testStringWithSpecialCharacters() {
        StringEncoder encoder = new Caverphone();
        String encoded = encoder.encode("Apple$!");
        String decoded = encoder.decode(encoded);
        String expected = "Apple$!"; // Caverphone algorithm does not remove special characters
        assertEquals(expected, decoded);
    }

    @Test(expected = EncoderException.class)
    public void testNullString() {
        StringEncoder encoder = new Caverphone();
        encoder.encode(null);
    }

    @Test
    public void testCaseSensitive() {
        StringEncoder encoder = new Caverphone();
        String encoded1 = encoder.encode("Apple");
        String encoded2 = encoder.encode("apple");
        boolean result = encoder.isCaverphoneEqual(encoded1, encoded2);
        assertTrue(result);
    }

    @Test
    public void testHandleStartOptions() {
        StringEncoder encoder = new Caverphone();
        String encoded1 = encoder.encode("cougH");
        String encoded2 = encoder.encode("cou2f");
        boolean result = encoder.isCaverphoneEqual(encoded1, encoded2);
        assertTrue(result);
    }

    @Test
    public void testHandleReplacements() {
        StringEncoder encoder = new Caverphone();
        String encoded1 = encoder.encode("tchisecret");
        String encoded2 = encoder.encode("2chisecret");
        boolean result = encoder.isCaverphoneEqual(encoded1, encoded2);
        assertTrue(result);
    }

    @Test
    public void testHandleRemovals() {
        StringEncoder encoder = new Caverphone();
        String encoded1 = encoder.encode("212121");
        String encoded2 = encoder.encode("111111");
        boolean result = encoder.isCaverphoneEqual(encoded1, encoded2);
        assertTrue(result);
    }

    @Test
    public void testHandleAdditions() {
        StringEncoder encoder = new Caverphone();
        String encoded1 = encoder.encode("12345");
        String encoded2 = encoder.encode("1234511111");
        boolean result = encoder.isCaverphoneEqual(encoded1, encoded2);
        assertTrue(result);
    }

    @Test
    public void testStringWithEmptySubstrings() {
        StringEncoder encoder = new Caverphone();
        String encoded = encoder.encode("tio tia d");
        String decoded = encoder.decode(encoded);
        assertEquals("tio tia d", decoded);
    }
}