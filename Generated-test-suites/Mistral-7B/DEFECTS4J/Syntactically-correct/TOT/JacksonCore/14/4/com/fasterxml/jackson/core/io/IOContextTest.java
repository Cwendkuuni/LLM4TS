package com.fasterxml.jackson.core.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class IOContextTest {

 private IOContext _context;

 @Before
 public void setUp() {
 _context = new IOContext(new BufferRecycler(), new Object(), true);
 }

 @Test
 public void testIOContextConstruction() {
 assertEquals("Object sourceRef is set correctly", new Object(), _context.getSourceReference());
 assertTrue("Managed resource flag is set correctly", _context.isResourceManaged());
 }

 @Test
 public void testSetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 _context.setEncoding(encoding);
 assertEquals("Encoding is set correctly", encoding, _context.getEncoding());
 }

 @Test
 public void testWithEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 IOContext contextWithEncoding = _context.withEncoding(encoding);
 assertEquals("Encoding is set correctly", encoding, contextWithEncoding.getEncoding());
 assertEquals("This and original context are not the same", _context, _context);
 assertEquals("Encodings are the same", encoding, _context.getEncoding());
 }

 @Test
 public void testAllocReadIOBuffer() {
 byte[] readIOBuffer = _context.allocReadIOBuffer();
 assertNull("Initial buffer is null", readIOBuffer);
 readIOBuffer = _context.allocReadIOBuffer(10);
 assertNotNull("Allocated buffer is not null", readIOBuffer);
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = _context.allocWriteEncodingBuffer();
 assertNull("Initial buffer is null", writeEncodingBuffer);
 writeEncodingBuffer = _context.allocWriteEncodingBuffer(10);
 assertNotNull("Allocated buffer is not null", writeEncodingBuffer);
 }

 @Test
 public void testAllocBase64Buffer() {
 byte[] base64Buffer = _context.allocBase64Buffer();
 assertNull("Initial buffer is null", base64Buffer);
 base64Buffer = _context.allocBase64Buffer();
 assertNotNull("Allocated buffer is not null", base64Buffer);
 }

 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = _context.allocTokenBuffer();
 assertNull("Initial buffer is null", tokenBuffer);
 tokenBuffer = _context.allocTokenBuffer(10);
 assertNotNull("Allocated buffer is not null", tokenBuffer);
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = _context.allocConcatBuffer();
 assertNull("Initial buffer is null", concatBuffer);
 concatBuffer = _context.allocConcatBuffer();
 assertNotNull("Allocated buffer is not null", concatBuffer);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = _context.allocNameCopyBuffer(10);
 assertNotNull("Allocated buffer is not null", nameCopyBuffer);
 nameCopyBuffer = _context.allocNameCopyBuffer(5);
 assertNull("Initial buffer is null", nameCopyBuffer);
 }

 @Test
 public void testReleaseReadIOBuffer() {
 byte[] readIOBuffer = _context.allocReadIOBuffer();
 assertNotNull("Allocated buffer is not null", readIOBuffer);
 _context.releaseReadIOBuffer(readIOBuffer);
 assertNull("Released buffer is null", readIOBuffer);
 }

 @Test
 public void testReleaseWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = _context.allocWriteEncodingBuffer();
 assertNotNull("Allocated buffer is not null", writeEncodingBuffer);
 _context.releaseWriteEncodingBuffer(writeEncodingBuffer);
 assertNull("Released buffer is null", writeEncodingBuffer);
 }

 @Test
 public void testReleaseBase64Buffer() {
 byte[] base64Buffer = _context.allocBase64Buffer();
 assertNotNull("Allocated buffer is not null", base64Buffer);
 _context.releaseBase64Buffer(base64Buffer);
 assertNull("Released buffer is null", base64Buffer);
 }

 @Test
 public void testReleaseTokenBuffer() {
 char[] tokenBuffer = _context.allocTokenBuffer();
 assertNotNull("Allocated buffer is not null", tokenBuffer);
 _context.releaseTokenBuffer(tokenBuffer);
 assertNull("Released buffer is null", tokenBuffer);
 }

 @Test
 public void testReleaseConcatBuffer() {
 char[] concatBuffer = _context.allocConcatBuffer();
 assertNotNull("Allocated buffer is not null", concatBuffer);
 _context.releaseConcatBuffer(concatBuffer);
 assertNull("Released buffer is null", concatBuffer);
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 char[] nameCopyBuffer = _context.allocNameCopyBuffer(10);
 assertNotNull("Allocated buffer is not null", nameCopyBuffer);
 _context.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull("Released buffer is null", nameCopyBuffer);
 }

 @After
 public void tearDown() {
// Recycling buffers is not possible in JUnit test environment
// _context.close();
 }

}