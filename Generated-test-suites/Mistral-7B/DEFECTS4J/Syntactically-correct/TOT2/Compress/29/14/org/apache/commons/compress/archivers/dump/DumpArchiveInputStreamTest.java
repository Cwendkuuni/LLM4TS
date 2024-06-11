import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveConstants;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.DumpArchiveUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.apache.commons.compress.archivers.dump.DumpArchiveConstants.SEGMENT_TYPE;

public class DumpArchiveInputStreamTest {

    private static final String TEST_FILE = "test.dump";
    private static final byte[] TEST_FILE_DATA = {(byte) 0x62, (byte) 0x74, (byte) 0x61, (byte) 0x73, (byte) 0x73, (byte) 0x20, (byte) 0x74, (byte) 0x61, (byte) 0x74,
            (byte) 0x61, (byte) 0x20, (byte) 0x63, (byte) 0x61, (byte) 0x6E, (byte) 0x6E, (byte) 0x6F, (byte) 0x74, (byte) 0x20, (byte) 0x65, (byte) 0x6E, (byte) 0x63,
            (byte) 0x6F, (byte) 0x64, (byte) 0x65, (byte) 0x20, (byte) 0x64, (byte) 0x65, (byte) 0x76, (byte) 0x65, (byte) 0x20, (byte) 0x63, (byte) 0x72, (byte) 0x65,
            (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x20, (byte) 0x62, (byte) 0x65, (byte) 0x66, (byte) 0x6F, (byte) 0x72, (byte) 0x20, (byte) 0x65, (byte) 0x6E,
            (byte) 0x63, (byte) 0x6F, (byte) 0x64, (byte) 0x65, (byte) 0x20, (byte) 0x63, (byte) 0x61, (byte) 0x6D, (byte) 0x65, (byte) 0x20, (byte) 0x73, (byte) 0x74,
            (byte) 0x72, (byte) 0x69, (byte) 0x67, (byte) 0x65, (byte) 0x20, (byte) 0x73, (byte) 0x65, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x20, (byte) 0x73,
            (byte) 0x61, (byte) 0x6D, (byte) 0x65, (byte) 0x20, (byte) 0x68, (byte) 0x72, (byte) 0x65, (byte) 0x66, (byte) 0x3B, (byte) 0x2E, (byte) 0x0A};

    private static final ZipEncoding TEST_FILE_ENCODING = ZipEncodingHelper.getZipEncoding(null);
    private DumpArchiveInputStream testFileInputStream;
    private DumpArchiveSummary testSummary;

    @Before
    public void setup() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(TEST_FILE_DATA, 0, TEST_FILE_DATA.length);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        testFileInputStream = new DumpArchiveInputStream(inputStream, TEST_FILE_ENCODING);
        testSummary = testFileInputStream.getSummary();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        DumpArchiveEntry firstEntry = testFileInputStream.getNextEntry();
        Assert.assertEquals("test.dir/", firstEntry.getName());
        Assert.assertTrue(firstEntry.isDirectory());

        DumpArchiveEntry secondEntry = testFileInputStream.getNextEntry();
        Assert.assertEquals("test.file", secondEntry.getName());
        Assert.assertFalse(secondEntry.isDirectory());
    }

    @Test(expected = ArchiveException.class)
    public void testGetNextEntryAfterEOF() throws IOException {
        testFileInputStream.getNextEntry();
        testFileInputStream.getNextEntry();
        testFileInputStream.getNextEntry();
        testFileInputStream.getNextEntry();

        testFileInputStream.getNextEntry(); // Should throw an exception
    }

    @Test
    public void testRead() throws IOException {
        byte[] readBuffer = new byte[5];

        int bytesRead = testFileInputStream.read(readBuffer, 0, readBuffer.length);
        Assert.assertEquals(5, bytesRead);
        Assert.assertEquals("test.file", new String(readBuffer, 0, 5));

        bytesRead = testFileInputStream.read(readBuffer, 0, readBuffer.length);
        Assert.assertEquals(10, bytesRead);
        Assert.assertEquals("test.filetest.dir", new String(readBuffer, 0, 10));
    }

    @Test(expected = EOFException.class)
    public void testReadAfterEOF() throws IOException {
        testFileInputStream.getNextEntry();
        testFileInputStream.getNextEntry();
        testFileInputStream.getNextEntry();
        testFileInputStream.getNextEntry();

        byte[] readBuffer = new byte[5];
        testFileInputStream.read(readBuffer, 0, readBuffer.length); // Should throw an exception
    }

    @Test
    public void testGetSummary() {
        Assert.assertEquals(6, testSummary.getNumberOfFiles());
        Assert.assertEquals(3, testSummary.getNumberOfDirectories());
        Assert.assertEquals(3 * DumpArchiveConstants.TP_SIZE + 4 * DumpArchiveConstants.TP_SIZE + DumpArchiveConstants.TP_SIZE,
                testSummary.getSize());
    }

    @Test(expected = ArchiveException.class)
    public void testInvalidFormat() throws IOException {
        ByteArrayInputStream invalidInput = new ByteArrayInputStream(new byte[]{});
        DumpArchiveInputStream invalidStream = new DumpArchiveInputStream(invalidInput, TEST_FILE_ENCODING);

        invalidStream.getNextEntry();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullEncoding() {
        new DumpArchiveInputStream(new ByteArrayInputStream(TEST_FILE_DATA), null);
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(DumpArchiveInputStream.matches(TEST_FILE_DATA, 0));
        Assert.assertTrue(DumpArchiveInputStream.matches(TEST_FILE_DATA, TEST_FILE_DATA.length));
        Assert.assertFalse(DumpArchiveInputStream.matches(TEST_FILE_DATA, TEST_FILE_DATA.length + 1));
    }

    @Test(expected = InvalidFormatException.class)
    public void testInvalidSegment() throws IOException {
        DumpArchiveInputStream invalidStream = new DumpArchiveInputStream(new ByteArrayInputStream(TEST_FILE_DATA), TEST_FILE_ENCODING);
        byte[] headerBytes = invalidStream.readRecord();
        Assert.assertEquals(SEGMENT_TYPE.ADDR, DumpArchiveUtil.getHeaderType(headerBytes));
        invalidStream.skip(DumpArchiveConstants.TP_SIZE);

        int bytesRead = invalidStream.read(new byte[1], 0, 1);
        Assert.assertEquals(-1, bytesRead);
    }

    @Test(expected = InvalidFormatException.class)
    public void testInvalidMagicValue() throws IOException {
        ByteArrayInputStream invalidInput = new ByteArrayInputStream(new byte[]{0x62, 0x74, 0x61, 0x73, 0x20, 0x62, 0x74, (byte) 0x61, 0x73, 0x20, 0x63, 0x61, 0x6E, 0x6E,
                0x6F, 0x74, 0x20, 0x65, 0x6E, 0x63, 0x6F, 0x64, 0x65, 0x20, 0x64, 0x65, 0x76, 0x65, 0x20, 0x63, 0x72, 0x65, 0x61, 0x74, 0x65,
                0x20, 0x62, 0x65, 0x66, 0x6F, 0x72, 0x20, 0x65, 0x6E, 0x63, 0x6F, 0x64, 0x65, 0x20, 0x63, 0x61, 0x6D, 0x65, 0x20, 0x73, 0x74, 0x72,
                0x69, 0x67, 0x65, 0x20, 0x73, 0x65, 0x61, 0x74, 0x65, 0x20, 0x73, 0x61, 0x6D, 0x65, 0x20, 0x68, 0x72, 0x65, 0x66, 0x3C, 0x2E, 0x0A});

        DumpArchiveInputStream invalidStream = new DumpArchiveInputStream(invalidInput, TEST_FILE_ENCODING);
        invalidStream.getNextEntry();
    }
}