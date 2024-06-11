import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

 @Before
 public void setUp() {
 // Set up any resources needed before each test
 }

 @After
 public void tearDown() {
 // Clean up any resources used during the test
 }

 /**
 * Test createArchiveInputStream() with known archiver name
 */
 @Test
 public void testCreateArchiveInputStreamWithKnownArchiverName() throws Exception {
 final byte[] arSignature = new byte[] { 0x78, 0x41, 0x78, 0x4D, 0x0A, 0x1A, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
 final byte[] tarSignature = new byte[] { 0x00, 0x00, 0x14, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
 final byte[] zipSignature = new byte[] { 0x50, 0x4B, 0x03, 0x04, 0x14, 0x00, 0x00, 0x00, 0x02, 0x00, 0x01, 0x0B, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19 };
 final byte[] jarSignature = new byte[] { 0x00, 0x02, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x69, 0x6D, 0x61, 0x72, 0x6C, 0x65, 0x61, 0x74, 0x65, 0x72, 0x00, 0x6A, 0x50, 0x2E, 0x62, 0x6A, 0x86, 0x53, 0x74, 0x72, 0x61, 0x3E };

 InputStream in = new ByteArrayInputStream(arSignature);
 ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, in);
 assertEquals("AR archive", ais.getFormat());
 ais.close();
 in = new ByteArrayInputStream(tarSignature);
 ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, in);
 assertEquals("TAR archive", ais.getFormat());
 ais.close();
 in = new ByteArrayInputStream(zipSignature);
 ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
 assertEquals("ZIP archive", ais.getFormat());
 ais.close();
 in = new ByteArrayInputStream(jarSignature);
 ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, in);
 assertEquals("JAR archive", ais.getFormat());
 ais.close();
 }

 /**
 * Test createArchiveInputStream() with unknown archiver name
 */
 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamWithUnknownArchiverName() throws Exception {
 InputStream in = new ByteArrayInputStream(new byte[] { 0x00, 0x01, 0x02 });
 new ArchiveStreamFactory().createArchiveInputStream("unknown", in);
 }

 /**
 * Test createArchiveOutputStream() with known archiver name
 */
 @Test
 public void testCreateArchiveOutputStreamWithKnownArchiverName() throws Exception {
 OutputStream out = new ByteArrayOutputStream();
 ArchiveOutputStream oos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, out);
 assertNotNull("AR archive output stream", oos);
 oos.close();
 out = new ByteArrayOutputStream();
 oos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
 assertNotNull("TAR archive output stream", oos);
 oos.close();
 out = new ByteArrayOutputStream();
 oos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
 assertNotNull("ZIP archive output stream", oos);
 oos.close();
 out = new ByteArrayOutputStream();
 oos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
 assertNotNull("JAR archive output stream", oos);
 oos.close();
 }

 /**
 * Test createArchiveOutputStream() with unknown archiver name
 */
 @Test(expected = ArchiveException.class)
 public void testCreateArchiveOutputStreamWithUnknownArchiverName() throws Exception {
 OutputStream out = new ByteArrayOutputStream();
 new ArchiveStreamFactory().createArchiveOutputStream("unknown", out);
 }

 /**
 * Test createArchiveInputStream() with null stream
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveInputStreamWithNullStream() throws Exception {
 new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, null);
 }

 /**
 * Test createArchiveOutputStream() with null stream
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveOutputStreamWithNullStream() throws Exception {
 new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
 }

 /**
 * Test createArchiveInputStream() with mark not supported stream
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveInputStreamWithMarkNotSupportedStream() throws Exception {
 InputStream in = new ByteArrayInputStream(new byte[] { 0x00 });
 in.mark(1);
 in.reset();
 new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, in);
 }

 /**
 * Test createArchiveInputStream() autodetection with known archive formats
 */
 @Test
 public void testCreateArchiveInputStreamAutodetection() throws Exception {
 final byte[] arHeader = new byte[] { 0x78, 0x41, 0x78, 0x4D, 0x0A, 0x1A, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
 final byte[] tarHeader = new byte[] { 0x00, 0x00, 0x14, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
 final byte[] zipHeader = new byte[] { 0x50, 0x4B, 0x03, 0x04, 0x14, 0x00, 0x00, 0x00, 0x02, 0x00, 0x01, 0x0B, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19 };
 final byte[] data = new byte[] { 0x01, 0x02, 0x03 };

 InputStream input = new ByteArrayInputStream(arHeader);
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 ArchiveInputStream ais = factory.createArchiveInputStream(input);
 assertEquals(ArArchiveInputStream.class, ais.getClass());
 input = new ByteArrayInputStream(tarHeader);
 ais = factory.createArchiveInputStream(input);
 assertEquals(TarArchiveInputStream.class, ais.getClass());
 input = new ByteArrayInputStream(zipHeader);
 ais = factory.createArchiveInputStream(input);
 assertEquals(ZipArchiveInputStream.class, ais.getClass());

 input = new ByteArrayInputStream(new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F, 0x20, 0x21, 0x22, 0x23 });
 ais = factory.createArchiveInputStream(input);
 assertEquals(ZipArchiveInputStream.class, ais.getClass());

 input = new ByteArrayInputStream(new byte[] { 0x00, 0x02, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x69, 0x6D, 0x61, 0x72, 0x6C, 0x65, 0x61, 0x74, 0x65, 0x72, 0x00, 0x6A, 0x50, 0x2E, 0x62, 0x6A, 0x86, 0x53, 0x74, 0x72, 0x61, 0x3E });
 ais = factory.createArchiveInputStream(input);
 assertEquals(JarArchiveInputStream.class, ais.getClass());

 input = new ByteArrayInputStream(new byte[] { 0x00, 0x01, 0x02, 0x03 });
 ais = factory.createArchiveInputStream(input);
 assertEquals(ZipArchiveInputStream.class, ais.getClass());

 // Reset the input stream and check the next entry
 input.reset();
 ArchiveEntry entry = ais.getNextEntry();
 assertNotNull("Next entry should not be null", entry);
 assertEquals("test.txt", entry.getName());

 // Read the data
 byte[] buffer = new byte[1024];
 int bytesRead = input.read(buffer, 0, buffer.length);
 assertEquals(data.length, bytesRead);
 assertArrayEquals(data, Arrays.copyOfRange(buffer, 0, data.length));

 // Check that the end of the archive has been reached
 assertFalse("End of the archive has not been reached", ais.hasNextEntry());

 ais.close();
 input.close();
 }

 /**
 * Test createArchiveInputStream() autodetection with unknown archive format
 */
 @Test
 public void testCreateArchiveInputStreamAutodetectionWithUnknownFormat() throws Exception {
 InputStream input = new ByteArrayInputStream(new byte[] { 0x00, 0x01, 0x02, 0x03 });
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 ArchiveInputStream ais = factory.createArchiveInputStream(input);
 assertEquals(ZipArchiveInputStream.class, ais.getClass());

 input.mark(1);
 try {
 ArchiveEntry entry = ais.getNextEntry();
 fail("Next entry should not be null");
 } catch (IOException e) {
 // Expected
 }
 input.reset();

 ais.close();
 input.close();
 }
}