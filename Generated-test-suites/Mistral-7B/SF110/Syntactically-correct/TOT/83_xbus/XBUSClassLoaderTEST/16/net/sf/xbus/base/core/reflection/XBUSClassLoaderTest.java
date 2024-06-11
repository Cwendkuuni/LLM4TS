import org.junit.Test;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {
    

    @Test
    public void testXBUSClassLoaderConstructor() {
        URL[] validUrls = new URL[] { new File("path/to/valid/url1").toURL(), new File("path/to/valid/url2").toURL() };
        XBUSClassLoader classLoader = new XBUSClassLoader(validUrls, ClassLoader.getSystemClassLoader());
        assertNotNull(classLoader);

        URL[] invalidUrls = new URL[] { new File("path/to/invalid/url1").toURL(), new File("path/to/invalid/url2").toURL() };
        try {
            XBUSClassLoader invalidClassLoader = new XBUSClassLoader(invalidUrls, ClassLoader.getSystemClassLoader());
            fail("Expected MalformedURLException but no exception was thrown.");
        } catch (MalformedURLException e) {
            // Expected
        }
    }

    
}