import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

 @Test
 public void testAltBase64ToByteArray() {
 String input = "QWRvYmFzZSBPcGFzcw==";
 byte[] expectedOutput = {31, 104, 111, 119, 112, 101, 114, 116, 102, 105, 103, 111, 111, 118, 101, 114, 116, 105, 111, 115, 115};
 byte[] output = Base64.altBase64ToByteArray(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testAltBase64ToByteArray_InvalidLength() {
 String input = "QWRvYmFzZSBPcGFzcw==."; // String length should be a multiple of 4.
 Assert.assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
 }

 @Test
 public void testAltBase64ToByteArray_InvalidCharacter() {
 String input = "QWRvYmFzZSBPcGFzcw==%"; // The input string should only contain base64 encoded characters.
 Assert.assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
 }

 @Test
 public void testBase64ToByteArray() {
 String input = "SGVsbG8gV29ybGQh";
 byte[] expectedOutput = {71, 66, 76, 65, 72, 65, 74, 69, 63, 68, 65, 78, 65, 72, 73, 73, 74, 65, 78, 74, 65, 64, 65, 64, 72, 72, 61};
 byte[] output = Base64.base64ToByteArray(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testBase64ToByteArray_InvalidLength() {
 String input = "SGVsbG8gV29ybGQh."; // String length should be a multiple of 4.
 Assert.assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));
 }

 @Test
 public void testBase64ToByteArray_InvalidCharacter() {
 String input = "SGVsbG8gV29ybGQh%"; // The input string should only contain base64 encoded characters.
 Assert.assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] input = {31, 104, 111, 119, 112, 101, 114, 116, 102, 105, 103, 111, 111, 118, 101, 114, 116, 105, 111, 115, 115};
 String expectedOutput = "QWRvYmFzZSBPcGFzcw==";
 String output = Base64.byteArrayToAltBase64(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] input = {71, 66, 76, 65, 72, 65, 74, 69, 63, 68, 65, 78, 65, 72, 73, 73, 74, 65, 78, 74, 65, 64, 65, 64, 72, 72, 61};
 String expectedOutput = "SGVsbG8gV29ybGQh";
 String output = Base64.byteArrayToBase64(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testByteArrayToBase64_InvalidLength() {
 byte[] input = {71, 66, 76, 65}; // The size of the byte array should be a multiple of 3.
 Assert.assertThrows(RuntimeException.class, () -> Base64.byteArrayToBase64(input));
 }
}