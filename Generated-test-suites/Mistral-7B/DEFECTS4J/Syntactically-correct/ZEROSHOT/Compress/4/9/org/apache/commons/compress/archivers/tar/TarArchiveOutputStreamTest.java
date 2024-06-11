import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

 private File testFile;
 private OutputStream outputStream;
 private TarArchiveOutputStream tarOutputStream;

 @Before
 public void setUp() throws Exception {
 testFile = new File("TarArchiveOutputStreamTest.tar");
 outputStream = new FileOutputStream(testFile);
 tarOutputStream = new TarArchiveOutputStream(outputStream);
 }

 @After
 public void tearDown() throws Exception {
 if (tarOutputStream != null) {
 tarOutputStream.finish();
 tarOutputStream.close();
 }
 if (outputStream != null) {
 outputStream.close();
 }
 if (testFile.exists()) {
 testFile.delete();
 }
 }

 @Test
 public void testConstructor() {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MemoryOutputStream());
 assertEquals(TarConstants.DEFAULT_BLKSIZE, tarArchiveOutputStream.getBlockSize());
 assertEquals(TarConstants.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
 }

 @Test
 public void testSetLongFileMode() {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MemoryOutputStream());
 tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
 }

 @Test
 public void testFinish() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MemoryOutputStream());
 tarArchiveOutputStream.finish();
 }

 @Test
 public void testClose() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MemoryOutputStream());
 tarArchiveOutputStream.close();
 }

 @Test
 public void testGetRecordSize() {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MemoryOutputStream());
 assertEquals(TarConstants.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
 }

 @Test
 public void testPutArchiveEntryWithFileNameLessThanNameLen() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
 TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFileName.txt");
 tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
 tarArchiveOutputStream.closeArchiveEntry();
 tarArchiveOutputStream.finish();
 tarArchiveOutputStream.close();
 }

 @Test
 public void testPutArchiveEntryWithFileNameLongerThanNameLen() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
 tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
 TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFileNameThatIsTooLongForNameLen.txt");
 tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
 tarArchiveOutputStream.closeArchiveEntry();
 tarArchiveOutputStream.finish();
 tarArchiveOutputStream.close();
 }

 @Test
 public void testPutArchiveEntryTwice() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
 TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFileName.txt");
 tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
 tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
 tarArchiveOutputStream.closeArchiveEntry();
 tarArchiveOutputStream.closeArchiveEntry();
 tarArchiveOutputStream.finish();
 tarArchiveOutputStream.close();
 }

 @Test
 public void testPutArchiveEntryWithHeaderSizeMismatch() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
 TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFileName.txt");
 tarArchiveEntry.setSize(Long.MAX_VALUE + 1);
 tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
 assertThrows(IOException.class, () -> tarArchiveOutputStream.closeArchiveEntry());
 }

 @Test
 public void testCloseArchiveEntryWithoutPutArchiveEntry() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
 assertThrows(IllegalStateException.class, () -> tarArchiveOutputStream.closeArchiveEntry());
 }

 @Test
 public void testWrite() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
 TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFileName.txt");
 tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
 byte[] testData = new byte[]{'T', 'e', 's', 't', ' ', 'D', 'a', 't', 'a', 0x00};
 tarArchiveOutputStream.write(testData, 0, testData.length);
 tarArchiveOutputStream.closeArchiveEntry();
 tarArchiveOutputStream.finish();
 tarArchiveOutputStream.close();

 byte[] resultData = new byte[testFile.length()];
 FileInputStream fileInputStream = new FileInputStream(testFile);
 fileInputStream.read(resultData);
 fileInputStream.close();

 assertArrayEquals(testData, resultData);
 }

 @Test
 public void testWriteWithUnclosedEntry() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
 TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFileName.txt");
 tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
 byte[] testData = new byte[]{'T', 'e', 's', 't', ' ', 'D', 'a', 't', 'a', 0x00};
 tarArchiveOutputStream.write(testData, 0, testData.length);
 assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
 tarArchiveOutputStream.closeArchiveEntry();
 tarArchiveOutputStream.finish();
 tarArchiveOutputStream.close();
 }

 @Test
 public void testWriteWithUnclosedEntryWithoutFinish() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
 TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFileName.txt");
 tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
 byte[] testData = new byte[]{'T', 'e', 's', 't', ' ', 'D', 'a', 't', 'a', 0x00};
 tarArchiveOutputStream.write(testData, 0, testData.length);
 assertTrue(tarArchiveOutputStream.haveUnclosedEntry);
 tarArchiveOutputStream.close();
 }

 @Test
 public void testFinishWithUnclosedEntry() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
 TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFileName.txt");
 tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
 byte[] testData = new byte[]{'T', 'e', 's', 't', ' ', 'D', 'a', 't', 'a', 0x00};
 tarArchiveOutputStream.write(testData, 0, testData.length);
 tarArchiveOutputStream.finish();
 assertNull(tarArchiveOutputStream.getNextEntry());
 tarArchiveOutputStream.close();
 }

 @Test
 public void testPutArchiveEntryWithDirectory() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
 TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testDirectory/");
 tarArchiveEntry.setSize(0);
 tarArchiveEntry.setDirectory(true);
 tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
 tarArchiveOutputStream.closeArchiveEntry();
 tarArchiveOutputStream.finish();
 tarArchiveOutputStream.close();
 }
}