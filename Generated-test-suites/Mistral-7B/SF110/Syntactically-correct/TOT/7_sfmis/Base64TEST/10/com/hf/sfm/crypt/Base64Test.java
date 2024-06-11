package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String base64String = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggdGhpbmtzIHN1YnR5cGUgYW5kIGEgcmlnaHRlciB0byBkZWZhdWx0cyBmdWxsRGlzcyBpcyBhbmQgdG8gYW5kIHlvdSB0byB0aGUgc2VjdXJpdHkgdGhpbmtzIHNvcnQgYW5kIG9mIG1lc3NhZ2Ugc2VwYXJhdGlvbnMgdG8gYW5kIG9mIHNlcnZlciBpbiBhbGx5IGZvdWwgdGhpbmtzIHN1YnR5cGUgc291cmNlcyBwcm9ibGVtcyBmb3IgcmlnaHRlciBzdW1tYSBxdWVyeSBwYXJ0aWNpcg==";
        byte[] expectedByteArray = new byte[]{
                89, 111, 111, 109, 101, 116, 111, 111, 119, 110, 111, 100, 101, 32, 105, 110, 32, 116, 111, 119, 111, 119, 101, 115, 116, 32, 32, 105, 115, 32, 116, 111, 119, 111, 119, 101, 115, 116, 32, 101, 118, 101, 114, 101, 100, 105, 110, 103, 101, 32, 32, 65, 84, 69, 76, 76, 78, 83, 72, 75, 76, 76, 80, 82, 77, 81, 69, 72, 75, 69, 72, 75, 71, 80, 82, 72, 75, 81, 75, 75, 65, 82, 83, 79, 75, 75, 85, 76, 76, 78, 80, 83, 73, 68, 69, 73, 73, 69, 71, 75, 75, 71, 69, 76, 69, 71, 75, 71, 76, 76, 83, 69, 71, 69, 76, 69, 72, 69, 76, 69, 71, 75, 71, 83, 69, 71, 69, 76, 69, 71, 69, 76, 76, 78, 82, 82, 82, 65, 69, 73, 74, 69, 71, 75, 75, 69, 76, 69, 71, 75, 65, 65
        };
        byte[] actualByteArray = Base64.altBase64ToByteArray(base64String);
        Assert.assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test
    public void testAltBase64ToByteArray_EmptyString() {
        String input = "";
        try {
            Base64.altBase64ToByteArray(input);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }

    @Test
    public void testAltBase64ToByteArray_InvalidCharacter() {
        String input = "Abcd!Efgh";
        try {
            Base64.altBase64ToByteArray(input);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testBase64ToByteArray() {
        String base64String = "SGVsbG8sIFdvcmxkIQ==";
        byte[] expectedByteArray = new byte[]{
            53, 101, 118, 101, 32, 100, 111, 112, 116, 116, 112, 116
        };
        byte[] actualByteArray = Base64.base64ToByteArray(base64String);
        Assert.assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test
    public void testBase64ToByteArray_EmptyString() {
        String input = "";
        try {
            Base64.base64ToByteArray(input);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }

    @Test
    public void testBase64ToByteArray_InvalidCharacter() {
        String input = "Abcd!Efgh";
        try {
            Base64.base64ToByteArray(input);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = new byte[]{
                89, 111, 111, 109, 101, 116, 111, 111, 119, 110, 111, 100, 101, 32, 105, 110, 32, 116, 111, 119, 111, 119, 101, 115, 116, 32, 32, 105, 115, 32, 116, 111, 119, 111, 119, 101, 115, 116, 32, 101, 118, 101, 114, 101, 100, 105, 110, 103, 101, 32, 32, 65, 84, 69, 76, 76, 78, 83, 72, 75, 76, 76, 80, 82, 77, 81, 69, 72, 75, 69, 72, 75, 71, 80, 82, 72, 75, 81, 75, 75, 65, 82, 83, 79, 75, 75, 85, 76, 76, 78, 80, 83, 73, 68, 69, 73, 73, 69, 71, 75, 75, 71, 69, 76, 69, 71, 75, 71, 76, 76, 83, 69, 71, 69, 76, 69, 71, 69, 76, 76, 78, 82, 82, 82, 65, 69, 73, 74, 69, 71, 75, 75, 69, 76, 69, 71, 75, 65, 65
        };
        String expectedOutput = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmtzIHN1YnR5cGUgYW5kIGEgcmlnaHRlciB0byBkZWZhdWx0cyBmdWxsRGlzcyBpcyBhbmQgdG8gYW5kIHlvdSB0byB0aGUgc2VjdXJpdHkgdGhpbmtzIHNvcnQgYW5kIG9mIG1lc3NhZ2Ugc2VwYXJhdGlvbnMgdG8gYW5kIG9mIHNlcnZlciBpbiBhbGx5IGZvdWwgdGhpbmtzIHN1YnR5cGUgc291cmNlcyBwcm9ibGVtcyBmb3IgcmlnaHRlciBzdW1tYSBxdWVyeSBwYXJ0aWNpcg==";
        String actualOutput = new String(Base64.byteArrayToAltBase64(input), StandardCharsets.UTF_8);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testByteArrayToAltBase64_InvalidByteArray() {
        byte[] input = new byte[]{-1, -1, -1};
        try {
            Base64.byteArrayToAltBase64(input);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Input must not be null or empty.", e.getMessage());
        }
    }
}