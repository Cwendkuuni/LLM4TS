import com.google.common.base.Utf8;
import org.junit.Test;
import org.junit.Assert.*;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "abcdefghijklmnopqrstuvwxyz"; // typical use case
 int actual = Utf8.encodedLength(sequence);
 int expected = 23;
 assertEquals(expected, actual);

 sequence = "\u0393\u03A9\u03B1\u03B4\u03B5"; // edge case with multibyte characters
 actual = Utf8.encodedLength(sequence);
 expected = 10;
 assertEquals(expected, actual);

 sequence = "\uD800\uDC00"; // error case: unpaired surrogate
 try {
 actual = Utf8.encodedLength(sequence);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x46, (byte) 0x47, (byte) 0x48}; // typical use case
 boolean actual = Utf8.isWellFormed(bytes);
 assertTrue(actual);

 bytes = new byte[]{(byte) -78, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x46, (byte) 0x47, (byte) 0x48}; // error case: non-positive byte
 assertFalse(Utf8.isWellFormed(bytes));

 bytes = new byte[]{(byte) 0xEF, (byte) 0xBF, (byte) 0xBD}; // error case: UTF-8 invalid sequence
 assertFalse(Utf8.isWellFormed(bytes));
 }
}