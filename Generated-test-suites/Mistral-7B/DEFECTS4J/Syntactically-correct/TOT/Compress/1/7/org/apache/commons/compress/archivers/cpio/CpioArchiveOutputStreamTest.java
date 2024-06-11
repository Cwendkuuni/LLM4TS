package org.apache.commons.compress.archivers.cpio.test;

import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntryBuilder;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants.FORMAT_NEW;
import org.apache.commons.compress.archivers.cpio.CpioConstants.FORMAT_OLD_ASCII;
import org.apache.commons.compress.archivers.cpio.CpioConstants.FORMAT_OLD_BINARY;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Test for CpioArchiveOutputStream class
 */
public class CpioArchiveOutputStreamTest {

    /**
     * Test writing new format entry with no name and no data
     */
    @Test
    public void testWriteNewFormatEntryNoNameNoData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(
                outputStream, FORMAT_NEW);
        cpioOutputStream.putNextEntry(new CpioArchiveEntryBuilder()
                .setFormat(FORMAT_NEW)
                .setSize(0)
                .build());
        cpioOutputStream.close();

        Map<String, CpioArchiveEntry> expectedEntryMap = new HashMap<>();
        CpioArchiveEntry expectedEntry = new CpioArchiveEntryBuilder()
                .setFormat(FORMAT_NEW)
                .setName("TRAILER!!!")
                .setSize(0)
                .build();
        expectedEntryMap.put("TRAILER!!!", expectedEntry);

        InputStream inputStream = new ByteArrayInputStream(
                outputStream.toByteArray());
        CpioArchiveInputStream cpioInputstream = new CpioArchiveInputStream(
                inputStream);
        Assert.assertEquals(expectedEntryMap, cpioInputstream.getEntries());
        cpioInputstream.close();
    }

    /**
     * Test writing new format entry with name and data
     */
    @Test
    public void testWriteNewFormatEntryNameData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(
                outputStream, FORMAT_NEW);
        String entryName = "TestEntry";
        CpioArchiveEntry entry = new CpioArchiveEntryBuilder()
                .setFormat(FORMAT_NEW)
                .setName(entryName)
                .setSize(testData.length)
                .build();
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write(testData);
        cpioOutputStream.close();

        Map<String, CpioArchiveEntry> expectedEntryMap = new HashMap<>();
        CpioArchiveEntry expectedEntry = new CpioArchiveEntryBuilder()
                .setFormat(FORMAT_NEW)
                .setName(entryName)
                .setSize(testData.length)
                .build();
        expectedEntryMap.put(entryName, expectedEntry);

        InputStream inputStream = new ByteArrayInputStream(
                outputStream.toByteArray());
        CpioArchiveInputStream cpioInputstream = new CpioArchiveInputStream(
                inputStream);
        Assert.assertEquals(expectedEntryMap, cpioInputstream.getEntries());
        Assert.assertArrayEquals(testData, cpioInputstream.getEntryData(entryName));
        cpioInputstream.close();
    }

    /**
     * Test writing old format ASCII entry with name and data
     */
    @Test
    public void testWriteOldFormatAsciiEntryNameData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(
                outputStream, FORMAT_OLD_ASCII);
        String entryName = "TestEntry";
        CpioArchiveEntry entry = new CpioArchiveEntryBuilder()
                .setFormat(FORMAT_OLD_ASCII)
                .setName(entryName)
                .setSize(testData.length)
                .build();
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write(testData);
        cpioOutputStream.close();

        Map<String, CpioArchiveEntry> expectedEntryMap = new HashMap<>();
        CpioArchiveEntry expectedEntry = new CpioArchiveEntryBuilder()
                .setFormat(FORMAT_OLD_ASCII)
                .setName(entryName)
                .setSize(testData.length)
                .build();
        expectedEntryMap.put(entryName, expectedEntry);

        InputStream inputStream = new ByteArrayInputStream(
                outputStream.toByteArray());
        CpioArchiveInputStream cpioInputstream = new CpioArchiveInputStream(
                inputStream);
        Assert.assertEquals(expectedEntryMap, cpioInputstream.getEntries());
        Assert.assertArrayEquals(testData, cpioInputstream.getEntryData(entryName));
        cpioInputstream.close();
    }

    /**
     * Test writing old format binary entry with name and data
     */
    @Test
    public void testWriteOldFormatBinaryEntryNameData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] testData = new byte[]{0x01, 0x02, 0x03};
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(
                outputStream, FORMAT_OLD_BINARY);
        String entryName = "TestEntry";
        CpioArchiveEntry entry = new CpioArchiveEntryBuilder()
                .setFormat(FORMAT_OLD_BINARY)
                .setName(entryName)
                .setSize(testData.length)
                .build();
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write(testData);
        cpioOutputStream.close();

        Map<String, CpioArchiveEntry> expectedEntryMap = new HashMap<>();
        CpioArchiveEntry expectedEntry = new CpioArchiveEntryBuilder()
                .setFormat(FORMAT_OLD_BINARY)
                .setName(entryName)
                .setSize(testData.length)
                .build();
        expectedEntryMap.put(entryName, expectedEntry);

        InputStream inputStream = new ByteArrayInputStream(
                outputStream.toByteArray());
        CpioArchiveInputStream cpioInputstream = new CpioArchiveInputStream(
                inputStream);
        Assert.assertEquals(expectedEntryMap, cpioInputstream.getEntries());
        Assert.assertArrayEquals(testData, cpioInputstream.getEntryData(entryName));
        cpioInputstream.close();
    }

    /**
     * Test writing old format binary entry with name, data, and swapHalfWord = true
     */
    @Test
    public void testWriteOldFormatBinaryEntryNameDataSwapHalfWord() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] testData = new byte[]{0x01, 0x02, 0x03};
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(
                outputStream, FORMAT_OLD_BINARY);
        String entryName = "TestEntry";
        CpioArchiveEntry entry = new CpioArchiveEntryBuilder()
                .setFormat(FORMAT_OLD_BINARY)
                .setName(entryName)
                .setSize(testData.length)
                .build();
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write(testData, 0, testData.length, 0, null);
        cpioOutputStream.close();

        Map<String, CpioArchiveEntry> expectedEntryMap = new HashMap<>();
        CpioArchiveEntry expectedEntry = new CpioArchiveEntryBuilder()
                .setFormat(FORMAT_OLD_BINARY)
                .setName(entryName)
                .setSize(testData.length)
                .build();
        expectedEntryMap.put(entryName, expectedEntry);

        InputStream inputStream = new ByteArrayInputStream(
                outputStream.toByteArray());
        CpioArchiveInputStream cpioInputstream = new CpioArchiveInputStream(
                inputStream);
        Assert.assertEquals(expectedEntryMap, cpioInputstream.getEntries());
        Assert.assertArrayEquals(testData, cpioInputstream.getEntryData(entryName));
        cpioInputstream.close();

        // Verify that bytes are swapped in the output
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                outputStream.toByteArray());
        byte[] readData = new byte[testData.length];
        byteArrayInputStream.read(readData, 0, testData.length);
        Assert.assertArrayEquals(
                CpioUtil.byteSwap(testData), readData);
    }
}