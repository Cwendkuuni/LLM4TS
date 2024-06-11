package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;

/**
 * Tests for {@link TarArchiveOutputStream}.
 */
public class TarArchiveOutputStreamTest {

    /**
     * Test writing a single file to a tar archive.
     */
    @org.junit.Test
    public void testWriteSingleFile() throws IOException {
        final String testFileName = "testFile.txt";
        final String testFileContent = "Test file content.";

        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os)) {

            // Set the record size to a multiple of TarConstants.NAMELEN to simplify testing
            tarOs.setBlockSize(TarConstants.NAMELEN * 2);
            tarOs.setRecordSize(TarConstants.NAMELEN * 2);

            // Write an entry
            TarArchiveEntry entry = new TarArchiveEntry(testFileName);
            entry.setSize(testFileContent.getBytes(StandardCharsets.UTF_8).length);
            tarOs.putArchiveEntry(entry);

            // Write file content
            tarOs.write(testFileContent.getBytes(StandardCharsets.UTF_8));
            tarOs.closeArchiveEntry();
            tarOs.finish();

            // Verify the generated archive
            byte[] archiveBytes = os.toByteArray();
            ArchiveOutputStream archiveOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
            archiveOut.setBlockSize(TarConstants.NAMELEN * 2);
            archiveOut.setRecordSize(TarConstants.NAMELEN * 2);
            archiveOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            archiveOut.writeAll(new ByteArrayInputStream(archiveBytes));
            ArchiveEntry archiveEntry = archiveOut.getNextEntry();
            assertNotNull(archiveEntry);
            assertEquals(TarConstants.STANDARD_MAGIC_NUMBER, archiveEntry.getMagic());
            assertEquals(testFileName, archiveEntry.getName());
            archive.close();

            // Verify the file content
            ArchiveEntry entryFromArchive = tarOs.getInputStream().getNextEntry();
            assertNotNull(entryFromArchive);
            assertEquals(testFileName, entryFromArchive.getName());
            String contentFromArchive = new String(tarOs.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            assertEquals(testFileContent, contentFromArchive);
        }
    }

    /**
     * Test writing an empty file to a tar archive.
     */
    @org.junit.Test
    public void testWriteEmptyFile() throws IOException {
        final String testFileName = "testEmptyFile.txt";

        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os)) {

            // Set the record size to a multiple of TarConstants.NAMELEN to simplify testing
            tarOs.setBlockSize(TarConstants.NAMELEN * 2);
            tarOs.setRecordSize(TarConstants.NAMELEN * 2);

            // Write an entry
            TarArchiveEntry entry = new TarArchiveEntry(testFileName);
            tarOs.putArchiveEntry(entry);
            tarOs.closeArchiveEntry();
            tarOs.finish();

            // Verify the generated archive
            byte[] archiveBytes = os.toByteArray();
            ArchiveOutputStream archiveOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
            archiveOut.setBlockSize(TarConstants.NAMELEN * 2);
            archiveOut.setRecordSize(TarConstants.NAMELEN * 2);
            archiveOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            archiveOut.writeAll(new ByteArrayInputStream(archiveBytes));
            ArchiveEntry archiveEntry = archiveOut.getNextEntry();
            assertNotNull(archiveEntry);
            assertEquals(TarConstants.STANDARD_MAGIC_NUMBER, archiveEntry.getMagic());
            assertEquals(testFileName, archiveEntry.getName());
            archive.close();

            // Verify the empty file content
            ArchiveEntry entryFromArchive = tarOs.getInputStream().getNextEntry();
            assertNotNull(entryFromArchive);
            assertEquals(testFileName, entryFromArchive.getName());
            byte[] contentFromArchive = tarOs.getInputStream().readAllBytes();
            assertFalse(contentFromArchive.length > 0);
        }
    }

    /**
     * Test writing an entry with a long file name.
     */
    @org.junit.Test
    public void testWriteLongFileName() throws IOException {
        final String testFileName = "testFileNameThatIsLongerThanNameLength.txt";

        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os)) {

            // Set the record size to a multiple of TarConstants.NAMELEN to simplify testing
            tarOs.setBlockSize(TarConstants.NAMELEN * 2);
            tarOs.setRecordSize(TarConstants.NAMELEN * 2);

            // Write an entry
            TarArchiveEntry entry = new TarArchiveEntry(testFileName);
            tarOs.putArchiveEntry(entry);
            tarOs.write("Test file content.".getBytes(StandardCharsets.UTF_8));
            tarOs.closeArchiveEntry();
            tarOs.finish();

            // Verify the generated archive
            byte[] archiveBytes = os.toByteArray();
            ArchiveOutputStream archiveOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
            archiveOut.setBlockSize(TarConstants.NAMELEN * 2);
            archiveOut.setRecordSize(TarConstants.NAMELEN * 2);
            archiveOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            archiveOut.writeAll(new ByteArrayInputStream(archiveBytes));
            ArchiveEntry archiveEntry = archiveOut.getNextEntry();
            assertNotNull(archiveEntry);
            assertEquals(TarConstants.STANDARD_MAGIC_NUMBER, archiveEntry.getMagic());
            assertEquals(testFileName, archiveEntry.getName());
            archive.close();

            // Verify the file content
            ArchiveEntry entryFromArchive = tarOs.getInputStream().getNextEntry();
            assertNotNull(entryFromArchive);
            assertEquals(testFileName, entryFromArchive.getName());
            String contentFromArchive = new String(tarOs.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            assertEquals("Test file content.", contentFromArchive);
        }
    }

    /**
     * Test writing an entry with an invalid long file name.
     */
    @org.junit.Test
    public void testWriteInvalidLongFileName() throws IOException {
        final String testFileName = "testFileNameThatIsLongerThanNameLength.";

        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os)) {

            // Set the record size to a multiple of TarConstants.NAMELEN to simplify testing
            tarOs.setBlockSize(TarConstants.NAMELEN * 2);
            tarOs.setRecordSize(TarConstants.NAMELEN * 2);

            // Write an entry
            TarArchiveEntry entry = new TarArchiveEntry(testFileName);
            tarOs.putArchiveEntry(entry);
            tarOs.write("Test file content.".getBytes(StandardCharsets.UTF_8));
            tarOs.closeArchiveEntry();

            // Assert an exception is thrown
            assertThrows(RuntimeException.class, () -> tarOs.finish());
        }
    }
}