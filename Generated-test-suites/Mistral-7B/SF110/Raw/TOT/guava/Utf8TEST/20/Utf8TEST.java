import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.Utf8;

public class Utf8Test {

 @Test
 public void testEncodedLength_BasicAscii() {
 CharSequence sequence = "abcdef";
 int expected = sequence.length();
 int actual = Utf8.encodedLength(sequence);
 assertEquals(expected, actual);
 }

 @Test
 public void testEncodedLength_MiddleOfUtf8() {
 CharSequence sequence = "abcdefghijkō";
 int expected = 12 * 0x7F + 3 * 0x7F; // 12 * 0x7F for basic ASCII, 3 * 0x7F for 'ō'
 int actual = Utf8.encodedLength(sequence);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLength_InvalidUtf8() {
 CharSequence sequence = "abcdefghijk\uD800"; // Unpaired surrogate
 int actual = Utf8.encodedLength(sequence);
 // An IllegalArgumentException should be thrown here
 }

 @Test
 public void testIsWellFormed_ValidUtf8() {
 byte[] bytes = "abcdefghijkō".getBytes();
 boolean expected = true;
 boolean actual = Utf8.isWellFormed(bytes);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsWellFormed_InvalidUtf8() {
 byte[] bytes = "\u0000\u0001\u0002\uD800"; // Unpaired surrogate
 boolean expected = false;
 boolean actual = Utf8.isWellFormed(bytes);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsWellFormed_InvalidUtf8_BeyondBounds() {
 byte[] bytes = "\u0000\u0001".getBytes();
 boolean expected = false;
 boolean actual = Utf8.isWellFormed(bytes, 1, bytes.length);
 assertEquals(expected, actual);
 }
}