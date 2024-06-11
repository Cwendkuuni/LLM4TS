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
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

 private InputStream inputStream;
 private OutputStream outputStream;

 @Before
 public void setUp() throws Exception {
 inputStream = new ByteArrayInputStream(new byte[0]);
 outputStream = new ByteArrayOutputStream();
 }

 @After
 public void tearDown() throws Exception {
 IOUtils.closeQuietly(inputStream);
 IOUtils.closeQuietly(outputStream);
 }

 @Test
 public void testCreateArchiveInputStream_AR() throws Exception {
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
 assertEquals(ArArchiveInputStream.class, archiveInputStream.getClass());
 }

 @Test
 public void testCreateArchiveInputStream_Cpio() throws Exception {
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
 assertEquals(CpioArchiveInputStream.class, archiveInputStream.getClass());
 }

 @Test
 public void testCreateArchiveInputStream_Dump() throws Exception {
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
 assertEquals(DumpArchiveInputStream.class, archiveInputStream.getClass());
 }

 @Test
 public void testCreateArchiveInputStream_Jar() throws Exception {
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
 assertEquals(JarArchiveInputStream.class, archiveInputStream.getClass());
 }

 @Test
 public void testCreateArchiveInputStream_Tar() throws Exception {
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
 assertEquals(TarArchiveInputStream.class, archiveInputStream.getClass());
 }

 @Test
 public void testCreateArchiveInputStream_Zip() throws Exception {
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
 assertEquals(ZipArchiveInputStream.class, archiveInputStream.getClass());
 }

 @Test
 public void testCreateArchiveInputStream_Unknown() throws Exception {
 try {
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("unknown", inputStream);
 fail("Expected ArchiveException");
 } catch (ArchiveException ex) {
 // Expected
 }
 }

 @Test
 public void testCreateArchiveOutputStream_AR() throws Exception {
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
 assertEquals(ArArchiveOutputStream.class, archiveOutputStream.getClass());
 }

 @Test
 public void testCreateArchiveOutputStream_Cpio() throws Exception {
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
 assertEquals(CpioArchiveOutputStream.class, archiveOutputStream.getClass());
 }

 @Test
 public void testCreateArchiveOutputStream_Dump() throws Exception {
 try {
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, outputStream);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException ex) {
 // Expected
 }
 }

 @Test
 public void testCreateArchiveOutputStream_Jar() throws Exception {
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
 assertEquals(JarArchiveOutputStream.class, archiveOutputStream.getClass());
 }

 @Test
 public void testCreateArchiveOutputStream_Tar() throws Exception {
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
 assertEquals(TarArchiveOutputStream.class, archiveOutputStream.getClass());
 }

 @Test
 public void testCreateArchiveOutputStream_Zip() throws Exception {
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
 assertEquals(ZipArchiveOutputStream.class, archiveOutputStream.getClass());
 }

 @Test
 public void testCreateArchiveOutputStream_Unknown() throws Exception {
 try {
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream("unknown", outputStream);
 fail("Expected ArchiveException");
 } catch (ArchiveException ex) {
 // Expected
 }
 }

 @Test
 public void testCreateArchiveInputStreamFromBytes() throws Exception {
 InputStream testInputStream = new ByteArrayInputStream(new byte[]{"!<arch>\0"});
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.AR, testInputStream);
 ArArchiveEntry entry = (ArArchiveEntry) archiveInputStream.getNextEntry();
 assertEquals("test", entry.getName());
 }

 @Test(expected = ArchiveException.class)
 public void testCreateArchiveInputStreamFromBytes_Unknown() throws Exception {
 InputStream testInputStream = new ByteArrayInputStream(new byte[]{"!<test>\0"});
 ArchiveStreamFactory.newInstance().createArchiveInputStream("unknown", testInputStream);
 }
}