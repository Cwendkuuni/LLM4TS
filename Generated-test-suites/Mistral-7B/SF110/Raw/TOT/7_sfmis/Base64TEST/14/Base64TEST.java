import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

 @Test
 public void testAltBase64ToByteArray() {
 String input = "RG9jdW1lbnQ=";
 byte[] expected = {-65, -65, -65, -65, -65, -65, -65, -65, 61, 128, 61, 128, 61, 128, 61, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, 61, 128, 61, 128, 61, 61};
 byte[] result = Base64.altBase64ToByteArray(input);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testAltBase64ToByteArrayWithInvalidLength() {
 String input = "RG9jdW1lbnQ=";
 byte[] result = Base64.altBase64ToByteArray(input.substring(0, input.length() - 1)); // remove last '='
 Assert.assertTrue(result.length % 3 != 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAltBase64ToByteArrayWithIllegalCharacter() {
 String input = "RG9jdW1lbnQ%";
 Base64.altBase64ToByteArray(input); // this line should throw an IllegalArgumentException
 }

 @Test
 public void testBase64ToByteArray() {
 String input = "SGVsbG8gV29ybGQh";
 byte[] expected = {83, 119, 111, 111, 100, 56, 57, 51, 50, -1, 52, 56, 51, 56, 52, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 50, 51, 52, 56, 51, 51};
 byte[] result = Base64.base64ToByteArray(input);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testBase64ToByteArrayWithPadding() {
 String input = "SGVsbG8gV1w=";
 byte[] expected = {83, 119, 111, 111, 100, 56, 57, 51, 50, -1, 52, 56, 51, 56, 52, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 50, 51};
 byte[] result = Base64.base64ToByteArray(input);
 Assert.assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBase64ToByteArrayWithIllegalCharacter() {
 String input = "SGVsbG8gV29ybGQh%";
 Base64.base64ToByteArray(input); // this line should throw an IllegalArgumentException
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] input = {48, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 60, 61, 61};
 String expected = "IjIjIjIjIjIjIjIjIjIjIjIjI=";
 String result = Base64.byteArrayToAltBase64(input);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] input = {48, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 60, 61, 61};
 String expected = "IjIjIjIjIjIjIjIjIjIjIjIjI=";
 String result = Base64.byteArrayToBase64(input);
 Assert.assertEquals(expected, result);
 }
}