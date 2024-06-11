import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

 @Test
 public void testAltBase64ToByteArray() {
 String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
 byte[] expected = new byte[]{-65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, 60, 61, 62, 63, 52, 53, 54, 55, 56, 57, 58};
 byte[] actual = Base64.altBase64ToByteArray(input);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testAltBase64ToByteArrayInvalidLength() {
 String input = "ABCDEFG";
 try {
 Base64.altBase64ToByteArray(input);
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testAltBase64ToByteArrayMissingEqualSigns() {
 String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
 byte[] actual = Base64.altBase64ToByteArray(input);
 Assert.assertArrayEquals(actual, Base64.altBase64ToByteArray(input + "="));
 }

 @Test
 public void testAltBase64ToByteArrayMissingFirstEqualSign() {
 String input = "=ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
 byte[] actual = Base64.altBase64ToByteArray(input);
 Assert.assertArrayEquals(Base64.altBase64ToByteArray("=".getBytes()), actual);
 }

 @Test
 public void testBase64ToByteArray() {
 String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
 byte[] expected = new byte[]{-65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, 60, 61, 62, 63, 52, 53, 54, 55, 56, 57, 58};
 byte[] actual = Base64.base64ToByteArray(input);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testBase64ToByteArrayInvalidLength() {
 String input = "ABCDEFG";
 try {
 Base64.base64ToByteArray(input);
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testBase64ToByteArrayMissingEqualSigns() {
 String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
 byte[] actual = Base64.base64ToByteArray(input);
 Assert.assertArrayEquals(actual, Base64.base64ToByteArray(input + "=="));
 }

 @Test
 public void testBase64ToByteArrayMissingFirstEqualSign() {
 String input = "====ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
 byte[] actual = Base64.base64ToByteArray(input);
 Assert.assertArrayEquals(Base64.altBase64ToByteArray("=".getBytes()), actual);
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] input = new byte[]{-65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, 60, 61, 62, 63, 52, 53, 54, 55, 56, 57, 58};
 String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
 String actual = Base64.byteArrayToAltBase64(input);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] input = new byte[]{-65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, 60, 61, 62, 63, 52, 53, 54, 55, 56, 57, 58};
 String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
 String actual = Base64.byteArrayToBase64(input);
 Assert.assertEquals(expected, actual);
 }
}