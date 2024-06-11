import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.powermock.api.mockito.powermock.PowerMock.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DeflateCompressorInputStream.class)
public class DeflateCompressorInputStreamTest {

    private InputStream inputStream;
    private DeflateCompressorInputStream deflateCompressorInputStream;
    private static final byte[] TEST_DATA = new byte[]{(byte) 0x78, (byte) 0x3c, (byte) 0x96, (byte) 0x64, (byte) 0x57, (byte) 0x2a};

    @Before
    public void setup() throws Exception {
        inputStream = new ByteArrayInputStream(TEST_DATA);
        deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[1];
        int read;

        // read first byte
        read = deflateCompressorInputStream.read();
        assertEquals((int) TEST_DATA[0], read);

        // read remaining bytes
        buffer[0] = (byte) deflateCompressorInputStream.read();
        assertEquals((int) TEST_DATA[1], (int) buffer[0]);
        buffer[0] = (byte) deflateCompressorInputStream.read();
        assertEquals((int) TEST_DATA[2], (int) buffer[0]);
        buffer[0] = (byte) deflateCompressorInputStream.read();
        assertEquals((int) TEST_DATA[3], (int) buffer[0]);
        buffer[0] = (byte) deflateCompressorInputStream.read();
        assertEquals((int) TEST_DATA[4], (int) buffer[0]);
        buffer[0] = (byte) deflateCompressorInputStream.read();
        assertEquals((int) TEST_DATA[5], (int) buffer[0]);

        // end of stream
        assertFalse(deflateCompressorInputStream.read() > -1);

        // clean up
        IOUtils.closeQuietly(deflateCompressorInputStream);
        IOUtils.closeQuietly(inputStream);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[TEST_DATA.length];
        int read = deflateCompressorInputStream.read(buffer, 0, TEST_DATA.length);

        assertEquals(TEST_DATA.length, read);
        assertNotNull(buffer);
        for (int i = 0; i < TEST_DATA.length; i++) {
            assertEquals(TEST_DATA[i], buffer[i]);
        }

        // clean up
        IOUtils.closeQuietly(deflateCompressorInputStream);
        IOUtils.closeQuietly(inputStream);
    }

    @Test
    public void testSkip() throws IOException {
        long skipped = deflateCompressorInputStream.skip(2);
        assertEquals(2, skipped);

        // clean up
        IOUtils.closeQuietly(deflateCompressorInputStream);
        IOUtils.closeQuietly(inputStream);
    }

    @Test
    public void testAvailable() throws IOException {
        int available = deflateCompressorInputStream.available();
        assertEquals(TEST_DATA.length - deflateCompressorInputStream.available(), deflateCompressorInputStream.read(new byte[TEST_DATA.length][0].length));

        // clean up
        IOUtils.closeQuietly(deflateCompressorInputStream);
        IOUtils.closeQuietly(inputStream);
    }

    @Test
    public void testClose() throws IOException {
        deflateCompressorInputStream.close();
        assertThrows(IOException.class, () -> deflateCompressorInputStream.read());

        // clean up
        IOUtils.closeQuietly(inputStream);
    }

    @Test
    public void testZlibHeader() throws IOException {
        DeflateParameters parameters = new DeflateParameters();
        parameters.setWithZlibHeader(true);
        InputStream zlibInputStream = new ByteArrayInputStream(new byte[]{0x78, 0x5c, 0x4d, 0x6f, 0x00, 0x14, TEST_DATA[0], TEST_DATA[1], TEST_DATA[2], TEST_DATA[3], TEST_DATA[4], TEST_DATA[5], 0x00, (byte) 0xff});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(zlibInputStream, parameters);

        byte[] buffer = new byte[1];
        int read;

        // read first byte
        read = deflateCompressorInputStream.read();
        assertEquals((int) TEST_DATA[0], read);

        // clean up
        IOUtils.closeQuietly(deflateCompressorInputStream);
        IOUtils.closeQuietly(zlibInputStream);
    }

    @Test
    public void testWithoutZlibHeader() throws IOException {
        InputStream noZlibInputStream = new ByteArrayInputStream(TEST_DATA);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(noZlibInputStream, new DeflateParameters());

        byte[] buffer = new byte[1];
        int read;

        // read first byte
        read = deflateCompressorInputStream.read();
        assertEquals((int) TEST_DATA[0], read);

        // clean up
        IOUtils.closeQuietly(deflateCompressorInputStream);
        IOUtils.closeQuietly(noZlibInputStream);
    }

    @Test
    public void testZipFile() throws IOException {
        byte[] testData = new byte[]{0x50, 0x4b, 0x03, 0x04, 0x14, (byte) 0x00, 0x00, 0x00, 0x00, 0x08, 0x06};
        String testName = "test.zip";

        try (ZipFile zipFile = new ZipFile(testName)) {
            ZipEntry entry = zipFile.getEntry("test.dat");
            InputStream inputStream = zipFile.getInputStream(entry);
            deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

            byte[] buffer = new byte[1];
            int read;

            // read first byte
            read = deflateCompressorInputStream.read();
            assertEquals((int) TEST_DATA[0], read);

            // clean up
            IOUtils.closeQuietly(deflateCompressorInputStream);
            IOUtils.closeQuietly(inputStream);
            zipFile.close();
        }
    }

    @Test
    public void testInvalidZipFile() throws IOException {
        String testName = "test.dat";

        try (ZipFile zipFile = new ZipFile(testName)) {
            assertThrows(IOException.class, () -> new DeflateCompressorInputStream(zipFile.getInputStream(new ZipEntry("test.dat")), new DeflateParameters()));
            zipFile.close();
        }
    }

    @Test
    public void testInvalidStream() {
        InputStream invalidStream = mock(InputStream.class);
        when(invalidStream.read()).thenThrow(new IOException("Invalid Stream"));
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(invalidStream, new DeflateParameters());

        assertThrows(IOException.class, () -> deflateCompressorInputStream.read());

        // clean up
        verifyStatic();
        deflateCompressorInputStream.close();
    }
}