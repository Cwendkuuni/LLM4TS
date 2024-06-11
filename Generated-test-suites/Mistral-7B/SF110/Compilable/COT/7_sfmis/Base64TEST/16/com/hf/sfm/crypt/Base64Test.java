package com.hf.sfm.crypt.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.hf.sfm.crypt.Base64;

public class Base64Test {

    // Test altBase64ToByteArray method

    @Test
    public void testAltBase64ToByteArray_Valid() {
        String base64String = "Rm9udCAoMTIzNDU2Nzg5QGdtYWlsLg==";
        byte[] expectedByteArray = {48, 51, 51, 50, 51, 49, 49, 48, 49, 50, 51, 50, 51, 50, 51, 49, 50, 51, 51};
        byte[] actualByteArray = Base64.altBase64ToByteArray(base64String);
        assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test
    public void testAltBase64ToByteArray_InvalidLength() {
        String base64String = "Rm9udCAoMTIzNDU2Nzg5QGdtYWls";
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(base64String));
    }

    @Test
    public void testAltBase64ToByteArray_InvalidCharacter() {
        String base64String = "Rm9udCAoMTIzNDU2Nzg5QGdtYWlsLg==x";
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(base64String));
    }

    // Test base64ToByteArray method

    @Test
    public void testBase64ToByteArray_Valid() {
        String base64String = "Rm9udCAoMTIzNDU2Nzg5QGdtYWls";
        byte[] expectedByteArray = {48, 51, 51, 50, 51, 49, 49, 48, 49, 50, 51, 50, 51, 50, 51, 49, 50, 51, 51};
        byte[] actualByteArray = Base64.base64ToByteArray(base64String);
        assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test
    public void testBase64ToByteArray_InvalidLength() {
        String base64String = "Rm9udCAoMTIzNDU2Nzg5QGdtYWls";
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(base64String));
    }

    @Test
    public void testBase64ToByteArray_InvalidCharacter() {
        String base64String = "Rm9udCAoMTIzNDU2Nzg5QGdtYWlsLg==x";
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(base64String));
    }

    // Test byteArrayToAltBase64 method

    @Test
    public void testByteArrayToAltBase64_Valid() {
        byte[] byteArray = {48, 51, 51, 50, 51, 49, 49, 48, 49, 50, 51, 50, 51, 50, 51, 49, 50, 51, 51};
        String expectedBase64String = "Rm9udCAoMTIzNDU2Nzg5QGdtYWlsLg==";
        String actualBase64String = Base64.byteArrayToAltBase64(byteArray);
        assertEquals(expectedBase64String, actualBase64String);
    }

    @Test
    public void testByteArrayToAltBase64_InvalidLength() {
        byte[] byteArray = {48, 51, 51, 50};
        assertThrows(IllegalArgumentException.class, () -> Base64.byteArrayToAltBase64(byteArray));
    }

    @Test
    public void testByteArrayToAltBase64_InvalidByte() {
        byte[] byteArray = {48, 51, 51, 50, (byte)128};
        assertThrows(IllegalArgumentException.class, () -> Base64.byteArrayToAltBase64(byteArray));
    }

    // Test byteArrayToBase64 method

    @Test
    public void testByteArrayToBase64_Valid() {
        byte[] byteArray = {48, 51, 51, 50, 51, 49, 49, 48, 49, 50, 51, 50, 51, 50, 51, 49, 50, 51, 51};
        String expectedBase64String = "Rm9udCAoMTIzNDU2Nzg5QGdtYWls";
        String actualBase64String = Base64.byteArrayToBase64(byteArray);
        assertEquals(expectedBase64String, actualBase64String);
    }

    @Test
    public void testByteArrayToBase64_InvalidLength() {
        byte[] byteArray = {48, 51, 51, 50};
        assertThrows(IllegalArgumentException.class, () -> Base64.byteArrayToBase64(byteArray));
    }

    @Test
    public void testByteArrayToBase64_InvalidByte() {
        byte[] byteArray = {48, 51, 51, 50, (byte)128};
        String base64String = Base64.byteArrayToBase64(byteArray);
        assertEquals("Rm9udCAoMTIzNDU2Nzg5QGdtYWlsIHdvcmxkIQ==", base64String);
    }
}