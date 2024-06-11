import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class XBUSClassLoaderTest {
    

    @Test
    public void testXBUSClassLoaderInstance() {
        URL[] urlArray = new URL[] { new File("path/to/xbus-home/lib").toURL(), new File("path/to/xbus-home").toURL(), new File("path/to/xbus-home/plugin/lib").toURL(), new File("path/to/xbus-home/test/lib").toURL() };
        XBUSClassLoader classLoader = new XBUSClassLoader(urlArray, this.getClass().getClassLoader());
        assertNotNull(classLoader);
    }

    
}