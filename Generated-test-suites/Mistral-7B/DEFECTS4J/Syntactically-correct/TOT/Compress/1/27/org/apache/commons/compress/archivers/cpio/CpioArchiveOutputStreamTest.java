package org.apache.commons.compress.archivers.cpio;

import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntryImpl;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream out;
    private CpioArchiveOutputStream cpioStream;
    private Map<String, CpioArchiveEntryImpl> entryNames;

    @Before
    public void setUp() throws IOException {
        out = new ByteArrayOutputStream();
        cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        entryNames = new HashMap<>();
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveEntryImpl entry = new CpioArchiveEntryImpl("test1.txt");
        entry.setSize(13);
        cpioStream.putNextEntry(entry);
        byte[] testData = "Test data for test file 1".getBytes();
        cpioStream.write(testData);
        cpioStream.closeArchiveEntry();

        Assert.assertEquals(13, cpioStream.getWritten());
        Assert.assertArrayEquals(testData, cpioStream.getWrittenData());

        byte[] actualData = ((ByteArrayOutputStream) out).toByteArray();
        byte[] expectedData = new byte[17 + testData.length];
        System.arraycopy(CpioConstants.MAGIC_NEW.getBytes(), 0, expectedData, 0,
                CpioConstants.MAGIC_NEW.length);
        writeNewEntry(entry, expectedData, 13);
        System.arraycopy(testData, 0, expectedData, 17, testData.length);
        Assert.assertArrayEquals(expectedData, actualData);

        entryNames.put(entry.getName(), entry);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithDuplicateName() throws IOException {
        CpioArchiveEntryImpl entry1 = new CpioArchiveEntryImpl("test2.txt");
        cpioStream.putNextEntry(entry1);
        CpioArchiveEntryImpl entry2 = new CpioArchiveEntryImpl("test2.txt");
        cpioStream.putNextEntry(entry2);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithNegativeSize() throws IOException {
        CpioArchiveEntryImpl entry = new CpioArchiveEntryImpl("test3.txt");
        entry.setSize(-1);
        cpioStream.putNextEntry(entry);
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntryImpl entry1 = new CpioArchiveEntryImpl("test4.txt");
        entry1.setSize(7);
        cpioStream.putNextEntry(entry1);
        byte[] testData1 = "Test data for test file 1".getBytes();
        cpioStream.write(testData1);
        cpioStream.closeArchiveEntry();

        CpioArchiveEntryImpl entry2 = new CpioArchiveEntryImpl("test5.txt");
        entry2.setSize(11);
        cpioStream.putNextEntry(entry2);
        byte[] testData2 = "Test data for test file 2".getBytes();
        cpioStream.write(testData2);
        cpioStream.closeArchiveEntry();

        cpioStream.finish();

        byte[] actualData = ((ByteArrayOutputStream) out).toByteArray();
        int numEntries = entryNames.size();
        int totalSize = 0;
        for (CpioArchiveEntryImpl entry : entryNames.values()) {
            totalSize += entry.getSize();
        }

        byte[] expectedData = new byte[numEntries * (17 + testData1.length) +
                testData2.length + 33];
        int pos = 0;
        for (CpioArchiveEntryImpl entry : entryNames.values()) {
            writeNewEntry(entry, expectedData, entry.getSize());
            System.arraycopy(entry.getName().getBytes(), 0, expectedData, pos,
                    entry.getName().length() + 1);
            pos += entry.getName().length() + 1;
        }
        System.arraycopy(CpioConstants.MAGIC_NEW_CRC.getBytes(), 0, expectedData,
                pos, CpioConstants.MAGIC_NEW_CRC.length);
        pos += CpioConstants.MAGIC_NEW_CRC.length;
        System.arraycopy(testData1, 0, expectedData, pos, testData1.length);
        pos += testData1.length;
        System.arraycopy(testData2, 0, expectedData, pos, testData2.length);

        Assert.assertArrayEquals(expectedData, actualData);
    }

    private void writeNewEntry(CpioArchiveEntryImpl entry, byte[] data, int size) {
        System.arraycopy(CpioConstants.MAGIC_NEW.getBytes(), 0, data, 0,
                CpioConstants.MAGIC_NEW.length);
        writeNewEntryContent(entry, data, 17, size);
        System.arraycopy(entry.getName().getBytes(), 0, data, 17,
                entry.getName().length() + 1);
    }

    private void writeNewEntryContent(CpioArchiveEntryImpl entry, byte[] data,
                                     int offset, int size) {
        data[offset + 8] = (byte) (CpioConstants.FORMAT_NEW & 0xFF);
        data[offset + 9] = (byte) ((CpioConstants.FORMAT_NEW >> 8) & 0xFF);
        data[offset + 10] = (byte) ((CpioConstants.FORMAT_NEW >> 16) & 0xFF);
        data[offset + 11] = (byte) ((CpioConstants.FORMAT_NEW >> 24) & 0xFF);

        data[offset + 12] = (byte) (entry.getMode() & 0xFF);
        data[offset + 13] = (byte) ((entry.getMode() >> 8) & 0xFF);

        writeAsciiLong(data, offset + 14, entry.getUID(), 8);
        writeAsciiLong(data, offset + 24, entry.getGID(), 8);
        writeAsciiLong(data, offset + 32, entry.getNumberOfLinks(), 8);
        writeAsciiLong(data, offset + 36, entry.getTime(), 8);
        writeAsciiLong(data, offset + 44, entry.getSize(), 8);
        writeAsciiLong(data, offset + 52, entry.getDeviceMaj(), 8);
        writeAsciiLong(data, offset + 56, entry.getDeviceMin(), 8);
        writeAsciiLong(data, offset + 60, entry.getRemoteDeviceMaj(), 8);
        writeAsciiLong(data, offset + 64, entry.getRemoteDeviceMin(), 8);
        writeAsciiLong(data, offset + 68, entry.getName().length() + 1, 8);
        writeAsciiLong(data, offset + 72, entry.getChksum(), 8);

        if (size > 0) {
            System.arraycopy(entry.getName().getBytes(), 0, data, offset + 84,
                    entry.getName().length());
            System.arraycopy(entry.getName().getBytes(), 0, data, offset + 91,
                    entry.getName().length());
            data[offset + 91 + entry.getName().length()] = 0;
        }
    }

    private void writeAsciiLong(byte[] data, int offset, long number, int radix) {
        String numberStr = Long.toString(number, radix);
        int length = numberStr.length();
        for (int i = length - 1; i >= 0; i--) {
            data[offset + i] = (byte) numberStr.charAt(i);
        }
    }

    private byte[] getWrittenData() throws IOException {
        return IOUtils.toByteArray(new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray()));
    }

    @Test
    public void testGetWrittenData() throws IOException {
        CpioArchiveEntryImpl entry1 = new CpioArchiveEntryImpl("test1.txt");
        entry1.setSize(13);
        cpioStream.putNextEntry(entry1);
        byte[] testData = "Test data for test file 1".getBytes();
        cpioStream.write(testData);
        cpioStream.closeArchiveEntry();

        byte[] actualData = cpioStream.getWrittenData();
        byte[] expectedData = new byte[17 + testData.length];
        System.arraycopy(CpioConstants.MAGIC_NEW.getBytes(), 0, expectedData, 0,
                CpioConstants.MAGIC_NEW.length);
        writeNewEntry(entry1, expectedData, 13);
        System.arraycopy(testData, 0, expectedData, 17, testData.length);
        Assert.assertArrayEquals(expectedData, actualData);
    }
}