package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtils;

public class CpioArchiveOutputStreamTest {

    @org.junit.Test
    public void testCpioArchiveOutputStream() throws IOException {
        // Test creating a new CpioArchiveOutputStream with the new format
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                outputStream, CpioConstants.FORMAT_NEW);
        assertFalse(cpioArchiveOutputStream.isFinished());
        assertFalse(cpioArchiveOutputStream.isClosed());

        // Test putting an entry and writing data
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG);
        cpioArchiveOutputStream.putArchiveEntry(entry);
        byte[] testData = "12345".getBytes();
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        // Test reading back the data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                outputStream.toByteArray());
        ArchiveEntry archiveEntry = cpioArchiveOutputStream.createArchiveEntry(
                new File("testfile"), "testfile");
        byte[] readData = new byte[testData.length];
        int read = inputStream.read(readData);
        assertEquals(testData.length, read);
        assertArrayEquals(testData, readData);

        // Test finishing the stream
        cpioArchiveOutputStream.finish();
        assertTrue(cpioArchiveOutputStream.isFinished());
        assertFalse(cpioArchiveOutputStream.isClosed());

        // Test writing multiple entries
        byte[] testData2 = "67890".getBytes();
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile2");
        entry2.setFileSize(testData2.length);
        entry2.setMode(CpioConstants.C_ISREG);
        cpioArchiveOutputStream.putArchiveEntry(entry2);
        cpioArchiveOutputStream.write(testData2, 0, testData2.length);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();

        // Test reading back all data
        byte[] expectedData = new byte[testData.length + testData2.length];
        System.arraycopy(testData, 0, expectedData, 0, testData.length);
        System.arraycopy(testData2, 0, expectedData, testData.length,
                testData2.length);
        byte[] readAllData = new byte[expectedData.length];
        int readAll = inputStream.read(readAllData);
        assertEquals(expectedData.length, readAll);
        assertArrayEquals(expectedData, readAllData);

        // Test closing the stream
        cpioArchiveOutputStream.close();
        assertTrue(cpioArchiveOutputStream.isClosed());
        assertNull(cpioArchiveOutputStream.entry);
        assertNull(cpioArchiveOutputStream.names.get("testfile"));
        assertNull(cpioArchiveOutputStream.names.get("testfile2"));
    }

    @org.junit.Test
    public void testCpioArchiveOutputStreamOldFormat() throws IOException {
        // Test creating a new CpioArchiveOutputStream with the old format
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                outputStream);
        assertFalse(cpioArchiveOutputStream.isFinished());
        assertFalse(cpioArchiveOutputStream.isClosed());

        // Test putting an entry and writing data
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG);
        entry.setFormat(CpioConstants.FORMAT_OLD_BINARY);
        cpioArchiveOutputStream.putArchiveEntry(entry);
        byte[] testData = "12345".getBytes();
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        // Test reading back the data with old format
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                outputStream.toByteArray());
        ArchiveEntry archiveEntry = cpioArchiveOutputStream.createArchiveEntry(
                new File("testfile"), "testfile");
        byte[] readData = new byte[testData.length];
        int read = inputStream.read(readData);
        assertEquals(testData.length, read);
        assertArrayEquals(testData, readData);

        // Test finishing the stream
        cpioArchiveOutputStream.finish();
        assertTrue(cpioArchiveOutputStream.isFinished());
        assertFalse(cpioArchiveOutputStream.isClosed());

        // Test writing multiple entries with old format
        byte[] testData2 = "67890".getBytes();
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile2");
        entry2.setFileSize(testData2.length);
        entry2.setMode(CpioConstants.C_ISREG);
        entry2.setFormat(CpioConstants.FORMAT_OLD_BINARY);
        cpioArchiveOutputStream.putArchiveEntry(entry2);
        cpioArchiveOutputStream.write(testData2, 0, testData2.length);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();

        // Test reading back all data with old format
        byte[] expectedData = new byte[testData.length + testData2.length];
        System.arraycopy(testData, 0, expectedData, 0, testData.length);
        System.arraycopy(testData2, 0, expectedData, testData.length,
                testData2.length);
        byte[] readAllData = new byte[expectedData.length];
        int readAll = inputStream.read(readAllData);
        assertEquals(expectedData.length, readAll);
        assertArrayEquals(expectedData, readAllData);

        // Test closing the stream
        cpioArchiveOutputStream.close();
        assertTrue(cpioArchiveOutputStream.isClosed());
        assertNull(cpioArchiveOutputStream.entry);
        assertNull(cpioArchiveOutputStream.names.get("testfile"));
        assertNull(cpioArchiveOutputStream.names.get("testfile2"));
    }

    @org.junit.Test
    public void testCpioArchiveOutputStreamNewFormatWithCRC()
            throws IOException {
        // Test creating a new CpioArchiveOutputStream with the new format and CRC
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                outputStream, CpioConstants.FORMAT_NEW_CRC);
        assertFalse(cpioArchiveOutputStream.isFinished());
        assertFalse(cpioArchiveOutputStream.isClosed());

        // Test putting an entry and writing data
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG);
        cpioArchiveOutputStream.putArchiveEntry(entry);
        byte[] testData = "12345".getBytes();
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        // Test reading back the data with new format and CRC
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                outputStream.toByteArray());
        ArchiveEntry archiveEntry = cpioArchiveOutputStream.createArchiveEntry(
                new File("testfile"), "testfile");
        byte[] readData = new byte[testData.length];
        int read = inputStream.read(readData);
        assertEquals(testData.length, read);
        assertArrayEquals(testData, readData);

        // Test checking the CRC
        byte[] calculatedCRC = new byte[4];
        for (int i = 0; i < testData.length; i++) {
            calculatedCRC[i % 4] ^= testData[i];
        }
        byte[] readCRC = new byte[4];
        int readCRCCount = inputStream.read(readCRC);
        assertArrayEquals(calculatedCRC, readCRC);

        // Test finishing the stream
        cpioArchiveOutputStream.finish();
        assertTrue(cpioArchiveOutputStream.isFinished());
        assertFalse(cpioArchiveOutputStream.isClosed());

        // Test writing multiple entries with new format and CRC
        byte[] testData2 = "67890".getBytes();
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile2");
        entry2.setFileSize(testData2.length);
        entry2.setMode(CpioConstants.C_ISREG);
        cpioArchiveOutputStream.putArchiveEntry(entry2);
        cpioArchiveOutputStream.write(testData2, 0, testData2.length);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();

        // Test reading back all data with new format and CRC
        byte[] expectedData = new byte[testData.length + testData2.length];
        System.arraycopy(testData, 0, expectedData, 0, testData.length);
        System.arraycopy(testData2, 0, expectedData, testData.length,
                testData2.length);
        byte[] readAllData = new byte[expectedData.length];
        int readAll = inputStream.read(readAllData);
        assertEquals(expectedData.length, readAll);
        assertArrayEquals(expectedData, readAllData);

        // Test checking the CRC for multiple entries
        byte[] calculatedCRC2 = new byte[4];
        for (int i = 0; i < testData.length + testData2.length; i++) {
            calculatedCRC2[i % 4] ^= expectedData[i];
        }
        byte[] readCRC2 = new byte[4];
        int readCRC2Count = inputStream.read(readCRC2);
        assertArrayEquals(calculatedCRC2, readCRC2);

        // Test closing the stream
        cpioArchiveOutputStream.close();
        assertTrue(cpioArchiveOutputStream.isClosed());
        assertNull(cpioArchiveOutputStream.entry);
        assertNull(cpioArchiveOutputStream.names.get("testfile"));
        assertNull(cpioArchiveOutputStream.names.get("testfile2"));
    }

    @org.junit.Test
    public void testCpioArchiveOutputStreamWithInvalidFormat()
            throws IOException {
        // Test creating a new CpioArchiveOutputStream with an invalid format
        OutputStream outputStream = new ByteArrayOutputStream();
        assertThrows(IllegalArgumentException.class, () -> {
            new CpioArchiveOutputStream(outputStream, (short) 10);
        });
    }

    @org.junit.Test
    public void testCpioArchiveOutputStreamWithEmptyEntryName()
            throws IOException {
        // Test creating a new CpioArchiveEntry with an empty entry name
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                outputStream);

        // Test putting an empty entry and writing data
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG);
        cpioArchiveOutputStream.putArchiveEntry(entry);
        byte[] testData = "12345".getBytes();
        cpioArchiveOutputStream.write(testData, 0, testData.length);

        // Test exception when closing the stream
        assertThrows(IOException.class, () -> {
            cpioArchiveOutputStream.closeArchiveEntry();
        });

        // Test finishing the stream
        cpioArchiveOutputStream.finish();

        // Test reading back the data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                outputStream.toByteArray());
        ArchiveEntry archiveEntry = cpioArchiveOutputStream.createArchiveEntry(
                new File(""), "");
        byte[] readData = new byte[testData.length];
        int read = inputStream.read(readData);
        assertEquals(testData.length, read);
        assertArrayEquals(testData, readData);

        // Test closing the stream
        cpioArchiveOutputStream.close();
    }

    @org.junit.Test
    public void testCpioArchiveOutputStreamWithDuplicateEntry()
            throws IOException {
        // Test creating a new CpioArchiveOutputStream
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                outputStream);

        // Test putting the same entry twice and writing data
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG);
        cpioArchiveOutputStream.putArchiveEntry(entry);
        byte[] testData = "12345".getBytes();
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        // Test exception when finishing the stream
        assertThrows(IOException.class, () -> {
            cpioArchiveOutputStream.finish();
        });

        // Test reading back the data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                outputStream.toByteArray());
        ArchiveEntry archiveEntry = cpioArchiveOutputStream.createArchiveEntry(
                new File("testfile"), "testfile");
        byte[] readData = new byte[testData.length];
        int read = inputStream.read(readData);
        assertEquals(testData.length, read);
        assertArrayEquals(testData, readData);

        // Test closing the stream
        cpioArchiveOutputStream.close();
    }
}