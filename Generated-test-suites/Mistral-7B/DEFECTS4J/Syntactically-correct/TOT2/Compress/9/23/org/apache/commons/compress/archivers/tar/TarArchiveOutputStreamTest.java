import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    @Test
    public void testNormalOperation() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        File testFile = new File("testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFile.txt");
        tarArchiveOutputStream.putNextEntry(entry);
        byte[] testData = "Test data for TarArchiveOutputStreamTest.".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveOutputStream testTarArchiveOutputStream = new TarArchiveOutputStream(inputStream);
        TarArchiveEntry testEntry = testTarArchiveOutputStream.getNextEntry();
        Assert.assertEquals("testFile.txt", testEntry.getName());
        byte[] readData = new byte[testData.length];
        testTarArchiveOutputStream.read(readData, 0, testData.length);
        Assert.assertArrayEquals(testData, readData);
        testTarArchiveOutputStream.close();
    }

    @Test
    public void testLongFileName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        File longFileNameFile = new File("longFileName.txt");
        TarArchiveEntry longFileNameEntry = new TarArchiveEntry(longFileNameFile, "longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName.txt");
        tarArchiveOutputStream.putNextEntry(longFileNameEntry);
        byte[] testData = "Test data for TarArchiveOutputStreamTest.".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveOutputStream testTarArchiveOutputStream = new TarArchiveOutputStream(inputStream);
        TarArchiveEntry testEntry = testTarArchiveOutputStream.getNextEntry();
        Assert.assertEquals("longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName.txt", testEntry.getName());
        byte[] readData = new byte[testData.length];
        testTarArchiveOutputStream.read(readData, 0, testData.length);
        Assert.assertArrayEquals(testData, readData);
        testTarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLongFileNameError() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        File longFileNameFile = new File("longFileName.txt");
        TarArchiveEntry longFileNameEntry = new TarArchiveEntry(longFileNameFile, "longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName/longFileName.txt");
        tarArchiveOutputStream.putNextEntry(longFileNameEntry);
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testCloseArchiveEntryBeforeFinish() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        File testFile = new File("testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFile.txt");
        tarArchiveOutputStream.putNextEntry(entry);
        byte[] testData = "Test data for TarArchiveOutputStreamTest.".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.closeArchiveEntry(); // <-- Shouldn't be called here
        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveOutputStream testTarArchiveOutputStream = new TarArchiveOutputStream(inputStream);
        TarArchiveEntry testEntry = testTarArchiveOutputStream.getNextEntry();
        Assert.assertEquals("testFile.txt", testEntry.getName());
        byte[] readData = new byte[testData.length];
        testTarArchiveOutputStream.read(readData, 0, testData.length);
        Assert.assertArrayEquals(testData, readData);
        testTarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        File testFile = new File("testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFile.txt");
        tarArchiveOutputStream.putNextEntry(entry);
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        File testDirectory = new File("testDirectory");
        TarArchiveEntry entry = new TarArchiveEntry(testDirectory, ".");
        tarArchiveOutputStream.putNextEntry(entry);
        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveOutputStream testTarArchiveOutputStream = new TarArchiveOutputStream(inputStream);
        TarArchiveEntry testEntry = testTarArchiveOutputStream.getNextEntry();
        Assert.assertTrue(testEntry.isDirectory());
        testTarArchiveOutputStream.close();
    }

    @Test
    public void testWriteBytesBeyondEntrySize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        File testFile = new File("testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFile.txt");
        tarArchiveOutputStream.putNextEntry(entry);
        byte[] testData = "Test data for TarArchiveOutputStreamTest.".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testData, 0, testData.length);
        byte[] extraData = "Extra test data.".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(extraData, 0, extraData.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveOutputStream testTarArchiveOutputStream = new TarArchiveOutputStream(inputStream);
        TarArchiveEntry testEntry = testTarArchiveOutputStream.getNextEntry();
        Assert.assertEquals("testFile.txt", testEntry.getName());
        byte[] readData = new byte[testData.length + extraData.length];
        testTarArchiveOutputStream.read(readData, 0, readData.length);
        Assert.assertArrayEquals(new byte[testData.length], Arrays.copyOfRange(readData, 0, testData.length));
        Assert.assertArrayEquals(new byte[extraData.length], Arrays.copyOfRange(readData, testData.length, readData.length));
        testTarArchiveOutputStream.close();
    }
}