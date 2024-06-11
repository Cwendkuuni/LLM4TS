package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IOContextTest {

 private BufferRecycler bufferRecycler;
 private IOContext ioContext;

 @Before
 public void setUp() {
 bufferRecycler = new BufferRecycler();
 ioContext = new IOContext(bufferRecycler, this, true);
 }

 @After
 public void tearDown() {
 ioContext.releaseAllBuffers();
 bufferRecycler = null; // for GC
 }

 @Test
 public void testIOContextConstruction() {
 assertSame(bufferRecycler, ioContext._bufferRecycler);
 assertSame(this, ioContext._sourceRef);
 assertTrue(ioContext._managedResource);
 }

 @Test
 public void testSetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 ioContext.setEncoding(encoding);
 assertSame(encoding, ioContext.getEncoding());
 }

 @Test
 public void testWithEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 IOContext ioContextWithEncoding = ioContext.withEncoding(encoding);
 assertSame(encoding, ioContextWithEncoding.getEncoding());
 assertNotSame(ioContext, ioContextWithEncoding);
 }

 @Test
 public void testGetSourceReference() {
 assertSame(this, ioContext.getSourceReference());
 }

 @Test
 public void testGetEncoding() {
 assertNull(ioContext.getEncoding());
 JsonEncoding encoding = JsonEncoding.UTF8;
 ioContext.setEncoding(encoding);
 assertSame(encoding, ioContext.getEncoding());
 }

 @Test
 public void testIsManagedResource() {
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

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBufferTwice() {
 ioContext.allocReadIOBuffer();
 ioContext.allocReadIOBuffer();
 }

 @Test
 public void testReleaseReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.releaseReadIOBuffer(readIOBuffer);
 assertNull(ioContext._readIOBuffer);
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocWriteEncodingBufferTwice() {
 ioContext.allocWriteEncodingBuffer();
 ioContext.allocWriteEncodingBuffer();
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 assertNull(ioContext._writeEncodingBuffer);
 }

 @Test
 public void testAllocBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 assertNotNull(base64Buffer);
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocTokenBufferTwice() {
 ioContext.allocTokenBuffer();
 ioContext.allocTokenBuffer();
 }

 @Test
 public void testReleaseTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 ioContext.releaseTokenBuffer(tokenBuffer);
 assertNull(ioContext._tokenCBuffer);
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull(ioContext._nameCopyBuffer);
 }

 @Test
 public void testVerifyAlloc() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext._verifyAlloc(readIOBuffer);
 assertNull(ioContext._readIOBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyRelease() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 byte[] differentBuffer = new byte[10];
 ioContext._verifyRelease(differentBuffer, readIOBuffer);
 }
}