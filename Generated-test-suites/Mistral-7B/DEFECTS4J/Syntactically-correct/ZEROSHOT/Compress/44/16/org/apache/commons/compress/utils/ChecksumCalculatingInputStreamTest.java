package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.ChecksumUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

/**
 * Test class for ChecksumCalculatingInputStream.
 */
public class ChecksumCalculatingInputStreamTest {

    private static final int TEST_DATA_SIZE = 1024;
    private static final byte[] TEST_DATA = new byte[TEST_DATA_SIZE];

    private InputStream testInputStream;
    private ChecksumCalculatingInputStream checksumCalculatingInputStream;
    private long checksumExpectedValue;

    @Before
    public void setUp() throws IOException, NoSuchAlgorithmException {
        // Initialize test data
        new ByteArrayOutputStream(TEST_DATA_SIZE).write(TEST_DATA);
        testInputStream = new ByteArrayInputStream(TEST_DATA);

        // Initialize checksumCalculatingInputStream
        final MessageDigest md = MessageDigest.getInstance("MD5");
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(new ChecksumUtils.Checksum(md), testInputStream);
        checksumExpectedValue = md.digest();
    }

    @After
    public void tearDown() throws IOException {
        if (checksumCalculatingInputStream != null) {
            checksumCalculatingInputStream.close();
        }
    }

    @Test
    public void testReadByte() throws IOException {
        final int readByte = checksumCalculatingInputStream.read();
        assertEquals(TEST_DATA[0], (byte) readByte);
    }

    @Test
    public void testReadByteArray() throws IOException {
        final byte[] byteArray = new byte[1];
        final int readLength = checksumCalculatingInputStream.read(byteArray);
        assertEquals(1, readLength);
        assertEquals(TEST_DATA[0], byteArray[0]);
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        final byte[] byteArray = new byte[TEST_DATA_SIZE];
        final int readLength = checksumCalculatingInputStream.read(byteArray, 0, TEST_DATA_SIZE);
        assertEquals(TEST_DATA_SIZE, readLength);
        assertEquals(TEST_DATA, byteArray);
    }

    @Test
    public void testSkip() throws IOException {
        final long skippedBytes = TEST_DATA_SIZE / 2;
        final long newPosition = checksumCalculatingInputStream.skip(skippedBytes);
        assertEquals(skippedBytes, newPosition);
        final byte[] byteArray = new byte[TEST_DATA_SIZE / 2];
        final int readLength = checksumCalculatingInputStream.read(byteArray, 0, TEST_DATA_SIZE / 2);
        assertEquals(TEST_DATA_SIZE / 2, readLength);
        for (int i = 0; i < TEST_DATA_SIZE / 2; i++) {
            assertEquals(TEST_DATA[i + skippedBytes], byteArray[i]);
        }
    }

    @Test
    public void testGetValue() throws IOException {
        final byte[] expectedChecksum = checksumCalculatingInputStream.getValue();
        final byte[] calculatedChecksum = md.digest();
        assertEquals(expectedChecksum, calculatedChecksum);
    }
}