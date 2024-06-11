import org.apache.commons.compress.archivers.ArchiveException;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

 @Test
 public void testCreateArchiveInputStream() throws ArchiveException, IOException {
 InputStream testInputStream = new ByteArrayInputStream(new byte[0]);
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, testInputStream);
 assertNull(archiveInputStream);

 InputStream tarInputStream = new ByteArrayInputStream(new byte[0]);
 archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
 assertNull(archiveInputStream);

 InputStream arInputStream = new ByteArrayInputStream(new byte[0]);
 archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
 assertNull(archiveInputStream);

 InputStream zipInputStream = new ByteArrayInputStream(new byte[0]);
 archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
 assertNotNull(archiveInputStream);
 assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);

 InputStream jarInputStream = new ByteArrayInputStream(new byte[0]);
 archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
 assertNotNull(archiveInputStream);
 assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);

 InputStream cpioInputStream = new ByteArrayInputStream(new byte[0]);
 archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
 assertNotNull(archiveInputStream);
 assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);

 InputStream dumpInputStream = new ByteArrayInputStream(new byte[0]);
 archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
 assertNotNull(archiveInputStream);
 assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);

 InputStream tarWithSignatureInputStream = new ByteArrayInputStream(new byte[512]);
 archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, tarWithSignatureInputStream);
 assertNotNull(archiveInputStream);
 assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);
 }

 @Test
 public void testCreateArchiveOutputStream() throws ArchiveException, IOException {
 OutputStream testOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, testOutputStream);
 assertNull(archiveOutputStream);

 OutputStream tarOutputStream = new ByteArrayOutputStream();
 archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
 assertNull(archiveOutputStream);

 OutputStream arOutputStream = new ByteArrayOutputStream();
 archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
 assertNull(archiveOutputStream);

 OutputStream zipOutputStream = new ByteArrayOutputStream();
 archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
 assertNotNull(archiveOutputStream);
 assertInstanceOf(ZipArchiveOutputStream.class, archiveOutputStream);

 OutputStream jarOutputStream = new ByteArrayOutputStream();
 archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
 assertNotNull(archiveOutputStream);
 assertInstanceOf(JarArchiveOutputStream.class, archiveOutputStream);

 OutputStream cpioOutputStream = new ByteArrayOutputStream();
 archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
 assertNotNull(archiveOutputStream);
 assertInstanceOf(CpioArchiveOutputStream.class, archiveOutputStream);

 OutputStream dumpOutputStream = new ByteArrayOutputStream();
 archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, dumpOutputStream);
 assertNotNull(archiveOutputStream);
 assertInstanceOf(DumpArchiveOutputStream.class, archiveOutputStream);
 }

 @Test
 public void testCreateArchiveInputStreamFromSignature() throws ArchiveException, IOException {
 byte[] zipSignature = ZipArchiveInputStream.getSignature();
 InputStream zipSignatureInputStream = new ByteArrayInputStream(zipSignature);
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipSignatureInputStream);
 assertNotNull(archiveInputStream);
 assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);

 byte[] tarSignature = TarArchiveInputStream.getSignature();
 InputStream tarSignatureInputStream = new ByteArrayInputStream(tarSignature);
 ArchiveInputStream archiveInputStream1 = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, tarSignatureInputStream);
 assertNotNull(archiveInputStream1);
 assertInstanceOf(TarArchiveInputStream.class, archiveInputStream1);
 }

 @Test(expected = ArchiveException.class)
 public void testUnknownArchiver() throws ArchiveException, IOException {
 InputStream testInputStream = new ByteArrayInputStream(new byte[0]);
 ArchiveStreamFactory.getInstance().createArchiveInputStream("unknown", testInputStream);
 }
}