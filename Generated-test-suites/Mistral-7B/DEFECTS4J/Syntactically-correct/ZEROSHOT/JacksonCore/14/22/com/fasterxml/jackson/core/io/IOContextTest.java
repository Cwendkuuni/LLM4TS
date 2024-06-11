package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

 IOContext ioContext;
 BufferRecycler bufferRecycler = new BufferRecycler();

 @Test
 public void testIOContextConstruction() {
 ioContext = new IOContext(bufferRecycler, null, false);
 assertNotNull(ioContext);
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
 ioContext = ioContext.withEncoding(encoding);
 assertEquals(encoding, ioContext.getEncoding());
 }

 @Test
 public void testGetSourceReference() {
 Object sourceRef = new Object();
 ioContext = new IOContext(bufferRecycler, sourceRef, false);
 assertEquals(sourceRef, ioContext.getSourceReference());
 }

 @Test
 public void testIsResourceManaged() {
 ioContext = new IOContext(bufferRecycler, null, true);
 assertTrue(ioContext.isResourceManaged());
 ioContext = new IOContext(bufferRecycler, null, false);
 assertFalse(ioContext.isResourceManaged());
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
 ioContext.releaseReadIOBuffer(readIOBuffer);
 }

 @Test
 public void testAllocReadIOBufferWithMinSize() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer(10);
 assertNotNull(readIOBuffer);
 ioContext.releaseReadIOBuffer(readIOBuffer);
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 @Test
 public void testAllocWriteEncodingBufferWithMinSize() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(10);
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
 public void testAllocTokenBufferWithMinSize() {
 char[] tokenBuffer = ioContext.allocTokenBuffer(10);
 assertNotNull(tokenBuffer);
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

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWrongBuffer() {
 ioContext = new IOContext(bufferRecycler, null, false);
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.releaseWriteEncodingBuffer(readIOBuffer);
 }
}