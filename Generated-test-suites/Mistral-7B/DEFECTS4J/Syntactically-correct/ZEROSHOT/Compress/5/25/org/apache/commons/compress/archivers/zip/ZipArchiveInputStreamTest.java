import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ZipArchiveInputStreamTest {

 private InputStream inputStream;
 private ZipArchiveInputStream zipArchiveInputStream;
 private ZipArchiveEntry zipArchiveEntry;

 @Before
 public void setUp() throws Exception {
 inputStream = this.getClass().getResourceAsStream("/test.zip");
 zipArchiveInputStream = new ZipArchiveInputStream(inputStream, null, false);
 }

 @After
 public void tearDown() throws Exception {
 zipArchiveInputStream.close();
 IOUtils.closeQuietly(inputStream);
 }

 @Test
 public void testGetNextZipEntry() throws Exception {
 zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
 assertNotNull(zipArchiveEntry);
 }

 @Test(expected = IOException.class)
 public void testGetNextZipEntryWhenFileIsEmpty() throws Exception {
 zipArchiveInputStream.getNextZipEntry();
 zipArchiveInputStream.getNextZipEntry();
 assertNotNull(zipArchiveInputStream.getNextZipEntry());
 }

 @Test
 public void testRead() throws Exception {
 byte[] buffer = new byte[ZipArchiveOutputStream.BUFFER_SIZE];
 for (int i = 0; i < buffer.length; i++) {
 buffer[i] = 0x01;
 }
 zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
 int length = zipArchiveInputStream.read(buffer, 0, buffer.length);
 assertEquals(length, buffer.length);
 }

 @Test(expected = IOException.class)
 public void testReadWhenStreamIsClosed() throws Exception {
 zipArchiveInputStream.close();
 int length = zipArchiveInputStream.read(new byte[1], 0, 1);
 }

 @Test
 public void testCloseEntry() throws Exception {
 zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
 int length = zipArchiveInputStream.read(new byte[1], 0, 1);
 zipArchiveInputStream.closeEntry();
 zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
 length = zipArchiveInputStream.read(new byte[1], 0, 1);
 }

 @Test
 public void testReadFromDataDescriptor() throws Exception {
 zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
 int length = zipArchiveInputStream.read(new byte[1], 0, 1);
 zipArchiveInputStream.getNextZipEntry();
 zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
 int readBytesOfEntry = zipArchiveInputStream.readBytesOfEntry();
 assertEquals(readBytesOfEntry, zipArchiveEntry.getSize());
 }
}