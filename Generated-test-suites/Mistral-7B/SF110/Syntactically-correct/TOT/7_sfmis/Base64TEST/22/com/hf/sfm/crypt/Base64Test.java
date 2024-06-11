import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

 @Test
 public void testAltBase64ToByteArray() {
 String testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
 byte[] expectedByteArray = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
 byte[] resultByteArray = Base64.altBase64ToByteArray(testString);
 Assert.assertArrayEquals(expectedByteArray, resultByteArray);
 }

 @Test
 public void testAltBase64ToByteArray_InvalidLength() {
 String testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/==";
 Assert.throwException(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(testString));
 }

 @Test
 public void testAltBase64ToByteArray_IllegalCharacter() {
 String testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/AB";
 Assert.throwException(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(testString));
 }

 @Test
 public void testBase64ToByteArray() {
 String testString = "SGVsbG8sIFdvcmxkIQ==";
 byte[] expectedByteArray = {71, 67, 84, 79, 84, 68, 65, 78, 61, 63, 65, 78, 74, 73, 74, 83, 67, 65, 84, 84, 69, 70, 65};
 byte[] resultByteArray = Base64.base64ToByteArray(testString);
 Assert.assertArrayEquals(expectedByteArray, resultByteArray);
 }

 @Test
 public void testBase64ToByteArray_InvalidLength() {
 String testString = "SGVsbG8sIFdvcmxkIQ===";
 Assert.throwException(IllegalArgumentException.class, () -> Base64.base64ToByteArray(testString));
 }

 @Test
 public void testBase64ToByteArray_IllegalCharacter() {
 String testString = "SGVsbG8sIFdvcmxkIQ==AB";
 Assert.throwException(IllegalArgumentException.class, () -> Base64.base64ToByteArray(testString));
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] testByteArray = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
 String expectedString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
 String resultString = Base64.byteArrayToAltBase64(testByteArray);
 Assert.assertEquals(expectedString, resultString);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] testByteArray = {71, 67, 84, 79, 84, 68, 65, 78, 61, 63, 65, 78, 74, 73, 74, 83, 67, 65, 84, 84, 69, 70, 65};
 String expectedString = "SGVsbG8sIFdvcmxkIQ==";
 String resultString = Base64.byteArrayToBase64(testByteArray);
 Assert.assertEquals(expectedString, resultString);
 }

 @Test
 public void testByteArrayToBase64_InvalidLength() {
 byte[] testByteArray = {71, 67, 84, 79, 84, 68, 65, 78, 61, 63, 65, 78, 74, 73, 74, 83, 67, 65, 84, 84, 69, 70, 65, 1};
 Assert.throwException(IllegalArgumentException.class, () -> Base64.byteArrayToBase64(testByteArray));
 }
}