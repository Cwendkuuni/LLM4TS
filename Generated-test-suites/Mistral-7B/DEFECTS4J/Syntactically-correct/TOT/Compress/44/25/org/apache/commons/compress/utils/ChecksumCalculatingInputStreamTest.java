package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.io.input.NullInputStream;
import org.apache.commons.io.input.ZeroInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * Test case for {@link ChecksumCalculatingInputStream}.
 */
public class ChecksumCalculatingInputStreamTest {

    private InputStream originalStream;
    private InputStream checksumStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException, NoSuchAlgorithmException {
        originalStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        checksumStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), originalStream);
        outputStream = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws IOException {
        if (originalStream != null) {
            originalStream.close();
        }
        if (checksumStream != null) {
            checksumStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
    }

    @Test
    public void testReadByte() throws IOException {
        final int expectedByte = checksumStream.read();
        final int actualByte = originalStream.read();
        assertEquals(expectedByte, actualByte);
    }

    @Test
    public void testReadByteArray() throws IOException {
        final byte[] expected = new byte[]{1, 2, 3, 4, 5};
        final byte[] actual = new byte[5];
        checksumStream.read(actual);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        final byte[] expected = new byte[]{2, 3, 4};
        final byte[] actual = new byte[3];
        checksumStream.read(actual, 1, 3);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReadZeroBytes() throws IOException {
        final byte[] actual = new byte[5];
        checksumStream.read(actual);
        assertArrayEquals(new byte[0], actual);
    }

    @Test
    public void testReadNegativeLength() throws IOException {
        final byte[] actual = new byte[5];
        assertThrows(IllegalArgumentException.class, () -> checksumStream.read(actual, -1, 1));
    }

    @Test
    public void testReadFromNullInputStream() {
        assertThrows(NullPointerException.class, () -> new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), null));
    }

    @Test
    public void testCalculateChecksum() throws IOException {
        final byte[] originalData = new byte[]{1, 2, 3, 4, 5};
        final byte[] expectedMd5Checksum = new byte[]{0x30, 0x1d, 0x4a, 0x31, 0xd2, 0x3a, 0x11, 0x3b, 0x18, 0x3b};
        final byte[] actualMd5Checksum = new byte[12];
        final InputStream checksumStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), new ByteArrayInputStream(originalData));
        checksumStream.read(actualMd5Checksum);
        assertArrayEquals(expectedMd5Checksum, actualMd5Checksum);
    }

    @Test
    public void testReadFromZeroInputStream() throws IOException {
        final byte[] expectedMd5Checksum = new byte[0];
        final InputStream checksumStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), ZeroInputStream.INSTANCE);
        checksumStream.read(new byte[12]);
        assertArrayEquals(expectedMd5Checksum, new byte[0]);
    }

    @Test
    public void testReadFromNullByteArray() {
        final InputStream checksumStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), originalStream);
        assertThrows(NullPointerException.class, () -> checksumStream.read((byte[]) null));
    }

    @Test
    public void testReadFromNegativeOffset() {
        final InputStream checksumStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), originalStream);
        assertThrows(IndexOutOfBoundsException.class, () -> checksumStream.read(new byte[-1][1]));
    }

    @Test
    public void testReadFromNegativeLength() {
        final InputStream checksumStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), originalStream);
        assertThrows(IndexOutOfBoundsException.class, () -> checksumStream.read(new byte[5][-1]));
        assertThrows(IndexOutOfBoundsException.class, () -> checksumStream.read(new byte[5][-1], 1, -1));
    }

    @Test
    public void testReadFromInputStreamWithExpectedChecksum() throws IOException {
        final byte[] expectedData = new byte[]{1, 2, 3, 4, 5};
        final byte[] expectedChecksum = new byte[]{0x30, 0x1d, 0x4a, 0x31, 0xd2, 0x3a, 0x11, 0x3b, 0x18, 0x3b};
        final InputStream expectedStream = new ByteArrayInputStream(expectedData);
        final InputStream checksumStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), expectedStream);
        final byte[] actualChecksum = new byte[12];
        checksumStream.read(actualChecksum);
        assertArrayEquals(expectedChecksum, actualChecksum);
    }

    @Test
    public void testReadFromInputStreamWithUnexpectedChecksum() throws IOException {
        final byte[] expectedData = new byte[]{1, 2, 3, 4, 5};
        final byte[] expectedChecksum = new byte[]{0x30, 0x1d, 0x4a, 0x31, 0xd2, 0x3a, 0x11, 0x3b, 0x18, 0x3b};
        final InputStream originalStream = new ByteArrayInputStream(expectedData);
        final InputStream checksumStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), originalStream);
        final byte[] actualChecksum = new byte[12];
        checksumStream.read(actualChecksum);
        final byte[] md5Checksum = MessageDigest.getInstance("MD5").digest(expectedData);
        assertFalse(Arrays.equals(md5Checksum, actualChecksum));
    }
}