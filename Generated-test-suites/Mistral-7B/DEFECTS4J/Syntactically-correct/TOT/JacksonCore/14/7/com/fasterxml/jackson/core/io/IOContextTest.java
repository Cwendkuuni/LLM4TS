package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test for {@link IOContext} class.
 */
public class IOContextTest {

    private BufferRecycler _bufferRecycler;
    private IOContext _context;

    @Before
    public void setUp() {
        _bufferRecycler = new BufferRecycler();
        _context = new IOContext(_bufferRecycler, this, true);
    }

    @After
    public void tearDown() {
        if (_context != null) {
            _context.releaseReadIOBuffer(_context.allocReadIOBuffer());
            _context.releaseWriteEncodingBuffer(_context.allocWriteEncodingBuffer());
            _context = null;
        }
        if (_bufferRecycler != null) {
            _bufferRecycler.clear();
            _bufferRecycler = null;
        }
    }

    @Test
    public void testInitialization() {
        assertNotNull(_context.getSourceReference());
        assertNull(_context.getEncoding());
        assertTrue(_context.isResourceManaged());
    }

    @Test
    public void testBufferAllocation() {
        byte[] readIOBuffer = _context.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        assertArrayEquals(new byte[0], readIOBuffer);

        byte[] writeEncodingBuffer = _context.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        assertArrayEquals(new byte[0], writeEncodingBuffer);

        char[] tokenBuffer = _context.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        assertArrayEquals(new char[0], tokenBuffer);

        char[] concatBuffer = _context.allocConcatBuffer();
        assertNotNull(concatBuffer);
        assertArrayEquals(new char[0], concatBuffer);

        char[] nameCopyBuffer = _context.allocNameCopyBuffer(10);
        assertNotNull(nameCopyBuffer);
        assertArrayEquals(new char[10], nameCopyBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testDoubleBufferAllocation() {
        _context.allocReadIOBuffer();
        _context.allocReadIOBuffer();
    }

    @Test
    public void testBufferRelease() {
        byte[] readIOBuffer = _context.allocReadIOBuffer();
        byte[] writeEncodingBuffer = _context.allocWriteEncodingBuffer();

        _context.releaseReadIOBuffer(readIOBuffer);
        _context.releaseWriteEncodingBuffer(writeEncodingBuffer);

        assertNull(_context.getReadIOBuffer());
        assertNull(_context.getWriteEncodingBuffer());
    }

    @Test
    public void testSetEncoding() {
        _context.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, _context.getEncoding());
    }

    private byte[] getReadIOBuffer() {
        return _context.allocReadIOBuffer();
    }

    private byte[] getWriteEncodingBuffer() {
        return _context.allocWriteEncodingBuffer();
    }

    private JsonEncoding getEncoding() {
        return _context.getEncoding();
    }

    private boolean isManagedResource() {
        return _context.isResourceManaged();
    }

    private static JsonEncoding getJsonEncoding(String encodingName) {
        for (JsonEncoding encoding : JsonEncoding.values()) {
            if (encoding.name().equalsIgnoreCase(encodingName)) {
                return encoding;
            }
        }
        return null;
    }
}