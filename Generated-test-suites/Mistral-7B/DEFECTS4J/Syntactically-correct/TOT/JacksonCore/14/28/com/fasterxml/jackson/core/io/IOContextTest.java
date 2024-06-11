package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class IOContextTest {

    private BufferRecycler bufferRecycler;
    private IOContext ioContext;

    @Before
    public void setUp() {
        bufferRecycler = new BufferRecycler();
        ioContext = new IOContext(bufferRecycler, "sourceRef", true);
    }

    @Test
    public void testConstructor() {
        assertNotNull(ioContext);
        assertEquals("sourceRef", ioContext.getSourceReference());
        assertTrue(ioContext.isResourceManaged());
        assertNull(ioContext.getEncoding());
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
        IOContext ioContextWithEncoding = ioContext.withEncoding(encoding);
        assertEquals(encoding, ioContextWithEncoding.getEncoding());
        assertEquals(ioContext, ioContextWithEncoding);
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        assertEquals(BufferRecycler.BYTE_READ_IO_BUFFER, readIOBuffer.length);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        assertEquals(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER, writeEncodingBuffer.length);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testDoubleAllocReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer();
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertNull(ioContext.getReadIOBuffer());
    }

    @Test
    public void testGetSourceReference() {
        assertEquals("sourceRef", ioContext.getSourceReference());
    }

    @Test
    public void testGetEncoding() {
        assertNull(ioContext.getEncoding());
    }

    @Test
    public void testIsResourceManaged() {
        assertTrue(ioContext.isResourceManaged());
    }

    @After
    public void tearDown() {
        if (ioContext != null) {
            ioContext.releaseAllBuffers();
        }
        bufferRecycler = null;
    }
}