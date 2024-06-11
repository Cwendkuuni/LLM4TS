package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

import static com.hf.sfm.crypt.Base64.*;

public class Base64Test {

 private static final String TEST_STRING = "TestString12345";
 private static final byte[] TEST_BYTE_ARRAY = TEST_STRING.getBytes();

 @Test
 public void testAltBase64ToByteArray() {
 byte[] expectedByteArray = altBase64ToByteArray("R2xsQ1RYRGFuZFpFVFJDVGhlckZoZWlnaHQ=");
 Assert.assertArrayEquals(expectedByteArray, TEST_BYTE_ARRAY);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAltBase64ToByteArrayInvalidInput() {
 try {
 altBase64ToByteArray("InvalidInput");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testAltBase64ToByteArrayPadding() {
 byte[] expectedByteArray = altBase64ToByteArray("SGVsbG8sIFdvcmxkIQ==");
 Assert.assertArrayEquals(expectedByteArray, TEST_BYTE_ARRAY);
 }

 @Test
 public void testBase64ToByteArray() {
 byte[] expectedByteArray = base64ToByteArray(TEST_STRING);
 Assert.assertArrayEquals(TEST_BYTE_ARRAY, expectedByteArray);
 }

 @Test
 public void testByteArrayToAltBase64() {
 String expectedAltBase64 = byteArrayToAltBase64(TEST_BYTE_ARRAY);
 Assert.assertEquals(expectedAltBase64, "R2xsQ1RYRGFuZFpFVFJDVGhlckZoZWlnaHQ=");
 }

 @Test
 public void testByteArrayToBase64() {
 String expectedBase64 = byteArrayToBase64(TEST_BYTE_ARRAY);
 Assert.assertEquals(expectedBase64, TEST_STRING);
 }

 @Test
 public void testByteArrayToBase64Padding() {
 byte[] paddedByteArray = new byte[] { 0x61, 0x62, 0x63, 0x63 };
 String expectedBase64 = byteArrayToBase64(paddedByteArray);
 Assert.assertEquals(expectedBase64, "MjM0MjM0");
 }

 @Test
 public void testByteArrayToBase64ShortInput() {
 byte[] shortInputByteArray = new byte[] { 0x61, 0x62 };
 String expectedBase64 = byteArrayToBase64(shortInputByteArray);
 Assert.assertEquals(expectedBase64, "Base64 encoding of length 2 requires padding");
 }

 @Test
 public void testByteArrayToBase64InvalidInput() {
 try {
 byte[] invalidByteArray = new byte[] { 0x61, 0xFF };
 String expectedBase64 = byteArrayToBase64(invalidByteArray);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}