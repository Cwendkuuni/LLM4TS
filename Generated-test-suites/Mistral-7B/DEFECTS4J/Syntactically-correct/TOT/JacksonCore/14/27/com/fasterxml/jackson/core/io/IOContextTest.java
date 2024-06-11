package com.fasterxml.jackson.core.io;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

 private IOContext ioContext;

 @Before
 public void setup() {
 ioContext = new IOContext(new BufferRecycler(), this, true);
 }

 @Test
 public void testSetEncoding() {
 JsonEncoding jsonEncoding = JsonEncoding.UTF8;
 ioContext.setEncoding(jsonEncoding);
 assertEquals(jsonEncoding, ioContext.getEncoding());
 }

 @Test
 public void testWithEncoding() {
 JsonEncoding jsonEncoding = JsonEncoding.UTF8;
 IOContext newIOContext = ioContext.withEncoding(jsonEncoding);
 assertEquals(jsonEncoding, newIOContext.getEncoding());
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
 fail("Should throw IllegalStateException when calling same method twice");
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
 }

 @Test
 public void testAllocNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(5);
 assertNotNull(nameCopyBuffer);
 assertEquals(5, nameCopyBuffer.length);
 }

 @Test
 public void testReleaseNameCopyBuffer() {
 char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(5);
 ioContext.releaseNameCopyBuffer(nameCopyBuffer);
 assertNull(ioContext.getNameCopyBuffer());
 }
}