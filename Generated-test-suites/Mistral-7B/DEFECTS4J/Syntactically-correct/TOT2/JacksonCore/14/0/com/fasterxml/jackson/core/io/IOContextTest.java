import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.io.IOContext;

public class IOContextTest {

    @Test
    public void testIOContextConstructor() {
        IOContext ioContext = new IOContext(null, null, false);
        assertNull(ioContext.getEncoding());
        assertFalse(ioContext.isResourceManaged());
        // Add edge cases for null arguments and illegal arguments for _bufferRecycler
    }

    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(null, null, false);
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
        ioContext.setEncoding(null);
        assertNull(ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext ioContext = new IOContext(null, null, false);
        IOContext newIOContext = ioContext.withEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, newIOContext.getEncoding());
        assertNotSame(ioContext, newIOContext);
    }

    // Add test cases for getSourceReference(), isResourceManaged(), constructTextBuffer(),
    // allocReadIOBuffer(), allocWriteEncodingBuffer(), allocBase64Buffer(), allocTokenBuffer(),
    // allocConcatBuffer(), and allocNameCopyBuffer() methods

    @Test
    public void testReleaseReadIOBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertNull(ioContext.getReadIOBuffer());
    }

    // Add test cases for releaseWriteEncodingBuffer(), releaseBase64Buffer(), releaseTokenBuffer(),
    // releaseConcatBuffer(), and releaseNameCopyBuffer() methods
}