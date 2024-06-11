import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers. JarArchiveInputStream;
import org.apache.commons.compress.archivers. JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.junit.Test;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertThrows;

public class ArchiveStreamFactoryTest {

 @Test
 public void testCreateArchiveInputStreamAR() {
 InputStream input = new ByteArrayInputStream(new byte[10]);
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.AR, input);
 assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);
 }

 @Test
 public void testCreateArchiveInputStreamCpio() {
 InputStream input = new ByteArrayInputStream(new byte[10]);
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, input);
 assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);
 }

 @Test
 public void testCreateArchiveInputStreamDump() {
 InputStream input = new ByteArrayInputStream(new byte[10]);
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, input);
 assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);
 }

 @Test
 public void testCreateArchiveInputStreamJar() {
 InputStream input = new ByteArrayInputStream(new byte[10]);
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, input);
 assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);
 }

 @Test
 public void testCreateArchiveInputStreamTar() {
 InputStream input = new ByteArrayInputStream(new byte[10]);
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, input);
 assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);
 }

 @Test
 public void testCreateArchiveInputStreamInvalidArchiverName() {
 InputStream input = new ByteArrayInputStream(new byte[10]);
 Assert.assertThrows(ArchiveException.class, () -> {
 ArchiveStreamFactory.newInstance().createArchiveInputStream("invalid", input);
 });
 }

 @Test
 public void testCreateArchiveInputStreamNullInputStream() {
 assertThrows(IllegalArgumentException.class, () -> {
 ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.AR, null);
 });
 }

 @Test
 public void testCreateArchiveOutputStreamAR() {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, output);
 assertInstanceOf(ArArchiveOutputStream.class, archiveOutputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamCpio() {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, output);
 assertInstanceOf(CpioArchiveOutputStream.class, archiveOutputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamDump() {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, output);
 assertInstanceOf(DumpArchiveOutputStream.class, archiveOutputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamJar() {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, output);
 assertInstanceOf(JarArchiveOutputStream.class, archiveOutputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamTar() {
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, output);
 assertInstanceOf(TarArchiveOutputStream.class, archiveOutputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamInvalidArchiverName() {
 OutputStream output = new ByteArrayOutputStream();
 Assert.assertThrows(ArchiveException.class, () -> {
 ArchiveStreamFactory.newInstance().createArchiveOutputStream("invalid", output);
 });
 }

 @Test
 public void testCreateArchiveOutputStreamNullOutputStream() {
 assertThrows(IllegalArgumentException.class, () -> {
 ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
 });
 }

 @Test
 public void testCreateArchiveOutputStreamFromInputStreamAR() {
 InputStream input = new ByteArrayInputStream(new byte[10]);
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, output);
 archiveOutputStream.putArchiveEntry(new ArArchiveEntry("entryname"));
 archiveOutputStream.writeToArchive(input, 0, input.available());
 archiveOutputStream.finish();
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(output.toByteArray()));
 assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamFromInputStreamCpio() {
 InputStream input = new ByteArrayInputStream(new byte[10]);
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, output);
 archiveOutputStream.putArchiveEntry(new CpioArchiveEntry("entryname"));
 archiveOutputStream.writeToArchive(input, 0, input.available());
 archiveOutputStream.finish();
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(output.toByteArray()));
 assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamFromInputStreamDump() {
 InputStream input = new ByteArrayInputStream(new byte[10]);
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, output);
 archiveOutputStream.putArchiveEntry(new DumpArchiveEntry("entryname"));
 archiveOutputStream.writeToArchive(input, 0, input.available());
 archiveOutputStream.finish();
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(output.toByteArray()));
 assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamFromInputStreamJar() {
 InputStream input = new ByteArrayInputStream(new byte[10]);
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, output);
 archiveOutputStream.putNextEntry(new JarArchiveEntry("entryname.txt"));
 archiveOutputStream.writeToArchive(input, 0, input.available());
 archiveOutputStream.finish();
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(output.toByteArray()));
 assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);
 }

 @Test
 public void testCreateArchiveOutputStreamFromInputStreamTar() {
 InputStream input = new ByteArrayInputStream(new byte[10]);
 OutputStream output = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, output);
 archiveOutputStream.putNextEntry(new TarArchiveEntry("entryname.txt"));
 archiveOutputStream.writeToArchive(input, 0, input.available());
 archiveOutputStream.finish();
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(output.toByteArray()));
 assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);
 }
}