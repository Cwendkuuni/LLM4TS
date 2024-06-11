package net.sf.xbus.base.core.reflection;

import org.junit.Assert;
import org.junit.Test;

public class XBUSClassLoaderTest {
    @Test
    public void testGetInstance() {
        ClassLoader parent = this.getClass().getClassLoader();
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.getInstance(parent);
        Assert.assertNotNull(xbusClassLoader);
    }

    @Test(expected = RuntimeException.class)
    public void testGetInstanceWithError() {
        ClassLoader parent = null;
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.getInstance(parent);
    }
}