package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

import static com.hf.sfm.crypt.Base64.*;

public class Base64Test {

 private static final String TEST_BASE64 = "RG9lMjEwMCwK";
 private static final String TEST_ALT_BASE64 = "VGpzYwplY3Rpb24=";
 private static final byte[] TEST_BYTE_ARRAY = {0, 1, 2, 3, 4, 5, 6, 7};

 @Test
 public void testBase64ToByteArray() {
 byte[] result = base64ToByteArray(TEST_BASE64);
 Assert.assertArrayEquals(TEST_BYTE_ARRAY, result);
 }

 @Test
 public void testAltBase64ToByteArray() {
 byte[] result = altBase64ToByteArray(TEST_ALT_BASE64);
 Assert.assertArrayEquals(TEST_BYTE_ARRAY, result);
 }

 @Test
 public void testByteArrayToBase64() {
 String result = byteArrayToBase64(TEST_BYTE_ARRAY);
 Assert.assertEquals(TEST_BASE64, result);
 }

 @Test
 public void testByteArrayToAltBase64() {
 String result = byteArrayToAltBase64(TEST_BYTE_ARRAY);
 Assert.assertEquals(TEST_ALT_BASE64, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStringLengthMustBeMultipleOfFour() {
 String invalidString = "123";
 byte[] result = altBase64ToByteArray(invalidString);
 }

 @Test
 public void testNegativeInputs() {
 // Base64._$23169 and _$23168 are initialized with -1 in the class
 // So, negative inputs should be treated as illegal.
 String negativeTestString = "abc-";
 Assert.assertThrows(IllegalArgumentException.class, () -> altBase64ToByteArray(negativeTestString));
 }
}