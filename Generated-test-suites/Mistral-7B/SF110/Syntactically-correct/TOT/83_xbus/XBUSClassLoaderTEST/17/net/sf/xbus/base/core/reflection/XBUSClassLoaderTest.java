import org.junit.Test;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {
  
  @Test
  public void testGetInstance() {
    ClassLoader parent = XBUSClassLoader.class.getClassLoader();
    XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(parent);
    XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(parent);

    // Test that getInstance returns the same instance
    assertSame(classLoader1, classLoader2);
  }
  
}