import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.DefaultDecompressor;
import org.apache.commons.compress.compressors.DefaultCompressor;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test for CompressorStreamFactory class
 */
public class CompressorStreamFactoryTest {

 private CompressorStreamFactory factory;
 private CompressorInputStream inputStream;
 private CompressorOutputStream outputStream;

 @Before
 public void setUp() {
 factory = new CompressorStreamFactory();
 }

 /**
 * Test creating compressor input stream from an input stream with known compressor type
 */
 @Test
 public void testCreateCompressorInputStreamWithKnownType() throws IOException, CompressorException {
 byte[] gzipSignature = new byte[] {(byte) 0x47, (byte) 0x49, (byte) 0x46, (byte) 0x37, (byte) 0x32, (byte) 0x53, (byte) 0x42, (byte) 0x53, (byte) 0x54, (byte) 0x45, (byte) 0x54};
 byte[] bzip2Signature = new byte[] {(byte) 0x42, (byte) 0x5A, (byte) 0x4D, (byte) 0x4C, (byte) 0x4C, (byte) 0x31, (byte) 0x42, (byte) 0x5A, (byte) 0x53, (byte) 0x42};

 ByteArrayInputStream gzipTestInputStream = new ByteArrayInputStream(gzipSignature);
 ByteArrayInputStream bzip2TestInputStream = new ByteArrayInputStream(bzip2Signature);

 inputStream = factory.createCompressorInputStream("GZIP", gzipTestInputStream);
 Assert.assertEquals("Expected GZIP compressor", "gzip", inputStream.getName());

 inputStream = factory.createCompressorInputStream("BZIP2", bzip2TestInputStream);
 Assert.assertEquals("Expected BZIP2 compressor", "bzip2", inputStream.getName());
 }

 /**
 * Test creating compressor input stream from an input stream with unknown compressor type
 */
 @Test
 public void testCreateCompressorInputStreamWithUnknownType() throws IOException, CompressorException {
 ByteArrayInputStream unknownTestInputStream = new ByteArrayInputStream(new byte[] {});

 try {
 inputStream = factory.createCompressorInputStream("UNKNOWN", unknownTestInputStream);
 Assert.fail("Expected CompressorException to be thrown");
 } catch (CompressorException e) {
 // Expected result
 }
 }

 /**
 * Test creating compressor output stream with a known compressor name
 */
 @Test
 public void testCreateCompressorOutputStreamWithKnownType() throws IOException, CompressorException {
 outputStream = factory.createCompressorOutputStream("GZIP", new ByteArrayOutputStream());
 Assert.assertEquals("Expected GZIP compressor", "gzip", outputStream.getName());
 }

 /**
 * Test creating compressor output stream with an unknown compressor name
 */
 @Test
 public void testCreateCompressorOutputStreamWithUnknownType() throws IOException, CompressorException {
 try {
 outputStream = factory.createCompressorOutputStream("UNKNOWN", new ByteArrayOutputStream());
 Assert.fail("Expected CompressorException to be thrown");
 } catch (CompressorException e) {
 // Expected result
 }
 }

 /**
 * Test decompression of compressed data
 */
 @Test
 public void testDecompression() throws IOException, CompressorException {
 ByteArrayOutputStream gzipTestOutputStream = new ByteArrayOutputStream();
 factory.createCompressorOutputStream("GZIP", gzipTestOutputStream).write("Test data".getBytes());
 gzipTestOutputStream.close();

 inputStream = factory.createCompressorInputStream("GZIP", new ByteArrayInputStream(gzipTestOutputStream.toByteArray()));
 Assert.assertEquals("Decompressed data", "Test data", IOUtils.toString(inputStream));
 }

 /**
 * Test decompression of invalid compressed data
 */
 @Test
 public void testDecompressionWithInvalidData() throws IOException, CompressorException {
 byte[] invalidData = new byte[] {(byte) 0x01, (byte) 0x02};

 inputStream = factory.createCompressorInputStream("GZIP", new ByteArrayInputStream(invalidData));
 try {
 String decompressedData = IOUtils.toString(inputStream);
 Assert.fail("Expected CompressorException to be thrown");
 } catch (CompressorException e) {
 // Expected result
 }
 }

 /**
 * Test setting compressor factory to decompress concatenated files
 */
 @Test
 public void testSetDecompressConcatenated() {
 factory = new CompressorStreamFactory(true);
 Assert.assertTrue("Expected decompressConcatenated to be set to true", factory.getDecompressConcatenated());
 }

 /**
 * Test setting compressor factory to not decompress concatenated files
 */
 @Test
 public void testSetDecompressConcatenatedToFalse() {
 factory = new CompressorStreamFactory(false);
 Assert.assertFalse("Expected decompressConcatenated to be set to false", factory.getDecompressConcatenated());
 }
}