package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.JsonEncoding;
import org.junit.Assert;
import org.junit.Test;

public class IOContextTest {

    @Test
    public void testIOContextConstruction() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);

        Assert.assertSame(sourceRef, ioContext.getSourceReference());
        Assert.assertNull(ioContext.getEncoding());
        Assert.assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testIOContextWithEncoding() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        JsonEncoding encoding = JsonEncoding.UTF8;

        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        ioContext = ioContext.withEncoding(encoding);

        Assert.assertSame(sourceRef, ioContext.getSourceReference());
        Assert.assertEquals(encoding, ioContext.getEncoding());
        Assert.assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testAllocReadIOBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);

        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        Assert.assertNotNull(readIOBuffer);
        Assert.assertSame(readIOBuffer, ioContext.getReadIOBuffer());
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);

        int minSize = 10;
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
        Assert.assertNotNull(readIOBuffer);
        Assert.assertArrayEquals(new byte[minSize], readIOBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocReadIOBufferTwice() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);

        byte[] readIOBuffer1 = ioContext.allocReadIOBuffer();
        byte[] readIOBuffer2 = ioContext.allocReadIOBuffer();
    }

    @Test
    public void testReleaseReadIOBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);

        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);

        Assert.assertNull(ioContext.getReadIOBuffer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWrongReadIOBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);

        byte[] readIOBuffer1 = ioContext.allocReadIOBuffer();
        byte[] readIOBuffer2 = new byte[10];

        ioContext.releaseReadIOBuffer(readIOBuffer2);
    }

    // Add more test cases for other methods in IOContext
}