package net.sf.xbus.base.core.reflection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.*;
import java.util.*;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class XBUSClassLoaderTest {

 private XBUSClassLoader mClassLoader;
 private static final String XBUS_HOME = "test/xbus_home";
 private static final String LIB_DIR = XBUS_HOME + "/lib";
 private static final String RUNTIME_DIR = XBUS_HOME + "/lib/runtime";
 private static final String PLUGIN_DIR = XBUS_HOME + "/plugin/lib";
 private static final String TEST_DIR = XBUS_HOME + "/test/lib";
 private static final String[] TEST_JARS = {"test1.jar", "test2.jar"};
 private static final URL[] TEST_URLS = new URL[TEST_JARS.length];

 @Before
 public void setUp() throws Exception {
 MockitoAnnotations.initMocks(this);
 mClassLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
 when(Constants.XBUS_HOME).thenReturn(XBUS_HOME);
 }

 @Test
 public void testInstance() {
 XBUSClassLoader classLoader = XBUSClassLoader.getInstance(getClass().getClassLoader());
 assertEquals(getClass().getClassLoader(), classLoader.getParent());
 }

 @Test
 public void testCreateClassLoader() throws Exception {
 XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(getClass().getClassLoader());
 assertNotNull(classLoader);
 assertEquals(getClass().getClassLoader(), classLoader.getParent());

 File libPath = mock(File.class);
 when(libPath.listFiles()).thenReturn(TEST_JARS);
 when(libPath.toURL()).thenAnswer(invocation -> new URL(invocation.getArgument(0) + File.separator + TEST_JARS[0]));

 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, LIB_DIR);
 assertEquals(TEST_JARS.length, urls.size());

 URL[] urlArray = new URL[urls.size()];
 for (int i = 0; i < urls.size(); ++i) {
 urlArray[i] = urls.elementAt(i);
 }
 XBUSClassLoader newClassLoader = new XBUSClassLoader(urlArray, getClass().getClassLoader());
 assertNotSame(classLoader, newClassLoader);
 }

 @Test
 public void testAddUrls() throws Exception {
 Vector urls = new Vector();
 XBUSClassLoader.addUrls(urls, LIB_DIR);
 assertEquals(4, urls.size());

 File libPath = new File(LIB_DIR);
 File[] jars = libPath.listFiles();
 for (int i = 0; i < jars.length; ++i) {
 if (jars[i].isFile()) {
 if (!jars[i].getName().endsWith("jar")) {
 if (!jars[i].getName().endsWith("zip")) {
 continue;
 }
 }
 File jarFile = jars[i];
 URL jarUrl = jarFile.toURL();
 for (int j = 0; j < urls.size(); ++j) {
 URL url = urls.elementAt(j);
 assertEquals(jarUrl.toString(), url.toString());
 }
 }
 }
 }

 @Test
 public void testStaticInit() {
 XBUSClassLoader classLoader = mock(XBUSClassLoader.class);
 XBUSClassLoader.mClassLoader = classLoader;
 assertEquals(classLoader, XBUSClassLoader.getInstance(getClass().getClassLoader()));
 }

 @After
 public void tearDown() throws Exception {
 mClassLoader = null;
 }
}