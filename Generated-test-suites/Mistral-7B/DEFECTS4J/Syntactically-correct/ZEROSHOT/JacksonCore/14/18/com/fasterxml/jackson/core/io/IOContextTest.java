package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.JsonEncoding;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class IOContextTest {

 private static final int BUFFER_SIZE = 10;

 @Mock
 private BufferRecycler bufferRecycler;

 @InjectMocks
 private IOContext ioContext;

 @Before
 public void setUp() {
 ioContext = new IOContext(bufferRecycler, new Object(), true);
 }

 @Test
 public void shouldInitializeContextWithDefaultValues() {
 // Configuration
 assertNull(ioContext.getEncoding());
 assertFalse(ioContext.isResourceManaged());

 // Buffer handling, recycling
 assertNull(ioContext.getReadIOBuffer());
 assertNull(ioContext.getWriteEncodingBuffer());
 assertNull(ioContext.getBase64Buffer());
 assertNull(ioContext.getTokenCBuffer());
 assertNull(ioContext.getConcatCBuffer());
 assertNull(ioContext.getNameCopyBuffer());
 }

 @Test
 public void shouldAllocateAndReleaseReadIOBuffer() {
 byte[] readIOBuffer = ioContext.allocReadIOBuffer();
 byte[] expectedReadIOBuffer = bufferRecycler.getByteBuffer(BufferRecycler.BYTE_READ_IO_BUFFER);
 assertArrayEquals(expectedReadIOBuffer, readIOBuffer);
 ioContext.releaseReadIOBuffer(readIOBuffer);
 assertNull(ioContext.getReadIOBuffer());
 }

 @Test(expected = IllegalStateException.class)
 public void shouldThrowExceptionWhenAllocatingReadIOBufferTwice() {
 ioContext.allocReadIOBuffer();
 ioContext.allocReadIOBuffer();
 }

 // Similar test for writeEncodingBuffer, base64Buffer, tokenCBuffer, concatCBuffer, nameCopyBuffer

 @Test
 public void shouldSetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 ioContext.setEncoding(encoding);
 assertSame(encoding, ioContext.getEncoding());
 }

 @Test
 public void shouldSupportFluentSetEncoding() {
 JsonEncoding encoding = JsonEncoding.UTF8;
 ioContext = ioContext.withEncoding(encoding);
 assertSame(encoding, ioContext.getEncoding());
 }
}