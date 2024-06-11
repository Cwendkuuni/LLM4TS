// BaseNCodecInputStreamTest.java

package org.junit.tests;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {

 @Test
 public void testConstructor() {
 InputStream input = new ByteArrayInputStream("BaseNTestData".getBytes());
 BaseNCodec baseNCodec = new BaseNCodec();
 BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, baseNCodec, false);
 assertNotNull(baseNCodecInputStream);
 }

 @Test
 public void testRead() throws IOException {
 InputStream input = new ByteArrayInputStream("BaseNTestData".getBytes());
 BaseNCodec baseNCodec = new BaseNCodec();
 BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, baseNCodec, false);
 byte[] buffer = new byte[10];
 int read = baseNCodecInputStream.read(buffer, 0, buffer.length);
 assertEquals(read, buffer.length);
 }

 @Test
 public void testReadEndOfFile() throws IOException {
 InputStream input = new ByteArrayInputStream("BaseNTestData".getBytes());
 BaseNCodec baseNCodec = new BaseNCodec();
 BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, baseNCodec, false);
 byte[] buffer = new byte[1];
 int read = baseNCodecInputStream.read(buffer, 0, buffer.length);
 assertEquals(-1, read);
 }

 @Test
 public void testMarkSupported() {
 InputStream input = new ByteArrayInputStream("BaseNTestData".getBytes());
 BaseNCodec baseNCodec = new BaseNCodec();
 BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, baseNCodec, false);
 assertFalse(baseNCodecInputStream.markSupported());
 }

 @Test
 public void testSkip() throws IOException {
 InputStream input = new ByteArrayInputStream("BaseNTestData".getBytes());
 BaseNCodec baseNCodec = new BaseNCodec();
 BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, baseNCodec, false);
 long expected = 1024;
 long actual = baseNCodecInputStream.skip(expected);
 assertEquals(expected, actual);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadWithInvalidOffset() {
 InputStream input = new ByteArrayInputStream("BaseNTestData".getBytes());
 BaseNCodec baseNCodec = new BaseNCodec();
 BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, baseNCodec, false);
 byte[] buffer = new byte[1];
 int read = baseNCodecInputStream.read(buffer, 10, buffer.length);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadWithInvalidLength() {
 InputStream input = new ByteArrayInputStream("BaseNTestData".getBytes());
 BaseNCodec baseNCodec = new BaseNCodec();
 BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, baseNCodec, false);
 byte[] buffer = new byte[1];
 int read = baseNCodecInputStream.read(buffer, 0, -1);
 }

 @Test(expected = NullPointerException.class)
 public void testReadWithNullByteArray() {
 InputStream input = new ByteArrayInputStream("BaseNTestData".getBytes());
 BaseNCodec baseNCodec = new BaseNCodec();
 BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, baseNCodec, false);
 int read = baseNCodecInputStream.read(null, 0, 1);
 }

 @Test
 public void testReadString() throws IOException {
 InputStream input = new ByteArrayInputStream("BaseNTestData".getBytes());
 BaseNCodec baseNCodec = new BaseNCodec();
 BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, baseNCodec, false);
 String expected = "BaseNTestData";
 String actual = new String(baseNCodecInputStream.readAllBytes());
 assertEquals(expected, actual);
 }
}