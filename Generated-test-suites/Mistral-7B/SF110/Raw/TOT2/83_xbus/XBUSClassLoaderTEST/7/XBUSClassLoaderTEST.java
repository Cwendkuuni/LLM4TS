package jointbase;

import net.sf.xbus.base.core.Constants;
import net.sf.xbus.base.core.trace.Trace;
import org.junit.Test;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {
    @Test
    public void testGetInstance() {
        ClassLoader parent = new URLClassLoader(new URL[]{}, null); // empty class loader for testing
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(xbusClassLoader);
        assertNotSame(parent, xbusClassLoader);
    }

    // Error scenario testing: XBUS_HOME not set
    @Test(expected = RuntimeException.class)
    public void testGetInstanceXBUSHomeNotSet() {
        Constants.XBUS_HOME = null;
        XBUSClassLoader.getInstance(new URLClassLoader(new URL[]{}, null));
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = new URLClassLoader(new URL[]{}, null); // empty class loader for testing
        XBUSClassLoader xbusClassLoader = XBUSClassLoader.createClassLoader(parent);
        assertNotNull(xbusClassLoader);
        assertNotSame(parent, xbusClassLoader);
    }

    // Error scenario testing: Constants.XBUS_HOME not set
    @Test(expected = RuntimeException.class)
    public void testCreateClassLoaderXBUSHomeNotSet() {
        Constants.XBUS_HOME = null;
        XBUSClassLoader.createClassLoader(new URLClassLoader(new URL[]{}, null));
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector<>();
        String dirName = "/test/lib";
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(1, urls.size());
        assertNotNull(urls.elementAt(0));
    }

    // Edge case testing: null urls
    @Test(expected = NullPointerException.class)
    public void testAddUrlsNullUrls() {
        XBUSClassLoader.addUrls(null, "test/lib");
    }

    // Edge case testing: empty dirName
    @Test
    public void testAddUrlsEmptyDirName() {
        Vector urls = new Vector<>();
        String dirName = "";
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(0, urls.size());
    }
}