package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.junit.Test;

public class XBUSClassLoaderTest {

	@Test
	public void testGetInstance() {
		XBUSClassLoader classLoader = XBUSClassLoader.getInstance(null);
		assertNotNull(classLoader);
	}

	@Test
	public void testCreateClassLoader() {
		XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(null);
		assertNotNull(classLoader);
	}

	@Test
	public void testAddUrls() {
		Vector<URL> urls = new Vector<URL>();
		String dirName = "testDirectory";
		XBUSClassLoader.addUrls(urls, dirName);
		assertFalse(urls.isEmpty());

		// Test with an invalid directory
		dirName = "nonExistentDirectory";
		XBUSClassLoader.addUrls(urls, dirName);
		assertTrue(urls.isEmpty());
	}

	@Test
	public void testConstructor() throws MalformedURLException {
		String dirName = "testDirectory";
		URL[] urlArray = new URL[3];
		Vector<URL> urls = new Vector<URL>();
		addUrls(urls, dirName);
		for (int i = 0; i < urls.size(); ++i) {
			urlArray[i] = urls.elementAt(i);
		}
		XBUSClassLoader classLoader = new XBUSClassLoader(urlArray, null);
		assertNotNull(classLoader);
	}

	private void addUrls(Vector<URL> urls, String dirName) {
		File libPath = new File(dirName);
		File[] jars = libPath.listFiles();
		for (File jar : jars) {
			if (jar != null) {
				String fileName = jar.getName();
				if (fileName.endsWith("jar") || fileName.endsWith("zip")) {
					try {
						urls.add(jar.toURL());
					} catch (MalformedURLException e) {
						fail("Failed to create URL for file: " + jar.getAbsolutePath());
					}
				}
			}
		}
	}

}