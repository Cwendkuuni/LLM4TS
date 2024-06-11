package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * Test for {@link BitInputStream} class.
 */
public class BitInputStreamTest {

    private InputStream inputStream;

    @Before
    public void setUp() {
        byte[] testData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        inputStream = new ByteArrayInputStream(testData);
    }

    @Test
    public void testReadBitsWhenInputValid() throws IOException {
        // Create BitInputStream with test data
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        // Read 1 bit
        long bit1 = bitInputStream.readBits(1);
        assertEquals(0x01 & 0x01, bit1);

        // Read 6 bits
        long bits6 = bitInputStream.readBits(6);
        assertEquals(0x21, bits6);

        // Read 7 bits from end of stream
        assertThrows(IOException.class, () -> bitInputStream.readBits(7));

        // Clear bit cache
        bitInputStream.clearBitCache();

        // Read 1 bit after clearing cache
        long bit1AfterClear = bitInputStream.readBits(1);
        assertFalse(bit1AfterClear > 0);
    }

    @Test
    public void testReadBitsWhenInputStreamClosed() throws IOException {
        // Create BitInputStream with test data
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        // Close input stream
        inputStream.close();

        // Read bits from closed stream
        assertThrows(IOException.class, () -> bitInputStream.readBits(1));
    }

    @Test
    public void testReadBitsWhenNegativeCount() throws IOException {
        // Create BitInputStream with test data
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        // Test reading bits with negative count
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(-1));
    }

    @Test
    public void testReadBitsWhenCountBiggerThan63() throws IOException {
        // Create BitInputStream with test data
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        // Test reading bits with count bigger than 63
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(64));
    }
}