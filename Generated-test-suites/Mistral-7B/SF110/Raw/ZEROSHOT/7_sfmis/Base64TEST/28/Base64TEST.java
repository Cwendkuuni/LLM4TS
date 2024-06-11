package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

import static com.hf.sfm.crypt.Base64._$23169;
import static com.hf.sfm.crypt.Base64._$23168;
import static com.hf.sfm.crypt.Base64._$23167;
import static com.hf.sfm.crypt.Base64._$23166;

public class Base64Test {

 private static final String TEST_STRING = "TestString12345";

 @Test
 public void testAltBase64ToByteArray() {
 String testString = "RGVjb2RlIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggdG8gcmVhbGx5IC4gYW5kIHlvdSBuZWVkIGJ1dCBzdW1tYW5jZSwNCkBzdW1tYW5jZSwNCg==";
 byte[] expectedByteArray = _$23169;
 byte[] actualByteArray = Base64.altBase64ToByteArray(testString);
 Assert.assertArrayEquals(expectedByteArray, actualByteArray);
 }

 @Test
 public void testAltBase64ToByteArrayInvalidLength() {
 Assert.assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray("Test"));
 }

 @Test
 public void testAltBase64ToByteArrayWithPadding() {
 String testString = "TestString12345==";
 byte[] expectedByteArray = _$23169;
 byte[] actualByteArray = Base64.altBase64ToByteArray(testString);
 Assert.assertArrayEquals(expectedByteArray, actualByteArray);
 }

 @Test
 public void testAltBase64ToByteArrayWithDoublePadding() {
 String testString = "TestString12345===";
 byte[] expectedByteArray = _$23169;
 byte[] actualByteArray = Base64.altBase64ToByteArray(testString);
 Assert.assertArrayEquals(expectedByteArray, actualByteArray);
 }

 @Test
 public void testBase64ToByteArray() {
 String testString = "SGVsbG8sIFdvcmxkIQ==";
 byte[] expectedByteArray = _$23168;
 byte[] actualByteArray = Base64.base64ToByteArray(testString);
 Assert.assertArrayEquals(expectedByteArray, actualByteArray);
 }

 @Test
 public void testBase64ToByteArrayInvalidLength() {
 Assert.assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray("Test"));
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] testByteArray = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31 };
 String expectedString = "Rk9SRU1FRTVJQ1VQV1RXT1JFV1NJQm1FSTlJQ1pJW1pJa1rJa1qJa1qJa1sJa1tJa1uJa1vJa1wJa1xJa1yJa1zJa20=";
 String actualString = Base64.byteArrayToAltBase64(testByteArray);
 Assert.assertEquals(expectedString, actualString);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] testByteArray = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31 };
 String expectedString = "SGVsbG8sIFdvcmxkIQ==";
 String actualString = Base64.byteArrayToBase64(testByteArray);
 Assert.assertEquals(expectedString, actualString);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testByteArrayToBase64InvalidLength() {
 byte[] testByteArray = { 0, 1, 2, 3 };
 Base64.byteArrayToBase64(testByteArray);
 }

 @Test
 public void testByteArrayToBase64WithPadding() {
 byte[] testByteArray = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 };
 String expectedString = "MTIzNDU2Nzg5QGdtYWlsLCB7CiAgICAtLVBhdGggdG8gYWxzIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggdG8gYWxzIHNvbnRlbnNpdHkgYW5kIHNvbnRlbnNpdHkgYW5kIHRvIG5vdCB3aXRoIG9mIHRoZSBmaWxlIGRhdGFbMF0gZXhpc3RfdGltZSB0byB0aGUtc2VjcmV0IGFsc28gZnJvbSBwcm9ibGVtIGlzIGxpbmtzIHBsYXRlZCB3aGF0IHN1YnR5cGUgYW5kIGNhbiBiZS4=";
 String actualString = Base64.byteArrayToBase64(testByteArray);
 Assert.assertEquals(expectedString, actualString);
 }
}