import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class ArArchiveInputStreamTest {

 private ArArchiveInputStream inputStream;
 private byte[] testData;

 @BeforeEach
 public void setUp() throws IOException {
 testData = createTestData();
 inputStream = new ArArchiveInputStream(new ByteArrayInputStream(testData));
 }

 @AfterEach
 public void tearDown() throws IOException {
 if (inputStream != null) {
 inputStream.close();
 }
 }

 @Test
 public void testGetNextArEntry() throws IOException {
 ArArchiveEntry entry = inputStream.getNextArEntry();
 assertNotNull(entry);
 assertEquals("test", entry.getName());
 assertEquals(12L, entry.getSize());
 entry = inputStream.getNextArEntry();
 assertNull(entry);
 }

 @Test
 public void testGetNextEntry() throws IOException {
 ArchiveEntry entry = inputStream.getNextEntry();
 assertEquals(ArArchiveEntry.class, entry.getClass());
 entry = inputStream.getNextEntry();
 assertNull(entry);
 }

 @Test
 public void testClose() throws IOException {
 inputStream.close();
 assertThrows(IOException.class, () -> inputStream.getNextArEntry());
 }

 @Test
 public void testRead() throws IOException {
 byte[] readData = new byte[10];
 int result = inputStream.read(readData);
 assertEquals(10, result);
 }

 @Test
 public void testReadByte() throws IOException {
 int result = inputStream.read();
 assertNotEquals(-1, result);
 }

 @Test
 public void testReadByteArray() throws IOException {
 byte[] readData = new byte[10];
 inputStream.read(readData);
 assertArrayEquals(new byte[] { 0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x3e, 0x0a, 0x00 }, readData);
 }

 @Test
 public void testReadByteArrayWithOffset() throws IOException {
 byte[] readData = new byte[10];
 int offset = 0;
 int length = 8;
 inputStream.read(readData, offset, length);
 assertArrayEquals(new byte[] { 0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0a }, readData, offset, length);
 }

 @Test
 public void testMatches() {
 byte[] signature = new byte[] { 0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x3e, 0x0a, 0x00 };
 assertTrue(ArArchiveInputStream.matches(signature, signature.length));
 }

 private byte[] createTestData() throws IOException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 OutputStream archiveStream = new ArArchiveOutputStream(output);
 ArArchiveEntry entry = new ArArchiveEntry("test", 12);
 archiveStream.putNextEntry(entry);
 archiveStream.write(new byte[] { 0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x3e, 0x0a, 0x00 });
 archiveStream.write("test".getBytes());
 archiveStream.closeEntry();
 archiveStream.finish();
 return output.toByteArray();
 }
}