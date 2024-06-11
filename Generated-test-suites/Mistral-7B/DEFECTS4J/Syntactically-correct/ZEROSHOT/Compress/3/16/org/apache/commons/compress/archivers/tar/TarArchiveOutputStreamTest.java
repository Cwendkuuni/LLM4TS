package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.apache.commons.compress.archivers.tar.TarConstants.NAMELEN;

/**
 * Test class for TarArchiveOutputStream.
 */
public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    /**
     * Test that the constructor works correctly.
     */
    @Test
    public void testConstructor() {
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    /**
     * Test that setting long file mode works correctly.
     */
    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    /**
     * Test that finish method writes two EOF records.
     */
    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.write("Hello, World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] record1 = new byte[TarConstants.TAR_REC_SIZE];
        byte[] record2 = new byte[TarConstants.TAR_REC_SIZE];
        int bytesRead;

        // Read first record
        bytesRead = inputStream.read(record1);
        Assert.assertEquals(TarConstants.TAR_REC_SIZE, bytesRead);
        Assert.assertArrayEquals(TarConstants.TAR_MAGIC, Arrays.copyOfRange(record1, 0, TarConstants.TAR_MAGIC.length));
        Assert.assertEquals(TarConstants.TAR_REC_HEADER_SIZE, record1[TarConstants.TAR_REC_HEADER_LEN]);
        Assert.assertEquals(0, record1[TarConstants.TAR_REC_NAME_LEN]);
        Assert.assertEquals(0, record1[TarConstants.TAR_REC_FILE_SIZE]);
        Assert.assertEquals(0, record1[TarConstants.TAR_REC_MOD_TIME]);
        Assert.assertEquals(0, record1[TarConstants.TAR_REC_CHECKSUM]);
        Assert.assertEquals(TarConstants.TAR_REC_TYPE_NEW, record1[TarConstants.TAR_REC_TYPE_Flags]);
        Assert.assertEquals(0, record1[TarConstants.TAR_REC_LINK_NAME_LEN]);
        Assert.assertEquals(0, record1[TarConstants.TAR_REC_MAGIC]);

        // Read second record
        bytesRead = inputStream.read(record2);
        Assert.assertEquals(TarConstants.TAR_REC_SIZE, bytesRead);
        Assert.assertArrayEquals(Arrays.constant((byte) 0), record2);
    }

    /**
     * Test that close method writes the final record.
     */
    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.write("Hello, World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] record = new byte[TarConstants.TAR_REC_SIZE];
        int bytesRead;

        // Read record
        bytesRead = inputStream.read(record);
        Assert.assertEquals(TarConstants.TAR_REC_SIZE, bytesRead);
        Assert.assertArrayEquals(TarConstants.TAR_MAGIC, Arrays.copyOfRange(record, 0, TarConstants.TAR_MAGIC.length));
        Assert.assertEquals(TarConstants.TAR_REC_HEADER_SIZE, record[TarConstants.TAR_REC_HEADER_LEN]);
        Assert.assertEquals(0, record[TarConstants.TAR_REC_NAME_LEN]);
        Assert.assertEquals("test.txt".getBytes().length, record[TarConstants.TAR_REC_FILE_NAME_LEN]);
        Assert.assertEquals(12, record[TarConstants.TAR_REC_FILE_SIZE]);
        Assert.assertEquals(0, record[TarConstants.TAR_REC_MOD_TIME]);
        Assert.assertEquals(0, record[TarConstants.TAR_REC_CHECKSUM]);
        Assert.assertEquals(TarConstants.TAR_REC_TYPE_NEW, record[TarConstants.TAR_REC_TYPE_Flags]);
        Assert.assertEquals(0, record[TarConstants.TAR_REC_LINK_NAME_LEN]);
        Assert.assertEquals(TarConstants.TAR_REC_MAGIC, record[TarConstants.TAR_REC_MAGIC]);
    }

    /**
     * Test that writing a file with a long name works correctly.
     */
    @Test
    public void testLongFileName() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        String longFileName = "some/long/path/test.txt";
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry(longFileName));
        tarArchiveOutputStream.write("Hello, World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] record1 = new byte[TarConstants.TAR_REC_SIZE];
        byte[] record2 = new byte[TarConstants.TAR_REC_SIZE];
        byte[] longFileNameBytes = longFileName.getBytes();
        int bytesRead;

        // Read first record
        bytesRead = inputStream.read(record1);
        Assert.assertEquals(TarConstants.TAR_REC_SIZE, bytesRead);
        Assert.assertArrayEquals(TarConstants.TAR_MAGIC, Arrays.copyOfRange(record1, 0, TarConstants.TAR_MAGIC.length));
        Assert.assertEquals(TarConstants.TAR_REC_HEADER_SIZE, record1[TarConstants.TAR_REC_HEADER_LEN]);
        Assert.assertEquals(longFileNameBytes.length + 1, record1[TarConstants.TAR_REC_NAME_LEN]);
        Assert.assertEquals(0, record1[TarConstants.TAR_REC_FILE_SIZE]);
        Assert.assertEquals(0, record1[TarConstants.TAR_REC_MOD_TIME]);
        Assert.assertEquals(0, record1[TarConstants.TAR_REC_CHECKSUM]);
        Assert.assertEquals(TarConstants.TAR_REC_TYPE_NEW, record1[TarConstants.TAR_REC_TYPE_Flags]);
        Assert.assertEquals(longFileNameBytes.length, record1[TarConstants.TAR_REC_NAME_LEN + TarConstants.TAR_REC_NAME_LEN]);
        Assert.assertEquals(TarConstants.TAR_REC_MAGIC, record1[TarConstants.TAR_REC_MAGIC]);

        // Read second record
        bytesRead = inputStream.read(record2);
        Assert.assertEquals(TarConstants.TAR_REC_SIZE, bytesRead);
        Assert.assertArrayEquals(Arrays.constant((byte) 0), record2);
    }

    /**
     * Test that writing a file with a long name that is truncated works correctly.
     */
    @Test
    public void testTruncatedLongFileName() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        String longFileName = "some/long/path/test.txt";
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry(longFileName));
        tarArchiveOutputStream.write("Hello, World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] record1 = new byte[TarConstants.TAR_REC_SIZE];
        byte[] longFileNameBytes = longFileName.getBytes();
        int bytesRead;

        // Read record
        bytesRead = inputStream.read(record1);
        Assert.assertEquals(TarConstants.TAR_REC_SIZE, bytesRead);
        Assert.assertArrayEquals(TarConstants.TAR_MAGIC, Arrays.copyOfRange(record1, 0, TarConstants.TAR_MAGIC.length));
        Assert.assertEquals(TarConstants.TAR_REC_HEADER_SIZE, record1[TarConstants.TAR_REC_HEADER_LEN]);
        Assert.assertEquals(NAMELEN, record1[TarConstants.TAR_REC_NAME_LEN]);
        Assert.assertEquals("some/long/path/".getBytes().length, record1[TarConstants.TAR_REC_NAME_LEN + TarConstants.TAR_REC_NAME_LEN]);
        Assert.assertEquals(0, record1[TarConstants.TAR_REC_FILE_SIZE]);
        Assert.assertEquals(0, record1[TarConstants.TAR_REC_MOD_TIME]);
        Assert.assertEquals(0, record1[TarConstants.TAR_REC_CHECKSUM]);
        Assert.assertEquals(TarConstants.TAR_REC_TYPE_NEW, record1[TarConstants.TAR_REC_TYPE_Flags]);
        Assert.assertEquals(0, record1[TarConstants.TAR_REC_LINK_NAME_LEN]);
        Assert.assertEquals(TarConstants.TAR_REC_MAGIC, record1[TarConstants.TAR_REC_MAGIC]);
    }

    /**
     * Test that writing a file with a long name using GNU extensions works correctly.
     */
    @Test
    public void testLongFileNameWithGNUExtensions() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        String longFileName = "some/long/path/test.txt";
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry(TarConstants.GNU_LONGLINK, longFileName.getBytes()));
        tarArchiveOutputStream.write(longFileName.substring(TarConstants.NAMELEN).getBytes());
        tarArchiveOutputStream.write(0); // NUL terminator
        tarArchiveOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] record1 = new byte[TarConstants.TAR_REC_SIZE];
        byte[] record2 = new byte[TarConstants.TAR_REC_SIZE];
        byte[] longFileNameBytes = longFileName.getBytes();
        int bytesRead;

        // Read first record
        bytesRead = inputStream.read(record1);
        Assert.assertEquals(TarConstants.TAR_REC_SIZE, bytesRead);
        Assert.assertArrayEquals(TarConstants.TAR_MAGIC, Arrays.copyOfRange(record1, 0, TarConstants.TAR_MAGIC.length));
        Assert.assertEquals(TarConstants.TAR_REC_HEADER_SIZE, record1[TarConstants.TAR_REC_HEADER_LEN]);
        Assert.assertEquals(TarConstants.GNU_LONGLINK_LEN, record1[TarConstants.TAR_REC_NAME_LEN]);
        Assert.assertEquals(0, record1[TarConstants.TAR_REC_FILE_SIZE]);
        Assert.assertEquals(0, record1[TarConstants.TAR_REC_MOD_TIME]);
        Assert.assertEquals(0, record1[TarConstants.TAR_REC_CHECKSUM]);
        Assert.assertEquals(TarConstants.TAR_REC_TYPE_LINK, record1[TarConstants.TAR_REC_TYPE_Flags]);
        Assert.assertEquals(longFileNameBytes.length, record1[TarConstants.TAR_REC_NAME_LEN + TarConstants.TAR_REC_NAME_LEN]);
        Assert.assertEquals(TarConstants.TAR_REC_MAGIC, record1[TarConstants.TAR_REC_MAGIC]);

        // Read second record
        bytesRead = inputStream.read(record2);
        Assert.assertEquals(TarConstants.TAR_REC_SIZE, bytesRead);
        Assert.assertArrayEquals(Arrays.constant((byte) 0), record2);

        // Read third record - the long file name
        bytesRead = inputStream.read(record2);
        Assert.assertEquals(longFileNameBytes.length + 1, bytesRead);
        Assert.assertArrayEquals(longFileNameBytes, Arrays.copyOfRange(record2, 0, longFileNameBytes.length));
        Assert.assertEquals(0, record2[longFileNameBytes.length]);
    }
}