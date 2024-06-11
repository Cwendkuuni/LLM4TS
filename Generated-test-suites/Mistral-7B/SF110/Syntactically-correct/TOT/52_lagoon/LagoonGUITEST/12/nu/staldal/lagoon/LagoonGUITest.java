package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.xtree.Element;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.*;

public class LagoonGUITest {

 private LagoonGUI gui;

 @Before
 public void setUp() throws Exception {
 System.setProperty("java.awt.headless", "true");
 gui = new LagoonGUI();
 gui.show();
 }

 @After
 public void tearDown() {
 gui.exit();
 }

 @Test
 public void testLoadProperties() {
 final File propFile = new File("testProperties.properties");
 final Properties prop = new Properties();
 prop.setProperty("sitemapFile", "testSitemap.xml");
 prop.setProperty("sourceDir", "testSourceDir");
 prop.setProperty("targetURL", "http://test.com");
 prop.setProperty("password", "testPassword");
 try (FileOutputStream fos = new FileOutputStream(propFile)) {
 prop.store(fos, "Lagoon properties");
 }
 gui.loadProperties(propFile);
 assertEquals("testSitemap.xml", gui.sitemapFile.getValue());
 assertEquals("testSourceDir", gui.sourceDir.getValue());
 assertEquals("http://test.com", gui.targetURL.getValue());
 assertEquals("testPassword", gui.password);
 }

 @Test
 public void testLoadPropertiesEmpty() {
 gui.loadProperties(new File(""));
 assertNull(gui.sitemapFile.getValue());
 assertNull(gui.sourceDir.getValue());
 assertNull(gui.targetURL.getValue());
 assertNull(gui.password);
 }

 @Test
 public void testLoadPropertiesNull() {
 gui.loadProperties(null);
 assertNull(gui.sitemapFile.getValue());
 assertNull(gui.sourceDir.getValue());
 assertNull(gui.targetURL.getValue());
 assertNull(gui.password);
 }

 @Test
 public void testBuildWithValidProperties() {
 final Element sitemapTree = new Element("sitemap");
 gui.init();
 gui.build(false);
 assertTrue(gui.statusLabel.getText().contains("Building..."));
 final TextArea progressArea = gui.progressArea;
 assertTrue(progressArea.getText().contains("--- Start build ---"));
 assertFalse(progressArea.getText().contains("--- Build finished with error(s) ---"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildWithNullProcessor() {
 gui.build(false);
 }

 @Test(expected = IOException.class)
 public void testBuildWithInvalidSitemap() {
 final Element sitemapTree = new Element("invalidSitemap");
 gui.init();
 gui.processor = new LagoonProcessor(gui.targetURL.getValue(), sitemapTree, new File("testSourceDir"), gui.password, new PrintWriter(new TextAreaWriter(gui.progressArea)), new PrintWriter(new TextAreaWriter(gui.progressArea)));
 gui.build(false);
 }

 @Test(expected = AuthenticationException.class)
 public void testBuildWithInvalidPassword() {
 gui.init();
 gui.password = "invalidPassword";
 gui.build(false);
 }

 @Test(expected = FileNotFoundException.class)
 public void testBuildWithMissingSourceDir() {
 final File sourceDir = new File("missingSourceDir");
 gui.init();
 gui.sourceDir.setValue(sourceDir.getAbsolutePath());
 gui.build(false);
 }

 @Test(expected = LagoonException.class)
 public void testBuildWithMissingSitemapFile() {
 final File sitemapFile = new File("missingSitemapFile");
 gui.init();
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 gui.build(false);
 }

 @Test(expected = LagoonException.class)
 public void testBuildWithEmptySitemapFile() {
 final File sitemapFile = new File("emptySitemapFile.xml");
 gui.init();
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 gui.build(false);
 }

 @Test(expected = LagoonException.class)
 public void testBuildWithInvalidTargetURL() {
 final File sitemapFile = new File("validSitemap.xml");
 gui.init();
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 gui.targetURL.setValue("http://invalidURL:123");
 gui.build(false);
 }
}