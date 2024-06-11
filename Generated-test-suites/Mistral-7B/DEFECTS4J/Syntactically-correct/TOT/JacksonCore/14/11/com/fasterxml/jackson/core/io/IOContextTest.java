package com.fasterxml.jackson.core.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * IOContext test case.
 */
public class IOContextTest {
    private IOContext _context;

    @Before
    public void setUp() {
        _context = new IOContext(new BufferRecycler(), this, false);
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = _context.constructTextBuffer();
        assertNotNull(textBuffer);
        textBuffer.write("Hello World");
        assertArrayEquals("Hello World", textBuffer.getText());
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = _context.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        assertArrayEquals(new byte[0], readIOBuffer);

        // read some data into the buffer
        System.arraycopy("Hello World".getBytes(), 0, readIOBuffer, 0, "Hello World".length());

        // check data is written correctly
        String data = new String(readIOBuffer);
        assertArrayEquals("Hello World".getBytes(), readIOBuffer);
        assertArrayEquals("Hello World", data.getBytes());

        // release the buffer
        _context.releaseReadIOBuffer(readIOBuffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = _context.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        assertArrayEquals(new byte[0], writeEncodingBuffer);

        // write some data into the buffer
        _context.writeEncodingBuffer(writeEncodingBuffer, "Hello World".getBytes());

        // check data is written correctly
        assertArrayEquals("Hello World".getBytes(), writeEncodingBuffer);

        // release the buffer
        _context.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] base64Buffer = _context.allocBase64Buffer();
        assertNotNull(base64Buffer);
        assertArrayEquals(new byte[0], base64Buffer);

        // write some base64 data into the buffer
        _context.writeBase64Buffer(base64Buffer, "Hello World".getBytes());

        // check data is written correctly
        assertArrayEquals("SGVsbG8sIFdvcmxkIQ==".getBytes(), base64Buffer);

        // release the buffer
        _context.releaseBase64Buffer(base64Buffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocateTwice() {
        // allocate once
        byte[] readIOBuffer = _context.allocReadIOBuffer();

        // try to allocate again
        byte[] readIOBuffer2 = _context.allocReadIOBuffer();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWrongBuffer() {
        byte[] readIOBuffer = _context.allocReadIOBuffer();
        byte[] wrongBuffer = new byte[10];

        _context.releaseReadIOBuffer(wrongBuffer);
    }

    @After
    public void tearDown() {
        _context = null;
    }
}