package com.fasterxml.jackson.core.io;

import org.junit.After;
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

 @After
 public void tearDown() {
 if (ioContext != null) {
 ioContext.releaseAllBuffers();
 ioContext = null;
 }
 bufferRecycler = null;
 }

 @Test
 public void testIOContextConstruction() {
 IOContext ioContext1 = new IOContext(bufferRecycler, null, false);
 IOContext ioContext2 = new IOContext(bufferRecycler, null, true);
 assertNotSame(ioContext1, ioContext2);
 }

 @Test
 public void testGetSourceReference() {
 Object sourceRef = new Object();
 IOContext ioContext = new IOContext(bufferRecycler, sourceRef, false);
 assertEquals(sourceRef, ioContext.getSourceReference());
 }

 @Test
 public void testGetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 IOContext ioContext = new IOContext(bufferRecycler, null, false);
 ioContext.setEncoding(encoding);
 assertEquals(encoding, ioContext.getEncoding());
 }

 @Test
 public void testIsResourceManaged() {
 IOContext ioContext1 = new IOContext(bufferRecycler, null, false);
 assertTrue(ioContext1.isResourceManaged());
 IOContext ioContext2 = new IOContext(bufferRecycler, null, true);
 assertFalse(ioContext2.isResourceManaged());
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

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBufferTwice() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.allocReadIOBuffer();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseReadIOBufferNull() {
 ioContext.releaseReadIOBuffer(null);
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocWriteEncodingBufferTwice() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 ioContext.allocWriteEncodingBuffer();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWriteEncodingBufferNull() {
 ioContext.releaseWriteEncodingBuffer(null);
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

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseTokenBufferNull() {
 ioContext.releaseTokenBuffer(null);
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 assertNotNull(concatBuffer);
 ioContext.releaseConcatBuffer(concatBuffer);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(5);
 assertNotNull(nameCopyBuffer);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 }

 @Test
 public void testReleaseAllBuffers() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 ioContext.releaseAllBuffers();
 assertNull(ioContext.getReadIOBuffer());
 assertNull(ioContext.getWriteEncodingBuffer());
 }

}