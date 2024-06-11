import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ZipArchiveInputStreamTest {

 @Test
 public void testEmptyZipFile() throws Exception {
 ZipFile zipFile = new ZipFile("src/test/resources/empty.zip");
 try (ZipArchiveInputStream is = new ZipArchiveInputStream(zipFile.getInputStream())) {
 assertEquals(0, is.getNextEntry().getSize());
 assertFalse(is.hasNextEntry());
 }
 zipFile.close();
 }

 @Test
 public void testBasicZipFile() throws Exception {
 ZipFile zipFile = new ZipFile("src/test/resources/basic.zip");
 try (ZipArchiveInputStream is = new ZipArchiveInputStream(zipFile.getInputStream())) {
 ZipArchiveEntry entry = is.getNextEntry();
 assertEquals("test/file1.txt", entry.getName());
 assertEquals("UTF-8", entry.getEncoding());
 assertEquals("application/octet-stream", entry.getCompressedSize());
 assertEquals(-1L, entry.getSize());
 assertNull(entry.getExtra());
 assertFalse(is.hasDataDescriptor());
 byte[] buffer = new byte[1024];
 int read = is.read(buffer);
 assertEquals(read, 10); // the first 10 bytes of the file
 assertSame(entry, is.getNextEntry());
 read = is.read(buffer);
 assertEquals(read, 13); // the next 13 bytes of the file
 assertNull(is.getNextEntry());
 assertEquals(-1, is.read(new byte[5]));
 assertFalse(is.hasNextEntry());
 }
 zipFile.close();
 }

 @Test
 public void testUnicodeExtraFields() throws Exception {
 ZipFile zipFile = new ZipFile("src/test/resources/unicode.zip");
 try (ZipArchiveInputStream is = new ZipArchiveInputStream(zipFile.getInputStream())) {
 ZipArchiveEntry entry = is.getNextEntry();
 assertEquals("test/file1.txt", entry.getName());
 assertEquals("UTF-8", entry.getEncoding());
 assertEquals("application/octet-stream", entry.getCompressedSize());
 assertEquals(-1L, entry.getSize());
 byte[] extra = entry.getExtra();
 String decodedExtra = new String(extra, "UTF-8");
 assertEquals("test extra\r\nfield\r\n", decodedExtra);
 is.closeEntry();
 entry = is.getNextEntry();
 assertEquals("test/file2.txt", entry.getName());
 assertEquals("UTF-8", entry.getEncoding());
 assertEquals("application/octet-stream", entry.getCompressedSize());
 assertEquals(-1L, entry.getSize());
 assertNull(entry.getExtra());
 }
 zipFile.close();
 }

 @Test
 public void testDataDescriptor() throws Exception {
 ZipFile zipFile = new ZipFile("src/test/resources/datadescriptor.zip");
 try (ZipArchiveInputStream is = new ZipArchiveInputStream(zipFile.getInputStream())) {
 ZipArchiveEntry entry = is.getNextEntry();
 assertEquals("test/file1.txt", entry.getName());
 assertEquals("UTF-8", entry.getEncoding());
 assertEquals("application/octet-stream", entry.getCompressedSize());
 assertEquals(-1L, entry.getSize());
 assertFalse(is.hasDataDescriptor());
 is.closeEntry();
 entry = is.getNextEntry();
 assertEquals("test/file2.txt", entry.getName());
 assertEquals("UTF-8", entry.getEncoding());
 assertEquals("application/octet-stream", entry.getCompressedSize());
 assertEquals(-1L, entry.getSize());
 assertTrue(is.hasDataDescriptor());
 is.closeEntry();
 entry = is.getNextEntry();
 assertNull(entry);
 }
 zipFile.close();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeBufferLength() throws Exception {
 try (ZipArchiveInputStream is = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[0]))) {
 is.read(new byte[-1][]);
 }
 }

 @Test(expected = IOException.class)
 public void testEOFException() throws Exception {
 try (ZipArchiveInputStream is = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[0]))) {
 is.getNextEntry();
 is.read(new byte[10]);
 is.read(new byte[10]);
 is.read(new byte[1]);
 }
 }

 @Test
 public void testStreamClosed() throws Exception {
 try (ZipArchiveInputStream is = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[0]))) {
 is.close();
 assertFalse(is.hasNextEntry());
 }
 }
}