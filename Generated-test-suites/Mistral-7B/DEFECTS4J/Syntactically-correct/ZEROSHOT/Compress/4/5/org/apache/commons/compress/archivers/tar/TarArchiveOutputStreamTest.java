import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.close();
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarArchiveOutputStream1 = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024, 1024);
        TarArchiveOutputStream tarArchiveOutputStream2 = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 2048);

        assertNotSame(tarArchiveOutputStream, tarArchiveOutputStream1);
        assertNotSame(tarArchiveOutputStream, tarArchiveOutputStream2);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);

        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeWithInvalidMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        tarArchiveOutputStream.setLongFileMode(-1);
    }

    @Test
    public void testFinishAndClose() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveEntry.setSize(10);
        tarArchiveEntry.setName("test.txt");

        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.write(new byte[10], 0, 10);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        byte[] expectedBytes = new byte[1024 * 10]; // 10kB
        Arrays.fill(expectedBytes, (byte) 0);

        byte[] bytes = outputStream.toByteArray();
        assertEquals(expectedBytes.length, bytes.length);

        assertFalse(tarOutputStream.isOpen());
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveEntry.setSize(10);
        tarArchiveEntry.setName("test.txt");

        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.write(new byte[10], 0, 10);

        tarOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveEntry.setSize(10);
        tarArchiveEntry.setName("test.txt");

        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.write(new byte[10], 0, 10);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();

        assertFalse(tarOutputStream.isOpen());
    }

    @Test
    public void testWriteWithUnclosedEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveEntry.setSize(10);
        tarArchiveEntry.setName("test.txt");

        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.write(new byte[10], 0, 10);
        byte[] bytes = new byte[10];
        tarOutputStream.read(bytes, 0, 10);

        assertEquals(tarArchiveEntry.getName(), new String(bytes, StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testReadAfterEntryClosed() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveEntry.setSize(10);
        tarArchiveEntry.setName("test.txt");

        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.write(new byte[10], 0, 10);
        tarOutputStream.closeArchiveEntry();

        byte[] bytes = new byte[10];
        int bytesRead = tarOutputStream.read(bytes, 0, 10);

        assertEquals(-1, bytesRead);
    }

    @Test
    public void testWriteWithLongFileName() throws IOException {
        String longFileName = "test_entry_with_long_name_" + TarConstants.NAMELEN + "_characters.txt";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(longFileName);
        tarArchiveEntry.setSize(10);
        tarArchiveEntry.setName(longFileName);

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.write(new byte[10], 0, 10);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedBytes = new byte[1024 * 20 + 1]; // 20kB + 1 byte for NUL
        Arrays.fill(expectedBytes, (byte) 0);
        expectedBytes[0] = (byte) 0x05; // 'GNU'
        expectedBytes[1] = (byte) 0x21; // '!'
        expectedBytes[2] = 0; // NUL terminator

        byte[] bytes = outputStream.toByteArray();
        assertEquals(expectedBytes.length, bytes.length);

        String longFileNameInArchive = new String(Arrays.copyOfRange(bytes, 0, 1 + TarConstants.NAMELEN), StandardCharsets.UTF_8);
        assertEquals(longFileName, longFileNameInArchive);
    }

    @Test
    public void testWriteWithLongFileNameWithoutGNUExtension() throws IOException {
        String longFileName = "test_entry_with_long_name_" + TarConstants.NAMELEN + "_characters.txt";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(longFileName);
        tarArchiveEntry.setSize(10);
        tarArchiveEntry.setName(longFileName);

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.write(new byte[10], 0, 10);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedBytes = new byte[1024 * 10]; // 10kB
        Arrays.fill(expectedBytes, (byte) 0);

        byte[] bytes = outputStream.toByteArray();
        assertEquals(expectedBytes.length, bytes.length);

        String longFileNameInArchive = new String(bytes, StandardCharsets.UTF_8);
        assertTrue(longFileNameInArchive.length() <= TarConstants.NAMELEN);
    }

    @Test(expected = IOException.class)
    public void testWriteWithLongFileNameAndErrorMode() throws IOException {
        String longFileName = "test_entry_with_long_name_" + TarConstants.NAMELEN + "_characters.txt";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(longFileName);
        tarArchiveEntry.setSize(10);
        tarArchiveEntry.setName(longFileName);

        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.write(new byte[10], 0, 10);
        tarOutputStream.closeArchiveEntry();
    }
}