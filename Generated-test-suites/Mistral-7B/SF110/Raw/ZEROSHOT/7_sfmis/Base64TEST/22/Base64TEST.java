package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

import java.util.ByteArrayOutputStream;
import java.util.Base64; // For testing purpose only

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "RGVjbVlsIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggaHRlbXMgZW1haWxfdGltZXMgYW4gYSBzaXplIGFsc28gdG8gYW5kIGluIGRhdGFbMF0gZXhhbXBsZSBoYW5kIGZyb20gdG8gdGhlIHBsYW5ldC4=";
        byte[] expectedOutput = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57};
        byte[] output = Base64.altBase64ToByteArray(input);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(output, 0, output.length);
        byte[] comparisonOutput = baos.toByteArray();
        Assert.assertArrayEquals(expectedOutput, comparisonOutput);
    }

    @Test
    public void testAltBase64ToByteArrayWithMalformedInput() {
        String input = "RGVjbVlsIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggaHRlbXMgZW1haWxfdGltZXMgYW4gYSBzaXplIGFsc28gdG8gYW5kIGluIGRhdGFbMF0gZXhhbXBsZSBoYW5kIGZyb20gdG8gdGhlIHBsYW5ldC4=x";
        Assert.assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8gV29ybGQh";
        byte[] expectedOutput = {-67, -66, -65, -63, -61, -66, -66, -65, -66, -63, -61, -63, -68, 65, -61, 64, -62, 65, -62, 61, -63, -68, -63, 63, -62, 62, 65, 64};
        byte[] output = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testBase64ToByteArrayWithMalformedInput() {
        String input = "SGVsbG8gV29ybGQhX";
        Assert.assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(input));
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57};
        String expectedOutput = "Rk9SRU1FQVRJT0dFQVRJUElURFdFQVRJVGhlIG1pbnMgZW1haWxfdGltZXMgYW4gYSBzaXplIHBsYW5ldC4=";
        String output = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = {-67, -66, -65, -63, -61, -66, -66, -65, -66, -63, -61, -63, -68, 65, -61, 64, -62, 65, -62, 61, -63, -68, -63, 63, -62, 62, 65, 64};
        String expectedOutput = "SGVsbG8gV29ybGQh";
        String output = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expectedOutput, output);
    }

}