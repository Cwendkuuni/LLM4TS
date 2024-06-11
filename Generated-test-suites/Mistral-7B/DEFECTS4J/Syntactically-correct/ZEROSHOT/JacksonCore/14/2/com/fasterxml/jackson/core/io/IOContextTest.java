package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.BufferRecycler;

/**
 * Test class for IOContext
 */
public class IOContextTest {

    @Test
    public void testIOContext() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "sourceRef", true);

        assertNotNull(ioContext);
        assertSame(bufferRecycler, ioContext._bufferRecycler);
        assertNull(ioContext._sourceRef);
        assertTrue(ioContext.isResourceManaged());

        assertNull(ioContext.getEncoding());

        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());

        ioContext = ioContext.withEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());

        assertNull(ioContext.getSourceReference());
        assertFalse(ioContext.isResourceManaged());

        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);

        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);

        byte[] readIOBuffer2 = ioContext.allocReadIOBuffer();
        assertNull(readIOBuffer2); // Should not be able to allocate twice

        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);

        byte[] writeEncodingBuffer2 = ioContext.allocWriteEncodingBuffer();
        assertNull(writeEncodingBuffer2); // Should not be able to allocate twice

        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);

        char[] tokenBuffer2 = ioContext.allocTokenBuffer();
        assertNull(tokenBuffer2); // Should not be able to allocate twice

        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);

        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        assertNotNull(nameCopyBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);

        ioContext.releaseReadIOBuffer(readIOBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }
}