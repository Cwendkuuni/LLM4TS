import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class ArchiveStreamFactoryTest {

 @Test
 public void testCreateArchiveInputStream() throws Exception {
 // Test with known archivers
 InputStream arInputStream = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream arArchiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
 assertNotNull(arArchiveInputStream);
 arArchiveInputStream.close();

 InputStream zipInputStream = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream zipArchiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
 assertNotNull(zipArchiveInputStream);
 zipArchiveInputStream.close();

 InputStream tarInputStream = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream tarArchiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
 assertNotNull(tarArchiveInputStream);
 tarArchiveInputStream.close();

 InputStream jarInputStream = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream jarArchiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
 assertNotNull(jarArchiveInputStream);
 jarArchiveInputStream.close();

 InputStream cpioInputStream = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream cpioArchiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
 assertNotNull(cpioArchiveInputStream);
 cpioArchiveInputStream.close();

 InputStream dumpInputStream = new ByteArrayInputStream(new byte[1024]);
 ArchiveInputStream dumpArchiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
 assertNotNull(dumpArchiveInputStream);
 dumpArchiveInputStream.close();

 // Test with unknown archiver
 InputStream unknownInputStream = new ByteArrayInputStream(new byte[1024]);
 try {
 ArchiveInputStream unknownArchiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("unknown", unknownInputStream);
 assert null : "Unexpected ArchiveInputStream creation.";
 } catch (ArchiveException e) {
 // Expected
 } finally {
 unknownInputStream.close();
 }
 }

 @Test
 public void testCreateArchiveOutputStream() throws Exception {
 // Test with known archivers
 OutputStream arOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream arArchiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
 arArchiveOutputStream.finish();
 arArchiveOutputStream.close();
 assert arOutputStream.size() > 0;

 OutputStream zipOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream zipArchiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
 zipArchiveOutputStream.putNextEntry(new JarArchiveEntry("test.jar"));
 IOUtils.copy(new ByteArrayInputStream(new byte[1024]), zipArchiveOutputStream);
 zipArchiveOutputStream.finish();
 zipArchiveOutputStream.close();
 assert zipOutputStream.size() > 0;

 OutputStream tarOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream tarArchiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
 tarArchiveOutputStream.putNextEntry(new TarEntry("test.tar"));
 IOUtils.copy(new ByteArrayInputStream(new byte[1024]), tarArchiveOutputStream);
 tarArchiveOutputStream.finish();
 tarArchiveOutputStream.close();
 assert tarOutputStream.size() > 0;

 OutputStream jarOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream jarArchiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
 jarArchiveOutputStream.putNextEntry(new JarArchiveEntry("test.jar"));
 IOUtils.copy(new ByteArrayInputStream(new byte[1024]), jarArchiveOutputStream);
 jarArchiveOutputStream.finish();
 jarArchiveOutputStream.close();
 assert jarOutputStream.size() > 0;

 OutputStream cpioOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream cpioArchiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
 cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry("test.cpio"));
 IOUtils.copy(new ByteArrayInputStream(new byte[1024]), cpioArchiveOutputStream);
 cpioArchiveOutputStream.finish();
 cpioArchiveOutputStream.close();
 assert cpioOutputStream.size() > 0;

 OutputStream dumpOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream dumpArchiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, dumpOutputStream);
 dumpArchiveOutputStream.putNextEntry(new DumpArchiveEntry("test.dump"));
 IOUtils.copy(new ByteArrayInputStream(new byte[1024]), dumpArchiveOutputStream);
 dumpArchiveOutputStream.finish();
 dumpArchiveOutputStream.close();
 assert dumpOutputStream.size() > 0;

 // Test with unknown archiver
 OutputStream unknownOutputStream = new ByteArrayOutputStream();
 try {
 ArchiveOutputStream unknownArchiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream("unknown", unknownOutputStream);
 assert null : "Unexpected ArchiveOutputStream creation.";
 } catch (ArchiveException e) {
 // Expected
 } finally {
 unknownOutputStream.close();
 }
 }

 @Test
 public void testCreateArchiveInputStreamFromBytes() throws Exception {
 // Test with known archivers
 byte[] arBytes = new byte[1024];
 ArchiveInputStream arArchiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(arBytes));
 assertNotNull(arArchiveInputStream);
 arArchiveInputStream.close();

 byte[] zipBytes = new byte[1024];
 ArchiveInputStream zipArchiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(zipBytes));
 assertNotNull(zipArchiveInputStream);
 zipArchiveInputStream.close();

 byte[] tarBytes = new byte[1024];
 ArchiveInputStream tarArchiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(tarBytes));
 assertNotNull(tarArchiveInputStream);
 tarArchiveInputStream.close();

 byte[] jarBytes = new byte[1024];
 ArchiveInputStream jarArchiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(jarBytes));
 assertNotNull(jarArchiveInputStream);
 jarArchiveInputStream.close();

 byte[] cpioBytes = new byte[1024];
 ArchiveInputStream cpioArchiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(cpioBytes));
 assertNotNull(cpioArchiveInputStream);
 cpioArchiveInputStream.close();

 byte[] dumpBytes = new byte[1024];
 ArchiveInputStream dumpArchiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(dumpBytes));
 assertNotNull(dumpArchiveInputStream);
 dumpArchiveInputStream.close();

 // Test with unknown archiver
 byte[] unknownBytes = new byte[1024];
 try {
 ArchiveInputStream unknownArchiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("unknown", new ByteArrayInputStream(unknownBytes));
 assert null : "Unexpected ArchiveInputStream creation.";
 } catch (ArchiveException e) {
 // Expected
 }
 }

 @Test
 public void testCreateArchiveOutputStreamFromBytes() throws Exception {
 // Test with known archivers
 byte[] arBytes = new byte[1024];
 ArchiveOutputStream arArchiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayOutputStream());
 arArchiveOutputStream.putNextEntry(new ArArchiveEntry("test.ar"));
 IOUtils.copy(new ByteArrayInputStream(arBytes), arArchiveOutputStream);
 arArchiveOutputStream.finish();
 arArchiveOutputStream.close();
 assert ((ByteArrayOutputStream) arArchiveOutputStream.getOutputStream()).toByteArray().length > 0;

 byte[] zipBytes = new byte[1024];
 ArchiveOutputStream zipArchiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream());
 zipArchiveOutputStream.putNextEntry(new JarArchiveEntry("test.jar"));
 IOUtils.copy(new ByteArrayInputStream(zipBytes), zipArchiveOutputStream);
 zipArchiveOutputStream.finish();
 zipArchiveOutputStream.close();
 assert ((ByteArrayOutputStream) zipArchiveOutputStream.getOutputStream()).toByteArray().length > 0;

 byte[] tarBytes = new byte[1024];
 ArchiveOutputStream tarArchiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream());
 tarArchiveOutputStream.putNextEntry(new TarEntry("test.tar"));
 IOUtils.copy(new ByteArrayInputStream(tarBytes), tarArchiveOutputStream);
 tarArchiveOutputStream.finish();
 tarArchiveOutputStream.close();
 assert ((ByteArrayOutputStream) tarArchiveOutputStream.getOutputStream()).toByteArray().length > 0;

 byte[] jarBytes = new byte[1024];
 ArchiveOutputStream jarArchiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, new ByteArrayOutputStream());
 jarArchiveOutputStream.putNextEntry(new JarArchiveEntry("test.jar"));
 IOUtils.copy(new ByteArrayInputStream(jarBytes), jarArchiveOutputStream);
 jarArchiveOutputStream.finish();
 jarArchiveOutputStream.close();
 assert ((ByteArrayOutputStream) jarArchiveOutputStream.getOutputStream()).toByteArray().length > 0;

 byte[] cpioBytes = new byte[1024];
 ArchiveOutputStream cpioArchiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, new ByteArrayOutputStream());
 cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry("test.cpio"));
 IOUtils.copy(new ByteArrayInputStream(cpioBytes), cpioArchiveOutputStream);
 cpioArchiveOutputStream.finish();
 cpioArchiveOutputStream.close();
 assert ((ByteArrayOutputStream) cpioArchiveOutputStream.getOutputStream()).toByteArray().length > 0;

 byte[] dumpBytes = new byte[1024];
 ArchiveOutputStream dumpArchiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, new ByteArrayOutputStream());
 dumpArchiveOutputStream.putNextEntry(new DumpArchiveEntry("test.dump"));
 IOUtils.copy(new ByteArrayInputStream(dumpBytes), dumpArchiveOutputStream);
 dumpArchiveOutputStream.finish();
 dumpArchiveOutputStream.close();
 assert ((ByteArrayOutputStream) dumpArchiveOutputStream.getOutputStream()).toByteArray().length > 0;

 // Test with unknown archiver
 byte[] unknownBytes = new byte[1024];
 try {
 ArchiveOutputStream unknownArchiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream("unknown", new ByteArrayOutputStream());
 assert null : "Unexpected ArchiveOutputStream creation.";
 } catch (ArchiveException e) {
 // Expected
 }
 }
}