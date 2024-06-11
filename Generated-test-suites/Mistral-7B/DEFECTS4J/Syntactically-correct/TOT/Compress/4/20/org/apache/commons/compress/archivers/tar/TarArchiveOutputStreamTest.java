import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private static final int RECORD_SIZE = TarConstants.RECORDSIZE;
    private static final int BLOCK_SIZE = TarConstants.BLOCKSIZE;
    private OutputStream out;
    private TarArchiveOutputStream tarOs;
    private ByteArrayOutputStream baos;

    @Before
    public void setUp() throws Exception {
        baos = new ByteArrayOutputStream(RECORD_SIZE * 2);
        out = baos;
        tarOs = new TarArchiveOutputStream(out, BLOCK_SIZE, RECORD_SIZE);
        tarOs.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @After
    public void tearDown() throws Exception {
        tarOs.finish();
        tarOs.close();
    }

    @Test
    public void testPutArchiveEntryWithSmallFileName() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOs.putArchiveEntry(entry);
        tarOs.write("Test file content".getBytes());
        tarOs.closeArchiveEntry();

        byte[] expectedHeader = entry.getHeader();
        byte[] expectedContent = "Test file content".getBytes();

        assertEquals(expectedHeader.length, tarOs.getBuffer().getPosition());
        byte[] tarData = tarOs.getBuffer().toByteArray();

        int headerEnd = 512; // 512 bytes for the header
        int recordStart = headerEnd;

        // Check the header
        for (int i = 0; i < expectedHeader.length; i++) {
            assertEquals(expectedHeader[i], tarData[i + headerEnd]);
        }

        // Check the content
        for (int i = 0; i < expectedContent.length; i++) {
            assertEquals(expectedContent[i], tarData[recordStart + i]);
        }

        // Check the record size
        assertEquals(expectedHeader.length + expectedContent.length, tarOs.getRecordSize());
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "a".repeat(TarConstants.NAMELEN + 1);
        TarArchiveEntry entry = new TarArchiveEntry(new File("test.txt"), longFileName);
        tarOs.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntryWithLongLongFileName() throws IOException {
        String longFileName = "a".repeat(TarConstants.NAMELEN + 1);
        TarArchiveEntry entry = new TarArchiveEntry(new File("test.txt"), longFileName);

        if (tarOs.getLongFileMode() != TarArchiveOutputStream.LONGFILE_GNU) {
            tarOs.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        }

        tarOs.putArchiveEntry(entry);
        tarOs.write("Test file content".getBytes());
        tarOs.closeArchiveEntry();

        byte[] expectedHeader = entry.getHeader();
        byte[] expectedContent = "Test file content".getBytes();
        byte[] longLinkHeader = TarConstants.GNU_LONGLINK_MAGIC;

        assertEquals(expectedHeader.length + longLinkHeader.length + expectedContent.length, tarOs.getRecordSize());

        byte[] tarData = tarOs.getBuffer().toByteArray();

        int headerEnd = 512; // 512 bytes for the header
        int recordStart = headerEnd;

        // Check the long link header
        for (int i = 0; i < longLinkHeader.length; i++) {
            assertEquals(longLinkHeader[i], tarData[i + headerEnd]);
        }

        // Check the long link name
        for (int i = 0; i < longFileName.length(); i++) {
            assertEquals(longFileName.charAt(i), tarData[i + headerEnd + longLinkHeader.length]);
        }

        // Check the content
        for (int i = 0; i < expectedContent.length; i++) {
            assertEquals(expectedContent[i], tarData[recordStart + i]);
        }
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testDir/", ".");
        tarOs.putArchiveEntry(entry);
        tarOs.finish();
        tarOs.close();

        byte[] tarData = baos.toByteArray();

        // Check the directory entry header
        TarArchiveEntry entryFromTar = new TarArchiveEntry(tarData, 0);
        assertEquals("testDir/.", entryFromTar.getName());
        assertEquals(TarConstants.TYPEGNU_DIR, entryFromTar.getHeader().getFileType());

        // Check the EOF record
        int eofRecordIndex = baos.size() - 1024;
        for (int i = 0; i < 512; i++) {
            assertEquals(0, tarData[eofRecordIndex + i]);
        }
    }

    @Test
    public void testWriteWithSmallContent() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOs.putArchiveEntry(entry);
        tarOs.write("Test file content".getBytes());
        tarOs.closeArchiveEntry();

        byte[] expectedContent = "Test file content".getBytes();
        byte[] tarData = tarOs.getBuffer().toByteArray();

        for (int i = 0; i < expectedContent.length; i++) {
            assertEquals(expectedContent[i], tarData[recordSize(entry.getSize()) + i]);
        }
    }

    @Test
    public void testWriteWithLongContent() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOs.putArchiveEntry(entry);

        byte[] largeContent = new byte[Long.MAX_VALUE];
        tarOs.write(largeContent);

        tarOs.closeArchiveEntry();
        tarOs.finish();
        tarOs.close();

        byte[] tarData = baos.toByteArray();

        assertEquals(Long.MAX_VALUE, entry.getSize());

        // Check the header size
        int headerEnd = 512;
        int recordStart = headerEnd;

        for (int i = 0; i < TarConstants.NAMELEN; i++) {
            assertEquals(entry.getName().charAt(i), tarData[i]);
        }

        assertEquals(TarConstants.TYPEGNU_NORMAL, tarData[headerEnd + TarConstants.NAMELEN]);

        assertEquals(Long.toUnsignedLong(entry.getSize()), tarData[headerEnd + TarConstants.NAMELEN + 1] & 0xFFFFFFFFL);
        assertEquals(entry.getCrc(), tarData[headerEnd + TarConstants.NAMELEN + 5]);

        assertEquals(Long.toUnsignedLong(entry.getMtime()), tarData[headerEnd + TarConstants.NAMELEN + 9]);

        // Check the content
        for (int i = 0; i < Long.MAX_VALUE; i += BLOCK_SIZE) {
            for (int j = 0; j < Math.min(BLOCK_SIZE, Long.MAX_VALUE - i); j++) {
                assertEquals(largeContent[i + j], tarData[recordStart + j]);
            }
            recordStart += BLOCK_SIZE;
        }
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOs.putArchiveEntry(entry);
        tarOs.write("Test file content".getBytes());
        tarOs.closeArchiveEntry();

        tarOs.finish();
        tarOs.close();

        byte[] tarData = baos.toByteArray();

        // Check the EOF record
        int eofRecordIndex = baos.size() - 1024;
        for (int i = 0; i < 512; i++) {
            assertEquals(0, tarData[eofRecordIndex + i]);
        }

        // Check the EOF record size
        int eofRecordLength = 512;
        assertEquals(eofRecordLength, tarOs.getRecordSize());
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOs.putArchiveEntry(entry);
        tarOs.write("Test file content".getBytes());

        tarOs.close();
        tarOs.finish();

        byte[] tarData = baos.toByteArray();

        // Check the EOF record
        int eofRecordIndex = baos.size() - 1024;
        for (int i = 0; i < 512; i++) {
            assertEquals(0, tarData[eofRecordIndex + i]);
        }

        // Check the EOF record size
        int eofRecordLength = 512;
        assertEquals(eofRecordLength, tarOs.getRecordSize());

        tarOs.close();

        assertNull(tarOs.getBuffer());
        assertFalse(tarOs.isOpen());
    }

    private int recordSize(long size) {
        return TarConstants.RECORDSIZE + (int) Math.ceil((double) size / (double) TarConstants.BLOCKSIZE) * TarConstants.BLOCKSIZE;
    }
}