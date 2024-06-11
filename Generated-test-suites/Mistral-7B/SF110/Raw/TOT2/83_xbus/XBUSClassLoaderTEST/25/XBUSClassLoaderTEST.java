package net.sf.xbus.base.core.reflection;

import org.junit.Test;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {

   // Test for getInstance method
   @Test
   public void testGetInstance() {
      ClassLoader parent = XBUSClassLoader.class.getClassLoader();
      XBUSClassLoader classLoader = XBUSClassLoader.getInstance(parent);
      assertNotNull(classLoader);
   }

   // Test for getInstance with null parent
   @Test(expected = IllegalArgumentException.class)
   public void testGetInstanceWithNullParent() {
      XBUSClassLoader.getInstance(null);
   }

   // Test for createClassLoader method
   @Test
   public void testCreateClassLoader() {
      ClassLoader parent = XBUSClassLoader.class.getClassLoader();
      XBUSClassLoader classLoader = XBUSClassLoader.createClassLoader(parent);
      assertNotNull(classLoader);
   }

   // Test for createClassLoader with null parent
   @Test(expected = IllegalArgumentException.class)
   public void testCreateClassLoaderWithNullParent() {
      XBUSClassLoader.createClassLoader(null);
   }

   // Test for addUrls method
   @Test
   public void testAddUrls() {
      Vector urls = new Vector();
      XBUSClassLoader.addUrls(urls, "test");
      assertEquals(1, urls.size());
   }

   // Test for addUrls with null urls vector
   @Test(expected = IllegalArgumentException.class)
   public void testAddUrlsWithNullVector() {
      XBUSClassLoader.addUrls(null, "test");
   }

   // Test for error handling when adding a non-JAR or non-ZIP file
   @Test(expected = IllegalArgumentException.class)
   public void testAddUrlsWithInvalidFile() {
      Vector urls = new Vector();
      String invalidFilePath = "test/invalidFile.txt";
      XBUSClassLoader.addUrls(urls, invalidFilePath);
   }
}