package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.ChecksumUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import org.junit.Test;

/**
 * Test class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

    /**
     * Test method for {@link ChecksumCalculatingInputStream#ChecksumCalculatingInputStream(Checksum, InputStream)} constructors.
     * @throws IOException
     */
    @Test
    public void testChecksumCalculatingInputStreamConstruction() throws IOException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        InputStream input = new ByteArrayInputStream(new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5 });
        Checksum checksum = ChecksumUtils.getChecksum(md);
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);
        assertNotNull(cis);
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#read()} method.
     * @throws IOException
     */
    @Test
    public void testRead() throws IOException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        InputStream input = new ByteArrayInputStream(new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5 });
        Checksum checksum = ChecksumUtils.getChecksum(md);
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);

        final int readByte = cis.read();
        assertEquals(0x1, readByte);

        final int readByte2 = cis.read();
        assertEquals(0x2, readByte2);

        final int readByte3 = cis.read();
        assertEquals(0x3, readByte3);

        final int readByte4 = cis.read();
        assertEquals(0x4, readByte4);

        final int readByte5 = cis.read();
        assertEquals(0x5, readByte5);

        // Check that the checksum is correct
        final long expectedChecksum = 15722691305932016771L;
        assertEquals(expectedChecksum, cis.getValue());
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#read(byte[], int, int)} method.
     * @throws IOException
     */
    @Test
    public void testReadByteArray() throws IOException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] data = new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5 };
        InputStream input = new ByteArrayInputStream(data);
        Checksum checksum = ChecksumUtils.getChecksum(md);
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);

        final byte[] readBuffer = new byte[5];
        int length = cis.read(readBuffer, 0, 5);
        assertEquals(5, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[i], readBuffer[i]);
        }

        // Check that the checksum is correct
        final long expectedChecksum = 15722691305932016771L;
        assertEquals(expectedChecksum, cis.getValue());
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#skip(long)} method.
     * @throws IOException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSkip() throws IOException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        InputStream input = new ByteArrayInputStream(new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5 });
        Checksum checksum = ChecksumUtils.getChecksum(md);
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);
        cis.skip(1);
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#getValue()} method.
     * @throws IOException
     */
    @Test
    public void testGetValue() throws IOException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        InputStream input = new ByteArrayInputStream(new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5 });
        Checksum checksum = ChecksumUtils.getChecksum(md);
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);

        // Read all the data
        cis.read();
        cis.read();
        cis.read();
        cis.read();
        cis.read();

        final long actualChecksum = cis.getValue();
        final long expectedChecksum = 15722691305932016771L;
        assertEquals(expectedChecksum, actualChecksum);
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#close()} method.
     * @throws IOException
     */
    @Test
    public void testClose() throws IOException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        InputStream input = new ByteArrayInputStream(new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5 });
        Checksum checksum = ChecksumUtils.getChecksum(md);
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);

        // Read all the data
        cis.read();
        cis.read();
        cis.read();
        cis.read();
        cis.read();

        // Close the stream
        cis.close();

        // The checksum should be the same
        final long actualChecksum = cis.getValue();
        final long expectedChecksum = 15722691305932016771L;
        assertEquals(expectedChecksum, actualChecksum);
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#close()} method.
     * @throws IOException
     */
    @Test
    public void testCloseTwice() throws IOException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        InputStream input = new ByteArrayInputStream(new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5 });
        Checksum checksum = ChecksumUtils.getChecksum(md);
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);

        // Read all the data
        cis.read();
        cis.read();
        cis.read();
        cis.read();
        cis.read();

        // Close the stream
        cis.close();
        cis.close();

        // The checksum should be the same
        final long actualChecksum = cis.getValue();
        final long expectedChecksum = 15722691305932016771L;
        assertEquals(expectedChecksum, actualChecksum);
    }
}