package com.fasterxml.jackson.core.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class IOContextTest {

 private IOContext _context;
 private BufferRecycler _bufferRecycler = new BufferRecycler();

 @Before
 public void setup() {
 _context = new IOContext(_bufferRecycler, this, true);
 }

 @After
 public void teardown() {
 _context.releaseReadIOBuffer(_context.allocReadIOBuffer());
 _context.releaseWriteEncodingBuffer(_context.allocWriteEncodingBuffer());
 _context = null;
 _bufferRecycler = null;
 }

 /**
 * Test constructor
 */
 @Test
 public void testConstructor() {
 Object sourceRef = new Object();
 IOContext context = new IOContext(_bufferRecycler, sourceRef, true);
 assertNull(context.getReadIOBuffer());
 assertNull(context.getWriteEncodingBuffer());
 assertSame(sourceRef, context.getSourceReference());
 assertSame(true, context.isResourceManaged());
 }

 /**
 * Test setEncoding method
 */
 @Test
 public void testSetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 _context.setEncoding(encoding);
 assertSame(encoding, _context.getEncoding());
 }

 /**
 * Test withEncoding method
 */
 @Test
 public void testWithEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 IOContext context = _context.withEncoding(encoding);
 assertSame(encoding, context.getEncoding());
 assertSame(_context, _context.getInputBuffer());
 }

 /**
 * Test getSourceReference method
 */
 @Test
 public void testGetSourceReference() {
 Object sourceRef = new Object();
 _context = new IOContext(_bufferRecycler, sourceRef, true);
 assertSame(sourceRef, _context.getSourceReference());
 }

 /**
 * Test getEncoding method
 */
 @Test
 public void testGetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 _context.setEncoding(encoding);
 assertSame(encoding, _context.getEncoding());
 }

 /**
 * Test isResourceManaged method
 */
 @Test
 public void testIsResourceManaged() {
 assertSame(true, _context.isResourceManaged());
 }

 /**
 * Test constructTextBuffer method
 */
 @Test
 public void testConstructTextBuffer() {
 TextBuffer textBuffer = _context.constructTextBuffer();
 assertNotNull(textBuffer);
 }

 /**
 * Test allocReadIOBuffer method
 */
 @Test
 public void testAllocReadIOBuffer() {
 byte[] readIOBuffer = _context.allocReadIOBuffer();
 assertNotNull(readIOBuffer);
 assertNull(_context.getReadIOBuffer());
 _context.releaseReadIOBuffer(readIOBuffer);
 }

 /**
 * Test allocReadIOBuffer with minimum size
 */
 @Test
 public void testAllocReadIOBufferWithMinimumSize() {
 int minSize = 1024;
 byte[] readIOBuffer = _context.allocReadIOBuffer(minSize);
 assertNotNull(readIOBuffer);
 assertNull(_context.getReadIOBuffer());
 _context.releaseReadIOBuffer(readIOBuffer);
 }

 /**
 * Test allocWriteEncodingBuffer method
 */
 @Test
 public void testAllocWriteEncodingBuffer() {
 byte[] writeEncodingBuffer = _context.allocWriteEncodingBuffer();
 assertNotNull(writeEncodingBuffer);
 assertNull(_context.getWriteEncodingBuffer());
 _context.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 /**
 * Test allocWriteEncodingBuffer with minimum size
 */
 @Test
 public void testAllocWriteEncodingBufferWithMinimumSize() {
 int minSize = 1024;
 byte[] writeEncodingBuffer = _context.allocWriteEncodingBuffer(minSize);
 assertNotNull(writeEncodingBuffer);
 assertNull(_context.getWriteEncodingBuffer());
 _context.releaseWriteEncodingBuffer(writeEncodingBuffer);
 }

 /**
 * Test allocBase64Buffer method
 */
 @Test
 public void testAllocBase64Buffer() {
 byte[] base64Buffer = _context.allocBase64Buffer();
 assertNotNull(base64Buffer);
 assertNull(_context.getBase64Buffer());
 _context.releaseBase64Buffer(base64Buffer);
 }

 /**
 * Test allocTokenBuffer method
 */
 @Test
 public void testAllocTokenBuffer() {
 char[] tokenBuffer = _context.allocTokenBuffer();
 assertNotNull(tokenBuffer);
 assertNull(_context.getTokenBuffer());
 _context.releaseTokenBuffer(tokenBuffer);
 }

 /**
 * Test allocTokenBuffer with minimum size
 */
 @Test
 public void testAllocTokenBufferWithMinimumSize() {
 int minSize = 1024;
 char[] tokenBuffer = _context.allocTokenBuffer(minSize);
 assertNotNull(tokenBuffer);
 assertNull(_context.getTokenBuffer());
 _context.releaseTokenBuffer(tokenBuffer);
 }

 /**
 * Test allocConcatBuffer method
 */
 @Test
 public void testAllocConcatBuffer() {
 char[] concatBuffer = _context.allocConcatBuffer();
 assertNotNull(concatBuffer);
 assertNull(_context.getConcatBuffer());
 _context.releaseConcatBuffer(concatBuffer);
 }

 /**
 * Test allocNameCopyBuffer method
 */
 @Test
 public void testAllocNameCopyBuffer() {
 int minSize = 1024;
 char[] nameCopyBuffer = _context.allocNameCopyBuffer(minSize);
 assertNotNull(nameCopyBuffer);
 assertNull(_context.getNameCopyBuffer());
 _context.releaseNameCopyBuffer(nameCopyBuffer);
 }

 /**
 * Test releaseReadIOBuffer method
 */
 @Test(expected = IllegalArgumentException.class)
 public void testReleaseReadIOBufferWithNullBuffer() {
 _context.releaseReadIOBuffer(null);
 }

 /**
 * Test releaseWriteEncodingBuffer method
 */
 @Test(expected = IllegalArgumentException.class)
 public void testReleaseWriteEncodingBufferWithNullBuffer() {
 _context.releaseWriteEncodingBuffer(null);
 }

 /**
 * Test releaseBase64Buffer method
 */
 @Test(expected = IllegalArgumentException.class)
 public void testReleaseBase64BufferWithNullBuffer() {
 _context.releaseBase64Buffer(null);
 }

 /**
 * Test releaseTokenBuffer method
 */
 @Test(expected = IllegalArgumentException.class)
 public void testReleaseTokenBufferWithNullBuffer() {
 _context.releaseTokenBuffer(null);
 }

 /**
 * Test releaseConcatBuffer method
 */
 @Test(expected = IllegalArgumentException.class)
 public void testReleaseConcatBufferWithNullBuffer() {
 _context.releaseConcatBuffer(null);
 }

 /**
 * Test releaseNameCopyBuffer method
 */
 @Test(expected = IllegalArgumentException.class)
 public void testReleaseNameCopyBufferWithNullBuffer() {
 _context.releaseNameCopyBuffer(null);
 }
}