import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.util.zip.Adler32;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws IOException {
        final Checksum checksum = new Adler32();
        final InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4});
        final ChecksumCalculatingInputStream checksumInput = new ChecksumCalculatingInputStream(checksum, input);

        final int readByte = checksumInput.read();
        assertEquals(1, readByte);

        final int readBytes = checksumInput.read(new byte[3]);
        assertEquals(3, readBytes);

        // Test edge cases and exception handling
        final InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        final ChecksumCalculatingInputStream emptyChecksumInput = new ChecksumCalculatingInputStream(checksum, emptyInput);
        try {
            emptyChecksumInput.read();
            fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }

        final InputStream failingInput = new ThrowingInputStream(new IOException("Test failure"));
        final ChecksumCalculatingInputStream failingChecksumInput = new ChecksumCalculatingInputStream(checksum, failingInput);
        try {
            failingChecksumInput.read();
            fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }

        final InputStream inputWithDifferentValues = new ByteArrayInputStream(new byte[]{1, 2, 3, 5});
        final ChecksumCalculatingInputStream inputWithDifferentValuesChecksumInput = new ChecksumCalculatingInputStream(checksum, inputWithDifferentValues);
        final int readByteWithDifferentValues = inputWithDifferentValuesChecksumInput.read();
        assertEquals(1, readByteWithDifferentValues);
    }

    @Test
    public void testReadBytes() throws IOException {
        final Checksum checksum = new Adler32();
        final InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        final ChecksumCalculatingInputStream checksumInput = new ChecksumCalculatingInputStream(checksum, input);

        final byte[] buffer = new byte[5];
        final int bytesRead = checksumInput.read(buffer, 0, 5);

        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, buffer);
        assertEquals(5, bytesRead);

        // Test edge cases and exception handling
        final byte[] bufferSmallSize = new byte[2];
        final int bytesReadSmallSize = checksumInput.read(bufferSmallSize, 0, 2);
        assertEquals(2, bytesReadSmallSize);
        assertArrayEquals(new byte[]{1, 2}, bufferSmallSize);

        final byte[] bufferBigSize = new byte[10];
        final int bytesReadBigSize = checksumInput.read(bufferBigSize, 0, 10);
        assertEquals(9, bytesReadBigSize);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6, 7, 8}, bufferBigSize);

        final InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        final ChecksumCalculatingInputStream emptyChecksumInput = new ChecksumCalculatingInputStream(checksum, emptyInput);
        try {
            emptyChecksumInput.read(new byte[5], 0, 5);
            fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }

        final InputStream failingInput = new ThrowingInputStream(new IOException("Test failure"));
        final ChecksumCalculatingInputStream failingChecksumInput = new ChecksumCalculatingInputStream(checksum, failingInput);
        try {
            failingChecksumInput.read(new byte[5], 0, 5);
            fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testSkip() throws IOException {
        final Checksum checksum = new Adler32();
        final InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4});
        final ChecksumCalculatingInputStream checksumInput = new ChecksumCalculatingInputStream(checksum, input);

        final long skipped = checksumInput.skip(4);
        assertEquals(4, skipped);

        // Test edge cases and exception handling
        final long skippedLargeNumber = checksumInput.skip(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, skippedLargeNumber);

        final InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        final ChecksumCalculatingInputStream emptyChecksumInput = new ChecksumCalculatingInputStream(checksum, emptyInput);
        try {
            emptyChecksumInput.skip(4);
            fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }

        final InputStream failingInput = new ThrowingInputStream(new IOException("Test failure"));
        final ChecksumCalculatingInputStream failingChecksumInput = new ChecksumCalculatingInputStream(checksum, failingInput);
        try {
            failingChecksumInput.skip(4);
            fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testGetValue() throws IOException {
        final Checksum checksum = new Adler32();
        final InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4});
        final ChecksumCalculatingInputStream checksumInput = new ChecksumCalculatingInputStream(checksum, input);

        final long value = checksumInput.getValue();
        assertEquals(Adler32.adler32(1, 2, 3, 4), value);
    }
}

class ThrowingInputStream extends InputStream {
    private final IOException exception;

    public ThrowingInputStream(IOException exception) {
        this.exception = exception;
    }

    @Override
    public int read() throws IOException {
        throw exception;
    }

    @Override
    public int read(byte[] b) throws IOException {
        throw exception;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        throw exception;
    }

    @Override
    public long skip(long n) throws IOException {
        throw exception;
    }
}