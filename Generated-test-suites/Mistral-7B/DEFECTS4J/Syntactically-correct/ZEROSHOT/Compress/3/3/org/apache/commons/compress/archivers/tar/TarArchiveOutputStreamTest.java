import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.test.util.TestFileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TarArchiveOutputStreamTest {

    private File testFile;
    private OutputStream outputStream;
    private ByteArrayOutputStream tarOutputStream;

    @Before
    public void setUp() throws Exception {
        testFile = TestFileUtils.getTestFile("test.tar");
        outputStream = new FileOutputStream(testFile);
        tarOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(tarOutputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt", new File("src/test/resources/test.txt")));
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8), 0, "Hello World!".length());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
    }

    @After
    public void tearDown() throws Exception {
        TestFileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testPutNextEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", new File("src/test/resources/test.txt"));

        tarArchiveOutputStream.putNextEntry(tarArchiveEntry);
        assertNotNull(tarArchiveOutputStream.getNextEntry());
        assertEquals("test.txt", tarArchiveOutputStream.getNextEntry().getName());

        tarArchiveOutputStream.closeArchiveEntry();
        assertNull(tarArchiveOutputStream.getNextEntry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNextEntryNull() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.putNextEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryInvalidFile() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", null);
        tarArchiveOutputStream.putNextEntry(tarArchiveEntry);
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", new File("src/test/resources/test.txt"));
        tarArchiveOutputStream.putNextEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] inputData = TestFileUtils.readFileToByteArray(testFile);
        assertEquals(data.length, inputData.length);
        TestFileUtils.deleteQuietly(testFile);
    }

    @Test(expected = IOException.class)
    public void testWriteNull() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        byte[] data = null;
        tarArchiveOutputStream.write(data, 0, data.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testWriteNegativeOffset() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(data, -1, data.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteNegativeLength() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(data, 0, -1);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", new File("src/test/resources/test.txt"));
        tarArchiveOutputStream.putNextEntry(tarArchiveEntry);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        byte[] inputData = TestFileUtils.readFileToByteArray(testFile);
        int eofRecordLength = TarConstants.TAR_MAGIC.length + TarConstants.HEADER_SIZE * 2;
        int headerRecordLength = tarArchiveEntry.getSize() + TarConstants.HEADER_SIZE;
        int dataLength = tarArchiveEntry.getSize();
        int expectedSize = eofRecordLength + headerRecordLength + dataLength;
        assertEquals(expectedSize, inputData.length);

        TestFileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        ByteArrayOutputStream tarOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(tarOutputStream);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", new File("src/test/resources/test.txt"));
        tarArchiveOutputStream.putNextEntry(tarArchiveEntry);
        String longFileName = "long_" + new String(new char[1024]).replace("\0", "a");
        tarArchiveEntry = new TarArchiveEntry(longFileName, new File("src/test/resources/test.txt"));
        tarArchiveOutputStream.putNextEntry(tarArchiveEntry);

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8), 0, "Hello World!".length());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(tarOutputStream.toByteArray());
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
        assertNotNull(TarArchiveEntry.read(inputStream));

        entry = new TarArchiveEntry("long_" + new String(new char[1024]).substring(0, 512));
        assertNull(TarArchiveEntry.read(inputStream));
    }
}