import static org.junit.Assert.*;
import com.google.common.base.Utf8;

public class Utf8Test {

 // Test case for encodedLength method with normal UTF-8 characters
 @Test
 public void testEncodedLengthNormalCharacters() {
 String sequence = "Hello World";
 int expectedLength = Utf8.encodedLength(sequence);
 int actualLength = Utf8.encodedLength(sequence);
 assertEquals("Incorrect UTF-8 length for normal characters", expectedLength, actualLength);
 }

 // Test case for encodedLength method with multi-byte characters
 @Test
 public void testEncodedLengthMultiByteCharacters() {
 String sequence = "Mojito\u00ED";
 int expectedLength = Utf8.encodedLength(sequence);
 int actualLength = Utf8.encodedLength(sequence);
 assertEquals("Incorrect UTF-8 length for multi-byte characters", expectedLength, actualLength);
 }

 // Test case for encodedLength method with surrogate pairs
 @Test
 public void testEncodedLengthSurrogatePairs() {
 String sequence = "\uD800\uDC00\uD800\uDCCC";
 int expectedLength = Utf8.encodedLength(sequence);
 int actualLength = Utf8.encodedLength(sequence);
 assertEquals("Incorrect UTF-8 length for surrogate pairs", expectedLength, actualLength);
 }

 // Test case for isWellFormed method with valid UTF-8 byte array
 @Test
 public void testIsWellFormedValidUtf8ByteArray() {
 byte[] validUtf8Bytes = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33 };
 boolean expectedResult = Utf8.isWellFormed(validUtf8Bytes);
 boolean actualResult = Utf8.isWellFormed(validUtf8Bytes);
 assertTrue("Invalid result for valid UTF-8 byte array", expectedResult == actualResult);
 }

 // Test case for isWellFormed method with invalid UTF-8 byte array
 @Test
 public void testIsWellFormedInvalidUtf8ByteArray() {
 byte[] invalidUtf8Bytes = { 72, 101, 108, 111, 111, 196, 168, 136, 132, 33 };
 boolean expectedResult = false;
 boolean actualResult = Utf8.isWellFormed(invalidUtf8Bytes);
 assertTrue("Invalid result for invalid UTF-8 byte array", expectedResult == actualResult);
 }

 // Test case for isWellFormed method with empty byte array
 @Test
 public void testIsWellFormedEmptyByteArray() {
 byte[] emptyByteArray = new byte[0];
 boolean expectedResult = true;
 boolean actualResult = Utf8.isWellFormed(emptyByteArray);
 assertTrue("Invalid result for empty byte array", expectedResult == actualResult);
 }
}