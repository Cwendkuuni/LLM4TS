package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test class for {@link BitInputStream}.
 */
public class BitInputStreamTest {

    private InputStream inputStream;
    private ByteOrder byteOrder;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() throws Exception {
        inputStream = new MockInputStream();
        byteOrder = ByteOrder.LITTLE_ENDIAN;
        bitInputStream = new BitInputStream(inputStream, byteOrder);
    }

    @After
    public void tearDown() throws Exception {
        bitInputStream.close();
        inputStream.close();
    }

    @Test
    public void testReadBits_ValidCount() throws Exception {
        // Test reading 1, 5, 12, 16, 48, 63 bits
        for (int count : new int[] {1, 5, 12, 16, 48, 63}) {
            long expectedBits = 0x123456789ABCDEF0L << count;
            long actualBits = bitInputStream.readBits(count);
            assertEquals(expectedBits, actualBits);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_InvalidCount() throws Exception {
        // Test reading a negative or invalid number of bits
        bitInputStream.readBits(-1);
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBits_EndOfStream() throws Exception {
        inputStream.pushByte((byte) 0xFF);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(8);

        // Read past the end of the stream
        assertThrows(IOException.class, () -> bitInputStream.readBits(1));
    }

    @Test
    public void testClearBitCache() throws Exception {
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.clearBitCache();

        // The cache should be empty
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    // Add more test cases here as needed
}

class MockInputStream implements InputStream {
    private byte[] buffer = new byte[]{(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78};
    private int index = 0;

    @Override
    public int read() throws IOException {
        if (index < buffer.length) {
            return buffer[index++] & 0xFF;
        } else {
            return -1;
        }
    }

    @Override
    public void close() throws IOException {
        // No-op
    }

    @Override
    public synchronized void mark(int readlimit) {
        // No-op
    }

    @Override
    public synchronized void reset() throws IOException {
        // No-op
    }

    public void pushByte(byte b) {
        buffer = new byte[] {b};
        index = 0;
    }
}