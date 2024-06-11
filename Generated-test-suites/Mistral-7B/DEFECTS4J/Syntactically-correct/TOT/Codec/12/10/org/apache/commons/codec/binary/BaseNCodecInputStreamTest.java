package org.apache.commons.codec.binary;

 import static org.apache.commons.codec.binary.BaseNCodec.EOF;
 import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertFalse;
 import static org.junit.Assert.assertThrows;
 import static org.junit.Assert.assertTrue;

 import java.io.ByteArrayInputStream;
 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.io.InputStream;

 import org.apache.commons.codec.binary.BaseNCodec;
 import org.apache.commons.codec.binary.BaseNCodecInputStream;

 /**
  * Test class for {@link BaseNCodecInputStream}.
  */
 public class BaseNCodecInputStreamTest {

     private static final byte[] ENCODED_DATA = BaseNCodec.BASE32_ALPHABET.encode(new byte[] { 1, 2, 3, 4, 5 });
     private static final byte[] DECODED_DATA = new byte[] { 1, 2, 3, 4, 5 };

     /**
      * Test for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)} constructor.
      * @throws IOException if an I/O error occurs.
      */
     @org.junit.Test
     public void testConstructor() throws IOException {
         InputStream input = new ByteArrayInputStream(ENCODED_DATA);
         BaseNCodec baseNCodec = new BaseNCodec();
         BaseNCodecInputStream testee = new BaseNCodecInputStream(input, baseNCodec, false);
         assertEquals(input, testee.in);
         assertEquals(baseNCodec, testee.baseNCodec);
         assertFalse(testee.doEncode);
     }

     /**
      * Test for {@link BaseNCodecInputStream#read()} method.
      * @throws IOException if an I/O error occurs.
      */
     @org.junit.Test
     public void testRead() throws IOException {
         InputStream input = new ByteArrayInputStream(ENCODED_DATA);
         BaseNCodec baseNCodec = new BaseNCodec();
         BaseNCodecInputStream testee = new BaseNCodecInputStream(input, baseNCodec, false);

         byte readByte;
         int i = 0;
         while ((readByte = testee.read()) != EOF) {
             assertEquals(DECODED_DATA[i++], readByte);
         }
         assertEquals(DECODED_DATA.length, i);
     }

     /**
      * Test for {@link BaseNCodecInputStream#read(byte[], int, int)} method.
      * @throws IOException if an I/O error occurs.
      */
     @org.junit.Test
     public void testReadByteArray() throws IOException {
         InputStream input = new ByteArrayInputStream(ENCODED_DATA);
         BaseNCodec baseNCodec = new BaseNCodec();
         BaseNCodecInputStream testee = new BaseNCodecInputStream(input, baseNCodec, false);

         byte[] result = new byte[DECODED_DATA.length];
         int bytesRead = testee.read(result, 0, DECODED_DATA.length);
         assertEquals(DECODED_DATA.length, bytesRead);
         for (int i = 0; i < DECODED_DATA.length; i++) {
             assertEquals(DECODED_DATA[i], result[i]);
         }
     }

     /**
      * Test for {@link BaseNCodecInputStream#markSupported()} method.
      * @throws IOException if an I/O error occurs.
      */
     @org.junit.Test
     public void testMarkSupported() throws IOException {
         InputStream input = new ByteArrayInputStream(ENCODED_DATA);
         BaseNCodec baseNCodec = new BaseNCodec();
         BaseNCodecInputStream testee = new BaseNCodecInputStream(input, baseNCodec, false);

         assertThrows(UnsupportedOperationException.class, testee::markSupported);
     }

     /**
      * Test for {@link BaseNCodecInputStream#skip(long)} method.
      * @throws IOException if an I/O error occurs.
      */
     @org.junit.Test
     public void testSkip() throws IOException {
         InputStream input = new ByteArrayInputStream(ENCODED_DATA);
         BaseNCodec baseNCodec = new BaseNCodec();
         BaseNCodecInputStream testee = new BaseNCodecInputStream(input, baseNCodec, false);

         long skipAmount = 10;
         long skippedBytes = testee.skip(skipAmount);
         assertEquals(DECODED_DATA.length - skipAmount, skippedBytes);
     }

     /**
      * Test for {@link BaseNCodecInputStream#read(byte[], int, int)} method with invalid offset and length parameters.
      * @throws IOException if an I/O error occurs.
      */
     @org.junit.Test
     public void testInvalidReadParameters() throws IOException {
         InputStream input = new ByteArrayInputStream(ENCODED_DATA);
         BaseNCodec baseNCodec = new BaseNCodec();
         BaseNCodecInputStream testee = new BaseNCodecInputStream(input, baseNCodec, false);

         byte[] buffer = new byte[DECODED_DATA.length];

         // Test negative offset
         assertThrows(IndexOutOfBoundsException.class, () -> testee.read(buffer, -1, 1));

         // Test negative length
         assertThrows(IndexOutOfBoundsException.class, () -> testee.read(buffer, 0, -1));

         // Test invalid offset and length
         assertThrows(IndexOutOfBoundsException.class, () -> testee.read(buffer, 1, DECODED_DATA.length - 2));
     }

     /**
      * Test for {@link BaseNCodecInputStream#read(byte[], int, int)} method with invalid input stream.
      * @throws IOException if an I/O error occurs.
      */
     @org.junit.Test
     public void testInvalidInputStream() throws IOException {
         byte[] invalidData = new byte[] { (byte) 0xff, (byte) 0x00 };
         InputStream input = new ByteArrayInputStream(invalidData);
         BaseNCodec baseNCodec = new BaseNCodec();
         BaseNCodecInputStream testee = new BaseNCodecInputStream(input, baseNCodec, false);

         byte[] result = new byte[DECODED_DATA.length];
         int bytesRead = testee.read(result, 0, DECODED_DATA.length);

         assertEquals(-1, bytesRead);
         assertFalse(testee.hasNextByte());
     }

     /**
      * Test for {@link BaseNCodecInputStream#read(byte[], int, int)} method with invalid decoder.
      * @throws IOException if an I/O error occurs.
      */
     @org.junit.Test
     public void testInvalidDecoder() throws IOException {
         InputStream input = new ByteArrayInputStream(ENCODED_DATA);
         BaseNCodec baseNCodec = new BaseNCodec() {
             @Override
             public int decode(byte[] input, int offset, int len) {
                 throw new UnsupportedOperationException();
             }
         };
         BaseNCodecInputStream testee = new BaseNCodecInputStream(input, baseNCodec, false);

         byte[] result = new byte[DECODED_DATA.length];
         int bytesRead = testee.read(result, 0, DECODED_DATA.length);

         assertEquals(-1, bytesRead);
         assertFalse(testee.hasNextByte());
     }
 }