import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.Utf8;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 charSequence sequence = "test";
 int expected = Utf8.encodedLength(sequence);
 int actual = Utf8.encodedLength(sequence);
 assertEquals("Encoded length should match", expected, actual);

 sequence = "möö";
 expected = Utf8.encodedLength(sequence);
 actual = Utf8.encodedLength(sequence);
 assertEquals("Encoded length should match", expected, actual);

 sequence = "👋💔";
 expected = Utf8.encodedLength(sequence);
 actual = Utf8.encodedLength(sequence);
 assertEquals("Encoded length should match", expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithIllegalCharSequence() {
 CharSequence sequence = new String(new char[]{'\uD800', '\uDC00'});
 Utf8.encodedLength(sequence);
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = new byte[]{(byte) 72, (byte) 71, (byte) 73, (byte) 74};
 assertTrue("UTF-8 bytes should be well-formed: " + bytes, Utf8.isWellFormed(bytes));

 bytes = new byte[]{(byte) 72, (byte) 71, (byte) -63, (byte) 73, (byte) 74};
 assertTrue("UTF-8 bytes should be well-formed: " + bytes, Utf8.isWellFormed(bytes));

 bytes = new byte[]{(byte) -63, (byte) 73, (byte) 74, (byte) 72, (byte) 71};
 assertFalse("UTF-8 bytes should not be well-formed: " + bytes, Utf8.isWellFormed(bytes));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedWithIllegalByteArray() {
 Utf8.isWellFormed(new byte[]{(byte) -128});
 }
}