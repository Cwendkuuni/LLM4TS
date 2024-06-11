package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

import static com.hf.sfm.crypt.Base64._$23169;
import static com.hf.sfm.crypt.Base64._$23168;
import static com.hf.sfm.crypt.Base64._$23167;
import static com.hf.sfm.crypt.Base64._$23166;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expectedByteArray = _$23169;
        byte[] actualByteArray = Base64.altBase64ToByteArray(testString);
        Assert.assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidInput() {
        String invalidInput = "ABCDEFGH";
        Base64.altBase64ToByteArray(invalidInput);
    }

    @Test
    public void testBase64ToByteArray() {
        String testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expectedByteArray = _$23169;
        byte[] actualByteArray = Base64.base64ToByteArray(testString);
        Assert.assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidInputLength() {
        String invalidInput = "ABCDEF";
        Base64.base64ToByteArray(invalidInput);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] testByteArray = _$23169;
        String expectedAltBase64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String actualAltBase64 = Base64.byteArrayToAltBase64(testByteArray);
        Assert.assertEquals(expectedAltBase64, actualAltBase64);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] testByteArray = _$23169;
        String expectedBase64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String actualBase64 = Base64.byteArrayToBase64(testByteArray);
        Assert.assertEquals(expectedBase64, actualBase64);
    }

    @Test
    public void testByteArrayToBase64WithPadding() {
        byte[] testByteArray = new byte[]{1, 2, 3};
        String expectedBase64 = "MTIzNDU2Nzg5";
        String actualBase64 = Base64.byteArrayToBase64(testByteArray);
        Assert.assertEquals(expectedBase64, actualBase64);
    }

    @Test
    public void testBase64ToByteArrayWithPadding() {
        String testBase64 = "MTIzNDU2Nzg5";
        byte[] expectedByteArray = new byte[]{1, 2, 3};
        byte[] actualByteArray = Base64.base64ToByteArray(testBase64);
        Assert.assertArrayEquals(expectedByteArray, actualByteArray);
    }

}