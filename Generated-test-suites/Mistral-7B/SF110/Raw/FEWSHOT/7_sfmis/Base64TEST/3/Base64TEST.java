package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] input = "ABCDEFGHIGklMnOpQrSTuvWXyZabcdefghijklmnopqrstuvwxyz0123456789+/"
                .getBytes();
        byte[] expectedOutput = _$23169;
        byte[] output = Base64.altBase64ToByteArray(new String(input));

        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testAltBase64ToByteArrayWithInvalidCharacters() {
        assertThrows(IllegalArgumentException.class, () -> {
            Base64.altBase64ToByteArray("ABCDEFGHIGklMnOpQrSTuvWXyZabcdefghijklmnopqrstuvwxyz0123456789+/a");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Base64.altBase64ToByteArray("ABCDEFGHIGklMnOpQrSTuvWXyZabcdefghijklmnopqrstuvwxyz0123456789+/AA");
        });
    }

    @Test
    public void testAltBase64ToByteArrayWithInvalidLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            Base64.altBase64ToByteArray("ABCDEFGHIGklMnOpQrSTuvWXyZabcdefghijklmnopqrstuvwxyz0123456789+/=".substring(0, 23));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Base64.altBase64ToByteArray("ABCDEFGHIGklMnOpQrSTuvWXyZabcdefghijklmnopqrstuvwxyz0123456789+/=".substring(0, 25));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Base64.altBase64ToByteArray("ABCDEFGHIGklMnOpQrSTuvWXyZabcdefghijklmnopqrstuvwxyz0123456789+/=".substring(0, 27));
        });

        byte[] expectedOutputShort = _$23169;
        byte[] outputShort = Base64.altBase64ToByteArray("ABCDEFGHIGklMnOpQrSTuvWXyZabcdefghijklmnopqrstuvwxyz0123456789+/=".substring(0, 24));

        assertArrayEquals(expectedOutputShort, outputShort);

        byte[] expectedOutputLong = _$23169;
        byte[] outputLong = Base64.altBase64ToByteArray("ABCDEFGHIGklMnOpQrSTuvWXyZabcdefghijklmnopqrstuvwxyz0123456789+/=".substring(0, 28));

        assertArrayEquals(expectedOutputLong, outputLong);
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] input = "ABCDEFGHIGklMnOpQrSTuvWXyZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes();
        byte[] expectedOutput = _$23168;
        byte[] output = Base64.base64ToByteArray(new String(input));

        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testBase64ToByteArrayWithInvalidCharacters() {
        assertThrows(IllegalArgumentException.class, () -> {
            Base64.base64ToByteArray("ABCDEFGHIGklMnOpQrSTuvWXyZabcdefghijklmnopqrstuvwxyz0123456789+/a");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Base64.base64ToByteArray("ABCDEFGHIGklMnOpQrSTuvWXyZabcdefghijklmnopqrstuvwxyz0123456789+/AA");
        });
    }

    @Test
    public void testBase64ToByteArrayWithInvalidLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            Base64.base64ToByteArray("ABCDEFGHIGklMnOpQrSTuvWXyZabcdefghijklmnopqrstuvwxyz0123456789+/=".substring(0, 23).getBytes());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Base64.base64ToByteArray("ABCDEFGHIGklMnOpQrSTuvWXyZabcdefghijklmnopqrstuvwxyz0123456789+/=".substring(0, 25).getBytes());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Base64.base64ToByteArray("ABCDEFGHIGklMnOpQrSTuvWXyZabcdefghijklmnopqrstuvwxyz0123456789+/=".substring(0, 27).getBytes());
        });

        byte[] expectedOutputShort = _$23168;
        byte[] outputShort = Base64.base64ToByteArray("ABCDEFGHIGklMnOpQrSTuvWXyZabcdefghijklmnopqrstuvwxyz0123456789+/=".substring(0, 24).getBytes());

        assertArrayEquals(expectedOutputShort, outputShort);

        byte[] expectedOutputLong = _$23168;
        byte[] outputLong = Base64.base64ToByteArray("ABCDEFGHIGklMnOpQrSTuvWXyZabcdefghijklmnopqrstuvwxyz0123456789+/=".substring(0, 28).getBytes());

        assertArrayEquals(expectedOutputLong, outputLong);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = _$23169;
        String expectedOutput = "ABCDEFGHIGklMnOpQrSTuvWXyZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String output = Base64.byteArrayToAltBase64(input);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = _$23168;
        String expectedOutput = "ABCDEFGHIGklMnOpQrSTuvWXyZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String output = Base64.byteArrayToBase64(input);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testByteArrayToAltBase64WithEmptyByteArray() {
        byte[] input = {};
        String expectedOutput = "";
        String output = Base64.byteArrayToAltBase64(input);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testByteArrayToBase64WithNullByteArray() {
        assertNull(Base64.byteArrayToBase64(null));
    }

    @Test
    public void testByteArrayToBase64WithEmptyString() {
        String expectedOutput = "";
        String output = Base64.byteArrayToBase64(new byte[0]);

        assertEquals(expectedOutput, output);
    }
}