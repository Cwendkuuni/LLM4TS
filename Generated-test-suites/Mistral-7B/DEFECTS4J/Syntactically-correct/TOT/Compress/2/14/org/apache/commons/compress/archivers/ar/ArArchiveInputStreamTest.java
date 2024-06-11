import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArArchiveInputStreamTest {

 private InputStream inputStream;
 private ArArchiveInputStream archiveInputStream;

 @Before
 public void setUp() throws Exception {
 inputStream = new ByteArrayInputStream(
 "3c21 7261 6863 0a3e61 7261 2d75 7361 6e63 6f64 6564 2d73 6572 7669 6365 2d73 7472 696e 670a"
 + "0000000000000002 00000010 00000016 name.txt 00000001 00000001 00000006 owner 00000006 group 644\n"
 + "3c21 7261 6863 0a3e61 7261 2d75 7361 6e63 6f64 6564 2d73 6572 7669 6365 2d73 7472 696e 670a"
 + "0000000000000002 00000012 00000016 name2.txt 00000001 00000001 00000006 owner 00000006 group 644\n"
 + "3c21 7261 6863 0a"
 // EOF marker
 ).wrap(ArArchiveInputStream.class);
 archiveInputStream = new ArArchiveInputStream(inputStream);
 }

 @Test
 public void testGetNextEntry() throws IOException {
 ArArchiveEntry entry1 = archiveInputStream.getNextArEntry();
 assertNotNull(entry1);
 assertEquals("name.txt", entry1.getName());
 ArArchiveEntry entry2 = archiveInputStream.getNextArEntry();
 assertNotNull(entry2);
 assertEquals("name2.txt", entry2.getName());
 ArArchiveEntry entry3 = archiveInputStream.getNextArEntry();
 assertNull(entry3);
 }

 @Test(expected = IOException.class)
 public void testGetNextEntryInvalidHeader() throws IOException {
 // simulate a corrupted header
 inputStream = new ByteArrayInputStream(
 "3c21 7261 6863 0a3e61 7261 2d75 7361 6e63 6f64 6564 2d73 6572 7669 6365 2d73 7472 696e 670a"
 + "0000000000000002 00000010 00000016 name.txt 00000001 00000001 00000006 owner 00000006 group 644\n"
 + "3c21 7261 6863 0a3e61 7261 2d75 7361 6e63 6f64 6564 2d73 6572 7669 6365 2d73 7472 696e 670a"
 + "0000000000000002 00000012 00000016 name2.txt 00000001 00000001 00000006 owner 00000006 group 644\n"
 + "3c21 7261 6863 0a" // missing EOF marker
 );
 ArArchiveInputStream wrongArchiveInputStream = new ArArchiveInputStream(inputStream);
 wrongArchiveInputStream.getNextArEntry(); // this should fail
 }

 @Test(expected = IOException.class)
 public void testGetNextEntryUnexpectedEOF() throws IOException {
 // simulate a premature EOF
 inputStream = new ByteArrayInputStream(
 "3c21 7261 6863 0a3e61 7261 2d75 7361 6e63 6f64 6564 2d73 6572 7669 6365 2d73 7472 696e 670a"
 + "0000000000000002 00000010 00000016 name.txt 00000001 00000001 00000006 owner 00000006 group 644\n"
 );
 ArArchiveInputStream unexpectedEofArchiveInputStream = new ArArchiveInputStream(inputStream);
 unexpectedEofArchiveInputStream.getNextArEntry(); // this should fail
 unexpectedEofArchiveInputStream.getNextArEntry(); // this should fail
 }
}