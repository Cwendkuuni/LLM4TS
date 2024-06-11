package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = {1, 2, 3, 4};
        String input = "ABCD";
        byte[] result = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testAltBase64ToByteArrayWithPadding() {
        byte[] expected = {1, 2, 3};
        String input = "ABC";
        byte[] result = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidLength() {
        String input = "ABCDE";
        Base64.altBase64ToByteArray(input);
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] expected = {1, 2, 3, 4};
        String input = "AQIDBA==";
        byte[] result = Base64.base64ToByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBase64ToByteArrayWithPadding() {
        byte[] expected = {1, 2, 3};
        String input = "AQID";
        byte[] result = Base64.base64ToByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidLength() {
        String input = "AQIDBA===";
        Base64.base64ToByteArray(input);
    }

    @Test
    public void testByteArrayToAltBase64() {
        String expected = "ABCD";
        byte[] input = {1, 2, 3, 4};
        String result = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToAltBase64WithPadding() {
        String expected = "ABC";
        byte[] input = {1, 2, 3};
        String result = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64() {
        String expected = "AQIDBA==";
        byte[] input = {1, 2, 3, 4};
        String result = Base64.byteArrayToBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testByteArrayToBase64WithPadding() {
        String expected = "AQID";
        byte[] input = {1, 2, 3};
        String result = Base64.byteArrayToBase64(input);
        assertEquals(expected, result);
    }

    @Test
    public void testMain() {
        String input = "0123456789";
        byte[] bytes = input.getBytes();
        String encoded = Base64.byteArrayToBase64(bytes);
        System.out.println(encoded);
        byte[] decoded = Base64.base64ToByteArray(encoded);
        String decodedString = new String(decoded);
        assertEquals(input, decodedString);
    }

    @Test
    public void testInitStaticVariables() {
        assertNull(Base64._$23169);
        assertNull(Base64._$23168);
        assertNull(Base64._$23167);
        assertNull(Base64._$23166);
    }
}