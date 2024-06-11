import org.apache.commons.compress.compressors.DeflateParameters;
import org.apache.commons.compress.compressors.InputSupplier;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Deflater;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@SuppressWarnings("ResultOfMethodCallIgnored")
public class DeflateCompressorInputStreamTest {
    private InputStream inputStream;
    private DeflateCompressorInputStream deflateStream;
    private CompressorInputStream expectedDecompressor;
    private InputSupplier inputSupplier;

    @Before
    public void setUp() throws Exception {
        AnnotatedClassLoader classLoader = new AnnotatedClassLoader();
        Class<?> compressorFactoryClass = classLoader.loadClass("org.apache.commons.compress.compressors.DefaultCompressorFactory");
        InputSupplier inputSupplier = (InputSupplier) classLoader.newInstance(compressorFactoryClass, DeflateCompressorInputStream.class);
        this.inputSupplier = inputSupplier;

        DeflateParameters deflateParameters = new DeflateParameters();
        this.expectedDecompressor = inputSupplier.createDecompressor(deflateParameters.getDefaultParameters());
        this.inputStream = new ByteArrayInputStream(new byte[]{0x7F, 0x4E, 0x42, 0x61, 0x56, 0x49, 0x42, 0x41, 0x50, 0x45, 0x4E, 0x53, 0x53, 0x4D, 0x42, 0x00, 0x00});
        this.deflatStream = new DeflateCompressorInputStream(this.inputStream);
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(this.inputStream);
        IOUtils.closeQuietly(this.deflatStream);
        IOUtils.closeQuietly(this.expectedDecompressor);
    }

    @Test
    public void testConstructorWithInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF});
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);
        assertNotNull(deflatStream);
        assertSame(inputStream, deflateStream.getInputStream());
    }

    @Test
    public void testConstructorWithInputStreamAndParameters() throws IOException {
        DeflateParameters deflateParameters = new DeflateParameters();
        deflateParameters.setCompressionLevel(5);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF});
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream, deflateParameters);
        assertNotNull(deflatStream);
        assertSame(inputStream, deflateStream.getInputStream());
        assertEquals(deflateParameters, deflateStream.getParameters());
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[1024];
        int read = this.deflatStream.read(buffer, 0, buffer.length);
        assertTrue(read > 0);
        assertFalse(read == -1);
        assertSame(buffer, this.deflatStream.getBuffer());

        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF});
        InputStream expectedInput = this.expectedDecompressor;
        int expectedRead = expectedInput.read(buffer, 0, buffer.length);
        assertTrue(expectedRead > 0);
        assertFalse(expectedRead == -1);
        assertSame(buffer, expectedInput.getBuffer());

        assertEquals(expectedRead, read);
        assertTrue(IOUtils.contentEquals(expectedInput, this.deflatStream));
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[1024];
        int offset = 5;
        int length = 12;
        int read = this.deflatStream.read(buffer, offset, length);
        assertTrue(read > 0);
        assertFalse(read == -1);
        assertSame(buffer, this.deflatStream.getBuffer());

        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF});
        InputStream expectedInput = this.expectedDecompressor;
        int expectedRead = expectedInput.read(buffer, offset, length);
        assertTrue(expectedRead > 0);
        assertFalse(expectedRead == -1);
        assertSame(buffer, expectedInput.getBuffer());

        assertEquals(expectedRead, read);
        assertTrue(IOUtils.subArrayEquals(expectedInput, buffer, offset, length));
    }

    @Test
    public void testReadWithEmptyBuffer() throws IOException {
        byte[] buffer = new byte[0];
        int read = this.deflatStream.read(buffer, 0, buffer.length);
        assertEquals(0, read);
    }

    @Test
    public void testReadWithNullBuffer() throws IOException {
        int read = this.deflatStream.read(null, 0, 0);
        assertNull(read);
    }

    @Test
    public void testReadWithNegativeOffset() throws IOException {
        byte[] buffer = new byte[1024];
        int offset = -12;
        int length = buffer.length;
        int read = this.deflatStream.read(buffer, offset, length);
        assertEquals(-1, read);
    }

    @Test
    public void testReadWithNegativeLength() throws IOException {
        byte[] buffer = new byte[1024];
        int length = -12;
        int read = this.deflatStream.read(buffer, 0, length);
        assertEquals(-1, read);
    }

    @Test
    public void testSkip() throws IOException {
        long skip = this.deflatStream.skip(20L);
        assertTrue(skip >= 20);
    }

    @Test
    public void testSkipWithNegativeNumber() throws IOException {
        long skip = this.deflatStream.skip(-20L);
        assertEquals(0, skip);
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] buffer = new byte[1024];
        int read = this.deflatStream.read(buffer, 0, buffer.length);
        long available = this.deflatStream.available();
        assertEquals(buffer.length - read, available);
    }

    @Test
    public void testClose() throws IOException {
        byte[] buffer = new byte[1024];
        int read = this.deflatStream.read(buffer, 0, buffer.length);
        this.deflatStream.close();
        int readClosed = this.deflatStream.read(buffer, 0, buffer.length);
        assertEquals(-1, readClosed);
    }

    @Test
    public void testIsZlibHeaderPresent() throws IOException {
        byte[] buffer1 = new byte[]{0x78, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF};
        DeflateCompressorInputStream deflateStream1 = new DeflateCompressorInputStream(new ByteArrayInputStream(buffer1));
        assertTrue(deflatStream1.isZlibHeaderPresent());

        byte[] buffer2 = new byte[]{0x78, 0x4E, 0x42, 0x61, 0x56, 0x49, 0x42, 0x41, 0x50, 0x45, 0x4E, 0x53, 0x53, 0x4D, 0x42, 0x00, 0x00};
        DeflateCompressorInputStream deflateStream2 = new DeflateCompressorInputStream(new ByteArrayInputStream(buffer2));
        assertTrue(deflatStream2.isZlibHeaderPresent());

        byte[] buffer3 = new byte[]{0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF};
        DeflateCompressorInputStream deflateStream3 = new DeflateCompressorInputStream(new ByteArrayInputStream(buffer3));
        assertFalse(deflatStream3.isZlibHeaderPresent());

        byte[] buffer4 = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF};
        DeflateCompressorInputStream deflateStream4 = new DeflateCompressorInputStream(new ByteArrayInputStream(buffer4));
        assertFalse(deflatStream4.isZlibHeaderPresent());
    }

    @Test
    public void testReadWithZlibHeader() throws IOException {
        byte[] buffer1 = new byte[]{0x78, 0x4E, 0x42, 0x61, 0x56, 0x49, 0x42, 0x41, 0x50, 0x45, 0x4E, 0x53, 0x53, 0x4D, 0x42, 0x00, 0x00};
        InputStream expectedInput = new ByteArrayInputStream(new byte[]{0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF});
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(new ByteArrayInputStream(buffer1));
        InputStream inputStream = deflateStream.getInputStream();

        InputStream streamToCheck = new DeflateCompressorInputStream(inputStream);
        assertTrue(streamToCheck.isZlibHeaderPresent());
        assertTrue(IOUtils.contentEquals(expectedInput, streamToCheck));

        byte[] buffer2 = new byte[1024];
        int read = deflateStream.read(buffer2, 0, buffer2.length);
        InputStream decompressedStream = this.expectedDecompressor;
        int expectedRead = decompressedStream.read(buffer2, 0, buffer2.length);
        assertTrue(read > 0);
        assertFalse(read == -1);
        assertSame(buffer2, this.expectedDecompressor.getBuffer());

        assertEquals(expectedRead, read);
        assertTrue(IOUtils.contentEquals(decompressedStream, deflateStream));
    }

    @Test
    public void testReadWithZlibHeaderAndInvalidData() throws IOException {
        byte[] buffer1 = new byte[]{0x78, 0x4E, 0x42, 0x61, 0x56, 0x49, 0x42, 0x41, 0x50, 0x45, 0x4E, 0x53, 0x53, 0x4D, 0x42, 0x00, 0x00, 0x02, 0x01};
        InputStream expectedInput = new ByteArrayInputStream(new byte[]{0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0x02, 0x01});
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(new ByteArrayInputStream(buffer1));
        InputStream inputStream = deflateStream.getInputStream();

        InputStream streamToCheck = new DeflateCompressorInputStream(inputStream);
        assertTrue(streamToCheck.isZlibHeaderPresent());
        assertFalse(IOUtils.contentEquals(expectedInput, streamToCheck));

        byte[] buffer2 = new byte[1024];
        int read = deflateStream.read(buffer2, 0, buffer2.length);
        InputStream decompressedStream = this.expectedDecompressor;
        int expectedRead = decompressedStream.read(buffer2, 0, buffer2.length);
        assertEquals(-1, read);
        assertEquals(-1, expectedRead);

        byte[] buffer3 = new byte[1024];
        int readWithInvalidData = deflateStream.read(buffer3, 0, buffer3.length);
        assertEquals(-1, readWithInvalidData);
        assertEquals(-1, expectedRead);

        assertTrue(IOUtils.contentEquals(expectedInput, deflateStream.getInputStream()));
    }
}