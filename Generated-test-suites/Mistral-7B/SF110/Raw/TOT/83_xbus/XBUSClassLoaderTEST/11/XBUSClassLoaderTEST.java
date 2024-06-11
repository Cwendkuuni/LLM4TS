import org.junit.Assert;
import org.junit.Test;

public class XBUSClassLoaderTest {
    

    @Test
    public void testConstructor() throws MalformedURLException {
        final URL[] urlArray = new URL[] {new File("path/to/xbus/home/lib").toURL(), new File("path/to/parent").toURL()};
        XBUSClassLoader classLoader = new XBUSClassLoader(urlArray, null);
        Assert.assertNotNull(classLoader);
        
    }
}