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
import static org.junit.Assert.assertNull;

public class ArchiveStreamFactoryTest {

 @Test
 public void testCreateArchiveInputStreamNullArchiverName() {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 InputStream input = new ByteArrayInputStream(new byte[0]);
 try {
 factory.createArchiveInputStream(null, input);
 assert false;
 } catch (ArchiveException e) {
 // pass
 }
 }

 @Test
 public void testCreateArchiveInputStreamNullInputStream() {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 String archiverName = ArchiveStreamFactory.ZIP;
 try {
 factory.createArchiveInputStream(archiverName, null);
 assert false;
 } catch (ArchiveException e) {
 // pass
 }
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamUnknownArchiverName() {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 InputStream input = new ByteArrayInputStream(new byte[0]);
 String archiverName = "unknown";
 factory.createArchiveInputStream(archiverName, input);
 }

 @Test
 public void testCreateArchiveInputStreamAR() {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 InputStream input = new ByteArrayInputStream(new byte[0]);
 String archiverName = ArchiveStreamFactory.AR;
 ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(archiverName, input);
 assert archiveInputStream instanceof ArArchiveInputStream;
 }

 @Test
 public void testCreateArchiveInputStreamZIP() {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 InputStream input = new ByteArrayInputStream(new byte[0]);
 String archiverName = ArchiveStreamFactory.ZIP;
 ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(archiverName, input);
 assert archiveInputStream instanceof ZipArchiveInputStream;
 }

 @Test
 public void testCreateArchiveInputStreamTAR() {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 InputStream input = new ByteArrayInputStream(new byte[0]);
 String archiverName = ArchiveStreamFactory.TAR;
 ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(archiverName, input);
 assert archiveInputStream instanceof TarArchiveInputStream;
 }

 @Test
 public void testCreateArchiveInputStreamJAR() {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 InputStream input = new ByteArrayInputStream(new byte[0]);
 String archiverName = ArchiveStreamFactory.JAR;
 ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(archiverName, input);
 assert archiveInputStream instanceof JarArchiveInputStream;
 }

 @Test
 public void testCreateArchiveInputStreamCPIO() {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 InputStream input = new ByteArrayInputStream(new byte[0]);
 String archiverName = ArchiveStreamFactory.CPIO;
 ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(archiverName, input);
 assert archiveInputStream instanceof CpioArchiveInputStream;
 }

 @Test
 public void testCreateArchiveInputStreamDUMP() {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 InputStream input = new ByteArrayInputStream(new byte[0]);
 String archiverName = ArchiveStreamFactory.DUMP;
 ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(archiverName, input);
 assert archiveInputStream instanceof DumpArchiveInputStream;
 }

 @Test
 public void testCreateArchiveInputStreamNoMatchingSignature() {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 InputStream input = new ByteArrayInputStream(new byte[0]);
 ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(input);
 assertNull(archiveInputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamNullArchiverName() {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 OutputStream output = new ByteArrayOutputStream();
 try {
 factory.createArchiveOutputStream(null, output);
 assert false;
 } catch (ArchiveException e) {
 // pass
 }
 }

 @Test
 public void testCreateArchiveOutputStreamNullOutputStream() {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 String archiverName = ArchiveStreamFactory.ZIP;
 try {
 factory.createArchiveOutputStream(archiverName, null);
 assert false;
 } catch (ArchiveException e) {
 // pass
 }
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveOutputStreamUnknownArchiverName() {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 OutputStream output = new ByteArrayOutputStream();
 String archiverName = "unknown";
 factory.createArchiveOutputStream(archiverName, output);
 }

 @Test
 public void testCreateArchiveOutputStreamAR() {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 OutputStream output = new ByteArrayOutputStream();
 String archiverName = ArchiveStreamFactory.AR;
 ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(archiverName, output);
 assert archiveOutputStream instanceof ArArchiveOutputStream;
 }

 @Test
 public void testCreateArchiveOutputStreamZIP() {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 OutputStream output = new ByteArrayOutputStream();
 String archiverName = ArchiveStreamFactory.ZIP;
 ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(archiverName, output);
 assert archiveOutputStream instanceof ZipArchiveOutputStream;
 }

 @Test
 public void testCreateArchiveOutputStreamTAR() {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 OutputStream output = new ByteArrayOutputStream();
 String archiverName = ArchiveStreamFactory.TAR;
 ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(archiverName, output);
 assert archiveOutputStream instanceof TarArchiveOutputStream;
 }

 @Test
 public void testCreateArchiveOutputStreamJAR() {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 OutputStream output = new ByteArrayOutputStream();
 String archiverName = ArchiveStreamFactory.JAR;
 ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(archiverName, output);
 assert archiveOutputStream instanceof JarArchiveOutputStream;
 }

 @Test
 public void testCreateArchiveOutputStreamCPIO() {
 ArchiveStreamFactory factory = new ArchiveStreamFactory();
 OutputStream output = new ByteArrayOutputStream();
 String archiverName = ArchiveStreamFactory.CPIO;
 ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(archiverName, output);
 assert archiveOutputStream instanceof CpioArchiveOutputStream;
 }
}