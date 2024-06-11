import com.google.common.base.Utf8;
import org.junit.Assert;
import org.junit.Test;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 charSequence sequence = "abcdefghijklmnopqrstuvwxyz"; // ASCII characters
 int length = Utf8.encodedLength(sequence);
 Assert.assertEquals(11, length);

 sequence = "abcdefghijklmnopqrstuvwxyzáéíóú"; // Latin-1 characters
 length = Utf8.encodedLength(sequence);
 Assert.assertEquals(23, length);

 sequence = "abcdefghijklmnopqrstuvwxyżź"; // Characters with diacritics
 length = Utf8.encodedLength(sequence);
 Assert.assertEquals(25, length);

 sequence = "\u0394\u0395\u0396\u0397"; // Greek characters
 length = Utf8.encodedLength(sequence);
 Assert.assertEquals(12, length);

 sequence = "\uD800\uDC00\uD800\uDCCC"; // Surrogate pairs
 try {
 Utf8.encodedLength(sequence);
 Assert.fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = new byte[]{(byte) 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4a}; // ASCII characters
 Assert.assertTrue(Utf8.isWellFormed(bytes));

 bytes = new byte[]{(byte) 0xE2, 0x9A, 0x85, 0x61, 0x72, 0x73, 0x63, 0x75, 0x53, 0x74, 0x61, 0x72, 0x65, 0x6E}; // Latin-1 characters
 Assert.assertTrue(Utf8.isWellFormed(bytes));

 bytes = new byte[]{(byte) 0xE2, 0x9A, 0x85, 0xE2, 0x9A, 0x82, 0xE2, 0x9A, 0x83, 0xE2, 0x9A, 0x84, 0xE2, 0x9A, 0x85, 0xE2, 0x9A, 0x85, 0xE2, 0x9A, 0x85}; // Invalid UTF-8 sequence
 Assert.assertFalse(Utf8.isWellFormed(bytes));
 }
}