package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] testBytes = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, -1, 62, 9, 10, 11, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 12, 13, 14, -1, 15, 63, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, -1, 18, 19, 21, 20, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 22, 23, 24, 25, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90};
        String testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] result = Base64.altBase64ToByteArray(testString);
        assertArrayEquals(testBytes, result);

        testString = "";
        assertNull(Base64.altBase64ToByteArray(testString));

        testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        result = Base64.altBase64ToByteArray(testString);
        assertArrayEquals(testBytes, result);

        testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".substring(0, testString.length() - 1);
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(testString));
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] testBytes = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
        String testString = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwgYW5kIFRoZSBmaWxlIGRheSBmcm9tIGRheSBwbGVhc2UgYmVsb3B5IG9mIHRoZSBzYW5jZWQgdGhlIGlzIGxpa2UgYWxzbyBkYXRhIG5vdCB5b3VyIG5vdCB0cmFuc2xhdGlvbnMgYW5kIHBvc2l0aW9u";
        byte[] result = Base64.base64ToByteArray(testString);
        assertArrayEquals(testBytes, result);

        testString = "";
        assertNull(Base64.base64ToByteArray(testString));

        testString = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwgYW5kIFRoZSBmaWxlIGRheSBmcm9tIGRheSBwbGVhc2UgYmVsb3B5IG9mIHRoZSBzYW5jZWQgdGhlIGlzIGxpa2UgYWxzbyBkYXRhIG5vdCB5b3VyIG5vdCB0cmFuc2xhdGlvbnMgYW5kIHBvc2l0aW9u";
        result = Base64.base64ToByteArray(testString);
        assertArrayEquals(testBytes, result);

        testString = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwgYW5kIFRoZSBmaWxlIGRheSBmcm9tIGRheSBwbGVhc2UgYmVsb3B5IG9mIHRoZSBzYW5jZWQgdGhlIGlzIGxpa2UgYWxzbyBkYXRhIG5vdCB5b3VyIG5vdCB0cmFuc2xhdGlvbnMgYW5kIHBvc2l0aW9u".substring(0, testString.length() - 1);
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(testString));
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] testBytes = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, -1, 62, 9, 10, 11, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 12, 13, 14, -1, 15, 63, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, -1, 18, 19, 21, 20, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
        String testString = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        String result = Base64.byteArrayToAltBase64(testBytes);
        assertEquals(testString, result);

        testBytes = new byte[]{0};
        result = Base64.byteArrayToAltBase64(testBytes);
        assertEquals("", result);

        testBytes = new byte[]{-1, -1};
        assertNull(Base64.byteArrayToAltBase64(testBytes));
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] testBytes = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, -1, 62, 9, 10, 11, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 12, 13, 14, -1, 15, 63, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, -1, 18, 19, 21, 20, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
        String testString = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwgYW5kIFRoZSBmaWxlIGRheSBmcm9tIGRheSBwbGVhc2UgYmVsb3B5IG9mIHRoZSBzYW5jZWQgdGhlIGlzIGxpa2UgYWxzbyBkYXRhIG5vdCB5b3VyIG5vdCB0cmFuc2xhdGlvbnMgYW5kIHBvc2l0aW9u";
        String result = Base64.byteArrayToBase64(testBytes);
        assertEquals(testString, result);

        testBytes = new byte[]{0};
        result = Base64.byteArrayToBase64(testBytes);
        assertEquals("AQIDBA==", result);

        testBytes = new byte[]{-1, -1};
        assertNull(Base64.byteArrayToBase64(testBytes));
    }
}