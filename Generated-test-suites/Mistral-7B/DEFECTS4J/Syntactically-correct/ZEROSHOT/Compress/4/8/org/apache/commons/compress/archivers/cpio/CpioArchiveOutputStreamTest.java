package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest {

    private File outputFile;
    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setUp() throws Exception {
        outputFile = new File(TestUtils.getTestFileName("cpio.out"));
        outputStream = new FileOutputStream(outputFile);
        cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws Exception {
        if (cpioOutputStream != null) {
            cpioOutputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }

    /**
     * Tests construction of {@link CpioArchiveOutputStream} with a specified format.
     */
    @Test
    public void testCpioArchiveOutputStreamCreation() throws IOException {
        assertEquals(CpioConstants.FORMAT_NEW, cpioOutputStream.getFormat());
    }

    /**
     * Tests writing an entry with default attributes.
     */
    @Test
    public void testWriteEntryWithDefaultAttributes() throws IOException {
        String entryName = "testEntry";
        ArchiveEntry entry = new CpioArchiveEntry(new File("testFile.txt"));
        entry.setName(entryName);

        cpioOutputStream.putArchiveEntry(entry);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] testFileContent = "Test content".getBytes();
        byteArrayOutputStream.write(testFileContent);
        cpioOutputStream.write(testFileContent, 0, testFileContent.length);
        cpioOutputStream.closeArchiveEntry();

        InputStream inputStream = new ByteArrayInputStream(outputFile.getBytes());
        ArchiveEntry readEntry = cpioOutputStream.createArchiveEntry(null, entryName);
        assertSame(entry, readEntry);
        assertEquals(entryName, cpioOutputStream.getNameIndex(entryName));
        assertEquals(testFileContent.length, cpioOutputStream.getNextSizer());
        assertEquals(testFileContent.length, cpioOutputStream.read(new byte[testFileContent.length]));
        inputStream.close();
        outputFile.delete();
    }

    /**
     * Tests writing an entry with custom attributes.
     */
    @Test
    public void testWriteEntryWithCustomAttributes() throws IOException {
        String entryName = "testEntry";
        ArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setMode(CpioConstants.C_ISDIR | CpioConstants.C_EXEC);
        entry.setSize(1024);
        entry.setTime(System.currentTimeMillis());

        cpioOutputStream.putArchiveEntry(entry);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] testFileContent = "Test content".getBytes();
        byteArrayOutputStream.write(testFileContent);
        cpioOutputStream.write(testFileContent, 0, testFileContent.length);
        cpioOutputStream.closeArchiveEntry();

        InputStream inputStream = new ByteArrayInputStream(outputFile.getBytes());
        ArchiveEntry readEntry = cpioOutputStream.createArchiveEntry(null, entryName);
        assertSame(entry, readEntry);
        assertEquals(entryName, cpioOutputStream.getNameIndex(entryName));
        assertEquals(testFileContent.length, cpioOutputStream.getNextSizer());
        assertEquals(testFileContent.length, cpioOutputStream.read(new byte[testFileContent.length]));
        inputStream.close();
        outputFile.delete();
    }

    /**
     * Tests writing multiple entries.
     */
    @Test
    public void testWriteMultipleEntries() throws IOException {
        String entryName1 = "testEntry1";
        String entryName2 = "testEntry2";
        ArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(entryName1);

        ArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(entryName2);

        cpioOutputStream.putArchiveEntry(entry1);
        ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
        byte[] testFileContent1 = "Test content1".getBytes();
        byteArrayOutputStream1.write(testFileContent1);
        cpioOutputStream.write(testFileContent1, 0, testFileContent1.length);
        cpioOutputStream.closeArchiveEntry();

        cpioOutputStream.putArchiveEntry(entry2);
        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        byte[] testFileContent2 = "Test content2".getBytes();
        byteArrayOutputStream2.write(testFileContent2);
        cpioOutputStream.write(testFileContent2, 0, testFileContent2.length);
        cpioOutputStream.closeArchiveEntry();

        InputStream inputStream = new ByteArrayInputStream(outputFile.getBytes());
        ArchiveEntry readEntry1 = cpioOutputStream.createArchiveEntry(null, entryName1);
        assertSame(entry1, readEntry1);
        assertEquals(entryName1, cpioOutputStream.getNameIndex(entryName1));
        assertEquals(testFileContent1.length, cpioOutputStream.getNextSizer());
        assertEquals(testFileContent1.length, cpioOutputStream.read(new byte[testFileContent1.length]));

        ArchiveEntry readEntry2 = cpioOutputStream.createArchiveEntry(null, entryName2);
        assertSame(entry2, readEntry2);
        assertEquals(entryName2, cpioOutputStream.getNameIndex(entryName2));
        assertEquals(testFileContent2.length, cpioOutputStream.getNextSizer());
        assertEquals(testFileContent2.length, cpioOutputStream.read(new byte[testFileContent2.length]));
        inputStream.close();
        outputFile.delete();
    }

    /**
     * Tests writing an entry with the specified format.
     */
    @Test
    public void testWriteEntryWithSpecifiedFormat() throws IOException {
        String entryName = "testEntry";
        ArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setFormat(CpioConstants.FORMAT_OLD_ASCII);

        cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_OLD_ASCII);
        cpioOutputStream.putArchiveEntry(entry);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] testFileContent = "Test content".getBytes();
        byteArrayOutputStream.write(testFileContent);
        cpioOutputStream.write(testFileContent, 0, testFileContent.length);
        cpioOutputStream.closeArchiveEntry();

        InputStream inputStream = new ByteArrayInputStream(outputFile.getBytes());
        ArchiveEntry readEntry = cpioOutputStream.createArchiveEntry(null, entryName);
        assertSame(entry, readEntry);
        assertEquals(entryName, cpioOutputStream.getNameIndex(entryName));
        assertEquals(testFileContent.length, cpioOutputStream.getNextSizer());
        assertEquals(testFileContent.length, cpioOutputStream.read(new byte[testFileContent.length]));
        inputStream.close();
        outputFile.delete();
    }

    /**
     * Tests verifying the written CPIO archive.
     */
    @Test
    public void testVerifyWrittenCpioArchive() throws IOException {
        String entryName1 = "testEntry1";
        String entryName2 = "testEntry2";
        ArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(entryName1);

        ArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(entryName2);

        cpioOutputStream.putArchiveEntry(entry1);
        ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
        byte[] testFileContent1 = "Test content1".getBytes();
        byteArrayOutputStream1.write(testFileContent1);
        cpioOutputStream.write(testFileContent1, 0, testFileContent1.length);
        cpioOutputStream.closeArchiveEntry();

        cpioOutputStream.putArchiveEntry(entry2);
        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        byte[] testFileContent2 = "Test content2".getBytes();
        byteArrayOutputStream2.write(testFileContent2);
        cpioOutputStream.write(testFileContent2, 0, testFileContent2.length);
        cpioOutputStream.closeArchiveEntry();

        cpioOutputStream.finish();

        File inputFile = new File(outputFile.getAbsolutePath() + ".cpio");
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(new FileInputStream(inputFile));
        ArchiveEntry readEntry1 = archiveInputStream.getNextEntry();
        assertSame(entry1, readEntry1);
        assertEquals(entryName1, readEntry1.getName());
        assertEquals(testFileContent1.length, archiveInputStream.read(new byte[testFileContent1.length]));

        ArchiveEntry readEntry2 = archiveInputStream.getNextEntry();
        assertSame(entry2, readEntry2);
        assertEquals(entryName2, readEntry2.getName());
        assertEquals(testFileContent2.length, archiveInputStream.read(new byte[testFileContent2.length]));

        archiveInputStream.close();
        outputFile.delete();
        inputFile.delete();
    }

    /**
     * Tests writing a CPIO archive with a trailing header.
     */
    @Test
    public void testWriteCpioArchiveWithTrailingHeader() throws IOException {
        String entryName = "testEntry";
        ArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);

        cpioOutputStream.putArchiveEntry(entry);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] testFileContent = "Test content".getBytes();
        byteArrayOutputStream.write(testFileContent);
        cpioOutputStream.write(testFileContent, 0, testFileContent.length);
        cpioOutputStream.closeArchiveEntry();

        cpioOutputStream.finish();
        assertEquals(1, cpioOutputStream.getNextSizer());
        cpioOutputStream.write(new byte[1]);

        cpioOutputStream.putArchiveEntry(new CpioArchiveEntry(CpioConstants.FORMAT_NEW, CpioConstants.C_ISDIR | CpioConstants.C_EXEC, "."));
        cpioOutputStream.write(new byte[0]);

        cpioOutputStream.finish();

        File inputFile = new File(outputFile.getAbsolutePath() + ".cpio");
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(new FileInputStream(inputFile));
        ArchiveEntry readEntry = archiveInputStream.getNextEntry();
        assertSame(entry, readEntry);
        assertEquals(entryName, readEntry.getName());
        assertEquals(testFileContent.length, archiveInputStream.read(new byte[testFileContent.length]));

        archiveInputStream.close();
        outputFile.delete();
        inputFile.delete();
    }

    /**
     * Tests writing a CPIO archive with a custom header format.
     */
    @Test
    public void testWriteCpioArchiveWithCustomHeaderFormat() throws IOException {
        String entryName = "testEntry";
        ArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setFormat(CpioConstants.FORMAT_OLD_BINARY);

        cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_OLD_BINARY);
        cpioOutputStream.putArchiveEntry(entry);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] testFileContent = "Test content".getBytes();
        byteArrayOutputStream.write(testFileContent);
        cpioOutputStream.write(testFileContent, 0, testFileContent.length);
        cpioOutputStream.closeArchiveEntry();

        cpioOutputStream.finish();

        File inputFile = new File(outputFile.getAbsolutePath() + ".cpio");
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(new FileInputStream(inputFile));
        ArchiveEntry readEntry = archiveInputStream.getNextEntry();
        assertSame(entry, readEntry);
        assertEquals(entryName, readEntry.getName());
        assertEquals(testFileContent.length, archiveInputStream.read(new byte[testFileContent.length]));

        archiveInputStream.close();
        outputFile.delete();
        inputFile.delete();
    }
}