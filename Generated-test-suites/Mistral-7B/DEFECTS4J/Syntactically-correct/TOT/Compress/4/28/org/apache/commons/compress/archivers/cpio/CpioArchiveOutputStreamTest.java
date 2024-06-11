package org.apache.commons.compress.archivers.cpio;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.utils.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest {

    private static final byte[] TEST_DATA = "test data".getBytes();

    private OutputStream outputStream;
    private ArchiveOutputStream archiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream(1024);
        archiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws Exception {
        archiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithNullEntry() {
        try {
            archiveOutputStream.putArchiveEntry(null);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testPutArchiveEntryWithInvalidFormat() {
        try {
            archiveOutputStream = new CpioArchiveOutputStream(outputStream, (short) 0xFF);
            ArchiveEntry entry = new CpioArchiveEntry();
            archiveOutputStream.putArchiveEntry(entry);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        } finally {
            archiveOutputStream.close();
        }
    }

    @Test
    public void testPutArchiveEntryWithExistingName() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("test.txt");
        archiveOutputStream.putArchiveEntry(entry1);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("test.txt");
        try {
            archiveOutputStream.putArchiveEntry(entry2);
            fail("Should have thrown an IOException");
        } catch (IOException e) {
            // expected
        }

        archiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithValidData() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(TEST_DATA.length);
        archiveOutputStream.putArchiveEntry(entry);
        archiveOutputStream.write(TEST_DATA, 0, TEST_DATA.length);
        archiveOutputStream.closeArchiveEntry();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        CpioArchiveEntry entryFromStream = (CpioArchiveEntry) new CpioArchiveInputStream(inputStream, null).getNextEntry();
        assertSame(entry, entryFromStream);
    }

    @Test
    public void testFinishWithoutClosingEntries() throws IOException {
        archiveOutputStream.finish();
        assertFalse(archiveOutputStream.hasNextEntry());
    }

    @Test
    public void testCloseAfterFinish() throws Exception {
        archiveOutputStream.finish();
        archiveOutputStream.close();
        assertFalse(archiveOutputStream.hasNextEntry());
    }

    @Test
    public void testWriteWithNullByteArray() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.bin");
        entry.setSize(10);
        archiveOutputStream.putArchiveEntry(entry);
        archiveOutputStream.write(null, 0, 0);
        archiveOutputStream.closeArchiveEntry();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        CpioArchiveEntry entryFromStream = (CpioArchiveEntry) new CpioArchiveInputStream(inputStream, null).getNextEntry();
        assertNull(entryFromStream);
    }

    @Test
    public void testWriteWithNegativeOffset() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.bin");
        entry.setSize(10);
        archiveOutputStream.putArchiveEntry(entry);
        try {
            archiveOutputStream.write(TEST_DATA, -1, TEST_DATA.length);
            fail("Should have thrown an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        } finally {
            archiveOutputStream.closeArchiveEntry();
        }

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        CpioArchiveEntry entryFromStream = (CpioArchiveEntry) new CpioArchiveInputStream(inputStream, null).getNextEntry();
        assertNull(entryFromStream);
    }

    @Test
    public void testWriteWithNegativeLength() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.bin");
        entry.setSize(10);
        archiveOutputStream.putArchiveEntry(entry);
        try {
            archiveOutputStream.write(TEST_DATA, TEST_DATA.length, -1);
            fail("Should have thrown an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        } finally {
            archiveOutputStream.closeArchiveEntry();
        }

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        CpioArchiveEntry entryFromStream = (CpioArchiveEntry) new CpioArchiveInputStream(inputStream, null).getNextEntry();
        assertNull(entryFromStream);
    }

    @Test
    public void testWriteWithValidData() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.bin");
        entry.setSize(10);
        archiveOutputStream.putArchiveEntry(entry);
        byte[] testData = "test data".getBytes();
        archiveOutputStream.write(testData, 0, testData.length);
        archiveOutputStream.closeArchiveEntry();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        CpioArchiveEntry entryFromStream = (CpioArchiveEntry) new CpioArchiveInputStream(inputStream, null).getNextEntry();
        assertEquals(entry, entryFromStream);
        byte[] dataFromStream = new byte[testData.length];
        inputStream.read(dataFromStream);
        assertTrue(CpioUtil.areEqual(testData, dataFromStream));
    }

    @Test
    public void testWriteWithValidDataAndCrc() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.bin");
        entry.setSize(10);
        entry.setFormat(CpioConstants.FORMAT_NEW_CRC);
        archiveOutputStream.putArchiveEntry(entry);
        byte[] testData = "test data".getBytes();
        archiveOutputStream.write(testData, 0, testData.length);
        archiveOutputStream.closeArchiveEntry();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        CpioArchiveEntry entryFromStream = (CpioArchiveEntry) new CpioArchiveInputStream(inputStream, null).getNextEntry();
        assertEquals(entry, entryFromStream);
        byte[] dataFromStream = new byte[testData.length];
        inputStream.read(dataFromStream);
        assertTrue(CpioUtil.areEqual(testData, dataFromStream));

        byte testCrc[] = CpioUtil.crc32(testData);
        assertEquals(CpioUtil.bytes2int(testCrc), entryFromStream.getChksum());
    }

    @Test
    public void testCreateArchiveEntryWithNullFile() {
        try {
            archiveOutputStream.createArchiveEntry(null, "test.txt");
            fail("Should have thrown an NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveEntryWithEmptyFileName() {
        try {
            archiveOutputStream.createArchiveEntry(new File(""), "");
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveEntryWithValidName() throws IOException {
        File testFile = new File("test.tmp");
        CpioArchiveEntry entry = archiveOutputStream.createArchiveEntry(testFile, "test.txt");
        assertTrue(entry.getName().equals("test.txt"));
        assertEquals(testFile.length(), entry.getSize());
        assertEquals(CpioConstants.FORMAT_NEW, entry.getFormat());
        testFile.deleteOnExit();
    }

    @Test
    public void testCreateArchiveEntryWithExistingFile() throws IOException {
        File testFile = new File("test.tmp");
        testFile.createNewFile();
        CpioArchiveEntry entry = archiveOutputStream.createArchiveEntry(testFile, "test.txt");
        assertEquals(testFile.length(), entry.getSize());
        assertEquals(CpioConstants.FORMAT_NEW, entry.getFormat());
        testFile.deleteOnExit();

        try {
            archiveOutputStream.createArchiveEntry(testFile, "test.txt");
            fail("Should have thrown an IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveEntryWithEmptyFile() throws IOException {
        File testFile = new File("test.tmp");
        testFile.createNewFile();
        CpioArchiveEntry entry = archiveOutputStream.createArchiveEntry(testFile, "test.txt");
        assertEquals(0, entry.getSize());
        assertEquals(CpioConstants.FORMAT_NEW, entry.getFormat());
        testFile.deleteOnExit();

        try {
            archiveOutputStream.createArchiveEntry(testFile, "test.txt");
            fail("Should have thrown an IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testWriteAndCloseEntryWithValidData() throws IOException {
        File testFile = new File("test.tmp");
        testFile.createNewFile();
        CpioArchiveEntry entry = archiveOutputStream.createArchiveEntry(testFile, "test.bin");
        entry.setSize(TEST_DATA.length);
        archiveOutputStream.putArchiveEntry(entry);
        archiveOutputStream.write(TEST_DATA, 0, TEST_DATA.length);
        archiveOutputStream.closeArchiveEntry();
        testFile.deleteOnExit();

        InputStream inputStream = new FileInputStream(testFile);
        CpioArchiveEntry entryFromFile = (CpioArchiveEntry) new CpioArchiveInputStream(inputStream, null).getNextEntry();
        assertEquals(entry, entryFromFile);
        byte[] dataFromFile = new byte[TEST_DATA.length];
        inputStream.read(dataFromFile);
        assertTrue(CpioUtil.areEqual(TEST_DATA, dataFromFile));
        inputStream.close();
        testFile.delete();
    }
}