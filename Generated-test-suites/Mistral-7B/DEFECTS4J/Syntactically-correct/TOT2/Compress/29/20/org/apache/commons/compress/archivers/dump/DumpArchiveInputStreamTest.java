import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DumpArchiveInputStreamTest {

 private ArchiveInputStream input;
 private ZipEncoding encoding;

 @Before
 public void setUp() throws Exception {
 input = new DumpArchiveInputStream(TestUtils.getTestResourceAsStream("test-dump.dump"));
 encoding = ZipEncodingHelper.getZipEncoding(null);
 }

 @Test
 public void testGetNextEntry() throws IOException {
 DumpArchiveEntry entry;
 for (int i = 0; i < 5; i++) {
 entry = input.getNextEntry();
 assertEquals("Entry " + i, entry.getName(), "entry" + i);
 }
 assertEquals(null, input.getNextEntry());
 }

 @Test(expected = IOException.class)
 public void testGetNextEntryAfterEOF() throws IOException {
 input.getNextEntry();
 input.getNextEntry();
 input.close();
 input.getNextEntry();
 }

 @Test
 public void testRead() throws IOException {
 byte[] buf = new byte[10];
 int len;
 for (int i = 0; i < 5; i++) {
 len = input.read(buf);
 assertEquals("Read " + i, len, buf.length);
 assertArrayEquals("Contents " + i, "test".getBytes(), Arrays.copyOfRange(buf, 0, len));
 }
 len = input.read(buf);
 assertEquals(-1, len);
 }

 @Test(expected = EOFException.class)
 public void testReadAfterEOF() throws IOException {
 input.close();
 input.read(new byte[10]);
 }

 @Test
 public void testReadLargeFile() throws IOException {
 byte[] buf = new byte[1024];
 int len;
 for (int i = 0; i < 1024; i += buf.length) {
 len = input.read(buf);
 assertNotEquals(-1, len);
 }
 }

 @Test
 public void testClose() throws IOException {
 input.close();
 input.getNextEntry();
 }

 @Test(expected = ArchiveException.class)
 public void testInvalidFile() throws Exception {
 input = new DumpArchiveInputStream(TestUtils.getTestResourceAsStream("invalid-dump.dump"));
 input.getNextEntry();
 }

 @Test(expected = IOException.class)
 public void testIOException() throws Exception {
 input = new DumpArchiveInputStream(TestUtils.getTestResourceAsStream("test-dump.dump"), encoding);
 input.getNextEntry();
 input.getInputStream().close();
 input.getNextEntry();
 }
}