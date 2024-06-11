import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.io.IOContext;

public class IOContextTest {

    @Test
    public void testIOContextConstruction() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, false);
        assertNotNull(ioContext);
    }

    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, false);
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocReadIOBufferTwice() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer(); // Should throw an exception
    }
}