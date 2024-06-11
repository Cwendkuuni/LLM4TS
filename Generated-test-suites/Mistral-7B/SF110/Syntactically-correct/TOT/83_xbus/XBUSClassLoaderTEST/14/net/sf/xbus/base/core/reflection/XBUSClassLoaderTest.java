import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class XBUSClassLoaderTest {
    
    private static ClassLoader parent;
    static XBUSClassLoader classUnderTest;

    @BeforeClass
    public static void setup() {
        parent = XBUSClassLoader.class.getClassLoader();
    }

    @Test
    public void testInstance() {
        classUnderTest = XBUSClassLoader.getInstance(parent);
        assertNotNull(classUnderTest);
    }

    
}