import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ArchiveStreamFactoryTest {

 @Test
 public void testCreateArchiveInputStream() throws Exception {
 final String archiveNames[] = {ArchiveStreamFactory.AR, ArchiveStreamFactory.CPIO, ArchiveStreamFactory.DUMP,
 ArchiveStreamFactory.JAR, ArchiveStreamFactory.TAR, ArchiveStreamFactory.ZIP};
 final InputStream testStream = new ByteArrayInputStream(new byte[1024]);

 for (String archiveName : archiveNames) {
 try {
 ArchiveInputStream in = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiveName, testStream);
 assertEquals("Unexpected archive type for given name.", archiveName, in.getFormat().getName());
 assertNotNull("ArchiveInputStream must not be null.", in);
 } catch (ArchiveException e) {
 // expected for not supported archive types
 }
 }
 }

 @Test
 public void testCreateArchiveInputStream_UnsupportedArchiveFormat() throws Exception {
 final String unsupportedFormat = "unexpected_format";
 final InputStream testStream = new ByteArrayInputStream(new byte[1024]);

 try {
 ArchiveInputStream in = ArchiveStreamFactory.newInstance().createArchiveInputStream(unsupportedFormat, testStream);
 fail("Expected ArchiveException.");
 } catch (ArchiveException e) {
 //expected
 }
 }

 @Test
 public void testCreateArchiveInputStream_NullInputStream() {
 final String archiveName = ArchiveStreamFactory.ZIP;

 try {
 ArchiveInputStream in = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiveName, null);
 fail("Expected IllegalArgumentException.");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test
 public void testCreateArchiveOutputStream() throws Exception {
 final String archiveNames[] = {ArchiveStreamFactory.AR, ArchiveStreamFactory.CPIO, ArchiveStreamFactory.DUMP,
 ArchiveStreamFactory.JAR, ArchiveStreamFactory.TAR, ArchiveStreamFactory.ZIP};
 final OutputStream testOutputStream = new ByteArrayOutputStream();

 for (String archiveName : archiveNames) {
 ArchiveOutputStream out = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiveName, testOutputStream);
 assertEquals("Unexpected archive type for given name.", archiveName, out.getFormat().getName());
 assertNotNull("ArchiveOutputStream must not be null.", out);
 }
 }

 @Test
 public void testCreateArchiveOutputStream_UnsupportedArchiveFormat() {
 final String unsupportedFormat = "unexpected_format";
 final OutputStream testOutputStream = new ByteArrayOutputStream();

 try {
 ArchiveOutputStream out = ArchiveStreamFactory.newInstance().createArchiveOutputStream(unsupportedFormat, testOutputStream);
 fail("Expected ArchiveException.");
 } catch (ArchiveException e) {
 //expected
 }
 }

 @Test
 public void testCreateArchiveOutputStream_NullOutputStream() {
 final String archiveName = ArchiveStreamFactory.ZIP;

 try {
 ArchiveOutputStream out = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiveName, null);
 fail("Expected IllegalArgumentException.");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test
 public void testCreateArchiveInputStream_UnknownArchiveType() throws Exception {
 final InputStream testStream = new ByteArrayInputStream(new byte[1024]);

 try {
 ArchiveInputStream in = ArchiveStreamFactory.newInstance().createArchiveInputStream("unknown_format", testStream);
 assertNull("ArchiveInputStream must be null.", in);
 } catch (ArchiveException e) {
 //expected
 }
 }

 @Test
 public void testCreateArchiveOutputStream_NoMarkerSupported() {
 final InputStream testStream = new ByteArrayInputStream(new byte[1024]);

 try {
 ArchiveInputStream in = ArchiveStreamFactory.newInstance().createArchiveInputStream(testStream);
 fail("Expected IOException as InputStream does not support marker.");
 } catch (IOException e) {
 //expected
 }
 }
}

class ArchiveStreamFactoryTest_Instance {
 private static final ArchiveStreamFactory INSTANCE = new ArchiveStreamFactory();
}