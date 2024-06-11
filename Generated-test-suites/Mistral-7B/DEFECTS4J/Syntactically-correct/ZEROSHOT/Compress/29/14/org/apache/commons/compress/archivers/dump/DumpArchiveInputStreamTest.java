import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.junit.Test;
import static org.junit.Assert.*;

public class DumpArchiveInputStreamTest {

 private static final String TEST_FILE = "DumpArchiveInputStreamTest.class";

 @Test
 public void testMatches() throws IOException {
 byte[] buffer = readFile(TEST_FILE);
 assertTrue(DumpArchiveInputStream.matches(buffer, buffer.length));
 }

 @Test
 public void testNewInstance() throws IOException, ArchiveException {
 InputStream inputStream = new ByteArrayInputStream(readFile(TEST_FILE));
 DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);
 DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
 assertEquals("", entry.getName());
 assertEquals(2, entry.getSize());
 dumpArchiveInputStream.close();
 }

 @Test(expected = ArchiveException.class)
 public void testNewInstanceInvalidFormat() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[10]);
 DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);
 }

 @Test
 public void testGetNextEntry() throws IOException, ArchiveException {
 InputStream inputStream = new ByteArrayInputStream(readFile(TEST_FILE));
 DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);
 DumpArchiveEntry entry1 = dumpArchiveInputStream.getNextEntry();
 DumpArchiveEntry entry2 = dumpArchiveInputStream.getNextEntry();
 DumpArchiveEntry entry3 = dumpArchiveInputStream.getNextEntry();
 DumpArchiveEntry entry4 = dumpArchiveInputStream.getNextEntry();
 DumpArchiveEntry entry5 = dumpArchiveInputStream.getNextEntry();
 DumpArchiveEntry entry6 = dumpArchiveInputStream.getNextEntry();
 DumpArchiveEntry entry7 = dumpArchiveInputStream.getNextEntry();
 DumpArchiveEntry entry8 = dumpArchiveInputStream.getNextEntry();
 DumpArchiveEntry entry9 = dumpArchiveInputStream.getNextEntry();
 DumpArchiveEntry entry10 = dumpArchiveInputStream.getNextEntry();
 DumpArchiveEntry entry11 = dumpArchiveInputStream.getNextEntry();
 DumpArchiveEntry entry12 = dumpArchiveInputStream.getNextEntry();
 dumpArchiveInputStream.close();
 assertEquals("META.DIR/", entry1.getName());
 assertEquals(2212, entry1.getSize());
 assertEquals("META.DIR/.", entry2.getName());
 assertEquals(0, entry2.getSize());
 assertEquals("META.DIR/..", entry3.getName());
 assertEquals(0, entry3.getSize());
 assertEquals("META.DIR/.dmp", entry4.getName());
 assertEquals(95, entry4.getSize());
 assertEquals("META.DIR/MNT", entry5.getName());
 assertEquals(4, entry5.getSize());
 assertEquals("MNT/", entry6.getName());
 assertEquals(0, entry6.getSize());
 assertEquals("MNT/etc", entry7.getName());
 assertEquals(1202, entry7.getSize());
 assertEquals("MNT/etc/passwd", entry8.getName());
 assertEquals(1138, entry8.getSize());
 assertEquals("MNT/etc/group", entry9.getName());
 assertEquals(387, entry9.getSize());
 assertEquals("MNT/etc/passwd.bak", entry10.getName());
 assertEquals(1138, entry10.getSize());
 assertEquals("MNT/etc/group.bak", entry11.getName());
 assertEquals(387, entry11.getSize());
 assertEquals("MNT/etc/passwd.bak.bak", entry12.getName());
 assertEquals(1138, entry12.getSize());
 }

 @Test
 public void testGetNextEntryEmptyQueue() throws IOException, ArchiveException {
 InputStream inputStream = new ByteArrayInputStream(readFile(TEST_FILE));
 DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);
 DumpArchiveEntry entry1 = dumpArchiveInputStream.getNextEntry();
 DumpArchiveEntry entry2 = dumpArchiveInputStream.getNextEntry();
 DumpArchiveEntry entry3 = dumpArchiveInputStream.getNextEntry();
 DumpArchiveEntry entry4 = dumpArchiveInputStream.getNextEntry();
 DumpArchiveEntry entry5 = dumpArchiveInputStream.getNextEntry();
 DumpArchiveEntry entry6 = dumpArchiveInputStream.getNextEntry();
 DumpArchiveInputStream.DumpArchiveEntry entry7 = dumpArchiveInputStream.getNextEntry();
 dumpArchiveInputStream.close();
 assertNull(entry7);
 }

 @Test
 public void testGetSummary() throws IOException, ArchiveException {
 InputStream inputStream = new ByteArrayInputStream(readFile(TEST_FILE));
 DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);
 DumpArchiveSummary summary = dumpArchiveInputStream.getSummary();
 assertEquals("SunOS 5.10 Generic_121472-06 sun4u sparc", summary.getOwner());
 assertEquals("SunOS 5.10 Generic_121472-06 sun4u sun4u", summary.getGroup());
 assertEquals("unix", summary.getSystem());
 assertEquals("SunOS 5.10 Generic_121472-06", summary.getBsdName());
 assertEquals("sun4u", summary.getMachine());
 assertEquals("SunOS 5.10", summary.getVolumeName());
 assertEquals("SunOS 5.10 Generic_121472-06", summary.getVolumeLabel());
 assertEquals(18, summary.getBlockSize());
 assertEquals(18, summary.getRecordSize());
 assertEquals(18, summary.getFileOffsetTableSize());
 assertEquals(1, summary.getNumberOfFileRecords());
 assertEquals(1, summary.getNumberOfDirectories());
 assertEquals(1, summary.getNumberOfFileMasterFileTableRecords());
 assertEquals(1, summary.getNumberOfNodeFileTableRecords());
 assertEquals(1, summary.getNumberOfDumpIds());
 dumpArchiveInputStream.close();
 }

 @Test
 public void testGetBytesRead() throws IOException, ArchiveException {
 InputStream inputStream = new ByteArrayInputStream(readFile(TEST_FILE));
 DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);
 long bytesRead = 0;
 DumpArchiveEntry entry = null;
 while ((entry = dumpArchiveInputStream.getNextEntry()) != null) {
 bytesRead += entry.getSize();
 }
 dumpArchiveInputStream.close();
 assertEquals(13461, bytesRead);
 }

 @Test
 public void testGetCount() throws IOException, ArchiveException {
 InputStream inputStream = new ByteArrayInputStream(readFile(TEST_FILE));
 DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);
 int count = 0;
 DumpArchiveEntry entry = null;
 while ((entry = dumpArchiveInputStream.getNextEntry()) != null) {
 count++;
 }
 assertEquals(12, count);
 dumpArchiveInputStream.close();
 }

 @Test
 public void testRead() throws IOException, ArchiveException {
 InputStream inputStream = new ByteArrayInputStream(readFile(TEST_FILE));
 DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);
 byte[] buffer = new byte[100];
 int read = 0;
 DumpArchiveEntry entry = null;
 while ((entry = dumpArchiveInputStream.getNextEntry()) != null) {
 read += dumpArchiveInputStream.read(buffer, 0, buffer.length);
 }
 dumpArchiveInputStream.close();
 assertEquals(13461, read);
 }
}