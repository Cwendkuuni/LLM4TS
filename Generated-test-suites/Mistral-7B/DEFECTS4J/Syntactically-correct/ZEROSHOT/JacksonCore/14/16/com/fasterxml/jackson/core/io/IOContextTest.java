package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IOContextTest {

 private BufferRecycler bufferRecycler;
 private IOContext ioContext;

 @Before
 public void setUp() {
 bufferRecycler = mock(BufferRecycler.class);
 ioContext = new IOContext(bufferRecycler, new Object(), true);
 }

 @After
 public void tearDown() {
 if (ioContext != null) {
 ioContext.releaseAllBuffers();
 }
 }

 @Test
 public void testConstructor() {
 assertSame(bufferRecycler, ioContext._bufferRecycler);
 assertNull(ioContext._sourceRef);
 assertTrue(ioContext._managedResource);
 }

 @Test
 public void testSetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 ioContext.setEncoding(encoding);
 assertEquals(encoding, ioContext.getEncoding());
 }

 @Test
 public void testWithEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 IOContext withEncodingContext = ioContext.withEncoding(encoding);
 assertEquals(encoding, withEncodingContext.getEncoding());
 assertEquals(ioContext, withEncodingContext);
 }

 @Test
 public void testGetSourceReference() {
 Object reference = new Object();
 ioContext = new IOContext(bufferRecycler, reference, true);
 assertEquals(reference, ioContext.getSourceReference());
 }

 @Test
 public void testGetEncoding() {
 assertNull(ioContext.getEncoding());
 }

 @Test
 public void testIsResourceManaged() {
 assertTrue(ioContext.isResourceManaged());
 }

 @Test
 public void testConstructTextBuffer() {
 TextBuffer textBuffer = ioContext.constructTextBuffer();
 assertSame(bufferRecycler, textBuffer._bufferRecycler);
 }

 @Test
 public void testAllocReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 assertNull(ioContext._readIOBuffer);
 assertSame(bufferRecycler, readIOBuffer._recycler);
 }

 @Test
 public void testAllocReadIOBufferWithMinSize() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer(10);
 assertNull(ioContext._readIOBuffer);
 assertSame(bufferRecycler, readIOBuffer._recycler);
 assertEquals(10, readIOBuffer.length);
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNull(ioContext._writeEncodingBuffer);
 assertSame(bufferRecycler, writeEncodingBuffer._recycler);
 }

 @Test
 public void testAllocWriteEncodingBufferWithMinSize() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(10);
 assertNull(ioContext._writeEncodingBuffer);
 assertSame(bufferRecycler, writeEncodingBuffer._recycler);
 assertEquals(10, writeEncodingBuffer.length);
 }

 @Test
 public void testAllocBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 assertNull(ioContext._base64Buffer);
 assertSame(bufferRecycler, base64Buffer._recycler);
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 assertNull(ioContext._tokenCBuffer);
 assertSame(bufferRecycler, tokenBuffer._recycler);
 }

 @Test
 public void testAllocTokenBufferWithMinSize() {
 char[] tokenBuffer = ioContext.allocTokenBuffer(10);
 assertNull(ioContext._tokenCBuffer);
 assertSame(bufferRecycler, tokenBuffer._recycler);
 assertEquals(10, tokenBuffer.length);
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 assertNull(ioContext._concatCBuffer);
 assertSame(bufferRecycler, concatBuffer._recycler);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 assertNull(ioContext._nameCopyBuffer);
 assertSame(bufferRecycler, nameCopyBuffer._recycler);
 assertEquals(10, nameCopyBuffer.length);
 }

 @Test
 public void testReleaseReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.releaseReadIOBuffer(readIOBuffer);
 assertNull(ioContext._readIOBuffer);
 verify(bufferRecycler, times(1)).releaseByteBuffer(BufferRecycler.BYTE_READ_IO_BUFFER, readIOBuffer);
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 assertNull(ioContext._writeEncodingBuffer);
 verify(bufferRecycler, times(1)).releaseByteBuffer(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER, writeEncodingBuffer);
 }

 @Test
 public void testReleaseBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 ioContext.releaseBase64Buffer(base64Buffer);
 assertNull(ioContext._base64Buffer);
 verify(bufferRecycler, times(1)).releaseByteBuffer(BufferRecycler.BYTE_BASE64_CODEC_BUFFER, base64Buffer);
 }

 @Test
 public void testReleaseTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 ioContext.releaseTokenBuffer(tokenBuffer);
 assertNull(ioContext._tokenCBuffer);
 verify(bufferRecycler, times(1)).releaseCharBuffer(BufferRecycler.CHAR_TOKEN_BUFFER, tokenBuffer);
 }

 @Test
 public void testReleaseConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 ioContext.releaseConcatBuffer(concatBuffer);
 assertNull(ioContext._concatCBuffer);
 verify(bufferRecycler, times(1)).releaseCharBuffer(BufferRecycler.CHAR_CONCAT_BUFFER, concatBuffer);
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull(ioContext._nameCopyBuffer);
 verify(bufferRecycler, times(1)).releaseCharBuffer(BufferRecycler.CHAR_NAME_COPY_BUFFER, nameCopyBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseUnownedBuffer() {
 byte[] unownedBuffer = new byte[10];
 ioContext.releaseReadIOBuffer(unownedBuffer);
 }

 @Test
 public void testReleaseAllBuffers() {
 ioContext.releaseAllBuffers();
 assertNull(ioContext._readIOBuffer);
 assertNull(ioContext._writeEncodingBuffer);
 assertNull(ioContext._base64Buffer);
 assertNull(ioContext._tokenCBuffer);
 assertNull(ioContext._concatCBuffer);
 assertNull(ioContext._nameCopyBuffer);
 }

}