package org.apache.commons.compress.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest {

    private BitInputStream bitInputStream;
    private InputStream inputStream;

    @Before
    public void setup() {
        inputStream = mock(InputStream.class);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testConstructor() {
        assertNotNull(bitInputStream);
        assertEquals(ByteOrder.LITTLE_ENDIAN, bitInputStream.byteOrder);
        assertNull(bitInputStream.in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeCount() {
        new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithLargeCount() {
        new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN, 64);
    }

    @Test
    public void testClearBitCache() throws IOException {
        when(inputStream.read()).thenReturn(-1L);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBitsWithZeroCount() throws IOException {
        when(inputStream.read()).thenReturn((byte) 0);
        assertEquals(0, bitInputStream.readBits(0));
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBitsWithOneBit() throws IOException {
        when(inputStream.read()).thenReturn((byte) 0x01);
        assertEquals(1, bitInputStream.readBits(1));
        assertEquals(1, bitInputStream.bitsCached);
        assertEquals(1, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBitsWithMultipleBits() throws IOException {
        when(inputStream.read()).thenReturn((byte) 0x0f);
        assertEquals(0b1111, bitInputStream.readBits(4));
        assertEquals(0b11110000, bitInputStream.bitsCached);
        assertEquals(8, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBitsWithMultipleBytes() throws IOException {
        ByteArrayInputStream inputStreamMock = new ByteArrayInputStream(new byte[]{0x0f, 0x01});
        bitInputStream = new BitInputStream(inputStreamMock, ByteOrder.LITTLE_ENDIAN);
        assertEquals(0b11110001, bitInputStream.readBits(8));
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test(expected = IOException.class)
    public void testReadBitsWithEndOfStreamReached() throws IOException {
        when(inputStream.read()).thenReturn((byte) 0x0f);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        when(inputStream.read()).thenReturn(-1L);
        bitInputStream.readBits(1);
    }
}