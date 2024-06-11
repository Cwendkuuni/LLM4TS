import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(5);
        tarArchiveOutputStream.putArchiveEntry(entry);

        ByteArrayOutputStream entryOutputStream = new ByteArrayOutputStream();
        byte[] testData = "Hello World".getBytes();
        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.closeArchiveEntry();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry testEntry = new TarArchiveEntry(inputStream);
        Assert.assertEquals("test.txt", testEntry.getName());
        Assert.assertEquals(6L, testEntry.getSize());
        Assert.assertArrayEquals(testData, IOUtils.toByteArray(testEntry.getInputStream()));

        tarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        TarArchiveEntry entry = new TarArchiveEntry("test/testFile.txt");
        entry.setSize(5);
        tarArchiveOutputStream.putArchiveEntry(entry);

        ByteArrayOutputStream entryOutputStream = new ByteArrayOutputStream();
        byte[] testData = "Hello World".getBytes();
        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.closeArchiveEntry();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry testEntry = new TarArchiveEntry(inputStream);
        Assert.assertEquals("test/testFile.txt", testEntry.getName());
        Assert.assertEquals(6L, testEntry.getSize());
        Assert.assertArrayEquals(testData, IOUtils.toByteArray(testEntry.getInputStream()));

        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(5);
        tarArchiveOutputStream.putArchiveEntry(entry);

        ByteArrayOutputStream entryOutputStream = new ByteArrayOutputStream();
        byte[] testData = "Hello World".getBytes();
        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry testEntry = new TarArchiveEntry(inputStream);
        Assert.assertEquals("test.txt", testEntry.getName());
        Assert.assertEquals(6L, testEntry.getSize());
        Assert.assertArrayEquals(testData, IOUtils.toByteArray(testEntry.getInputStream()));

        tarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(5);
        tarArchiveOutputStream.putArchiveEntry(entry);

        ByteArrayOutputStream entryOutputStream = new ByteArrayOutputStream();
        byte[] testData = "Hello World".getBytes();
        tarArchiveOutputStream.write(testData, 0, testData.length);

        tarArchiveOutputStream.finish();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry testEntry = new TarArchiveEntry(inputStream);
        Assert.assertEquals("test.txt", testEntry.getName());
        Assert.assertEquals(6L, testEntry.getSize());
        Assert.assertArrayEquals(testData, IOUtils.toByteArray(testEntry.getInputStream()));

        tarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryAfterFinish() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.finish();

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(5);
        tarArchiveOutputStream.putArchiveEntry(entry);

        ByteArrayOutputStream entryOutputStream = new ByteArrayOutputStream();
        byte[] testData = "Hello World".getBytes();
        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.close();
    }
}