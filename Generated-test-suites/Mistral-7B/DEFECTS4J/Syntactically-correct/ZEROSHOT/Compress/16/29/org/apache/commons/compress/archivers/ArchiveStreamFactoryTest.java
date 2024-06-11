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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class ArchiveStreamFactoryTest {

 @Test
 public void testCreateArchiveInputStream() throws Exception {
 final String[] archiverNames = {ArchiveStreamFactory.AR, ArchiveStreamFactory.CPIO,
 ArchiveStreamFactory.DUMP, ArchiveStreamFactory.JAR, ArchiveStreamFactory.TAR,
 ArchiveStreamFactory.ZIP};
 final InputStream[] inputs = new InputStream[] {
 new ByteArrayInputStream("ar headers".getBytes()),
 new ByteArrayInputStream("cpio headers".getBytes()),
 new ByteArrayInputStream("dump headers".getBytes()),
 new JarArchiveInputStream(new ByteArrayInputStream("jar headers".getBytes())),
 new TarArchiveInputStream(new ByteArrayInputStream("tar headers".getBytes())),
 new ZipArchiveInputStream(new ByteArrayInputStream("zip headers".getBytes()))
 };

 for (int i = 0; i < archiverNames.length; i++) {
 ArchiveInputStream expected = null;
 if (ArchiveStreamFactory.AR.equalsIgnoreCase(archiverNames[i])) {
 expected = new ArArchiveInputStream(inputs[i]);
 } else if (ArchiveStreamFactory.CPIO.equalsIgnoreCase(archiverNames[i])) {
 expected = new CpioArchiveInputStream(inputs[i]);
 } else if (ArchiveStreamFactory.DUMP.equalsIgnoreCase(archiverNames[i])) {
 expected = new DumpArchiveInputStream(inputs[i]);
 } else if (ArchiveStreamFactory.JAR.equalsIgnoreCase(archiverNames[i])) {
 expected = inputs[i];
 } else if (ArchiveStreamFactory.TAR.equalsIgnoreCase(archiverNames[i])) {
 expected = new TarArchiveInputStream(inputs[i]);
 } else if (ArchiveStreamFactory.ZIP.equalsIgnoreCase(archiverNames[i])) {
 expected = new ZipArchiveInputStream(inputs[i]);
 }

 ArchiveInputStream actual = new ArchiveStreamFactory().createArchiveInputStream(
 archiverNames[i], inputs[i]);
 assertSame(expected, actual);
 }
 }

 @Test
 public void testCreateArchiveInputStreamUnknownFormat() throws Exception {
 assertNull(new ArchiveStreamFactory().createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0])));
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamNullFormat() throws Exception {
 new ArchiveStreamFactory().createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
 }

 @Test
 public void testCreateArchiveInputStreamNullInputStream() throws Exception {
 assertNull(new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, null));
 }

 @Test
 public void testCreateArchiveOutputStream() throws Exception {
 final String[] archiverNames = {ArchiveStreamFactory.AR, ArchiveStreamFactory.CPIO,
 ArchiveStreamFactory.DUMP, ArchiveStreamFactory.JAR, ArchiveStreamFactory.TAR,
 ArchiveStreamFactory.ZIP};
 final OutputStream[] outputs = new OutputStream[] {
 new MemoryOutputStream(),
 new MemoryOutputStream(),
 new MemoryOutputStream(),
 new java.io.FileOutputStream("test.jar"),
 new java.io.FileOutputStream("test.tar"),
 new java.io.FileOutputStream("test.zip")
 };

 for (int i = 0; i < archiverNames.length; i++) {
 ArchiveOutputStream expected = null;
 if (ArchiveStreamFactory.AR.equalsIgnoreCase(archiverNames[i])) {
 expected = new ArArchiveOutputStream(outputs[i]);
 } else if (ArchiveStreamFactory.CPIO.equalsIgnoreCase(archiverNames[i])) {
 expected = new CpioArchiveOutputStream(outputs[i]);
 } else if (ArchiveStreamFactory.DUMP.equalsIgnoreCase(archiverNames[i])) {
 expected = new DumpArchiveOutputStream(outputs[i]);
 } else if (ArchiveStreamFactory.JAR.equalsIgnoreCase(archiverNames[i])) {
 expected = new JarArchiveOutputStream(outputs[i]);
 } else if (ArchiveStreamFactory.TAR.equalsIgnoreCase(archiverNames[i])) {
 expected = new TarArchiveOutputStream(outputs[i]);
 } else if (ArchiveStreamFactory.ZIP.equalsIgnoreCase(archiverNames[i])) {
 expected = new ZipArchiveOutputStream(outputs[i]);
 }

 ArchiveOutputStream actual = new ArchiveStreamFactory().createArchiveOutputStream(
 archiverNames[i], outputs[i]);
 assertSame(expected, actual);
 }
 }

 @Test
 public void testCreateArchiveOutputStreamUnknownFormat() throws Exception {
 assertNull(new ArchiveStreamFactory().createArchiveOutputStream("unknown", new ByteArrayInputStream(new byte[0])));
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveOutputStreamNullFormat() throws Exception {
 new ArchiveStreamFactory().createArchiveOutputStream(null, new ByteArrayInputStream(new byte[0]));
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveOutputStreamNullOutputStream() throws Exception {
 new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
 }
}

class MemoryOutputStream extends OutputStream {
 private byte[] buffer = new byte[1024];
 private int length = 0;

 @Override
 public void write(int b) throws IOException {
 buffer[length++] = (byte) b;
 }

 @Override
 public void write(byte[] b, int off, int len) throws IOException {
 System.arraycopy(b, off, buffer, length, len);
 length += len;
 }

 @Override
 public void close() throws IOException {
 // do nothing
 }

 @Override
 public void flush() throws IOException {
 // do nothing
 }

 @Override
 public synchronized void reset() throws IOException {
 length = 0;
 }

 public byte[] toByteArray() {
 return new byte[] {}.concat(Arrays.copyOfRange(buffer, 0, length));
 }
}