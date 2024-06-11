package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonException;

import org.junit.Test;

public class LagoonGUITest {

 private static final String TEST_SITEMAP_FILE = "testSitemap.xml";
 private static final String TEST_SOURCE_DIR = "testSourceDir";
 private static final String TEST_TARGET_URL = "http://test.com";
 private static final String TEST_PASSWORD = "testPassword";

 @Test
 public void testInit() throws LagoonException {
 LagoonGUI appFrame = new LagoonGUI();
 appFrame.sitemapFile.setValue(TEST_SITEMAP_FILE);
 appFrame.sourceDir.setValue(TEST_SOURCE_DIR);
 appFrame.targetURL.setValue(TEST_TARGET_URL);
 appFrame.init();
 assertEquals("Initialized", appFrame.statusLabel.getText());
 }

 @Test(expected = LagoonException.class)
 public void testInitWithInvalidSitemapFile() throws LagoonException {
 LagoonGUI appFrame = new LagoonGUI();
 appFrame.sitemapFile.setValue("nonExistentFile.xml");
 appFrame.init();
 fail("Initialization should fail with an exception");
 }

 @Test
 public void testBuild() throws Exception {
 LagoonGUI appFrame = new LagoonGUI();
 appFrame.sitemapFile.setValue(TEST_SITEMAP_FILE);
 appFrame.sourceDir.setValue(TEST_SOURCE_DIR);
 appFrame.targetURL.setValue(TEST_TARGET_URL);
 appFrame.password = TEST_PASSWORD;
 appFrame.init();
 appFrame.buildButton.doClick();
 String expectedOutput = "--- Build finished successfully ---"; // Add expected output here
 String actualOutput = appFrame.progressArea.getText();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IOException.class)
 public void testBuildWithInvalidSourceDirectory() throws Exception {
 LagoonGUI appFrame = new LagoonGUI();
 appFrame.sitemapFile.setValue(TEST_SITEMAP_FILE);
 appFrame.sourceDir.setValue("nonExistentDir");
 appFrame.targetURL.setValue(TEST_TARGET_URL);
 appFrame.password = TEST_PASSWORD;
 appFrame.init();
 appFrame.buildButton.doClick();
 fail("Build process should fail with an IOException");
 }

 @Test(expected = AuthenticationException.class)
 public void testBuildWithInvalidPassword() throws Exception {
 LagoonGUI appFrame = new LagoonGUI();
 appFrame.sitemapFile.setValue(TEST_SITEMAP_FILE);
 appFrame.sourceDir.setValue(TEST_SOURCE_DIR);
 appFrame.targetURL.setValue(TEST_TARGET_URL);
 appFrame.password = "invalidPassword";
 appFrame.init();
 appFrame.buildButton.doClick();
 fail("Build process should fail with an AuthenticationException");
 }

 @Test
 public void testLoadProperties() throws Exception {
 LagoonGUI appFrame = new LagoonGUI();
 File propertyFile = new File("testProperties.properties");
 appFrame.loadProperties(propertyFile);
 assertEquals(TEST_SITEMAP_FILE, appFrame.sitemapFile.getValue());
 assertEquals(TEST_SOURCE_DIR, appFrame.sourceDir.getValue());
 assertEquals(TEST_TARGET_URL, appFrame.targetURL.getValue());
 assertEquals(TEST_PASSWORD, appFrame.password);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadPropertiesWithNonExistentFile() throws Exception {
 LagoonGUI appFrame = new LagoonGUI();
 File propertyFile = new File("nonExistentFile.properties");
 appFrame.loadProperties(propertyFile);
 fail("Loading properties should fail with a FileNotFoundException");
 }
}