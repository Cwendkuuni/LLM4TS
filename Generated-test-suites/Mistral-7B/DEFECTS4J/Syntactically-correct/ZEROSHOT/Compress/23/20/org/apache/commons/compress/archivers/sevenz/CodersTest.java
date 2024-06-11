import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import static org.junit.Assert.*;

public class CodersTest {

 @Test
 public void testAddDecoder() throws Exception {
 Coder copyCoder = new CopyDecoder();
 InputStream expectedCopyInputStream = new ByteArrayInputStream(new byte[] {0x01, 0x02, 0x03});
 InputStream actualCopyInputStream = Coders.addDecoder(new ByteArrayInputStream(new byte[] {0x01, 0x02, 0x03}), copyCoder, null);
 assertEquals("Unexpected input stream for COPY decoder", expectedCopyInputStream, actualCopyInputStream);

 SevenZMethod lzmaMethod = SevenZMethod.LZMA;
 LZMADecoder lzmaDecoder = new LZMADecoder();
 InputStream expectedLzmaInputStream = new ByteArrayInputStream(new byte[] {0x04, 0x05, 0x06});
 InputStream actualLzmaInputStream = Coders.addDecoder(new ByteArrayInputStream(new byte[] {0x04, 0x05, 0x06}), lzmaDecoder, null);
 assertEquals("Unexpected input stream for LZMA decoder", expectedLzmaInputStream, actualLzmaInputStream);

 SevenZMethod deflateMethod = SevenZMethod.DEFLATE;
 DeflateDecoder deflateDecoder = new DeflateDecoder();
 InputStream expectedDeflateInputStream = new InflaterInputStream(new ByteArrayInputStream(new byte[] {0x07, 0x08, 0x09}), new Inflater(true));
 InputStream actualDeflateInputStream = Coders.addDecoder(new ByteArrayInputStream(new byte[] {0x07, 0x08, 0x09}), deflateDecoder, null);
 assertEquals("Unexpected input stream for DEFLATE decoder", expectedDeflateInputStream, actualDeflateInputStream);

 SevenZMethod bzip2Method = SevenZMethod.BZIP2;
 BZIP2Decoder bzip2Decoder = new BZIP2Decoder();
 InputStream expectedBzip2InputStream = new BZip2CompressorInputStream(new ByteArrayInputStream(new byte[] {0x0a, 0x0b, 0x0c}));
 InputStream actualBzip2InputStream = Coders.addDecoder(new ByteArrayInputStream(new byte[] {0x0a, 0x0b, 0x0c}), bzip2Decoder, null);
 assertEquals("Unexpected input stream for BZIP2 decoder", expectedBzip2InputStream, actualBzip2InputStream);

 SevenZMethod aes256Sha256Method = SevenZMethod.AES256SHA256;
 AES256SHA256Decoder aes256Sha256Decoder = new AES256SHA256Decoder();
 byte[] password = new byte[] {0x01, 0x02, 0x03};
 InputStream expectedAes256Sha256InputStream = new CipherInputStream(new ByteArrayInputStream(new byte[] {0x0d, 0x0e, 0x0f}), aes256Sha256Decoder, password);
 InputStream actualAes256Sha256InputStream = Coders.addDecoder(new ByteArrayInputStream(new byte[] {0x0d, 0x0e, 0x0f}), aes256Sha256Decoder, password);
 assertEquals("Unexpected input stream for AES256SHA256 decoder", expectedAes256Sha256InputStream, actualAes256Sha256InputStream);

 Coder unknownCoder = new Object() {};
 InputStream expectedUnknownInputStream = new ByteArrayInputStream(new byte[] {});
 InputStream actualUnknownInputStream = Coders.addDecoder(new ByteArrayInputStream(new byte[] {}), unknownCoder, null);
 assertTrue("Unexpected input stream for unknown decoder", actualUnknownInputStream.available() == 0);
 }

 @Test
 public void testAddEncoder() throws Exception {
 SevenZMethod copyMethod = SevenZMethod.COPY;
 CopyDecoder copyEncoder = new CopyEncoder();
 OutputStream expectedCopyOutputStream = new ByteArrayOutputStream();
 OutputStream actualCopyOutputStream = Coders.addEncoder(expectedCopyOutputStream, copyMethod, null);
 assertEquals("Unexpected output stream for COPY encoder", expectedCopyOutputStream, actualCopyOutputStream);

 SevenZMethod lzmaMethod = SevenZMethod.LZMA;
 LZMADecoder lzmaEncoder = new LZMADecoder();
 OutputStream expectedLzmaOutputStream = new ByteArrayOutputStream();
 OutputStream actualLzmaOutputStream = Coders.addEncoder(expectedLzmaOutputStream, lzmaMethod, null);
 assertEquals("Unexpected output stream for LZMA encoder", expectedLzmaOutputStream, actualLzmaOutputStream);

 SevenZMethod deflateMethod = SevenZMethod.DEFLATE;
 DeflateDecoder deflateEncoder = new DeflateEncoder();
 OutputStream expectedDeflateOutputStream = new ByteArrayOutputStream();
 OutputStream actualDeflateOutputStream = Coders.addEncoder(expectedDeflateOutputStream, deflateMethod, null);
 assertEquals("Unexpected output stream for DEFLATE encoder", new DeflaterOutputStream(expectedDeflateOutputStream, new Deflater(9, true)), actualDeflateOutputStream);

 SevenZMethod bzip2Method = SevenZMethod.BZIP2;
 BZIP2Decoder bzip2Encoder = new BZIP2Decoder();
 OutputStream expectedBzip2OutputStream = new ByteArrayOutputStream();
 OutputStream actualBzip2OutputStream = Coders.addEncoder(expectedBzip2OutputStream, bzip2Method, null);
 assertEquals("Unexpected output stream for BZIP2 encoder", new BZip2CompressorOutputStream(expectedBzip2OutputStream), actualBzip2OutputStream);

 SevenZMethod aes256Sha256Method = SevenZMethod.AES256SHA256;
 AES256SHA256Decoder aes256Sha256Encoder = new AES256SHA256Decoder();
 byte[] password = new byte[] {0x01, 0x02, 0x03};
 OutputStream expectedAes256Sha256OutputStream = new ByteArrayOutputStream();
 OutputStream actualAes256Sha256OutputStream = Coders.addEncoder(expectedAes256Sha256OutputStream, aes256Sha256Method, password);
 assertEquals("Unexpected output stream for AES256SHA256 encoder", new CipherOutputStream(expectedAes256Sha256OutputStream, Cipher.getInstance("AES/CBC/NoPadding")), actualAes256Sha256OutputStream);
 }
}