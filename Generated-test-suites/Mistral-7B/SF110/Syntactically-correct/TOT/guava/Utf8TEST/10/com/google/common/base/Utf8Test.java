import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.Utf8;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
 int actual = Utf8.encodedLength(sequence);
 int expected = sequence.length() * 3 >> 1; // Approximate value based on ASCII characters
 assertEquals("Encoded length is incorrect", expected, actual);

 sequence = "\u00A0\u20AC"; // Null and Currency symbol
 actual = Utf8.encodedLength(sequence);
 expected = 4;
 assertEquals("Encoded length is incorrect", expected, actual);

 sequence = "\uD800\uDC00"; // Surrogate pair
 try {
 actual = Utf8.encodedLength(sequence);
 fail("Expected IllegalArgumentException for surrogate pair");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testEncodedLengthGeneral() {
 CharSequence sequence = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\uD800\uDC00abcdefghijklmnopqrstuvwxyz";
 int actual = Utf8.encodedLengthGeneral(sequence, 0);
 int expected = sequence.length() * 3 >> 1; // Approximate value based on ASCII characters and surrogate pair
 assertEquals("Encoded length is incorrect", expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthIllegalArgument() {
 CharSequence sequence = "\uD800\uDC01"; // Surrogate pair followed by non-surrogate character
 int actual = Utf8.encodedLength(sequence);
 fail("Expected IllegalArgumentException for invalid surrogate pair");
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4A, 0x4B, 0x4C, 0x4D, 0x4E, 0x4F };
 boolean actual = Utf8.isWellFormed(bytes);
 assertTrue("UTF-8 bytes are not well-formed", actual);
 }

 @Test
 public void testIsWellFormedInvalidUtf8() {
 byte[] bytes = { 0xE1, 0xBE, 0xAD, 0xBE, 0xEF, 0xBD, 0xAD };
 boolean actual = Utf8.isWellFormed(bytes);
 assertFalse("Invalid UTF-8 bytes are well-formed", actual);
 }

 @Test
 public void testIsWellFormedSurrogatePair() {
 byte[] bytes = { 0xD8, 0x00, 0xDC, 0x00 };
 boolean actual = Utf8.isWellFormed(bytes);
 assertFalse("Surrogate pair is well-formed", actual);
 }

 @Test
 public void testIsWellFormedValidUtf16() {
 byte[] bytes = { 0xD8, 0x00, 0xDC, 0x01, 0xD8, 0x01, 0xDC, 0x02 };
 boolean actual = Utf8.isWellFormed(bytes);
 assertTrue("UTF-16 bytes are not well-formed", actual);
 }

 @Test
 public void testIsWellFormedValidUtf8() {
 byte[] bytes = { 0x41, 0x80, 0x42, 0x80, 0x43, 0x80 };
 boolean actual = Utf8.isWellFormed(bytes);
 assertTrue("UTF-8 bytes are not well-formed", actual);
 }
}