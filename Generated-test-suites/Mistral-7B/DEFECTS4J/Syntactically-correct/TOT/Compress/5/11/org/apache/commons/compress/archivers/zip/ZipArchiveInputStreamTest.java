import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class ZipArchiveInputStreamTest {

 private ZipArchiveInputStream zipInputStream;
 private ZipFile zipFile;

 @Before
 public void setup() throws Exception {
 zipFile = new ZipFile("test.zip");
 zipInputStream = new ZipArchiveInputStream(zipFile.getInputStream(
 "testFile.txt"));
 }

 @Test
 public void testGetNextEntry() throws Exception {
 ZipArchiveEntry entry = zipInputStream.getNextEntry();
 Assert.assertNotNull(entry);
 Assert.assertEquals("testFile.txt", entry.getName());
 }

 @Test
 public void testRead() throws Exception {
 byte[] buffer = new byte[1024];
 int length = zipInputStream.read(buffer);
 Assert.assertNotEqual(-1, length);
 Assert.assertEquals(buffer.length, length);
 }

 @Test
 public void testReadWithPartialData() throws Exception {
 byte[] buffer = new byte[1024];
 int length = zipInputStream.read(buffer, 256, 512);
 Assert.assertNotEqual(-1, length);
 Assert.assertEquals(768, length);
 }

 @Test(expected = IOException.class)
 public void testInvalidSignature() throws Exception {
 byte[] invalidSignature = new byte[] { (byte) 0x04, 0x03, 0x4d, 0x50, 0x4b, 0x50, 0x05, 0x04 };
 InputStream input = new ByteArrayInputStream(invalidSignature);
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(input);
 zipInputStream.getNextEntry();
 }

 @Test(expected = IOException.class)
 public void testNoEntry() throws Exception {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
 new ByteArrayInputStream(new byte[1]));
 zipInputStream.getNextEntry();
 }

 @Test(expected = EOFException.class)
 public void testEndOfFile() throws Exception {
 byte[] buffer = new byte[1];
 int length = zipInputStream.read(buffer);
 int length2 = zipInputStream.read(buffer);
 Assert.assertEquals(-1, length2);
 }

 @Test(expected = IOException.class)
 public void testReadWithClosedStream() throws Exception {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
 zipFile.getInputStream("testFile.txt"));
 zipInputStream.close();
 int length = zipInputStream.read(buffer);
 }

 @Test
 public void testCloseEntry() throws Exception {
 ZipArchiveEntry entry = zipInputStream.getNextEntry();
 Assert.assertNotNull(entry);
 zipInputStream.closeEntry();
 Assert.assertNull(zipInputStream.getNextEntry());
 }

 @Test
 public void testReadWithClosedStreamAfterReadingData() throws Exception {
 byte[] buffer = new byte[1];
 int length = zipInputStream.read(buffer);
 zipInputStream.closeEntry();
 int length2 = zipInputStream.read(buffer);
 Assert.assertEquals(-1, length2);
 }

 @Test
 public void testReadWithLargeData() throws Exception {
 byte[] buffer = new byte[1024 * 10];
 int length = zipInputStream.read(buffer);
 Assert.assertNotEqual(-1, length);
 }

 @Test
 public void testReadWithZeroData() throws Exception {
 byte[] buffer = new byte[0];
 int length = zipInputStream.read(buffer);
 Assert.assertEquals(0, length);
 }
}