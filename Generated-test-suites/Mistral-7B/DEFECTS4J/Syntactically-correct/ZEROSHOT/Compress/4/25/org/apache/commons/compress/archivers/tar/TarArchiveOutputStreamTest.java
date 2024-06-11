import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for TarArchiveOutputStream.
 */
public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    /**
     * Test constructor with default record size.
     */
    @Test
    public void testConstructorWithDefaultRecordSize() {
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNull(tarOutputStream.getBuffer());
    }

    /**
     * Test constructor with specified record size.
     */
    @Test
    public void testConstructorWithSpecifiedRecordSize() {
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        assertEquals(512, tarOutputStream.getBuffer().getRecordSize());
    }

    /**
     * Test constructor with specified block size and record size.
     */
    @Test
    public void testConstructorWithSpecifiedBlockSizeAndRecordSize() {
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        assertEquals(512, tarOutputStream.getBuffer().getBlockSize());
        assertEquals(1024, tarOutputStream.getBuffer().getRecordSize());
    }

    /**
     * Test setting long file mode.
     */
    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    /**
     * Test writing a single byte.
     */
    @Test
    public void testWriteSingleByte() throws IOException {
        tarOutputStream.putNextEntry(new TarArchiveEntry("test.txt", "A"));
        tarOutputStream.write(55);
        tarOutputStream.closeArchiveEntry();

        byte[] bytes = outputStream.toByteArray();
        assertEquals(71, bytes[0]); // headers have standard ASCII codes
        assertEquals(55, bytes[77]); // our byte
    }

    /**
     * Test writing multiple bytes.
     */
    @Test
    public void testWriteMultipleBytes() throws IOException {
        tarOutputStream.putNextEntry(new TarArchiveEntry("test.txt", "A"));
        tarOutputStream.write(new byte[]{(byte) 0xAA, (byte) 0xBB, (byte) 0xCC});
        tarOutputStream.closeArchiveEntry();

        byte[] bytes = outputStream.toByteArray();
        assertEquals(71, bytes[0]); // headers have standard ASCII codes
        assertEquals((byte) 0xAA, bytes[78]); // first byte
        assertEquals((byte) 0xBB, bytes[79]); // second byte
        assertEquals((byte) 0xCC, bytes[80]); // third byte
    }

    /**
     * Test writing an empty file with length 0.
     */
    @Test
    public void testWriteEmptyFileWithLengthZero() throws IOException {
        tarOutputStream.putNextEntry(new TarArchiveEntry("test.txt", ""));
        tarOutputStream.closeArchiveEntry();

        byte[] bytes = outputStream.toByteArray();
        assertEquals(71 * 2, bytes.length); // headers have standard ASCII codes
    }

    /**
     * Test writing a file with a name longer than TAR_NAMELEN.
     */
    @Test(expected = RuntimeException.class)
    public void testWriteFileWithLongName() throws IOException {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        String longFileName = "a" + new String(new char[TarConstants.NAMELEN + 1])[0];

        tarOutputStream.putNextEntry(new TarArchiveEntry(longFileName, ""));
        tarOutputStream.write(new byte[0]);
        tarOutputStream.closeArchiveEntry();
    }

    /**
     * Test writing a directory.
     */
    @Test
    public void testWriteDirectory() throws IOException {
        tarOutputStream.putNextEntry(new TarArchiveEntry("test.dir/", ""));
        tarOutputStream.putNextEntry(new TarArchiveEntry("test.dir/test.txt", ""));
        tarOutputStream.closeArchiveEntry();

        byte[] bytes = outputStream.toByteArray();

        // The directory header
        assertEquals(512, bytes.length - 512 * 2); // two EOF headers
        assertEquals(TarConstants.MAGIC, bytes[0]);
        assert (bytes[1] == (byte) '0' || bytes[1] == (byte) '1'); // Version
        assertEquals(TarConstants.FLAGS_PAX_HEADER, bytes[2]);
        assertEquals(TarConstants.TYPEFLAG_DIR, bytes[3]);
        assertEquals(TarConstants.UNIX_USER_ID, bytes[11]);
        assertEquals(TarConstants.UNIX_GROUP_ID, bytes[12]);

        // The file header
        byte[] fileBytes = new byte[512];
        System.arraycopy(bytes, 512, fileBytes, 0, fileBytes.length);

        assertEquals("test.txt", new String(fileBytes, 100, 10));
        assertEquals(TarConstants.TYPEFLAG_REG, fileBytes[13]);
        assertEquals(TarConstants.UNIX_USER_ID, fileBytes[15]);
        assertEquals(TarConstants.UNIX_GROUP_ID, fileBytes[16]);
        assertEquals(0, fileBytes[200]); // size is 0
        assertEquals(0, fileBytes[202]); // mod time is 0
    }

    /**
     * Test putting multiple entries without closing them.
     */
    @Test(expected = IOException.class)
    public void testPutMultipleEntriesWithoutClosing() throws IOException {
        tarOutputStream.putNextEntry(new TarArchiveEntry("test1.txt", ""));
        tarOutputStream.putNextEntry(new TarArchiveEntry("test2.txt", ""));
        tarOutputStream.finish(); // Finish writes the EOF headers

        // This should throw an IOException because there are still unclosed entries
        tarOutputStream.close();
    }

    /**
     * Test writing multiple entries with closing each one.
     */
    @Test
    public void testWriteMultipleEntriesWithClosing() throws IOException {
        TarArchiveEntry test1 = new TarArchiveEntry("test1.txt", "");
        TarArchiveEntry test2 = new TarArchiveEntry("test2.txt", "");

        tarOutputStream.putNextEntry(test1);
        tarOutputStream.write("Test entry 1".getBytes());
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.putNextEntry(test2);
        tarOutputStream.write("Test entry 2".getBytes());
        tarOutputStream.closeArchiveEntry();

        byte[] bytes = outputStream.toByteArray();

        // The directory header
        assertEquals(512, bytes.length - 512 * 2); // two EOF headers

        // The first entry
        byte[] entry1Bytes = new byte[512];
        System.arraycopy(bytes, 512, entry1Bytes, 0, entry1Bytes.length);

        assertEquals("test1.txt", new String(entry1Bytes, 100, 10));
        assertEquals(TarConstants.TYPEFLAG_REG, entry1Bytes[13]);
        assertEquals(TarConstants.UNIX_USER_ID, entry1Bytes[15]);
        assertEquals(TarConstants.UNIX_GROUP_ID, entry1Bytes[16]);
        assertEquals(13, entry1Bytes[144]); // size is 13
        assertEquals(0, entry1Bytes[200]); // mod time is 0

        String entry1Content = new String(bytes, 512 + 13, 13);
        assertEquals("Test entry 1", entry1Content);

        // The second entry
        byte[] entry2Bytes = new byte[512];
        System.arraycopy(bytes, 512 + 13 + 13 + 512, entry2Bytes, 0, entry2Bytes.length);

        assertEquals("test2.txt", new String(entry2Bytes, 100, 10));
        assertEquals(TarConstants.TYPEFLAG_REG, entry2Bytes[13]);
        assertEquals(TarConstants.UNIX_USER_ID, entry2Bytes[15]);
        assertEquals(TarConstants.UNIX_GROUP_ID, entry2Bytes[16]);
        assertEquals(13, entry2Bytes[144]); // size is 13
        assertEquals(0, entry2Bytes[200]); // mod time is 0

        String entry2Content = new String(bytes, 512 + 13 + 13 + 512 + 13, 13);
        assertEquals("Test entry 2", entry2Content);
    }

    /**
     * Test writing an empty file with a size of 0.
     */
    @Test
    public void testWriteEmptyFileWithSizeZero() throws IOException {
        tarOutputStream.putNextEntry(new TarArchiveEntry("test.txt", ""));
        tarOutputStream.closeArchiveEntry();

        byte[] bytes = outputStream.toByteArray();

        // The directory header
        assertEquals(512, bytes.length - 512 * 2); // two EOF headers

        // The file header
        byte[] fileBytes = new byte[512];
        System.arraycopy(bytes, 512, fileBytes, 0, fileBytes.length);

        assertEquals("test.txt", new String(fileBytes, 100, 10));
        assertEquals(TarConstants.TYPEFLAG_REG, fileBytes[13]);
        assertEquals(TarConstants.UNIX_USER_ID, fileBytes[15]);
        assertEquals(TarConstants.UNIX_GROUP_ID, fileBytes[16]);
        assertEquals(0, fileBytes[144]); // size is 0
        assertEquals(0, fileBytes[200]); // mod time is 0
    }
}