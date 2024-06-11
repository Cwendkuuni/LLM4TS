package com.hf.sfm.crypt.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.hf.sfm.crypt.Base64;

public class Base64Test {

 private byte[] getTestData() {
 return new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
 }

 @Test
 public void testAltBase64ToByteArray() {
 String testString = "ZXhhbXBsZSBkYXRhIGlzIG15IGZvdWwgdG8gcmVhbGx5IGZvdWw=";
 byte[] expectedResult = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
 byte[] result = Base64.altBase64ToByteArray(testString);
 assertArrayEquals(expectedResult, result);
 }

 @Test
 public void testAltBase64ToByteArrayWithInvalidStringLength() {
 String testString = "ZXhhbXBsZSBkYXRhIGlzIG15IGZvdWwgdG8gcmVhbGx5IGZvdWw=";
 assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(testString));
 }

 @Test
 public void testAltBase64ToByteArrayWithInvalidCharacter() {
 String testString = "ZXhhbXBsZSBkYXRhIGlzIG15IGZvdWwgdG8gcmVhbGx5IGZvdWw%";
 assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(testString));
 }

 @Test
 public void testBase64ToByteArray() {
 String testString = "ZXhhbXBsZSBkYXRhIGlzIG15IGZvdWwgdG8gcmVhbGx5IGZvdWw=";
 byte[] expectedResult = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
 byte[] result = Base64.base64ToByteArray(testString);
 assertArrayEquals(expectedResult, result);
 }

 @Test
 public void testBase64ToByteArrayWithInvalidStringLength() {
 String testString = "ZXhhbXBsZSBkYXRhIGlzIG15IGZvdWwgdG8gcmVhbGx5IGZvdWw=";
 byte[] result = Base64.base64ToByteArray("ZXhhbXBsZSBkYXRhIGlzIG15IGZvdWwgdG8gcmVhbGx5IGZvdWw");
 assertArrayEquals(result, Base64.base64ToByteArray(testString));
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] testData = getTestData();
 String expectedResult = "Zg==";
 String result = Base64.byteArrayToAltBase64(testData);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testByteArrayToAltBase64WithExtraBytes() {
 byte[] testData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 };
 String expectedResult = "Zg==";
 String result = Base64.byteArrayToAltBase64(testData, 0, 3 * testData.length / 4);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] testData = getTestData();
 String expectedResult = "ZXhhbXBsZSBkYXRhIGlzIG15IGZvdWwgdG8gcmVhbGx5IGZvdWw=";
 String result = Base64.byteArrayToBase64(testData);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testByteArrayToBase64WithExtraBytes() {
 byte[] testData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 };
 String expectedResult = "ZXhhbXBsZSBkYXRhIGlzIG15IGZvdWwgdG8gcmVhbGx5";
 String result = Base64.byteArrayToBase64(testData, 0, 3 * testData.length / 4);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testByteArrayToBase64WithInvalidLength() {
 byte[] testData = new byte[] { 0x01, 0x02, 0x03 };
 String result = Base64.byteArrayToBase64(testData);
 }

 @Test
 public void testBase64ToByteArrayWithInvalidBase64String() {
 assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray("ZXhhbXBsZSBkYXRhIGlzIG15IGZvdWwX"));
 }

}