package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

import static com.hf.sfm.crypt.Base64._$23169;
import static com.hf.sfm.crypt.Base64._$23168;
import static com.hf.sfm.crypt.Base64._$23167;
import static com.hf.sfm.crypt.Base64._$23166;

public class Base64Test {

 private static final String TEST_STRING = "TestString1234567890";

 @Test
 public void testAltBase64ToByteArray() {
 String testString = "RnJvbSBzYXlzdGVtcyBpbiBwYXlzIGZvdWwgdG8gZXhpZG5hbWU=";
 byte[] expectedByteArray = _$23169;
 byte[] resultByteArray = Base64.altBase64ToByteArray(testString);
 Assert.assertArrayEquals(expectedByteArray, resultByteArray);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAltBase64ToByteArrayInvalidLength() {
 String testString = "TestString";
 Base64.altBase64ToByteArray(testString);
 }

 @Test
 public void testBase64ToByteArray() {
 String testString = "SGVsbG8sIFdvcmxkIQ==";
 byte[] expectedByteArray = _$23169;
 byte[] resultByteArray = Base64.base64ToByteArray(testString);
 Assert.assertArrayEquals(expectedByteArray, resultByteArray);
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] testByteArray = new byte[]{"T", "e", "s", "t", "S", "t", "r", "i", "n", "g", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
 String expectedString = "VGhyb3VnaG5lcw==";
 String resultString = Base64.byteArrayToAltBase64(testByteArray);
 Assert.assertEquals(expectedString, resultString);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] testByteArray = new byte[]{"T", "e", "s", "t", "S", "t", "r", "i", "n", "g", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
 String expectedString = "SGVsbG8sIFdvcmxkIQ==";
 String resultString = Base64.byteArrayToBase64(testByteArray);
 Assert.assertEquals(expectedString, resultString);
 }

 @Test
 public void testInvalidBase64Character() {
 String testString = "SGVsbG8sIFdvcmxkIQ==x";
 Assert.assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(testString));
 }

 @Test
 public void testInvalidInputToAltBase64() {
 String testString = "TestString1234567890=";
 byte[] expectedByteArray = _$23169;
 byte[] resultByteArray = Base64.altBase64ToByteArray(testString);
 Assert.assertArrayEquals(expectedByteArray, resultByteArray);
 }

 @Test
 public void testBase64ToByteArrayWithPadding() {
 String testString = "SGVsbG8sIFdvcmxkIQ==";
 byte[] expectedByteArray = new byte[]{56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 61, 61, 61, 61};
 byte[] resultByteArray = Base64.base64ToByteArray(testString);
 Assert.assertArrayEquals(expectedByteArray, resultByteArray);
 }

 @Test
 public void testAltBase64ToByteArrayWithShortLength() {
 String testString = "RnJvbSBz";
 byte[] expectedByteArray = new byte[]{72, 73, 83, 76};
 byte[] resultByteArray = Base64.altBase64ToByteArray(testString);
 Assert.assertArrayEquals(expectedByteArray, resultByteArray);
 }

 @Test
 public void testAltBase64ToByteArrayWithShortLengthNoPadding() {
 String testString = "RnJvbSBz";
 byte[] expectedByteArray = new byte[]{72, 73, 83, 76};
 byte[] resultByteArray = Base64.altBase64ToByteArray(testString, false);
 Assert.assertArrayEquals(expectedByteArray, resultByteArray);
 }
}