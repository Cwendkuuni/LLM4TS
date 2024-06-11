import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

public class CodersTest {

 @Test
 public void testAddDecoder_validInputStream_validCoderId_returnsInputStream() throws Exception {
 InputStream inputStream = mock(InputStream.class);
 Coder coder = mock(Coder.class);
 byte[] password = new byte[16];
 CoderId coderId = new CoderId(SevenZMethod.COPY, new CopyDecoder());

 InputStream result = Coders.addDecoder(inputStream, coder, password);

 assertNotNull(result);
 assertInstanceOf(InputStream.class, result);
 }

 @Test(expected = IOException.class)
 public void testAddDecoder_invalidInputStream_throwsIOException() throws Exception {
 InputStream inputStream = null;
 Coder coder = mock(Coder.class);
 byte[] password = new byte[16];

 InputStream result = Coders.addDecoder(inputStream, coder, password);

 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testAddDecoder_unsupportedCoderId_throwsIOException() throws Exception {
 InputStream inputStream = mock(InputStream.class);
 Coder coder = mock(Coder.class);
 byte[] password = new byte[16];
 CoderId unsupportedCoderId = new CoderId(new SevenZMethod("UnsupportedMethod"), new CopyDecoder());

 InputStream result = Coders.addDecoder(inputStream, coder, password);

 assertNull(result);
 }

 @Test
 public void testAddEncoder_validOutputStream_validSevenZMethod_returnsOutputStream() throws Exception {
 OutputStream outputStream = mock(OutputStream.class);
 SevenZMethod sevenZMethod = SevenZMethod.DEFLATE;
 byte[] password = new byte[16];
 CoderId coderId = new CoderId(sevenZMethod, new DeflateDecoder());

 OutputStream result = Coders.addEncoder(outputStream, sevenZMethod, password);

 assertNotNull(result);
 assertInstanceOf(OutputStream.class, result);
 }

 @Test(expected = IOException.class)
 public void testAddEncoder_invalidOutputStream_throwsIOException() throws Exception {
 OutputStream outputStream = null;
 SevenZMethod sevenZMethod = SevenZMethod.DEFLATE;
 byte[] password = new byte[16];
 CoderId coderId = new CoderId(sevenZMethod, new DeflateDecoder());

 OutputStream result = Coders.addEncoder(outputStream, sevenZMethod, password);

 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testAddEncoder_unsupportedSevenZMethod_throwsIOException() throws Exception {
 OutputStream outputStream = mock(OutputStream.class);
 SevenZMethod unsupportedSevenZMethod = new SevenZMethod("UnsupportedMethod");
 byte[] password = new byte[16];
 CoderId coderId = new CoderId(unsupportedSevenZMethod, new DeflateDecoder());

 OutputStream result = Coders.addEncoder(outputStream, unsupportedSevenZMethod, password);

 assertNull(result);
 }

 @Test
 public void testCoderIdConstruction() {
 SevenZMethod sevenZMethod = SevenZMethod.COPY;
 CoderBase copyDecoder = new CopyDecoder();
 CoderId copyCoderId = new CoderId(sevenZMethod, copyDecoder);

 SevenZMethod copyMethod = copyCoderId.method;
 CoderBase copyDecoderInstance = copyCoderId.coder;

 assertEquals(sevenZMethod, copyMethod);
 assertEquals(copyDecoder, copyDecoderInstance);
 }
}