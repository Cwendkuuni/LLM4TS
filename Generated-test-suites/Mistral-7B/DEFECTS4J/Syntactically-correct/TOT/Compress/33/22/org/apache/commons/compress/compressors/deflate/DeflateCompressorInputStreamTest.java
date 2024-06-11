import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;

/**
 * Test class for DeflateCompressorInputStream.
 */
public class DeflateCompressorInputStreamTest {

    /**
     * Test reading data from a DeflateCompressorInputStream with valid data.
     *
     * @throws IOException when an error occurs while reading or writing
     */
    @Test
    public void testValidData() throws IOException {
        String inputData = "Hello, World!";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DeflateParameters parameters = new DeflateParameters();

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData.getBytes())) {
            try (DeflateCompressorInputStream compressedStream = new DeflateCompressorInputStream(inputStream, parameters)) {
                byte[] buffer = new byte[inputData.length()];
                int length;

                while ((length = compressedStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                Assert.assertEquals(inputData, outputStream.toString());
            }
        }
    }

    /**
     * Test reading data from a DeflateCompressorInputStream with empty data.
     *
     * @throws IOException when an error occurs while reading or writing
     */
    @Test
    public void testEmptyData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DeflateParameters parameters = new DeflateParameters();

        try (DeflateCompressorInputStream compressedStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[0]), parameters)) {
            byte[] buffer = new byte[1];
            int length;

            while ((length = compressedStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            Assert.assertEquals("", outputStream.toString());
        }
    }

    /**
     * Test reading data from a DeflateCompressorInputStream with incorrect data.
     *
     * @throws IOException when an error occurs while reading or writing
     */
    @Test(expected = DataFormatException.class)
    public void testIncorrectData() throws IOException {
        String inputData = "Hello, World!";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DeflateParameters parameters = new DeflateParameters();

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData.getBytes())) {
            try (DeflateCompressorInputStream compressedStream = new DeflateCompressorInputStream(inputStream, parameters)) {
                byte[] buffer = new byte[2];
                int length;

                while ((length = compressedStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                Assert.fail("Expected DataFormatException but received valid data");
            }
        }
    }

    /**
     * Test reading data from a DeflateCompressorInputStream with zlib header.
     *
     * @throws IOException when an error occurs while reading or writing
     */
    @Test
    public void testZlibHeader() throws IOException {
        String inputData = "Hello, World!";
        byte[] zlibHeader = new byte[]{0x78, 0x5E, 0x01, 0x00, 0x14}; // FLAGS: LZW, Adler-32, uncompressed

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(zlibHeader)) {
            try (ByteArrayInputStream dataStream = new ByteArrayInputStream(inputData.getBytes())) {
                try (DeflateCompressorInputStream compressedStream = new DeflateCompressorInputStream(dataStream)) {
                    try (DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters())) {
                        byte[] buffer = new byte[inputData.length()];
                        int length;

                        while ((length = testStream.read(buffer)) > 0) {
                            compressedStream.read(buffer, 0, length);
                        }

                        Assert.assertEquals(inputData, IOUtils.toString(new ByteArrayInputStream(compressedStream.getRemainingBytes())));
                    }
                }
            }
        }
    }

    /**
     * Test reading data from a DeflateCompressorInputStream with incorrect zlib header.
     *
     * @throws IOException when an error occurs while reading or writing
     */
    @Test(expected = DataFormatException.class)
    public void testIncorrectZlibHeader() throws IOException {
        String inputData = "Hello, World!";
        byte[] zlibHeader = new byte[]{0x78, 0x5E, 0x01, 0x01, 0x14}; // FLAGS: LZW, wrong checksum

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(zlibHeader)) {
            try (ByteArrayInputStream dataStream = new ByteArrayInputStream(inputData.getBytes())) {
                try (DeflateCompressorInputStream compressedStream = new DeflateCompressorInputStream(dataStream)) {
                    try (DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters())) {
                        byte[] buffer = new byte[inputData.length()];
                        int length;

                        while ((length = testStream.read(buffer)) > 0) {
                            compressedStream.read(buffer, 0, length);
                        }

                        Assert.fail("Expected DataFormatException but received valid data");
                    }
                }
            }
        }
    }
}