import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.XBUSClassLoader;

public class XBUSClassLoaderTest {
    
    
    @Test
    public void testGetInstance() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);
    }

    @Test(expected = NullPointerException.class)
    public void testGetInstanceWithNullParent() {
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
    }

    @Test
    public void testGetInstanceTwice() {
        ClassLoader parent = getClass().getClassLoader();
        XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(parent);
        XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(parent);
        assertSame(classLoader1, classLoader2);
    }

    
}