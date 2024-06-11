package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

import static com.hf.sfm.crypt.Base64._$23169;
import static com.hf.sfm.crypt.Base64._$23168;
import static com.hf.sfm.crypt.Base64._$23167;
import static com.hf.sfm.crypt.Base64._$23166;

public class Base64Test {

 private byte[] getExpectedByteArray(String input) {
 byte[] expected = new byte[input.length() / 4 * 3];
 int index = 0;
 for (int i = 0; i < input.length(); i += 4) {
 int b1 = _$23183(input.charAt(i), _$23169);
 int b2 = _$23183(input.charAt(i + 1), _$23169);
 int b3 = _$23183(input.charAt(i + 2), _$23169);
 int b4 = _$23183(input.charAt(i + 3), _$23169);
 expected[index++] = (byte) ((b1 << 2) | (b2 >> 4));
 expected[index++] = (byte) ((b2 << 4) | (b3 >> 2));
 expected[index++] = (byte) ((b3 << 6) | b4);
 }
 return expected;
 }

 @Test
 public void testAltBase64ToByteArray() {
 String testData = "Zm9vMDpTc3RhbnQgQnJlYXQgYW5kIGlzIGtleSBuZWVkIGZvciBhbGx5IGZvdWwgdG8gYW5kIHNlcnZpY2U=";
 byte[] expected = getExpectedByteArray(testData);
 byte[] actual = Base64.altBase64ToByteArray(testData);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAltBase64ToByteArrayInvalidData1() {
 Base64.altBase64ToByteArray("hello");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAltBase64ToByteArrayInvalidData2() {
 Base64.altBase64ToByteArray("hello==");
 }

 @Test
 public void testAltBase64ToByteArrayValidData2() {
 String testData = "SGVsbG8sIFdvcmxkIQ==";
 byte[] expected = new byte[] { 1, 0, 1, 2, 0, 1, 3, 1, 3, 1, 1, 4, 1, 1, 1, 0, 0, 0, 0, 0, 1, 2, 0, 2, 0, 2, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
 byte[] actual = Base64.altBase64ToByteArray(testData);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testBase64ToByteArray() {
 String testData = "SGVsbG8sIHdvcmxkIQ==";
 byte[] expected = getExpectedByteArray(testData);
 byte[] actual = Base64.base64ToByteArray(testData);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testBase64ToByteArrayValidData1() {
 String testData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdGhpbmdzIHVwIHlvdSBmb3IgY29tcHV0ZXIuCiAgICAtLVBhdGggYW5kIHlvdSBmb3IgY29tcHV0ZXIu";
 byte[] actual = Base64.base64ToByteArray(testData);
 byte[] expected = new byte[] { -30, -123, -119, 101, -122, -101, 111, -113, -107, 97, 99, -111, 110, 97, -117, 108, 111, 99, -117, 110, 100, 111, 118, 101, 114, 103, 114, 111, 101, -116, -122, 105, -121, -118, -111, -116, 111, 118, 101, 99, 111, 108, 97, -117, 108, 99, -117, 108, 111, -118, 101, 99, 111, 109, 111, 107, 115, 101, 115, 116, 101 };
 Assert.assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBase64ToByteArrayInvalidData1() {
 Base64.base64ToByteArray("hello");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBase64ToByteArrayInvalidData2() {
 Base64.base64ToByteArray("hello==");
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] testData = new byte[] { 1, 0, 1, 2, 0, 1, 3, 1, 3, 1, 1, 4, 1, 1, 1, 0, 0, 0, 0, 0, 1, 2, 0, 2, 0, 2, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
 String expected = "Zm9vMDpTc3RhbnQgQnJlYXQgYW5kIGlzIGtleSBuZWVkIGZvciBhbGx5IGZvdWwgdG8gYW5kIHNlcnZpY2U=";
 String actual = Base64.byteArrayToAltBase64(testData);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] testData = new byte[] { 1, 0, 1, 2, 0, 1, 3, 1, 3, 1, 1, 4, 1, 1, 1, 0, 0, 0, 0, 0, 1, 2, 0, 2, 0, 2, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
 String expected = "SGVsbG8sIHdvcmxkIQ==";
 String actual = Base64.byteArrayToBase64(testData);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testByteArrayToBase64ValidData1() {
 byte[] testData = new byte[] { -30, -123, -119, 101, -122, -101, 111, -113, -107, 97, 99, -111, 110, 97, -117, 108, 111, 99, -117, 110, 100, 111, 118, 101, 114, 103, 114, 111, 101, -116, -122, 105, -121, -118, -111, -116, 111, 118, 101, 99, 111, 108, 97, -117, 108, 99, -117, 108, 111, -118, 101, 99, 111, 109, 111, 107, 115, 101, 115, 116, 101 };
 String expected = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdGhpbmdzIHVwIHlvdSBmb3IgY29tcHV0ZXIuCiAgICAtLVBhdGggYW5kIHlvdSBmb3IgY29tcHV0ZXIu";
 String actual = Base64.byteArrayToBase64(testData);
 Assert.assertEquals(expected, actual);
 }

}