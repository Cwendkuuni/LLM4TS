import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class ArchiveStreamFactoryTest {

 @Test
 public void testCreateArchiveInputStream() throws Exception {
 // Test creating ArchiveInputStreams from known archive names and byte arrays
 InputStream arInputStream = new ByteArrayInputStream(new byte[100]);
 ArchiveInputStream arArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
 assertNotNull(arArchiveInputStream);
 arArchiveInputStream.close();

 InputStream cpioInputStream = new ByteArrayInputStream(new byte[100]);
 ArchiveInputStream cpioArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
 assertNotNull(cpioArchiveInputStream);
 cpioArchiveInputStream.close();

 InputStream jarInputStream = new ByteArrayInputStream(new byte[100]);
 ArchiveInputStream jarArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
 assertNotNull(jarArchiveInputStream);
 jarArchiveInputStream.close();

 InputStream tarInputStream = new ByteArrayInputStream(new byte[100]);
 ArchiveInputStream tarArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
 assertNotNull(tarArchiveInputStream);
 tarArchiveInputStream.close();

 InputStream zipInputStream = new ByteArrayInputStream(new byte[100]);
 ArchiveInputStream zipArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
 assertNotNull(zipArchiveInputStream);
 zipArchiveInputStream.close();

 // Test creating ArchiveInputStream from an unknown archive name
 InputStream unknownInputStream = new ByteArrayInputStream(new byte[100]);
 try {
 ArchiveInputStream unknownArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream("unknown", unknownInputStream);
 assert (false); // ArchiveException expected
 } catch (ArchiveException e) {
 // expected
 }
 unknownArchiveInputStream.close();
 }

 @Test
 public void testCreateArchiveOutputStream() throws Exception {
 // Test creating ArchiveOutputStreams from known archive names and OutputStreams
 OutputStream arOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream arArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
 arArchiveOutputStream.finish();
 assertNotNull(arArchiveOutputStream.getInputStream());
 arArchiveOutputStream.close();

 OutputStream cpioOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream cpioArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
 cpioArchiveOutputStream.finish();
 assertNotNull(cpioArchiveOutputStream.getInputStream());
 cpioArchiveOutputStream.close();

 OutputStream jarOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream jarArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
 jarArchiveOutputStream.finish();
 assertNotNull(jarArchiveOutputStream.getInputStream());
 jarArchiveOutputStream.close();

 OutputStream tarOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream tarArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
 tarArchiveOutputStream.finish();
 assertNotNull(tarArchiveOutputStream.getInputStream());
 tarArchiveOutputStream.close();

 OutputStream zipOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream zipArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
 zipArchiveOutputStream.finish();
 assertNotNull(zipArchiveOutputStream.getInputStream());
 zipArchiveOutputStream.close();

 // Test creating ArchiveOutputStream from an unknown archive name
 OutputStream unknownOutputStream = new ByteArrayOutputStream();
 try {
 ArchiveOutputStream unknownArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream("unknown", unknownOutputStream);
 assert (false); // ArchiveException expected
 } catch (ArchiveException e) {
 // expected
 }
 unknownArchiveOutputStream.close();
 }
}