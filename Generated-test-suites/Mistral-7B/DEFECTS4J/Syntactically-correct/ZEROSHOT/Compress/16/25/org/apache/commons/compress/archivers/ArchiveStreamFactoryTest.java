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
import static org.junit.Assert.assertThrows;

public class ArchiveStreamFactoryTest {

 @Test
 public void testCreateArchiveInputStream() throws Exception {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();

 // Test valid archives
 InputStream input = new ByteArrayInputStream(CpioArchiveInputStream.signature);
 ArchiveInputStream ais = factory.createArchiveInputStream("cpio", input);
 assert (ais instanceof CpioArchiveInputStream);
 ais.close();

 input = new ByteArrayInputStream(JarArchiveInputStream.signature);
 ais = factory.createArchiveInputStream("jar", input);
 assert (ais instanceof JarArchiveInputStream);
 ais.close();

 input = new ByteArrayInputStream(ArArchiveInputStream.signature);
 ais = factory.createArchiveInputStream("ar", input);
 assert (ais instanceof ArArchiveInputStream);
 ais.close();

 input = new ByteArrayInputStream(ZipArchiveInputStream.signature);
 ais = factory.createArchiveInputStream("zip", input);
 assert (ais instanceof ZipArchiveInputStream);
 ais.close();

 input = new ByteArrayInputStream(TarArchiveInputStream.signature);
 ais = factory.createArchiveInputStream("tar", input);
 assert (ais instanceof TarArchiveInputStream);
 ais.close();

 input = new ByteArrayInputStream(DumpArchiveInputStream.signature);
 ais = factory.createArchiveInputStream("dump", input);
 assert (ais instanceof DumpArchiveInputStream);
 ais.close();

 // Test invalid archives
 assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream("invalid", new ByteArrayInputStream(new byte[10])));
 }

 @Test
 public void testCreateArchiveOutputStream() throws Exception {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();

 // Test valid archives
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream oos = factory.createArchiveOutputStream("cpio", output);
 oos.close();

 output = new ByteArrayOutputStream();
 oos = factory.createArchiveOutputStream("jar", output);
 oos.close();

 output = new ByteArrayOutputStream();
 oos = factory.createArchiveOutputStream("ar", output);
 oos.close();

 output = new ByteArrayOutputStream();
 oos = factory.createArchiveOutputStream("zip", output);
 oos.close();

 output = new ByteArrayOutputStream();
 oos = factory.createArchiveOutputStream("tar", output);
 oos.close();

 // Test invalid archives
 assertThrows(ArchiveException.class, () -> factory.createArchiveOutputStream("invalid", new ByteArrayOutputStream()));
 }

 @Test
 public void testCreateArchiveInputStreamWithUnknownArchive() throws Exception {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 InputStream input = new ByteArrayInputStream(new byte[10]);
 assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream("unknown", input));
 }

 @Test
 public void testCreateArchiveOutputStreamWithUnknownArchive() throws Exception {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 OutputStream output = new ByteArrayOutputStream();
 assertThrows(ArchiveException.class, () -> factory.createArchiveOutputStream("unknown", output));
 }
}