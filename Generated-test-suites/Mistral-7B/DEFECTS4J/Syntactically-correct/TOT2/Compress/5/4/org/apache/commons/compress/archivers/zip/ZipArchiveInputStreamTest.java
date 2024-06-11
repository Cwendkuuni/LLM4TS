import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.junit.Test;
import org.junit.Assert.*;

public class ZipArchiveInputStreamTest {

 private static final String TEST_FILE = "test.zip";

 @Test
 public void testConstructor() {
 ZipArchiveInputStream inputStream = null;
 try {
 inputStream = new ZipArchiveInputStream(getClass().getResourceAsStream(TEST_FILE));
 Assert.assertNotNull(inputStream);
 } catch (IOException e) {
 e.printStackTrace();
 } finally {
 try {
 if (inputStream != null) {
 inputStream.close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testGetNextZipEntry() throws IOException {
 ZipArchiveInputStream inputStream = null;
 try {
 inputStream = new ZipArchiveInputStream(getClass().getResourceAsStream(TEST_FILE));
 ZipArchiveEntry entry = inputStream.getNextZipEntry();
 Assert.assertNotNull(entry);
 } finally {
 try {
 if (inputStream != null) {
 inputStream.close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testGetNextEntry() throws IOException {
 ZipArchiveInputStream inputStream = null;
 try {
 inputStream = new ZipArchiveInputStream(getClass().getResourceAsStream(TEST_FILE));
 ZipArchiveEntry entry = (ZipArchiveEntry) inputStream.getNextEntry();
 Assert.assertNotNull(entry);
 } finally {
 try {
 if (inputStream != null) {
 inputStream.close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testRead_NormalCase() throws IOException {
 ZipArchiveInputStream inputStream = null;
 byte[] buffer = new byte[1024];
 try {
 inputStream = new ZipArchiveInputStream(getClass().getResourceAsStream(TEST_FILE));
 int bytesRead = inputStream.read(buffer);
 Assert.assertGreaterThan(bytesRead, 0);
 } finally {
 try {
 if (inputStream != null) {
 inputStream.close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testRead_EOC_Case() throws IOException {
 ZipArchiveInputStream inputStream = null;
 byte[] buffer = new byte[1024];
 try {
 inputStream = new ZipArchiveInputStream(getClass().getResourceAsStream(TEST_FILE));
 int bytesRead = inputStream.read(buffer);
 Assert.assertEquals(-1, bytesRead);
 } finally {
 try {
 if (inputStream != null) {
 inputStream.close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testRead_ClosedStream_Case() throws IOException {
 ZipArchiveInputStream inputStream = null;
 byte[] buffer = new byte[1024];
 try {
 inputStream = new ZipArchiveInputStream(getClass().getResourceAsStream(TEST_FILE));
 inputStream.close();
 int bytesRead = inputStream.read(buffer);
 Assert.assertEquals(-1, bytesRead);
 } finally {
 // No need to close the stream as it's already closed
 }
 }

 @Test
 public void testRead_ZeroLengthEntry_Case() throws IOException {
 ZipArchiveInputStream inputStream = null;
 byte[] buffer = new byte[1024];
 try {
 inputStream = new ZipArchiveInputStream(getClass().getResourceAsStream(TEST_FILE));
 ZipArchiveEntry entry = inputStream.getNextEntry();
 Assert.assertNotNull(entry);
 int bytesRead = inputStream.read(buffer);
 Assert.assertEquals(-1, bytesRead);
 } finally {
 try {
 if (inputStream != null) {
 inputStream.close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testRead_DataDescriptor_Case() throws IOException {
 ZipArchiveInputStream inputStream = null;
 byte[] buffer = new byte[1024];
 try {
 inputStream = new ZipArchiveInputStream(getClass().getResourceAsStream(TEST_FILE));
 ZipArchiveEntry entry = inputStream.getNextEntry();
 Assert.assertNotNull(entry);
 if (entry.getMethod() == ZipArchiveOutputStream.DEFLATED) {
 int bytesRead = inputStream.read(buffer, 0, 1);
 Assert.assertEquals(-1, bytesRead);
 }
 } finally {
 try {
 if (inputStream != null) {
 inputStream.close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testRead_InvalidSignature_Case() throws IOException {
 ZipArchiveInputStream inputStream = null;
 try {
 inputStream = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[]{-10, 1, 2, 3, 4}));
 inputStream.read();
 Assert.fail("Should throw an exception");
 } finally {
 try {
 if (inputStream != null) {
 inputStream.close();
 }
 } catch (IOException e) {
 // Expected
 }
 }
 }

 @Test
 public void testCloseEntry() throws IOException {
 ZipArchiveInputStream inputStream = null;
 try {
 inputStream = new ZipArchiveInputStream(getClass().getResourceAsStream(TEST_FILE));
 ZipArchiveEntry entry = inputStream.getNextEntry();
 Assert.assertNotNull(entry);
 inputStream.closeEntry();
 } finally {
 try {
 if (inputStream != null) {
 inputStream.close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testSkip() throws IOException {
 ZipArchiveInputStream inputStream = null;
 try {
 inputStream = new ZipArchiveInputStream(getClass().getResourceAsStream(TEST_FILE));
 inputStream.skip(Long.MAX_VALUE);
 } finally {
 try {
 if (inputStream != null) {
 inputStream.close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testClose_AlreadyClosed_Case() throws IOException {
 ZipArchiveInputStream inputStream = null;
 try {
 inputStream = new ZipArchiveInputStream(getClass().getResourceAsStream(TEST_FILE));
 inputStream.close();
 inputStream.close();
 } finally {
 try {
 if (inputStream != null) {
 inputStream.close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testReadFully() throws IOException {
 ZipArchiveInputStream inputStream = null;
 byte[] buffer = new byte[1024];
 try {
 inputStream = new ZipArchiveInputStream(getClass().getResourceAsStream(TEST_FILE));
 inputStream.readFully(buffer);
 } finally {
 try {
 if (inputStream != null) {
 inputStream.close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }
}