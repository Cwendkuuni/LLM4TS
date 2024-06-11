import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DeflateCompressorInputStreamTest {

    private static final byte[] TEST_DATA_COMPRESSED = new byte[] {
            0x78, 0x9c, 0x7f, 0x4e, 0x00, 0x04, 0x14, 0x00, 0x02, 0x01, 0x0b, 0xbe, 0xeb, 0x80, 0x3c, 0x02, 0x01, 0x00, 0x02, 0x00, 0x00, 0x00, 0x03, 0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x02, 0x02, 0x01, 0x01, 0x00, 0x02, 0x04, 0x78, 0x42, 0x01, 0x0d, 0x02, 0x1a, 0x47, 0x54, 0x54, 0x72, 0x6b, 0x20, 0x20, 0x20, 0x6c, 0x69, 0x6e, 0x65, 0x20, 0x43, 0x41, 0x53, 0x53, 0x45, 0x4c, 0x45, 0x44, 0x20, 0x57, 0x49, 0x50, 0x55, 0x4e, 0x44, 0x4f, 0x53, 0x20, 0x41, 0x56, 0x54, 0x45, 0x4c, 0x45, 0x52
    };
    private static final byte[] TEST_DATA_UNCOMPRESSED = new byte[] {
            'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!', 'D', 'a', 't', 'a', ' ', 'C', 'o', 'm', 'p', 'r', 'e', 's', 's', 'i', 'o', 'n', 's', ' ', 'v', '1.0'
    };

    private DeflateCompressorInputStream testStream;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream testData = new ByteArrayInputStream(TEST_DATA_COMPRESSED);
        testStream = new DeflateCompressorInputStream(testData);
        inputStream = new ByteArrayInputStream(TEST_DATA_UNCOMPRESSED);
    }

    @After
    public void tearDown() throws IOException {
        testStream.close();
        outputStream.close();
        inputStream.close();
    }

    @Test
    public void testDeflateCompressorInputStreamWithZlibHeader() throws IOException, DataFormatException {
        byte[] decompressedData = new byte[TEST_DATA_UNCOMPRESSED.length];
        int read = testStream.read(decompressedData, 0, decompressedData.length);
        assertFalse("The stream should have a zlib header", testStream.hasZlibHeader());
        assertEquals(TEST_DATA_UNCOMPRESSED.length, read);
        assertNotNull("The data should have been read correctly", decompressedData);
        assertArrayEquals(TEST_DATA_UNCOMPRESSED, decompressedData);
    }

    @Test
    public void testDeflateCompressorInputStreamWithoutZlibHeader() throws IOException, DataFormatException {
        DeflateParameters deflateParameters = new DeflateParameters();
        deflateParameters.setWithZlibHeader(false);
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        testStream = new DeflateCompressorInputStream(inputStream, deflateParameters);
        byte[] compressedData = testStream.readAllBytes();
        assertFalse("The stream should not have a zlib header", testStream.hasZlibHeader());
        assertNotNull("The data should have been written correctly", compressedData);
        assertEquals(TEST_DATA_COMPRESSED.length, compressedData.length);
        Inflater inflater = new Inflater();
        ByteArrayInputStream testDataDecompressed = new ByteArrayInputStream(compressedData);
        byte[] decompressedData = new byte[TEST_DATA_UNCOMPRESSED.length];
        int read = testDataDecompressed.read(decompressedData, 0, decompressedData.length);
        assertEquals(TEST_DATA_UNCOMPRESSED.length, read);
        assertNotNull("The data should have been decompressed correctly", decompressedData);
        assertArrayEquals(TEST_DATA_UNCOMPRESSED, decompressedData);
    }

    @Test(expected = IOException.class)
    public void testDeflateCompressorInputStreamWithNegativeLength() throws IOException {
        testStream = new DeflateCompressorInputStream(inputStream);
        testStream.skip(-1);
    }
}