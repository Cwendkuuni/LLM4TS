import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

 private ArchiveStreamFactory factory;
 private byte[] testData;

 @Before
 public void setUp() {
 factory = new ArchiveStreamFactory();
 testData = new byte[1024];
 }

 @Test
 public void testCreateArchiveInputStream_AR() throws Exception {
 InputStream input = new ByteArrayInputStream(testData);
 ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("ar", input);
 assertTrue(archiveInputStream instanceof ArArchiveInputStream);
 }

 @Test(expected = StreamingNotSupportedException.class)
 public void testCreateArchiveInputStream_ARJ() throws Exception {
 InputStream input = new ByteArrayInputStream(testData);
 factory.createArchiveInputStream("arj", input);
 }

 @Test
 public void testCreateArchiveInputStream_CPIO() throws Exception {
 InputStream input = new ByteArrayInputStream(testData);
 ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("cpio", input);
 assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
 }

 @Test(expected = StreamingNotSupportedException.class)
 public void testCreateArchiveInputStream_DUMP() throws Exception {
 InputStream input = new ByteArrayInputStream(testData);
 factory.createArchiveInputStream("dump", input);
 }

 @Test
 public void testCreateArchiveInputStream_JAR() throws Exception {
 InputStream input = new ByteArrayInputStream(testData);
 ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("jar", input);
 assertTrue(archiveInputStream instanceof JarArchiveInputStream);
 }

 @Test
 public void testCreateArchiveInputStream_SEVEN_Z() throws Exception {
 InputStream input = new ByteArrayInputStream(testData);
 try {
 factory.createArchiveInputStream("7z", input);
 fail("Expected StreamingNotSupportedException");
 } catch (StreamingNotSupportedException e) {
 // expected
 }
 }

 @Test
 public void testCreateArchiveInputStream_TAR() throws Exception {
 InputStream input = new ByteArrayInputStream(testData);
 ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("tar", input);
 assertTrue(archiveInputStream instanceof TarArchiveInputStream);
 }

 @Test
 public void testCreateArchiveInputStream_ZIP() throws Exception {
 InputStream input = new ByteArrayInputStream(testData);
 ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("zip", input);
 assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
 }

 @Test
 public void testCreateArchiveOutputStream_AR() throws Exception {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream("ar", output);
 assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);
 archiveOutputStream.finish();
}

 @Test(expected = StreamingNotSupportedException.class)
 public void testCreateArchiveOutputStream_ARJ() throws Exception {
 OutputStream output = new ByteArrayOutputStream();
 factory.createArchiveOutputStream("arj", output);
 }

 @Test
 public void testCreateArchiveOutputStream_CPIO() throws Exception {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream("cpio", output);
 assertTrue(archiveOutputStream instanceof CpioArchiveOutputStream);
 archiveOutputStream.finish();
 }

 @Test(expected = StreamingNotSupportedException.class)
 public void testCreateArchiveOutputStream_DUMP() throws Exception {
 OutputStream output = new ByteArrayOutputStream();
 factory.createArchiveOutputStream("dump", output);
 }

 @Test
 public void testCreateArchiveOutputStream_JAR() throws Exception {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream("jar", output);
 assertTrue(archiveOutputStream instanceof JarArchiveOutputStream);
 archiveOutputStream.finish();
 }

 @Test
 public void testCreateArchiveOutputStream_SEVEN_Z() throws Exception {
 OutputStream output = new ByteArrayOutputStream();
 try {
 factory.createArchiveOutputStream("7z", output);
 fail("Expected StreamingNotSupportedException");
 } catch (StreamingNotSupportedException e) {
 // expected
 }
 }

 @Test
 public void testCreateArchiveOutputStream_TAR() throws Exception {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream("tar", output);
 assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
 archiveOutputStream.finish();
 }

 @Test
 public void testCreateArchiveOutputStream_ZIP() throws Exception {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream("zip", output);
 assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
 archiveOutputStream.finish();
 }

 @Test
 public void testCreateArchiveInputStream_UnknownFormat() throws Exception {
 InputStream input = new ByteArrayInputStream(testData);
 try {
 factory.createArchiveInputStream("unknown", input);
 fail("Expected ArchiveException");
 } catch (ArchiveException e) {
 // expected
 }
 }

 @Test
 public void testCreateArchiveOutputStream_NullInput() throws Exception {
 try {
 factory.createArchiveOutputStream("tar", null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCreateArchiveInputStream_NullOutput() throws Exception {
 try {
 factory.createArchiveOutputStream("tar", null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

}