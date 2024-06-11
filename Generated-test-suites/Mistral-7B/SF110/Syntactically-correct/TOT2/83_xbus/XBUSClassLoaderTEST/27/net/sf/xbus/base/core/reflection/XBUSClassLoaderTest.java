import org.junit.Test;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {
    @Test
    public void testgetInstance() {
        ClassLoader parent = new URLClassLoader(new URL[] {}, null);
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);
    }

    // Edge case: Parent class loader is null
    @Test(expected = IllegalArgumentException.class)
    public void testgetInstanceWithNullParent() {
        XBUSClassLoader.getInstance(null);
    }
}