import org.apache.commons.compress.archivers.sevenz.Coder;
import org.apache.commons.compress.archivers.sevenz.CoderId;
import org.apache.commons.compress.archivers.sevenz.CoderTable;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import static org.junit.Assert.*;

public class CodersTest {

 @Test
 public void testAddDecoder() throws Exception {
 Coder coders = new Coders();
 SevenZMethod method = SevenZMethod.DEFLATE;
 byte[] password = new byte[0];
 InputStream expectedInput = new ByteArrayInputStream(new byte[]{"Expected byte array for DEFLATE decoder input".getBytes()});
 CoderId deflateCoderId = new Coders.CoderId(method, new Coders.DeflateDecoder());
 InputStream actualInput = coders.addDecoder(expectedInput, deflateCoderId, password);
 byte[] actualByteArray = new byte[1024];
 int actualBytesRead = actualInput.read(actualByteArray);
 assertEquals("Expected byte array for DEFLATE decoder output", expectedInput.readAllBytes(), actualByteArray, 0, actualBytesRead);
 }

 @Test(expected = IOException.class)
 public void testUnsupportedDecoderMethod() throws Exception {
 Coder coders = new Coders();
 SevenZMethod unsupportedMethod = new SevenZMethod(-1); // Unsupported method ID
 byte[] password = new byte[0];
 try {
 coders.addDecoder(new ByteArrayInputStream(new byte[0]), new Coders.CoderId(unsupportedMethod, new Coders.CopyDecoder()), password);
 } catch (IllegalArgumentException iae) {
 fail("Unexpected exception. Expected IOException for unsupported decoder.");
 }
 }

 @Test
 public void testAddEncoder() throws Exception {
 Coder coders = new Coders();
 SevenZMethod method = SevenZMethod.DEFLATE;
 byte[] password = new byte[0];
 OutputStream expectedOutput = new ByteArrayOutputStream();
 CoderId deflateCoderId = new Coders.CoderId(method, new Coders.DeflateDecoder());
 OutputStream actualOutput = coders.addEncoder(expectedOutput, method, password);
 String expectedOutputString = "Expected output string for DEFLATE encoder";
 byte[] expectedOutputBytes = expectedOutputString.getBytes();
 expectedOutput.close();
 byte[] actualByteArray = new byte[expectedOutput.size()];
 int actualBytesRead = actualOutput.read(actualByteArray);
 assertArrayEquals("Expected output byte array for DEFLATE encoder", expectedOutputBytes, actualByteArray);
 }

 @Test(expected = IOException.class)
 public void testUnsupportedEncoderMethod() throws Exception {
 Coder coders = new Coders();
 SevenZMethod unsupportedMethod = new SevenZMethod(-1); // Unsupported method
 byte[] password = new byte[0];
 try {
 coders.addEncoder(new ByteArrayOutputStream(), unsupportedMethod, password);
 } catch (IllegalArgumentException iae) {
 fail("Unexpected exception. Expected IOException for unsupported encoder.");
 }
 }

 @Test
 public void testCoderIds() throws Exception {
 Coder coders = new Coders();
 CoderId[] coderIds = coders.coderTable;
 SevenZMethod[] expectedMethods = {SevenZMethod.COPY, SevenZMethod.LZMA, SevenZMethod.LZMA2, SevenZMethod.DEFLATE, SevenZMethod.BZIP2, SevenZMethod.AES256SHA256};
 for (int i = 0; i < coderIds.length; i++) {
 assertEquals("Expected method " + expectedMethods[i] + " for coder ID " + i, expectedMethods[i], coderIds[i].method);
 }
 }

}