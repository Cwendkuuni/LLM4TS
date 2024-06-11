import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class XBUSClassLoaderTest {
  @Test
  public void testGetInstance() {
    assertNotNull(XBUSClassLoader.getInstance(null));
  }
}