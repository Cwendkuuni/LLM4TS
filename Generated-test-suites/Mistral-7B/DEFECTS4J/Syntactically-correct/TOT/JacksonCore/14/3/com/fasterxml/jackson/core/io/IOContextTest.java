package com.fasterxml.jackson.core.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

 private IOContext ioContext;

 @Before
 public void setUp() {
 ioContext = new IOContext(new BufferRecycler(), this, true);
 }

 @Test
 public void testConstructor() {
 // Testing if the constructor is called correctly
 assertNotNull(ioContext);
 assertSame(this, ioContext.getSourceReference());
 assertTrue(ioContext.isResourceManaged());
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
 IOContext ioContextWithEncoding = ioContext.withEncoding(encoding);
 assertEquals(encoding, ioContextWithEncoding.getEncoding());
 }

 @Test
 public void testAllocReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 ioContext.releaseReadIOBuffer(readIOBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBufferTwice() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.allocReadIOBuffer();
 fail("Expected IllegalStateException but it did not occur.");
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 @Test
 public void testAllocBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 assertNotNull(base64Buffer);
 ioContext.releaseBase64Buffer(base64Buffer);
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 ioContext.releaseTokenBuffer(tokenBuffer);
 }

 @Test
 public void testAllocTokenBufferMinSize() {
 char[] tokenBuffer = ioContext.allocTokenBuffer(10);
 assertNotNull(tokenBuffer);
 assertArrayEquals(new char[10], tokenBuffer);
 ioContext.releaseTokenBuffer(tokenBuffer);
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 assertNotNull(concatBuffer);
 ioContext.releaseConcatBuffer(concatBuffer);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 assertNotNull(nameCopyBuffer);
 assertArrayEquals(new char[10], nameCopyBuffer);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 }

 @Test
 public void testReleaseReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.releaseReadIOBuffer(readIOBuffer);
 assertNull(ioContext._readIOBuffer);
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 assertNull(ioContext._writeEncodingBuffer);
 }

 @Test
 public void testReleaseBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 ioContext.releaseBase64Buffer(base64Buffer);
 assertNull(ioContext._base64Buffer);
 }

 @Test
 public void testReleaseTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 ioContext.releaseTokenBuffer(tokenBuffer);
 assertNull(ioContext._tokenCBuffer);
 }

 @Test
 public void testReleaseConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 ioContext.releaseConcatBuffer(concatBuffer);
 assertNull(ioContext._concatCBuffer);
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull(ioContext._nameCopyBuffer);
 }

 @After
 public void tearDown() {
 // Release all buffers to make sure they are recycled properly
 ioContext.releaseReadIOBuffer(_readIOBuffer);
 ioContext.releaseWriteEncodingBuffer(_writeEncodingBuffer);
 ioContext.releaseBase64Buffer(_base64Buffer);
 ioContext.releaseTokenBuffer(_tokenCBuffer);
 ioContext.releaseConcatBuffer(_concatCBuffer);
 ioContext.releaseNameCopyBuffer(_nameCopyBuffer);
 }
}