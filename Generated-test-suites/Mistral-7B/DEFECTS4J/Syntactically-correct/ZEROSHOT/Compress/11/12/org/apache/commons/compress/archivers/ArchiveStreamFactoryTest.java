import org.apache.commons.compress.archivers.*;
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
import static org.junit.Assert.assertNotNull;

public class ArchiveStreamFactoryTest {

 @Test
 public void testCreateArchiveInputStream() throws Exception {
 // Test with valid archiver names and InputStreams
 ArchiveInputStream inZip = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
 assertNotNull(inZip);
 ArchiveInputStream inTar = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
 assertNotNull(inTar);
 ArchiveInputStream inJar = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0]));
 assertNotNull(inJar);
 ArchiveInputStream inCpio = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0]));
 assertNotNull(inCpio);
 ArchiveInputStream inAr = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
 assertNotNull(inAr);
 ArchiveInputStream inDump = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0]));
 assertNotNull(inDump);

 // Test with invalid archiver names
 try {
 new ArchiveStreamFactory().createArchiveInputStream("invalid", new ByteArrayInputStream(new byte[0]));
 assert(false); // Should throw ArchiveException
 } catch (ArchiveException e) {
 // Expected
 }
 }

 @Test
 public void testCreateArchiveOutputStream() throws Exception {
 // Test with valid archiver names and OutputStreams
 OutputStream outZip = new ByteArrayOutputStream();
 ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outZip);
 assertNotNull(out);
 OutputStream outTar = new ByteArrayOutputStream();
 ArchiveOutputStream outTar1 = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outTar);
 assertNotNull(outTar1);
 OutputStream outTar2 = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outTar);
 assertNotNull(outTar2); // Should reuse the same instance
 assertSame(outTar1, outTar2);
 OutputStream outJar = new ByteArrayOutputStream();
 ArchiveOutputStream outJar1 = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, outJar);
 assertNotNull(outJar1);
 OutputStream outJar2 = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, outJar);
 assertNotNull(outJar2); // Should create a new instance
 assertNotSame(outJar1, outJar2);
 OutputStream outCpio = new ByteArrayOutputStream();
 ArchiveOutputStream outCpio1 = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, outCpio);
 assertNotNull(outCpio1);
 OutputStream outCpio2 = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, outCpio);
 assertNotNull(outCpio2); // Should reuse the same instance
 assertSame(outCpio1, outCpio2);
 OutputStream outAr = new ByteArrayOutputStream();
 ArchiveOutputStream outAr1 = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, outAr);
 assertNotNull(outAr1);
 OutputStream outAr2 = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, outAr);
 assertNotNull(outAr2); // Should reuse the same instance
 assertSame(outAr1, outAr2);
 OutputStream outDump = new ByteArrayOutputStream();
 ArchiveOutputStream outDump1 = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, outDump);
 assertNotNull(outDump1);
 OutputStream outDump2 = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, outDump);
 assertNotNull(outDump2); // Should reuse the same instance
 assertSame(outDump1, outDump2);

 // Test with invalid archiver names
 try {
 new ArchiveStreamFactory().createArchiveOutputStream("invalid", new ByteArrayOutputStream());
 assert(false); // Should throw ArchiveException
 } catch (ArchiveException e) {
 // Expected
 }
 }

 @Test
 public void testCreateArchiveInputStreamFromBytes() throws Exception {
 // Test with valid archiver names and byte arrays
 byte[] zipBytes = new byte[0];
 ArchiveInputStream inZip = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(zipBytes));
 assertNotNull(inZip);
 byte[] tarBytes = new byte[0];
 ArchiveInputStream inTar = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(tarBytes));
 assertNotNull(inTar);
 byte[] jarBytes = new byte[0];
 ArchiveInputStream inJar = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(jarBytes));
 assertNotNull(inJar);
 byte[] cpioBytes = new byte[0];
 ArchiveInputStream inCpio = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(cpioBytes));
 assertNotNull(inCpio);
 byte[] arBytes = new byte[0];
 ArchiveInputStream inAr = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(arBytes));
 assertNotNull(inAr);
 byte[] dumpBytes = new byte[0];
 ArchiveInputStream inDump = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(dumpBytes));
 assertNotNull(inDump);

 // Test with invalid archiver names
 try {
 new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
 assert(false); // Should throw ArchiveException
 } catch (ArchiveException e) {
 // Expected
 }
 }
}