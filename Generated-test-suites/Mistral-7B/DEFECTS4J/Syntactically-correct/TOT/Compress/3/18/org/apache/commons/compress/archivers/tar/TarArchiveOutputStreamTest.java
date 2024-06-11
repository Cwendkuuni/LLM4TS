package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;

public class TarArchiveOutputStreamTest {

    /**
     * Test constructor with default settings.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testDefaultConstructor() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        tarOut.close();
    }

    /**
     * Test constructor with specific settings.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testSpecificConstructor() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os, 1024, 512);
        tarOut.close();
    }

    /**
     * Test setLongFileMode method.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testSetLongFileMode() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOut.close();
    }

    /**
     * Test finish method.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testFinish() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        tarOut.finish();
        tarOut.close();
    }

    /**
     * Test close method.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testClose() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        tarOut.close();
    }

    /**
     * Test getRecordSize method.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testGetRecordSize() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        int recordSize = tarOut.getRecordSize();
        tarOut.close();
        assertEquals(512, recordSize);
    }

    /**
     * Test putArchiveEntry method with valid parameters.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testPutArchiveEntryValid() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);

        File inputFile = new File("testInputFile.txt");
        ArchiveEntry archiveEntry = new TarArchiveEntry("testEntry.txt");
        tarOut.putArchiveEntry(archiveEntry);
        tarOut.write(new byte[]{'T', 'e', 's', 't', ' ', 'I', 'n', 'p', 'u', 't',
                ' ', 'F', 'i', 'l', 'e', '.', 't', 'x', 't'}, 0, 13);
        tarOut.closeArchiveEntry();
        tarOut.finish();
        tarOut.close();

        assertEquals(13, inputFile.length());
    }

    /**
     * Test putArchiveEntry method with incorrect archive entry.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testPutArchiveEntryIncorrectArchiveEntry() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);

        String incorrectArchiveEntryName = "testEntry.txt12345";
        ArchiveEntry archiveEntry = new TarArchiveEntry(incorrectArchiveEntryName);

        assertThrows(ClassCastException.class, () -> {
            tarOut.putArchiveEntry(archiveEntry);
        });

        tarOut.close();
    }

    /**
     * Test putArchiveEntry method with long file name.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testPutArchiveEntryLongFileName() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        String longFileName = "longFileNameTestLongFileNameTestLongFileNameTestLongFileNameTestLongFileNameTest";
        File inputFile = new File(longFileName);
        ArchiveEntry archiveEntry = new TarArchiveEntry(inputFile, longFileName);
        tarOut.putArchiveEntry(archiveEntry);
        tarOut.write(longFileName.getBytes());
        tarOut.write(0);
        tarOut.closeArchiveEntry();
        tarOut.finish();
        tarOut.close();

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream archiveInputStream = new TarArchiveInputStream(testInputStream);
        TarArchiveEntry longFileNameTest = (TarArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals(longFileName, longFileNameTest.getName());
    }

    /**
     * Test closeArchiveEntry method.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);

        File inputFile = new File("testInputFile.txt");
        ArchiveEntry archiveEntry = new TarArchiveEntry("testEntry.txt");
        tarOut.putArchiveEntry(archiveEntry);
        tarOut.write(new byte[]{'T', 'e', 's', 't', ' ', 'I', 'n', 'p', 'u', 't',
                ' ', 'F', 'i', 'l', 'e', '.', 't', 'x', 't'}, 0, 13);
        tarOut.closeArchiveEntry();

        byte[] actualBytes = tarOut.toByteArray();
        byte[] expectedBytes = new byte[]{'T', 'o', 'o', 'l', 's', ' ',
                '1', '0', '0', ' ', '0', ' ', 'N', 'u', 'l', 'l', ' ',
                '1', '0', '0', ' ', '0', ' ', '0', '5', '1', '2', ' ',
                '0', '7', '5', ' ', 'T', 'e', 's', 't', ' ', 'I', 'n', 'p',
                'u', 't', '.', 't', 'x', 't', '\n', '0', '\n', '0', '0'};

        assertArrayEquals(expectedBytes, actualBytes);

        tarOut.finish();
        tarOut.close();
    }
}