package org.apache.commons.compress.compressors.test;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.compressors.LZMACompressorInputStream;
import org.apache.commons.compress.compressors.DecompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.junit.Assert.*;

public class CompressorStreamFactoryTest {

 private static final int SIZE = 1024;

 @Test
 public void testCreateBZip2CompressorInputStream() throws IOException {
 CompressorFactory factory = new CompressorFactory();
 CompressorInputStream inputStream = factory.createCompressorInputStream("bzip2", new ByteArrayInputStream(new byte[SIZE]));
 assertTrue(inputStream instanceof BZip2CompressorInputStream);
 }

 @Test(expected = CompressorException.class)
 public void testCreateUnknownCompressorInputStream() throws IOException {
 CompressorFactory factory = new CompressorFactory();
 factory.createCompressorInputStream("unknown", new ByteArrayInputStream(new byte[SIZE]));
 fail("Should throw CompressorException");
 }

 @Test
 public void testCreateDeflateCompressorInputStream() throws IOException {
 CompressorFactory factory = new CompressorFactory();
 CompressorInputStream inputStream = factory.createCompressorInputStream("deflate", new ByteArrayInputStream(new byte[SIZE]));
 assertTrue(inputStream instanceof DeflateCompressorInputStream);
 }

 @Test
 public void testCreateGzipCompressorInputStream() throws IOException {
 CompressorFactory factory = new CompressorFactory();
 CompressorInputStream inputStream = factory.createCompressorInputStream("gzip", new ByteArrayInputStream(new byte[SIZE]));
 assertTrue(inputStream instanceof GzipCompressorInputStream);
 }

 @Test
 public void testCreateXZCompressorInputStream() throws IOException {
 CompressorFactory factory = new CompressorFactory();
 CompressorInputStream inputStream = factory.createCompressorInputStream("xz", new ByteArrayInputStream(new byte[SIZE]));
 assertTrue(inputStream instanceof XZCompressorInputStream);
 }

 @Test
 public void testCreateLZMACompressorInputStream() throws IOException {
 CompressorFactory factory = new CompressorFactory();
 CompressorInputStream inputStream = factory.createCompressorInputStream("lzma", new ByteArrayInputStream(new byte[SIZE]));
 assertTrue(inputStream instanceof LZMACompressorInputStream);
 }

 @Test
 public void testCreatePack200CompressorInputStream() throws IOException {
 CompressorFactory factory = new CompressorFactory();
 CompressorInputStream inputStream = factory.createCompressorInputStream("pack200", new ByteArrayInputStream(new byte[SIZE]));
 assertTrue(inputStream instanceof Pack200CompressorInputStream);
 }

 @Test
 public void testCreateSnappyCompressorInputStream() throws IOException {
 CompressorFactory factory = new CompressorFactory();
 CompressorInputStream inputStream = factory.createCompressorInputStream("snappy-raw", new ByteArrayInputStream(new byte[SIZE]));
 assertTrue(inputStream instanceof SnappyCompressorInputStream);
 }

 @Test
 public void testCreateSnappyFramedCompressorInputStream() throws IOException {
 CompressorFactory factory = new CompressorFactory();
 CompressorInputStream inputStream = factory.createCompressorInputStream("snappy-framed", new ByteArrayInputStream(new byte[SIZE]));
 assertTrue(inputStream instanceof FramedSnappyCompressorInputStream);
 }

 @Test
 public void testCreateZCompressorInputStream() throws IOException {
 CompressorFactory factory = new CompressorFactory();
 CompressorInputStream inputStream = factory.createCompressorInputStream("z", new ByteArrayInputStream(new byte[SIZE]));
 assertTrue(inputStream instanceof ZCompressorInputStream);
 }

 @Test
 public void testCreateCompressorOutputStream() throws IOException {
 CompressorFactory factory = new CompressorFactory();
 CompressorOutputStream outputStream = factory.createCompressorOutputStream("bzip2", new ByteArrayOutputStream(SIZE));
 assertNotNull(outputStream);
 }

 @Test(expected = CompressorException.class)
 public void testCreateUnknownCompressorOutputStream() throws IOException {
 CompressorFactory factory = new CompressorFactory();
 factory.createCompressorOutputStream("unknown", new ByteArrayOutputStream(SIZE));
 fail("Should throw CompressorException");
 }

 @Test
 public void testCreateDeflateCompressorOutputStream() throws IOException {
 CompressorFactory factory = new CompressorFactory();
 CompressorOutputStream outputStream = factory.createCompressorOutputStream("deflate", new ByteArrayOutputStream(SIZE));
 assertNotNull(outputStream);
 }

 @Test
 public void testCreateGzipCompressorOutputStream() throws IOException {
 CompressorFactory factory = new CompressorFactory();
 CompressorOutputStream outputStream = factory.createCompressorOutputStream("gzip", new ByteArrayOutputStream(SIZE));
 assertNotNull(outputStream);
 }

 @Test
 public void testCreateXZCompressorOutputStream() throws IOException {
 CompressorFactory factory = new CompressorFactory();
 CompressorOutputStream outputStream = factory.createCompressorOutputStream("xz", new ByteArrayOutputStream(SIZE));
 assertNotNull(outputStream);
 }

 @Test
 public void testCreateLZMACompressorOutputStream() throws IOException {
 CompressorFactory factory = new CompressorFactory();
 CompressorOutputStream outputStream = factory.createCompressorOutputStream("lzma", new ByteArrayOutputStream(SIZE));
 assertNotNull(outputStream);
 }

 @Test
 public void testCreatePack200CompressorOutputStream() throws IOException {
 CompressorFactory factory = new CompressorFactory();
 CompressorOutputStream outputStream = factory.createCompressorOutputStream("pack200", new ByteArrayOutputStream(SIZE));
 assertNotNull(outputStream);
 }

 @Test
 public void testCreateBZip2DecompressorInputStream() throws IOException {
 CompressorFactory factory = new CompressorFactory();
 CompressorInputStream inputStream = factory.createCompressorInputStream("bzip2", new ByteArrayInputStream(new byte[SIZE]));
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("bzip2", outputStream);
 inputStream.transferTo(compressorOutputStream);
 compressorOutputStream.finish();
 compressorOutputStream.close();
 DecompressorInputStream decompressorInputStream = factory.createCompressorInputStream("bzip2", new ByteArrayInputStream(outputStream.toByteArray()));
 byte[] result = new byte[SIZE];
 int length = decompressorInputStream.read(result);
 assertEquals(SIZE, length);
 assertArrayEquals(new byte[SIZE], result);
 }

 @Test
 public void testCreateGzipDecompressorInputStream() throws IOException {
 CompressorFactory factory = new CompressorFactory();
 CompressorInputStream inputStream = factory.createCompressorInputStream("gzip", new ByteArrayInputStream(new byte[SIZE]));
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("gzip", outputStream);
 inputStream.transferTo(compressorOutputStream);
 compressorOutputStream.finish();
 compressorOutputStream.close();
 DecompressorInputStream decompressorInputStream = factory.createCompressorInputStream("gzip", new ByteArrayInputStream(outputStream.toByteArray()));
 byte[] result = new byte[SIZE];
 int length = decompressorInputStream.read(result);
 assertEquals(SIZE, length);
 assertArrayEquals(new byte[SIZE], result);
 }

 @Test
 public void testCreateXZDecompressorInputStream() throws IOException {
 CompressorFactory factory = new CompressorFactory();
 CompressorInputStream inputStream = factory.createCompressorInputStream("xz", new ByteArrayInputStream(new byte[SIZE]));
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("xz", outputStream);
 inputStream.transferTo(compressorOutputStream);
 compressorOutputStream.finish();
 compressorOutputStream.close();
 DecompressorInputStream decompressorInputStream = factory.createCompressorInputStream("xz", new ByteArrayInputStream(outputStream.toByteArray()));
 byte[] result = new byte[SIZE];
 int length = decompressorInputStream.read(result);
 assertEquals(SIZE, length);
 assertArrayEquals(new byte[SIZE], result);
 }

 @Test
 public void testCreateLZMADecompressorInputStream() throws IOException {
 CompressorFactory factory = new CompressorFactory();
 CompressorInputStream inputStream = factory.createCompressorInputStream("lzma", new ByteArrayInputStream(new byte[SIZE]));
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("lzma", outputStream);
 inputStream.transferTo(compressorOutputStream);
 compressorOutputStream.finish();
 compressorOutputStream.close();
 DecompressorInputStream decompressorInputStream = factory.createCompressorInputStream("lzma", new ByteArrayInputStream(outputStream.toByteArray()));
 byte[] result = new byte[SIZE];
 int length = decompressorInputStream.read(result);
 assertEquals(SIZE, length);
 assertArrayEquals(new byte[SIZE], result);
 }

 @Test
 public void testCreateDecompressorInputStreamWithWrongCompressor() throws IOException {
 CompressorFactory factory = new CompressorFactory();
 CompressorInputStream inputStream = factory.createCompressorInputStream("bzip2", new ByteArrayInputStream(new byte[SIZE]));
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("gzip", outputStream);
 inputStream.transferTo(compressorOutputStream);
 compressorOutputStream.finish();
 compressorOutputStream.close();
 DecompressorInputStream decompressorInputStream = factory.createCompressorInputStream("gzip", new ByteArrayInputStream(outputStream.toByteArray()));
 try {
 decompressorInputStream.read(new byte[SIZE]);
 fail("Should throw CompressorException");
 } catch (CompressorException e) {
 // expected
 }
 }
}