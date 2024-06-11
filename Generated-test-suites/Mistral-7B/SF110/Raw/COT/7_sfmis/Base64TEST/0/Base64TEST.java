package com.hf.sfm.crypt;

import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    

    @Test
    public void testBase64ToByteArray() {
        String base64String = "RG9lMjEyMzQ1Mg==";
        byte[] expectedByteArray = {48, 65, 61, 6D, 65, 0};
        byte[] actualByteArray = Base64.base64ToByteArray(base64String);
        assertArrayEquals("base64ToByteArray should return the expected byte array", expectedByteArray, actualByteArray);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] inputByteArray = {48, 65, 61, 6D, 65, 0};
        String expectedBase64String = "RG9lMjEyMzQ1Mg==";
        String actualBase64String = Base64.byteArrayToBase64(inputByteArray);
        assertEquals("byteArrayToBase64 should return the expected base64 string", expectedBase64String, actualBase64String);
    }

    @Test
    public void testAltBase64ToByteArray() {
        String altBase64String = "_ZXhhbXBsZS4=";
        byte[] expectedByteArray = {-68, -65, 83, 104, 101, 108, 101, 116, -65, 112, 114, 105, 110, 103, 104, 116, 116, 112, 101, -65, -66, 115, 111, 118, 101, 114, 100, 116, 32, 105, 110, 32, 121, 112, 112, 114, 111, 108, 100, 101, 116, 32, 115, 116, 111, 117, 115, 104, 32, 105, 110, 32, 116, 111, 111, 117, 108, 100, 105, 103, 115, 111, 46, 111, 116, 116, 112, 101};
        byte[] actualByteArray = Base64.altBase64ToByteArray(altBase64String);
        assertArrayEquals("altBase64ToByteArray should return the expected byte array", expectedByteArray, actualByteArray);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] inputByteArray = {48, 65, 61, 6D, 65, 0};
        String expectedAltBase64String = "_ZXhhbXBsZS4=";
        String actualAltBase64String = Base64.byteArrayToAltBase64(inputByteArray);
        assertEquals("byteArrayToAltBase64 should return the expected altBase64 string", expectedAltBase64String, actualAltBase64String);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayWithInvalidString() {
        String invalidString = "abcd";
        Base64.altBase64ToByteArray(invalidString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayWithInvalidString() {
        String invalidString = "abcd";
        Base64.base64ToByteArray(invalidString);
    }

    @Test
    public void testBase64ToByteArrayWithInvalidLength() {
        String invalidBase64String = "RG9lMjEyMzQ1Mg===";
        byte[] expectedByteArray = {48, 65, 61, 6D, 65, 0};
        byte[] actualByteArray = Base64.base64ToByteArray(invalidBase64String);
        assertArrayEquals("base64ToByteArray should return the expected byte array (short)", expectedByteArray, actualByteArray);
    }

    @Test
    public void testAltBase64ToByteArrayWithInvalidLength() {
        String invalidAltBase64String = "_ZXhhbXBsZS4===";
        byte[] expectedByteArray = {48, 65, 61, 6D, 65, 0};
        byte[] actualByteArray = Base64.altBase64ToByteArray(invalidAltBase64String);
        assertArrayEquals("altBase64ToByteArray should return the expected byte array (short)", expectedByteArray, actualByteArray);
    }

    
}