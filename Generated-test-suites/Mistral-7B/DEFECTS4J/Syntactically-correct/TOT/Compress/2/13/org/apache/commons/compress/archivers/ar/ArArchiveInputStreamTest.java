import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArArchiveInputStreamTest {

 private InputStream inputStream;
 private ArArchiveInputStream archiveInputStream;

 @Before
 public void setUp() throws Exception {
 inputStream = this.getClass().getResourceAsStream("/test-data/test-archive.tar.gz");
 archiveInputStream = new ArArchiveInputStream(inputStream);
 }

 @Test
 public void testGetNextArEntry() throws Exception {
 ArArchiveEntry expectedEntry = new ArArchiveEntry("entry1.txt", 10);
 ArArchiveEntry actualEntry = archiveInputStream.getNextArEntry();
 assertEquals(expectedEntry.getName(), actualEntry.getName());
 assertEquals(expectedEntry.getSize(), actualEntry.getSize());

 // Test getting the next entry after the first one
 ArArchiveEntry nextEntry = archiveInputStream.getNextArEntry();
 assertNotNull(nextEntry);
 assertNotSame(expectedEntry, nextEntry);
 }

 @Test(expected = IOException.class)
 public void testGetNextArEntryWhenEndOfStreamReached() throws Exception {
 archiveInputStream.getNextArEntry();
 archiveInputStream.getNextArEntry();
 archiveInputStream.getNextArEntry(); // This should throw an IOException
 }

 @Test(expected = IOException.class)
 public void testGetNextArEntryWithInvalidHeader() throws Exception {
 // Create a InputStream with invalid header
 InputStream invalidInput = this.getClass().getResourceAsStream("/test-data/invalid-archive.tar.gz");
 ArArchiveInputStream invalidArchiveInputStream = new ArArchiveInputStream(invalidInput);
 ArArchiveEntry entry = invalidArchiveInputStream.getNextArEntry();
 fail("Getting a entry from an invalid archive should not be possible");
 }

 @Test
 public void testGetNextEntry() throws Exception {
 ArchiveEntry expectedEntry = expectedEntry = archiveInputStream.getNextArEntry();
 ArchiveEntry actualEntry = archiveInputStream.getNextEntry();
 assertEquals(expectedEntry.getClass(), actualEntry.getClass());
 assertEquals(expectedEntry.getName(), actualEntry.getName());
 assertEquals(expectedEntry.getSize(), actualEntry.getSize());
 }

 @Test(expected = IOException.class)
 public void testGetNextEntryWhenEndOfStreamReached() throws Exception {
 archiveInputStream.getNextArEntry();
 archiveInputStream.getNextArEntry();
 ArchiveEntry entry = archiveInputStream.getNextEntry();
 fail("Getting an entry from an end of stream should not be possible");
 }

 @Test
 public void testClose() throws Exception {
 archiveInputStream.close();
 assertFalse(archiveInputStream.isOpen());
 }

 @Test
 public void testRead() throws Exception {
 byte[] buffer = new byte[10];
 int bytesRead = archiveInputStream.read(buffer);
 assertNotSame(-1, bytesRead);
 assertEquals(bytesRead, buffer.length);
 }

 @Test
 public void testReadArray() throws Exception {
 byte[] buffer = new byte[10];
 int bytesRead = archiveInputStream.read(buffer, 0, buffer.length);
 assertNotSame(-1, bytesRead);
 assertEquals(bytesRead, buffer.length);
 }

 @Test
 public void testReadWithoutConsuming() throws Exception {
 byte[] buffer = new byte[10];
 int bytesRead = archiveInputStream.read(buffer, 0, buffer.length);
 assertEquals(bytesRead, buffer.length);
 int bytesRead2 = archiveInputStream.read(buffer, 0, buffer.length);
 assertNotSame(bytesRead, bytesRead2);
 }

 @Test(expected = IOException.class)
 public void testReadWithNegativeOffset() throws Exception {
 archiveInputStream.read(-1);
 fail("Reading with a negative offset should not be possible");
 }

 @Test(expected = IOException.class)
 public void testReadWithNullBuffer() throws Exception {
 archiveInputStream.read(null);
 fail("Reading with a null buffer should not be possible");
 }

 @Test(expected = IOException.class)
 public void testReadWithNegativeLength() throws Exception {
 archiveInputStream.read(new byte[]{-1}, 0, -1);
 fail("Reading with a negative length should not be possible");
 }

 @Test
 public void testReadAll() throws Exception {
 byte[] buffer = new byte[10];
 IOUtils.closeQuietly(archiveInputStream);
 int bytesRead = 0;
 while (bytesRead < buffer.length) {
 int read = archiveInputStream.read(buffer, bytesRead, buffer.length - bytesRead);
 bytesRead += read;
 }
 assertArrayEquals(buffer, archiveInputStream.readAllBytes());
 }

 @Test(expected = IOException.class)
 public void testReadAllWithClosedStream() throws Exception {
 IOUtils.closeQuietly(archiveInputStream);
 archiveInputStream.readAllBytes();
 fail("Reading all bytes from a closed stream should not be possible");
 }

}