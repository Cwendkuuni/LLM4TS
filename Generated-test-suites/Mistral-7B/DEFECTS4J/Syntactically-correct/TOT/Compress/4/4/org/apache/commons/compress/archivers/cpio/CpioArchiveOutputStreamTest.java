import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Test cases for CpioArchiveOutputStream class.
 */
public class CpioArchiveOutputStreamTest {

    private ByteArrayOutputStream out;
    private CpioArchiveOutputStream cpioStream;

    @Before
    public void setUp() throws IOException {
        this.out = new ByteArrayOutputStream();
        this.cpioStream = new CpioArchiveOutputStream(this.out, CpioConstants.FORMAT_NEW);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // Create test file entry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG); // regular file
        entry.setTime(System.currentTimeMillis());

        // Write test file to CPIO stream
        cpioStream.putArchiveEntry(entry);
        String contents = "Hello World!";
        cpioStream.write(contents.getBytes(StandardCharsets.UTF_8));
        cpioStream.closeArchiveEntry();

        // Validate CPIO stream contents
        byte[] cpioBytes = this.out.toByteArray();
        Map<String, CpioArchiveEntry> entries = new HashMap<>();
        cpioStream = new CpioArchiveOutputStream(new FileOutputStream(new File("test.cpio")), CpioConstants.FORMAT_NEW);
        ArchiveEntry testEntry = cpioStream.createArchiveEntry(new File("testfile"), "testfile");
        cpioStream.putArchiveEntry(testEntry);
        cpioStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        cpioStream.closeArchiveEntry();
        cpioStream.close();

        cpioStream = new CpioArchiveOutputStream(new FileInputStream("test.cpio"), CpioConstants.FORMAT_NEW);
        CpioArchiveEntry readEntry = (CpioArchiveEntry) cpioStream.getNextEntry();
        Assert.assertEquals(readEntry.getName(), "testfile");
        Assert.assertEquals(readEntry.getSize(), 10);
        Assert.assertArrayEquals(readEntry.getData(), contents.getBytes(StandardCharsets.UTF_8));
        cpioStream.close();

        File testFile = new File("testfile");
        Assert.assertTrue(testFile.exists());
        testFile.delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        this.cpioStream = new CpioArchiveOutputStream(this.out, (short) 0xFF);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateEntry() throws IOException {
        // Create test file entry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG); // regular file
        entry.setTime(System.currentTimeMillis());

        // Write test file to CPIO stream
        cpioStream.putArchiveEntry(entry);
        cpioStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        cpioStream.closeArchiveEntry();

        // Try to write same entry again
        cpioStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidEntryName() throws IOException {
        // Create test file entry with invalid name
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("\0");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG); // regular file
        entry.setTime(System.currentTimeMillis());

        // Write test file to CPIO stream
        cpioStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testWriteBeyondFileSizeLimit() throws IOException {
        // Create test file entry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG); // regular file
        entry.setTime(System.currentTimeMillis());

        // Write test file to CPIO stream
        cpioStream.putArchiveEntry(entry);
        String contents = "Hello World!";
        int length = contents.length();
        cpioStream.write(contents.getBytes(StandardCharsets.UTF_8));
        cpioStream.write(new byte[length + 10]); // write beyond file size limit
        cpioStream.closeArchiveEntry();
    }

    @Test
    public void testWriteWithSwappedEndian() throws IOException {
        // Create test file entry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG); // regular file
        entry.setTime(System.currentTimeMillis());

        // Write test file to CPIO stream with swapped endian
        cpioStream = new CpioArchiveOutputStream(this.out, CpioConstants.FORMAT_OLD_BINARY);
        cpioStream.putArchiveEntry(entry);
        String contents = "Hello World!";
        cpioStream.write(contents.getBytes(StandardCharsets.UTF_8));
        cpioStream.closeArchiveEntry();

        // Read test file from CPIO stream with swapped endian
        byte[] cpioBytes = this.out.toByteArray();
        cpioStream = new CpioArchiveOutputStream(new ByteArrayInputStream(cpioBytes), CpioConstants.FORMAT_OLD_BINARY, true);
        CpioArchiveEntry readEntry = (CpioArchiveEntry) cpioStream.getNextEntry();
        Assert.assertEquals(readEntry.getName(), "testfile");
        Assert.assertEquals(readEntry.getSize(), 10);

        byte[] readData = new byte[readEntry.getSize()];
        int readLength = cpioStream.read(readData, 0, readData.length);
        Assert.assertEquals(readLength, readData.length);
        String readContents = new String(readData, StandardCharsets.UTF_8);
        Assert.assertEquals(readContents, new String(contents.getBytes(StandardCharsets.UTF_8), CpioUtil.BYTE_ORDER_BIG_ENDIAN));

        cpioStream.close();
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidEndian() throws IOException {
        // Create test file entry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG); // regular file
        entry.setTime(System.currentTimeMillis());

        // Write test file to CPIO stream with invalid endian
        cpioStream = new CpioArchiveOutputStream(this.out, CpioConstants.FORMAT_OLD_BINARY);
        cpioStream.putArchiveEntry(entry);
        String contents = "Hello World!";
        cpioStream.write(contents.getBytes(StandardCharsets.UTF_8));
        cpioStream.closeArchiveEntry();

        // Read test file from CPIO stream with valid endian
        byte[] cpioBytes = this.out.toByteArray();
        cpioStream = new CpioArchiveOutputStream(new ByteArrayInputStream(cpioBytes), CpioConstants.FORMAT_OLD_BINARY, false);
        CpioArchiveEntry readEntry = (CpioArchiveEntry) cpioStream.getNextEntry();
        Assert.assertEquals(readEntry.getName(), "testfile");
        Assert.assertEquals(readEntry.getSize(), 10);

        // Try to read test file from CPIO stream with invalid endian
        byte[] readData = new byte[readEntry.getSize()];
        int readLength = cpioStream.read(readData, 0, readData.length);
        Assert.assertEquals(readLength, -1);
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidFormat() throws IOException {
        // Create test file entry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG); // regular file
        entry.setTime(System.currentTimeMillis());

        // Write test file to CPIO stream with invalid format
        cpioStream = new CpioArchiveOutputStream(this.out, (short) 0xFF);
        cpioStream.putArchiveEntry(entry);
        String contents = "Hello World!";
        cpioStream.write(contents.getBytes(StandardCharsets.UTF_8));
        cpioStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullName() throws IOException {
        // Create test file entry with null name
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(null);
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG); // regular file
        entry.setTime(System.currentTimeMillis());

        // Write test file to CPIO stream
        cpioStream.putArchiveEntry(entry);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithInvalidEntry() throws IOException {
        // Create test file entry with invalid class
        Object entry = new Object();

        // Try to write test file to CPIO stream
        cpioStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        // Try to write test file to CPIO stream with null entry
        cpioStream.putArchiveEntry(null);
    }

    @Test
    public void testFinish() throws IOException {
        // Create test file entry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG); // regular file
        entry.setTime(System.currentTimeMillis());

        // Write test file to CPIO stream
        cpioStream.putArchiveEntry(entry);
        String contents = "Hello World!";
        cpioStream.write(contents.getBytes(StandardCharsets.UTF_8));
        cpioStream.finish();

        byte[] cpioBytes = this.out.toByteArray();
        cpioStream = new CpioArchiveOutputStream(new ByteArrayInputStream(cpioBytes), CpioConstants.FORMAT_NEW);
        CpioArchiveEntry readEntry = (CpioArchiveEntry) cpioStream.getNextEntry();
        Assert.assertEquals(readEntry.getName(), "testfile");
        Assert.assertEquals(readEntry.getSize(), 10);

        byte[] readData = new byte[readEntry.getSize()];
        int readLength = cpioStream.read(readData, 0, readData.length);
        Assert.assertEquals(readLength, readData.length);
        String readContents = new String(readData, StandardCharsets.UTF_8);
        Assert.assertEquals(readContents, contents);

        cpioStream.close();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        // Create test file entry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG); // regular file
        entry.setTime(System.currentTimeMillis());

        // Write test file to CPIO stream
        cpioStream.putArchiveEntry(entry);

        // Try to finish without closing the entry
        cpioStream.finish();
    }
}