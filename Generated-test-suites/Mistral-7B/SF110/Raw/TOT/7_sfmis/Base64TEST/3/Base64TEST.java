import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

 @Test
 public void testAltBase64ToByteArray() {
 String input = "QWxhZG9iIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
 byte[] expectedOutput = new byte[]{(byte) 0x32, (byte) 0x30, (byte) 0x31, (byte) 0x37, (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37, (byte) 0x38, (byte) 0x39};
 byte[] output = Base64.altBase64ToByteArray(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testAltBase64ToByteArrayWithInvalidInputLength() {
 String input = "QWxhZG9iIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIu";
 Assert.throwException(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
 }

 @Test
 public void testAltBase64ToByteArrayWithInvalidInputCharacter() {
 String input = "QWxhZG9iIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=AB";
 Assert.throwException(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
 }

 @Test
 public void testBase64ToByteArray() {
 String input = "SGVsbG8sIHdvcmxkIQ==";
 byte[] expectedOutput = new byte[]{(byte) 0x53, (byte) 0x75, (byte) 0x67, (byte) 0x67, (byte) 0x65, (byte) 0x72, (byte) 0x65, (byte) 0x74, (byte) 0x68, (byte) 0x65, (byte) 0x78, (byte) 0x61, (byte) 0x73, (byte) 0x74, (byte) 0x43};
 byte[] output = Base64.base64ToByteArray(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testBase64ToByteArrayWithInvalidInputLength() {
 String input = "SGVsbG8sIHdvcmxkIQ==AB";
 Assert.throwException(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] input = new byte[]{(byte) 0x48, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F};
 String expectedOutput = "VG8gZXJyYXNlIGlzIHN1YnR5cgu=";
 String output = Base64.byteArrayToAltBase64(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] input = new byte[]{(byte) 0x48, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F};
 String expectedOutput = "VG8gZXJyYXNlIQ==";
 String output = Base64.byteArrayToBase64(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testByteArrayToBase64WithPadding() {
 byte[] input = new byte[]{(byte) 0x48, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F, (byte) 0x01};
 String expectedOutput = "VG8gZXJyYXNlIGZvdWw=";
 String output = Base64.byteArrayToBase64(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testByteArrayToBase64WithInvalidInputLength() {
 byte[] input = new byte[]{(byte) 0x48, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F, (byte) 0x02};
 Assert.throwException(IllegalArgumentException.class, () -> Base64.byteArrayToBase64(input));
 }

 @Test
 public void testByteArrayToBase64WithInvalidInputCharacter() {
 byte[] input = new byte[]{(byte) 0x48, (byte) 0x65, (byte) 0x6C, (byte) 0xCC, (byte) 0x6F};
 Assert.throwException(IllegalArgumentException.class, () -> Base64.byteArrayToBase64(input));
 }
}