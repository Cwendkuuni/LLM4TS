package org.apache.commons.compress.compressors.deflate;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-37, 77, 77, 138, -125, -84, 121, 117, 105, 101, 116, 114, 116, 32, 83, 77, 83, 76, 67, 67, 65, 62, 61, 76, 65, 72, 73, 73, 73, 4, 32, 13, 10, 21, 5, 6, 2, 1}); // Deflate-compressed data
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[5];
        int actual = deflateStream.read(buffer, 0, 5);
        byte[] expected = new byte[]{1, 1, 1, 1, 1}; // First five bytes of the deflate stream
        Assert.assertArrayEquals(expected, buffer);
        Assert.assertEquals(5, actual);
    }

    @Test
    public void testReadByteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{2, 1, 2, 3, 4, 5, 6, 7, 8});
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[10];
        int actual = deflateStream.read(buffer, 0, 10);
        byte[] expected = new byte[]{2, 1, 2, 3, 4, 5, 6, 7, 8, 0}; // Read 10 bytes from the input stream
        Assert.assertArrayEquals(expected, buffer);
        Assert.assertEquals(10, actual);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);
        long actual = deflateStream.skip(5L);
        long expected = 5L;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);
        deflateStream.close();
        byte[] buffer = new byte[1];
        int actual = deflateStream.read(buffer, 0, 1);
        Assert.assertEquals(-1, actual); // EOFException should be thrown
    }

    @Test
    public void testIsZlibHeaderValid() {
        byte[] signature = new byte[]{0x7F, 0x45, 0x4C, 0x46, 0x01, 0x01};
        boolean actual = DeflateCompressorInputStream.isZlibHeader(signature, signature.length);
        Assert.assertTrue(actual);
    }

    @Test
    public void testIsZlibEdgeCase() {
        byte[] signature = new byte[0];
        boolean actual = DeflateCompressorInputStream.isZlibHeader(signature, 0);
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsZlibInvalid() {
        byte[] signature = new byte[]{0x7F, 0x45, 0x4C, 0x47, 0x01, 0x01};
        boolean actual = DeflateCompressorInputStream.isZlibHeader(signature, signature.length);
        Assert.assertFalse(actual);
    }

    @Test
    public void testReadWithNegativeLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[10];
        int actual = deflateStream.read(buffer, 0, -5); // Negative length
        Assert.assertEquals(-1, actual); // EOFException should be thrown
    }

    @Test
    public void testReadWithNullBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);
        int actual = deflateStream.read(null, 0, 10); // Null buffer
        Assert.assertNull(actual); // NullPointerException should be thrown
    }
}