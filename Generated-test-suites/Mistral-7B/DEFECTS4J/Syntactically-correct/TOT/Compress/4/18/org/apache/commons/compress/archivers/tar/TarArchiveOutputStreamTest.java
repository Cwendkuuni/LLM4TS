package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;

/**
 * Tests for {@link TarArchiveOutputStream}.
 */
public class TarArchiveOutputStreamTest {

    /**
     * Test writing a single file entry with a short name.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testPutArchiveEntryShortName() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(out);
        File inputFile = new File("testFile.txt");
        String name = "testFile.txt";
        ArchiveEntry entry = new TarArchiveEntry(inputFile, name);

        archiveOutputStream.putArchiveEntry(entry);
        byte[] bytesToWrite = "Hello World".getBytes(StandardCharsets.UTF_8);
        archiveOutputStream.write(bytesToWrite, 0, bytesToWrite.length);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();

        byte[] expectedOutput = createExpectedOutput(name, bytesToWrite);
        assertArrayEquals(expectedOutput, out.toByteArray());
    }

    /**
     * Test writing a single file entry with a long name.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testPutArchiveEntryLongName() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(out);
        File inputFile = new File("testLongFileName.txt");
        String name = "testLongFileName.txt";
        ArchiveEntry entry = new TarArchiveEntry(inputFile, name);

        archiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        archiveOutputStream.putArchiveEntry(entry);
        byte[] bytesToWrite = "Hello World".getBytes(StandardCharsets.UTF_8);
        archiveOutputStream.write(bytesToWrite, 0, bytesToWrite.length);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();

        byte[] expectedOutput = createExpectedOutputWithLongName(name, bytesToWrite);
        assertArrayEquals(expectedOutput, out.toByteArray());
    }

    /**
     * Test writing multiple file entries.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testPutMultiArchiveEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(out);

        File inputFile1 = new File("testFile1.txt");
        String name1 = "testFile1.txt";
        ArchiveEntry entry1 = new TarArchiveEntry(inputFile1, name1);
        archiveOutputStream.putArchiveEntry(entry1);
        byte[] bytesToWrite1 = "Test file 1 content".getBytes(StandardCharsets.UTF_8);
        archiveOutputStream.write(bytesToWrite1, 0, bytesToWrite1.length);
        archiveOutputStream.closeArchiveEntry();

        File inputFile2 = new File("testFile2.txt");
        String name2 = "testFile2.txt";
        ArchiveEntry entry2 = new TarArchiveEntry(inputFile2, name2);
        archiveOutputStream.putArchiveEntry(entry2);
        byte[] bytesToWrite2 = "Test file 2 content".getBytes(StandardCharsets.UTF_8);
        archiveOutputStream.write(bytesToWrite2, 0, bytesToWrite2.length);
        archiveOutputStream.closeArchiveEntry();

        archiveOutputStream.finish();

        byte[] expectedOutput = createExpectedOutputForMultiArchiveEntry(name1,
                bytesToWrite1, name2, bytesToWrite2);
        assertArrayEquals(expectedOutput, out.toByteArray());
    }

    /**
     * Test putting an archive entry without closing it before putting a new one.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testPutArchiveEntryWithoutClosingPrevious() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(out);

        ArchiveEntry entry1 = new TarArchiveEntry(new File("testFile1.txt"), "testFile1.txt");
        archiveOutputStream.putArchiveEntry(entry1);

        ArchiveEntry entry2 = new TarArchiveEntry(new File("testFile2.txt"), "testFile2.txt");
        assertThrows(IllegalStateException.class, () -> archiveOutputStream.putArchiveEntry(entry2));
        archiveOutputStream.close();
    }

    /**
     * Test writing a file with a long name when longFileMode is set to LONGFILE_TRUNCATE.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testPutArchiveEntryLongNameWithTruncate() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(out);
        File inputFile = new File("testLongFileName.txt");
        String name = "testLongFileName.txt";
        ArchiveEntry entry = new TarArchiveEntry(inputFile, name);

        archiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        archiveOutputStream.putArchiveEntry(entry);
        byte[] bytesToWrite = "Hello World".getBytes(StandardCharsets.UTF_8);
        archiveOutputStream.write(bytesToWrite, 0, bytesToWrite.length);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();

        byte[] expectedOutput = new byte[TarConstants.HEADER_LENGTH + bytesToWrite.length];
        Arrays.fill(expectedOutput, (byte) 0);
        System.arraycopy(bytesToWrite, 0, expectedOutput, TarConstants.HEADER_LENGTH, bytesToWrite.length);
        assertArrayEquals(expectedOutput, out.toByteArray());
    }

    /**
     * Test writing a file with a long name when longFileMode is set to LONGFILE_ERROR.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testPutArchiveEntryLongNameWithError() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(out);
        File inputFile = new File("testLongFileName.txt");
        String name = "testLongFileName.txt";
        ArchiveEntry entry = new TarArchiveEntry(inputFile, name);

        archiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertThrows(RuntimeException.class, () -> archiveOutputStream.putArchiveEntry(entry));
        archiveOutputStream.close();
    }

    /**
     * Create an expected output for a single file entry.
     * @param name the name of the file
     * @param bytesToWrite the content of the file
     * @return the expected output as a byte[]
     */
    private byte[] createExpectedOutput(String name, byte[] bytesToWrite) {
        byte[] header = TarEntryHeaderRecord.create(TarConstants.NORMAL_FILE, name.getBytes(StandardCharsets.UTF_8),
                (long) bytesToWrite.length).getBytes();
        byte[] record = new byte[TarConstants.RECORD_LENGTH];
        System.arraycopy(header, 0, record, 0, TarConstants.HEADER_LENGTH);
        System.arraycopy(bytesToWrite, 0, record, TarConstants.HEADER_LENGTH, bytesToWrite.length);

        byte[] expectedOutput = new byte[TarConstants.HEADER_LENGTH + bytesToWrite.length];
        System.arraycopy(header, 0, expectedOutput, 0, TarConstants.HEADER_LENGTH);
        System.arraycopy(bytesToWrite, 0, expectedOutput, TarConstants.HEADER_LENGTH, bytesToWrite.length);

        return expectedOutput;
    }

    /**
     * Create an expected output for a single file entry with a long name.
     * @param name the name of the file
     * @param bytesToWrite the content of the file
     * @return the expected output as a byte[]
     */
    private byte[] createExpectedOutputWithLongName(String name, byte[] bytesToWrite) {
        byte[] header = TarEntryHeaderRecord.create(TarConstants.NORMAL_FILE, name.getBytes(StandardCharsets.UTF_8),
                (long) bytesToWrite.length, TarConstants.LF_GNUTYPE_LONGNAME).getBytes();
        byte[] record = new byte[TarConstants.RECORD_LENGTH + name.length() + 1];
        System.arraycopy(header, 0, record, 0, TarConstants.HEADER_LENGTH);
        System.arraycopy(bytesToWrite, 0, record, TarConstants.HEADER_LENGTH, bytesToWrite.length);
        int nameLength = name.length();
        System.arraycopy(name.getBytes(StandardCharsets.UTF_8), 0, record, TarConstants.HEADER_LENGTH + bytesToWrite.length, nameLength);
        record[TarConstants.HEADER_LENGTH + bytesToWrite.length + nameLength] = 0;

        byte[] expectedOutput = new byte[TarConstants.HEADER_LENGTH + bytesToWrite.length + name.length() + 1];
        System.arraycopy(header, 0, expectedOutput, 0, TarConstants.HEADER_LENGTH);
        System.arraycopy(bytesToWrite, 0, expectedOutput, TarConstants.HEADER_LENGTH, bytesToWrite.length);
        System.arraycopy(name.getBytes(StandardCharsets.UTF_8), 0, expectedOutput, TarConstants.HEADER_LENGTH + bytesToWrite.length, nameLength);
        expectedOutput[TarConstants.HEADER_LENGTH + bytesToWrite.length + nameLength] = 0;

        return expectedOutput;
    }

    /**
     * Create an expected output for multiple file entries.
     * @param name1 the name of the first file
     * @param bytesToWrite1 the content of the first file
     * @param name2 the name of the second file
     * @param bytesToWrite2 the content of the second file
     * @return the expected output as a byte[]
     */
    private byte[] createExpectedOutputForMultiArchiveEntry(String name1, byte[] bytesToWrite1, String name2, byte[] bytesToWrite2) {
        byte[] header1 = TarEntryHeaderRecord.create(TarConstants.NORMAL_FILE, name1.getBytes(StandardCharsets.UTF_8),
                (long) bytesToWrite1.length).getBytes();
        byte[] record1 = new byte[TarConstants.RECORD_LENGTH];
        System.arraycopy(header1, 0, record1, 0, TarConstants.HEADER_LENGTH);
        System.arraycopy(bytesToWrite1, 0, record1, TarConstants.HEADER_LENGTH, bytesToWrite1.length);

        byte[] header2 = TarEntryHeaderRecord.create(TarConstants.NORMAL_FILE, name2.getBytes(StandardCharsets.UTF_8),
                (long) bytesToWrite2.length).getBytes();
        byte[] record2 = new byte[TarConstants.RECORD_LENGTH];
        System.arraycopy(header2, 0, record2, 0, TarConstants.HEADER_LENGTH);
        System.arraycopy(bytesToWrite2, 0, record2, TarConstants.HEADER_LENGTH, bytesToWrite2.length);

        byte[] expectedOutput = new byte[TarConstants.HEADER_LENGTH + bytesToWrite1.length + bytesToWrite2.length];
        System.arraycopy(header1, 0, expectedOutput, 0, TarConstants.HEADER_LENGTH);
        System.arraycopy(bytesToWrite1, 0, expectedOutput, TarConstants.HEADER_LENGTH, bytesToWrite1.length);
        System.arraycopy(header2, 0, expectedOutput, TarConstants.HEADER_LENGTH + bytesToWrite1.length, TarConstants.HEADER_LENGTH);
        System.arraycopy(bytesToWrite2, 0, expectedOutput, TarConstants.HEADER_LENGTH + bytesToWrite1.length + bytesToWrite2.length, bytesToWrite2.length);

        return expectedOutput;
    }
}