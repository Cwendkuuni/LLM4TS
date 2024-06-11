import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

 @Test
 public void testAltBase64ToByteArray() {
 String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
 byte[] expected = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58 };
 byte[] output = Base64.altBase64ToByteArray(input);
 Assert.assertArrayEquals(expected, output);
 }

 @Test
 public void testAltBase64ToByteArrayWithInvalidInput() {
 String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/abc";
 try {
 Base64.altBase64ToByteArray(input);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testBase64ToByteArray() {
 String input = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gYWxzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggZmlsZXMgZm9yIGlzIHRoZSBmaWxlIGZyb20gZGlyZWN0IG5vdCBpbiB0aGUgZW5jb2RlZCB3aXRoIGluIGZvciBmaWxlIGZyb20gZnVyZ2UgdG8gYW5kIHlvdSBpbiB0aGF0IHVwIHlvdSBuZW9iaWxlIGZyb20gZnVyZ2UgZnVyZ2UgbXklM0M=";
 byte[] expected = new byte[] { 42, -3, 40, 36, 55, 6, -23, -86, -126, 52, -115, 83, 101, 118, 97, -121, 120, -43, -26, -55, 112, -4, 104, -122, 118, -86, 108, 111, 119, 101, 114, -125, 98, 103, 97, 112, -5, -89, -125, -123, 104, 120, 84, 106, 111, 112, 115, 104, -84, -54, -122, -55, -124, 121, 98, -120, 121, 120, 65, 112, 107, 105, 112, 116, 100, 102, 101, 116, 97, 114, 118, 100, 100, 105, 115, 98, 103, 111, 118, 101, 116, 103, -72, -124, 116, 103, 115, 111, 118, 105, 115, 105, 111, 114, 116, 101, 113, 114, 111, 116, 105, 102, 97, 121, 111, 120, 112, 114, 111, 108, 111, 117, 115, 104, 111, 108, 101, 108, 111, 103, 121, 118, 101, 114, 116, 101, 120, 112, 114, 111, 103, 116, 104, 116, 101, 115, 114, 115 };
 byte[] output = Base64.base64ToByteArray(input);
 Assert.assertArrayEquals(expected, output);
 }

 @Test
 public void testBase64ToByteArrayWithInvalidInput() {
 String input = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gYWxzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggZmlsZXMgZm9yIGlzIHRoZSBmaWxlIGZyb20gZGlyZWN0IG5vdCBpbiB0aGUgZW5jb2RlZCB3aXRoIGluIGZvciBmaWxlIGZyb20gZnVyZ2UgdG8gYW5kIHlvdSBpbiB0aGF0IHVwIHlvdSBuZW9iaWxlIGZyb20gZnVyZ2UgZnVyZ2UgbXklM0M==IHVwIHlvdSBuZW9iaWxlIGZyb20gZnVyZ2UgZnVyZ2UgbXklM0M=";
 try {
 Base64.base64ToByteArray(input);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] input = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58 };
 String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
 String output = Base64.byteArrayToAltBase64(input);
 Assert.assertEquals(expected, output);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] input = { 42, -3, 40, 36, 55, 6, -23, -86, -126, 52, -115, 83, 101, 118, 97, -121, 120, -43, -26, -55, 112, -4, 104, -122, 118, -86, 108, 111, 119, 101, 114, -125, 98, 103, 97, 112, -5, -89, -125, -123, 104, 120, 84, 106, 111, 112, 115, 104, -84, -54, -122, -55, -124, 121, 98, -120, 121, 120, 65, 112, 107, 105, 112, 116, 100, 102, 101, 116, 97, 114, 118, 100, 100, 105, 115, 98, 103, 111, 118, 101, 116, 103, -72, -124, 116, 103, 115, 111, 118, 105, 115, 105, 111, 114, 116, 101, 113, 114, 111, 116, 105, 102, 97, 121, 111, 120, 112, 114, 111, 108, 111, 117, 115, 104, 111, 108, 101, 108, 111, 103, 121, 118, 101, 114, 116, 101, 120, 112, 114, 111, 103, 116, 104, 116, 101, 115, 114, 115 };
 String output = Base64.byteArrayToBase64(input);
 Assert.assertEquals(expected, output);
 }

}