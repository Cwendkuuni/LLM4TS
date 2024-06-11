package com.hf.sfm.crypt;

import org.junit.Assert;
import org.junit.Test;

import static com.hf.sfm.crypt.Base64.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "RnJvbSBvZiBhbGx5IGZvdWwgdG8gcmVhbGx5IGZvdWw=";
        byte[] expectedOutput = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        byte[] output = altBase64ToByteArray(input);
        Assert.assertArrayEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayWithInvalidInput() {
        String input = "RnJvbSBvZiBhbGx5IGZvdWwgdG8gcmVhbGx5IGZvdWw=".substring(0, 76);
        altBase64ToByteArray(input);
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8gV29ybGQh";
        byte[] expectedOutput = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        byte[] output = base64ToByteArray(input);
        Assert.assertArrayEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayWithInvalidInput() {
        String input = "SGVsbG8gV29ybGQh".substring(0, 19);
        base64ToByteArray(input);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        String expectedOutput = "RnJvbSBvZiBhbGx5IGZvdWwgdG8gcmVhbGx5IGZvdWw=";
        String output = byteArrayToAltBase64(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        String expectedOutput = "SGVsbG8gV29ybGQh";
        String output = byteArrayToBase64(input);
        Assert.assertEquals(expectedOutput, output);
    }
}