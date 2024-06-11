import org.apache.commons.compress.archivers.*;// Import all archivers for testing
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for ArchiveStreamFactory
 */
public class ArchiveStreamFactoryTest {

 private ArchiveStreamFactory factory;
 @Before
 public void setUp() {
 factory = new ArchiveStreamFactory();
 }

 /**
 * Test createArchiveInputStream() with known archive types.
 */
 @Test
 public void testCreateArchiveInputStreamWithKnownArchiveTypes() throws Exception {
 InputStream testStream = new ByteArrayInputStream(new byte[0]);
 // Test AR archive
 ArchiveInputStream arInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, testStream);
 assertTrue(arInputStream instanceof ArArchiveInputStream);
 arInputStream.close();

 // Test CPIO archive
 testStream = new ByteArrayInputStream(new byte[0]);
 ArchiveInputStream cpioInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, testStream);
 assertTrue(cpioInputStream instanceof CpioArchiveInputStream);
 cpioInputStream.close();

 // Test DUMP archive
 testStream = new ByteArrayInputStream(new byte[0]);
 ArchiveInputStream dumpInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, testStream);
 assertTrue(dumpInputStream instanceof DumpArchiveInputStream);
 dumpInputStream.close();

 // Test JAR archive
 testStream = new ByteArrayInputStream(new byte[0]);
 ArchiveInputStream jarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, testStream);
 assertTrue(jarInputStream instanceof JarArchiveInputStream);
 jarInputStream.close();

 // Test TAR archive
 testStream = new ByteArrayInputStream(new byte[0]);
 ArchiveInputStream tarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, testStream);
 assertTrue(tarInputStream instanceof TarArchiveInputStream);
 tarInputStream.close();

 // Test ZIP archive
 testStream = new ByteArrayInputStream(new byte[0]);
 ArchiveInputStream zipInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, testStream);
 assertTrue(zipInputStream instanceof ZipArchiveInputStream);
 zipInputStream.close();
 }

 /**
 * Test createArchiveInputStream() with invalid archiver name.
 */
 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamWithInvalidArchiverName() throws Exception {
 InputStream testStream = new ByteArrayInputStream(new byte[0]);
 factory.createArchiveInputStream("invalid", testStream);
 }

 /**
 * Test createArchiveOutputStream() with known archive types.
 */
 @Test
 public void testCreateArchiveOutputStreamWithKnownArchiveTypes() throws Exception {
 OutputStream testStream = new ByteArrayOutputStream();
 ArchiveOutputStream arOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, testStream);
 assertTrue(arOutputStream instanceof ArArchiveOutputStream);
 arOutputStream.finish();
 testStream.close();

 // Test CPIO archive
 testStream = new ByteArrayOutputStream();
 ArchiveOutputStream cpioOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, testStream);
 assertTrue(cpioOutputStream instanceof CpioArchiveOutputStream);
 cpioOutputStream.finish();
 testStream.close();

 // Test DUMP archive
 testStream = new ByteArrayOutputStream();
 ArchiveOutputStream dumpOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, testStream);
 assertTrue(dumpOutputStream instanceof DumpArchiveOutputStream);
 dumpOutputStream.finish();
 testStream.close();

 // Test JAR archive
 testStream = new ByteArrayOutputStream();
 ArchiveOutputStream jarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, testStream);
 assertTrue(jarOutputStream instanceof JarArchiveOutputStream);
 jarOutputStream.putNextEntry(new JarArchiveEntry("test.jar"));
 jarOutputStream.finish();
 testStream.close();

 // Test TAR archive
 testStream = new ByteArrayOutputStream();
 ArchiveOutputStream tarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, testStream);
 tarOutputStream.putNextEntry(new TarEntry("test.tar"));
 tarOutputStream.finish();
 testStream.close();

 // Test ZIP archive
 testStream = new ByteArrayOutputStream();
 ArchiveOutputStream zipOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, testStream);
 zipOutputStream.putNextEntry(new ZipArchiveEntry("test.zip"));
 zipOutputStream.finish();
 testStream.close();
 }

 /**
 * Test createArchiveInputStream() with unknown archive types.
 */
 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamWithUnknownArchiveTypes() throws Exception {
 InputStream testStream = new ByteArrayInputStream(new byte[0]);
 factory.createArchiveInputStream("unknown", testStream);
 }

 /**
 * Test createArchiveOutputStream() with null output stream.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStreamWithNullOutputStream() {
 try {
 factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 /**
 * Test createArchiveOutputStream() with known archive types and different compressors.
 */
 @Test
 public void testCreateArchiveOutputStreamWithKnownArchiveTypesAndCompressors() throws Exception {
 OutputStream testStream = new ByteArrayOutputStream();
 ArchiveOutputStream arOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, testStream);
 arOutputStream.setCompressMethod(CompressorStreamFactory.GZIP);
 arOutputStream.finish();
 testStream.close();

 // Test CPIO archive
 testStream = new ByteArrayOutputStream();
 ArchiveOutputStream cpioOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, testStream);
 cpioOutputStream.setCompressMethod(CompressorStreamFactory.GZIP);
 cpioOutputStream.finish();
 testStream.close();

 // Test DUMP archive
 testStream = new ByteArrayOutputStream();
 ArchiveOutputStream dumpOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, testStream);
 dumpOutputStream.setCompressMethod(CompressorStreamFactory.GZIP);
 dumpOutputStream.finish();
 testStream.close();

 // Test JAR archive
 testStream = new ByteArrayOutputStream();
 ArchiveOutputStream jarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, testStream);
 jarOutputStream.setCompressMethod(CompressorStreamFactory.GZIP);
 jarOutputStream.putNextEntry(new JarArchiveEntry("test.jar"));
 jarOutputStream.finish();
 testStream.close();

 // Test TAR archive
 testStream = new ByteArrayOutputStream();
 ArchiveOutputStream tarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, testStream);
 tarOutputStream.setCompressMethod(CompressorStreamFactory.GZIP);
 tarOutputStream.putNextEntry(new TarEntry("test.tar"));
 tarOutputStream.finish();
 testStream.close();

 // Test ZIP archive
 testStream = new ByteArrayOutputStream();
 ArchiveOutputStream zipOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, testStream);
 zipOutputStream.setCompressMethod(CompressorStreamFactory.GZIP);
 zipOutputStream.putNextEntry(new ZipArchiveEntry("test.zip"));
 zipOutputStream.finish();
 testStream.close();
 }
}