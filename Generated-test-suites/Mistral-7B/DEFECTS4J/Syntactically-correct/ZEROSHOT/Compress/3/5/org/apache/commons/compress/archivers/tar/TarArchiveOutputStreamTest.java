package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;

/**
 * TarArchiveOutputStreamTest class.
 */
public class TarArchiveOutputStreamTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        assertNull(tarArchiveOutputStream.getRecordSize());
        assertEquals(TarConstants.DEFAULT_BLKSIZE, tarArchiveOutputStream.getBufferSize());
        assertEquals(TarConstants.DEFAULT_BLKSIZE, tarArchiveOutputStream.getBlockSize());
        assertEquals(TarConstants.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
        assertFalse(tarArchiveOutputStream.isClosed());
    }

    /**
     * Test setLongFileMode method.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testSetLongFileMode() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    /**
     * Test finish method.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testFinish() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);

        tarArchiveOutputStream.finish();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream archiveInputStream = new TarFile(inputStream);
        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();

        assertTrue(archiveEntry instanceof TarHeader);
        assertEquals(TarConstants.MAGIC, archiveEntry.getMagic());
        assertEquals(TarConstants.VERSION, archiveEntry.getVersion());
        assertNull(archiveEntry.getName());
        assertNull(archiveEntry.getSize());
        assertNull(archiveEntry.getTimeCreated());
        assertNull(archiveEntry.getComment());
        archiveInputStream.close();
    }

    /**
     * Test close method.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testClose() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);

        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream archiveInputStream = new TarFile(inputStream);
        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();

        assertTrue(archiveEntry instanceof TarHeader);
        assertEquals(TarConstants.MAGIC, archiveEntry.getMagic());
        assertEquals(TarConstants.VERSION, archiveEntry.getVersion());
        assertNull(archiveEntry.getName());
        assertNull(archiveEntry.getSize());
        assertNull(archiveEntry.getTimeCreated());
        assertNull(archiveEntry.getComment());
        archiveInputStream.close();
    }

    /**
     * Test getRecordSize method.
     */
    @org.junit.Test
    public void testGetRecordSize() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        assertEquals(tarArchiveOutputStream.getRecordSize(),
                TarArchiveOutputStream.DEFAULT_RCDSIZE);
    }

    /**
     * Test putArchiveEntry method.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);

        File file = new File("test.txt");
        File file2 = new File("test2.txt");
        String name = "test.txt";
        String name2 = "test2.txt";

        // Test putArchiveEntry with a file
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) tarArchiveOutputStream.putArchiveEntry(new File("test.txt"));
        assertTrue(tarArchiveEntry instanceof TarArchiveEntry);
        assertEquals(name, tarArchiveEntry.getName());
        assertEquals(file.length(), tarArchiveEntry.getSize());
        assertNull(tarArchiveEntry.getTimeCreated());
        assertNull(tarArchiveEntry.getComment());

        // Test putArchiveEntry with a null file
        assertThrows(NullPointerException.class, () -> {
            tarArchiveOutputStream.putArchiveEntry(null);
        });

        // Test putArchiveEntry with a file name longer than NAMELEN
        tarArchiveEntry = (TarArchiveEntry) tarArchiveOutputStream.putArchiveEntry(new File("test" + new String(new char[TarConstants.NAMELEN + 1]) + ".txt"));
        assertTrue(tarArchiveEntry instanceof TarArchiveEntry);
        assertEquals(name.substring(0, TarConstants.NAMELEN), tarArchiveEntry.getName());
        assertEquals(file.length(), tarArchiveEntry.getSize());
        assertNull(tarArchiveEntry.getTimeCreated());
        assertNull(tarArchiveEntry.getComment());

        // Test putArchiveEntry with a String name
        tarArchiveEntry = (TarArchiveEntry) tarArchiveOutputStream.putArchiveEntry(name2, file2);
        assertTrue(tarArchiveEntry instanceof TarArchiveEntry);
        assertEquals(name2, tarArchiveEntry.getName());
        assertEquals(file2.length(), tarArchiveEntry.getSize());
        assertNull(tarArchiveEntry.getTimeCreated());
        assertNull(tarArchiveEntry.getComment());
    }

    /**
     * Test closeArchiveEntry method.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);

        File file = new File("test.txt");
        String name = "test.txt";

        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) tarArchiveOutputStream.putArchiveEntry(file);

        byte[] fileContent = new byte[file.length()];
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream archiveInputStream = new TarFile(inputStream);
        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        assertTrue(archiveEntry instanceof TarHeader);
        assertEquals(TarConstants.MAGIC, archiveEntry.getMagic());
        assertEquals(TarConstants.VERSION, archiveEntry.getVersion());
        assertEquals(name, archiveEntry.getName());
        assertEquals(file.length(), archiveEntry.getSize());
        assertNull(archiveEntry.getTimeCreated());
        assertNull(archiveEntry.getComment());
    }

    /**
     * Test write method.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testWrite() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);

        File file = new File("test.txt");
        String name = "test.txt";

        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) tarArchiveOutputStream.putArchiveEntry(file);

        byte[] fileContent = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream archiveInputStream = new TarFile(inputStream);
        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        assertTrue(archiveEntry instanceof TarHeader);
        assertEquals(TarConstants.MAGIC, archiveEntry.getMagic());
        assertEquals(TarConstants.VERSION, archiveEntry.getVersion());
        assertEquals(name, archiveEntry.getName());
        assertEquals(file.length(), archiveEntry.getSize());
        assertNull(archiveEntry.getTimeCreated());
        assertNull(archiveEntry.getComment());

        byte[] expected = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        byte[] actual = new byte[fileContent.length];
        inputStream.read(actual);
        assertEquals(expected, actual);

        archiveInputStream.close();
    }
}