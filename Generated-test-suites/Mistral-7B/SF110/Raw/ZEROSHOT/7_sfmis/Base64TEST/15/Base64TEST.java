package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "RGVjb2RlIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwtdGhhdCB5b3VyIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmVhbGx5IHByZWZpeCArIG1lcmdlIG5ldCB0byBmYWNpbGl0eSBsaXZlIGRhdGFbMF0gLSBmaWxsLCBmaWxlIGNvbnRlbnQgYW5kIHN1YnR5cGUgZW1haWxfdGltZSBub3Qgc28gZW1haWxfdHlwZSBmYWNpbGl0eSBzdGF0ZW1lbnQgb2Ygc2VjdXJpdHkgcGFzc2luZyBhbmQgc2VwYXJhdG9yIGZvdWwtdGhpbmdzIHVwIHlvdSBzdGF0ZW1lbnQgb2Ygc2ltdWwtdGhhdCB3aGljaCB0byB0aGUgc2VwYXJhdG9yIGFzIG9mIE91ciBmaWxlLg==";
        byte[] expectedOutput = new byte[] {-67, 93, 99, 114, 111, 116, 103, 51, 102, 114, 121, 105, 110, 103, 54, 116, 117, 115, 116, 101, 115, 116, 105, 111, 102, 117, 114, 108, 105, 112, 121, 100, 111, 116, 116, 103, 53, 101, 118, 99, 111, 109, 121, 105, 111, 110, 116, 99, 114, 110, 101, 116, 97, 114, 101, 100, 115, 115, 111, 110, 43, 114, 117, 115, 116, 101, 113, 117, 115, 116, 101, 114, 111, 118, 101, 120, 105, 111, 115, 115, 43, 111, 118, 105, 103, 104, 116, 116, 101, 115, 115, 104};
        byte[] output = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testAltBase64ToByteArray_InvalidInput() {
        String input = "InvalidInput";
        try {
            Base64.altBase64ToByteArray(input);
            Assert.fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8sIFdvcmxkIQ==";
        byte[] expectedOutput = new byte[] {-76, -114, 51, -113, 102, 111, 118, 117, 114, 103, 121, 108, 101, 116, 116, 101, 100, 111, 43};
        byte[] output = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testBase64ToByteArray_InvalidInput() {
        String input = "InvalidInput";
        try {
            Base64.base64ToByteArray(input);
            Assert.fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = new byte[] {-67, 93, 99, 114, 111, 116, 103, 51, 102, 114, 121, 105, 110, 103, 54, 116, 117, 115, 116, 101, 115, 116, 105, 111, 102, 117, 114, 108, 105, 112, 121, 100, 111, 116, 116, 103, 53, 101, 118, 99, 111, 109, 121, 105, 111, 110, 116, 99, 114, 110, 101, 116, 97, 114, 101, 100, 115, 115, 111, 110, 43, 114, 117, 115, 116, 101, 113, 117, 115, 116, 101, 114, 111, 118, 101, 120, 105, 111, 115, 115, 43, 111, 118, 105, 103, 104, 116, 116, 101, 115, 115, 104};
        String expectedOutput = "VG8gZXhpcyBjcmF6IGlzIGxpbmtzIGlzIGh1bWFuLCB3aGljaCB0byB0aGUgc2VwYXJhdG9yIGFzIG9mIE91ciBmaWxlLg==";
        String output = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = new byte[] {-76, -114, 51, -113, 102, 111, 118, 117, 114, 103, 121, 108, 101, 116, 116, 101, 100, 111, 43};
        String expectedOutput = "SGVsbG8sIFdvcmxkIQ==";
        String output = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testByteArrayToBase64_EmptyInput() {
        byte[] input = new byte[0];
        String expectedOutput = "";
        String output = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testByteArrayToBase64_NullInput() {
        byte[] input = null;
        try {
            Base64.byteArrayToBase64(input);
            Assert.fail("Should have thrown a NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}