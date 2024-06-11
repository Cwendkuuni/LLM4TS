import org.apache.commons.compress.*;
import org.apache.commons.compress.compressors.*;
import org.apache.commons.compress.compressors.utils.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CompressorStreamFactoryTest {

 private InputStream inputStream;
 private OutputStream outputStream;

 @Before
 public void setUp() throws Exception {
 inputStream = new ByteArrayInputStream(new byte[0]);
 outputStream = new ByteArrayOutputStream();
 }

 @After
 public void tearDown() throws Exception {
 IOUtils.closeQuietly(inputStream);
 IOUtils.closeQuietly(outputStream);
 }

 /**
 * Test case for creating a compressor input stream from an input stream using autodetection.
 */
 @Test
 public void testCreateCompressorInputStreamAutoDetection() throws Exception {
 CompressorFactory compressorFactory = new CompressorFactory();
 CompressorInputStream compressorInputStream = compressorFactory.createCompressorInputStream("unknown", inputStream);
 assertNull(compressorInputStream);

 // Test Gzip compression
 inputStream = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
 compressorInputStream = compressorFactory.createCompressorInputStream("gzip", inputStream);
 assertNotNull(compressorInputStream);
 assertTrue(compressorInputStream instanceof GzipCompressorInputStream);

 // Test Bzip2 compression
 inputStream = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_STREAM_SIGNATURE);
 compressorInputStream = compressorFactory.createCompressorInputStream("bzip2", inputStream);
 assertNotNull(compressorInputStream);
 assertTrue(compressorInputStream instanceof BZip2CompressorInputStream);

 // Test XZ compression
 inputStream = new ByteArrayInputStream(XZCompressorInputStream.XZ_STREAM_SIGNATURE);
 compressorInputStream = compressorFactory.createCompressorInputStream("xz", inputStream);
 assertNotNull(compressorInputStream);
 assertTrue(compressorInputStream instanceof XZCompressorInputStream);

 // Test Unsupported compression
 inputStream = new ByteArrayInputStream(new byte[0]);
 compressorInputStream = compressorFactory.createCompressorInputStream("unsupported", inputStream);
 assertNull(compressorInputStream);
 }

 /**
 * Test case for creating a compressor input stream from a compressor name and an input stream.
 */
 @Test
 public void testCreateCompressorInputStreamByName() throws Exception {
 CompressorFactory compressorFactory = new CompressorFactory();

 // Test Gzip compression
 InputStream gzipInputStream = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_STREAM_DATA);
 CompressorInputStream compressorInputStream = compressorFactory.createCompressorInputStream("gzip", gzipInputStream);
 assertNotNull(compressorInputStream);
 assertTrue(compressorInputStream instanceof GzipCompressorInputStream);

 // Test Bzip2 compression
 InputStream bzip2InputStream = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_STREAM_DATA);
 compressorInputStream = compressorFactory.createCompressorInputStream("bzip2", bzip2InputStream);
 assertNotNull(compressorInputStream);
 assertTrue(compressorInputStream instanceof BZip2CompressorInputStream);

 // Test XZ compression
 InputStream xzInputStream = new ByteArrayInputStream(XZCompressorInputStream.XZ_STREAM_DATA);
 compressorInputStream = compressorFactory.createCompressorInputStream("xz", xzInputStream);
 assertNotNull(compressorInputStream);
 assertTrue(compressorInputStream instanceof XZCompressorInputStream);

 // Test Unsupported compression
 InputStream unsupportedInputStream = new ByteArrayInputStream(new byte[0]);
 compressorInputStream = compressorFactory.createCompressorInputStream("unsupported", unsupportedInputStream);
 assertNull(compressorInputStream);
 }

 /**
 * Test case for creating a compressor output stream from a compressor name and an output stream.
 */
 @Test
 public void testCreateCompressorOutputStream() throws Exception {
 CompressorFactory compressorFactory = new CompressorFactory();

 // Test Gzip compression
 CompressorOutputStream compressorOutputStream = compressorFactory.createCompressorOutputStream("gzip", outputStream);
 assertNotNull(compressorOutputStream);
 assertTrue(compressorOutputStream instanceof GzipCompressorOutputStream);

 // Test Bzip2 compression
 compressorOutputStream = compressorFactory.createCompressorOutputStream("bzip2", outputStream);
 assertNotNull(compressorOutputStream);
 assertTrue(compressorOutputStream instanceof BZip2CompressorOutputStream);

 // Test XZ compression
 compressorOutputStream = compressorFactory.createCompressorOutputStream("xz", outputStream);
 assertNotNull(compressorOutputStream);
 assertTrue(compressorOutputStream instanceof XZCompressorOutputStream);

 // Test Unsupported compression
 compressorOutputStream = compressorFactory.createCompressorOutputStream("unsupported", outputStream);
 assertNull(compressorOutputStream);
 }

 /**
 * Test case for decompressing a compressed input stream using autodetection.
 */
 @Test
 public void testDecompressCompressorInputStreamAutoDetection() throws Exception {
 // Test Gzip decompression
 CompressorFactory compressorFactory = new CompressorFactory();
 CompressorInputStream compressorInputStream = compressorFactory.createCompressorInputStream("gzip", inputStream);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 compressorFactory.createCompressorOutputStream("gzip", outputStream).write(compressorInputStream.readAllBytes());
 byte[] expectedOutput = new byte[15];
 System.arraycopy(GzipCompressorOutputStream.GZIP_STREAM_DATA, 0, expectedOutput, 0, expectedOutput.length);
 assertArrayEquals(expectedOutput, outputStream.toByteArray());

 // Test Bzip2 decompression
 compressorInputStream = compressorFactory.createCompressorInputStream("bzip2", inputStream);
 outputStream = new ByteArrayOutputStream();
 compressorFactory.createCompressorOutputStream("bzip2", outputStream).write(compressorInputStream.readAllBytes());
 expectedOutput = new byte[BZip2CompressorInputStream.BZIP2_STREAM_DATA.length];
 System.arraycopy(BZip2CompressorInputStream.BZIP2_STREAM_DATA, 0, expectedOutput, 0, expectedOutput.length);
 assertArrayEquals(expectedOutput, outputStream.toByteArray());

 // Test XZ decompression
 compressorInputStream = compressorFactory.createCompressorInputStream("xz", inputStream);
 outputStream = new ByteArrayOutputStream();
 compressorFactory.createCompressorOutputStream("xz", outputStream).write(compressorInputStream.readAllBytes());
 expectedOutput = new byte[XZCompressorInputStream.XZ_STREAM_DATA.length];
 System.arraycopy(XZCompressorInputStream.XZ_STREAM_DATA, 0, expectedOutput, 0, expectedOutput.length);
 assertArrayEquals(expectedOutput, outputStream.toByteArray());

 // Test Unsupported compression
 compressorInputStream = compressorFactory.createCompressorInputStream("unsupported", inputStream);
 ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
 compressorFactory.createCompressorOutputStream("gzip", errorStream).write(compressorInputStream.readAllBytes());
 assertEquals(0, errorStream.size());
 }

 /**
 * Test case for decompressing a compressed input stream by name.
 */
 @Test
 public void testDecompressCompressorInputStreamByName() throws Exception {
 // Test Gzip decompression
 CompressorFactory compressorFactory = new CompressorFactory();
 CompressorInputStream compressorInputStream = compressorFactory.createCompressorInputStream("gzip", inputStream);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 compressorFactory.createCompressorOutputStream("gzip", outputStream).write(compressorInputStream.readAllBytes());
 byte[] expectedOutput = new byte[15];
 System.arraycopy(GzipCompressorInputStream.GZIP_STREAM_DATA, 0, expectedOutput, 0, expectedOutput.length);
 assertArrayEquals(expectedOutput, outputStream.toByteArray());

 // Test Bzip2 decompression
 compressorInputStream = compressorFactory.createCompressorInputStream("bzip2", inputStream);
 outputStream = new ByteArrayOutputStream();
 compressorFactory.createCompressorOutputStream("bzip2", outputStream).write(compressorInputStream.readAllBytes());
 byte[] expectedErrorOutput = new byte[BZip2CompressorInputStream.BZIP2_STREAM_DATA.length];
 System.arraycopy(BZip2CompressorInputStream.BZIP2_STREAM_DATA, 0, expectedErrorOutput, 0, expectedErrorOutput.length);
 assertArrayNotEquals(expectedOutput, outputStream.toByteArray());

 // Test XZ decompression
 compressorInputStream = compressorFactory.createCompressorInputStream("xz", inputStream);
 outputStream = new ByteArrayOutputStream();
 compressorFactory.createCompressorOutputStream("xz", outputStream).write(compressorInputStream.readAllBytes());
 byte[] expectedXzOutput = new byte[XZCompressorInputStream.XZ_STREAM_DATA.length];
 System.arraycopy(XZCompressorInputStream.XZ_STREAM_DATA, 0, expectedXzOutput, 0, expectedXzOutput.length);
 assertArrayEquals(expectedXzOutput, outputStream.toByteArray());

 // Test Unsupported compression
 compressorInputStream = compressorFactory.createCompressorInputStream("unsupported", inputStream);
 ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
 compressorFactory.createCompressorOutputStream("gzip", errorStream).write(compressorInputStream.readAllBytes());
 assertEquals(0, errorStream.size());
 }
}