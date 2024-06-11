package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Test file for BitInputStream class.
 */
public class BitInputStreamTest {

    @Test
    public void testBitInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x1a, (byte) 0x2b});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        // Test readBits method with valid arguments
        Assert.assertEquals(0x1a << 0, bitInputStream.readBits(1));
        Assert.assertEquals((0x1a & 0x01) << 0, bitInputStream.readBits(1));
        Assert.assertEquals((0x1a >> 7) << 5, bitInputStream.readBits(3));
        Assert.assertEquals((0x1a >> 1) << 3, bitInputStream.readBits(3));
        Assert.assertEquals((0x1a >> 5) << 1, bitInputStream.readBits(3));
        Assert.assertEquals((0x1a & 0x1f) << 3, bitInputStream.readBits(5));
        Assert.assertEquals((0x1a << 3) | ((0x2b >> 5) & 0x1f), bitInputStream.readBits(7));
        Assert.assertEquals(0x1a << 3 | ((0x2b >> 1) << 5), bitInputStream.readBits(7));
        Assert.assertEquals(0x1a << 7 | ((0x2b) << 1), bitInputStream.readBits(8));

        // Test readBits method with invalid arguments
        try {
            bitInputStream.readBits(64);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        try {
            bitInputStream.readBits(-1);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        // Test clearBitCache method
        bitInputStream.clearBitCache();
        Assert.assertEquals(0, bitInputStream.readBits(1));

        // Test close method
        bitInputStream.close();
        try {
            bitInputStream.readBits(1);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }
}