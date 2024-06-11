import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.net.URL;
import java.lang.reflect.Method;

public class XBUSClassLoaderTest {

  @Test
  public void testInstance() {
    XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
    Assert.assertNotNull(classLoader);
  }

  @Test(expected = NullPointerException.class)
  public void testInstanceNullParent() {
    XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
  }

  @Test
  public void testCreateClassLoader() throws Exception {
    Constructor constructor = XBUSClassLoader.class.getDeclaredConstructor(ClassLoader.class);
    constructor.setAccessible(true);
    XBUSClassLoader classLoader = (XBUSClassLoader) constructor.newInstance(null);
    Assert.assertNotNull(classLoader);
  }

  @Test(expected = NullPointerException.class)
  public void testCreateClassLoaderNullParent() throws Exception {
    Constructor constructor = XBUSClassLoader.class.getDeclaredConstructor(ClassLoader.class);
    constructor.setAccessible(true);
    XBUSClassLoader classLoader = (XBUSClassLoader) constructor.newInstance(null);
  }

  @Test
  public void testAddUrls() throws Exception {
    Method method = XBUSClassLoader.class.getDeclaredMethod("addUrls", Vector.class, String.class);
    method.setAccessible(true);
    XBUSClassLoader classLoader = new XBUSClassLoader(new URL[0], null);
    Vector urls = new Vector();
    String dirName = "test/lib";
    method.invoke(classLoader, urls, dirName);
    URL[] urlArray = (URL[]) urls.toArray(new URL[0]);
    Assert.assertArrayEquals(new URL[]{new File("test/lib/lib.jar").toURL()}, urlArray);
  }

  @Test
  public void testAddNullURLs() {
    XBUSClassLoader classLoader = new XBUSClassLoader(new URL[0], null);
    Vector urls = null;
    XBUSClassLoader.addUrls(urls, "test/lib");
    Assert.assertTrue(urls.isEmpty());
  }

  @Test
  public void testAddEmptyStringDirName() {
    XBUSClassLoader classLoader = new XBUSClassLoader(new URL[0], null);
    Vector urls = new Vector();
    String dirName = "";
    XBUSClassLoader.addUrls(urls, dirName);
    URL[] urlArray = (URL[]) urls.toArray(new URL[0]);
    Assert.assertTrue(urlArray.length == 0);
  }

  @Test
  public void testAddWrongFileExtension() {
    XBUSClassLoader classLoader = new XBUSClassLoader(new URL[0], null);
    Vector urls = new Vector();
    String dirName = "test/lib/file.txt";
    XBUSClassLoader.addUrls(urls, dirName);
    URL[] urlArray = (URL[]) urls.toArray(new URL[0]);
    Assert.assertTrue(urlArray.length == 0);
  }

  @Test
  public void testAddUnreachableFile() {
    XBUSClassLoader classLoader = new XBUSClassLoader(new URL[0], null);
    Vector urls = new Vector();
    String dirName = "unreachable/file.jar";
    XBUSClassLoader.addUrls(urls, dirName);
    URL[] urlArray = (URL[]) urls.toArray(new URL[0]);
    Assert.assertTrue(urlArray.length == 0);
  }

  @Test
  public void testAddLargeFile() {
    XBUSClassLoader classLoader = new XBUSClassLoader(new URL[0], null);
    Vector urls = new Vector();
    String dirName = "test/lib/largeFile.jar";
    XBUSClassLoader.addUrls(urls, dirName);
    URL[] urlArray = (URL[]) urls.toArray(new URL[0]);
    Assert.assertTrue(urlArray.length == 0);
  }

  @Test(expected = IOException.class)
  public void testAddFileCannotRead() throws Exception {
    XBUSClassLoader classLoader = new XBUSClassLoader(new URL[0], null);
    Vector urls = new Vector();
    String dirName = "test/lib/cannotReadFile.txt";
    XBUSClassLoader.addUrls(urls, dirName);
  }

  @Test
  public void testGetInstanceWithParent() {
    XBUSClassLoader parent = new XBUSClassLoader(new URL[0], null);
    XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
    Assert.assertNotNull(classLoader);
  }
}