import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

 @Test
 public void testAltBase64ToByteArray() {
 String input = "RG9jdW1lbnRzIHdvcmxkIQ==";
 byte[] expectedOutput = { 72, 101, 108, 108, 111, 119, 100, 32, 77, 101, 115, 117, 108, 100, 33, 32, 32, 115, 117, 108, 108, 111, 32, 101, 118, 101, 114, 116, 32, 82, 101, 114, 111, 118, 105, 115, 111, 110, 103 };
 byte[] output = Base64.altBase64ToByteArray(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testAltBase64ToByteArray_InvalidLength() {
 String input = "RG9jdW1lbnRzIHdvcmxkIQ==AB";
 Assert.throwException(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
 }

 @Test
 public void testAltBase64ToByteArray_InvalidInput() {
 String input = "RG9jdW1lbnRzIHdvcmxkIQ==%";
 Assert.throwException(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
 }

 @Test
 public void testBase64ToByteArray() {
 String input = "SGVsbG8sIHRoZSBmaWxlIFdvcmxkIQ==";
 byte[] expectedOutput = { 83, 76, 72, 76, 65, 72, 73, 73, 73, 101, 118, 101, 114, 116, 32, 84, 111, 112, 101, 108, 108, 111, 32, 78, 101, 114, 111, 118, 105, 115, 111, 110, 103 };
 byte[] output = Base64.base64ToByteArray(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testBase64ToByteArray_EmptyInput() {
 String input = "";
 byte[] output = Base64.base64ToByteArray(input);
 Assert.assertArrayEquals(new byte[0], output);
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] input = { 72, 101, 108, 108, 111, 119, 100, 32, 77, 101, 115, 117, 108, 100, 33, 32, 32, 115, 117, 108, 108, 111, 32, 101, 118, 101, 114, 116, 32, 82, 101, 114, 111, 118, 105, 115, 111, 110, 103 };
 String expectedOutput = "RG9jdW1lbnRzIHdvcmxkIQ==";
 String output = Base64.byteArrayToAltBase64(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] input = { 83, 76, 72, 76, 65, 72, 73, 73, 73, 101, 118, 101, 114, 116, 32, 84, 111, 112, 101, 108, 108, 111, 32, 78, 101, 114, 111, 118, 105, 115, 111, 110, 103 };
 String expectedOutput = "SGVsbG8sIHRoZSBmaWxlIFdvcmxkIQ==";
 String output = Base64.byteArrayToBase64(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testByteArrayToBase64_EmptyInput() {
 byte[] input = {};
 String output = Base64.byteArrayToBase64(input);
 Assert.assertEquals("", output);
 }
}