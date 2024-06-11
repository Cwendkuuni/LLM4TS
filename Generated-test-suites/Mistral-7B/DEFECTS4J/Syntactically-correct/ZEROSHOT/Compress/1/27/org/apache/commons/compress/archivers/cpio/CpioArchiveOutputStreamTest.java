import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CpioArchiveOutputStreamTest {

 private CpioArchiveOutputStream outputStream;
 private CpioArchiveEntry entry;

 @BeforeAll
 public void setUp() throws IOException {
 String filePath = "target/test-outputs/CpioArchiveOutputStreamTest.cpio";
 outputStream = new CpioArchiveOutputStream(new FileOutputStream(filePath), CpioConstants.FORMAT_NEW);
 }

 @AfterAll
 public void tearDown() throws IOException {
 outputStream.close();
 }

 @Test
 public void testPutNextEntry() throws IOException {
 entry = new CpioArchiveEntry();
 entry.setName("testfile");
 entry.setSize(5);
 outputStream.putNextEntry(entry);
 assertArrayEquals(new byte[]{"60 6f 63 74 65 64 2d 74 68 65 72 61 73 73 2e"}, outputStream.getHeader());
 }

 @Test
 public void testPutNextEntryWithSizeAndFormat() throws IOException {
 entry = new CpioArchiveEntry();
 entry.setName("testfile");
 entry.setSize(5);
 entry.setFormat(CpioConstants.FORMAT_OLD_BINARY);
 outputStream.putNextEntry(entry);
 assertArrayEquals(new byte[]{(byte) 0x17, 0x01, 0x01, 0x06, 0x60, 0x6f, 0x63,
 (byte) 0x74, 0x65, 0x64, 0x2d, 0x74, 0x68, 0x65, 0x72, 0x61, 0x73, 0x73, 0x02, 0x00, 0x05,
 (byte) 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 (byte) 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 (byte) 0xff}, outputStream.getHeader());
 }

 @Test
 public void testWriteAndCloseEntry() throws IOException {
 byte[] testData = new byte[]{'T', 'e', 's', 't', ' ', 'F', 'i', 'l', 'e'};
 entry = new CpioArchiveEntry();
 entry.setName("testfile");
 entry.setSize(testData.length);
 outputStream.putNextEntry(entry);
 outputStream.write(testData);
 outputStream.closeArchiveEntry();
 assertFalse(outputStream.isWritable());
 }

 @Test
 public void testFinish() throws IOException {
 outputStream.finish();
 }

 @Test
 public void testWriteAndFinish() throws IOException {
 byte[] testData = new byte[]{'T', 'e', 's', 't', ' ', 'F', 'i', 'l', 'e'};
 entry = new CpioArchiveEntry();
 entry.setName("testfile");
 entry.setSize(testData.length);
 outputStream.putNextEntry(entry);
 outputStream.write(testData);
 outputStream.finish();
 }
}