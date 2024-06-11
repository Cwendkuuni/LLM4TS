package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
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
 IOContext ioContext1 = new IOContext(bufferRecycler, null, false);
 IOContext ioContext2 = new IOContext(bufferRecycler, null, true);

 assertEquals(bufferRecycler, ioContext1._bufferRecycler);
 assertEquals(bufferRecycler, ioContext2._bufferRecycler);
 assertNotNull(ioContext1._sourceRef);
 assertFalse(ioContext1._managedResource);
 assertFalse(ioContext2._managedResource);
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

 @Test(expected = IllegalStateException.class)
 public void testAllocReadIOBufferTwice() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 ioContext.allocReadIOBuffer();
 }

 @Test
 public void testAllocReadIOBufferMinSize() {
 int minSize = 10;
 byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
 assertEquals(minSize, readIOBuffer.length);
 }

 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 }

 @Test
 public void testAllocWriteEncodingBufferMinSize() {
 int minSize = 10;
 byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
 assertEquals(minSize, writeEncodingBuffer.length);
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

 @Test
 public void testAllocTokenBufferMinSize() {
 int minSize = 10;
 char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
 assertEquals(minSize, tokenBuffer.length);
 }

 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = ioContext.allocConcatBuffer();
 assertNotNull(concatBuffer);
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
 assertNotNull(nameCopyBuffer);
 assertEquals(10, nameCopyBuffer.length);
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
}