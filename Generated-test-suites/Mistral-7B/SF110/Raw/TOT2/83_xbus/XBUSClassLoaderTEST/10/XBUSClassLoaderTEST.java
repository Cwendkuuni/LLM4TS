import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.reflection.XBUSClassLoader;

public class XBUSClassLoaderTest {

    @Test
    public void testGetInstance() {
        ClassLoader parent = XBUSClassLoader.class.getClassLoader();
        XBUSClassLoader loader = XBUSClassLoader.getInstance(parent);
        assertNotNull(loader);
    }

    @Test
    public void testCreateClassLoader() {
        ClassLoader parent = null; // Test null parent
        try {
            XBUSClassLoader loader = XBUSClassLoader.createClassLoader(parent);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        ClassLoader parent2 = XBUSClassLoader.class.getClassLoader();
        XBUSClassLoader loader2 = XBUSClassLoader.createClassLoader(parent2);
        assertNotNull(loader2);
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        String dirName = "test/resources";
        XBUSClassLoader.addUrls(urls, dirName);
        assertEquals(1, urls.size()); // Assuming there is one URL in the test/resources directory

        String invalidDirName = "test/invalid";
        XBUSClassLoader.addUrls(urls, invalidDirName);
        assertEquals(1, urls.size()); // The method should not add any URLs when the directory is not valid

        File invalidFile = new File("test/TestInvalidFile.txt");
        String invalidFileName = invalidFile.getAbsolutePath();
        try {
            XBUSClassLoader.addUrls(urls, invalidFileName);
            fail("Expected MalformedURLException or IOException");
        } catch (MalformedURLException | IOException e) {
            // Expected
        }

        File invalidJar = new File("test/TestInvalidJar.jar");
        String invalidJarName = invalidJar.getAbsolutePath();
        try {
            XBUSClassLoader.addUrls(urls, invalidJarName);
            fail("Expected MalformedURLException or IOException");
        } catch (MalformedURLException | IOException e) {
            // Expected
        }
    }

    @Test
    public void testStaticBlock() {
        assertNull(XBUSClassLoader.mClassLoader);

        System.setProperty("Constants.XBUS_HOME", "test/xbus_home");
        XBUSClassLoader loader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
        assertNotNull(loader);
    }

    @Test(expected = IllegalStateException.class)
    public void testStaticBlockWithInitializationError() {
        System.setProperty("Constants.XBUS_HOME", "test/xbus_home");
        System.setProperty("Constants.XBUS_HOME_INITIALIZATION_ERROR", "true");
        XBUSClassLoader loader = XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader());
        fail("Expected IllegalStateException");
    }
}