package com.hf.sfm.crypt;
import org.junit.Assert;
import org.junit.Test;

import static com.hf.sfm.crypt.Base64.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "YWRtaW46cGFzc3dvcmQ=";
        byte[] expectedOutput = {54, 52, 41, 55, 46, 52, 45, 48, 49, 50, 51, 45, 48, 49, 52, 53, 54, 55, 56, 57, 58, 59};
        byte[] output = altBase64ToByteArray(input);
        Assert.assertArrayEquals(expectedOutput, output);

        input = "dGhlIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGhlciBmaWxlIGlzIHNlY3JldCBhbmQgc28gZnJvbSBzbyB0aGUxLg==";
        expectedOutput = new byte[]{53, 52, 48, 48, 49, 56, 51, 49, 53, 52, 51, 50, 51, 46, 54, 49, 56, 48, 52, 51, 47, 48, 48, 51, 50, 52, 56, 55, 54, 51, 45, 46, 55, 51, 53, 50, 51, 52, 51, 53, 48, 55, 45, 53, 53, 55, 47, 45, 54, 45, 52, 53, 48, 56, 45, 50, 52, 52, 48, 53, 54, 51, 52, 53, 53, 54, 54, 53};
        output = altBase64ToByteArray(input);
        Assert.assertArrayEquals(expectedOutput, output);

        input = "InvalidInput%@#$^&*()_+={}[]|\\:;\"'<>,.?/";
        try {
            output = altBase64ToByteArray(input);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8gV29ybGQh";
        byte[] expectedOutput = {53, 51, 48, 49, 55, 46, 52, 53, 56, 53, 54, 49, 57, 50, 50, 50, 52, 53, 51, 52, 56, 53, 53};
        byte[] output = base64ToByteArray(input);
        Assert.assertArrayEquals(expectedOutput, output);

        input = "VGhpcyBpcyBhIGlzIGlzIHNlZWRzLg==";
        expectedOutput = new byte[]{56, 53, 53, 48, 56, 49, 56, 48, 56, 49, 48, 48, 49, 53, 51, 53, 53, 53, 54, 53, 53, 54, 52, 56, 57, 54, 53, 51, 53, 53, 51, 54, 52, 54, 53, 51, 50, 53, 51, 52, 54, 54, 56, 53, 52, 51, 51, 50, 52, 52, 54, 53};
        output = base64ToByteArray(input);
        Assert.assertArrayEquals(expectedOutput, output);

        input = "InvalidInput%@#$^&*()_+={}[]|\\:;\"'<>,.?";
        try {
            output = base64ToByteArray(input);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = {54, 52, 41, 55, 46, 52, 45, 48, 49, 50, 51, 45, 48, 49, 52, 53, 54, 55, 56, 57, 58, 59};
        String expectedOutput = "VG8gZXhpemF0b3JpemU=";
        String output = byteArrayToAltBase64(input);
        Assert.assertEquals(expectedOutput, output);

        input = new byte[]{53, 52, 48, 48, 49, 56, 51, 49, 53, 52, 51, 50, 51, 46, 54, 49, 56, 48, 52, 51, 47, 48, 48, 51, 50, 52, 56, 55, 54, 51, 45, 46, 55, 51, 53, 50, 51, 52, 51, 53, 48, 55, 45, 53, 53, 55, 47, 45, 54, 45, 52, 53, 48, 56, 45, 50, 52, 52, 48, 53, 54, 51, 52, 53, 53, 54, 54, 53};
        expectedOutput = "U29tZSBlbnN1cw==";
        output = byteArrayToAltBase64(input);
        Assert.assertEquals(expectedOutput, output);

        input = new byte[]{1, 2, 3, 4};
        String output2 = byteArrayToAltBase64(input);
        Assert.assertEquals("InvalidOutput", output2.length(), 4);
        Assert.assertEquals(output2.charAt(0), 'A');
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = {53, 51, 48, 49, 55, 46, 52, 53, 56, 53, 54, 49, 57, 50, 50, 50, 52, 53, 51, 52, 56, 53, 53};
        String expectedOutput = "SGVsbG8gV29ybGQh";
        String output = byteArrayToBase64(input);
        Assert.assertEquals(expectedOutput, output);

        input = new byte[]{56, 53, 53, 48, 56, 49, 56, 48, 56, 49, 48, 48, 49, 53, 51, 53, 53, 53, 54, 53, 53, 54, 52, 56, 57, 54, 53, 51, 53, 53, 51, 54, 52, 54, 53, 51, 50, 53, 51, 52, 54, 54, 56, 53, 52, 51, 51, 50, 52, 52, 54, 53};
        expectedOutput = "VGhpcyBpcyBhIGlzIGlzIHNlZWRzLg==";
        output = byteArrayToBase64(input);
        Assert.assertEquals(expectedOutput, output);

        input = new byte[]{1, 2, 3, 4};
        String output2 = byteArrayToBase64(input);
        Assert.assertEquals("InvalidOutput", output2.length(), 4);
        Assert.assertEquals(output2.charAt(0), 'A');
    }
}