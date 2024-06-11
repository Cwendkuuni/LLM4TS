package org.apache.commons.compress.utils;

import static org.junit.Assert.fail;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

/**
 * Test class for BitInputStream
 */
public class BitInputStreamTest {

    private ByteArrayInputStream inputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[]{(byte) 0b11110110, (byte) 0b10101010, (byte) 0b11111111});
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
        inputStream.close();
    }

    @Test
    public void testConstructors() {
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03});
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        assert (bitInputStream.byteOrder == byteOrder);
        assert (bitInputStream.in == inputStream);
    }

    @Test
    public void testClose() throws IOException {
        bitInputStream.close();
        try {
            bitInputStream.readBits(1);
            fail("Expected IOException after close()");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.readBits(8);
        bitInputStream.clearBitCache();
        long bits = bitInputStream.readBits(3);
        assert (bits == 0);
    }

    @Test
    public void testMaximumCacheSize() throws IOException {
        for (int i = 0; i <= 63; i++) {
            long bits = bitInputStream.readBits(i);
            if (bits == -1) {
                fail("Expected to read " + i + " bits, but end of stream reached.");
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsGreaterCacheSizeCount() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBits() throws IOException {
        long expected = 0b111101101010101111111111;
        for (int i = 1; i <= 16; i++) {
            long bits = bitInputStream.readBits(i);
            long mask = MASKS[i];
            long expectedMasked = (expected & mask) >>> (32 - Integer.numberOfLeadingZeros(i));
            assert (bits == expectedMasked);
            expected &= ~mask;
        }
        long bits = bitInputStream.readBits(16);
        assert (bits == -1);
    }
}