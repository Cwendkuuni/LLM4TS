package org.apache.commons.compress.archivers.cpio;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.utils.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;
    private Map<String, ArchiveEntry> entries;

    @Before
    public void setUp() throws IOException {
        outputStream = new FilterOutputStream(new ByteArrayOutputStream());
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);
        entries = new HashMap<>();
    }

    @After
    public void tearDown() throws IOException {
        if (cpioArchiveOutputStream != null) {
            cpioArchiveOutputStream.close();
        }
    }

    /**
     * Test writing a single entry with default parameters.
     */
    @Test
    public void testWriteSingleEntry() throws IOException {
        String name = "testfile";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(name);
        entry.setSize(5);
        cpioArchiveOutputStream.putNextEntry(entry);
        byte[] testData = "12345".getBytes();
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) outputStream;
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ArchiveEntry readEntry = cpioArchiveOutputStream.getNextEntry();
        Assert.assertEquals(name, readEntry.getName());
        Assert.assertEquals(5, readEntry.getSize());
        byte[] readData = new byte[5];
        inputStream.read(readData);
        Assert.assertArrayEquals(testData, readData);
        inputStream.close();
        readEntry.close();
    }

    /**
     * Test writing an entry with a custom format.
     */
    @Test
    public void testWriteCustomFormatEntry() throws IOException {
        String name = "testfile";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(name);
        entry.setFormat(CpioConstants.FORMAT_OLD_BINARY);
        entry.setSize(5);
        cpioArchiveOutputStream.putNextEntry(entry);
        byte[] testData = "12345".getBytes();
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) outputStream;
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ArchiveEntry readEntry = cpioArchiveOutputStream.getNextEntry();
        Assert.assertEquals(name, readEntry.getName());
        Assert.assertEquals(5, readEntry.getSize());
        Assert.assertEquals(CpioConstants.FORMAT_OLD_BINARY, readEntry.getFormat());
        byte[] readData = new byte[5];
        inputStream.read(readData);
        Assert.assertArrayEquals(testData, readData);
        inputStream.close();
        readEntry.close();
    }

    /**
     * Test writing multiple entries.
     */
    @Test
    public void testWriteMultipleEntries() throws IOException {
        String name1 = "testfile1";
        String name2 = "testfile2";
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(name1);
        entry1.setSize(5);
        cpioArchiveOutputStream.putNextEntry(entry1);
        byte[] testData1 = "12345".getBytes();
        cpioArchiveOutputStream.write(testData1, 0, testData1.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(name2);
        entry2.setSize(10);
        entry2.setFormat(CpioConstants.FORMAT_NEW_CRC);
        cpioArchiveOutputStream.putNextEntry(entry2);
        byte[] testData2 = "6789012345".getBytes();
        cpioArchiveOutputStream.write(testData2, 0, testData2.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) outputStream;
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ArchiveEntry readEntry1 = cpioArchiveOutputStream.getNextEntry();
        Assert.assertEquals(name1, readEntry1.getName());
        Assert.assertEquals(5, readEntry1.getSize());
        readEntry1.close();

        ArchiveEntry readEntry2 = cpioArchiveOutputStream.getNextEntry();
        Assert.assertEquals(name2, readEntry2.getName());
        Assert.assertEquals(10, readEntry2.getSize());
        Assert.assertEquals(CpioConstants.FORMAT_NEW_CRC, readEntry2.getFormat());
        byte[] readData2 = new byte[10];
        inputStream.read(readData2);
        Assert.assertArrayEquals(testData2, readData2);
        inputStream.close();
        readEntry2.close();
    }

    /**
     * Test writing an entry with a negative size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWriteNegativeSizeEntry() throws IOException {
        String name = "testfile";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(name);
        entry.setSize(-5);
        cpioArchiveOutputStream.putNextEntry(entry);
    }

    /**
     * Test writing an entry with a negative name length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWriteNegativeNameLengthEntry() throws IOException {
        String name = "testfile";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(name);
        entry.setSize(5);
        entry.setNameLength(-1);
        cpioArchiveOutputStream.putNextEntry(entry);
    }
}