import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * TarArchiveOutputStreamTest
 */
public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setUp() throws Exception {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new FileOutputStream("test.tar");
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        if (tarArchiveOutputStream != null) {
            tarArchiveOutputStream.finish();
            tarArchiveOutputStream.close();
            if (new File("test.tar").exists()) {
                new File("test.tar").delete();
            }
        }
        if (byteArrayOutputStream != null) {
            byteArrayOutputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
    }

    /**
     * test write single file
     * @throws IOException
     */
    @Test
    public void testWriteSingleFile() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] fileContent = "Test content".getBytes();
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] expectedTarRecord = createExpectedTarRecord(TarConstants.REGULAR_FILE_MAGIC,
                "test.txt".getBytes(), (byte) 0, (byte) (TarConstants.MAGIC_VERSION >> 8), (byte) (TarConstants.MAGIC_VERSION & 0xFF),
                (byte) 0, (byte) 0, (byte) 0, (byte) 10, (byte) 0);
        byte[] actualTarRecord = byteArrayOutputStream.toByteArray();
        assertEquals(expectedTarRecord.length, actualTarRecord.length);
        for (int i = 0; i < expectedTarRecord.length; i++) {
            assertEquals(expectedTarRecord[i], actualTarRecord[i]);
        }
        byteArrayInputStream = new ByteArrayInputStream(actualTarRecord);
        TarArchiveEntry readEntry = new TarArchiveEntry(byteArrayInputStream);
        byte[] readContent = new byte[readEntry.getSize()];
        byteArrayInputStream.read(readContent, 0, readContent.length);
        assertEquals(new String(readContent), "Test content");
    }

    /**
     * test write directory
     * @throws IOException
     */
    @Test
    public void testWriteDirectory() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test");
        entry.setSize(0);
        entry.setDirectory(true);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();
        byte[] expectedTarRecord = createExpectedTarRecord(TarConstants.DIRECTORY_MAGIC,
                "test".getBytes(), (byte) 0, (byte) (TarConstants.MAGIC_VERSION >> 8), (byte) (TarConstants.MAGIC_VERSION & 0xFF),
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        byte[] actualTarRecord = byteArrayOutputStream.toByteArray();
        assertEquals(expectedTarRecord.length, actualTarRecord.length);
        for (int i = 0; i < expectedTarRecord.length; i++) {
            assertEquals(expectedTarRecord[i], actualTarRecord[i]);
        }
    }

    /**
     * test long file name
     * @throws IOException
     */
    @Test
    public void testLongFileName() throws IOException {
        String longFileName = "a/long/file/name/with/many/parts.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        entry.setSize(10);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] fileContent = "Test content".getBytes();
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] expectedTarRecord = createExpectedTarRecord(TarConstants.GNU_LONGLINK_MAGIC,
                longFileName.getBytes(), (byte) 0, (byte) (TarConstants.MAGIC_VERSION >> 8), (byte) (TarConstants.MAGIC_VERSION & 0xFF),
                (byte) 0, (byte) 0, (byte) 0, (byte) 10, (byte) 0);
        byte[] actualTarRecord = byteArrayOutputStream.toByteArray();
        assertEquals(expectedTarRecord.length, actualTarRecord.length);
        for (int i = 0; i < expectedTarRecord.length; i++) {
            assertEquals(expectedTarRecord[i], actualTarRecord[i]);
        }
    }

    /**
     * test write EOF
     * @throws IOException
     */
    @Test
    public void testWriteEOF() throws IOException {
        tarArchiveOutputStream.finish();
        byte[] expectedTarRecord = new byte[1024 * 2]; // 2 * 512 bytes for EOF
        byte[] actualTarRecord = byteArrayOutputStream.toByteArray();
        assertEquals(expectedTarRecord.length, actualTarRecord.length);
        for (int i = 0; i < expectedTarRecord.length; i++) {
            assertEquals(expectedTarRecord[i], actualTarRecord[i]);
        }
    }

    /**
     * test write unclosed entry
     * @throws IOException
     */
    @Test
    public void testWriteUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(entry);
        assertThrows(IOException.class, () -> tarArchiveOutputStream.write("Test content".getBytes(), 0, 10));
    }

    /**
     * test write without header
     * @throws IOException
     */
    @Test
    public void testWriteWithoutHeader() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] fileContent = "Test content".getBytes();
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        assertThrows(IOException.class, () -> tarArchiveOutputStream.closeArchiveEntry());
    }

    /**
     * test setLongFileMode
     */
    @Test
    public void testSetLongFileNameMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new FileOutputStream("test.tar"));
        assertFalse(tarArchiveOutputStream.isLongFileNameSupported());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertTrue(tarArchiveOutputStream.isLongFileNameSupported());
        tarArchiveOutputStream.close();
    }

    /**
     * create expected tar record
     * @param magic
     * @param fileName
     * @param mode
     * @param uid
     * @param gid
     * @param size
     * @param mtime
     * @param mtimeNsec
     * @param checksum
     * @param linkName
     * @param flags
     * @return
     */
    private byte[] createExpectedTarRecord(byte magic, byte[] fileName,
                                          byte mode, byte uid, byte gid,
                                          long size, long mtime, long mtimeNsec,
                                          byte checksum, byte[] linkName,
                                          byte flags) {
        byte[] result = new byte[TarConstants.HEADER_SIZE + fileName.length + linkName.length];
        System.arraycopy(new byte[]{magic, 0, 0}, 0, result, 0, 3);
        System.arraycopy(fileName, 0, result, 3, fileName.length);
        result[fileName.length + 3] = (byte) (TarConstants.MAGIC_VERSION >> 8);
        result[fileName.length + 4] = (byte) (TarConstants.MAGIC_VERSION & 0xFF);
        result[fileName.length + 5] = mode;
        result[fileName.length + 6] = uid;
        result[fileName.length + 7] = gid;
        byte[] sizeBytes = new byte[8];
        longToByteArray(size, sizeBytes);
        System.arraycopy(sizeBytes, 0, result, fileName.length + 8, 8);
        byte[] mtimeBytes = new byte[8];
        longToByteArray(mtime, mtimeBytes);
        System.arraycopy(mtimeBytes, 0, result, fileName.length + 16, 8);
        longToByteArray(mtimeNsec, sizeBytes);
        System.arraycopy(sizeBytes, 0, result, fileName.length + 24, 8);
        System.arraycopy(checksum, 0, result, fileName.length + 32, 1);
        if (linkName != null) {
            System.arraycopy(linkName, 0, result, fileName.length + 33, linkName.length);
        }
        System.arraycopy(new byte[]{flags}, 0, result, fileName.length + 33 + linkName.length, 1);
        return result;
    }

    /**
     * long to byte array
     * @param value
     * @param byteArray
     */
    private void longToByteArray(long value, byte[] byteArray) {
        for (int i = 0; i < 8; i++) {
            byteArray[i] = (byte) (value >> (8 * i & 0x7));
            value <<= 8;
        }
    }
}