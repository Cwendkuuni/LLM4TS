import org.junit.Test;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {

    @Test
    public void testInstance() {
        // Test with valid ClassLoader
        ClassLoader parent = this.getClass().getClassLoader();
        XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
        assertNotNull(classLoader);

        // Test with null ClassLoader
        ClassLoader nullParent = null;
        XBUSClassLoader nullClassLoader = XBUSClassLoader.getInstance(nullParent);
        assertNull(nullClassLoader);

        // Test with empty URL array
        URL[] emptyUrlArray = new URL[0];
        XBUSClassLoader emptyClassLoader = new XBUSClassLoader(emptyUrlArray, parent);
        XBUSClassLoader emptyInstance = XBUSClassLoader.getInstance(emptyClassLoader);
        assertSame(emptyClassLoader, emptyInstance);

        // Test with invalid URLs
        Vector urls = new Vector();
        urls.add(new URL("invalid://url"));
        XBUSClassLoader invalidClassLoader = new XBUSClassLoader(urls.toArray(new URL[0]), parent);
        XBUSClassLoader invalidInstance = XBUSClassLoader.getInstance(invalidClassLoader);
        assertSame(invalidClassLoader, invalidInstance);

        // Test with existing URLs
        Vector validUrls = new Vector();
        addUrls(validUrls, "valid_path/lib");
        XBUSClassLoader validClassLoader = new XBUSClassLoader(validUrls.toArray(new URL[0]), parent);
        XBUSClassLoader validInstance = XBUSClassLoader.getInstance(validClassLoader);
        assertNotSame(parent, validInstance);
    }
}