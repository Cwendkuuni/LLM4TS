package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = new byte[] { 1, 2, 3, 4 };
        String input = "AQIDBA==";
        byte[] result = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expected, result);

        input = "zWVwYW4=";
        result = Base64.altBase64ToByteArray(input);
        expected = new byte[] { 255, 212, 191, 225, 200, 197, 199, 194, 134, 201, 203, 205, 139, 208, 213, 214 };
        assertArrayEquals(expected, result);

        input = "QXx//jzmfjQ=";
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));

        input = "";
        assertNull(Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] expected = new byte[] { 1, 2, 3, 4 };
        String input = "AQIDBA==";
        byte[] result = Base64.base64ToByteArray(input);
        assertArrayEquals(expected, result);

        input = "ZXhhbXBsZSBmaWxlIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggZmlsbD4=";
        result = Base64.base64ToByteArray(input);
        expected = new byte[] { 1, 11, 97, 105, 118, 101, 32, 48, 99, 111, 117, 108, 101, 114, 121, 112, 112, 117, 100, 32, 56, 57, 99, 107, 101, 115, 115, 104, 105, 111, 116, 116, 101, 117, 115, 115, 32, 50, 48, 48, 51, 48, 49, 114, 111, 109, 44, 32, 51, 50, 53, 48, 48, 55, 53, 53, 44, 32, 49, 54, 48, 48, 49, 114, 111, 109, 44, 32, 57, 55, 52, 53, 53, 53, 53, 53, 53, 53, 53 };
        assertArrayEquals(expected, result);

        input = "XQ==";
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));

        input = "";
        assertNull(Base64.base64ToByteArray(input));
    }

    @Test
    public void testByteArrayToAltBase64() {
        String expected = "ZW5ndGggZW1haWxfc291cmNl";
        byte[] input = new byte[] { 255, 212, 191, 225, 200, 197, 199, 194, 134, 201, 203, 205, 139, 208, 213, 214 };
        String result = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, result);

        input = new byte[] { 1, 2, 3, 4 };
        result = Base64.byteArrayToAltBase64(input);
        expected = "AQIDBA==";
        assertEquals(expected, result);

        input = new byte[0];
        result = Base64.byteArrayToAltBase64(input);
        assertEquals("", result);

        input = new byte[] { 1 };
        result = Base64.byteArrayToAltBase64(input);
        assertEquals("A", result);
    }

    @Test
    public void testByteArrayToBase64() {
        String expected = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggZmlsbD4=";
        byte[] input = new byte[] { 1, 11, 97, 105, 118, 101, 32, 48, 99, 111, 117, 108, 101, 114, 121, 112, 112, 117, 100, 32, 56, 57, 99, 107, 101, 115, 115, 104, 105, 111, 116, 116, 101, 117, 115, 115 };
        String result = Base64.byteArrayToBase64(input);
        assertEquals(expected, result);

        input = new byte[] { 1, 2, 3, 4 };
        result = Base64.byteArrayToBase64(input);
        expected = "AQIDBA==";
        assertEquals(expected, result);

        input = new byte[0];
        result = Base64.byteArrayToBase64(input);
        assertEquals("", result);

        input = new byte[] { 1 };
        result = Base64.byteArrayToBase64(input);
        assertEquals("A", result);
    }
}