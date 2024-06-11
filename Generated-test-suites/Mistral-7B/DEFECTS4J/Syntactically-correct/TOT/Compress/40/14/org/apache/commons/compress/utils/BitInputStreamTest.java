package org.apache.commons.compress.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.utils.ByteOrder.*;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest {

    /**
     * Test method for {@link BitInputStream#BitInputStream(InputStream, ByteOrder)}.
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testConstruction() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0b11110000, (byte) 0b11111111, (byte) 0b01111111});
        BitInputStream bitInputStream = new BitInputStream(input, LITTLE_ENDIAN);
        Assert.assertEquals(LITTLE_ENDIAN, bitInputStream.getByteOrder());
        bitInputStream.close();
    }

    /**
     * Test method for {@link BitInputStream#clearBitCache()}.
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testClearBitCache() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0b11110000, (byte) 0b11111111, (byte) 0b01111111});
        BitInputStream bitInputStream = new BitInputStream(input, LITTLE_ENDIAN);
        long bits = bitInputStream.readBits(16);
        bitInputStream.clearBitCache();
        long zeroBits = bitInputStream.readBits(16);
        Assert.assertEquals(0, zeroBits);
        bitInputStream.close();
    }

    /**
     * Test method for {@link BitInputStream#readBits(int)}.
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testReadBits() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0b11110000, (byte) 0b11111111, (byte) 0b01111111});
        BitInputStream bitInputStream = new BitInputStream(input, LITTLE_ENDIAN);

        // Read 1 bit
        long bit1 = bitInputStream.readBits(1);
        Assert.assertEquals(1, bit1);

        // Read 5 bits
        long bits5 = bitInputStream.readBits(5);
        Assert.assertEquals(0b11111, bits5);

        // Read 64 bits
        long bits64 = bitInputStream.readBits(64);
        Assert.assertEquals(0xFFFFFFFFFFFFFFFFL, bits64);

        // Read beyond the end of the stream
        long bitBeyondEnd = bitInputStream.readBits(1);
        Assert.assertEquals(-1, bitBeyondEnd);

        // Test reading with BigEndian byte order
        BitInputStream bigEndianBitInputStream = new BitInputStream(input, BIG_ENDIAN);
        long bitsBigEndian = bigEndianBitInputStream.readBits(5);
        Assert.assertEquals(0b11111, bitsBigEndian << 3);

        bitInputStream.close();
        bigEndianBitInputStream.close();
    }
}