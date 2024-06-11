package nu.staldal.lagoon.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import nu.staldal.lagoon.*;
import nu.staldal.xtree.*;
import org.xml.sax.*;

public class LagoonGUITest {

 private LagoonGUI gui;

 @Before
 public void setUp() {
 gui = new LagoonGUI();
 gui.show();
 }

 @After
 public void tearDown() {
 gui.exit();
 }

 @Test
 public void testInitialization() {
 assertNotNull(gui.getStatusLabel());
 assertNotNull(gui.getProgressArea());
 assertNull(gui.getProcessor());
 assertFalse(gui.isInitialized());
 }

 @Test
 public void testLoadProperties() {
 gui.loadProperties(new File("testProperties.txt"));
 assertEquals("testSitemapFile", gui.getSitemapFile().getValue());
 assertEquals("testSourceDir", gui.getSourceDir().getValue());
 assertEquals("testTargetURL", gui.getTargetURL().getValue());
 assertNull(gui.getPassword());
 }

 @Test(expected = LagoonException.class)
 public void testLoadPropertiesWithInvalidFile() {
 gui.loadProperties(new File("doesnotexist.txt"));
 fail("Expected LagoonException to be thrown");
 }

 @Test
 public void testPropertiesFileSave() {
 final File propertyFile = new File("testPropertiesFile.txt");
 gui.savePropertyFile();
 assertTrue(propertyFile.exists());
 }

 @Test
 public void testBuild() {
 final File sitemapFile = new File("testSitemap.xml");
 final File sourceDir = new File("testSource");
 final File targetURL = new File("testTarget");
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 gui.sourceDir.setValue(sourceDir.getAbsolutePath());
 gui.targetURL.setValue(targetURL.getAbsolutePath());
 gui.build(false);
 assertTrue(gui.isInitialized());
 assertNotNull(gui.getProcessor());
 }

 @Test(expected = IOException.class)
 public void testBuildWithInvalidSitemapFile() {
 final File sitemapFile = new File("testSitemap.txt");
 final File sourceDir = new File("testSource");
 final File targetURL = new File("testTarget");
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 gui.sourceDir.setValue(sourceDir.getAbsolutePath());
 gui.targetURL.setValue(targetURL.getAbsolutePath());
 gui.init();
 gui.build(false);
 fail("Expected IOException to be thrown");
 }

 @Test
 public void testForceBuild() {
 final File sitemapFile = new File("testSitemap.xml");
 final File sourceDir = new File("testSource");
 final File targetURL = new File("testTarget");
 final File outputFile = new File("testOutput.html");
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 gui.sourceDir.setValue(sourceDir.getAbsolutePath());
 gui.targetURL.setValue(targetURL.getAbsolutePath());
 gui.init();
 gui.build(false);
 gui.build(true);
 assertTrue(outputFile.exists());
 }

 @Test
 public void testExit() {
 gui.exit();
 assertFalse(gui.isDisplayable());
 }
}