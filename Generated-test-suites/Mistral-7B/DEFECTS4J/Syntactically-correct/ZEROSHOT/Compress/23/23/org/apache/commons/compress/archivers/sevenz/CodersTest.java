import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import static org.junit.Assert.*;

public class CodersTest {

 @Test
 public void testAddDecoder() throws Exception {
 Coder copyCoder = new CopyDecoder();
 InputStream inputStream = new ByteArrayInputStream(new byte[10]);
 byte[] password = new byte[0];
 InputStream result = Coders.addDecoder(inputStream, copyCoder, password);
 assertEquals(inputStream, result);

 SevenZMethod lzmaMethod = SevenZMethod.LZMA;
 InputStream lzmaInputStream = new ByteArrayInputStream(new byte[10]);
 byte[] lzmaPassword = new byte[1];
 result = Coders.addDecoder(lzmaInputStream, lzmaMethod, lzmaPassword);
 assertTrue(result instanceof LZMAInputStream);

 SevenZMethod deflateMethod = SevenZMethod.DEFLATE;
 InputStream deflateInputStream = new ByteArrayInputStream(new byte[10]);
 byte[] deflatePassword = new byte[1];
 result = Coders.addDecoder(deflateInputStream, deflateMethod, deflatePassword);
 assertTrue(result instanceof InflaterInputStream);

 SevenZMethod bzip2Method = SevenZMethod.BZIP2;
 InputStream bzip2InputStream = new ByteArrayInputStream(new byte[10]);
 result = Coders.addDecoder(bzip2InputStream, bzip2Method, null);
 assertTrue(result instanceof BZip2CompressorInputStream);

 SevenZMethod aes256sha256Method = SevenZMethod.AES256SHA256;
 InputStream aes256sha256InputStream = new ByteArrayInputStream(new byte[10]);
 byte[] aes256sha256Password = new byte[32];
 result = Coders.addDecoder(aes256sha256InputStream, aes256sha256Method, aes256sha256Password);
 assertNotNull(result);
 }

 @Test(expected = IOException.class)
 public void testUnsupportedDecoder() throws Exception {
 Coder unsupportedCoder = new Object() {};
 InputStream inputStream = new ByteArrayInputStream(new byte[10]);
 byte[] password = new byte[0];
 Coders.addDecoder(inputStream, unsupportedCoder, password);
 fail();
 }

 @Test
 public void testAddEncoder() throws Exception {
 OutputStream outputStream = new ByteArrayOutputStream();
 SevenZMethod copyMethod = SevenZMethod.COPY;
 Coder copyCoder = new CopyEncoder();
 byte[] password = new byte[0];
 Coders.addEncoder(outputStream, copyMethod, password);
 assertEquals(outputStream.size(), 0);

 SevenZMethod lzmaMethod = SevenZMethod.LZMA;
 Coder lzmaCoder = new LZMADecoder();
 byte[] lzmaPassword = new byte[1];
 Coders.addEncoder(outputStream, lzmaMethod, lzmaPassword);
 fail(); // LZMAEncoder doesn't support writing

 SevenZMethod deflateMethod = SevenZMethod.DEFLATE;
 Coder deflateCoder = new DeflateEncoder();
 byte[] deflatePassword = new byte[1];
 Coders.addEncoder(outputStream, deflateMethod, deflatePassword);
 OutputStream result = outputStream;
 byte[] expectedResult = new byte[10];
 outputStream.write(expectedResult);
 outputStream.close();
 assertEquals(expectedResult, result.toByteArray());

 SevenZMethod bzip2Method = SevenZMethod.BZIP2;
 Coder bzip2Coder = new BZIP2Encoder();
 byte[] bzip2Password = new byte[1];
 Coders.addEncoder(outputStream, bzip2Method, bzip2Password);
 outputStream = new ByteArrayOutputStream();
 outputStream.write(expectedResult);
 outputStream.close();
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 InputStream decoderResult = new BZip2CompressorInputStream(inputStream);
 byte[] decodedResult = new byte[10];
 decoderResult.read(decodedResult);
 assertEquals(expectedResult, decodedResult);

 SevenZMethod aes256sha256Method = SevenZMethod.AES256SHA256;
 Coder aes256sha256Coder = new AES256SHA256Encoder();
 byte[] aes256sha256Password = new byte[32];
 Coders.addEncoder(outputStream, aes256sha256Method, aes256sha256Password);
 outputStream = new ByteArrayOutputStream();
 outputStream.write(expectedResult);
 outputStream.close();
 InputStream encoderResult = new CipherInputStream(new ByteArrayInputStream(outputStream.toByteArray()), aes256sha256Coder);
 byte[] encryptedResult = new byte[10];
 encoderResult.read(encryptedResult);
 assertEquals(expectedResult, encryptedResult);
 }

 @Test(expected = IOException.class)
 public void testUnsupportedEncoder() throws Exception {
 SevenZMethod unsupportedMethod = new Object() {};
 OutputStream outputStream = new ByteArrayOutputStream();
 byte[] password = new byte[0];
 Coders.addEncoder(outputStream, unsupportedMethod, password);
 fail();
 }
}