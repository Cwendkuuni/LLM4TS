package test;

import net.sf.xbus.base.core.reflection.XBUSClassLoader;
import org.junit.Test;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {
    @Test
    public void testGetInstance() {
        ClassLoader parent = new URLClassLoader(new URL[]{}, null);
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);
    }

    @Test
    public void testGetInstance_NullParent() {
        assertThrows(NullPointerException.class, () -> XBUSClassLoader.getInstance(null));
    }

    @Test
    public void testCreateClassLoader_EmptyUrlArray() {
        ClassLoader parent = new URLClassLoader(new URL[]{}, null);
        assertThrows(IllegalArgumentException.class, () -> XBUSClassLoader.createClassLoader(parent, new URL[0]));
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = new URLClassLoader(new URL[]{}, null);
        XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(classLoader);
    }
}