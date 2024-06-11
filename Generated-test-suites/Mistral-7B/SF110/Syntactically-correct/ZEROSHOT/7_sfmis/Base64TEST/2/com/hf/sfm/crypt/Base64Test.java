package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class Base64Test {

 private static final String TEST_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ012345678901234567890123456789";

 @Test
 public void testAltBase64ToByteArray() {
 String testString = TEST_STRING;
 byte[] expectedByteArray = Base64._$23180(testString, true);
 byte[] actualByteArray = Base64.altBase64ToByteArray(testString);
 assertArrayEquals(expectedByteArray, actualByteArray);
 }

 @Test
 public void testAltBase64ToByteArrayWithInvalidInput() {
 String testString = "invalidInput";
 assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(testString));
 }

 @Test
 public void testBase64ToByteArray() {
 String testString = TEST_STRING;
 byte[] expectedByteArray = Base64._$23180(testString, false);
 byte[] actualByteArray = Base64.base64ToByteArray(testString);
 assertArrayEquals(expectedByteArray, actualByteArray);
 }

 @Test
 public void testBase64ToByteArrayWithInvalidInput() {
 String testString = "invalidInput";
 assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(testString));
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] testByteArray = Base64._$23180(TEST_STRING.getBytes(), true);
 String expectedAltBase64 = Base64._$23170(testByteArray, true);
 String actualAltBase64 = Base64.byteArrayToAltBase64(testByteArray);
 assertEquals(expectedAltBase64, actualAltBase64);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] testByteArray = Base64._$23180(TEST_STRING.getBytes(), true);
 String expectedBase64 = Base64._$23170(testByteArray, false);
 String actualBase64 = Base64.byteArrayToBase64(testByteArray);
 assertEquals(expectedBase64, actualBase64);
 }

 @Test
 public void testAltBase64ToByteArrayWithPadding() {
 String testString = TEST_STRING + "=";
 byte[] expectedByteArray = Base64._$23180(testString, true);
 byte[] actualByteArray = Base64.altBase64ToByteArray(testString);
 assertArrayEquals(expectedByteArray, actualByteArray);
 }

 @Test
 public void testBase64ToByteArrayWithPadding() {
 String testString = TEST_STRING + "==";
 byte[] expectedByteArray = Base64._$23180(testString, false);
 byte[] actualByteArray = Base64.base64ToByteArray(testString);
 assertArrayEquals(expectedByteArray, actualByteArray);
 }

 @Test
 public void testByteArrayToAltBase64WithPadding() {
 byte[] testByteArray = Base64.concat(Base64._$23180(TEST_STRING.getBytes(), true), new byte[]{1, 2, 3});
 String expectedAltBase64 = Base64._$23170(testByteArray, true) + "==";
 String actualAltBase64 = Base64.byteArrayToAltBase64(testByteArray);
 assertEquals(expectedAltBase64, actualAltBase64);
 }

 @Test
 public void testByteArrayToBase64WithPadding() {
 byte[] testByteArray = Base64.concat(Base64._$23180(TEST_STRING.getBytes(), true), new byte[]{1, 2, 3});
 String expectedBase64 = Base64._$23170(testByteArray, false) + "==";
 String actualBase64 = Base64.byteArrayToBase64(testByteArray);
 assertEquals(expectedBase64, actualBase64);
 }

 @Test
 public void testNullInput() {
 assertNull(Base64.altBase64ToByteArray(null));
 assertNull(Base64.base64ToByteArray(null));
 assertNull(Base64.byteArrayToAltBase64(null));
 assertNull(Base64.byteArrayToBase64(null));
 }

 private static byte[] concat(byte[] a, byte[] b) {
 byte[] result = new byte[a.length + b.length];
 System.arraycopy(a, 0, result, 0, a.length);
 System.arraycopy(b, 0, result, a.length, b.length);
 return result;
 }

}