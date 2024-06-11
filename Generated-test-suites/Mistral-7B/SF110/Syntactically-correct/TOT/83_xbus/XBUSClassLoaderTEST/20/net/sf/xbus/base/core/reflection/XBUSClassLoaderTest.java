import org.junit.Assert;
import org.junit.Test;
import net.sf.xbus.base.core.reflection.XBUSClassLoader;
import net.sf.xbus.base.core.Constants;

public class XBUSClassLoaderTest {
    
    @Test
    public void testGetInstance() {
        Assert.assertNotNull(XBUSClassLoader.getInstance(null)); // Test null parent
        Assert.assertNotNull(XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader())); // Test valid parent
        Assert.assertNotSame(XBUSClassLoader.class.getClassLoader(), XBUSClassLoader.getInstance(XBUSClassLoader.class.getClassLoader())); // Test new instance
    }
    
}