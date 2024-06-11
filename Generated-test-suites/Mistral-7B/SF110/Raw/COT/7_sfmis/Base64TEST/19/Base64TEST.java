package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.hf.sfm.crypt.Base64._$23167;
import static com.hf.sfm.crypt.Base64._$23168;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray_validInput() {
        String base64 = "RG9jdW1lbnRzIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggYWpheg==";
        byte[] expected = {41, 49, 46, 46, 51, 49, 48, 48, 49, 50, 50, 48, 51, 48, 48, 49, 52, 52, 52, 51, 50, 49, 49, 50, 49, 51, 49, 50, 51, 50, 50, 51, 50, 52, 51, 49, 49, 51, 51, 51, 50, 52, 51, 52, 51, 48, 51, 51, 50, 51, 50, 52, 51, 51, 50, 52, 51, 51, 51};
        byte[] result = Base64.altBase64ToByteArray(base64);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_invalidInputLength() {
        String base64 = "RG9jdW1lbnRzIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIu";
        Base64.altBase64ToByteArray(base64);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_invalidInput() {
        String base64 = "RG9jdW1lbnRzIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggYWpheg==x";
        Base64.altBase64ToByteArray(base64);
    }

    @Test
    public void testAltBase64ToByteArray_noPadding() {
        String base64 = "RG9jdW1lbnRz";
        byte[] result = Base64.altBase64ToByteArray(base64);
        byte[] expected = {41, 49, 46, 46, 51};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testAltBase64ToByteArray_singlePadding() {
        String base64 = "RG9jdW1lbnRzIG==";
        byte[] result = Base64.altBase64ToByteArray(base64);
        byte[] expected = {41, 49, 46, 46, 51, 61};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testAltBase64ToByteArray_doublePadding() {
        String base64 = "RG9jdW1lbnRzIG==IG==";
        byte[] result = Base64.altBase64ToByteArray(base64);
        byte[] expected = {41, 49, 46, 46, 51, 61, 61};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBase64ToByteArray_validInput() {
        String base64 = "RG9jdW1lbnRzIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggYWpheg==";
        byte[] expected = {41, 49, 46, 46, 51, 49, 48, 48, 49, 50, 50, 48, 51, 48, 48, 49, 52, 52, 52, 51, 50, 49, 49, 50, 49, 51, 49, 50, 51, 50, 50, 51, 50, 52, 51, 49, 49, 51, 51, 51, 50, 52, 51, 52, 51, 48, 51, 51, 50, 51, 50, 52, 51, 51, 50, 52, 51, 51, 51};
        byte[] result = Base64.base64ToByteArray(base64);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_invalidInputLength() {
        String base64 = "RG9jdW1lbnRzIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIu";
        Base64.base64ToByteArray(base64);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_invalidInput() {
        String base64 = "RG9jdW1lbnRzIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggYWpheg==x";
        Base64.base64ToByteArray(base64);
    }

    @Test
    public void testBase64ToByteArray_noPadding() {
        String base64 = "RG9jdW1lbnRz";
        byte[] result = Base64.base64ToByteArray(base64);
        byte[] expected = {41, 49, 46, 46, 51};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBase64ToByteArray_singlePadding() {
        String base64 = "RG9jdW1lbnRzIG==";
        byte[] result = Base64.base64ToByteArray(base64);
        byte[] expected = {41, 49, 46, 46, 51, 61};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBase64ToByteArray_doublePadding() {
        String base64 = "RG9jdW1lbnRzIG==IG==";
        byte[] result = Base64.base64ToByteArray(base64);
        byte[] expected = {41, 49, 46, 46, 51, 61, 61};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testByteArrayToAltBase64_validInput() {
        byte[] input = {41, 49, 46, 46, 51, 49, 48, 48, 49, 50, 50, 48, 51, 48, 48, 49, 52, 52, 52, 51, 50, 49, 49, 50, 49, 51, 49, 50, 51, 50, 50, 51, 50, 52, 51, 49, 49, 51, 51, 51, 50, 52, 51, 52, 51, 48, 51, 51, 50, 51, 50, 52, 51, 51, 50, 52, 51, 51, 51};
        String expected = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggYWpheg==";
        String result = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToAltBase64_noPadding() {
        byte[] input = {41, 49, 46, 46, 51};
        String expected = "VG8gZXJyIGlz";
        String result = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToAltBase64_singlePadding() {
        byte[] input = {41, 49, 46, 46, 51, 61};
        String expected = "VG8gZXJyIGlzIG==";
        String result = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToAltBase64_doublePadding() {
        byte[] input = {41, 49, 46, 46, 51, 61, 61};
        String expected = "VG8gZXJyIGlzIG==IG==";
        String result = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64_validInput() {
        byte[] input = {41, 49, 46, 46, 51, 49, 48, 48, 49, 50, 50, 48, 51, 48, 48, 49, 52, 52, 52, 51, 50, 49, 49, 50, 49, 51, 49, 50, 51, 50, 50, 51, 50, 52, 51, 49, 49, 51, 51, 51, 50, 52, 51, 52, 51, 48, 51, 51, 50, 51, 50, 52, 51, 51, 50, 52, 51, 51, 51};
        String expected = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggYWpheg==";
        String result = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64_noPadding() {
        byte[] input = {41, 49, 46, 46, 51};
        String expected = "VG8gZXJyIGlz";
        String result = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64_singlePadding() {
        byte[] input = {41, 49, 46, 46, 51, 61};
        String expected = "VG8gZXJyIGlzIG==";
        String result = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64_doublePadding() {
        byte[] input = {41, 49, 46, 46, 51, 61, 61};
        String expected = "VG8gZXJyIGlzIG==IG==";
        String result = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64_invalidInput() {
        byte[] input = {41, 49, 46, 46, 51, 61, 61, -1};
        String result = Base64.byteArrayToBase64(input);
        Assert.assertNull(result);
    }
}