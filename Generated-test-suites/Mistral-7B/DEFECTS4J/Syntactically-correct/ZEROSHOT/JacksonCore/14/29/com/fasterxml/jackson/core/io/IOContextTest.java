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
 ioContext = new IOContext(bufferRecycler, "SourceRef", true);
 }

 @After
 public void tearDown() {
 ioContext = null;
 bufferRecycler = null;
 }

 @Test
 public void testIOContextConstruction() {
 assertNotNull(ioContext);
 assertEquals("SourceRef", ioContext.getSourceReference());
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
 IOContext contextWithEncoding = ioContext.withEncoding(encoding);
 assertEquals(encoding, contextWithEncoding.getEncoding());
 assertEquals(ioContext, contextWithEncoding);
 }

 @Test
 public void testAllocReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 assertNull(ioContext.getReadIOBuffer());
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBufferTwice() {
 ioContext.allocReadIOBuffer();
 ioContext.allocReadIOBuffer();
 fail("Should not allocate read IO buffer twice");
 }

 @Test
 public void testReleaseReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.releaseReadIOBuffer(readIOBuffer);
 assertNull(ioContext.getReadIOBuffer());
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 assertNull(ioContext.getWriteEncodingBuffer());
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocWriteEncodingBufferTwice() {
 ioContext.allocWriteEncodingBuffer();
 ioContext.allocWriteEncodingBuffer();
 fail("Should not allocate write encoding buffer twice");
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
 assertNull(ioContext.getWriteEncodingBuffer());
 }

 @Test
 public void testAllocBase64Buffer() {
 byte[] base64Buffer = ioContext.allocBase64Buffer();
 assertNotNull(base64Buffer);
 assertNull(ioContext.getBase64Buffer());
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 assertNull(ioContext.getTokenBuffer());
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocTokenBufferTwice() {
 ioContext.allocTokenBuffer();
 ioContext.allocTokenBuffer();
 fail("Should not allocate token buffer twice");
 }

 @Test
 public void testReleaseTokenBuffer() {
 char[] tokenBuffer = ioContext.allocTokenBuffer();
 ioContext.releaseTokenBuffer(tokenBuffer);
 assertNull(ioContext.getTokenBuffer());
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 assertNotNull(concatBuffer);
 assertNull(ioContext.getConcatBuffer());
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocConcatBufferTwice() {
 ioContext.allocConcatBuffer();
 ioContext.allocConcatBuffer();
 fail("Should not allocate concat buffer twice");
 }

 @Test
 public void testReleaseConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 ioContext.releaseConcatBuffer(concatBuffer);
 assertNull(ioContext.getConcatBuffer());
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(5);
 assertNotNull(nameCopyBuffer);
 assertNull(ioContext.getNameCopyBuffer());
 assertEquals(5, nameCopyBuffer.length);
 }

 @Test(expected = IllegalStateException.class)
 public void testAllocNameCopyBufferTwice() {
 ioContext.allocNameCopyBuffer();
 ioContext.allocNameCopyBuffer();
 fail("Should not allocate name copy buffer twice");
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(5);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull(ioContext.getNameCopyBuffer());
 }
}