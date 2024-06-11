package net.sf.xbus.base.core.reflection.test;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Vector;

import net.sf.xbus.base.core.Constants;
import net.sf.xbus.base.core.trace.Trace;

import org.junit.Test;

public class XBUSClassLoaderTest {

	@Test
	public void testGetInstance() {
		ClassLoader parent = this.getClass().getClassLoader();
		XBUSClassLoader loadedClassLoader = XBUSClassLoader.getInstance(parent);
		assertNotNull(loadedClassLoader);
	}

	@Test
	public void testCreateClassLoader() {
		ClassLoader parent = this.getClass().getClassLoader();
		XBUSClassLoader loadedClassLoader = XBUSClassLoader.createClassLoader(parent);
		assertNotNull(loadedClassLoader);

		// Check if the correct URLs are added to the classloader
		URL[] urls = loadedClassLoader.getURLs();
		Vector<String> expectedDirs = new Vector<String>();
		expectedDirs.add(Constants.XBUS_HOME + "/lib");
		expectedDirs.add(Constants.XBUS_HOME + "/lib/runtime");
		expectedDirs.add(Constants.XBUS_HOME + "/plugin/lib");
		expectedDirs.add(Constants.XBUS_HOME + "/test/lib");

		for (URL url : urls) {
			String urlString = url.toString();
			assertTrue(expectedDirs.contains(new File(urlString).getParentFile().getAbsolutePath()));
		}
	}

	@Test(expected = NullPointerException.class)
	public void testCreateClassLoaderNullParent() {
		XBUSClassLoader.createClassLoader(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateClassLoaderEmptyUrls() {
		ClassLoader parent = this.getClass().getClassLoader();
		Vector<URL> urls = new Vector<URL>();
		XBUSClassLoader.createClassLoader(new URL[0], parent);
	}

	@Test
	public void testAddUrls() {
		Vector<String> expectedDirs = new Vector<String>();
		expectedDirs.add(Constants.XBUS_HOME + "/lib");
		expectedDirs.add(Constants.XBUS_HOME + "/lib/runtime");
		expectedDirs.add(Constants.XBUS_HOME + "/plugin/lib");
		expectedDirs.add(Constants.XBUS_HOME + "/test/lib");

		Vector<URL> urls = new Vector<URL>();
		XBUSClassLoader.addUrls(urls, Constants.XBUS_HOME + "/");

		for (String dir : expectedDirs) {
			File libPath = new File(dir);
			File[] jars = libPath.listFiles();
			assertTrue(jars != null && jars.length > 0);

			for (File jar : jars) {
				if (jar.isFile()) {
					if (!jar.getName().endsWith("jar")) {
						continue;
					}
					URL url = jar.toURL();
					urls.add(url);
				}
			}
		}

		URL[] urlArray = new URL[urls.size()];
		for (int i = 0; i < urls.size(); ++i) {
			urlArray[i] = urls.elementAt(i);
		}

		XBUSClassLoader classLoader = new XBUSClassLoader(urlArray, null);
		assertEquals(urls.size(), classLoader.getURLs().length);
	}

	@Test(expected = MalformedURLException.class)
	public void testAddUrlsInvalidURL() {
		Vector<URL> urls = new Vector<URL>();
		XBUSClassLoader.addUrls(urls, "invalid_url");
	}

	// Add more test cases as needed
	// ...

	static {
		Constants.XBUS_HOME = System.getProperty("user.dir");
	}
}