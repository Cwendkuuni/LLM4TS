package com.google.common.base;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        char[] sequence = {'a', 'b', 'c'};
        int length = Utf8.encodedLength(sequence);
        int expectedLength = 3;
        Assert.assertEquals("Unexpected UTF-8 length for given sequence.", expectedLength, length);

        sequence = new char[]{'á', 'é', 'í'};
        length = Utf8.encodedLength(sequence);
        expectedLength = 6;
        Assert.assertEquals("Unexpected UTF-8 length for given sequence.", expectedLength, length);

        sequence = new char[]{'±', 'µ'};
        length = Utf8.encodedLength(sequence);
        expectedLength = 3;
        Assert.assertEquals("Unexpected UTF-8 length for given sequence.", expectedLength, length);

        sequence = new char[]{'💡', '💡', '💡'};
        length = Utf8.encodedLength(sequence);
        expectedLength = 9;
        Assert.assertEquals("Unexpected UTF-8 length for given sequence.", expectedLength, length);

        sequence = new char[]{'💡', '\uD800', '\uDFFF'};
        Assert.assertThrows("Unexpected exception when computing UTF-8 length.", IllegalArgumentException.class, () -> Utf8.encodedLength(sequence));
    }

    @Test
    public void testEncodedLengthGeneral() {
        char[] sequence = {'a', 'b', 'c'};
        int start = 1;
        int length = Utf8.encodedLengthGeneral(sequence, start);
        int expectedLength = 3 - start;
        Assert.assertEquals("Unexpected UTF-8 length for given sequence and start index.", expectedLength, length);

        sequence = new char[]{'á', 'é', 'í'};
        start = 1;
        length = Utf8.encodedLengthGeneral(sequence, start);
        expectedLength = 6 - start;
        Assert.assertEquals("Unexpected UTF-8 length for given sequence, start index, and char index.", expectedLength, length);

        sequence = new char[]{'á', '\uD800', '\uDFFF'};
        start = 1;
        Assert.assertThrows("Unexpected exception when computing UTF-8 length.", IllegalArgumentException.class, () -> Utf8.encodedLengthGeneral(sequence, start));
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = {97, 98, 99};
        boolean result = Utf8.isWellFormed(bytes);
        assertTrue("UTF-8 sequence should be well-formed.", result);

        bytes = new byte[]{-69, -69, -69};
        result = Utf8.isWellFormed(bytes);
        assertTrue("UTF-8 sequence should be well-formed.", result);

        bytes = new byte[]{196, 168, 196, 185, 196, 160, 196, 168, 196, 185};
        result = Utf8.isWellFormed(bytes);
        assertTrue("UTF-8 sequence should be well-formed.", result);

        bytes = new byte[]{195, 162, 199, 178, 196, 160, 195, 162, 199, 168};
        result = Utf8.isWellFormed(bytes);
        assertFalse("UTF-8 sequence should not be well-formed.", result);
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = {97, 98, 99};
        boolean result = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertTrue("UTF-8 sequence should be well-formed.", result);

        bytes = new byte[]{-69, -69, -69, -69};
        result = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertTrue("UTF-8 sequence should be well-formed.", result);

        bytes = new byte[]{196, 168, 196, 185, 196, 160, 196, 168, 196, 185, 196, 160, 196, 168, 196, 185};
        result = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertTrue("UTF-8 sequence should be well-formed.", result);

        bytes = new byte[]{195, 162, 199, 178, 196, 160, 195, 162, 199, 168, 195, 162, 199, 168};
        result = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertFalse("UTF-8 sequence should not be well-formed.", result);
    }
}