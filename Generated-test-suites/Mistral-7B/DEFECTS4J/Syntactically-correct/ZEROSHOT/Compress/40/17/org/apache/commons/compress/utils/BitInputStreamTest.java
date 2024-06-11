package org.apache.commons.compress.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.Assert.*;

/**
 * JUnit test class for BitInputStream.
 */
public class BitInputStreamTest {

    private static final byte[] TEST_DATA = new byte[]{'A', 'B', 'C', 'D'};
    private static final ByteOrder LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
    private static final ByteOrder BIG_ENDIAN = ByteOrder.BIG_ENDIAN;
    private InputStream inputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() throws IOException {
        inputStream = new ByteArrayInputStream(TEST_DATA);
        bitInputStream = new BitInputStream(inputStream, LITTLE_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        if (bitInputStream != null) {
            bitInputStream.close();
            inputStream.close();
        }
    }

    @Test
    public void testBitInputStreamConstructor() {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        try {
            new BitInputStream(inputStream, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        new BitInputStream(null, LITTLE_ENDIAN);
        new BitInputStream(inputStream, null);
        new BitInputStream(null, ByteOrder.LITTLE_ENDIAN);

        bitInputStream = new BitInputStream(inputStream, BIG_ENDIAN);
        assertEquals(BIG_ENDIAN, bitInputStream.getByteOrder());
    }

    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBitsCountLessThanOne() throws IOException {
        assertEquals(0, bitInputStream.readBits(0));
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBitsCountGreaterThanMaximum() throws IOException {
        bitInputStream.clearBitCache();
        assertEquals(-1, bitInputStream.readBits(64));
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);

        bitInputStream.clearBitCache();
        bitInputStream.read(new byte[TEST_DATA.length]);
        assertEquals(-1, bitInputStream.readBits(65));
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBitsCountLessThanOrEqualToMaximum() throws IOException {
        // read first byte
        assertEquals(TEST_DATA[0], (byte) ((bitInputStream.readBits(8) >> 0) & 0xFF));
        assertEquals(TEST_DATA[0], (byte) ((bitInputStream.readBits(8) >> 0) & 0xFF));
        assertEquals(8, bitInputStream.bitsCachedSize);

        // read second byte
        assertEquals(TEST_DATA[1], (byte) ((bitInputStream.readBits(8) >> 0) & 0xFF));
        assertEquals(TEST_DATA[1], (byte) ((bitInputStream.readBits(8) >> 0) & 0xFF));
        assertEquals(8, bitInputStream.bitsCachedSize);

        // read remaining bits
        assertEquals(TEST_DATA[2] & 0x01, (byte) ((bitInputStream.readBits(1) >> 0) & 0x01));
        assertEquals(TEST_DATA[2] & 0x02, (byte) ((bitInputStream.readBits(1) >> 0) & 0x02));
        assertEquals(TEST_DATA[2] & 0x04, (byte) ((bitInputStream.readBits(1) >> 0) & 0x04));
        assertEquals(TEST_DATA[2] & 0x08, (byte) ((bitInputStream.readBits(1) >> 0) & 0x08));
        assertEquals(TEST_DATA[2] & 0x10, (byte) ((bitInputStream.readBits(1) >> 0) & 0x10));
        assertEquals(TEST_DATA[2] & 0x20, (byte) ((bitInputStream.readBits(1) >> 0) & 0x20));
        assertEquals(TEST_DATA[2] & 0x40, (byte) ((bitInputStream.readBits(1) >> 0) & 0x40));
        assertEquals(TEST_DATA[2] & 0x80, (byte) ((bitInputStream.readBits(1) >> 0) & 0x80));

        assertEquals(-1, bitInputStream.readBits(1));
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBitsLittleEndian() throws IOException {
        bitInputStream = new BitInputStream(inputStream, LITTLE_ENDIAN);
        byte expectedByte = 0x0A;

        // read first byte
        assertEquals(expectedByte, (byte) ((bitInputStream.readBits(8) >> 0) & 0xFF));
        assertEquals(expectedByte, (byte) ((bitInputStream.readBits(8) >> 0) & 0xFF));
        assertEquals(8, bitInputStream.bitsCachedSize);

        // read second byte
        assertEquals(TEST_DATA[1], (byte) ((bitInputStream.readBits(8) >> 0) & 0xFF));
        assertEquals(TEST_DATA[1], (byte) ((bitInputStream.readBits(8) >> 0) & 0xFF));
        assertEquals(8, bitInputStream.bitsCachedSize);

        // read remaining bits
        assertEquals(TEST_DATA[0] & 0x80, (byte) ((bitInputStream.readBits(1) >> 7) & 0x01));
        assertEquals(TEST_DATA[0] & 0x40, (byte) ((bitInputStream.readBits(1) >> 6) & 0x01));
        assertEquals(TEST_DATA[0] & 0x20, (byte) ((bitInputStream.readBits(1) >> 5) & 0x01));
        assertEquals(TEST_DATA[0] & 0x10, (byte) ((bitInputStream.readBits(1) >> 4) & 0x01));
        assertEquals(TEST_DATA[0] & 0x08, (byte) ((bitInputStream.readBits(1) >> 3) & 0x01));
        assertEquals(TEST_DATA[0] & 0x04, (byte) ((bitInputStream.readBits(1) >> 2) & 0x01));
        assertEquals(TEST_DATA[0] & 0x02, (byte) ((bitInputStream.readBits(1) >> 1) & 0x01));
        assertEquals(TEST_DATA[0] & 0x01, (byte) ((bitInputStream.readBits(1) >> 0) & 0x01));

        assertEquals(-1, bitInputStream.readBits(1));
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBitsBigEndian() throws IOException {
        bitInputStream = new BitInputStream(inputStream, BIG_ENDIAN);
        byte expectedByte = 0x0A;

        // read first byte
        assertEquals(TEST_DATA[3], (byte) ((bitInputStream.readBits(8) >> 0) & 0xFF));
        assertEquals(TEST_DATA[3], (byte) ((bitInputStream.readBits(8) >> 0) & 0xFF));
        assertEquals(8, bitInputStream.bitsCachedSize);

        // read second byte
        assertEquals(TEST_DATA[2], (byte) ((bitInputStream.readBits(8) >> 0) & 0xFF));
        assertEquals(TEST_DATA[2], (byte) ((bitInputStream.readBits(8) >> 0) & 0xFF));
        assertEquals(8, bitInputStream.bitsCachedSize);

        // read remaining bits
        assertEquals(TEST_DATA[1] & 0x01, (byte) ((bitInputStream.readBits(1) >> 0) & 0x01));
        assertEquals(TEST_DATA[1] & 0x02, (byte) ((bitInputStream.readBits(1) >> 1) & 0x01));
        assertEquals(TEST_DATA[1] & 0x04, (byte) ((bitInputStream.readBits(1) >> 2) & 0x01));
        assertEquals(TEST_DATA[1] & 0x08, (byte) ((bitInputStream.readBits(1) >> 3) & 0x01));
        assertEquals(TEST_DATA[1] & 0x10, (byte) ((bitInputStream.readBits(1) >> 4) & 0x01));
        assertEquals(TEST_DATA[1] & 0x20, (byte) ((bitInputStream.readBits(1) >> 5) & 0x01));
        assertEquals(TEST_DATA[1] & 0x40, (byte) ((bitInputStream.readBits(1) >> 6) & 0x01));
        assertEquals(TEST_DATA[1] & 0x80, (byte) ((bitInputStream.readBits(1) >> 7) & 0x01));

        assertEquals(-1, bitInputStream.readBits(1));
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }
}