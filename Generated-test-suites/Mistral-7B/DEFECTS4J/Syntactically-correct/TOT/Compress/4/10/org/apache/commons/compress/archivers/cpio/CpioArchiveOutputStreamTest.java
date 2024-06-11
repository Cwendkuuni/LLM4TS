import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class CpioArchiveOutputStreamTest {

    private OutputStream os;
    private CpioArchiveOutputStream out;

    @Before
    public void setUp() throws Exception {
        os = Mockito.mock(OutputStream.class);
        out = new CpioArchiveOutputStream(os, CpioConstants.FORMAT_NEW);
    }

    @Test
    public void testWriteSingleEntry() throws IOException {
        final String testFileName = "testFile.txt";
        final String testFileContent = "Test content";
        final long expectedSize = testFileContent.length();

        final CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setSize(expectedSize);
        entry.setFormat(CpioConstants.FORMAT_NEW);

        out.putArchiveEntry(entry);
        out.write(testFileContent.getBytes());
        out.closeArchiveEntry();

        Assert.assertArrayEquals(testFileContent.getBytes(), out.getBuffer());
        Assert.assertEquals(expectedSize, out.getWritten());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteInvalidFormat() throws IOException {
        final OutputStream outputStream = new FilterOutputStream(new FileOutputStream("test.cpio"));
        final CpioArchiveOutputStream out = new CpioArchiveOutputStream(outputStream, 3); // format 3 is not valid
    }

    @Test
    public void testWriteMultipleEntries() throws IOException {
        final String testFileName1 = "testFile1.txt";
        final String testFileContent1 = "Test content 1";
        final long expectedSize1 = testFileContent1.length();

        final String testFileName2 = "testFile2.txt";
        final String testFileContent2 = "Test content 2";
        final long expectedSize2 = testFileContent2.length();

        out.putArchiveEntry(new CpioArchiveEntry());
        out.write(testFileContent1.getBytes());
        out.closeArchiveEntry();

        out.putArchiveEntry(new CpioArchiveEntry(testFileName2, expectedSize2));
        out.write(testFileContent2.getBytes());
        out.closeArchiveEntry();

        out.finish();

        byte[] expectedBuffer = new byte[expectedSize1 + expectedSize2 + 128]; // 128 is a random number for buffer size
        System.arraycopy(testFileContent1.getBytes(), 0, expectedBuffer, 0, expectedSize1);
        System.arraycopy(testFileContent2.getBytes(), 0, expectedBuffer, expectedSize1, expectedSize2);

        Assert.assertArrayEquals(expectedBuffer, out.getBuffer());
        Assert.assertEquals(expectedSize1 + expectedSize2, out.getWritten());
    }

    @Test
    public void testWriteTrailer() throws IOException {
        out.putArchiveEntry(new CpioArchiveEntry(CPIO_TRAILER, 0));
        out.finish();

        ByteArrayInputStream trailerInputStream = new ByteArrayInputStream(CpioUtil.MAGIC_TRAILER);
        byte[] trailer = new byte[trailerInputStream.available()];
        trailerInputStream.read(trailer);

        Assert.assertArrayEquals(trailer, out.getBuffer());
    }

    @Test
    public void testWriteWithFilters() throws IOException {
        OutputStream filterOutputStream = new FilterOutputStream(os);
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(filterOutputStream, CpioConstants.FORMAT_NEW);

        out.putArchiveEntry(new CpioArchiveEntry());
        out.write("Test content".getBytes());
        out.closeArchiveEntry();

        out.finish();

        Assert.assertArrayEquals("Test content".getBytes(), out.getBuffer());
    }

    @Test
    public void testWriteWithInvalidEntryName() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("\0"); // null character in the name

        out.putArchiveEntry(entry);
        try {
            out.write("Test content".getBytes());
        } catch (Exception e) {
            Assert.assertEquals("Invalid CPIO entry name: \0", e.getMessage());
        }

        out.closeArchiveEntry();
    }

    @Test
    public void testWriteWithInvalidSize() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile.txt");
        entry.setSize(-1); // negative size

        out.putArchiveEntry(entry);

        try {
            out.write("Test content".getBytes());
        } catch (Exception e) {
            Assert.assertEquals("Invalid entry size: -1", e.getMessage());
        }

        out.closeArchiveEntry();
    }

    @Test
    public void testWriteWithInvalidFormat() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile.txt");
        entry.setFormat((short) 6); // invalid format

        out.putArchiveEntry(entry);

        try {
            out.write("Test content".getBytes());
        } catch (Exception e) {
            Assert.assertEquals("Invalid format: 6", e.getMessage());
        }

        out.closeArchiveEntry();
    }

    @Test
    public void testWriteWithInvalidDevice() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile.txt");
        entry.setDevice(-1); // invalid device number

        out.putArchiveEntry(entry);

        try {
            out.write("Test content".getBytes());
        } catch (Exception e) {
            Assert.assertEquals("Invalid device number: -1", e.getMessage());
        }

        out.closeArchiveEntry();
    }

    @Test
    public void testWriteWithInvalidTime() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile.txt");
        entry.setTime(-1); // invalid time

        out.putArchiveEntry(entry);

        try {
            out.write("Test content".getBytes());
        } catch (Exception e) {
            Assert.assertEquals("Invalid time: -1", e.getMessage());
        }

        out.closeArchiveEntry();
    }

    @Test
    public void testWriteWithDuplicateName() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testFile.txt");

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testFile.txt");

        out.putArchiveEntry(entry1);
        out.putArchiveEntry(entry2);

        try {
            out.write("Test content1".getBytes());
            out.write("Test content2".getBytes());
        } catch (Exception e) {
            Assert.assertEquals("Duplicate entry name: testFile.txt", e.getMessage());
        }

        out.closeArchiveEntry();
        out.closeArchiveEntry();
    }

    @Test
    public void testWriteWithInvalidChksum() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile.txt");
        entry.setFormat(CpioConstants.FORMAT_NEW_CRC);

        out.putArchiveEntry(entry);
        out.write("Test content".getBytes());

        try {
            out.finish();
        } catch (Exception e) {
            Assert.assertEquals("Invalid checksum", e.getMessage());
        }

        out.closeArchiveEntry();
    }

    @Test
    public void testWriteWithInvalidHeaderPadCount() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile.txt");
        entry.setPadCount(-1); // invalid pad count

        out.putArchiveEntry(entry);

        try {
            out.write("Test content".getBytes());
            out.finish();
        } catch (Exception e) {
            Assert.assertEquals("Invalid header pad count: -1", e.getMessage());
        }

        out.closeArchiveEntry();
    }

    @Test
    public void testWriteWithDataPadCount() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile.txt");

        out.putArchiveEntry(entry);
        out.write("Test content".getBytes());

        try {
            out.finish();
        } catch (Exception e) {
            Assert.assertEquals("Unexpected end of data", e.getMessage());
        }

        byte[] expectedPad = new byte[entry.getDataPadCount()];

        out.write(expectedPad);
        out.finish();

        Assert.assertArrayEquals(expectedPad, out.getBuffer());
    }

    @Test
    public void testWriteWithSwapHalfWord() throws IOException {
        OutputStream filterOutputStream = new FilterOutputStream(os);
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(filterOutputStream, CpioConstants.FORMAT_OLD_BINARY);

        final String testFileName = "testFile.txt";
        final String testFileContent = "Test content";
        final long expectedSize = testFileContent.length();

        final CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setSize(expectedSize);
        entry.setFormat(CpioConstants.FORMAT_OLD_BINARY);

        out.putArchiveEntry(entry);
        out.write(testFileContent.getBytes());
        out.closeArchiveEntry();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[expectedSize];
        System.arraycopy(testFileContent.getBytes(), 0, buffer, 0, expectedSize);
        CpioUtil.swapHalfWords(buffer);

        Assert.assertArrayEquals(buffer, out.getBuffer());
        Assert.assertEquals(expectedSize, out.getWritten());
    }

    @Test
    public void testWriteCpioTrailer() throws IOException {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new ByteArrayOutputStream(), CpioConstants.FORMAT_NEW);

        File testFile = new File("testFile.txt");
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFile.getName());
        entry.setSize(testFile.length());
        entry.setFormat(CpioConstants.FORMAT_NEW);

        out.putArchiveEntry(entry);

        byte[] content = new byte[(int) testFile.length()];
        testFile.getBytes(0, content, 0, (int) testFile.length());
        out.write(content);
        out.closeArchiveEntry();

        out.finish();

        byte[] expectedTrailer = CpioUtil.MAGIC_TRAILER;
        Assert.assertArrayEquals(expectedTrailer, out.getBuffer());
    }
}