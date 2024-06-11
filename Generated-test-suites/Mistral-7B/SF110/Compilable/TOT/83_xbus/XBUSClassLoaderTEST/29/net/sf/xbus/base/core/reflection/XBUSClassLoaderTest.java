package net.sf.xbus.base.core.reflection;
import org.junit.Test;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {
    

    @Test
    public void testGetInstance() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(parent);
        XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(parent);
        assertEquals(classLoader1, classLoader2);
    }

    @Test(expected = Exception.class)
    public void testGetInstanceWithNullParent() {
        XBUSClassLoader.getInstance(null);
    }

    
}