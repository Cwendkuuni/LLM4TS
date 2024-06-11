package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

/**
 * Test {@link ZipArchiveInputStream}.
 */
public class ZipArchiveInputStreamTest {

    /**
     * Test reading a valid ZIP file with no compression.
     */
    @Test
    public void testReadNonCompressedEntry() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/non_compressed_entry.zip")) {
            ZipArchiveInputStream zipIn = new ZipArchiveInputStream(is, ZipEncodingHelper.UTF8, true);
            ArchiveEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {
                // read data
                byte[] data = new byte[1024];
                int length = zipIn.read(data, 0, data.length);
                Assert.assertNotEquals(-1, length);

                // check CRC
                byte[] expectedCrcBytes = new byte[4];
                zipIn.readFully(expectedCrcBytes);
                CRC32 crc = new CRC32();
                crc.update(data, 0, length);
                byte[] calculatedCrcBytes = new byte[4];
                crc.getValue(calculatedCrcBytes);
                Assert.assertArrayEquals(expectedCrcBytes, calculatedCrcBytes);
            }
            zipIn.close();
        }
    }

    /**
     * Test reading a valid ZIP file with DEFLATE compression.
     */
    @Test
    public void testReadDeflateCompressedEntry() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/deflate_compressed_entry.zip")) {
            ZipArchiveInputStream zipIn = new ZipArchiveInputStream(is, ZipEncodingHelper.UTF8, true);
            ArchiveEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {
                // read data
                byte[] data = new byte[1024];
                int length = zipIn.read(data, 0, data.length);
                Assert.assertNotEquals(-1, length);

                // check CRC
                byte[] expectedCrcBytes = new byte[4];
                zipIn.readFully(expectedCrcBytes);
                CRC32 crc = new CRC32();
                crc.update(data, 0, length);
                byte[] calculatedCrcBytes = new byte[4];
                crc.getValue(calculatedCrcBytes);
                Assert.assertArrayEquals(expectedCrcBytes, calculatedCrcBytes);
            }
            zipIn.close();
        }
    }

    /**
     * Test reading an invalid ZIP file (missing signature).
     */
    @Test(expected = IOException.class)
    public void testReadInvalidFileNoSignature() throws IOException {
        try (InputStream is = new ByteArrayInputStream(new byte[]{})) {
            ZipArchiveInputStream zipIn = new ZipArchiveInputStream(is, ZipEncodingHelper.UTF8, true);
            zipIn.getNextEntry();
            zipIn.close();
        }
    }

    /**
     * Test reading an invalid ZIP file (invalid signature).
     */
    @Test(expected = IOException.class)
    public void testReadInvalidFileInvalidSignature() throws IOException {
        try (InputStream is = new ByteArrayInputStream(new byte[]{0x04, 0x03, (byte) 0x4B, 0x50, 0x03, 0x04, 0x03, 0x4B, 0x50})) {
            ZipArchiveInputStream zipIn = new ZipArchiveInputStream(is, ZipEncodingHelper.UTF8, true);
            zipIn.getNextEntry();
            zipIn.close();
        }
    }

    /**
     * Test reading a ZIP file with a malformed local file header.
     */
    @Test(expected = IOException.class)
    public void testReadMalformedFile() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(10);) {
            byte[] lfhSignature = ZipLong.LFH_SIG.getBytes();
            bos.write(lfhSignature);
            bos.write(0x08, 0, 1);
            bos.write(0x01, 0, 1);
            bos.write(0x01, 0, 1);
            bos.write(0x01, 0, 1);
            bos.write(0x01, 0, 1);
            bos.write(0x01, 0, 1);
            bos.write(0x01, 0, 1);
            bos.write(0x01, 0, 1);
            bos.write(0x01, 0, 1);
            bos.write(0x01, 0, 1);
            bos.write(0x01, 0, 1);
            bos.write(0x01, 0, 1);
            bos.write(0x01, 0, 1);
            bos.write(0x01, 0, 1);
            byte[] data = bos.toByteArray();
            try (InputStream is = new ByteArrayInputStream(data)) {
                ZipArchiveInputStream zipIn = new ZipArchiveInputStream(is, ZipEncodingHelper.UTF8, true);
                zipIn.getNextEntry();
                zipIn.close();
            }
        }
    }

    /**
     * Test closing the input stream.
     */
    @Test
    public void testClose() throws IOException {
        try (InputStream is = new ByteArrayInputStream(new byte[]{0x04, 0x03, 0x4B, 0x50})) {
            ZipArchiveInputStream zipIn = new ZipArchiveInputStream(is, ZipEncodingHelper.UTF8, true);
            zipIn.getNextEntry();
            zipIn.close();
            Assert.assertFalse(zipIn.isOpen());
        }
    }

    /**
     * Test reading null input stream.
     */
    @Test(expected = NullPointerException.class)
    public void testReadNullInputStream() {
        new ZipArchiveInputStream(null, ZipEncodingHelper.UTF8, true);
    }

    /**
     * Test reading empty input stream.
     */
    @Test
    public void testReadEmptyInputStream() throws IOException {
        try (InputStream is = new ByteArrayInputStream(new byte[]{})) {
            ZipArchiveInputStream zipIn = new ZipArchiveInputStream(is, ZipEncodingHelper.UTF8, true);
            Assert.assertNull(zipIn.getNextEntry());
            zipIn.close();
        }
    }

    /**
     * Test reading invalid ZIP file (missing central directory).
     */
    @Test(expected = IOException.class)
    public void testReadMissingCentralDirectory() throws IOException {
        try (InputStream is = new ByteArrayInputStream(new byte[]{0x04, 0x03, 0x4B, 0x50, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07})) {
            ZipArchiveInputStream zipIn = new ZipArchiveInputStream(is, ZipEncodingHelper.UTF8, true);
            zipIn.getNextEntry();
            zipIn.close();
        }
    }
}