package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class IOContextTest {

    private IOContext _context;
    private BufferRecycler _bufferRecycler;

    @Before
    public void setUp() {
        _bufferRecycler = new BufferRecycler();
        _context = new IOContext(_bufferRecycler, this, true);
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = _context.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        assertNull(_context._readIOBuffer);

        byte[] input = new byte[]{0x1, 0x2, 0x3};
        System.arraycopy(input, 0, readIOBuffer, 0, input.length);

        _context.releaseReadIOBuffer(readIOBuffer);
        assertNull(_context._readIOBuffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = _context.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        assertNull(_context._writeEncodingBuffer);

        byte[] output = new byte[]{0x1, 0x2, 0x3};
        _context.releaseWriteEncodingBuffer(output);
        assertNull(_context._writeEncodingBuffer);
    }

    @Test
    public void testSetEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        _context.setEncoding(encoding);
        assertNotNull(_context._encoding);
        assertSame(encoding, _context._encoding);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetEncodingTwice() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        _context.setEncoding(encoding);
        _context.setEncoding(encoding);
    }

    @Test
    public void testIsResourceManaged() {
        assertTrue(_context.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = _context.constructTextBuffer();
        assertNotNull(textBuffer);
    }
}