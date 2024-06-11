package nu.staldal.lagoon.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Properties;

import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.xtree.TreeBuilder;

public class LagoonGUITest {

 private LagoonGUI lagoonGUI;
 private Properties properties;

 @Before
 public void setUp() {
 lagoonGUI = new LagoonGUI();
 properties = new Properties();
 }

 @After
 public void tearDown() {
 lagoonGUI.exit();
 }

 @Test
 public void testInitialization() throws Exception {
 // Test initial state
 assertNotNull(lagoonGUI.getStatusLabel());
 assertNotNull(lagoonGUI.getProgressArea());
 assertNull(lagoonGUI.getProcessor());

 // Test initialization with valid sitemap file and properties
 File sitemapFile = new File("testSitemap.xml");
 properties.setProperty("sitemapFile", sitemapFile.getAbsolutePath());
 properties.setProperty("sourceDir", "testSourceDir");
 properties.setProperty("targetURL", "testTargetURL");
 lagoonGUI.loadProperties(properties);
 lagoonGUI.build(false);
 verify(mock(LagoonProcessor.class), times(1)).build(false);
 }

 @Test(expected = LagoonException.class)
 public void testInitializationWithMissingSitemapFile() throws Exception {
 properties.setProperty("sitemapFile", "");
 lagoonGUI.loadProperties(properties);
 lagoonGUI.build(false);
 verifyNoInteractions(mock(LagoonProcessor.class));
 fail("Expected LagoonException to be thrown");
 }

 @Test(expected = LagoonException.class)
 public void testInitializationWithInvalidSitemapFile() throws Exception {
 properties.setProperty("sitemapFile", "testInvalidFile");
 lagoonGUI.loadProperties(properties);
 lagoonGUI.build(false);
 verifyNoInteractions(mock(LagoonProcessor.class));
 fail("Expected LagoonException to be thrown");
 }

 @Test
 public void testLoadProperties() throws Exception {
 // Test loading properties from a file
 File propertyFile = new File("testPropertyFile.properties");
 lagoonGUI.loadProperties(propertyFile);
 assertEquals("testSitemap.xml", lagoonGUI.getSitemapFile().getValue());
 assertEquals("testSourceDir", lagoonGUI.getSourceDir().getValue());
 assertEquals("testTargetURL", lagoonGUI.getTargetURL().getValue());
 }

 @Test
 public void testSaveProperties() throws Exception {
 // Test saving properties to a file
 File tempPropertyFile = File.createTempFile("testPropertyFile", ".properties");
 lagoonGUI.savePropertyFile();
 Properties loadedProperties = new Properties();
 FileInputStream fis = new FileInputStream(tempPropertyFile);
 loadedProperties.load(fis);
 fis.close();
 assertEquals("testSitemap.xml", loadedProperties.getProperty("sitemapFile"));
 assertEquals("testSourceDir", loadedProperties.getProperty("sourceDir"));
 assertEquals("testTargetURL", loadedProperties.getProperty("targetURL"));
 }

 @Test
 public void testBuild() throws Exception {
 // Test build without force
 lagoonGUI.build(false);
 // Test build with force
 lagoonGUI.build(true);
 }

 @Test
 public void testExit() throws Exception {
 // Test exit
 lagoonGUI.exit();
 // Test exit with exception
 doThrow(new IOException("Test exception")).when(mock(LagoonProcessor.class)).destroy();
 lagoonGUI.exit();
 verify(mock(LagoonProcessor.class), times(1)).destroy();
 }
}