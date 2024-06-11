package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest {

    private static final int TEST_COUNT = 64;
    private static final String TEST_DATA = "TestData";

    private BitInputStream bitInputStream;
    private ByteArrayInputStream inputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        inputStream = new ByteArrayInputStream(TEST_DATA.getBytes());
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        outputStream = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
        inputStream.close();
        outputStream.close();
    }

    /**
     * Test reading single bit.
     */
    @Test
    public void testReadBit() throws IOException {
        Assert.assertEquals(TEST_DATA.charAt(0) & 0x01, bitInputStream.readBits(1) & 0x01);
    }

    /**
     * Test reading multiple bits.
     */
    @Test
    public void testReadMultipleBits() throws IOException {
        Assert.assertEquals((TEST_DATA.charAt(0) << 4) & 0xF0, bitInputStream.readBits(4));
    }

    /**
     * Test reading bits bigger than available.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsBiggerThanAvailable() throws IOException {
        bitInputStream.readBits(65);
    }

    /**
     * Test reading bits from empty stream.
     */
    @Test(expected = IOException.class)
    public void testReadBitsFromEmptyStream() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[0]);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(1);
    }

    /**
     * Test clearing bit cache.
     */
    @Test
    public void testClearBitCache() throws IOException {
        final int readBitsCount = 128;
        for (int i = 0; i < readBitsCount; i += TEST_COUNT) {
            bitInputStream.readBits(TEST_COUNT);
        }
        bitInputStream.clearBitCache();
        try {
            bitInputStream.readBits(1);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    /**
     * Test reading data from the stream to an output stream.
     */
    @Test
    public void testReadData() throws IOException {
        final int dataLength = 128;
        final int bitsPerByte = 8;
        byte[] expectedData = new byte[dataLength / bitsPerByte];
        bitInputStream.readBits(expectedData.length * bitsPerByte);
        byte[] actualData = outputStream.toByteArray();
        Assert.assertArrayEquals(expectedData, actualData);
    }
}