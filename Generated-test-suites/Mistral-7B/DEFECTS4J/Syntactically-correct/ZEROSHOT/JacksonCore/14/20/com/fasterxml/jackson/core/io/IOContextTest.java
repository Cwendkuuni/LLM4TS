package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.BufferRecycler;

public class IOContextTest {

 private BufferRecycler bufferRecycler;
 private IOContext ioContext;

 @Before
 public void setup() {
 bufferRecycler = new BufferRecycler();
 ioContext = new IOContext(bufferRecycler, null, false);
 }

 @Test
 public void testIOContextConstruction() {
 IOContext context = new IOContext(bufferRecycler, null, false);
 assertEquals(bufferRecycler, context._bufferRecycler);
 assertNull(context._sourceRef);
 assertFalse(context._managedResource);
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
 IOContext context = ioContext.withEncoding(encoding);
 assertEquals(encoding, context.getEncoding());
 assertEquals(ioContext, context); // verify it's the same instance
 }

 @Test
 public void testGetSourceReference() {
 Object sourceRef = new Object();
 IOContext context = new IOContext(bufferRecycler, sourceRef, false);
 assertEquals(sourceRef, context.getSourceReference());
 }

 @Test
 public void testGetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 IOContext context = new IOContext(bufferRecycler, null, false);
 context.setEncoding(encoding);
 assertEquals(encoding, context.getEncoding());
 }

 @Test
 public void testIsResourceManaged() {
 IOContext context = new IOContext(bufferRecycler, null, false);
 assertFalse(context.isResourceManaged());
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
 int minSize = 10;
 byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
 assertNotNull(readIOBuffer);
 assertArrayEquals(new byte[minSize], readIOBuffer);
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
 int minSize = 10;
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
 assertNotNull(writeEncodingBuffer);
 assertArrayEquals(new byte[minSize], writeEncodingBuffer);
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
 int minSize = 10;
 char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
 assertNotNull(tokenBuffer);
 assertArrayEquals(new char[minSize], tokenBuffer);
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

 @Test(expected = IllegalStateException.class)
 public void testDoubleRelease() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.releaseReadIOBuffer(readIOBuffer);
 ioContext.releaseReadIOBuffer(readIOBuffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReleaseDifferentBuffer() {
 byte[] otherReadIOBuffer = new byte[10];
 ioContext.releaseReadIOBuffer(otherReadIOBuffer);
 }

}