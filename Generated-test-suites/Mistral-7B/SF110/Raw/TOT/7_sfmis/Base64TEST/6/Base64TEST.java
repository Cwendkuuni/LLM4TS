import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class Base64Test {

 private byte[] getTestData() {
 return new byte[]{"Hello, World!".getBytes()};
 }

 @Test
 public void testAltBase64ToByteArray() {
 String testString = "SGVsbG8sIFdvcmxkIQ==";
 byte[] expectedByteArray = {72, 101, 108, 108, 111, 119, 105, 110, 32, 87, 111, 114, 108, 100, 33};
 byte[] resultByteArray = Base64.altBase64ToByteArray(testString);
 assertArrayEquals(expectedByteArray, resultByteArray);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAltBase64ToByteArrayWithInvalidStringLength() {
 String testString = "Hello";
 Base64.altBase64ToByteArray(testString);
 }

 @Test
 public void testBase64ToByteArray() {
 String testString = "SGVsbG8sIHdvcmxkIQ==";
 byte[] expectedByteArray = {72, 101, 108, 108, 111, 119, 105, 110, 32, 87, 111, 114, 108, 100, 33};
 byte[] resultByteArray = Base64.base64ToByteArray(testString);
 assertArrayEquals(expectedByteArray, resultByteArray);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBase64ToByteArrayWithInvalidCharacter() {
 String testString = "SGVsbG8sIFdvcmxkIQ==!";
 Base64.base64ToByteArray(testString);
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] testByteArray = getTestData();
 String expectedString = "SGVsbG8sIFdvcmxkIQ==";
 String resultString = Base64.byteArrayToAltBase64(testByteArray);
 assertEquals(expectedString, resultString);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] testByteArray = getTestData();
 String expectedString = "SGVsbG8sIHdvcmxkIQ==";
 String resultString = Base64.byteArrayToBase64(testByteArray);
 assertEquals(expectedString, resultString);
 }
}