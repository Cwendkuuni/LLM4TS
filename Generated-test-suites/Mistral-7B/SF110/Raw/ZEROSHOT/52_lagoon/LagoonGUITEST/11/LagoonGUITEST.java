package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.xtree.TreeBuilder;
import nu.staldal.xtree.XMLInputSource;

public class LagoonGUITest {

 private LagoonGUI lagoonGUI;
 private File sitemapFile;
 private File sourceDir;
 private File targetURL;
 private File propertyFile;
 private Properties properties;

 @Before
 public void setUp() throws Exception {
 lagoonGUI = new LagoonGUI();
 sitemapFile = new File("testSitemap.xml");
 sourceDir = new File("testSourceDir");
 targetURL = new File("testTargetURL");
 propertyFile = new File("testPropertyFile.properties");
 properties = new Properties();
 }

 @Test
 public void testLoadProperties() throws Exception {
 // Test loading properties from null file
 lagoonGUI.loadPropertyFile();
 assertNull(lagoonGUI.sitemapFile.getValue());
 assertNull(lagoonGUI.sourceDir.getValue());
 assertNull(lagoonGUI.targetURL.getValue());
 assertNull(lagoonGUI.password);

 // Test loading properties from existing file
 properties.setProperty("sitemapFile", sitemapFile.getPath());
 properties.setProperty("sourceDir", sourceDir.getPath());
 properties.setProperty("targetURL", targetURL.getPath());
 properties.setProperty("password", "testPassword");
 FileOutputStream fos = new FileOutputStream(propertyFile);
 properties.store(fos, "Lagoon properties");
 fos.close();
 lagoonGUI.loadPropertyFile();
 assertEquals(sitemapFile.getPath(), lagoonGUI.sitemapFile.getValue());
 assertEquals(sourceDir.getPath(), lagoonGUI.sourceDir.getValue());
 assertEquals(targetURL.getPath(), lagoonGUI.targetURL.getValue());
 assertEquals("testPassword", lagoonGUI.password);
 }

 @Test
 public void testSaveProperties() throws Exception {
 // Test saving properties to null file
 lagoonGUI.savePropertyFile();
 assertNull(propertyFile.length());

 // Test saving properties to existing file
 lagoonGUI.sitemapFile.setValue("testSitemap.xml");
 lagoonGUI.sourceDir.setValue("testSourceDir");
 lagoonGUI.targetURL.setValue("testTargetURL");
 lagoonGUI.password = "testPassword";
 lagoonGUI.savePropertyFile();
 FileInputStream fis = new FileInputStream(propertyFile);
 Properties prop = new Properties();
 prop.load(fis);
 fis.close();
 assertEquals("testSitemap.xml", prop.getProperty("sitemapFile"));
 assertEquals("testSourceDir", prop.getProperty("sourceDir"));
 assertEquals("testTargetURL", prop.getProperty("targetURL"));
 assertEquals("testPassword", prop.getProperty("password"));
 }

 @Test
 public void testBuild() throws Exception {
 // Test building with null processor
 assertNull(lagoonGUI.processor);
 lagoonGUI.build(false);
 assertTrue(lagoonGUI.statusLabel.getText().contains("Not initialized"));

 // Test building with initialized processor
 File sitemapTreeFile = new File("testSitemapTree.xml");
 XMLInputSource sitemapSource = new XMLInputSource(new FileInputStream(sitemapTreeFile));
 Element sitemapTree = TreeBuilder.parseXML(sitemapSource, false).getDocumentElement();
 lagoonGUI.init();
 lagoonGUI.processor = new LagoonProcessor(targetURL, sitemapTree, sourceDir, "testPassword", null, null);
 lagoonGUI.build(false);
 assertTrue(lagoonGUI.statusLabel.getText().contains("Building..."));
 }

 @Test(expected = LagoonException.class)
 public void testBuildWithError() throws Exception {
 // Test building with error
 File sitemapTreeFile = new File("testSitemapTreeWithError.xml");
 XMLInputSource sitemapSource = new XMLInputSource(new FileInputStream(sitemapTreeFile));
 Element sitemapTree = TreeBuilder.parseXML(sitemapSource, false).getDocumentElement();
 lagoonGUI.init();
 lagoonGUI.processor = new LagoonProcessor(targetURL, sitemapTree, sourceDir, "testPassword", null, null);
 lagoonGUI.build(false);
 assertTrue(lagoonGUI.statusLabel.getText().contains("Building..."));
 lagoonGUI.build(false); // Second build should fail
 fail("Expected LagoonException but didn't get one");
 }

 @Test
 public void testExit() {
 // Test exit
 lagoonGUI.exit();
 assertTrue(lagoonGUI.isDisplayable()); // Should be false after exit
 }
}