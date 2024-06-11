package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expected = new byte[]{-65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65};
        byte[] result = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(expected, result);

        // Edge case: empty string
        String emptyInput = "";
        byte[] emptyResult = Base64.altBase64ToByteArray(emptyInput);
        Assert.assertArrayEquals(new byte[0], emptyResult);

        // Error case: null input
        Assert.assertThrows(NullPointerException.class, () -> Base64.altBase64ToByteArray(null));

        // Error case: invalid character
        String invalidInput = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/x";
        Assert.assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(invalidInput));
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        byte[] expected = new byte[]{42, 50, 53, 44, 52, 45, 51, 56, 48, 49, 44, 41, 56, 49, 52, 52, 55, 52, 50, 51, 54, 52, 51, 50, 48, 51, 50, 52, 49, 53, 54, 52, 50, 53, 54, 55, 52};
        byte[] result = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(expected, result);

        // Edge case: empty string
        String emptyInput = "";
        byte[] emptyResult = Base64.base64ToByteArray(emptyInput);
        Assert.assertArrayEquals(new byte[0], emptyResult);

        // Error case: null input
        Assert.assertThrows(NullPointerException.class, () -> Base64.base64ToByteArray(null));

        // Error case: invalid character
        String invalidInput = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=x";
        Assert.assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(invalidInput));
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = new byte[]{-65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65};
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String result = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expected, result);

        // Edge case: empty byte array
        byte[] emptyInput = new byte[0][];
        String emptyResult = Base64.byteArrayToAltBase64(emptyInput);
        Assert.assertEquals("", emptyResult);

        // Error case: null input
        Assert.assertThrows(NullPointerException.class, () -> Base64.byteArrayToAltBase64(null));
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = new byte[]{42, 50, 53, 44, 52, 45, 51, 56, 48, 49, 44, 41, 56, 49, 52, 52, 55, 52, 50, 51, 54, 52, 51, 50, 48, 51, 50, 52, 49, 53, 54, 52, 50, 53, 54, 55, 52};
        String expected = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        String result = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expected, result);

        // Edge case: empty byte array
        byte[] emptyInput = new byte[0][];
        String emptyResult = Base64.byteArrayToBase64(emptyInput);
        Assert.assertEquals("", emptyResult);

        // Error case: null input
        Assert.assertThrows(NullPointerException.class, () -> Base64.byteArrayToBase64(null));
    }

    @Test
    public void testRandomData() {
        Random random = new Random();
        byte[] input = new byte[1024];
        random.nextBytes(input);
        String result = Base64.byteArrayToBase64(input);
        byte[] decoded = Base64.base64ToByteArray(result);
        Assert.assertArrayEquals(input, decoded);
    }
}