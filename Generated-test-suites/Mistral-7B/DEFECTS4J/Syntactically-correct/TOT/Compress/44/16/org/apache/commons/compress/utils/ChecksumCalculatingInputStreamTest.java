import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.ChecksumUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * Test class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

    /**
     * Test for reading bytes from the stream.
     * @throws IOException if an error occurs while testing
     */
    @Test
    public void testReadByte() throws IOException {
        byte[] input = {0x01, 0x02, 0x03};
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(ChecksumUtils.createChecksum(MessageDigest.getInstance("MD5")), new ByteArrayInputStream(input));

        byte b;
        for (int i = 0; i < input.length; i++) {
            b = (byte) inputStream.read();
            Assert.assertEquals(input[i], b);
        }
        Assert.assertEquals(-1, inputStream.read());
    }

    /**
     * Test for reading byte arrays from the stream.
     * @throws IOException if an error occurs while testing
     */
    @Test
    public void testReadByteArray() throws IOException {
        byte[] input = {0x01, 0x02, 0x03};
        byte[] expectedOutput = {0x01, 0x02, 0x03};

        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(ChecksumUtils.createChecksum(MessageDigest.getInstance("MD5")), new ByteArrayInputStream(input));

        byte[] output = new byte[input.length];
        int actualLength = inputStream.read(output, 0, output.length);

        Assert.assertEquals(input.length, actualLength);
        Assert.assertArrayEquals(expectedOutput, output);
    }

    /**
     * Test for reading large byte arrays from the stream.
     * @throws IOException if an error occurs while testing
     */
    @Test
    public void testReadByteArrayLarge() throws IOException {
        byte[] input = new byte[1024 * 1024];
        byte[] expectedOutput = new byte[input.length];

        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(ChecksumUtils.createChecksum(MessageDigest.getInstance("MD5")), new ByteArrayInputStream(input));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int length;
        while ((length = inputStream.read(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        byte[] output = outputStream.toByteArray();

        Assert.assertArrayEquals(input, output);
    }

    /**
     * Test for reading bytes from the stream and checking the calculated checksum.
     * @throws IOException if an error occurs while testing
     */
    @Test
    public void testReadAndCheckChecksum() throws IOException {
        byte[] input = {0x01, 0x02, 0x03};
        byte[] expectedChecksum = new byte[16]; // MD5 checksum is 16 bytes

        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(ChecksumUtils.createChecksum(MessageDigest.getInstance("MD5")), new ByteArrayInputStream(input));
        byte[] output = new byte[input.length];
        int actualLength = inputStream.read(output, 0, output.length);

        Assert.assertArrayEquals(input, output);

        byte[] checksum = new byte[expectedChecksum.length];
        inputStream.read(checksum, 0, expectedChecksum.length);

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] calculatedChecksum = md.digest(input);

        Assert.assertArrayEquals(calculatedChecksum, expectedChecksum);
    }

    /**
     * Test for reading bytes from a divided input stream and checking the calculated checksum.
     * @throws IOException if an error occurs while testing
     */
    @Test
    public void testReadAndCheckChecksumDivided() throws IOException {
        byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};
        byte[] expectedChecksum = new byte[16]; // MD5 checksum is 16 bytes

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(ChecksumUtils.createChecksum(MessageDigest.getInstance("MD5")), new ByteArrayInputStream(input));

        byte[] buffer = new byte[4];
        int length;
        byte[] output = new byte[input.length];

        for (int i = 0; i < input.length; i += buffer.length) {
            if (i + buffer.length > input.length) {
                length = input.length - i;
                System.arraycopy(input, i, buffer, 0, length);

                inputStream.read(output, i, length);
                outputStream.write(output, i, length);

                inputStream = new ChecksumCalculatingInputStream(ChecksumUtils.createChecksum(MessageDigest.getInstance("MD5")), new ByteArrayInputStream(outputStream.toByteArray()));
            } else {
                length = inputStream.read(buffer, 0, buffer.length);
                System.arraycopy(buffer, 0, output, i, length);
                outputStream.write(buffer, 0, length);
            }
        }

        byte[] checksum = new byte[expectedChecksum.length];
        inputStream.read(checksum, 0, expectedChecksum.length);

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] calculatedChecksum = md.digest(input);

        Assert.assertArrayEquals(calculatedChecksum, expectedChecksum);
    }

    /**
     * Test for reading from an empty stream and checking the calculated checksum.
     * @throws IOException if an error occurs while testing
     */
    @Test
    public void testEmptyStream() throws IOException {
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(ChecksumUtils.createChecksum(MessageDigest.getInstance("MD5")), new ByteArrayInputStream(new byte[0]));
        byte[] checksum = new byte[16];
        inputStream.read(checksum, 0, checksum.length);

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] calculatedChecksum = md.digest(new byte[0]);

        Assert.assertArrayEquals(calculatedChecksum, new byte[0]);
    }

    /**
     * Test for reading from a stream with negative length and checking the calculated checksum.
     * @throws IOException if an error occurs while testing
     */
    @Test(expected = IOException.class)
    public void testNegativeLength() throws IOException {
        byte[] input = {0x01, 0x02, 0x03};
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(ChecksumUtils.createChecksum(MessageDigest.getInstance("MD5")), new ByteArrayInputStream(input));

        inputStream.skip(-1);
    }
}