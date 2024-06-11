package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test suite for BitInputStream class.
 */
public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    @Before
    public void setUp() throws IOException {
        byte[] testData = {0x01, 0x23, 0x45, 0x67};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testData);
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        if (bitInputStream != null) {
            bitInputStream.close();
        }
    }

    @Test
    public void testReadBitsWithValidCount() throws IOException {
        for (int i = 0; i <= 63; i += 8) {
            long expected = (long) testData[i / 8] << (i % 8);
            long actual = bitInputStream.readBits(i);
            assertEquals(expected, actual);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithInvalidNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithInvalidCountBiggerThanMaximum() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testClearBitCache() throws IOException {
        long bits = 0xFFFFFFFFFFFFFL;
        for (int i = 0; i <= 63; i += 8) {
            bitInputStream.readBits(i);
            bitInputStream.clearBitCache();
        }
        long actual = bitInputStream.readBits(64);
        assertEquals(-1, actual);
    }

    @Test
    public void testReadBitsMultipleTimesWithSmallCount() throws IOException {
        long expected = 0x01_02_04_08;
        long actual = 0;
        actual = bitInputStream.readBits(3);
        assertEquals(0x01, actual);
        actual = bitInputStream.readBits(3);
        assertEquals(0x02, actual);
        actual = bitInputStream.readBits(3);
        assertEquals(0x04, actual);
        actual = bitInputStream.readBits(2);
        assertEquals(0x08, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testReadBitsMultipleTimesWithBigCount() throws IOException {
        long expected = 0x01_02_04_08_16_20_24_28_30_32_34_36_37_40_44;
        long actual = 0;
        for (int i = 13; i <= 27; i += 8) {
            actual = bitInputStream.readBits(i);
        }
        assertEquals(expected, actual);
    }
}