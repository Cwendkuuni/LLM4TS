import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOutputStream2 = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        TarArchiveOutputStream tarOutputStream3 = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);

        assertTrue(tarOutputStream instanceof TarArchiveOutputStream);
        assertTrue(tarOutputStream2 instanceof TarArchiveOutputStream);
        assertTrue(tarOutputStream3 instanceof TarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);

        assertThat(tarOutputStream.getLongFileMode(), is(TarArchiveOutputStream.LONGFILE_ERROR));
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();

        byte[] expected = new byte[1024 * 2]; // Two 512 bytes of zeros

        byte[] actual = outputStream.toByteArray();

        assertThat(actual, is(expected));
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.close();

        byte[] expected = new byte[512]; // Header record
        byte[] actual = outputStream.toByteArray();

        assertThat(actual, is(expected));
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);

        int expected = 512;
        int actual = tarOutputStream.getRecordSize();

        assertThat(actual, is(expected));
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);

        byte[] expectedHeader = entry.getHeaderBytes();

        byte[] actual = outputStream.toByteArray();

        byte[] actualHeader = new byte[expectedHeader.length];
        System.arraycopy(actual, 0, actualHeader, 0, expectedHeader.length);

        assertThat(actualHeader, is(expectedHeader));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);

        byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();

        byte[] expected = new byte[512 + data.length]; // Header record + data
        byte[] actual = outputStream.toByteArray();

        assertThat(actual, is(expected));
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);

        byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();

        byte[] expected = new byte[512 + data.length]; // Header record + data
        byte[] actual = outputStream.toByteArray();

        assertThat(actual, is(expected));
    }
}