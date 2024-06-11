import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.util.IOUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DumpArchiveInputStreamTest {

 private InputStream inputStream;
 private DumpArchiveInputStream dumpArchiveInputStream;

 @Before
 public void setUp() throws Exception {
 inputStream = new MockInputStream(new byte[]{"\000\012\020Dump\000\014\001"
         + "\000\022\005\000\021\000\021\022\000"
         + "\021\031\000\000\020\001\000\003\005"
         + "\000\000\002\030\004\001\000"});
 dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);
 }

 @Test
 public void testGetNextEntry() throws Exception {
 DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
 assertNotNull(entry);
 assertEquals("", entry.getName());
 assertEquals(1, entry.getIno());
 assertEquals(1, entry.getParentIno());
 assertEquals(3, entry.getFormatVersion());
 assertEquals(false, entry.isDirectory());
 assertEquals(0L, entry.getSize());
 assertEquals(0, entry.getOffset());
 assertEquals(0L, entry.getCrc());
 }

 @Test(expected = ArchiveException.class)
 public void testGetNextEntry_EndOfArchive() throws Exception {
 dumpArchiveInputStream.getNextEntry();
 dumpArchiveInputStream.getNextEntry();
 assertFalse(dumpArchiveInputStream.hasNextEntry());
 dumpArchiveInputStream.getNextEntry();
 }

 @Test
 public void testGetSummary() throws Exception {
 DumpArchiveSummary summary = dumpArchiveInputStream.getSummary();
 assertNotNull(summary);
 assertEquals(3, summary.getFormatVersion());
 assertEquals(2, summary.getMagic());
 }

 @Test(expected = IOException.class)
 public void testGetNextEntry_IOException() throws Exception {
 inputStream = new MockInputStream(new byte[]{"\000\012\020Dump\000\014\001"
         + "\000\022\005\000\021\000\021\022\000"
         + "\021\031\000\000\020\001\000\003\005"
         + "\000\000\002\030\004\001\000\015"});
 dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);
 dumpArchiveInputStream.getNextEntry();
 }

 @Test(expected = EOFException.class)
 public void testGetNextEntry_EOF() throws Exception {
 DumpArchiveInputStream newDumpArchiveInputStream = new DumpArchiveInputStream(
         new ByteArrayInputStream(new byte[0]));
 newDumpArchiveInputStream.getNextEntry();
 newDumpArchiveInputStream.getNextEntry();
 assertFalse(newDumpArchiveInputStream.hasNextEntry());
 newDumpArchiveInputStream.getNextEntry();
 }

 @Test(expected = ArchiveException.class)
 public void testGetNextEntry_InvalidMagic() throws Exception {
 inputStream = new MockInputStream(new byte[]{"\000\012\020Dump\000\014\001"
         + "\000\022\005\000\021\000\021\022\000"
         + "\021\031\000\000\020\001\000\003\005"
         + "\000\000\002\032\004\001\000"});
 dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);
 dumpArchiveInputStream.getNextEntry();
 }

 @Test
 public void testGetNextEntry_NextEntryIsDirectory() throws Exception {
 DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
 assertNotNull(entry);
 assertEquals("", entry.getName());
 assertEquals(1, entry.getIno());
 assertEquals(1, entry.getParentIno());
 assertEquals(3, entry.getFormatVersion());
 assertEquals(true, entry.isDirectory());
 assertEquals(0L, entry.getSize());
 assertEquals(0, entry.getOffset());
 assertEquals(0L, entry.getCrc());
 }

 @Test
 public void testGetNextEntry_ReadDirectoryEntry() throws Exception {
 DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
 assertNotNull(entry);
 assertEquals("", entry.getName());
 assertEquals(1, entry.getIno());
 assertEquals(1, entry.getParentIno());
 assertEquals(3, entry.getFormatVersion());
 assertEquals(false, entry.isDirectory());
 assertEquals(0L, entry.getSize());
 assertEquals(0, entry.getOffset());
 assertEquals(0L, entry.getCrc());

 DumpArchiveEntry directory = dumpArchiveInputStream.getNextEntry();
 assertNotNull(directory);
 assertEquals("", directory.getName());
 assertEquals(2, directory.getIno());
 assertEquals(1, directory.getParentIno());
 assertEquals(3, directory.getFormatVersion());
 assertEquals(true, directory.isDirectory());
 assertEquals(0L, directory.getSize());
 assertEquals(0, directory.getOffset());
 assertEquals(0L, directory.getCrc());
 }

 @Test
 public void testRead() throws Exception {
 byte[] buffer = new byte[5];
 int result = dumpArchiveInputStream.read(buffer, 0, 5);
 assertEquals(5, result);
 assertEquals(new String(buffer, 0, 5), "Dump");
 }

 @Test
 public void testClose() throws Exception {
 dumpArchiveInputStream.close();
 }

 @Test(expected = ArchiveException.class)
 public void testGetNextEntry_AfterClose() throws Exception {
 dumpArchiveInputStream.close();
 dumpArchiveInputStream.getNextEntry();
 }

 @Test
 public void testReadAllBytes() throws Exception {
 byte[] bytes = IOUtils.toByteArray(dumpArchiveInputStream);
 }

 @Test(expected = IOException.class)
 public void testGetNextEntry_NullInputStream() throws Exception {
 new DumpArchiveInputStream(null);
 }
}