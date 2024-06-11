package org.apache.commons.compress.archivers.sevenz;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.sevenz.Coders;

public class CodersTest {

 @Test
 public void testAddDecoder() throws Exception {
 Coder copyCoder = new CopyDecoder();
 InputStream expectedInputStream = new ByteArrayInputStream(new byte[0]);
 InputStream actualInputStream = Coders.addDecoder(new ByteArrayInputStream(new byte[0]), copyCoder, null);
 assertEquals(expectedInputStream, actualInputStream);

 SevenZMethod lzmaMethod = SevenZMethod.LZMA;
 Coder lzmaCoder = new LZMADecoder();
 InputStream expectedLzmaInputStream = new LZMAInputStream(new ByteArrayInputStream(new byte[0]), -1, (byte) 0, 0);
 InputStream actualLzmaInputStream = Coders.addDecoder(new ByteArrayInputStream(new byte[0]), lzmaCoder, null);
 assertEquals(expectedLzmaInputStream, actualLzmaInputStream);

 SevenZMethod deflateMethod = SevenZMethod.DEFLATE;
 Coder deflateCoder = new DeflateDecoder();
 InputStream expectedDeflateInputStream = new InflaterInputStream(new ByteArrayInputStream(new byte[0]), new Inflater(true));
 InputStream actualDeflateInputStream = Coders.addDecoder(new ByteArrayInputStream(new byte[0]), deflateCoder, null);
 assertEquals(expectedDeflateInputStream, actualDeflateInputStream);
 }

 @Test(expected = IOException.class)
 public void testUnsupportedDecoder() throws Exception {
 Coder unsupportedCoder = new Object() {
 @Override
 public InputStream decode(InputStream is, Coder coder, byte[] password) throws IOException {
 throw new UnsupportedOperationException();
 }
 };
 Coders.addDecoder(new ByteArrayInputStream(new byte[0]), unsupportedCoder, null);
 }

 @Test
 public void testAddEncoder() throws Exception {
 OutputStream outputStream = new ByteArrayOutputStream();
 SevenZMethod copyMethod = SevenZMethod.COPY;
 Coder copyCoder = new CopyEncoder();
 OutputStream expectedOutputStream = outputStream;
 OutputStream actualOutputStream = Coders.addEncoder(outputStream, copyMethod, null);
 assertEquals(expectedOutputStream, actualOutputStream);

 SevenZMethod lzmaMethod = SevenZMethod.LZMA;
 Coder lzmaCoder = new LZMAEncoder();
 OutputStream expectedLzmaOutputStream = new LZMAOutputStream(outputStream);
 OutputStream actualLzmaOutputStream = Coders.addEncoder(outputStream, lzmaMethod, null);
 assertEquals(expectedLzmaOutputStream, actualLzmaOutputStream);
 }

 @Test(expected = IOException.class)
 public void testUnsupportedEncoder() throws Exception {
 Coder unsupportedCoder = new Object() {
 @Override
 public OutputStream encode(OutputStream out, byte[] password) throws IOException {
 throw new UnsupportedOperationException();
 }
 };
 Coders.addEncoder(new ByteArrayOutputStream(), SevenZMethod.COPY, null);
 }
}