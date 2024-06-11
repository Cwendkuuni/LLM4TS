package com.hf.sfm.crypt;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class Base64Test {

 private byte[] base64Data = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
 private byte[] altBase64Data = { 0x3C, (byte)0x2E, (byte)0x3C, (byte)0x2E, (byte)0x3C, (byte)0x2E, (byte)0x3C, (byte)0x2E, (byte)0x3C, (byte)0x2E, (byte)0x3C, (byte)0x2E, (byte)0x3C, (byte)0x2E, (byte)0x3C, (byte)0x2E, 0x09, 0x00, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10 };

 @Test
 public void testBase64ToByteArray() {
 String base64String = "RkVWVWxrZW1zLg==";
 byte[] result = Base64.base64ToByteArray(base64String);
 assertArrayEquals(new byte[] { 0x52, 0x65, 0x61, 0x63, 0x65, 0x68, 0x61, 0x76, 0x65, 0x72, 0x73 }, result);
 }

 @Test
 public void testAltBase64ToByteArray() {
 String altBase64String = "==z/nKDw==";
 byte[] result = Base64.altBase64ToByteArray(altBase64String);
 assertArrayEquals(altBase64Data, result);
 }

 @Test
 public void testByteArrayToBase64() {
 byte[] input = base64Data;
 String result = Base64.byteArrayToBase64(input);
 assertEquals("RkVWVWxrZW1zLg==", result);
 }

 @Test
 public void testByteArrayToAltBase64() {
 byte[] input = altBase64Data;
 String result = Base64.byteArrayToAltBase64(input);
 assertEquals("==z/nKDw==", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidBase64String() {
 Base64.base64ToByteArray("abcdefg");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidAltBase64String() {
 Base64.altBase64ToByteArray("abcd");
 }
}