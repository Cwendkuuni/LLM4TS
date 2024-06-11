import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.io.IOContext;

public class IOContextTest {

    @Test
    public void testIOContextConstruction() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        assertNotNull(ioContext.getEncoding());
        assertNotNull(ioContext._managedResource);
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocReadIOBufferTwice() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer();
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocWriteEncodingBufferTwice() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.allocWriteEncodingBuffer();
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongBufferRelease() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseConcatBuffer(tokenBuffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);
        assertNotNull(nameCopyBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }

    @Test
    public void testReleaseAllBuffers() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        char[] concatBuffer = ioContext.allocConcatBuffer();
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(10);

        ioContext.releaseReadIOBuffer(readIOBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        ioContext.releaseTokenBuffer(tokenBuffer);
        ioContext.releaseConcatBuffer(concatBuffer);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
    }
}