import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

    /**
     * Test that data is correctly read and hashed.
     */
    @Test
    public void testReadAndHash() throws Exception {
        final String testData = "This is a test";

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(testData.getBytes(StandardCharsets.UTF_8));
        final InputStream input = new ByteArrayInputStream(output.toByteArray());
        final ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(Checksums.adler32(), input);

        final byte[] expectedAdler = new byte[4];
        MessageDigest adlerDigest = MessageDigest.getInstance("Adler-32");
        adlerDigest.update(testData.getBytes(StandardCharsets.UTF_8));
        adlerDigest.getByteArray(expectedAdler);

        final byte[] readBuffer = new byte[testData.length()];
        int read;
        while ((read = checksumStream.read(readBuffer)) > 0) {
            // empty
        }
        final long checksum = checksumStream.getValue();

        Assertions.assertArrayEquals(expectedAdler, new byte[]{(byte) 0x2D, (byte) 0x18, (byte) 0x36, (byte) 0x1F}, "Checksum does not match");
        Assertions.assertEquals(expectedAdler.length, checksum, "Checksum length does not match");
    }

    /**
     * Test that reading from an empty input stream returns -1 and does not update the checksum.
     */
    @Test
    public void testEmptyStream() throws IOException {
        final ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(Checksums.adler32(), new ByteArrayInputStream(new byte[0][0]));
        final int read = checksumStream.read();
        assertEquals(-1, read);
        assertEquals(0, checksumStream.getValue());
    }

    /**
     * Test that reading past the end of the stream returns -1 and does not update the checksum.
     */
    @Test
    public void testReadPastEnd() throws IOException {
        final String testData = "This is a test";

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(testData.getBytes(StandardCharsets.UTF_8));
        final InputStream input = new ByteArrayInputStream(output.toByteArray());
        final ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(Checksums.adler32(), input);

        final byte[] readBuffer = new byte[testData.length() * 2];
        int read;
        final int expectedRead = input.read(readBuffer, 0, readBuffer.length);
        read = checksumStream.read(readBuffer, 0, readBuffer.length);

        Assertions.assertEquals(expectedRead, read);
        Assertions.assertEquals(-1, read);
        Assertions.assertEquals(0, checksumStream.getValue());
    }

    /**
     * Test that the checksum is correctly updated even if data is read in smaller chunks.
     */
    @Test
    public void testPartialReads() throws IOException {
        final String testData = "This is a test";

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(testData.getBytes(StandardCharsets.UTF_8));
        final InputStream input = new ByteArrayInputStream(output.toByteArray());
        final ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(Checksums.adler32(), input);

        final byte[] readBuffer = new byte[testData.length() / 2];
        int read;
        while ((read = checksumStream.read(readBuffer)) > 0) {
            // empty
        }
        final long checksum = checksumStream.getValue();

        final byte[] expectedAdler = new byte[4];
        MessageDigest adlerDigest = MessageDigest.getInstance("Adler-32");
        adlerDigest.update(testData.getBytes(StandardCharsets.UTF_8));
        adlerDigest.getByteArray(expectedAdler);

        Assertions.assertArrayEquals(expectedAdler, new byte[]{(byte) 0x2D, (byte) 0x18, (byte) 0x36, (byte) 0x1F}, "Checksum does not match");
        Assertions.assertEquals(expectedAdler.length, checksum, "Checksum length does not match");
    }

    /**
     * Test that incorrect checksums are detected when reading from the stream.
     */
    @Test
    public void testInvalidChecksum() throws IOException, NoSuchAlgorithmException {
        final String testData = "This is a test";

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(testData.getBytes(StandardCharsets.UTF_8));
        final InputStream input = new ByteArrayInputStream(output.toByteArray());
        final ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(Checksums.adler32(), input);

        final byte[] readBuffer = new byte[testData.length()];
        int read;
        while ((read = checksumStream.read(readBuffer)) > 0) {
            // empty
        }

        // Provide incorrect checksum
        final long incorrectChecksum = 0x12345678;
        Assertions.assertThrows(IOException.class, () -> checksumStream.setValue(incorrectChecksum));
    }
}