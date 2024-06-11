package com.hf.sfm.crypt;

import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

 private byte[] expectedByteArray;
 private String expectedBase64String;
 private String inputString;

 @Test
 public void testAltBase64ToByteArray() {
 inputString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/\r\n";
 expectedByteArray = new byte[] { 76, 65, 63, 68, 65, 84, 70, 65, 84, 69, 67, 68, 69, 73, 81, 62, 65, 63, 72, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63 };

 byte[] testByteArray = Base64.altBase64ToByteArray(inputString);
 assertArrayEquals(expectedByteArray, testByteArray);
 }

 @Test
 public void testBase64ToByteArray() {
 inputString = "SGVsbG8gV29ybGQh";
 expectedByteArray = new byte[] { 83, 115, 103, 111, 99, 107 };

 byte[] testByteArray = Base64.base64ToByteArray(inputString);
 assertArrayEquals(expectedByteArray, testByteArray);
 }

 @Test
 public void testByteArrayToAltBase64() {
 expectedBase64String = "Um9sbG93aWNpYWxfaWQ+JWFjaXMtYWQvdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSIlMDkgMzggMCwgMywgMjw0IEwgMjAwIEwgMTAwIEwgMzAwIEwgMTIwIEwgMjMgTCAxMCAwIDAgb2Jq";
 byte[] testByteArray = new byte[] { -115, -101, -118, -101, -114, -121, -116, -116, -113, -99, 32, 105, 97, -111, 110, 121, 112, 121, 100, 33, 103, 111, 111, 117, 101, 100, 32, 105, 110, 33, 116, 32, 104, 116, 116, 104, 101, 114, 111, 119, 110, 116, 32, 104, 101, 114, 111, 114, 101, 100, 105, 111, 117, 105, 103, 104, 32, 111, 119, 111, 111, 118, 101, 114, 116, 101, 115, 115, 111, 110, 33, 102, 111, 111, 32, 111, 111, 117, 115, 115, 115, 111, 117, 115, 101, 116, 111, 117, 115, 115, 111, 32, 105, 110, 33 };

 String testString = Base64.byteArrayToAltBase64(testByteArray);
 assertEquals(expectedBase64String, testString);
 }

 @Test
 public void testByteArrayToBase64() {
 expectedBase64String = "SGVsbG8gV29ybGQh";
 byte[] testByteArray = new byte[] { 83, 115, 103, 111, 99, 107 };

 String testString = Base64.byteArrayToBase64(testByteArray);
 assertEquals(expectedBase64String, testString);
 }
}