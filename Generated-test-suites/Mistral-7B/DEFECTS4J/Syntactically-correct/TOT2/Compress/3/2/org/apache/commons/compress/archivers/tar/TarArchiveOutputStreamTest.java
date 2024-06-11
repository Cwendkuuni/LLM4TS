import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private File testFile;
    private ByteArrayOutputStream byteArrayOutputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        testFile = new File("test.tar");
        byteArrayOutputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(new FileOutputStream(testFile), 512);
    }

    @After
    public void tearDown() throws Exception {
        tarArchiveOutputStream.close();
        testFile.delete();
    }

    @Test
    public void testConstructor() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new FileOutputStream("test.tar"));
        tarArchiveOutputStream.close();
    }

    @Test
    public void testConstructorWithBlockSizeAndRecordSize() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new FileOutputStream("test.tar"), 512, 1024);
        tarArchiveOutputStream.close();
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new FileOutputStream("test.tar"), 512);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.close();

        tarArchiveOutputStream = new TarArchiveOutputStream(new FileOutputStream("test.tar"), 512);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.close();
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new FileOutputStream("test.tar"), 512);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(new File("test/path/with/long/filename.txt"));
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream, 512);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveEntry.setSize(100);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        byte[] header = tarArchiveEntry.getHeader(true);
        tarArchiveOutputStream.write(header, 0, header.length);
        tarArchiveOutputStream.write(new byte[100]);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expected = Arrays.copyOfRange(header, 0, header.length + 100);
        assertEquals(new String(expected, StandardCharsets.US_ASCII), byteArrayOutputStream.toString());
        tarArchiveOutputStream.close();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream, 512);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        byte[] header = tarArchiveEntry.getHeader(true);
        tarArchiveOutputStream.write(header, 0, header.length);

        assertFalse(tarArchiveOutputStream.hasNextEntry());
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expected = Arrays.copyOfRange(header, 0, header.length + 100);
        assertEquals(new String(expected, StandardCharsets.US_ASCII), byteArrayOutputStream.toString());
        tarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testWriteWithTooLittleBytes() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream, 512);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        byte[] header = tarArchiveEntry.getHeader(true);
        tarArchiveOutputStream.write(header, 0, header.length - 1);
        tarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testWriteWithTooManyBytes() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream, 512);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        byte[] header = tarArchiveEntry.getHeader(true);
        tarArchiveOutputStream.write(header, 0, header.length);
        tarArchiveOutputStream.write(new byte[1000]);
        tarArchiveOutputStream.close();
    }

    @Test
    public void testFinish() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream, 512);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        byte[] header = tarArchiveEntry.getHeader(true);
        tarArchiveOutputStream.write(header, 0, header.length);
        tarArchiveOutputStream.write(new byte[100]);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();
        byte[] expected = Arrays.copyOfRange(header, 0, header.length + 100 + 2 * 512);
        assertEquals(new String(expected, StandardCharsets.US_ASCII), byteArrayOutputStream.toString());
        tarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testCloseWithoutFinish() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream, 512);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        byte[] header = tarArchiveEntry.getHeader(true);
        tarArchiveOutputStream.write(header, 0, header.length);
        tarArchiveOutputStream.write(new byte[100]);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream, 512);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        byte[] header = tarArchiveEntry.getHeader(true);
        tarArchiveOutputStream.write(header, 0, header.length);
        tarArchiveOutputStream.write(new byte[100]);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        byte[] expected = Arrays.copyOfRange(header, 0, header.length + 100 + 2 * 512);
        assertEquals(new String(expected, StandardCharsets.US_ASCII), byteArrayOutputStream.toString());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
        assertEquals("test.txt", tarArchiveEntry.getName());
        assertEquals(new File("test.txt").length(), tarArchiveEntry.getSize());
    }

    @Test
    public void testCreateArchiveEntryWithNullName() throws IOException {
        assertNull(tarArchiveOutputStream.createArchiveEntry(null, "test.txt"));
    }

    @Test
    public void testCreateArchiveEntryWithNullFile() throws IOException {
        assertNull(tarArchiveOutputStream.createArchiveEntry(null, null));
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream, 512);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test");
        tarArchiveEntry.setSize(0);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        assertEquals(0, tarArchiveOutputStream.getBytesWritten());
        tarArchiveOutputStream.finish();
        byte[] expected = Arrays.copyOfRange(tarArchiveEntry.getHeader(true), 0, tarArchiveEntry.getHeader(true).length + 512);
        assertEquals(new String(expected, StandardCharsets.US_ASCII), byteArrayOutputStream.toString());
        byteArrayOutputStream.reset();
        tarArchiveOutputStream.close();
    }
}