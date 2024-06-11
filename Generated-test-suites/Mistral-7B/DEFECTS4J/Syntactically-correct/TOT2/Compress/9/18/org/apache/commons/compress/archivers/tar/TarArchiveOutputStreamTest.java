import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream out;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setup() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        out = new TarArchiveOutputStream(byteArrayOutputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (out != null) {
            out.close();
        }
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test.tar")));
        assertNotNull(tarOutputStream);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        out.putArchiveEntry(new TarArchiveEntry("test.txt", "Test file contents".getBytes()));
        out.write("Test file contents".getBytes());
        out.closeArchiveEntry();

        byte[] expectedTarHeader = new byte[]{(byte) 0x0, (byte) 'T', (byte) 'a', (byte) 'r', (byte) 0, (byte) '0', (byte) ' ',
                (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) '/',
                (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) 0,
                (byte) 0, (byte) (short) 14, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 12,
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 10, (byte) 4, (byte) 0, (byte) 0, (byte) 12, (byte) 12, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0};

        byte[] expectedData = "Test file contents".getBytes();

        assertEquals(expectedTarHeader.length + expectedData.length, byteArrayOutputStream.size());
        assertTrue(Arrays.equals(expectedTarHeader, byteArrayOutputStream.toByteArray().subArray(0, expectedTarHeader.length)));
        assertTrue(Arrays.equals(expectedData, byteArrayOutputStream.toByteArray().subArray(expectedTarHeader.length, expectedTarHeader.length + expectedData.length)));
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "a/long/test/path/test.txt";
        out.putArchiveEntry(new TarArchiveEntry(longFileName, "Test file contents".getBytes()));
        out.write("Test file contents".getBytes());
        out.closeArchiveEntry();

        byte[] expectedTarHeader = new byte[]{(byte) 0x0, (byte) 'T', (byte) 'a', (byte) 'r', (byte) 0, (byte) '0', (byte) ' ',
                (byte) 'l', (byte) 'o', (byte) 'n', (byte) 'g', (byte) '/', (byte) 't', (byte) 'e', (byte) 's', (byte) 't',
                (byte) '/', (byte) 'p', (byte) 'a', (byte) 't', (byte) 'h', (byte) '/', (byte) 't', (byte) 'e', (byte) 's',
                (byte) 't', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) 'x', (byte) ' ', (byte) 0, (byte) 0,
                (byte) (short) 31, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 12, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 10, (byte) 4, (byte) 0, (byte) 0, (byte) 12, (byte) 12,
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0, (byte) 10, (byte) 4, (byte) 0, (byte) 0, (byte) 12, (byte) 12, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0};

        byte[] expectedData = "Test file contents".getBytes();

        assertEquals(expectedTarHeader.length + expectedData.length, byteArrayOutputStream.size());
        assertTrue(Arrays.equals(expectedTarHeader, byteArrayOutputStream.toByteArray().subArray(0, expectedTarHeader.length)));
        assertTrue(Arrays.equals(expectedData, byteArrayOutputStream.toByteArray().subArray(expectedTarHeader.length, expectedTarHeader.length + expectedData.length)));
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileNameAndLongFileModeError() throws IOException {
        String longFileName = "a/long/test/path/test.txt";
        out.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        out.putArchiveEntry(new TarArchiveEntry(longFileName, "Test file contents".getBytes()));
    }

    @Test
    public void testFinish() throws IOException {
        out.putArchiveEntry(new TarArchiveEntry("test.txt", "Test file contents".getBytes()));
        out.write("Test file contents".getBytes());
        out.closeArchiveEntry();
        out.finish();

        byte[] expectedEndOfArchiveRecord = new byte[1024]; // endOfArchiveRecord is a record of all zeros

        assertEquals(byteArrayOutputStream.size(), expectedEndOfArchiveRecord.length * 2);
        assertTrue(Arrays.equals(expectedEndOfArchiveRecord, byteArrayOutputStream.toByteArray().subArray(0, expectedEndOfArchiveRecord.length)));
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        out.putArchiveEntry(new TarArchiveEntry("test.txt", "Test file contents".getBytes()));
        out.write("Test file contents".getBytes());

        out.finish();
        out.putArchiveEntry(new TarArchiveEntry("test2.txt", "Test file contents2".getBytes()));
    }

    @Test
    public void testWrite() throws IOException {
        out.putArchiveEntry(new TarArchiveEntry("test.txt", "Test file contents".getBytes()));
        out.write("Test file contents".getBytes());

        out.closeArchiveEntry();
        out.finish();

        byte[] expectedTarHeader = new byte[]{(byte) 0x0, (byte) 'T', (byte) 'a', (byte) 'r', (byte) 0, (byte) '0', (byte) ' ',
                (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) ' ',
                (byte) 0, (byte) 0, (byte) (short) 14, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 12, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 10, (byte) 4, (byte) 0, (byte) 0, (byte) 12, (byte) 12, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0};

        byte[] expectedData = "Test file contents".getBytes();

        assertEquals(expectedTarHeader.length + expectedData.length, byteArrayOutputStream.size());
        assertTrue(Arrays.equals(expectedTarHeader, byteArrayOutputStream.toByteArray().subArray(0, expectedTarHeader.length)));
        assertTrue(Arrays.equals(expectedData, byteArrayOutputStream.toByteArray().subArray(expectedTarHeader.length, expectedTarHeader.length + expectedData.length)));
    }

    @Test
    public void testClose() throws IOException {
        out.putArchiveEntry(new TarArchiveEntry("test.txt", "Test file contents".getBytes()));
        out.write("Test file contents".getBytes());
        out.close();

        byte[] expectedTarHeader = new byte[]{(byte) 0x0, (byte) 'T', (byte) 'a', (byte) 'r', (byte) 0, (byte) '0', (byte) ' ',
                (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) ' ',
                (byte) 0, (byte) 0, (byte) (short) 14, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 12, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 10, (byte) 4, (byte) 0, (byte) 0, (byte) 12, (byte) 12, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0};

        byte[] expectedData = "Test file contents".getBytes();

        assertEquals(expectedTarHeader.length + expectedData.length, byteArrayOutputStream.size());
        assertTrue(Arrays.equals(expectedTarHeader, byteArrayOutputStream.toByteArray().subArray(0, expectedTarHeader.length)));
        assertTrue(Arrays.equals(expectedData, byteArrayOutputStream.toByteArray().subArray(expectedTarHeader.length, expectedTarHeader.length + expectedData.length)));

        byte[] output = byteArrayOutputStream.toByteArray();
        byte[] expectedEndOfArchiveRecord = new byte[1024]; // endOfArchiveRecord is a record of all zeros

        assertEquals(expectedTarHeader.length + expectedData.length + expectedEndOfArchiveRecord.length, output.length);
        assertTrue(Arrays.equals(expectedTarHeader, Arrays.copyOfRange(output, 0, expectedTarHeader.length)));
        assertTrue(Arrays.equals(expectedData, Arrays.copyOfRange(output, expectedTarHeader.length, expectedTarHeader.length + expectedData.length)));
        assertTrue(Arrays.equals(expectedEndOfArchiveRecord, Arrays.copyOfRange(output, expectedTarHeader.length + expectedData.length, output.length)));
    }

    @Test
    public void testWriteToFile() throws IOException {
        String testFile = "test_output.tar";
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new File(testFile));

        tarOutputStream.putNextEntry(new TarArchiveEntry("test.txt", "Test file contents".getBytes()));
        tarOutputStream.write("Test file contents".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        tarOutputStream = new TarArchiveOutputStream(inputStream, 512, 512);

        TarArchiveEntry entry = tarOutputStream.getNextEntry();
        byte[] output = new byte[1024];

        int read = -1;
        while ((read = tarOutputStream.read(output)) != -1) {
            System.out.write(output, 0, read);
        }

        assertEquals(expectedTarHeader.length + expectedData.length, byteArrayOutputStream.size());

        File testFileOutput = new File(testFile);
        byte[] expectedFileContent = new byte[expectedTarHeader.length + expectedData.length];
        IOUtils.copy(new FileInputStream(testFileOutput), new ByteArrayOutputStream(expectedFileContent.length).toByteArrayOutputStrm());

        assertTrue(Arrays.equals(expectedTarHeader, Arrays.copyOfRange(expectedFileContent, 0, expectedTarHeader.length)));
        assertTrue(Arrays.equals(expectedData, Arrays.copyOfRange(expectedFileContent, expectedTarHeader.length, expectedTarHeader.length + expectedData.length)));

        testFileOutput.delete();
    }
}