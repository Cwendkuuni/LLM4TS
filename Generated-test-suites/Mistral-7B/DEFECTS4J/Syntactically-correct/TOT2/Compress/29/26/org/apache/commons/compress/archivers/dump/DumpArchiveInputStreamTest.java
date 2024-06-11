import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DumpArchiveInputStreamTest {

 private InputStream inputStream;
 private DumpArchiveInputStream dumpArchiveInputStream;
 private DumpArchiveSummary dumpArchiveSummary;

 @Before
 public void setUp() throws Exception {
 inputStream = new TestUtils.ByteArrayInputStream(TestData.DUMP_ARCHIVE_DATA);
 dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, null);
 dumpArchiveSummary = dumpArchiveInputStream.getSummary();
 }

 @Test
 public void testMatches() throws Exception {
 assertTrue(DumpArchiveInputStream.matches(TestData.DUMP_ARCHIVE_DATA,
 TestData.DUMP_ARCHIVE_DATA.length));
 }

 @Test
 public void testGetNextEntry() throws Exception {
 DumpArchiveEntry firstEntry = dumpArchiveInputStream.getNextEntry();
 assertEquals(1, firstEntry.getHeaderType());
 assertEquals(1, firstEntry.getHeaderCount());
 assertEquals(TestData.FIRST_ENTRY_NAME, firstEntry.getName());

 DumpArchiveEntry secondEntry = dumpArchiveInputStream.getNextEntry();
 assertEquals(1, secondEntry.getHeaderType());
 assertEquals(1, secondEntry.getHeaderCount());
 assertEquals(TestData.SECOND_ENTRY_NAME, secondEntry.getName());

 DumpArchiveEntry thirdEntry = dumpArchiveInputStream.getNextEntry();
 assertNull(thirdEntry);
 }

 @Test
 public void testGetSummary() throws Exception {
 assertEquals(TestData.EXPECTED_NAME_COUNT, dumpArchiveSummary.getEntryCount());
 assertEquals(TestData.EXPECTED_NAME_COUNT, dumpArchiveSummary.getFileCount());
 }

 @Test(expected = ArchiveException.class)
 public void testGetNextEntryAfterEnd() throws Exception {
 dumpArchiveInputStream.getNextEntry();
 dumpArchiveInputStream.getNextEntry();
 dumpArchiveInputStream.getNextEntry();
 assertNull(dumpArchiveInputStream.getNextEntry());
 }

 @Test
 public void testRead() throws Exception {
 byte[] buf = new byte[10];
 int bytesRead = dumpArchiveInputStream.read(buf, 0, buf.length);
 assertEquals(TestData.FIRST_ENTRY_SIZE, bytesRead);
 byte[] firstEntryData = new byte[TestData.FIRST_ENTRY_SIZE];
 System.arraycopy(buf, 0, firstEntryData, 0, TestData.FIRST_ENTRY_SIZE);
 assertArrayEquals(TestData.FIRST_ENTRY_DATA, firstEntryData);
 }

 @Test
 public void testClose() throws Exception {
 dumpArchiveInputStream.close();
 assertFalse(dumpArchiveInputStream.isOpen());
 }

}