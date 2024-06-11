package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "Rm9udCAoKSxoJw==";
        byte[] expectedOutput = {41, 56, 51, 48, 49, 44, 52, 46, 51};
        byte[] output = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayWithInvalidInput() {
        String input = "Rm9udCAoKSxoJw==a";
        Base64.altBase64ToByteArray(input);
    }

    @Test
    public void testAltBase64ToByteArrayWithEmptyInput() {
        String input = "";
        byte[] output = Base64.altBase64ToByteArray(input);
        Assert.assertArrayEquals(new byte[0], output);
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8sIFdvcmxkIQ==";
        byte[] expectedOutput = {53, 48, 51, 46, 43, 54, 49, 52, 53, 50, 51, 45, 52, 51, 56, 45, 48, 54, 53, 52, 51, 57, 55, 50, 48, 56, 52, 53, 51, 51, 50, 51, 52, 48, 56, 51, 53, 52, 51, 55, 51, 53, 51, 55, 54, 53, 55, 51, 54, 52, 51, 51};
        byte[] output = Base64.base64ToByteArray(input);
        Assert.assertArrayEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayWithInvalidInput() {
        String input = "SGVsbG8sIFdvcmxkIQ==a";
        Base64.base64ToByteArray(input);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = {53, 48, 51, 46, 43, 54, 49, 52, 53, 50, 51, 45, 52, 51, 56, 45, 48, 54, 53, 52, 51, 57, 55, 50, 48, 56, 52, 53, 51, 51, 50, 51, 52, 48, 56, 51, 53, 52, 51, 55, 51, 53, 51, 55, 54, 53, 55, 51, 54, 52, 51, 51};
        String expectedOutput = "SGVsbG8sIFdvcmxkIQ==";
        String output = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = {53, 48, 51, 46, 43, 54, 49, 52, 53, 50, 51, 45, 52, 51, 56, 45, 48, 54, 53, 52, 51, 57, 55, 50, 48, 56, 52, 53, 51, 51, 50, 51, 52, 48, 56, 51, 53, 52, 51, 55, 51, 53, 51, 55, 54, 53, 55, 51, 54, 52, 51, 51};
        String expectedOutput = "SGVsbG8sIFdvcmxkIQ==";
        String output = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testByteArrayToBase64AndAltBase64RoundTrip() {
        byte[] input = {53, 48, 51, 46, 43, 54, 49, 52, 53, 50, 51, 45, 52, 51, 56, 45, 48, 54, 53, 52, 51, 57, 55, 50, 48, 56, 52, 53, 51, 51, 50, 51, 52, 48, 56, 51, 53, 52, 51, 55, 51, 53, 51, 55, 54, 53, 55, 51, 54, 52, 51, 51};
        String base64Output = Base64.byteArrayToBase64(input);
        String altBase64Output = Base64.byteArrayToAltBase64(input);
        Assert.assertEquals(base64Output, altBase64Output);
    }

    @Test
    public void testByteArrayToBase64WithEmptyInput() {
        byte[] input = {};
        String expectedOutput = "";
        String output = Base64.byteArrayToBase64(input);
        Assert.assertEquals(expectedOutput, output);
    }
}