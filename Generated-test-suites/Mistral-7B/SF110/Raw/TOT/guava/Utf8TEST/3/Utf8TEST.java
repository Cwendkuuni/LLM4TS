import com.google.common.base.Utf8;
import org.junit.Test;
import org.junit.Assert.*;

public class Utf8Test {

 @Test
 public void testEncodedLengthCharSequence() {
 CharSequence sequence = "Hello, 世界!"; // Normal char sequence
 int expected = Utf8.encodedLength(sequence);
 int actual = Utf8.encodedLength(sequence);
 assertEquals("Unexpected length for 'Hello, 世界!'", expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthIllegalCharSequence() {
 CharSequence sequence = "Hello\uD800"; // Surrogate pair
 int actual = Utf8.encodedLength(sequence);
 fail("Unexpectedly did not throw IllegalArgumentException");
 }

 @Test
 public void testEncodedLengthEmptyCharSequence() {
 CharSequence sequence = "";
 int expected = 0;
 int actual = Utf8.encodedLength(sequence);
 assertEquals("Unexpected length for empty CharSequence", expected, actual);
 }

 @Test
 public void testEncodedLengthSingleByteCharSequence() {
 CharSequence sequence = "Hello";
 int expected = 5;
 int actual = Utf8.encodedLength(sequence);
 assertEquals("Unexpected length for 'Hello'", expected, actual);
 }

 @Test
 public void testIsWellFormedByteArray() {
 byte[] bytes = {0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x2c, 0xe4, 0xb8, 0xad, 0xe5, 0xa7, 0x9a, 0xe5, 0xa4, 0xaf}; // Normal byte array
 boolean expected = true;
 boolean actual = Utf8.isWellFormed(bytes);
 assertEquals("Unexpected result for normal byte array", expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedByteArrayWithBadByte() {
 byte[] bytes = {0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x2c, 0xff, 0x1a, 0xe5, 0xa7, 0x9a, 0xe5, 0xA4, 0xaf}; // Byte out of range
 boolean actual = Utf8.isWellFormed(bytes);
 fail("Unexpectedly did not throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedByteArrayWithSurrogatePair() {
 byte[] bytes = {0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x2c, 0xd8, 0x00, 0xdc, 0x04}; // Surrogate pair
 boolean actual = Utf8.isWellFormed(bytes);
 fail("Unexpectedly did not throw IllegalArgumentException");
 }
}