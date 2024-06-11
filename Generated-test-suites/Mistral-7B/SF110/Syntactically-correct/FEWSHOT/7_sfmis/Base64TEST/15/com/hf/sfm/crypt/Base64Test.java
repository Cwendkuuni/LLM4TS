package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Base64Test {

 private byte[] getTestData(String testString) {
 return testString.getBytes();
 }

 @Test
 public void testAltBase64ToByteArray() {
 String testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
 byte[] expectedByteArray = getTestData(testString);
 byte[] resultByteArray = Base64.altBase64ToByteArray(testString);
 assertArrayEquals(expectedByteArray, resultByteArray);
 }

 @Test
 public void testAltBase64ToByteArrayWithInvalidLength() {
 String testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789==";
 assertNull(Base64.altBase64ToByteArray(testString));
 }

 @Test
 public void testAltBase64ToByteArrayWithInvalidCharacters() {
 String testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!";
 assertNull(Base64.altBase64ToByteArray(testString));
 }

 @Test
 public void testBase64ToByteArray() {
 String testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
 byte[] expectedByteArray = getTestData(testString);
 byte[] resultByteArray = Base64.base64ToByteArray(testString);
 assertArrayEquals(expectedByteArray, resultByteArray);
 }

 @Test
 public void testBase64ToByteArrayWithInvalidLength() {
 String testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789==";
 assertNull(Base64.base64ToByteArray(testString));
 }

 @Test
 public void testBase64ToByteArrayWithInvalidCharacters() {
 String testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!";
 assertNull(Base64.base64ToByteArray(testString));
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] testByteArray = getTestData("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
 String expectedString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
 String resultString = Base64.byteArrayToAltBase64(testByteArray);
 assertEquals(expectedString, resultString);
 }

 @Test
 public void testByteArrayToAltBase64WithPadding() {
 byte[] testByteArray = getTestData("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567".getBytes());
 String expectedString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567";
 String resultString = Base64.byteArrayToAltBase64(testByteArray);
 assertEquals(expectedString, resultString);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] testByteArray = getTestData("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
 String expectedString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
 String resultString = Base64.byteArrayToBase64(testByteArray);
 assertEquals(expectedString, resultString);
 }

 @Test
 public void testByteArrayToBase64WithPadding() {
 byte[] testByteArray = getTestData("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567".getBytes());
 String expectedString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567";
 String resultString = Base64.byteArrayToBase64(testByteArray);
 assertEquals(expectedString, resultString);
 }
}