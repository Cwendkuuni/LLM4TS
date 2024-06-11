import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import nu.staldal.lagoon.LagoonException;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.xtree.Element;

public class LagoonGUITest {

 private LagoonGUI gui;

 @Before
 public void setUp() {
 System.setProperty("java.awt.headless", "true"); // Run tests without GUI
 gui = new LagoonGUI();
 }

 @Test
 public void testConstructor() {
 // Check that constructor does not throw an exception
 gui = new LagoonGUI();
 }

 @Test
 public void testLoadProperties() throws IOException, LagoonException {
 final File file = new File("test.properties"); // Replace with a valid property file
 gui.loadProperties(file);

 final String expectedSitemapFile = "test_sitemap.xml";
 final String expectedSourceDir = "test_source_dir";
 final String expectedTargetURL = "test_target_URL";
 final String expectedPassword = "test_password";

 assertEquals(expectedSitemapFile, gui.sitemapFile.getValue());
 assertEquals(expectedSourceDir, gui.sourceDir.getValue());
 assertEquals(expectedTargetURL, gui.targetURL.getValue());
 assertNull(gui.password);
 }

 @Test
 public void testSaveProperties() throws IOException {
 final Properties properties = new Properties();
 properties.setProperty("sitemapFile", "test_sitemap.xml");
 properties.setProperty("sourceDir", "test_source_dir");
 properties.setProperty("targetURL", "test_target_URL");
 properties.setProperty("password", "test_password");

 gui.savePropertyFile();
 final File savedPropertiesFile = new File("test_save_properties.properties");
 final String savedSitemapFile = gui.sitemapFile.getValue();
 final String savedSourceDir = gui.sourceDir.getValue();
 final String savedTargetURL = gui.targetURL.getValue();
 final String savedPassword = properties.getProperty("password");

 assertTrue(savedPropertiesFile.exists());
 assertTrue(savedPropertiesFile.length() > 0);
 assertEquals(savedSitemapFile, properties.getProperty("sitemapFile"));
 assertEquals(savedSourceDir, properties.getProperty("sourceDir"));
 assertEquals(savedTargetURL, properties.getProperty("targetURL"));
 assertEquals(savedPassword, properties.getProperty("password"));

 final File DialogPropertyFile = new File("test_dialog_save_properties.properties");
 final String savedDialogPassword = gui.getDialogPassword();
 if (savedDialogPassword != null) {
 properties.setProperty("password", savedDialogPassword);
 }
 gui.savePropertyFile();
 assertTrue(DialogPropertyFile.exists());
 assertTrue(DialogPropertyFile.length() > 0);
 assertNull(gui.password);
 }

 @Test
 public void testInit() throws IOException, LagoonException {
 gui.init();
 assertNull(gui.processor);
 }

 @Test
 public void testInitWithSitemapFile() throws IOException, LagoonException {
 final File sitemapFile = new File("test_sitemap.xml");
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 gui.init();
 assertNotNull(gui.processor);
 }

 @Test
 public void testInitWithSitemapFileChanged() throws IOException, LagoonException {
 final File sitemapFile = new File("test_sitemap.xml");
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 final Element element = new Element("test");
 gui.init();
 assertNotNull(gui.processor);
 gui.sitemapFile.setValue(new File("test_sitemap_changed.xml").getAbsolutePath());
 gui.init();
 assertNotNull(gui.processor);
 }

 @Test
 public void testInitWithSourceDirChanged() throws IOException, LagoonException {
 final File sourceDir = new File("test_source_dir");
 gui.sourceDir.setValue(sourceDir.getAbsolutePath());
 final Element element = new Element("test");
 gui.init();
 assertNotNull(gui.processor);
 gui.sourceDir.setValue(new File("test_source_dir_changed").getAbsolutePath());
 gui.init();
 assertNotNull(gui.processor);
 }

 @Test
 public void testInitWithTargetURLChanged() throws IOException, LagoonException {
 final File targetURL = new File("test_target_URL");
 gui.targetURL.setValue(targetURL.getAbsolutePath());
 final Element element = new Element("test");
 gui.init();
 assertNotNull(gui.processor);
 gui.targetURL.setValue(new File("test_target_URL_changed").getAbsolutePath());
 gui.init();
 assertNotNull(gui.processor);
 }

 @Test
 public void testBuildWithoutForce() throws IOException, LagoonException {
 final File sitemapFile = new File("test_sitemap.xml");
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 final Element element = new Element("test");
 gui.init();
 gui.build(false);
 }

 @Test(expected = LagoonException.class)
 public void testBuildWithForceAndNoProcessor() throws IOException, LagoonException {
 gui.build(true);
 }

 @Test(expected = LagoonException.class)
 public void testBuildWithForceAndFailedInitialization() throws IOException, LagoonException {
 final File sitemapFile = new File("test_sitemap.xml");
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 final Element element = new Element("test_initialization_error");
 gui.init();
 gui.build(true);
 }

 @Test(expected = FileNotFoundException.class)
 public void testBuildWithFileNotFoundException() throws IOException, LagoonException {
 final File sitemapFile = new File("test_sitemap_not_found.xml");
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 final Element element = new Element("test");
 gui.init();
 gui.build(true);
 }

 @Test(expected = IOException.class)
 public void testBuildWithIOException() throws IOException, LagoonException {
 final File sitemapFile = new File("test_sitemap.xml");
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 gui.progressArea = outputStream;
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 final Element element = new Element("test");
 gui.init();
 gui.build(true);
 }

 @Test(expected = LagoonException.class)
 public void testBuildWithLagoonException() throws IOException, LagoonException {
 final File sitemapFile = new File("test_sitemap.xml");
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 final Element element = new Element("test_lagoon_exception");
 gui.init();
 gui.build(true);
 }

 @Test
 public void testExit() {
 gui.exit();
 assertTrue(gui.isDisplayable());
 fail("Expected System.exit(0) to be called and the test to terminate");
 }
}