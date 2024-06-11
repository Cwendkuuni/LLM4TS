package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.*;
import nu.staldal.xtree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import junit.framework.Assert;

public class LagoonGUITest {

 private LagoonGUI lagoonGUI;

 @Before
 public void setUp() {
 lagoonGUI = new LagoonGUI();
 }

 @Test
 public void testWindowCreation() {
 assertEquals("Lagoon", lagoonGUI.getTitle());
 assertTrue(lagoonGUI.isShowing());
 }

 @Test
 public void testInputComponentsConfiguration() {
 assertEquals("Sitemap file", lagoonGUI.sitemapFile.getLabel());
 assertNotNull(lagoonGUI.sitemapFile.getValue());
 assertEquals(".", lagoonGUI.sourceDir.getValue());
 assertEquals(".", lagoonGUI.targetURL.getValue());
 }

 @Test
 public void testLoadButtonActionPerformed() {
 final File file = new File("src/test/resources/test.properties");
 lagoonGUI.loadButton.doClick();
 lagoonGUI.loadProperties(file);
 assertEquals("test.properties", lagoonGUI.sitemapFile.getValue());
 assertEquals("src/test/resources", lagoonGUI.sourceDir.getValue());
 assertEquals("http://www.test.com", lagoonGUI.targetURL.getValue());
 }

 @Test(expected = LagoonException.class)
 public void testLoadButtonActionPerformedWithNullFile() {
 lagoonGUI.loadButton.doClick();
 lagoonGUI.loadProperties(null);
 fail("LoadProperties should have thrown an exception when passed a null file");
 }

 @Test
 public void testSaveButtonActionPerformed() {
 final File file = new File("src/test/resources/test.properties");
 lagoonGUI.saveButton.doClick();
 lagoonGUI.savePropertyFile();
 assertTrue(file.exists());
 }

 @Test(expected = IOException.class)
 public void testSaveButtonActionPerformedWithIOError() {
 final File file = new File("src/test/resources/test.properties");
 final PrintWriter printWriter = new PrintWriter(new TextAreaWriter(lagoonGUI.progressArea), true) {
 @Override
 public void println(final String x) {
 throw new IOException("Println error");
 }
 };
 lagoonGUI.saveButton.doClick();
 lagoonGUI.savePropertyFile();
 fail("SavePropertyFile should have thrown an IOException when an error occurred writing to the TextAreaWriter");
 }

 @Test
 public void testBuildButtonActionPerformed() throws IOException, SAXException {
 lagoonGUI.sitemapFile.setValue("src/test/resources/test.xml");
 lagoonGUI.sourceDir.setValue("src/test/resources/testSourceDirectory");
 lagoonGUI.targetURL.setValue("http://www.test.com");
 lagoonGUI.buildButton.doClick();
 assertEquals("--- Build finished successfully 5 ms ---", lagoonGUI.progressArea.getText());
 }

 @Test(expected = LagoonException.class)
 public void testBuildButtonActionPerformedWithNullSitemapFile() {
 lagoonGUI.sitemapFile.setValue(null);
 lagoonGUI.buildButton.doClick();
 fail("Build should have thrown an exception when passed a null sitemap file");
 }

 @Test
 public void testBuildButtonActionPerformedWithExpiredSitemapFile() throws IOException, SAXException {
 lagoonGUI.sitemapFile.setValue("src/test/resources/test.xml");
 final File sitemapFile = new File("src/test/resources/test.xml");
 sitemapFile.setLastModified(System.currentTimeMillis() - 1000);
 lagoonGUI.buildButton.doClick();
 assertTrue(lagoonGUI.progressArea.getText().contains("--- Build finished with error"));
 }

 @Test
 public void testBuildButtonActionPerformedWithNonExistentSourceDirectory() throws IOException, SAXException {
 lagoonGUI.sitemapFile.setValue("src/test/resources/test.xml");
 lagoonGUI.sourceDir.setValue("src/test/nonExistentDirectory");
 lagoonGUI.buildButton.doClick();
 assertTrue(lagoonGUI.progressArea.getText().contains("--- Build finished with error"));
 }

 @Test
 public void testBuildButtonActionPerformedWithInvalidTargetURL() throws IOException, SAXException {
 lagoonGUI.sitemapFile.setValue("src/test/resources/test.xml");
 lagoonGUI.sourceDir.setValue("src/test/resources");
 lagoonGUI.targetURL.setValue("http://www.test.com:666/");
 lagoonGUI.buildButton.doClick();
 assertTrue(lagoonGUI.progressArea.getText().contains("--- Build finished with error"));
 }

 @Test
 public void testForceButtonActionPerformed() throws IOException, SAXException {
 lagoonGUI.sitemapFile.setValue("src/test/resources/test.xml");
 lagoonGUI.sourceDir.setValue("src/test/resources/testSourceDirectory");
 lagoonGUI.targetURL.setValue("http://www.test.com");
 lagoonGUI.buildButton.doClick();
 lagoonGUI.forceButton.doClick();
 assertEquals("--- Build finished successfully 5 ms ---", lagoonGUI.progressArea.getText());
 }

 @Test
 public void testExitButtonActionPerformed() {
 lagoonGUI.exitButton.doClick();
 assertFalse(lagoonGUI.isShowing());
 }

 @Test
 public void testLoadProperties() throws IOException {
 final Properties properties = new Properties();
 properties.setProperty("sitemapFile", "test.xml");
 properties.setProperty("sourceDir", "testSourceDirectory");
 properties.setProperty("targetURL", "http://www.test.com");
 lagoonGUI.loadProperties(properties);
 assertEquals("test.xml", lagoonGUI.sitemapFile.getValue());
 assertEquals("testSourceDirectory", lagoonGUI.sourceDir.getValue());
 assertEquals("http://www.test.com", lagoonGUI.targetURL.getValue());
 }

 @Test
 public void testSaveProperties() throws IOException {
 final File directory = new File("src/test/resources/testProperties");
 directory.mkdirs();
 lagoonGUI.saveButton.doClick();
 assertTrue(new File("src/test/resources/testProperties/Lagoon.properties").exists());
 }

 @Test
 public void testLoadAndSavePropertiesWithPassword() throws IOException {
 final String password = "testPassword";
 final Properties properties = new Properties();
 properties.setProperty("sitemapFile", "test.xml");
 properties.setProperty("sourceDir", "testSourceDirectory");
 properties.setProperty("targetURL", "http://www.test.com");
 properties.setProperty("password", password);
 lagoonGUI.loadProperties(properties);
 assertEquals(password, lagoonGUI.password);
 lagoonGUI.savePropertyFile();
 final Properties loadedProperties = new Properties();
 final File newFile = new File("src/test/resources/test.properties");
 loadedProperties.load(new FileInputStream(newFile));
 assertEquals(password, loadedProperties.getProperty("password"));
 }
}