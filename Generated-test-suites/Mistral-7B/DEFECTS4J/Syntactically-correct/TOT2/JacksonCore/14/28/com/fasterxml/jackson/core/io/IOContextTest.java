
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.io.BufferRecycler;
import com.fasterxml.jackson.core.io.IOContext;

public class IOContextTest {

    @Test
    public void testIOContextConstructor() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        assertNotNull(ioContext);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIOContextConstructorWithNullBufferRecycler() {
        Object sourceRef = new Object();
        boolean managedResource = true;
        new IOContext(null, sourceRef, managedResource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIOContextConstructorWithNullSourceRef() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        boolean managedResource = true;
        new IOContext(bufferRecycler, null, managedResource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIOContextConstructorWithNullManagedResource() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        new IOContext(bufferRecycler, sourceRef, null);
    }

    @Test
    public void testIOContextConstructorWithInvalidSourceRefType() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        int sourceRef = 42;
        boolean managedResource = true;
        try {
            new IOContext(bufferRecycler, sourceRef, managedResource);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIOContextConstructorWithManagedResourceButCannotManage() {
        BufferRecycler bufferRecycler = new BufferRecycler() {
            @Override
            public void releaseByteBuffer(int bufferType, byte[] buffer) {
                fail("Should not be called");
            }
        };
        Object sourceRef = new Object();
        boolean managedResource = true;
        try {
            new IOContext(bufferRecycler, sourceRef, managedResource);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}