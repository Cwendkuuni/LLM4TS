import com.google.common.base.Utf8;
import org.junit.Test;
import org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test empty CharSequence
        CharSequence emptySequence = "";
        int expectedLength = 0;
        int actualLength = Utf8.encodedLength(emptySequence);
        assertEquals(expectedLength, actualLength);

        // Test standard ASCII characters
        CharSequence asciiSequence = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int expectedAsciiLength = asciiSequence.length();
        int actualAsciiLength = Utf8.encodedLength(asciiSequence);
        assertEquals(expectedAsciiLength, actualAsciiLength);

        // Test single multibyte character
        CharSequence multibyteSequence = "é";
        int expectedMultibyteLength = 2;
        int actualMultibyteLength = Utf8.encodedLength(multibyteSequence);
        assertEquals(expectedMultibyteLength, actualMultibyteLength);

        // Test multiple multibyte characters
        CharSequence multibyteSequenceWithSurrogates = "\uD800\uDC00\uD801\uDC00";
        int expectedMultibyteWithSurrogatesLength = 6;
        int actualMultibyteWithSurrogatesLength = Utf8.encodedLength(multibyteSequenceWithSurrogates);
        assertEquals(expectedMultibyteWithSurrogatesLength, actualMultibyteWithSurrogatesLength);

        // Test invalid UTF-8 sequence
        CharSequence invalidUtf8Sequence = "\uFDD0\uD800\uDC00";
        try {
            Utf8.encodedLength(invalidUtf8Sequence);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsWellFormed() {
        // Test valid UTF-8 byte array
        byte[] validUtf8Bytes = new byte[]{0x41, 0x42, 0x43, 0xE2, 0x82, 0xAC};
        boolean expectedValid = true;
        boolean actualValid = Utf8.isWellFormed(validUtf8Bytes);
        assertTrue(expectedValid == actualValid);

        // Test invalid UTF-8 byte array
        byte[] invalidUtf8Bytes = new byte[]{0x41, 0x42, 0x43, (byte) 0xC2, 0x82, 0x83};
        boolean expectedInvalid = false;
        boolean actualInvalid = Utf8.isWellFormed(invalidUtf8Bytes);
        assertFalse(expectedInvalid == actualInvalid);
    }

    @Test
    public void testIsWellFormedSlowPath() {
        // Test valid UTF-8 byte array
        byte[] validUtf8Bytes = new byte[]{0x41, 0x42, 0x43, 0xE2, 0x82, 0xAC};
        boolean expectedValid = true;
        boolean actualValid = Utf8.isWellFormedSlowPath(validUtf8Bytes, 0, validUtf8Bytes.length);
        assertTrue(expectedValid == actualValid);

        // Test invalid UTF-8 byte array with single invalid byte
        byte[] invalidUtf8BytesWithSingleByte = new byte[]{0x41, 0x42, 0xC2, 0xE2, 0x82, 0xAC};
        boolean expectedInvalidWithSingleByte = false;
        boolean actualInvalidWithSingleByte = Utf8.isWellFormedSlowPath(invalidUtf8BytesWithSingleByte, 0, invalidUtf8BytesWithSingleByte.length);
        assertFalse(expectedInvalidWithSingleByte == actualInvalidWithSingleByte);

        // Test invalid UTF-8 byte array with multiple invalid bytes
        byte[] invalidUtf8BytesWithMultipleBytes = new byte[]{0x41, 0x42, 0xC2, 0x82, 0x83, 0xC3};
        boolean expectedInvalidWithMultipleBytes = false;
        boolean actualInvalidWithMultipleBytes = Utf8.isWellFormedSlowPath(invalidUtf8BytesWithMultipleBytes, 0, invalidUtf8BytesWithMultipleBytes.length);
        assertFalse(expectedInvalidWithMultipleBytes == actualInvalidWithMultipleBytes);
    }
}