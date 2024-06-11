import com.google.common.base.Utf8;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 char[] sequence = new char[]{'a', 'b', 'c', '\u0080', '\u0123', '\u0800', '\uD800', '\uDFFF'};
 byte[] expectedUtf8Bytes = new byte[]{(byte) 0x61, (byte) 0x62, (byte) 0x63, (byte) 0xc2, (byte) 0x83, (byte) 0xF8, 0xFF, 0xFF, (byte) 0xF0, 0x9F, 0xBF}; // UTF-8 encoding of the input sequence
 int length = Utf8.encodedLength(new String(sequence));
 byte[] actualUtf8Bytes = new byte[length];
 for (int i = 0; i < length; i++) {
 actualUtf8Bytes[i] = (byte) sequence[i];
 }
 assertEquals(expectedUtf8Bytes.length, Utf8.encodedLength(new String(sequence)));
 assertArrayEquals(expectedUtf8Bytes, actualUtf8Bytes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithIllegalArgument() {
 char[] sequence = new char[]{'a', 'b', '\ud800', '\udfff'};
 assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLength(new String(sequence)));
 }

 @Test
 public void testIsWellFormed() {
 byte[] wellFormedUtf8Bytes = new byte[]{(byte) 0x61, (byte) 0x62, (byte) 0x63, (byte) 0xc2, (byte) 0x83, (byte) 0xF8, 0xFF, 0xFF, (byte) 0xF0, 0x9F, 0xBF};
 byte[] invalidUtf8Bytes = new byte[]{(byte) 0x61, (byte) 0x62, (byte) 0x63, (byte) 0xFA, 0xFF, (byte) 0xF0, 0x9F, 0xBF}; // Invalid UTF-8 sequence
 assertFalse(Utf8.isWellFormed(wellFormedUtf8Bytes));
 assertFalse(Utf8.isWellFormed(invalidUtf8Bytes));
 }

 @Test
 public void testIsWellFormedWithValidUtf8() {
 byte[] wellFormedUtf8Bytes = new byte[]{(byte) 0x61, (byte) 0x62, (byte) 0x63, (byte) 0xc2, (byte) 0x83, (byte) 0xF8, 0xFF, 0xFF, (byte) 0xF0, 0x9F, 0xBF};
 assertTrue(Utf8.isWellFormed(wellFormedUtf8Bytes));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedWithInvalidUtf8() {
 byte[] invalidUtf8Bytes = new byte[]{(byte) 0x61, (byte) 0x62, (byte) 0xFA, 0xFF, (byte) 0xF0, 0x9F, 0xBF};
 assertThrows(IllegalArgumentException.class, () -> Utf8.isWellFormed(invalidUtf8Bytes));
 }
}