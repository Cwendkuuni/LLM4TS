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
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;

/**
 * Tests for {@link TarArchiveOutputStream} class.
 */
public class TarArchiveOutputStreamTest {

    @Test
    public void testEmptyTarFile() throws IOException {
        final int blockSize = 1024;
        final int recordSize = 512;
        OutputStream os = new ByteArrayOutputStream(blockSize * 3);

        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(os, blockSize, recordSize);

        tarOutputStream.finish();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream in = new TarInputStream(is);

        assertFalse(in.hasNextEntry());
        in.close();
    }

    @Test
    public void testPutDirectoryEntry() throws IOException {
        final int blockSize = 1024;
        final int recordSize = 512;
        OutputStream os = new ByteArrayOutputStream(blockSize * 3);

        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(os, blockSize, recordSize);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        File testDir = new File("testDir");
        testDir.mkdirs();

        ArchiveEntry entry = new TarArchiveEntry("testDir/", testDir.getAbsolutePath());

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream in = new TarInputStream(is);

        TarArchiveEntry tarEntry = (TarArchiveEntry) in.getNextEntry();
        assertEquals("testDir/", tarEntry.getName());
        assertEquals(TarEntry.TYPE_DIR, tarEntry.getFileType());
        tarEntry.close();

        assertTrue(in.hasNextEntry());
        TarArchiveEntry eofEntry = (TarArchiveEntry) in.getNextEntry();
        assertNull(eofEntry);
        in.close();
    }

    @Test
    public void testPutFileEntryWithLongFileName() throws IOException {
        final int blockSize = 1024;
        final int recordSize = 512;
        OutputStream os = new ByteArrayOutputStream(blockSize * 3);

        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(os, blockSize, recordSize);

        File file = new File("testLongFileName.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, "testLongFileName.txt");

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream in = new TarInputStream(is);

        TarArchiveEntry tarEntry = (TarArchiveEntry) in.getNextEntry();
        assertEquals("testLongFileName.txt", tarEntry.getName());
        tarEntry.close();

        assertTrue(in.hasNextEntry());
        TarArchiveEntry eofEntry = (TarArchiveEntry) in.getNextEntry();
        assertNull(eofEntry);
        in.close();
    }

    @Test
    public void testPutFileEntryWithWrongLongFileName() throws IOException {
        final int blockSize = 1024;
        final int recordSize = 512;
        OutputStream os = new ByteArrayOutputStream(blockSize * 3);

        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(os, blockSize, recordSize);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        File file = new File("testFileName.txt");
        String longFileName = "testFileName.txt".substring(0, TarConstants.NAMELEN)
                + "X".repeat(TarConstants.NAMELEN - "testFileName.txt".length());
        TarArchiveEntry entry = new TarArchiveEntry(file, longFileName);

        assertThrows(RuntimeException.class, () -> tarOutputStream.putArchiveEntry(entry));
    }

    @Test
    public void testPutFileEntryWithTruncatedLongFileName() throws IOException {
        final int blockSize = 1024;
        final int recordSize = 512;
        OutputStream os = new ByteArrayOutputStream(blockSize * 3);

        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(os, blockSize, recordSize);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        File file = new File("testFileName.txt");
        String longFileName = "testFileName.txt".substring(0, TarConstants.NAMELEN)
                + "X".repeat(TarConstants.NAMELEN - "testFileName.txt".length());
        TarArchiveEntry entry = new TarArchiveEntry(file, longFileName);

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream in = new TarInputStream(is);

        TarArchiveEntry tarEntry = (TarArchiveEntry) in.getNextEntry();
        assertEquals("testFileName.txt", tarEntry.getName());
        tarEntry.close();

        assertTrue(in.hasNextEntry());
        TarArchiveEntry eofEntry = (TarArchiveEntry) in.getNextEntry();
        assertNull(eofEntry);
        in.close();
    }

    @Test
    public void testPutFileEntryWithLongLinkName() throws IOException {
        final int blockSize = 1024;
        final int recordSize = 512;
        OutputStream os = new ByteArrayOutputStream(blockSize * 3);

        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(os, blockSize, recordSize);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        File file = new File("testLongFileName.txt");
        String longLinkName = "longLinkName".substring(0, TarConstants.NAMELEN)
                + "X".repeat(TarConstants.NAMELEN - "longLinkName".length());

        TarArchiveEntry entry = new TarArchiveEntry(file, TarConstants.GNU_LONGLINK, longLinkName);
        entry.setSize(longLinkName.length() + 1);

        tarOutputStream.putArchiveEntry(entry);
        writeStringToByteArray(entry.getName(), os);
        writeByteToByteArray(0, os); // NUL terminator
        tarOutputStream.finish();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream in = new TarInputStream(is);

        TarArchiveEntry tarEntry = (TarArchiveEntry) in.getNextEntry();
        assertEquals(TarConstants.GNU_LONGLINK, tarEntry.getFileType());
        assertEquals(longLinkName, tarEntry.getName());
        tarEntry.close();

        assertTrue(in.hasNextEntry());
        TarArchiveEntry eofEntry = (TarArchiveEntry) in.getNextEntry();
        assertNull(eofEntry);
        in.close();
    }

    @Test
    public void testPutFileEntryWithWrongLongLinkName() throws IOException {
        final int blockSize = 1024;
        final int recordSize = 512;
        OutputStream os = new ByteArrayOutputStream(blockSize * 3);

        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(os, blockSize, recordSize);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        File file = new File("testFileName.txt");
        String longLinkName = "testFileName.txt".substring(0, TarConstants.NAMELEN)
                + "X".repeat(TarConstants.NAMELEN - "testFileName.txt".length());

        TarArchiveEntry entry = new TarArchiveEntry(file, TarConstants.GNU_LONGLINK, longLinkName);
        entry.setSize(longLinkName.length() + 1);

        assertThrows(RuntimeException.class, () -> tarOutputStream.putArchiveEntry(entry));
    }

    @Test
    public void testPutFileEntryWithWrongLongLinkNameSize() throws IOException {
        final int blockSize = 1024;
        final int recordSize = 512;
        OutputStream os = new ByteArrayOutputStream(blockSize * 3);

        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(os, blockSize, recordSize);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        File file = new File("testFileName.txt");
        String longLinkName = "testFileName.txt".substring(0, TarConstants.NAMELEN)
                + "X".repeat(TarConstants.NAMELEN - "testFileName.txt".length());

        TarArchiveEntry entry = new TarArchiveEntry(file, TarConstants.GNU_LONGLINK, longLinkName);
        entry.setSize(longLinkName.length());

        assertThrows(RuntimeException.class, () -> tarOutputStream.putArchiveEntry(entry));
    }

    @Test
    public void testPutFileEntryWithWrongLongLinkNameLength() throws IOException {
        final int blockSize = 1024;
        final int recordSize = 512;
        OutputStream os = new ByteArrayOutputStream(blockSize * 3);

        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(os, blockSize, recordSize);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        File file = new File("testFileName.txt");
        String longLinkName = "testFileName.txt".substring(0, TarConstants.NAMELEN)
                + "X".repeat(TarConstants.NAMELEN - "testFileName.txt".length() + 1);

        TarArchiveEntry entry = new TarArchiveEntry(file, TarConstants.GNU_LONGLINK, longLinkName);
        entry.setSize(longLinkName.length());

        assertThrows(RuntimeException.class, () -> tarOutputStream.putArchiveEntry(entry));
    }

    @Test
    public void testWriteToArchiveEntry() throws IOException {
        final int blockSize = 1024;
        final int recordSize = 512;
        OutputStream os = new ByteArrayOutputStream(blockSize * 3);

        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(os, blockSize, recordSize);

        File file = new File("testData.txt");
        String data = "testData";
        byte[] dataBytes = ArchiveUtils.toByteArray(data);

        File testFile = new File("testFileName.txt");
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFileName.txt");
        entry.setSize(dataBytes.length);

        tarOutputStream.putArchiveEntry(entry);
        write(dataBytes, 0, dataBytes.length, tarOutputStream);
        tarOutputStream.finish();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream in = new TarInputStream(is);

        TarArchiveEntry tarEntry = (TarArchiveEntry) in.getNextEntry();
        byte[] bytes = new byte[dataBytes.length];
        int index = 0;
        while (in.read(bytes, index, bytes.length - index) != -1) {
            index += in.read(bytes, index, bytes.length - index);
        }

        assertEquals(data, new String(bytes, "UTF-8"));
        tarEntry.close();

        assertTrue(in.hasNextEntry());
        TarArchiveEntry eofEntry = (TarArchiveEntry) in.getNextEntry();
        assertNull(eofEntry);
        in.close();
    }

    @Test
    public void testWriteToArchiveEntryWithNoData() throws IOException {
        final int blockSize = 1024;
        final int recordSize = 512;
        OutputStream os = new ByteArrayOutputStream(blockSize * 3);

        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(os, blockSize, recordSize);

        File file = new File("testFileName.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, "testFileName.txt");
        entry.setSize(0);

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream in = new TarInputStream(is);

        TarArchiveEntry tarEntry = (TarArchiveEntry) in.getNextEntry();
        byte[] bytes = new byte[0];
        int index = 0;
        while (in.read(bytes, index, bytes.length - index) != -1) {
            index += in.read(bytes, index, bytes.length - index);
        }

        assertEquals(TarConstants.MAGIC_TAR_HEADER, bytes[0], 0);
        tarEntry.close();

        assertTrue(in.hasNextEntry());
        TarArchiveEntry eofEntry = (TarArchiveEntry) in.getNextEntry();
        assertNull(eofEntry);
        in.close();
    }

    private void writeStringToByteArray(String str, OutputStream os) throws IOException {
        byte[] bytes = ArchiveUtils.toByteArray(str);
        os.write(bytes, 0, bytes.length);
    }

    private void writeByteToByteArray(byte b, OutputStream os) throws IOException {
        os.write(b);
    }

    private void write(byte[] buffer, int offset, int length, OutputStream os)
            throws IOException {
        os.write(buffer, offset, length);
    }
}