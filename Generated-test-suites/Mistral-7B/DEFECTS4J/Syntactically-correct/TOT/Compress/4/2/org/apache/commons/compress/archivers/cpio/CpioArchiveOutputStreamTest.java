import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CpioArchiveOutputStreamTest {

    private static ByteArrayOutputStream testOutputStream;
    private static CpioArchiveOutputStream testCpioOutputStream;
    private static CpioArchiveEntry testEntry;

    @BeforeClass
    public static void setUp() throws IOException {
        testOutputStream = new ByteArrayOutputStream();
        testCpioOutputStream = new CpioArchiveOutputStream(testOutputStream, CpioConstants.FORMAT_NEW);
        testEntry = new CpioArchiveEntry();
        testEntry.setName("testfile");
        testEntry.setSize(10);
    }

    @Test
    public void testPutArchiveEntryWithValidData() throws IOException {
        testCpioOutputStream.putArchiveEntry(testEntry);
        testEntry.writeTo(testCpioOutputStream);
        testCpioOutputStream.write(new byte[10], 0, 10);
        testCpioOutputStream.closeArchiveEntry();
        assertEquals(CpioConstants.FORMAT_NEW, testCpioOutputStream.getFormat());
        assertEquals(1, testCpioOutputStream.getSize());
        assertEquals(1, testCpioOutputStream.getNumberOfEntries());
        testCpioOutputStream.finish();
        assertEquals("testfile", testCpioOutputStream.getNameIndex(0));
        assertEquals(10, testCpioOutputStream.getEntrySize(0));
        assertEquals(CpioConstants.C_ISREG, testCpioOutputStream.getEntryMode(0));
        assertNull(testCpioOutputStream.getEntryTime(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        testCpioOutputStream = new CpioArchiveOutputStream(testOutputStream, (short) 123);
        testCpioOutputStream.putArchiveEntry(testEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateEntry() throws IOException {
        testCpioOutputStream.putArchiveEntry(testEntry);
        testCpioOutputStream.putArchiveEntry(testEntry);
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedStream() throws IOException {
        testCpioOutputStream.close();
        testCpioOutputStream.write(new byte[10], 0, 10);
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidSize() throws IOException {
        testCpioOutputStream.putArchiveEntry(testEntry);
        testCpioOutputStream.write(new byte[11], 0, 10);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullData() throws IOException {
        testCpioOutputStream.putArchiveEntry(testEntry);
        testCpioOutputStream.write(null, 0, 0);
    }

    @Test(expected = IOException.class)
    public void testWriteWithWritingBeyondEntrySize() throws IOException {
        testCpioOutputStream.putArchiveEntry(testEntry);
        testCpioOutputStream.write(new byte[12], 0, 10);
    }

    @Test
    public void testWriteWithWritingValidData() throws IOException {
        testCpioOutputStream.putArchiveEntry(testEntry);
        testCpioOutputStream.write(new byte[10], 0, 10);
        testCpioOutputStream.closeArchiveEntry();
        byte[] testData = testOutputStream.toByteArray();
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData);
        CpioArchiveEntry testReadEntry = new CpioArchiveEntry(testInputStream);
        assertEquals("testfile", testReadEntry.getName());
        assertEquals(10, testReadEntry.getSize());
        byte[] readData = new byte[10];
        int readLen = testInputStream.read(readData);
        assertEquals(10, readLen);
        assertEquals(new String(readData, 0, 10), new String(new byte[10]));
    }

    @AfterClass
    public static void tearDown() throws IOException {
        testCpioOutputStream.close();
        testOutputStream.close();
    }
}