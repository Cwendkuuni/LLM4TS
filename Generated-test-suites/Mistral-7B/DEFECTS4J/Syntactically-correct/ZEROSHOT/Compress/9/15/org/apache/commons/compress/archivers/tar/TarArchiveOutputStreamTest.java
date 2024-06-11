import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream out;
    private File tempFile;

    @Before
    public void setUp() throws Exception {
        out = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(out));
        tempFile = File.createTempFile("tar-test", null);
    }

    @After
    public void tearDown() throws Exception {
        tarOutputStream.close();
        tempFile.delete();
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream(tempFile)));
        assertNull(tarOutputStream.getArchiveEntry());
        assertFalse(tarOutputStream.isFinished());
        assertFalse(tarOutputStream.isClosed());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeBlockSize() {
        new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream(tempFile)), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeRecordSize() {
        new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream(tempFile)), 1, -1);
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry tarEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(tarEntry);
        tarOutputStream.finish();
        byte[] expected = new byte[1024 * 2];
        assertEquals(expected.length, tarOutputStream.getBufferSize());
        byte[] actual = out.toByteArray();
        byte[] eof = new byte[2 * 1024];
        System.arraycopy(expected, 0, eof, 0, 2 * 1024);
        byte[] header = new byte[512];
        System.arraycopy(expected, 0, header, 0, 512);
        assertEquals(header, actual, 0, 512);
        assertEquals(eof, actual, 512, eof.length);

        TarArchiveEntry testEntry = tarOutputStream.getArchiveEntry();
        assertSame(tarEntry, testEntry);
        tarOutputStream.close();
        testEntry = tarOutputStream.getArchiveEntry();
        assertNull(testEntry);
        assertTrue(tarOutputStream.isFinished());
        assertTrue(tarOutputStream.isClosed());
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        TarArchiveEntry tarEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(tarEntry);
        tarOutputStream.finish();
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("test2.txt"));
    }

    @Test(expected = IOException.class)
    public void testCloseWithUnfinishedArchive() throws IOException {
        TarArchiveEntry tarEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(tarEntry);
        tarOutputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry tarEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(tarEntry);
        tarOutputStream.close();
        TarArchiveEntry testEntry = tarOutputStream.getArchiveEntry();
        assertNull(testEntry);
        assertTrue(tarOutputStream.isFinished());
        assertTrue(tarOutputStream.isClosed());
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(tarOutputStream.getRecordSize(), tarOutputStream.getBufferSize() / tarOutputStream.getBlockSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry tarEntry1 = new TarArchiveEntry("test1.txt");
        TarArchiveEntry tarEntry2 = new TarArchiveEntry("test2.txt");
        tarOutputStream.putArchiveEntry(tarEntry1);
        tarOutputStream.putArchiveEntry(tarEntry2);
        byte[] expected = new byte[2 * 1024 * 5 + 2 * 1024 * 512 + 2 * 1024 * 512];
        byte[] actual = out.toByteArray();
        assertEquals(expected.length, tarOutputStream.size());
        // Check first header
        byte[] header = new byte[512];
        System.arraycopy(expected, 0, header, 0, 512);
        assertEquals(header, actual, 0, 512);
        // Check first entry header
        byte[] entry1 = new byte[512];
        System.arraycopy(expected, 512, entry1, 0, 512);
        assertEquals(entry1, actual, 512, 512);
        // Check first entry data
        byte[] entry1Data = new byte[1024];
        System.arraycopy(expected, 512 + 512, entry1Data, 0, 1024);
        assertEquals(entry1Data, actual, 512 + 512, entry1Data.length);
        // Check second header
        byte[] secondHeader = new byte[512];
        System.arraycopy(expected, 512 + 512 + 1024, secondHeader, 0, 512);
        assertEquals(secondHeader, actual, 512 + 512 + 1024, 512);
        // Check second entry header
        byte[] entry2 = new byte[512];
        System.arraycopy(expected, 512 + 512 + 1024 + 512, entry2, 0, 512);
        assertEquals(entry2, actual, 512 + 512 + 1024 + 512, 512);
        // Check second entry data
        byte[] entry2Data = new byte[1024];
        System.arraycopy(expected, 512 + 512 + 1024 + 512 + 512, entry2Data, 0, 1024);
        assertEquals(entry2Data, actual, 512 + 512 + 1024 + 512 + 512, entry2Data.length);
        // Check eof records
        byte[] eof = new byte[2 * 1024 * 512];
        assertEquals(eof, actual, 512 + 512 + 1024 * 2 + 512 * 2, eof.length);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry tarEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(tarEntry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        byte[] expected = new byte[512 + 1024 + 512];
        byte[] actual = out.toByteArray();
        assertEquals(expected.length, tarOutputStream.size());
        // Check header
        byte[] header = new byte[512];
        System.arraycopy(expected, 0, header, 0, 512);
        assertEquals(header, actual, 0, 512);
        // Check entry header
        byte[] entry = new byte[512];
        System.arraycopy(expected, 512, entry, 0, 512);
        assertEquals(entry, actual, 512, 512);
        // Check entry data
        byte[] entryData = new byte[1024];
        System.arraycopy(expected, 512 + 512, entryData, 0, 1024);
        assertEquals(entryData, actual, 512 + 512, entryData.length);
        TarArchiveEntry testEntry = tarOutputStream.getArchiveEntry();
        assertSame(tarEntry, testEntry);
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry tarEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(tarEntry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        byte[] expected = new byte[512 + 1024 + 512];
        byte[] actual = out.toByteArray();
        assertEquals(expected.length, tarOutputStream.size());
        // Check header
        byte[] header = new byte[512];
        System.arraycopy(expected, 0, header, 0, 512);
        assertEquals(header, actual, 0, 512);
        // Check entry header
        byte[] entry = new byte[512];
        System.arraycopy(expected, 512, entry, 0, 512);
        assertEquals(entry, actual, 512, 512);
        // Check entry data
        byte[] entryData = new byte[1024];
        System.arraycopy(expected, 512 + 512, entryData, 0, 1024);
        assertEquals(entryData, actual, 512 + 512, entryData.length);
        assertEquals("Hello World!".length(), tarOutputStream.size() - 512 - 512 - 1024);
    }
}