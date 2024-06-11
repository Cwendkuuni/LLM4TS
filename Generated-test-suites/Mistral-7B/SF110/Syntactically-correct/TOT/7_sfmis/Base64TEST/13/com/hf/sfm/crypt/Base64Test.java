import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

 @Test
 public void testAltBase64ToByteArray() {
 String testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
 byte[] expectedByteArray = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57 };
 byte[] actualByteArray = Base64.altBase64ToByteArray(testString);
 Assert.assertArrayEquals(expectedByteArray, actualByteArray);
 }

 @Test
 public void testAltBase64ToByteArray_InvalidInput() {
 String testString = "invalidInput";
 try {
 Base64.altBase64ToByteArray(testString);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAltBase64ToByteArray_EmptyInput() {
 String testString = "";
 byte[] actualByteArray = Base64.altBase64ToByteArray(testString);
 Assert.assertArrayEquals(new byte[0], actualByteArray);
 }

 @Test
 public void testBase64ToByteArray() {
 String testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
 byte[] expectedByteArray = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57 };
 byte[] actualByteArray = Base64.base64ToByteArray(testString);
 Assert.assertArrayEquals(expectedByteArray, actualByteArray);
 }

 @Test
 public void testBase64ToByteArray_InvalidInput() {
 String testString = "invalidInput";
 try {
 Base64.base64ToByteArray(testString);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testBase64ToByteArray_EmptyInput() {
 String testString = "";
 byte[] actualByteArray = Base64.base64ToByteArray(testString);
 Assert.assertArrayEquals(new byte[0], actualByteArray);
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] testByteArray = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57 };
 String expectedTestString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
 String actualTestString = Base64.byteArrayToAltBase64(testByteArray);
 Assert.assertEquals(expectedTestString, actualTestString);
 }

 @Test
 public void testByteArrayToAltBase64_EmptyInput() {
 byte[] testByteArray = new byte[0][];
 String actualTestString = Base64.byteArrayToAltBase64(testByteArray);
 Assert.assertEquals("", actualTestString);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] testByteArray = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57 };
 String expectedTestString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
 String actualTestString = Base64.byteArrayToBase64(testByteArray);
 Assert.assertEquals(expectedTestString, actualTestString);
 }

 @Test
 public void testByteArrayToBase64_EmptyInput() {
 byte[] testByteArray = new byte[0][];
 String actualTestString = Base64.byteArrayToBase64(testByteArray);
 Assert.assertEquals("", actualTestString);
 }
}