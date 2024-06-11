import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        if (tarArchiveOutputStream != null) {
            tarArchiveOutputStream.close();
        }
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new File("test.tar"));
        assertNull(tarArchiveOutputStream.getArchiveEntry());
        tarArchiveOutputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullOutputStream() {
        new TarArchiveOutputStream(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetLongFileModeWithNullLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(null);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalStateException.class)
    public void testPutArchiveEntryAfterFinish() throws IOException {
        tarArchiveOutputStream.finish();
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        assertFalse(tarArchiveOutputStream.finished);
        assertEquals(entry, tarArchiveOutputStream.getArchiveEntry());
        assertEquals("test.txt", tarArchiveOutputStream.currName);
        assertTrue(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "test_long_name_" + TarConstants.NAMELEN + " test.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(entry);
        assertEquals(longFileName, tarArchiveOutputStream.currName);
        byte[] nameBytes = ArchiveUtils.toAsciiBytes(longFileName);
        assertEquals(nameBytes.length + 1, tarArchiveOutputStream.getNextEntry().getSize());
        assertTrue(tarArchiveOutputStream.getNextEntry().isDirectory());
        tarArchiveOutputStream.closeArchiveEntry();
        assertEquals(0, tarArchiveOutputStream.getNextEntry().getSize());
        assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test(expected = IllegalStateException.class)
    public void testPutArchiveEntryAfterClose() throws IOException {
        tarArchiveOutputStream.close();
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[1024]);
        tarArchiveOutputStream.closeArchiveEntry();
        assertEquals(0, tarArchiveOutputStream.currBytes);
        assertEquals(0, tarArchiveOutputStream.currSize);
        assertNull(tarArchiveOutputStream.currName);
        assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test(expected = IllegalStateException.class)
    public void testFinishBeforeClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[1024]);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        assertEquals(2 * TarConstants.BLOCK_SIZE, outputStream.size());
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "data".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
        byte[] expected = new byte[data.length + 1 + TarConstants.BLOCK_SIZE];
        System.arraycopy(data, 0, expected, 0, data.length);
        expected[data.length] = 0;
        byte[] actual = outputStream.toByteArray();
        System.arraycopy(actual, 0, expected, data.length + 1, TarConstants.BLOCK_SIZE);
        assertArrayEquals(expected, actual);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWriteHeader() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        TarHeader header = entry.getHeader();
        header.setSize(1024);
        header.setMode(0755);
        header.setMtime(System.currentTimeMillis());
        header.setName("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] expectedNameBytes = ArchiveUtils.toAsciiBytes("test.txt");
        assertEquals(expectedNameBytes.length, tarArchiveOutputStream.getNextEntry().getName().length());
        assertEquals("test.txt", tarArchiveOutputStream.getNextEntry().getName());
        assertEquals(1024, tarArchiveOutputStream.getNextEntry().getSize());
        assertEquals(0755, tarArchiveOutputStream.getNextEntry().getMode());
        assertEquals(System.currentTimeMillis(), tarArchiveOutputStream.getNextEntry().getMtime());
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWriteEOF() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[1024]);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        byte[] expectedEOF = new byte[2 * TarConstants.BLOCK_SIZE];
        for (int i = 0; i < expectedEOF.length; i++) {
            expectedEOF[i] = 0;
        }
        byte[] actualEOF = outputStream.toByteArray();
        assertArrayEquals(expectedEOF, actualEOF);
    }

    @Test
    public void testWriteEOFAfterClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[1024]);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.finish();
        byte[] expectedEOF = new byte[2 * TarConstants.BLOCK_SIZE];
        for (int i = 0; i < expectedEOF.length; i++) {
            expectedEOF[i] = 0;
        }
        byte[] actualEOF = outputStream.toByteArray();
        assertArrayEquals(expectedEOF, actualEOF);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteAfterFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.write(new byte[1024]);
    }

    @Test
    public void testWriteAfterCloseAndFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[1024]);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarConstants.BLOCK_SIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateArchiveEntryWithNullFileName() {
        tarArchiveOutputStream.createArchiveEntry(null, "test.txt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveEntryWithEmptyFileName() {
        tarArchiveOutputStream.createArchiveEntry(new File(""), "test.txt");
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
        assertEquals("test.txt", entry.getName());
        assertEquals(new File("test.txt").length(), entry.getSize());
        entry.writeEntryHeader(tarArchiveOutputStream.recordBuf);
        assertEquals(new File("test.txt").length(), tarArchiveOutputStream.currSize);
        byte[] expectedNameBytes = ArchiveUtils.toAsciiBytes("test.txt");
        assertEquals(expectedNameBytes.length, tarArchiveOutputStream.getNextEntry().getName().length());
        assertEquals("test.txt", tarArchiveOutputStream.getNextEntry().getName());
        tarArchiveOutputStream.closeArchiveEntry();
        assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
        assertFalse(tarArchiveOutputStream.finished);
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[1024]);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
        assertTrue(outputStream.size() == 2 * TarConstants.BLOCK_SIZE);
    }
}