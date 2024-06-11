package com.fasterxml.jackson.core.io;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.core.JsonEncoding;

public class IOContextTest {

 private IOContext ioContext;
 private BufferRecycler bufferRecycler;

 @Before
 public void setup() {
 bufferRecycler = mock(BufferRecycler.class);
 ioContext = new IOContext(bufferRecycler, new Object(), true);
 }

 @Test
 public void testIOContextConstruction() {
 assertSame(bufferRecycler, ioContext._bufferRecycler);
 assertNull(ioContext._readIOBuffer);
 assertNull(ioContext._writeEncodingBuffer);
 assertNull(ioContext._base64Buffer);
 assertNull(ioContext._tokenCBuffer);
 assertNull(ioContext._concatCBuffer);
 assertNull(ioContext._nameCopyBuffer);
 assertNull(ioContext._encoding);
 assertNull(ioContext._managedResource);
 }

 @Test
 public void testSetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 ioContext.setEncoding(encoding);
 assertSame(encoding, ioContext._encoding);
 }

 @Test
 public void testWithEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 IOContext ioContextWithEncoding = ioContext.withEncoding(encoding);
 assertSame(encoding, ioContextWithEncoding._encoding);
 }

 @Test
 public void testGetSourceReference() {
 assertSame(new Object(), ioContext.getSourceReference());
 }

 @Test
 public void testGetEncoding() {
 assertNull(ioContext._encoding);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetEncodingBeforeSet() {
 ioContext.getEncoding();
 }

 @Test
 public void testIsResourceManaged() {
 assertTrue(ioContext.isResourceManaged());
 }

 @Test
 public void testConstructTextBuffer() {
 TextBuffer textBuffer = ioContext.constructTextBuffer();
 assertNotNull(textBuffer);
 }

 @Test
 public void testAllocReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 }

 @Test
 public void testAllocReadIOBufferWithMinSize() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer(10);
 assertNotNull(readIOBuffer);
 assertArrayEquals(new byte[10], readIOBuffer);
 }

 @Test
 public void testReleaseReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.releaseReadIOBuffer(readIOBuffer);
 assertNull(ioContext._readIOBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseReadIOBufferWithNullBuffer() {
 ioContext.releaseReadIOBuffer(null);
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 }

 @Test
 public void testAllocWriteEncodingBufferWithMinSize() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(10);
 assertNotNull(writeEncodingBuffer);
 assertArrayEquals(new byte[10], writeEncodingBuffer);
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 assertNull(ioContext._writeEncodingBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWriteEncodingBufferWithNullBuffer() {
 ioContext.releaseWriteEncodingBuffer(null);
 }

 @Test
 public void testAllocBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 assertNotNull(base64Buffer);
 }

 @Test
 public void testReleaseBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 ioContext.releaseBase64Buffer(base64Buffer);
 assertNull(ioContext._base64Buffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseBase64BufferWithNullBuffer() {
 ioContext.releaseBase64Buffer(null);
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 }

 @Test
 public void testAllocTokenBufferWithMinSize() {
 char[] tokenBuffer = ioContext.allocTokenBuffer(10);
 assertNotNull(tokenBuffer);
 assertArrayEquals(new char[10], tokenBuffer);
 }

 @Test
 public void testReleaseTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 ioContext.releaseTokenBuffer(tokenBuffer);
 assertNull(ioContext._tokenCBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseTokenBufferWithNullBuffer() {
 ioContext.releaseTokenBuffer(null);
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 assertNotNull(concatBuffer);
 }

 @Test
 public void testReleaseConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 ioContext.releaseConcatBuffer(concatBuffer);
 assertNull(ioContext._concatCBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseConcatBufferWithNullBuffer() {
 ioContext.releaseConcatBuffer(null);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 assertNotNull(nameCopyBuffer);
 assertArrayEquals(new char[10], nameCopyBuffer);
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull(ioContext._nameCopyBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseNameCopyBufferWithNullBuffer() {
 ioContext.releaseNameCopyBuffer(null);
 }
}