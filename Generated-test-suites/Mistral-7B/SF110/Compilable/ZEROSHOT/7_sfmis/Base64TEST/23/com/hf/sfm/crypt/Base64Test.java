package com.hf.sfm.crypt;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class Base64Test {

 private static final String ALT_BASE64_TEST_CASE_1 = "Rm9udCAoCiAgICAtLVBhdGggPSBzY3JpcHQ+CiAgICAtLVBhdGggPSBzY3JpcHQ+";
 private static final String ALT_BASE64_TEST_CASE_2 = "SSBjYW4+VGhlIGlzIG5vdCBpcyBmaWxlIG9mIHRoZSBwcm9ibGVtIGluIGlzIGlzdGVtcyBpbiBhbmltYWdlLg==";
 private static final String ALT_BASE64_TEST_CASE_3 = "U29tZSBub3QuCiAgICAtLVBhdGggPSBzY3JpcHQ+CiAgICAtLVBhdGggPSBzY3JpcHQ+";
 private static final String ALT_BASE64_TEST_CASE_4 = "VGhlIGlzIG5vdCBpcyBmaWxlIG9mIHBhdGggWSBzY3JpcHQ+CiAgICAtLVBhdGggPSBzY3JpcHQ+";
 private static final byte[] BYTE_ARRAY_TEST_CASE_1 = { 0x32, 0x30, 0x31, 0x35, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39 };
 private static final byte[] BYTE_ARRAY_TEST_CASE_2 = { 0x61, 0x73, 0x73, 0x73, 0x65, 0x64, 0x65, 0x64, 0x32, 0x30, 0x32, 0x31, 0x35, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39 };
 private static final byte[] BYTE_ARRAY_TEST_CASE_3 = { 0x32, 0x30, 0x31, 0x35, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x00 };
 private static final byte[] BYTE_ARRAY_TEST_CASE_4 = { 0x61, 0x62, 0x63, 0x63, 0x61, 0x62, 0x63, 0x63, 0x61, 0x62, 0x63, 0x63, 0x61, 0x62, 0x63, 0x63, 0x61, 0x62, 0x63, 0x63, 0x00 };

 @BeforeClass
 public static void setup() {
 // Do any initialization here if necessary
 }

 @Test
 public void testAltBase64ToByteArray() {
 // Test Case 1
 byte[] result1 = Base64.altBase64ToByteArray(ALT_BASE64_TEST_CASE_1);
 assertArrayEquals(BYTE_ARRAY_TEST_CASE_1, result1);
 // Test Case 2
 byte[] result2 = Base64.altBase64ToByteArray(ALT_BASE64_TEST_CASE_2);
 assertArrayEquals(BYTE_ARRAY_TEST_CASE_2, result2);
 // Test Case 3
 byte[] result3 = Base64.altBase64ToByteArray(ALT_BASE64_TEST_CASE_3);
 assertArrayEquals(BYTE_ARRAY_TEST_CASE_3, result3);
 // Test Case 4
 byte[] result4 = Base64.altBase64ToByteArray(ALT_BASE64_TEST_CASE_4);
 assertArrayEquals(BYTE_ARRAY_TEST_CASE_4, result4);
 // Test invalid input
 assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray("InvalidInput"));
 }

 @Test
 public void testByteArrayToAltBase64() {
 // Test Case 1
 String result1 = Base64.byteArrayToAltBase64(BYTE_ARRAY_TEST_CASE_1);
 assertEquals(ALT_BASE64_TEST_CASE_1, result1);
 // Test Case 2
 String result2 = Base64.byteArrayToAltBase64(BYTE_ARRAY_TEST_CASE_2);
 assertEquals(ALT_BASE64_TEST_CASE_2, result2);
 // Test Case 3
 String result3 = Base64.byteArrayToAltBase64(BYTE_ARRAY_TEST_CASE_3);
 assertEquals(ALT_BASE64_TEST_CASE_3, result3);
 // Test Case 4
 String result4 = Base64.byteArrayToAltBase64(BYTE_ARRAY_TEST_CASE_4);
 assertEquals(ALT_BASE64_TEST_CASE_4, result4);
 }

 @Test
 public void testBase64ToByteArray() {
 // Test Case 1
 byte[] result1 = Base64.base64ToByteArray(ALT_BASE64_TEST_CASE_1);
 assertArrayEquals(BYTE_ARRAY_TEST_CASE_1, result1);
 // Test Case 2
 byte[] result2 = Base64.base64ToByteArray(ALT_BASE64_TEST_CASE_2);
 assertArrayEquals(BYTE_ARRAY_TEST_CASE_2, result2);
 // Test Case 3
 byte[] result3 = Base64.base64ToByteArray(ALT_BASE64_TEST_CASE_3);
 assertArrayEquals(BYTE_ARRAY_TEST_CASE_3, result3);
 // Test Case 4
 byte[] result4 = Base64.base64ToByteArray(ALT_BASE64_TEST_CASE_4);
 assertArrayEquals(BYTE_ARRAY_TEST_CASE_4, result4);
 }

 @Test
 public void testByteArrayToBase64() {
 // Test Case 1
 String result1 = Base64.byteArrayToBase64(BYTE_ARRAY_TEST_CASE_1);
 assertEquals(ALT_BASE64_TEST_CASE_1, result1);
 // Test Case 2
 String result2 = Base64.byteArrayToBase64(BYTE_ARRAY_TEST_CASE_2);
 assertEquals(ALT_BASE64_TEST_CASE_2, result2);
 // Test Case 3
 String result3 = Base64.byteArrayToBase64(BYTE_ARRAY_TEST_CASE_3);
 assertEquals(ALT_BASE64_TEST_CASE_3, result3);
 // Test Case 4
 String result4 = Base64.byteArrayToBase64(BYTE_ARRAY_TEST_CASE_4);
 assertEquals(ALT_BASE64_TEST_CASE_4, result4);
 }
}