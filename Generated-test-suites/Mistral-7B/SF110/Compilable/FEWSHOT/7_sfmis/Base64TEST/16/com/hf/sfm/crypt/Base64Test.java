package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = new byte[]{-65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, 60, 61, 62, -65, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 63, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, 6, 7, 8, 9};
        byte[] input = "AAAAAAAAAAAAAAAAAAA==".getBytes();
        assertArrayEquals(expected, Base64.altBase64ToByteArray(new String(input)));
    }

    @Test
    public void testAltBase64ToByteArrayWithInvalidLength() {
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray("A="));
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray("AA="));
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray("AAA="));
    }

    @Test
    public void testAltBase64ToByteArrayWithNullInput() {
        assertNull(Base64.altBase64ToByteArray(null));
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] expected = new byte[]{-65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, 62, -65, -65, -65, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 63, 63, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, 6, 7, 8, 9};
        byte[] input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789==".getBytes();
        assertArrayEquals(expected, Base64.base64ToByteArray(new String(input)));
    }

    @Test
    public void testBase64ToByteArrayWithInvalidLength() {
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray("ABC="));
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789= "));
    }

    @Test
    public void testBase64ToByteArrayWithNullInput() {
        assertNull(Base64.base64ToByteArray(null));
    }

    @Test
    public void testByteArrayToAltBase64() {
        String expected = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmVhbGx5IGZvdWwgdGhpbmdzIHJlc2lkZW50aWZpZXc";
        byte[] input = new byte[]{6, 7, 8, 9};
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
    }

    @Test
    public void testByteArrayToBase64() {
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        byte[] input = new byte[]{-65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, 62, -65, -65, -65, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 63, 63, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, 6};
        assertEquals(expected, Base64.byteArrayToBase64(input));
    }

    @Test
    public void testByteArrayToBase64WithNullInput() {
        assertNull(Base64.byteArrayToBase64(null));
    }
}