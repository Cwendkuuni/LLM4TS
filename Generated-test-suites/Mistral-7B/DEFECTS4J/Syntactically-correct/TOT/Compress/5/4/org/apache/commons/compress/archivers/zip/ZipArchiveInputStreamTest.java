import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;

public class ZipArchiveInputStreamTest {

    @Test
    public void testReadValidZipFile() throws IOException {
        try (ZipFile zipFile = new ZipFile("test.zip")) {
            ZipArchiveInputStream testStream = new ZipArchiveInputStream(
                    new FileInputStream(zipFile.getName()));
            ZipArchiveEntry entry = testStream.getNextZipEntry();
            assert entry != null;

            byte[] buffer = new byte[1024];
            int length;

            while ((length = testStream.read(buffer)) > 0) {
                // Do something with the read data
            }

            testStream.close();
        }
    }

    @Test
    public void testReadEmptyZipFile() throws IOException {
        try (ZipFile zipFile = new ZipFile("empty.zip")) {
            ZipArchiveInputStream testStream = new ZipArchiveInputStream(
                    new FileInputStream(zipFile.getName()));

            assert testStream.getNextZipEntry() == null;
            testStream.close();
        }
    }

    @Test(expected = IOException.class)
    public void testInvalidZipFile() throws IOException {
        try (FileInputStream inputStream = new FileInputStream("invalid.zip")) {
            ZipArchiveInputStream testStream = new ZipArchiveInputStream(
                    inputStream);
            testStream.getNextZipEntry();
            testStream.close();
        }
    }

    @Test
    public void testReadCorruptedZipFile() throws IOException {
        Path filePath = Files.createTempFile("corrupted", ".zip");
        Files.write(filePath, new byte[]{0x04, 0x03, 0x4b, 0x50, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00});

        try (ZipArchiveInputStream testStream = new ZipArchiveInputStream(
                new FileInputStream(filePath.toFile()))) {
            testStream.getNextZipEntry();
            testStream.close();
        }

        Files.delete(filePath);
    }

    @Test(expected = IOException.class)
    public void testReadClosedStream() throws IOException {
        try (ZipArchiveInputStream testStream = new ZipArchiveInputStream(
                new FileInputStream("test.zip"))) {
            testStream.close();
            testStream.getNextZipEntry();
        }
    }

    @Test(expected = IOException.class)
    public void testReadInvalidEntry() throws IOException {
        try (FileInputStream inputStream = new FileInputStream("test.zip")) {
            byte[] buffer = new byte[1];
            ZipArchiveInputStream testStream = new ZipArchiveInputStream(
                    inputStream, null, false);
            testStream.read(buffer);
            testStream.getNextZipEntry();
            testStream.close();
        }
    }

    @Test
    public void testReadLargeEntry() throws IOException {
        try (ZipFile zipFile = new ZipFile("large.zip")) {
            ZipArchiveInputStream testStream = new ZipArchiveInputStream(
                    new FileInputStream(zipFile.getName()));
            ZipArchiveEntry entry = testStream.getNextZipEntry();
            assert entry != null;

            byte[] buffer = new byte[entry.getSize()];
            int length;

            while ((length = testStream.read(buffer)) > 0) {
                // Do something with the read data
            }

            testStream.close();
        }
    }

    @Test(expected = EOFException.class)
    public void testReadEndOfStream() throws IOException {
        try (ZipArchiveInputStream testStream = new ZipArchiveInputStream(
                new FileInputStream("test.zip"))) {
            byte[] buffer = new byte[1];
            int length;

            while ((length = testStream.read(buffer)) > 0) {
                // Do something with the read data
            }

            testStream.getNextZipEntry();
            testStream.read(buffer);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadNegativeLength() throws IOException {
        try (ZipArchiveInputStream testStream = new ZipArchiveInputStream(
                new ByteArrayInputStream(new byte[]{-1}))) {
            testStream.read(new byte[1]);
        }
    }

    @Test
    public void testReadManyEntries() throws IOException {
        try (ZipFile zipFile = new ZipFile("test.zip")) {
            ZipArchiveInputStream testStream = new ZipArchiveInputStream(
                    new FileInputStream(zipFile.getName()));

            int count = 0;
            ZipArchiveEntry entry;

            while ((entry = testStream.getNextZipEntry()) != null) {
                count++;
                testStream.closeEntry();
            }

            Assert.assertEquals("Expected more entries in the zip file",
                     zipFile.size(), count);

            testStream.close();
        }
    }

    @Test(expected = ZipException.class)
    public void testReadInvalidEntryWithExtraFields() throws IOException {
        try (ZipFile zipFile = new ZipFile("invalid_extra_fields.zip")) {
            ZipArchiveInputStream testStream = new ZipArchiveInputStream(
                    new FileInputStream(zipFile.getName()));
            testStream.getNextZipEntry();
            testStream.close();
        }
    }

    @Test(expected = ZipException.class)
    public void testReadInvalidEntryWithUnsupportedEncoding() throws IOException {
        try (ZipFile zipFile = new ZipFile("invalid_encoding.zip")) {
            ZipArchiveInputStream testStream = new ZipArchiveInputStream(
                    new FileInputStream(zipFile.getName()), "ISO-8859-1", false);
            testStream.getNextZipEntry();
            testStream.close();
        }
    }
}