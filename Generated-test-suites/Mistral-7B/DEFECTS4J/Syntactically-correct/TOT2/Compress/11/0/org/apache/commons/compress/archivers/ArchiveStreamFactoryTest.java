import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
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
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

 private static final String TEST_ARCHIVE_NAME = "testArchive";

 @Test
 public void testCreateArchiveInputStream() throws Exception {
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
 assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
 archiveInputStream.close();
 }

 @Test
 public void testCreateArchiveInputStream_ArchiverNotFound() throws Exception {
 try {
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream("unknownArchiver", new ByteArrayInputStream(new byte[0]));
 fail("Expected ArchiveException to be thrown.");
 } catch (ArchiveException e) {
 // expected
 }
 }

 @Test
 public void testCreateArchiveInputStream_InvalidInputStream() throws Exception {
 try {
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
 fail("Expected IllegalArgumentException to be thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCreateArchiveOutputStream() throws Exception {
 OutputStream outputStream = new ByteArrayOutputStream();
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
 assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
 archiveOutputStream.finish();
 archiveOutputStream.close();
 }

 @Test
 public void testCreateArchiveOutputStream_ArchiverNotFound() throws Exception {
 try {
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream("unknownArchiver", new ByteArrayOutputStream());
 fail("Expected ArchiveException to be thrown.");
 } catch (ArchiveException e) {
 // expected
 }
 }

 @Test
 public void testCreateArchiveOutputStream_InvalidOutputStream() throws Exception {
 try {
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
 fail("Expected IllegalArgumentException to be thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCreateArchiveInputStreamFromSignature() throws Exception {
 InputStream inputStream = new ByteArrayInputStream(TEST_ARCHIVE_NAME.getBytes());
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
 assertTrue(archiveInputStream instanceof TarArchiveInputStream);
 archiveInputStream.close();
 }

 @Test
 public void testCreateArchiveOutputStream_InvalidSignature() throws Exception {
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 try {
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
 fail("Expected ArchiveException to be thrown.");
 } catch (ArchiveException e) {
 // expected
 }
 }

 @Test
 public void testCreateArchiveOutputStream_ZipFile() throws Exception {
 // Create a zip file with a single entry
 OutputStream outputStream = new FileOutputStream("test.zip");
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
 archiveOutputStream.putArchiveEntry(new ZipArchiveEntry("test.txt"));
 archiveOutputStream.writeTo(new ByteArrayInputStream("Test content.".getBytes()), 0, "test.txt".length());
 archiveOutputStream.closeArchiveEntry();
 archiveOutputStream.finish();
 archiveOutputStream.close();

 // Test reading the created file
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, new FileInputStream("test.zip"));
 ZipArchiveEntry entry = (ZipArchiveEntry) archiveInputStream.getNextEntry();
 assertEquals("test.txt", entry.getName());
 archiveInputStream.close();
 }

 @Test
 public void testCreateArchiveOutputStream_TarFile() throws Exception {
 // Create a tar file with a single entry
 OutputStream outputStream = new FileOutputStream("test.tar");
 ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
 archiveOutputStream.putNextEntry(new TarEntry("test.txt"));
 archiveOutputStream.writeTo(new ByteArrayInputStream("Test content.".getBytes()), 0, "test.txt".length());
 archiveOutputStream.finishEntry();
 archiveOutputStream.finish();
 archiveOutputStream.close();

 // Test reading the created file
 ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, new FileInputStream("test.tar"));
 TarEntry entry = (TarEntry) archiveInputStream.getNextEntry();
 assertEquals("test.txt", entry.getName());
 archiveInputStream.close();
 }
}