package org.apache.commons.compress.archivers.cpio;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.apache.commons.compress.utils.TestFiles;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link CpioArchiveOutputStream} class.
 */
public class CpioArchiveOutputStreamTest {

    private File outputFile;
    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioStream;

    @Before
    public void setUp() throws Exception {
        outputFile = new File(TestFiles.TEST_FILES_TEMP_FOLDER, "output.cpio");
        outputStream = new FileOutputStream(outputFile);
        cpioStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws Exception {
        if (cpioStream != null) {
            cpioStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        ArchiveEntry entry = new CpioArchiveEntry("testFile.txt");
        cpioStream.putArchiveEntry(entry);
        assertEquals("testFile.txt", cpioStream.getNextEntry().getName());
        cpioStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullEntry() throws Exception {
        cpioStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateName() throws Exception {
        ArchiveEntry entry = new CpioArchiveEntry("testFile.txt");
        cpioStream.putArchiveEntry(entry);
        cpioStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithWrongFormat() throws Exception {
        ArchiveEntry entry = new CpioArchiveEntry("testFile.txt");
        cpioStream.putArchiveEntry(entry);
        cpioStream.putArchiveEntry(entry);
        cpioStream.setFormat(CpioConstants.FORMAT_OLD_BINARY);
        cpioStream.putArchiveEntry(entry); // should throw an exception
        cpioStream.close();
    }

    @Test
    public void testWrite() throws Exception {
        String testString = "Test String.";
        ArchiveEntry entry = new CpioArchiveEntry("testFile.txt");
        cpioStream.putArchiveEntry(entry);
        cpioStream.write(testString.getBytes());
        assertEquals(testString.getBytes().length, cpioStream.getNextEntry().getSize());
        cpioStream.closeArchiveEntry();
    }

    @Test
    public void testFinish() throws Exception {
        ArchiveEntry entry = new CpioArchiveEntry("testFile.txt");
        cpioStream.putArchiveEntry(entry);
        cpioStream.write("Test String.".getBytes());
        cpioStream.finish();
        ArchiveEntry nextEntry = cpioStream.getNextEntry();
        assertNull(nextEntry);
        cpioStream.close();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws Exception {
        ArchiveEntry entry = new CpioArchiveEntry("testFile.txt");
        cpioStream.putArchiveEntry(entry);
        cpioStream.write("Test String.".getBytes());
        cpioStream.finish(); // close the ArchiveOutputStream, but not the test entry
        cpioStream.write("Test String.".getBytes()); // should throw an exception
    }

    @Test
    public void testCreateArchiveEntry() throws Exception {
        File testFile = new File(TestFiles.TEST_FILES_TEMP_FOLDER, "testFile.txt");
        ArchiveEntry entry = cpioStream.createArchiveEntry(testFile, "testFile.txt");
        assertEquals("testFile.txt", entry.getName());
        cpioStream.putArchiveEntry(entry);
        cpioStream.write("Test String.".getBytes());
        cpioStream.closeArchiveEntry();
        testFile.delete();
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryWithNullFile() throws Exception {
        cpioStream.createArchiveEntry(null, "testFile.txt");
    }
}