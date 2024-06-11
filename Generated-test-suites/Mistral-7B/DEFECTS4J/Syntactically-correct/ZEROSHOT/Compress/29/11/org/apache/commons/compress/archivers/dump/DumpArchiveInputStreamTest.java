import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class DumpArchiveInputStreamTest {

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] testData = new byte[1024];
        new TapeOutputStream(new ByteArrayOutputStream(1024), 512).write(testData);

        InputStream is = new ByteArrayInputStream(testData);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, null);

        DumpArchiveEntry entry = inputStream.getNextEntry();
        Assert.assertNotNull(entry);

        entry = inputStream.getNextEntry();
        Assert.assertNull(entry);

        inputStream.close();
    }

    @Test
    public void testRead() throws IOException {
        byte[] testData = new byte[1024];
        new TapeOutputStream(new ByteArrayOutputStream(1024), 512).write(testData);

        InputStream is = new ByteArrayInputStream(testData);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, null);

        byte[] buffer = new byte[256];
        int len = inputStream.read(buffer, 0, buffer.length);
        Assert.assertGreaterThan(len, 0);

        inputStream.close();
    }

    @Test
    public void testMatches() {
        byte[] testData = new byte[32];
        Arrays.fill(testData, (byte) 0x31);

        Assert.assertTrue(DumpArchiveInputStream.matches(testData, testData.length));

        testData[0] = (byte) 0x32;
        Assert.assertFalse(DumpArchiveInputStream.matches(testData, testData.length));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        new DumpArchiveInputStream(null, null);
    }

    @Test(expected = ArchiveException.class)
    public void testInvalidFile() throws IOException {
        byte[] testData = new byte[1024];
        new TapeOutputStream(new ByteArrayOutputStream(1024), 512).write(testData);

        InputStream is = new ByteArrayInputStream(testData);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, null);

        inputStream.read(new byte[1], 0, 1);
        inputStream.read(new byte[1], 0, 1);

        inputStream.getNextEntry();

        try {
            inputStream.getNextEntry();
        } finally {
            inputStream.close();
        }

        inputStream = new DumpArchiveInputStream(is, null);

        inputStream.getNextEntry();
        inputStream.close();

        inputStream = new DumpArchiveInputStream(is, null);

        try {
            inputStream.getNextEntry();
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testGetSummary() throws IOException {
        byte[] testData = new byte[1024];
        new TapeOutputStream(new ByteArrayOutputStream(1024), 512).write(testData);

        InputStream is = new ByteArrayInputStream(testData);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, null);

        DumpArchiveSummary summary = inputStream.getSummary();
        Assert.assertNotNull(summary);

        inputStream.close();
    }

    @Test
    public void testGetCount() throws IOException {
        byte[] testData = new byte[1024];
        new TapeOutputStream(new ByteArrayOutputStream(1024), 512).write(testData);

        InputStream is = new ByteArrayInputStream(testData);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, null);

        long count = inputStream.getBytesRead();
        Assert.assertEquals(0, count);

        byte[] buffer = new byte[256];
        int len = inputStream.read(buffer, 0, buffer.length);
        Assert.assertGreaterThan(len, 0);
        count += len;

        Assert.assertEquals(len, inputStream.getBytesRead() - count);

        inputStream.close();
    }

    @Test
    public void testGetBytesRead() throws IOException {
        byte[] testData = new byte[1024];
        new TapeOutputStream(new ByteArrayOutputStream(1024), 512).write(testData);

        InputStream is = new ByteArrayInputStream(testData);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, null);

        long count = inputStream.getBytesRead();
        Assert.assertEquals(0, count);

        byte[] buffer = new byte[256];
        int len = inputStream.read(buffer, 0, buffer.length);
        Assert.assertGreaterThan(len, 0);
        count += len;

        Assert.assertEquals(len, inputStream.getBytesRead() - count);

        inputStream.close();
    }

    @Test
    public void testReadCLRI() throws IOException {
        byte[] testData = new byte[1024];
        new TapeOutputStream(new ByteArrayOutputStream(1024), 512).write(testData);

        InputStream is = new ByteArrayInputStream(testData);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, null);

        inputStream.readCLRI();

        inputStream.close();
    }

    @Test
    public void testReadBITS() throws IOException {
        byte[] testData = new byte[1024];
        new TapeOutputStream(new ByteArrayOutputStream(1024), 512).write(testData);

        InputStream is = new ByteArrayInputStream(testData);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, null);

        inputStream.readBITS();

        inputStream.close();
    }

    @Test
    public void testGetPath() throws IOException {
        byte[] testData = new byte[1024];
        new TapeOutputStream(new ByteArrayOutputStream(1024), 512).write(testData);

        InputStream is = new ByteArrayInputStream(testData);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, null);

        DumpArchiveEntry entry = inputStream.getNextEntry();
        String path = inputStream.getPath(entry);
        Assert.assertNull(path);

        inputStream.close();
    }

    @Test
    public void testGetPathWithValidEntry() throws IOException {
        byte[] testData = new byte[1024];
        new TapeOutputStream(new ByteArrayOutputStream(1024), 512).write(testData);

        InputStream is = new ByteArrayInputStream(testData);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, null);

        DumpArchiveEntry entry = inputStream.getNextEntry();
        String path = inputStream.getPath(entry);
        Assert.assertNull(path);

        inputStream.getNextEntry();
        entry = inputStream.getNextEntry();

        path = inputStream.getPath(entry);
        Assert.assertNotNull(path);

        inputStream.close();
    }
}