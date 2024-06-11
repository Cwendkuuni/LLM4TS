package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

import java.util.Base64;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "Qm94ZWxkIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwgYW5kIG9mIHlvdSBjb250aW9uLCB3aXRoIGZsaW5lcyBmaWxlIGJ1dCBpcyBhbmQgY29tcHV0ZXIu";
        byte[] expected = {-65, -73, -68, 67, 65, -62, 63, 68, 61, -69, 66, -67, -62, -61, 66, -63, 65, -62, 63, 68, 61, -69, 66, -67, -62, -61, 66, -63, 65, -62, 63, 68, 61, -69, 66, -67, -62, -61, 66, -63, 65, -62, 63, 68, 61, -69, 66, -67, -62, -61, 66, -63, 65, -62, 63, -61};
        byte[] result = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testAltBase64ToByteArray_InvalidLength() {
        String input = "Qm94ZWxkIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIu";
        try {
            Base64.altBase64ToByteArray(input);
            Assert.fail("An IllegalArgumentException was expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testAltBase64ToByteArray_Empty() {
        String input = "";
        byte[] result = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(new byte[0], result);
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8sIFdvcmxkIQ==";
        byte[] expected = {-65, -73, -68, 67, 65, 63, 68, 61, -69, 66, -67, -62, 65, 63, 68, 61, -69, 66, -67, -62, -61, 66, -63, 65, 63, 68, 61, -69, 66, -67, -62, -61, 66, -63, 65, 63, 68, 61, -69, 66, -67, -62, -61, 66, -63, 65, 63};
        byte[] result = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBase64ToByteArray_InvalidLength() {
        String input = "SGVsbG8sIFdvcmxk";
        try {
            Base64.base64ToByteArray(input);
            Assert.fail("An IllegalArgumentException was expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testBase64ToByteArray_Empty() {
        String input = "";
        byte[] result = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(new byte[0], result);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = {-65, -73, -68, 67, 65, -62, 63, 68, 61, -69, 66, -67, -62, -61, 66, -63, 65, -62, 63, 68, 61, -69, 66, -67, -62, -61, 66, -63, 65, -62, 63, 68, 61, -69, 66, -67, -62, -61, 66, -63, 65, -62, 63, 68, 61, -69, 66, -67, -62, -61, 66, -63, 65, -62, 63};
        String expected = "Qm94ZWxkIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIu";
        String result = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToAltBase64_Padding() {
        byte[] input = {-65, -73, -68, 67, 65, 63, 68, 61, -69, 66, -67, -62, 65, 63, 68, 61, -69, 66, -67, -62, -61, 66, -63, 65, 63, 68, 61, -69, 66, -67, -62, -61, 66, -63, 65, 63, 68, 61, -69, 66, -67, -62, -61, 66, -63};
        String expected = "Qm94ZWxkIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwgYW5kIG9mIHlvdSBjb250aW9uLCB3aXRoIGZsaW5lcyBmaWxlIGJ1dCBpcyBhbmQgY29tcHV0ZXIu";
        String result = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = {-65, -73, -68, 67, 65, 63, 68, 61, -69, 66, -67, -62, 65, 63, 68, 61, -69, 66, -67, -62, -61, 66, -63, 65, 63, 68, 61, -69, 66, -67, -62, -61, 66, -63, 65, 63, 68, 61, -69, 66, -67, -62, -61, 66, -63};
        String expected = "SGVsbG8sIFdvcmxkIQ==";
        String result = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64_Padding() {
        byte[] input = {-65, -73, -68, 67, 65, 63, 68, 61, -69, 66, -67, -62, 65, 63, 68, 61, -69, 66, -67, -62, -61, 66, -63, 65, 63, 68, 61, -69, 66, -67, -62, -61, 66, -63, 65, 63, 68, 61, -69, 66, -67, -62, -61, 66, -63};
        String expected = "SGVsbG8sIFdvcmxkIG==";
        String result = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64_Empty() {
        byte[] input = new byte[0];
        String result = Base64.byteArrayToBase64(input);
        Assert.assertEquals("", result);
    }
}