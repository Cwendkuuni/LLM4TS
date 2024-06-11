import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.ChecksumUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.zip.Adler32;

public class ChecksumCalculatingInputStreamTest {

    private InputStream input;
    private ChecksumCalculatingInputStream checksumInput;
    private byte[] testData;
    private long expectedChecksum;

    @Before
    public void setUp() {
        testData = new byte[]{'T', 'e', 'st', 'D', 'a', 't', 'a'};
        input = new ByteArrayInputStream(testData);
        checksumInput = new ChecksumCalculatingInputStream(new Adler32(), input);
        expectedChecksum = new Adler32().value(testData);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        int readByte = checksumInput.read();
        Assert.assertEquals(-1, readByte); // EOF should be reached
        Assert.assertEquals(expectedChecksum, checksumInput.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[testData.length];
        int readBytes = checksumInput.read(buffer);
        Assert.assertEquals(testData.length, readBytes);
        Assert.assertArrayEquals(testData, buffer);
        Assert.assertEquals(expectedChecksum, checksumInput.getValue());
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] buffer = new byte[testData.length + 1];
        int offset = 1;
        int readBytes = checksumInput.read(buffer, offset, testData.length);
        Assert.assertEquals(testData.length, readBytes);
        Assert.assertArrayEquals(Arrays.copyOfRange(testData, 0, testData.length), Arrays.copyOfRange(buffer, offset, offset + testData.length));
        Assert.assertEquals(expectedChecksum, checksumInput.getValue());
    }

    @Test(expected = IOException.class)
    public void testThrowsIOExceptionOnStreamExhausted() throws IOException {
        while (checksumInput.read() >= 0) {
            // Empty
        }
    }

    @Test
    public void testGetChecksum() throws IOException {
        Assert.assertEquals(expectedChecksum, checksumInput.getValue());
    }

    @Test
    public void testReadWithChecksumMismatch() throws IOException {
        InputStream inputWithCorruptedData = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't', 'D', 'a', (byte) 0xCC});
        ChecksumCalculatingInputStream checksumInputWithMismatch = new ChecksumCalculatingInputStream(new Adler32(), inputWithCorruptedData);
        try {
            checksumInputWithMismatch.read();
            Assert.fail("Expected IOException when checksum doesn't match");
        } catch (IOException e) {
            // Expected
        }
    }

    @After
    public void cleanUp() throws IOException {
        input.close();
        checksumInput.close();
    }
}