import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

 private File tempFile;
 private TarArchiveOutputStream os;

 @Before
 public void setUp() throws Exception {
 tempFile = File.createTempFile("test-", ".tar");
 os = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream(tempFile)),
 TarConstants.DEFAULT_BLKSIZE, TarConstants.DEFAULT_RCDSIZE);
 }

 @After
 public void tearDown() throws Exception {
 if (os != null) {
 os.close();
 }
 if (tempFile != null && tempFile.exists()) {
 tempFile.delete();
 }
 }

 @Test
 public void testPutArchiveEntry() throws Exception {
 TarArchiveEntry entry = new TarArchiveEntry("test.txt", "Test file content");
 os.putArchiveEntry(entry);
 os.write("Hello World!".getBytes());
 os.closeArchiveEntry();
 }

 @Test
 public void testPutArchiveEntryWithLongFileName() throws Exception {
 String longFileName = "test_entry_with_long_name_that_is_longer_than_the_default_name_length_of_100_bytes.txt";
 TarArchiveEntry entry = new TarArchiveEntry(longFileName, "Test file content");
 os.putArchiveEntry(entry);
 os.write("Hello Long Name Test!".getBytes());
 os.closeArchiveEntry();
 }

 @Test(expected = RuntimeException.class)
 public void testPutArchiveEntryWithTooLongFileName() throws Exception {
 String tooLongFileName = "test_entry_with_too_long_name_that_is_longer_than_the_maximum_name_length_of_255_bytes.txt";
 TarArchiveEntry entry = new TarArchiveEntry(tooLongFileName, "Test file content");
 os.putArchiveEntry(entry);
 }

 @Test
 public void testFinish() throws Exception {
 os.finish();
 }

 @Test
 public void testClose() throws Exception {
 os.close();
 }

 @Test
 public void testWrite() throws Exception {
 byte[] data = "Test data".getBytes();
 os.write(data, 0, data.length);
 os.finish();
 }

 @Test
 public void testWriteWithLargeData() throws Exception {
 byte[] data = new byte[1024 * 1024]; // 1 MB
 os.write(data, 0, data.length);
 os.finish();
 }

 @Test
 public void testCreateArchiveEntry() throws Exception {
 File testFile = File.createTempFile("test-", ".txt");
 TarArchiveEntry entry = os.createArchiveEntry(testFile, "Test file content");
 os.putArchiveEntry(entry);
 testFile.delete();
 }

 @Test(expected = IOException.class)
 public void testPutArchiveEntryWithoutCloseArchiveEntry() throws Exception {
 TarArchiveEntry entry = new TarArchiveEntry("test.txt", "Test file content");
 os.putArchiveEntry(entry);
 }

 @Test(expected = IOException.class)
 public void testPutArchiveEntryWithClosedArchiveStream() throws Exception {
 TarArchiveEntry entry = new TarArchiveEntry("test.txt", "Test file content");
 os.finish();
 os.putArchiveEntry(entry);
 }

 @Test
 public void testGetRecordSize() {
 assertEquals(TarConstants.DEFAULT_RCDSIZE, os.getRecordSize());
 }

 @Test
 public void testSetLongFileMode() {
 os.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
 }

 @Test
 public void testGetLongFileMode() {
 assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, os.getLongFileMode());
 }
}