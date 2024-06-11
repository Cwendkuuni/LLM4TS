import org.apache.commons.compress.archivers.ArchiveException;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Test for DumpArchiveInputStream class
 */
public class DumpArchiveInputStreamTest {

    /**
     * Test constructor with default encoding
     * @throws Exception
     */
    @Test
    public void testDefaultEncoding() throws Exception {
        byte[] data = readDumpArchiveTestData();
        InputStream inputStream = new ByteArrayInputStream(data);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);

        DumpArchiveSummary summary = dumpArchiveInputStream.getSummary();
        Assert.assertNotNull(summary);
        Assert.assertEquals("testDefaultEncoding.dmp", summary.getName());
        Assert.assertEquals(5, summary.getEntryCount());

        DumpArchiveEntry testEntry = null;
        for (int i = 0; i < 5; i++) {
            testEntry = dumpArchiveInputStream.getNextEntry();
            Assert.assertNotNull(testEntry);
            Assert.assertNotEquals(0, testEntry.getSize());
        }

        dumpArchiveInputStream.close();
    }

    /**
     * Test constructor with custom encoding
     * @throws Exception
     */
    @Test
    public void testCustomEncoding() throws Exception {
        byte[] data = readDumpArchiveTestData();
        ZipEncoding encoding = ZipEncoding.UTF8; // use UTF-8 encoding here
        InputStream inputStream = new ByteArrayInputStream(data);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, encoding.getName());

        DumpArchiveSummary summary = dumpArchiveInputStream.getSummary();
        Assert.assertNotNull(summary);
        Assert.assertEquals("testCustomEncoding.dmp", summary.getName());
        Assert.assertEquals(5, summary.getEntryCount());

        DumpArchiveEntry testEntry = null;
        for (int i = 0; i < 5; i++) {
            testEntry = dumpArchiveInputStream.getNextEntry();
            Assert.assertNotNull(testEntry);
            Assert.assertNotEquals(0, testEntry.getSize());
        }

        dumpArchiveInputStream.close();
    }

    /**
     * Test reading invalid dump archive.
     * @throws Exception
     */
    @Test(expected = IOException.class)
    public void testInvalidDumpArchive() throws Exception {
        byte[] data = new byte[]{0x0, 0x1, 0x2, 0x3}; // invalid data
        InputStream inputStream = new ByteArrayInputStream(data);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);

        dumpArchiveInputStream.close();
    }

    /**
     * Test reading empty dump archive.
     * @throws Exception
     */
    @Test
    public void testEmptyDumpArchive() throws Exception {
        byte[] data = new byte[0];
        InputStream inputStream = new ByteArrayInputStream(data);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);

        DumpArchiveEntry testEntry = null;
        Assert.assertNull(dumpArchiveInputStream.getNextEntry());

        dumpArchiveInputStream.close();
    }

    /**
     * Test reading non-dump archive.
     * @throws Exception
     */
    @Test(expected = ArchiveException.class)
    public void testNonDumpArchive() throws Exception {
        byte[] data = new byte[100]; // random data
        InputStream inputStream = new ByteArrayInputStream(data);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);

        dumpArchiveInputStream.close();
    }

    /**
     * Test reading end-of-archive
     * @throws Exception
     */
    @Test
    public void testEndOfArchive() throws Exception {
        byte[] data = readDumpArchiveTestData();
        InputStream inputStream = new ByteArrayInputStream(data);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);

        DumpArchiveEntry testEntry = null;
        for (int i = 0; i < 5; i++) {
            testEntry = dumpArchiveInputStream.getNextEntry();
            Assert.assertNotNull(testEntry);
        }

        testEntry = dumpArchiveInputStream.getNextEntry();
        Assert.assertNull(testEntry);

        dumpArchiveInputStream.close();
    }

    /**
     * Test reading corrupted dump archive
     * @throws Exception
     */
    @Test(expected = IOException.class)
    public void testCorruptedDumpArchive() throws Exception {
        byte[] data = readDumpArchiveTestData();
        data[1000] = (byte) 0xFF; // corrupt a byte

        InputStream inputStream = new ByteArrayInputStream(data);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);

        dumpArchiveInputStream.close();
    }

    /**
     * Test reading sparse dump archive
     * @throws Exception
     */
    @Test
    public void testSparseDumpArchive() throws Exception {
        byte[] data = readDumpArchiveTestData();
        InputStream inputStream = new ByteArrayInputStream(data);
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);

        int totalRead = 0;
        byte[] buffer = new byte[1024];
        for (int i = 0; i < 5; i++) {
            int len = dumpArchiveInputStream.read(buffer, 0, buffer.length);
            totalRead += len;
            Assert.assertNotEquals(-1, len);
        }

        dumpArchiveInputStream.close();

        Assert.assertEquals(4379, totalRead);
    }

    /**
     * Helper method to read test data
     * @return test data as byte array
     */
    private byte[] readDumpArchiveTestData() {
        byte[] data = new byte[5120];

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DumpArchiveOutputStream dumpArchiveOutputStream = new DumpArchiveOutputStream(outputStream);

        // write test data
        for (int i = 0; i < 5; i++) {
            String name = String.format("test%d.txt", i);
            DumpArchiveEntry entry = new DumpArchiveEntry(name, 10, 20, false);
            dumpArchiveOutputStream.putNextEntry(entry);
            dumpArchiveOutputStream.write(new byte[10]);
            dumpArchiveOutputStream.closeEntry();
        }

        dumpArchiveOutputStream.finish();
        dumpArchiveOutputStream.close();

        System.arraycopy(outputStream.toByteArray(), 0, data, 0, data.length);

        return data;
    }
}