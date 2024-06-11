package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;

import net.sf.xbus.base.core.Constants;

import org.junit.Test;

public class XBUSClassLoaderTest {

	@Test
	public void testGetInstance() {
		XBUSClassLoader classLoader1 = XBUSClassLoader.getInstance(URLClassLoader.class.getInstance());
		XBUSClassLoader classLoader2 = XBUSClassLoader.getInstance(URLClassLoader.class.getInstance());
		assertEquals(classLoader1, classLoader2);
	}

	@Test
	public void testCreateClassLoaderWithNullParent() {
		assertNull(XBUSClassLoader.getInstance(null));
	}

	@Test
	public void testCreateClassLoaderWithEmptyUrls() {
		Vector urls = new Vector();
		XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(URLClassLoader.class.getInstance());
		assertNull(classLoader);
	}

	@Test
	public void testCreateClassLoaderWithValidUrls() {
		String xbusHome = System.getProperty("user.dir");
		Vector urls = new Vector();
		addUrls(urls, xbusHome + "/src/test/java/net/sf/xbus/base/core/reflection");
		XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(URLClassLoader.class.getInstance());
		assertNotNull(classLoader);
		assertEquals(urls.size(), classLoader.getURLs().length);
	}

	@Test
	public void testAddUrls() {
		String xbusHome = System.getProperty("user.dir");
		Vector urls = new Vector();
		addUrls(urls, xbusHome + "/src/test/java/net/sf/xbus/base/core/reflection");
		assertEquals(1, urls.size());
		URL url = urls.elementAt(0);
		File file = new File(url.getFile());
		assertTrue(file.exists());
	}

}