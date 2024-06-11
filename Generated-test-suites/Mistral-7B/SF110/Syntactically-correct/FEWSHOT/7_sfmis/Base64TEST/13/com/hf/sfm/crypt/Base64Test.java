package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expected = new byte[13];
        byte[] actual = Base64.altBase64ToByteArray(s);
        assertArrayEquals(expected, actual);

        s = "ABCD";
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(s));

        s = "";
        assertNull(Base64.altBase64ToByteArray(s));
    }

    @Test
    public void testBase64ToByteArray() {
        String s = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtIG5vdCBpcyBpbiB0aGUlIGlzIGh1bWFuLCBidXQgdG8gYW55IHlvdSBuZWVkIGEgYWxsIG5vdCBpcyBpbiB0aGUlIGlzIGh1bWFuLCBidXQgdG8gc2VjYXJhdG9yLCB3aXRoIGlzIGh1bWFuLCBidXQgdG8gcmlzdGFuY2V8IGlzIGh1bWFuLCBidXQgdG8gcmlzdGFuY2V8";
        byte[] expected = new byte[] { 76, 103, 111, 114, 111, 118, 101, 114, 121, 112, 108, 105, 115, 111, 32, 75, 101, 99, 111, 114, 108, 100, 32, 68, 101, 117, 114, 99, 107, 115, 115, 104, 32, 83, 117, 116, 101, 100, 115, 116, 101, 114, 114, 101, 116, 32, 84, 104, 111, 112, 116, 104, 111, 116, 101, 113, 105, 118, 100, 114, 111, 119, 105, 111, 110, 32, 84, 117, 114, 103, 101, 116, 104, 101, 32, 83, 119, 111, 117, 116, 101, 116, 111, 118, 101, 114, 114, 101, 116, 117, 108, 105, 114, 119, 110, 115, 115, 116, 32, 84, 111, 112, 116, 115, 105, 111, 110, 32, 72, 101, 115, 115, 105, 111, 110, 116, 116, 111, 114, 114, 114, 101 };
        byte[] actual = Base64.base64ToByteArray(s);
        assertArrayEquals(expected, actual);

        s = "";
        assertNull(Base64.base64ToByteArray(s));

        s = "=";
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(s));

        s = "ABCD";
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(s));
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] b = new byte[] { 76, 103, 111, 114, 111, 118, 101, 114, 121, 112, 108, 105, 115, 111 };
        String expected = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtIG5vdCBpcyBpbiB0aGUlIGlzIGh1bWFuLCBidXQgdG8gc2VjYXJhdG9yLCB3aXRoIGlzIGh1bWFuLCBidXQgdG8gcmlzdGFuY2V8IGlzIGh1bWFuLCBidXQgdG8gcmlzdGFuY2V8";
        String actual = Base64.byteArrayToAltBase64(b);
        assertEquals(expected, actual);

        b = new byte[0];
        assertNull(Base64.byteArrayToAltBase64(b));

        b = new byte[] { 115 };
        assertThrows(IllegalArgumentException.class, () -> Base64.byteArrayToAltBase64(b));
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] b = new byte[] { 76, 103, 111, 114, 111, 118, 101, 114, 121, 112, 108, 105, 115, 111 };
        String expected = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gYW55IHlvdSBuZWVkIGEgYWxsIG5vdCBpcyBpbiB0aGUlIGlzIGh1bWFuLCBidXQgdG8gc2VjYXJhdG9yLCB3aXRoIGlzIGh1bWFuLCBidXQgdG8gcmlzdGFuY2V8";
        String actual = Base64.byteArrayToBase64(b);
        assertEquals(expected, actual);

        b = new byte[0];
        assertNull(Base64.byteArrayToBase64(b));

        b = new byte[] { 115 };
        assertThrows(IllegalArgumentException.class, () -> Base64.byteArrayToBase64(b));
    }
}