import org.apache.commons.compress.archivers.ArchiveException;
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
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class ArchiveStreamFactoryTest {

 @Test
 public void testCreateArchiveInputStream() throws Exception {
 // test creating archive input streams from known signatures
 InputStream arStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x41, (byte) 0x7a, (byte) 0x45, (byte) 0x1, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x41, (byte) 0x7a, (byte) 0x45, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0});
 ArchiveInputStream arInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, arStream);
 assertTrue(arInput instanceof ArArchiveInputStream);

 InputStream tarStream = new ByteArrayInputStream(new byte[512]);
 TarArchiveInputStream tarInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, tarStream);
 assertTrue(tarInput instanceof TarArchiveInputStream);

 InputStream zipStream = new ByteArrayInputStream(new byte[0]);
 ArchiveInputStream zipInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipStream);
 assertTrue(zipInput instanceof ZipArchiveInputStream);

 InputStream jarStream = new ByteArrayInputStream(new byte[0]);
 JarArchiveInputStream jarInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, jarStream);
 assertTrue(jarInput instanceof JarArchiveInputStream);

 InputStream cpioStream = new ByteArrayInputStream(new byte[12]);
 CpioArchiveInputStream cpioInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioStream);
 assertTrue(cpioInput instanceof CpioArchiveInputStream);

 InputStream dumpStream = new ByteArrayInputStream(new byte[32]);
 DumpArchiveInputStream dumpInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpStream);
 assertTrue(dumpInput instanceof DumpArchiveInputStream);

 // test creating archive input streams with unknown signatures
 InputStream unknownStream = new ByteArrayInputStream(new byte[10]);
 try {
 ArchiveInputStream unknownInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, unknownStream);
 fail("Should have thrown ArchiveException for unknown signature");
 } catch (ArchiveException e) {
 // expected
 }
 }

 @Test
 public void testCreateArchiveOutputStream() throws Exception {
 // test creating archive output streams
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream arOutput = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, output);
 arOutput.close();
 assertTrue(output.size() > 0);

 OutputStream tarOutput = new ByteArrayOutputStream();
 ArchiveOutputStream tarOutput = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutput);
 tarOutput.close();
 assertTrue(tarOutput.size() > 0);

 OutputStream zipOutput = new ByteArrayOutputStream();
 ArchiveOutputStream zipOutput = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutput);
 zipOutput.close();
 assertTrue(zipOutput.size() > 0);

 OutputStream jarOutput = new ByteArrayOutputStream();
 ArchiveOutputStream jarOutput = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutput);
 jarOutput.close();
 assertTrue(jarOutput.size() > 0);

 OutputStream cpioOutput = new ByteArrayOutputStream();
 ArchiveOutputStream cpioOutput = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutput);
 cpioOutput.close();
 assertTrue(cpioOutput.size() > 0);

 OutputStream dumpOutput = new ByteArrayOutputStream();
 ArchiveOutputStream dumpOutput = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, dumpOutput);
 dumpOutput.close();
 assertTrue(dumpOutput.size() > 0);
 }

 @Test
 public void testCreateArchiveInputStreamFromInputStream() throws Exception {
 // test creating archive input streams from input streams with known signatures
 InputStream arInputStreamFromFile = new FileInputStream("path/to/ar/file.tar.gz");
 ArchiveInputStream arInputFromFile = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStreamFromFile);
 assertTrue(arInputFromFile instanceof ArArchiveInputStream);

 InputStream tarInputStreamFromFile = new FileInputStream("path/to/tar/file.tar");
 TarArchiveInputStream tarInputFromFile = (TarArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStreamFromFile);
 assertTrue(tarInputFromFile != null);

 InputStream zipInputStreamFromFile = new FileInputStream("path/to/zip/file.zip");
 ArchiveInputStream zipInputFromFile = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStreamFromFile);
 assertTrue(zipInputFromFile instanceof ZipArchiveInputStream);

 InputStream jarInputStreamFromFile = new FileInputStream("path/to/jar/file.jar");
 JarArchiveInputStream jarInputFromFile = (JarArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStreamFromFile);
 assertTrue(jarInputFromFile != null);

 InputStream cpioInputStreamFromFile = new FileInputStream("path/to/cpio/file.cpio");
 CpioArchiveInputStream cpioInputFromFile = (CpioArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStreamFromFile);
 assertTrue(cpioInputFromFile != null);

 InputStream dumpInputStreamFromFile = new FileInputStream("path/to/dump/file.dump");
 DumpArchiveInputStream dumpInputFromFile = (DumpArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStreamFromFile);
 assertTrue(dumpInputFromFile != null);

 // test creating archive input streams from input streams with unknown signatures
 InputStream unknownInputStream = new ByteArrayInputStream(new byte[10]);
 try {
 ArchiveInputStream unknownInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, unknownInputStream);
 fail("Should have thrown ArchiveException for unknown signature");
 } catch (ArchiveException e) {
 // expected
 }
 }
}