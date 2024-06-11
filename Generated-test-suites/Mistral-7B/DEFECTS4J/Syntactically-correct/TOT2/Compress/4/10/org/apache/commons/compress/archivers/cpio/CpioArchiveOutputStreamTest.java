import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        String testFileName = "testFile";
        String testFileContent = "Test content";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setSize(testFileContent.length());
        entry.setFormat(CpioConstants.FORMAT_NEW);

        outputStream.putArchiveEntry(entry);
        outputStream.write(testFileContent.getBytes());
        outputStream.closeArchiveEntry();

        byte[] actualData = byteArrayOutputStream.toByteArray();
        byte[] expectedData = generateExpectedData(testFileName, testFileContent);

        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        String testFileName = "testFile";
        String testFileContent = "Test content";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setSize(testFileContent.length());
        entry.setFormat(CpioConstants.FORMAT_OLD_BINARY); // Invalid format for this CpioArchiveOutputStream

        try {
            outputStream.putArchiveEntry(entry);
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testPutArchiveEntryWithDuplicateName() throws IOException {
        String testFileName = "testFile";
        String testFileContent = "Test content";
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(testFileName);
        entry1.setSize(testFileContent.length());
        entry1.setFormat(CpioConstants.FORMAT_NEW);

        outputStream.putArchiveEntry(entry1);
        outputStream.write(testFileContent.getBytes());
        outputStream.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(testFileName);
        entry2.setSize(testFileContent.length());
        entry2.setFormat(CpioConstants.FORMAT_NEW);

        try {
            outputStream.putArchiveEntry(entry2);
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCloseArchiveEntryWithInvalidEntry() throws IOException {
        CpioArchiveEntry entry = null;
        try {
            outputStream.closeArchiveEntry();
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }

        // Ensure previous test did not change the state of the stream
        Assert.assertFalse(outputStream.isClosed());

        try {
            outputStream.closeArchiveEntry();
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }

        try {
            outputStream.putArchiveEntry(new CpioArchiveEntry());
            outputStream.write(new byte[0]);
            outputStream.closeArchiveEntry();
        } finally {
            outputStream.close();
        }
    }

    @Test
    public void testFinish() throws IOException {
        String testFileName1 = "testFile1";
        String testFileName2 = "testFile2";
        String testFileContent = "Test content";
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(testFileName1);
        entry1.setSize(testFileContent.length());
        entry1.setFormat(CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(testFileName2);
        entry2.setSize(testFileContent.length());
        entry2.setFormat(CpioConstants.FORMAT_NEW);

        outputStream.putArchiveEntry(entry1);
        outputStream.write(testFileContent.getBytes());
        outputStream.closeArchiveEntry();

        outputStream.putArchiveEntry(entry2);
        outputStream.write(testFileContent.getBytes());
        outputStream.closeArchiveEntry();

        outputStream.finish();

        byte[] actualData = byteArrayOutputStream.toByteArray();
        byte[] expectedData = generateExpectedDataWithTrailer(testFileName1, testFileName2, testFileContent);

        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testWrite() throws IOException {
        String testFileName = "testFile";
        String testFileContent = "Test content";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setSize(testFileContent.length());
        entry.setFormat(CpioConstants.FORMAT_NEW);

        outputStream.putArchiveEntry(entry);
        outputStream.write(testFileContent.getBytes());
        outputStream.closeArchiveEntry();

        byte[] actualData = byteArrayOutputStream.toByteArray();
        byte[] expectedData = generateExpectedData(testFileName, testFileContent);

        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testWriteWithInvalidEntry() throws IOException {
        CpioArchiveEntry entry = null;

        try {
            outputStream.write(new byte[0]);
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }

        // Ensure previous test did not change the state of the stream
        Assert.assertFalse(outputStream.isClosed());

        try {
            outputStream.write(new byte[0]);
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }

        try {
            outputStream.putArchiveEntry(new CpioArchiveEntry());
            outputStream.write(new byte[0]);
            outputStream.closeArchiveEntry();
        } finally {
            outputStream.close();
        }
    }

    @Test
    public void testWriteWithInvalidEntrySize() throws IOException {
        String testFileName = "testFile";
        String testFileContent = "Test content";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setSize(testFileContent.length() + 1); // Invalid size
        entry.setFormat(CpioConstants.FORMAT_NEW);

        try {
            outputStream.putArchiveEntry(entry);
            outputStream.write(testFileContent.getBytes());
            outputStream.closeArchiveEntry();
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testWriteWithInvalidDataSize() throws IOException {
        String testFileName = "testFile";
        String testFileContent = "Test content";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setSize(testFileContent.length());
        entry.setFormat(CpioConstants.FORMAT_NEW);

        outputStream.putArchiveEntry(entry);

        try {
            outputStream.write(new byte[testFileContent.length() + 1]); // Invalid data size
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }

        outputStream.closeArchiveEntry();
    }

    private byte[] generateExpectedData(String testFileName, String testFileContent) {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setSize(testFileContent.length());
        entry.setFormat(CpioConstants.FORMAT_NEW);

        byte[] expectedHeader = writeEntryHeader(entry);
        byte[] expectedContent = testFileContent.getBytes();
        byte[] expectedTrailer = writeTrailer();

        return merge(expectedHeader, expectedContent, expectedTrailer);
    }

    private byte[] generateExpectedDataWithTrailer(String testFileName1, String testFileName2, String testFileContent) {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(testFileName1);
        entry1.setSize(testFileContent.length());
        entry1.setFormat(CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(testFileName2);
        entry2.setSize(testFileContent.length());
        entry2.setFormat(CpioConstants.FORMAT_NEW);

        byte[] expectedHeader1 = writeEntryHeader(entry1);
        byte[] expectedContent = testFileContent.getBytes();
        byte[] expectedHeader2 = writeEntryHeader(entry2);
        byte[] expectedTrailer = writeTrailer();

        return merge(expectedHeader1, expectedContent, expectedHeader2, expectedTrailer);
    }

    private byte[] writeEntryHeader(CpioArchiveEntry entry) {
        CpioArchiveOutputStream archiveOutputStream = new CpioArchiveOutputStream(new FileOutputStream(new File("temp.cpio")), CpioConstants.FORMAT_NEW);
        byte[] result;
        try {
            archiveOutputStream.putArchiveEntry(entry);
            result = archiveOutputStream.getHeader();
            archiveOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private byte[] writeTrailer() {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(CpioConstants.CPIO_TRAILER);
        entry.setNumberOfLinks(1);
        entry.setFormat(CpioConstants.FORMAT_NEW);

        try {
            CpioArchiveOutputStream archiveOutputStream = new CpioArchiveOutputStream(new FileOutputStream(new File("temp.cpio")), CpioConstants.FORMAT_NEW);
            archiveOutputStream.putArchiveEntry(entry);
            byte[] trailer = archiveOutputStream.getHeader();
            archiveOutputStream.close();
            new File("temp.cpio").delete();
            return trailer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] merge(byte[]... data) {
        int length = 0;
        for (byte[] b : data) {
            length += b.length;
        }
        byte[] result = new byte[length];
        int index = 0;
        for (byte[] b : data) {
            System.arraycopy(b, 0, result, index, b.length);
            index += b.length;
        }
        return result;
    }
}