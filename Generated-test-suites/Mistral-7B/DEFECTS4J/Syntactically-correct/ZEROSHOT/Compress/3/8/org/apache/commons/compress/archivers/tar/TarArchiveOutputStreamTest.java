import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarOutputStream tarOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        tarOutputStream.close();
    }

    @Test
    public void testConstructor() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new File("test.tar").getOutputStream());
        assertEquals("The constructor should initialize a TarArchiveOutputStream", tarArchiveOutputStream, null);
    }

    @Test(expected = NullPointerException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() throws IOException {
        tarOutputStream.putArchiveEntry(null);
    }

    @Test
    public void testPutArchiveEntryAndCloseArchiveEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.closeArchiveEntry();

        byte[] bytes = outputStream.toByteArray();
        byte[] expectedHeader = TarConstants.MAGIC_NUMBER;
        byte[] expectedName = "test.txt\0".getBytes(StandardCharsets.UTF_8);
        byte[] expectedSize = new byte[8];
        long size = tarArchiveEntry.getSize();
        expectedSize[0] = (byte) (size >>> 24);
        expectedSize[1] = (byte) (size >>> 16);
        expectedSize[2] = (byte) (size >>> 8);
        expectedSize[3] = (byte) size;

        byte[] expectedMode = new byte[8];
        expectedMode[0] = '0';
        expectedMode[1] = '1';
        expectedMode[2] = '0';
        expectedMode[3] = '0';
        expectedMode[4] = '0';
        expectedMode[5] = '-';
        expectedMode[6] = '-';
        expectedMode[7] = '\0';

        int headerLength = TarConstants.HEADER_LENGTH;
        int recordSize = tarOutputStream.getRecordSize();

        for (int i = 0; i < headerLength; ++i) {
            assertEquals("The header should match the expected bytes", expectedHeader[i], bytes[i]);
        }

        for (int i = headerLength; i < headerLength + expectedName.length; ++i) {
            assertEquals("The name should match the expected bytes", expectedName[i - headerLength], bytes[i]);
        }

        for (int i = headerLength + expectedName.length; i < headerLength + expectedName.length + expectedSize.length; ++i) {
            assertEquals("The size should match the expected bytes", expectedSize[i - (headerLength + expectedName.length)], bytes[i]);
        }

        for (int i = headerLength + expectedName.length + expectedSize.length; i < headerLength + expectedName.length + expectedSize.length + expectedMode.length; ++i) {
            assertEquals("The mode should match the expected bytes", expectedMode[i - (headerLength + expectedName.length + expectedSize.length)], bytes[i]);
        }

        assertEquals("The stream should have written the correct number of bytes", tarArchiveEntry.getSize() + TarConstants.HEADER_LENGTH, bytes.length);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.putArchiveEntry(tarArchiveEntry);
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.write("test content".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();

        byte[] bytes = outputStream.toByteArray();
        byte[] expectedContent = "test content".getBytes(StandardCharsets.UTF_8);

        int i = TarConstants.HEADER_LENGTH + tarArchiveEntry.getName().length() + tarArchiveEntry.getSize();

        for (int j = i; j < i + expectedContent.length; ++j) {
            assertEquals("The content should match the expected bytes", expectedContent[j - i], bytes[j]);
        }
    }

    @Test(expected = IOException.class)
    public void testWriteWithNonExistentArchiveEntry() throws IOException {
        tarOutputStream.write("test content".getBytes(StandardCharsets.UTF_8));
    }
}