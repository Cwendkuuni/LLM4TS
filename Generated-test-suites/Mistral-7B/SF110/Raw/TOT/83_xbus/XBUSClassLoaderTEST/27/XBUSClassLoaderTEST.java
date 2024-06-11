import org.junit.Assert;
import org.junit.Test;
import net.sf.xbus.base.core.Constants;
import net.sf.xbus.base.core.reflection.XBUSClassLoader;

public class XBUSClassLoaderTest {
    

    @Test
    public void testGetInstance() {
        // Given
        ClassLoader parent = this.getClass().getClassLoader();

        // When
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);

        // Then
        Assert.assertNotNull(classLoader);
        Assert.assertTrue(classLoader instanceof XBUSClassLoader);

        // Verifying that the URLs have been added correctly
        URL[] urls = classLoader.getURLs();
        int expectedUrlCount = 4; // Constants.XBUS_HOME/lib, Constants.XBUS_HOME/lib/runtime, Constants.XBUS_HOME/plugin/lib, Constants.XBUS_HOME/test/lib
        Assert.assertEquals(expectedUrlCount, urls.length);

        for (URL url : urls) {
            Assert.assertNotNull(url);
            String urlString = url.toString();
            if (urlString.startsWith(Constants.XBUS_HOME + "/lib")) {
                Assert.assertTrue(urlString.endsWith("/lib"));
            } else if (urlString.startsWith(Constants.XBUS_HOME + "/lib/runtime")) {
                Assert.assertTrue(urlString.endsWith("/lib/runtime"));
            } else if (urlString.startsWith(Constants.XBUS_HOME + "/plugin/lib")) {
                Assert.assertTrue(urlString.endsWith("/plugin/lib"));
            } else if (urlString.startsWith(Constants.XBUS_HOME + "/test/lib")) {
                Assert.assertTrue(urlString.endsWith("/test/lib"));
            }
        }
    }

    
}