// XBUSClassLoaderTest1.java
package net.sf.xbus.base.core.reflection;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest1 {

    private static XBUSClassLoader classLoader;

    @BeforeClass
    public static void setup() {
        classLoader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        String dirName = "test/lib"; // Replace with a valid directory path
        XBUSClassLoader.addUrls(urls, dirName);
        URL[] expectedUrls = new URL[] { new File("test/lib/expectedJar1.jar").toURL(), new File("test/lib/expectedJar2.jar").toURL() };
        URL[] actualUrls = new URL[urls.size()];
        for (int i = 0; i < urls.size(); ++i) {
            actualUrls[i] = urls.elementAt(i);
        }
        assertArrayEquals(expectedUrls, actualUrls);
    }
}