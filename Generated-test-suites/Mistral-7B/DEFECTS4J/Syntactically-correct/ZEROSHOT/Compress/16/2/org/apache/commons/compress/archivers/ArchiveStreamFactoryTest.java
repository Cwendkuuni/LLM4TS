import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
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
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

 @Test
 public void testCreateArchiveInputStream() throws Exception {
 ArchiveInputStream expectedArchiveStream = null;
 InputStream inputStream = null;

 // Test AR Archive Input Stream
 expectedArchiveStream = new ArArchiveInputStream(new ByteArrayInputStream(new byte[0]));
 String archiverName = ArchiveStreamFactory.AR;
 ArchiveInputStream actualArchiveStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
 assertEquals(expectedArchiveStream.getClass(), actualArchiveStream.getClass());

 // Test CPIO Archive Input Stream
 expectedArchiveStream = new CpioArchiveInputStream(new ByteArrayInputStream(new byte[0]));
 archiverName = ArchiveStreamFactory.CPIO;
 actualArchiveStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
 assertEquals(expectedArchiveStream.getClass(), actualArchiveStream.getClass());

 // Test DUMP Archive Input Stream
 expectedArchiveStream = new DumpArchiveInputStream(new ByteArrayInputStream(new byte[0]));
 archiverName = ArchiveStreamFactory.DUMP;
 actualArchiveStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
 assertEquals(expectedArchiveStream.getClass(), actualArchiveStream.getClass());

 // Test JAR Archive Input Stream
 expectedArchiveStream = new JarArchiveInputStream(new ByteArrayInputStream(new byte[0]));
 archiverName = ArchiveStreamFactory.JAR;
 actualArchiveStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
 assertEquals(expectedArchiveStream.getClass(), actualArchiveStream.getClass());

 // Test TAR Archive Input Stream
 expectedArchiveStream = new TarArchiveInputStream(new ByteArrayInputStream(new byte[0]));
 archiverName = ArchiveStreamFactory.TAR;
 actualArchiveStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
 assertEquals(expectedArchiveStream.getClass(), actualArchiveStream.getClass());

 // Test ZIP Archive Input Stream
 expectedArchiveStream = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[0]));
 archiverName = ArchiveStreamFactory.ZIP;
 actualArchiveStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
 assertEquals(expectedArchiveStream.getClass(), actualArchiveStream.getClass());
 }

 @Test
 public void testCreateArchiveOutputStream() throws Exception {
 OutputStream outputStream = null;
 ArchiveOutputStream expectedArchiveStream = null;

 // Test AR Archive Output Stream
 expectedArchiveStream = new ArArchiveOutputStream(outputStream);
 String archiverName = ArchiveStreamFactory.AR;
 ArchiveOutputStream actualArchiveStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream);
 assertEquals(expectedArchiveStream.getClass(), actualArchiveStream.getClass());

 // Test CPIO Archive Output Stream
 expectedArchiveStream = new CpioArchiveOutputStream(outputStream);
 archiverName = ArchiveStreamFactory.CPIO;
 actualArchiveStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream);
 assertEquals(expectedArchiveStream.getClass(), actualArchiveStream.getClass());

 // Test DUMP Archive Output Stream
 // DUMP format does not support output stream
 //
 // Test JAR Archive Output Stream
 expectedArchiveStream = new JarArchiveOutputStream(outputStream);
 archiverName = ArchiveStreamFactory.JAR;
 actualArchiveStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream);
 assertEquals(expectedArchiveStream.getClass(), actualArchiveStream.getClass());

 // Test TAR Archive Output Stream
 expectedArchiveStream = new TarArchiveOutputStream(outputStream);
 archiverName = ArchiveStreamFactory.TAR;
 actualArchiveStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream);
 assertEquals(expectedArchiveStream.getClass(), actualArchiveStream.getClass());

 // Test ZIP Archive Output Stream
 expectedArchiveStream = new ZipArchiveOutputStream(outputStream);
 archiverName = ArchiveStreamFactory.ZIP;
 actualArchiveStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream);
 assertEquals(expectedArchiveStream.getClass(), actualArchiveStream.getClass());
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamUnknownArchiver() throws Exception {
 ArchiveInputStream actualArchiveStream = null;
 InputStream inputStream = null;
 String archiverName = "unknown";
 actualArchiveStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
 fail("Expected ArchiveException but got: " + actualArchiveStream.getClass().getName());
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveOutputStreamUnknownArchiver() throws Exception {
 ArchiveOutputStream actualArchiveStream = null;
 OutputStream outputStream = null;
 String archiverName = "unknown";
 actualArchiveStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream);
 fail("Expected ArchiveException but got: " + actualArchiveStream.getClass().getName());
 }
}