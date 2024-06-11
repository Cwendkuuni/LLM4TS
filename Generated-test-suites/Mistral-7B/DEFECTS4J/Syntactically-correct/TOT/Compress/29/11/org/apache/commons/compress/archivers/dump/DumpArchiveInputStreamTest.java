package org.apache.commons.compress.archivers.dump;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.AbstractInputStreamTests;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link DumpArchiveInputStream}.
 */
public class DumpArchiveInputStreamTest extends AbstractInputStreamTests {

    @Test
    public void testEmptyDumpArchive() throws IOException, ArchiveException {
        // create an empty dump archive
        InputStream is = new ByteArrayInputStream(new byte[0]);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, null);

        // read the next entry should return null
        assertNull(inputStream.getNextEntry());

        // read bytes should return -1 at EOF
        assertEquals(-1, inputStream.read(new byte[1024], 0, 1024));

        // close the stream
        inputStream.close();
    }

    @Test
    public void testValidDumpArchive() throws IOException, ArchiveException {
        // create a valid dump archive
        InputStream is = new ByteArrayInputStream(DumpArchiveTestUtil.getDumpArchiveBytes());
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, null);

        // read the first entry
        DumpArchiveEntry entry = inputStream.getNextEntry();
        assertEquals("entry1.txt", entry.getName());
        assertEquals(5, entry.getSize());

        // read bytes from the first entry
        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer, 0, 5);
        assertEquals(5, bytesRead);
        assertEquals("hello", new String(buffer, 0, bytesRead, "US-ASCII"));

        // read the second entry
        entry = inputStream.getNextEntry();
        assertEquals("entry2.txt", entry.getName());
        assertEquals(10, entry.getSize());

        // read bytes from the second entry
        bytesRead = inputStream.read(buffer, 0, 10);
        assertEquals(10, bytesRead);
        assertEquals("world", new String(buffer, 0, bytesRead, "US-ASCII"));

        // close the stream
        inputStream.close();
    }

    @Test
    public void testInvalidDumpArchive() throws IOException, ArchiveException {
        // create an invalid dump archive
        InputStream is = new ByteArrayInputStream(DumpArchiveTestUtil.getInvalidDumpArchiveBytes());
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, null);

        // read the next entry should throw an exception
        assertThrows(ArchiveException.class, inputStream::getNextEntry);

        // read bytes should throw an exception at EOF
        assertThrows(IOException.class, () -> inputStream.read(new byte[1], 0, 1));

        // close the stream
        inputStream.close();
    }

    @Test
    public void testInvalidDumpArchiveWithWrongMagicNumber() throws IOException {
        // create an invalid dump archive with a wrong magic number
        InputStream is = new ByteArrayInputStream(DumpArchiveTestUtil.getInvalidDumpArchiveWithWrongMagicNumberBytes());
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, null);

        // read the next entry should throw an exception
        assertFalse(inputStream.matches(inputStream.readAllBytes(), 0));
        assertThrows(InvalidFormatException.class, inputStream::getNextEntry);

        // close the stream
        inputStream.close();
    }

    @Test
    public void testValidDumpArchiveWithCompressedEntries() throws IOException, ArchiveException {
        // create a valid dump archive with compressed entries
        InputStream is = new ByteArrayInputStream(DumpArchiveTestUtil.getDumpArchiveWithCompressedEntriesBytes());
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, null);

        // read the first entry
        DumpArchiveEntry entry = inputStream.getNextEntry();
        assertEquals("entry1.txt", entry.getName());
        assertEquals(5, entry.getSize());

        // read bytes from the first entry
        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer, 0, 5);
        assertEquals(5, bytesRead);
        assertEquals("hello", new String(buffer, 0, bytesRead, "US-ASCII"));

        // read the second entry
        entry = inputStream.getNextEntry();
        assertEquals("entry2.txt.gz", entry.getName());
        assertEquals(-1, entry.getSize());

        // create a ByteArrayOutputStream to store the compressed data
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // create a ByteArrayInputStream with the compressed data
        InputStream compressedInput = new ByteArrayInputStream(DumpArchiveTestUtil.getCompressedDataBytes());

        // copy the compressed data to the output stream
        byte[] buffer2 = new byte[1024];
        int bytesRead2 = 0;
        while ((bytesRead2 = compressedInput.read(buffer2, 0, buffer2.length)) > 0) {
            outputStream.write(buffer2, 0, bytesRead2);
        }

        // set the compressed input stream as the next entry's input stream
        entry.setInputStream(new ByteArrayInputStream(outputStream.toByteArray()));

        // read bytes from the second entry
        bytesRead = inputStream.read(buffer, 0, 10);
        assertEquals(10, bytesRead);
        assertEquals("world", new String(buffer, 0, bytesRead, "US-ASCII"));

        // close the streams
        inputStream.close();
        outputStream.close();
        compressedInput.close();
    }

    @Test
    public void testValidDumpArchiveWithSparseEntries() throws IOException, ArchiveException {
        // create a valid dump archive with sparse entries
        InputStream is = new ByteArrayInputStream(DumpArchiveTestUtil.getDumpArchiveWithSparseEntriesBytes());
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, null);

        // read the first entry
        DumpArchiveEntry entry = inputStream.getNextEntry();
        assertEquals("entry1.txt", entry.getName());
        assertEquals(5, entry.getSize());

        // read bytes from the first entry
        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer, 0, 5);
        assertEquals(5, bytesRead);
        assertEquals("hello", new String(buffer, 0, bytesRead, "US-ASCII"));

        // read the second entry
        entry = inputStream.getNextEntry();
        assertEquals("entry2.txt", entry.getName());
        assertEquals(10, entry.getSize());

        // read the first 5 bytes from the second entry
        buffer = new byte[5];
        bytesRead = inputStream.read(buffer, 0, 5);
        assertEquals(5, bytesRead);
        assertEquals("world", new String(buffer, 0, bytesRead, "US-ASCII"));

        // read the next 5 bytes from the second entry
        buffer = new byte[5];
        bytesRead = inputStream.read(buffer, 0, 5);
        assertEquals(5, bytesRead);
        assertEquals("file", new String(buffer, 0, bytesRead, "US-ASCII"));

        // read the last byte from the second entry
        buffer = new byte[1];
        bytesRead = inputStream.read(buffer, 0, 1);
        assertEquals(1, bytesRead);
        assertEquals('\n', buffer[0]);

        // close the stream
        inputStream.close();
    }

    @Test
    public void testValidDumpArchiveWithInvalidChecksum() throws IOException, ArchiveException {
        // create a valid dump archive with an invalid checksum
        InputStream is = new ByteArrayInputStream(DumpArchiveTestUtil.getDumpArchiveWithInvalidChecksumBytes());
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, null);

        // read the next entry should throw an exception
        assertThrows(ChecksumMismatchException.class, inputStream::getNextEntry);

        // close the stream
        inputStream.close();
    }

    @Test
    public void testValidDumpArchiveWithMissingEntries() throws IOException, ArchiveException {
        // create a valid dump archive with missing entries
        InputStream is = new ByteArrayInputStream(DumpArchiveTestUtil.getDumpArchiveWithMissingEntriesBytes());
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, null);

        // read the first entry
        DumpArchiveEntry entry = inputStream.getNextEntry();
        assertEquals("entry1.txt", entry.getName());
        assertEquals(5, entry.getSize());

        // read bytes from the first entry
        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer, 0, 5);
        assertEquals(5, bytesRead);
        assertEquals("hello", new String(buffer, 0, bytesRead, "US-ASCII"));

        // read the second entry should return null
        entry = inputStream.getNextEntry();
        assertNull(entry);

        // read bytes from the first entry again
        bytesRead = inputStream.read(buffer, 0, 5);
        assertEquals(5, bytesRead);
        assertEquals("hello", new String(buffer, 0, bytesRead, "US-ASCII"));

        // close the stream
        inputStream.close();
    }

    private static class DumpArchiveTestUtil {
        public static byte[] getDumpArchiveBytes() {
            return getDumpArchiveBytes("test1.dump");
        }

        public static byte[] getDumpArchiveBytes(String fileName) {
            try {
                InputStream is = DumpArchiveInputStreamTest.class.getResourceAsStream("/test-data/" + fileName);
                return is != null ? is.readAllBytes() : new byte[0];
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public static byte[] getDumpArchiveWithCompressedEntriesBytes() {
            return getDumpArchiveBytes("test2.dump");
        }

        public static byte[] getDumpArchiveWithSparseEntriesBytes() {
            return getDumpArchiveBytes("test3.dump");
        }

        public static byte[] getDumpArchiveWithInvalidChecksumBytes() {
            return getDumpArchiveBytes("test4.dump");
        }

        public static byte[] getDumpArchiveWithMissingEntriesBytes() {
            return getDumpArchiveBytes("test5.dump");
        }

        public static byte[] getInvalidDumpArchiveBytes() {
            return getInvalidDumpArchiveBytes("test6.dump");
        }

        public static byte[] getInvalidDumpArchiveWithWrongMagicNumberBytes() {
            return getInvalidDumpArchiveBytes("test7.dump");
        }

        private static byte[] getInvalidDumpArchiveBytes(String fileName) {
            try {
                InputStream is = DumpArchiveInputStreamTest.class.getResourceAsStream("/test-data/" + fileName);
                if (is == null) {
                    throw new RuntimeException();
                }

                byte[] headerBytes = is.readNBytes(DumpArchiveConstants.TP_SIZE);
                byte magicNumber = headerBytes[24];

                if (magicNumber == DumpArchiveConstants.NFS_MAGIC) {
                    is.close();
                    throw new RuntimeException();
                }

                return is.readAllBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public static byte[] getCompressedDataBytes() {
            return getCompressedDataBytes("test.gz");
        }

        public static byte[] getCompressedDataBytes(String fileName) {
            try {
                InputStream is = DumpArchiveInputStreamTest.class.getResourceAsStream("/test-data/" + fileName);
                return is != null ? is.readAllBytes() : new byte[0];
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}