import org.junit.Assert;
import org.junit.Test;
import net.sf.xbus.base.core.reflection.XBUSClassLoader;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        ClassLoader parent = new URLClassLoader(new URL[]{}, null);
        XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(parent);
        XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(parent);

        Assert.assertSame("Both instances should be the same", classLoader1, classLoader2);
    }
    
}