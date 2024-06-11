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
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

 @Test
 public void testCreateArchiveInputStream() throws ArchiveException, IOException {
 // Test AR archive
 InputStream arInputStream = new ByteArrayInputStream("!<arch>\0\0 type ar\n".getBytes());
 ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
 assertTrue(archiveInputStream instanceof ArArchiveInputStream);
 archiveInputStream.close();

 // Test ZIP archive
 InputStream zipInputStream = new ByteArrayInputStream("PK\0\0\0\0\0\0\0 local-header\0\0\0\0\0 end-of-central-directory\0\0\0\0\0\0\0\0".getBytes());
 ArchiveInputStream archiveZipInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
 assertTrue(archiveZipInputStream instanceof ZipArchiveInputStream);
 archiveZipInputStream.close();

 // Test JAR archive
 InputStream jarInputStream = new ByteArrayInputStream("0\0\0\100\0\0\0Manifest-Name: test-manifest.mf\n\0\0\0\0\0\0\0\0".getBytes());
 ArchiveInputStream archiveJarInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
 assertTrue(archiveJarInputStream instanceof JarArchiveInputStream);
 archiveJarInputStream.close();

 // Test CPIO archive
 InputStream cpioInputStream = new ByteArrayInputStream("07030C\0bin\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0".getBytes());
 ArchiveInputStream archiveCpioInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
 assertTrue(archiveCpioInputStream instanceof CpioArchiveInputStream);
 archiveCpioInputStream.close();

 // Test Dump archive
 InputStream dumpInputStream = new ByteArrayInputStream("07010Cdump\n".getBytes());
 ArchiveInputStream archiveDumpInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
 assertTrue(archiveDumpInputStream instanceof DumpArchiveInputStream);
 archiveDumpInputStream.close();

 // Test Tar archive
 InputStream tarInputStream = new ByteArrayInputStream("07000C\0\0\0\0\0\0\0tar\n\0\0\0\0\0\0\0\0".getBytes());
 ArchiveInputStream archiveTarInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
 assertTrue(archiveTarInputStream instanceof TarArchiveInputStream);
 archiveTarInputStream.close();
 }

 @Test
 public void testCreateArchiveOutputStream() throws ArchiveException, IOException {
 // Test AR archive
 OutputStream arOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
 archiveOutputStream.finish();
 byte[] arBytes = arOutputStream.toByteArray();
 assertTrue(arBytes.length > 0);
 assertEquals("!<arch>\0\0 type ar\n", new String(new ByteArrayInputStream(arBytes).readAllBytes()));
 archiveOutputStream.close();

 // Test ZIP archive
 OutputStream zipOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream archiveZipOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
 archiveZipOutputStream.putNextEntry(new JarArchiveEntry("testfile.txt"));
 archiveZipOutputStream.write("Test file content.".getBytes());
 archiveZipOutputStream.finish();
 byte[] zipBytes = zipOutputStream.toByteArray();
 assertTrue(zipBytes.length > 0);
 assertEquals("PK\0\0\0\0\0\0\0 local-header\0\0\0\0\0 end-of-central-directory\0\0\0\0\0\0\0\0", new String(new ByteArrayInputStream(zipBytes).readNBytes(16)));
 archiveZipOutputStream.close();

 // Test JAR archive
 OutputStream jarOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream archiveJarOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
 JarArchiveEntry jarEntry = new JarArchiveEntry("testfile.txt");
 jarEntry.setName("testfile.txt");
 jarEntry.setSize(10);
 jarEntry.setTimeStamp(System.currentTimeMillis());
 archiveJarOutputStream.putNextEntry(jarEntry);
 archiveJarOutputStream.write("Test file content.".getBytes());
 archiveJarOutputStream.finish();
 byte[] jarBytes = jarOutputStream.toByteArray();
 assertTrue(jarBytes.length > 0);
 assertEquals("0\0\0\100\0\0\0Manifest-Name: test-manifest.mf\n\0\0\0\0\0\0\0\0", new String(new ByteArrayInputStream(jarBytes).readNBytes(12)));
 archiveJarOutputStream.close();

 // Test CPIO archive
 OutputStream cpioOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream archiveCpioOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
 archiveCpioOutputStream.putNextEntry(new CpioArchiveEntry("testfile.txt"));
 archiveCpioOutputStream.write("Test file content.".getBytes());
 archiveCpioOutputStream.finish();
 byte[] cpioBytes = cpioOutputStream.toByteArray();
 assertTrue(cpioBytes.length > 0);
 assertEquals("07030C\0bin\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0", new String(new ByteArrayInputStream(cpioBytes).readAllBytes()));
 archiveCpioOutputStream.close();

 // Test Dump archive
 OutputStream dumpOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream archiveDumpOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, dumpOutputStream);
 archiveDumpOutputStream.putNextEntry(new DumpArchiveEntry("testfile.txt"));
 archiveDumpOutputStream.write("Test file content.".getBytes());
 archiveDumpOutputStream.finish();
 byte[] dumpBytes = dumpOutputStream.toByteArray();
 assertTrue(dumpBytes.length > 0);
 assertEquals("07010Cdump\n", new String(new ByteArrayInputStream(dumpBytes).readAllBytes()));
 archiveDumpOutputStream.close();

 // Test Tar archive
 OutputStream tarOutputStream = new ByteArrayOutputStream();
 ArchiveOutputStream archiveTarOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
 archiveTarOutputStream.putNextEntry(new TarArchiveEntry("testfile.txt"));
 archiveTarOutputStream.write("Test file content.".getBytes());
 archiveTarOutputStream.finish();
 byte[] tarBytes = tarOutputStream.toByteArray();
 assertTrue(tarBytes.length > 0);
 assertEquals("07000C\0\0\0\0\0\0\0tar\n\0\0\0\0\0\0\0\0", new String(new ByteArrayInputStream(tarBytes).readAllBytes()));
 archiveTarOutputStream.close();
 }

 @Test
 public void testCreateArchiveInputStreamFromInputStream() throws ArchiveException, IOException {
 InputStream inputStream = new ByteArrayInputStream("PK\0\0\0\0\0\0\0 local-header\0\0\0\0\0 end-of-central-directory\0\0\0\0\0\0\0\0".getBytes());
 ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
 assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
 assertEquals("testfile.txt", archiveInputStream.getNextEntry().getName());
 archiveInputStream.close();
 }
}