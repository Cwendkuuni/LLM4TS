package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expectedOutput = new byte[]{-66, -63, -65, -66, -66, -63, -65, -66, -66, -63, -65, -66, -66, -63, -65, -66, -66, -63, -65, -66, -66, -63, -65, -66, -66, -63, -65, -66, -66, -63, -65, -66, -66, 65, 61, 62, 63, 63, 63, 63, 63, 63, 63, 61, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62};
        byte[] output = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testAltBase64ToByteArrayWithInvalidInput() {
        String input = "abc";
        try {
            Base64.altBase64ToByteArray(input);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdmVyIG5vdCBhbGx5IHB1YnNjcmliZSB5b3UgYW5kIHdoZXJlIG9mIHRoZSBmaWxlIGZvdWwgdGhlIGZpbGVzIG5vdCBzdWZmaXggPSBzdWZmaXggPSBzdWZmaXggPC9kaXY+CiAgICAtLVBhdmVyIG5vdCBhbGx5IHB1YnNjcmliZSB5b3UgYW5kIHdoZXJlIG9mIHRoZSBmaWxlIGZvdWwgdGhlIGZpbGVzIG5vdCBzdWZmaXggPSBzdWZmaXggPSBzdWZmaXggPC9kaXY+CiAg";
        byte[] expectedOutput = new byte[]{42, -66, -67, -68, -69, -69, -69, -69, -69, -69, -69, -69, -69, -69, -69, -69, -69, -69, -69, -69, -69, -69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69};
        byte[] output = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testBase64ToByteArrayWithInvalidInput() {
        String input = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdmVyIG5vdCBhbGx5IHB1YnNjcmliZSB5b3UgYW5kIHdoZXJlIG9mIHRoZSBmaWxlIGZvdWwgdGhlIGZpbGVzIG5vdCBzdWZmaXggPSBzdWZmaXggPSBzdWZmaXggPC9kaXY+CiAgICAtLVBhdmVyIG5vdCBhbGx5IHB1YnNjcmliZSB5b3UgYW5kIHdoZXJlIG9mIHRoZSBmaWxlIGZvdWwgdGhlIGZpbGVzIG5vdCBzdWZmaXggPSBzdWZmaXggPSBzdWZmaXggPC9kaXY+CiAg";
        try {
            Base64.base64ToByteArray(input);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = new byte[]{-66, -63, -65, -66, -66, -63, -65, -66, -66, -63, -65, -66, -66, -63, -65, -66, -66, -63, -65, -66, -66, -63, -65, -66, -66, -63, -65, -66, -66, -63, -65, -66, -66, 65, 61, 62, 63, 63, 63, 63, 63, 63, 63, 61, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62};
        String expectedOutput = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String output = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = new byte[]{42, -66, -67, -68, -69, -69, -69, -69, -69, -69, -69, -69, -69, -69, -69, -69, -69, -69, -69, -69, -69, -69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69};
        String expectedOutput = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdmVyIG5vdCBhbGx5IHB1YnNjcmliZSB5b3UgYW5kIHdoZXJlIG9mIHRoZSBmaWxlIGZvdWwgdGhlIGZpbGVzIG5vdCBzdWZmaXggPSBzdWZmaXggPSBzdWZmaXggPC9kaXY+CiAgICAtLVBhdmVyIG5vdCBhbGx5IHB1YnNjcmliZSB5b3UgYW5kIHdoZXJlIG9mIHRoZSBmaWxlIGZvdWwgdGhlIGZpbGVzIG5vdCBzdWZmaXggPSBzdWZmaXggPSBzdWZmaXggPC9kaXY+CiAg";
        String output = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testByteArrayToBase64WithEmptyInput() {
        byte[] input = new byte[0];
        String expectedOutput = "";
        String output = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testBase64ToByteArrayWithEmptyOutput() {
        String input = "";
        byte[] expectedOutput = new byte[0];
        byte[] output = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(expectedOutput, output);
    }
}