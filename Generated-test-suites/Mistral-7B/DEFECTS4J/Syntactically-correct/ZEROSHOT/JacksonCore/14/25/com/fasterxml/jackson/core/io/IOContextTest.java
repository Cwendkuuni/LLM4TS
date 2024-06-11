package com.fasterxml.jackson.core.io;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;

public class IOContextTest {

 private IOContext ioContext;
 private BufferRecycler bufferRecycler;

 @Before
 public void setUp() {
 bufferRecycler = new BufferRecycler();
 ioContext = new IOContext(bufferRecycler, null, false);
 }

 @Test
 public void testIOContextConstruction() {
 assertNotNull(ioContext);
 assertEquals(bufferRecycler, ioContext.getBufferRecycler());
 assertNull(ioContext.getSourceRef());
 assertNull(ioContext.getEncoding());
 assertFalse(ioContext.isResourceManaged());
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
 IOContext contextWithEncoding = ioContext.withEncoding(encoding);
 assertEquals(encoding, contextWithEncoding.getEncoding());
 assertEquals(ioContext, contextWithEncoding);
 }

 @Test
 public void testGetSourceReference() {
 Object sourceRef = new Object();
 IOContext contextWithSourceRef = new IOContext(bufferRecycler, sourceRef, false);
 assertEquals(sourceRef, contextWithSourceRef.getSourceReference());
 }

 @Test
 public void testGetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 IOContext contextWithEncoding = new IOContext(bufferRecycler, null, false);
 contextWithEncoding.setEncoding(encoding);
 assertEquals(encoding, contextWithEncoding.getEncoding());
 }

 @Test
 public void testIsResourceManaged() {
 IOContext contextManagedResource = new IOContext(bufferRecycler, null, true);
 assertTrue(contextManagedResource.isResourceManaged());
 IOContext contextUnmanagedResource = new IOContext(bufferRecycler, null, false);
 assertFalse(contextUnmanagedResource.isResourceManaged());
 }

 @Test
 public void testConstructTextBuffer() {
 TextBuffer textBuffer = ioContext.constructTextBuffer();
 assertNotNull(textBuffer);
 assertEquals(bufferRecycler, textBuffer.getBufferRecycler());
 }

 @Test
 public void testAllocReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 assertTrue(readIOBuffer.length > 0);
 assertEquals(bufferRecycler, readIOBuffer.length > 0 ? readIOBuffer[0] : null, ioContext.getBufferRecycler());
 ioContext.releaseReadIOBuffer(readIOBuffer);
 }

 @Test
 public void testAllocReadIOBufferWithMinSize() {
 int minSize = 10;
 byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
 assertNotNull(readIOBuffer);
 assertTrue(readIOBuffer.length >= minSize);
 assertEquals(bufferRecycler, readIOBuffer.length > 0 ? readIOBuffer[0] : null, ioContext.getBufferRecycler());
 ioContext.releaseReadIOBuffer(readIOBuffer);
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 assertTrue(writeEncodingBuffer.length > 0);
 assertEquals(bufferRecycler, writeEncodingBuffer.length > 0 ? writeEncodingBuffer[0] : null, ioContext.getBufferRecycler());
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 @Test
 public void testAllocWriteEncodingBufferWithMinSize() {
 int minSize = 10;
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
 assertNotNull(writeEncodingBuffer);
 assertTrue(writeEncodingBuffer.length >= minSize);
 assertEquals(bufferRecycler, writeEncodingBuffer.length > 0 ? writeEncodingBuffer[0] : null, ioContext.getBufferRecycler());
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 @Test
 public void testAllocBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 assertNotNull(base64Buffer);
 assertTrue(base64Buffer.length > 0);
 assertEquals(bufferRecycler, base64Buffer.length > 0 ? base64Buffer[0] : null, ioContext.getBufferRecycler());
 ioContext.releaseBase64Buffer(base64Buffer);
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 assertTrue(tokenBuffer.length > 0);
 assertEquals(bufferRecycler, tokenBuffer.length > 0 ? tokenBuffer[0] : null, ioContext.getBufferRecycler());
 ioContext.releaseTokenBuffer(tokenBuffer);
 }

 @Test
 public void testAllocTokenBufferWithMinSize() {
 int minSize = 10;
 char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
 assertNotNull(tokenBuffer);
 assertTrue(tokenBuffer.length >= minSize);
 assertEquals(bufferRecycler, tokenBuffer.length > 0 ? tokenBuffer[0] : null, ioContext.getBufferRecycler());
 ioContext.releaseTokenBuffer(tokenBuffer);
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 assertNotNull(concatBuffer);
 assertTrue(concatBuffer.length > 0);
 assertEquals(bufferRecycler, concatBuffer.length > 0 ? concatBuffer[0] : null, ioContext.getBufferRecycler());
 ioContext.releaseConcatBuffer(concatBuffer);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(5);
 assertNotNull(nameCopyBuffer);
 assertTrue(nameCopyBuffer.length == 5);
 assertEquals(bufferRecycler, nameCopyBuffer.length > 0 ? nameCopyBuffer[0] : null, ioContext.getBufferRecycler());
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 }

 @Test
 public void testReleaseReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.releaseReadIOBuffer(readIOBuffer);
 assertNull(ioContext.getReadIOBuffer());
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 assertNull(ioContext.getWriteEncodingBuffer());
 }

 @Test
 public void testReleaseBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 ioContext.releaseBase64Buffer(base64Buffer);
 assertNull(ioContext.getBase64Buffer());
 }

 @Test
 public void testReleaseTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 ioContext.releaseTokenBuffer(tokenBuffer);
 assertNull(ioContext.getTokenBuffer());
 }

 @Test
 public void testReleaseConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 ioContext.releaseConcatBuffer(concatBuffer);
 assertNull(ioContext.getConcatBuffer());
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(5);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull(ioContext.getNameCopyBuffer());
 }
}