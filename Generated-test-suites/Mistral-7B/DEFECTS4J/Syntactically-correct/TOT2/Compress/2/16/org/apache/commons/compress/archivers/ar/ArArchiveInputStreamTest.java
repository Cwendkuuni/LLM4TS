import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.test.TestArArchiveInputStream;
import org.apache.commons.compress.archivers.test.TestArFile;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArArchiveInputStreamTest {

 @Test
 public void testGetNextEntry() throws Exception {
 ArArchiveInputStream input = new ArArchiveInputStream(new TestArFile("test.tar.gz").getInputStream());
 ArchiveEntry entry = null;
 while ((entry = input.getNextEntry()) != null) {
 System.out.println("Entry Name: " + entry.getName());
 assertEquals(ArchiveEntry.class, entry.getClass());
 }
 input.close();
 }

 @Test(expected = IOException.class)
 public void testGetNextEntry_IOException() throws Exception {
 ArArchiveInputStream input = new ArArchiveInputStream(new TestArFile("test.tar.gz_empty").getInputStream());
 input.getNextEntry(); // normal behavior
 input.getNextEntry(); // expected IOException
 input.close();
 }

 @Test(expected = IOException.class)
 public void testGetNextEntry_InvalidSignature() throws Exception {
 ArArchiveInputStream input = new ArArchiveInputStream(new TestArFile("test.tar.gz_invalid_signature").getInputStream());
 input.getNextEntry(); // normal behavior
 input.close();
 }

 @Test
 public void testGetNextEntry_NullName() throws Exception {
 ArArchiveInputStream input = new ArArchiveInputStream(new TestArFile("test.tar.gz_null_name").getInputStream());
 ArchiveEntry entry = input.getNextEntry();
 assertNull(entry);
 input.close();
 }

 @Test
 public void testGetNextEntry_EmptyName() throws Exception {
 ArArchiveInputStream input = new ArArchiveInputStream(new TestArFile("test.tar.gz_empty_name").getInputStream());
 ArchiveEntry entry = input.getNextEntry();
 assertEquals("", entry.getName());
 input.close();
 }

 @Test
 public void testGetNextEntry_ZeroLength() throws Exception {
 ArArchiveInputStream input = new ArArchiveInputStream(new TestArFile("test.tar.gz_zero_length").getInputStream());
 ArchiveEntry entry = input.getNextEntry();
 assertEquals(0, entry.getSize());
 input.close();
 }

 @Test
 public void testGetNextEntry_NegativeLength() throws Exception {
 ArArchiveInputStream input = new ArArchiveInputStream(new TestArFile("test.tar.gz_negative_length").getInputStream());
 ArchiveEntry entry = input.getNextEntry();
 assertNotNull(entry);
 input.close();
 }

 @Test
 public void testGetNextEntry_NonArEntry() throws Exception {
 ArArchiveInputStream input = new ArArchiveInputStream(new TestArFile("test.tar.gz_non_ar_entry").getInputStream());
 ArchiveEntry entry = input.getNextEntry();
 assertFalse(entry instanceof ArArchiveEntry);
 input.close();
 }

 @Test
 public void testClose() throws Exception {
 ArArchiveInputStream input = new ArArchiveInputStream(new TestArFile("test.tar.gz").getInputStream());
 assertFalse(input.isClosed());
 input.close();
 assertTrue(input.isClosed());
 }

 @Test(expected = IOException.class)
 public void testClose_AlreadyClosed() throws Exception {
 ArArchiveInputStream input = new ArArchiveInputStream(new TestArFile("test.tar.gz").getInputStream());
 input.close();
 input.close();
 }

 @Test
 public void testRead() throws Exception {
 ArArchiveInputStream input = new ArArchiveInputStream(new TestArFile("test.tar.gz").getInputStream());
 byte[] buffer = new byte[10];
 int length = input.read(buffer, 0, buffer.length);
 assertNotNull(length);
 assertEquals(10, length);
 input.close();
 }

 @Test
 public void testRead_EOF() throws Exception {
 ArArchiveInputStream input = new ArArchiveInputStream(new TestArFile("test.tar.gz").getInputStream());
 byte[] buffer = new byte[10];
 int length = input.read(buffer, 0, buffer.length);
 assertEquals(-1, length);
 input.close();
 }

 @Test
 public void testRead_NullBuffer() throws Exception {
 ArArchiveInputStream input = new ArArchiveInputStream(new TestArFile("test.tar.gz").getInputStream());
 int length = input.read(null, 0, 10);
 assertNull(length);
 input.close();
 }

 @Test
 public void testRead_InvalidOffset() throws Exception {
 ArArchiveInputStream input = new ArArchiveInputStream(new TestArFile("test.tar.gz").getInputStream());
 byte[] buffer = new byte[10];
 int length = input.read(buffer, -1, 10);
 assertEquals(-1, length);
 input.close();
 }

 @Test
 public void testRead_InvalidLength() throws Exception {
 ArArchiveInputStream input = new ArArchiveInputStream(new TestArFile("test.tar.gz").getInputStream());
 byte[] buffer = new byte[10];
 int length = input.read(buffer, 0, -1);
 assertEquals(-1, length);
 input.close();
 }

 @Test
 public void testRead_PartialRead() throws Exception {
 ArArchiveInputStream input = new ArArchiveInputStream(new TestArFile("test.tar.gz").getInputStream());
 byte[] buffer = new byte[10];
 int length = input.read(buffer, 0, 5);
 assertNotNull(length);
 assertEquals(5, length);
 input.close();
 }

 @Test
 public void testRead_NoBytesToRead() throws Exception {
 ArArchiveInputStream input = new ArArchiveInputStream(new TestArFile("test.tar.gz").getInputStream());
 byte[] buffer = new byte[10];
 int length = input.read(buffer, 0, 11);
 assertEquals(-1, length);
 input.close();
 }

 @Test
 public void testMatches() throws Exception {
 byte[] signature = new byte[16];
 ArArchiveInputStream input = new ArArchiveInputStream(new TestArFile("test.tar.gz").getInputStream());
 input.read(signature, 0, 16);
 assertTrue(ArArchiveInputStream.matches(signature, signature.length));
 input.close();
 }
}