import static org.junit.Assert.*;

import com.google.common.base.Utf8;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        String text = "Hello, World!";
        int actualLength = Utf8.encodedLength(text);
        int expectedLength = 11; // 11 bytes for "Hello, World!" in UTF-8 encoding
        assertEquals(expectedLength, actualLength);
    }
}