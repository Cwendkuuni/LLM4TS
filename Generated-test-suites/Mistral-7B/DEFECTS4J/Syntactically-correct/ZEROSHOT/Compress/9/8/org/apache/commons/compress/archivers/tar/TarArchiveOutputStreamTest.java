import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new CountingOutputStream(new ByteArrayOutputStream(1024), 1024);
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @After
    public void tearDown() throws Exception {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        File testFile = new File("testFile.txt");
        testFile.createNewFile();

        TarArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(testFile, "testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("Hello World!".getBytes(), 0, "Hello World!".length());
        tarArchiveOutputStream.closeArchiveEntry();
        testFile.delete();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(tarArchiveOutputStream.toByteArray());
        TarArchiveEntry testEntry = new TarArchiveEntry(inputStream);
        assertNotNull(testEntry);
        assertEquals("testFile.txt", testEntry.getName());
        assertEquals(10, testEntry.getSize());
        assertFalse(testEntry.isDirectory());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNegativeSize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testEntry.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("-1".getBytes(), 0, 1);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(new File("testFile.txt"), "testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("Hello World!".getBytes(), 0, "Hello World!".length());
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(tarArchiveOutputStream.toByteArray());
        TarArchiveEntry testEntry = new TarArchiveEntry(inputStream);
        assertNotNull(testEntry);
        assertEquals("testFile.txt", testEntry.getName());
        assertEquals(10, testEntry.getSize());
        assertFalse(testEntry.isDirectory());

        TarArchiveEntry eofEntry = new TarArchiveEntry(inputStream, TarConstants.GNU_STAR);
        assertEquals(".", eofEntry.getName());
        assertEquals(512, eofEntry.getSize());
        assertFalse(eofEntry.isDirectory());
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(new File("testFile.txt"), "testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("Hello World!".getBytes(), 0, "Hello World!".length());
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] bytes = tarArchiveOutputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        TarArchiveEntry testEntry = new TarArchiveEntry(inputStream);
        assertNotNull(testEntry);
        assertEquals("testFile.txt", testEntry.getName());
        assertEquals(10, testEntry.getSize());
        assertFalse(testEntry.isDirectory());

        TarArchiveEntry eofEntry = new TarArchiveEntry(inputStream, TarConstants.GNU_STAR);
        assertEquals(".", eofEntry.getName());
        assertEquals(512, eofEntry.getSize());
        assertFalse(eofEntry.isDirectory());

        tarArchiveOutputStream.close();
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream1 = new TarArchiveOutputStream(new ByteArrayOutputStream(1024), 512, 512);
        tarArchiveOutputStream1.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveEntry archiveEntry1 = tarArchiveOutputStream1.createArchiveEntry(new File("testFile1.txt"), "testFile1.txt");
        tarArchiveOutputStream1.putArchiveEntry(archiveEntry1);
        tarArchiveOutputStream1.write("Hello World!".getBytes(), 0, "Hello World!".length());
        tarArchiveOutputStream1.closeArchiveEntry();

        TarArchiveOutputStream tarArchiveOutputStream2 = new TarArchiveOutputStream(new ByteArrayOutputStream(1024), 512, 512);
        tarArchiveOutputStream2.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarArchiveEntry archiveEntry2 = tarArchiveOutputStream2.createArchiveEntry(new File("testFile2.txt"), "testFile2.txt");
        tarArchiveOutputStream2.putArchiveEntry(archiveEntry2);
        tarArchiveOutputStream2.write("Hello World!".getBytes(), 0, "Hello World!".length());
        tarArchiveOutputStream2.closeArchiveEntry();

        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(tarArchiveOutputStream1.toByteArray());
        TarArchiveEntry testEntry1 = new TarArchiveEntry(inputStream1);
        assertEquals(10, testEntry1.getSize());
        assertFalse(testEntry1.isDirectory());

        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(tarArchiveOutputStream2.toByteArray());
        TarArchiveEntry testEntry2 = new TarArchiveEntry(inputStream2);
        assertEquals(28, testEntry2.getSize());
        assertTrue(testEntry2.isDirectory());
    }
}