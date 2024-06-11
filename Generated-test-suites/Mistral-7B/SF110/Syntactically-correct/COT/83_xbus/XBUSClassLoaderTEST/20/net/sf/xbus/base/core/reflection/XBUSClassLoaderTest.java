import org.junit.Test;
import org.junit.Assert.*;
import net.sf.xbus.base.core.Constants;
import java.io.File;
import java.net.URL;
import java.net.MalformedURLException;

public class XBUSClassLoaderTest {

 private XBUSClassLoader classLoader;

 @Test
 public void testGetInstance() {
 ClassLoader parent = this.getClass().getClassLoader();
 classLoader = XBUSClassLoader.getInstance(parent);
 Assert.assertNotNull(classLoader);
 }

 @Test
 public void testCreateClassLoader() {
 ClassLoader parent = this.getClass().getClassLoader();
 classLoader = XBUSClassLoader.createClassLoader(parent);
 Assert.assertNotNull(classLoader);
 }

 @Test
 public void testAddUrls() {
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/lib");
 Assert.assertEquals(4, urls.size());
}

 @Test
 public void testGetInstanceWithNullXBUS_HOME() {
 ClassLoader parent = this.getClass().getClassLoader();
 Constants.XBUS_HOME = null;
 classLoader = XBUSClassLoader.getInstance(parent);
 Assert.assertNull(classLoader);
 }

 @Test
 public void testCreateClassLoaderWithNullXBUS_HOME() {
 ClassLoader parent = this.getClass().getClassLoader();
 Constants.XBUS_HOME = null;
 classLoader = XBUSClassLoader.createClassLoader(parent);
 Assert.assertNull(classLoader);
 }

 @Test(expected = RuntimeException.class)
 public void testCreateClassLoaderWithInvalidURLs() {
 ClassLoader parent = this.getClass().getClassLoader();
 Constants.XBUS_HOME = "/invalid/path";
 XBUSClassLoader.mClassLoader = null;
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME);
 URL[] urlArray = new URL[urls.size()];
 for (int i = 0; i < urls.size(); ++i) {
 urlArray[i] = urls.elementAt(i);
 }
 XBUSClassLoader xbusClassLoader = new XBUSClassLoader(urlArray, parent);
}

 @Test(expected = MalformedURLException.class)
 public void testAddUrlsWithInvalidURL() {
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, "invalid:url");
}

 @Test
 public void testGetMClassLoader() {
 ClassLoader parent = this.getClass().getClassLoader();
 classLoader = XBUSClassLoader.getInstance(parent);
 XBUSClassLoader anotherClassLoader = XBUSClassLoader.getInstance(parent);
 Assert.assertSame(classLoader, anotherClassLoader);
 }

 @Test
 public void testPrivateConstructor() {
 Assert.assertThrows(IllegalAccessError.class, () -> new XBUSClassLoader(new URL[]{}, null));
 }

}