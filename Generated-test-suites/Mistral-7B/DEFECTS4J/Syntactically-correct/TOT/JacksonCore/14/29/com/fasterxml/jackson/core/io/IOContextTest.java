package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.JsonEncoding;
import org.junit.Assert;
import org.junit.Test;

public class IOContextTest {

    @Test
    public void testIOContextCreation() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);

        Assert.assertEquals(sourceRef, ioContext.getSourceReference());
        Assert.assertNull(ioContext.getEncoding());
        Assert.assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testAllocReadIOBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);

        byte[] readBuffer = ioContext.allocReadIOBuffer();
        Assert.assertNotNull(readBuffer);
        ioContext.releaseReadIOBuffer(readBuffer);

        // Test allocation with minimum size
        int minSize = 10;
        byte[] readBufferMinSize = ioContext.allocReadIOBuffer(minSize);
        Assert.assertNotNull(readBufferMinSize);
        Assert.assertEquals(minSize, readBufferMinSize.length);
        ioContext.releaseReadIOBuffer(readBufferMinSize);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);

        byte[] writeBuffer = ioContext.allocWriteEncodingBuffer();
        Assert.assertNotNull(writeBuffer);
        ioContext.releaseWriteEncodingBuffer(writeBuffer);

        // Test allocation with minimum size
        int minSize = 20;
        byte[] writeBufferMinSize = ioContext.allocWriteEncodingBuffer(minSize);
        Assert.assertNotNull(writeBufferMinSize);
        Assert.assertEquals(minSize, writeBufferMinSize.length);
        ioContext.releaseWriteEncodingBuffer(writeBufferMinSize);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocateSameBufferTwice() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);

        byte[] readBuffer = ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWrongBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);

        byte[] wrongBuffer = new byte[10];
        ioContext.releaseReadIOBuffer(wrongBuffer);
    }
}