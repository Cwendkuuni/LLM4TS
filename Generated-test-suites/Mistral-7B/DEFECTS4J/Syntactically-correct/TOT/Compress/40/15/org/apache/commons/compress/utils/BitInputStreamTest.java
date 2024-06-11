package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Test;

/**
 * Test cases for {@link BitInputStream}.
 */
public class BitInputStreamTest {

    @Test
    public void testReadBits_givenEmptyInputStream_whenReadBits_thenReturnsMinusOne() throws IOException {
        final InputStream emptyInputStream = mock(InputStream.class);
        final BitInputStream bitInputStream = new BitInputStream(emptyInputStream, ByteOrder.LITTLE_ENDIAN);
        final long result = bitInputStream.readBits(1);
        assertEquals(-1, result);
    }

    @Test
    public void testReadBits_givenInputStreamWithOneByte_whenReadOneBit_thenReturnsCorrectBit() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 1 });
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        final long result = bitInputStream.readBits(1);
        assertEquals(1, result);
    }

    @Test
    public void testReadBits_givenInputStreamWithMultipleBytes_whenReadSeveralBits_thenReturnsCorrectBits()
            throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0b11110111, 0x01, 0x02 });
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        final long result1 = bitInputStream.readBits(3);
        assertEquals(0b111, result1);

        final long result2 = bitInputStream.readBits(3);
        assertEquals(0b011, result2);

        final long result3 = bitInputStream.readBits(2);
        assertEquals(0b01, result3);

        final long result4 = bitInputStream.readBits(1);
        assertEquals(0b0, result4);

        final long result5 = bitInputStream.readBits(2);
        assertEquals(0b01, result5);

        final long result6 = bitInputStream.readBits(3);
        assertEquals(0b011, result6);
    }

    @Test
    public void testReadBits_givenInputStreamWithMultiByteValue_whenReadSeveralBits_thenReturnsCorrectValue()
            throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0b11111111, 0x01, 0x02 });
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        final long value1 = bitInputStream.readBits(8);
        assertEquals(0xFF, value1);

        final long value2 = bitInputStream.readBits(8);
        assertEquals(0x01, value2);

        final long value3 = bitInputStream.readBits(8);
        assertEquals(0x02, value3);
    }

    @Test(expected = IOException.class)
    public void testReadBits_givenInputStreamWithError_whenReadBits_thenThrowsIOException() throws IOException {
        final InputStream errorInputStream = mock(InputStream.class);
        final BitInputStream bitInputStream = new BitInputStream(errorInputStream, ByteOrder.LITTLE_ENDIAN);
        when(errorInputStream.read()).thenThrow(new IOException());
        bitInputStream.readBits(1);
    }

    @Test
    public void testClearBitCache_givenBitInputStreamWithCache_whenClearBitCache_thenCacheIsEmpty() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01 });
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(1);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCachedSize);
    }
}