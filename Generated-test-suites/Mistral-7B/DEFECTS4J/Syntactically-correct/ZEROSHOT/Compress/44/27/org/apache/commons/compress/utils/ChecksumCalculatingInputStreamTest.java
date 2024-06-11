package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.io.input.ByteArrayInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.util.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrderedData;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class ChecksumCalculatingInputStreamTest {

    private static final Log LOG = LogFactory.getLog(ChecksumCalculatingInputStreamTest.class);

    @Test
    public void testChecksumCalculatingInputStream_SingleByte() throws IOException, NoSuchAlgorithmException {
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final int read = checksumCalculatingInputStream.read();
        Assert.assertEquals("The read value should be equal to the input byte value.", 0x01, read);

        final byte[] expectedChecksumBytes = checksum.digest();
        final byte[] actualChecksumBytes = Arrays.copyOfRange(checksumCalculatingInputStream.getValue(), 0, expectedChecksumBytes.length);

        LOG.info("Expected checksum bytes: " + Arrays.toString(expectedChecksumBytes));
        LOG.info("Actual checksum bytes: " + Arrays.toString(actualChecksumBytes));

        Assert.assertArrayEquals("The checksum bytes should be equal.", expectedChecksumBytes, actualChecksumBytes);
    }

    @Test
    public void testChecksumCalculatingInputStream_ByteArray() throws IOException, NoSuchAlgorithmException {
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final byte[] inputByteArray = new byte[]{0x02, 0x03};
        final InputStream inputStream = new ByteArrayInputStream(inputByteArray);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final int read = checksumCalculatingInputStream.read(new byte[1][0]);
        Assert.assertEquals("The read value should be equal to the input byte value.", inputByteArray[0], read);

        final int read2 = checksumCalculatingInputStream.read(new byte[1][0], 0, 1);
        Assert.assertEquals("The read value should be equal to the input byte value.", inputByteArray[1], read2);

        final byte[] expectedChecksumBytes = checksum.digest();
        final byte[] actualChecksumBytes = Arrays.copyOfRange(checksumCalculatingInputStream.getValue(), 0, expectedChecksumBytes.length);

        LOG.info("Expected checksum bytes: " + Arrays.toString(expectedChecksumBytes));
        LOG.info("Actual checksum bytes: " + Arrays.toString(actualChecksumBytes));

        Assert.assertArrayEquals("The checksum bytes should be equal.", expectedChecksumBytes, actualChecksumBytes);
    }

    @Test
    public void testChecksumCalculatingInputStream_ByteArrayOutputStream() throws IOException, NoSuchAlgorithmException {
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] inputByteArray = new byte[]{0x04, 0x05};
        byte[] outputByteArray = new byte[]{0x06, 0x07};
        byte[] expectedChecksumBytes = new byte[]{0x12, (byte) 0x13, 0x14, 0x15, 0x16, 0x17};

        byteArrayOutputStream.write(inputByteArray, 0, inputByteArray.length);
        byteArrayOutputStream.flush();
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] readByteArray = new byte[outputByteArray.length];
        checksumCalculatingInputStream.read(readByteArray, 0, readByteArray.length);

        Assert.assertArrayEquals("The read byte array should be equal to the output byte array.", outputByteArray, readByteArray);

        byte[] expectedChecksumBytes2 = checksum.digest();
        byte[] actualChecksumBytes = Arrays.copyOfRange(checksumCalculatingInputStream.getValue(), 0, expectedChecksumBytes2.length);

        LOG.info("Expected checksum bytes: " + Arrays.toString(expectedChecksumBytes2));
        LOG.info("Actual checksum bytes: " + Arrays.toString(actualChecksumBytes));

        Assert.assertArrayEquals("The checksum bytes should be equal.", expectedChecksumBytes2, actualChecksumBytes);
    }

    @Test
    public void testChecksumCalculatingInputStream_Closed() throws IOException, NoSuchAlgorithmException {
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{0x08});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final int read = checksumCalculatingInputStream.read();
        Assert.assertEquals("The read value should be equal to the input byte value.", 0x08, read);

        checksumCalculatingInputStream.close();

        try {
            checksumCalculatingInputStream.read();
            Assert.fail("The checksum calculating input stream should throw an IOException when closed.");
        } catch (IOException e) {
            // Expected
        }

        final byte[] expectedChecksumBytes = checksum.digest();
        final byte[] actualChecksumBytes = Arrays.copyOfRange(checksumCalculatingInputStream.getValue(), 0, expectedChecksumBytes.length);

        LOG.info("Expected checksum bytes: " + Arrays.toString(expectedChecksumBytes));
        LOG.info("Actual checksum bytes: " + Arrays.toString(actualChecksumBytes));

        Assert.assertArrayEquals("The checksum bytes should be equal.", expectedChecksumBytes, actualChecksumBytes);
    }

    @Test
    public void testChecksumCalculatingInputStream_Serialization() throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{0x09});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final byte[] serializedChecksumCalculatingInputStream = SerializationUtils.serialize(checksumCalculatingInputStream);

        final InputStream deserializedChecksumCalculatingInputStream = new ByteArrayInputStream(serializedChecksumCalculatingInputStream);
        final ChecksumCalculatingInputStream deserializedChecksumCalculatingInputStreamObject = (ChecksumCalculatingInputStream) SerializationUtils.deserialize(serializedChecksumCalculatingInputStream);

        final int read = deserializedChecksumCalculatingInputStreamObject.read();
        Assert.assertEquals("The read value should be equal to the input byte value.", 0x09, read);

        final byte[] expectedChecksumBytes = checksum.digest();
        final byte[] actualChecksumBytes = Arrays.copyOfRange(deserializedChecksumCalculatingInputStreamObject.getValue(), 0, expectedChecksumBytes.length);

        LOG.info("Expected checksum bytes: " + Arrays.toString(expectedChecksumBytes));
        LOG.info("Actual checksum bytes: " + Arrays.toString(actualChecksumBytes));

        Assert.assertArrayEquals("The checksum bytes should be equal.", expectedChecksumBytes, actualChecksumBytes);
    }

    @Test
    public void testChecksumCalculatingInputStream_Skipped() throws IOException, NoSuchAlgorithmException {
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{0x0A});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final long skip = checksumCalculatingInputStream.skip(1);
        Assert.assertEquals("The skipped bytes count should be equal to the request.", 1, skip);

        final byte[] expectedChecksumBytes = checksum.digest();
        final byte[] actualChecksumBytes = Arrays.copyOfRange(checksumCalculatingInputStream.getValue(), 0, expectedChecksumBytes.length);

        LOG.info("Expected checksum bytes: " + Arrays.toString(expectedChecksumBytes));
        LOG.info("Actual checksum bytes: " + Arrays.toString(actualChecksumBytes));

        Assert.assertArrayEquals("The checksum bytes should be equal.", expectedChecksumBytes, actualChecksumBytes);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChecksumCalculatingInputStream_MarkSupported() throws IOException {
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{0x0B});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        checksumCalculatingInputStream.mark(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChecksumCalculatingInputStream_Reset() throws IOException {
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{0x0C});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        checksumCalculatingInputStream.reset();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChecksumCalculatingInputStream_CloseOnMark() throws IOException {
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{0x0D});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        checksumCalculatingInputStream.mark(0);
        checksumCalculatingInputStream.close();
    }
}