package net.sf.xbus.base.core.reflection;
import org.junit.Test;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {
    @Test
    public void testGetInstance() {
        ClassLoader parent = null;
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(xbusClassLoader);
    }
}