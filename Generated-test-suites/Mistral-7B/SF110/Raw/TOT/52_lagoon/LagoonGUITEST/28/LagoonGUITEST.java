package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.*;

import java.awt.event.ActionEvent;
import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class LagoonGUITest {

 private LagoonGUI appFrame;

 @Before
 public void setUp() {
 appFrame = new LagoonGUI();
 }

 @Test
 public void testInitialization() {
 // Test if the window is initialized correctly
 assertEquals("Lagoon", appFrame.getTitle());
 // Test if the input components are disabled by default
 assertFalse(appFrame.loadButton.isEnabled());
 assertFalse(appFrame.saveButton.isEnabled());
 assertFalse(appFrame.buildButton.isEnabled());
 assertFalse(appFrame.forceButton.isEnabled());
 // Test if the status label is not initialized
 assertEquals("Not initialized", appFrame.statusLabel.getText());
 }

 @Test
 public void testLoadProperties() {
 // Set up some test properties
 final File propFile = new File("testProperties.txt");
 try {
 FileWriter writer = new FileWriter(propFile);
 writer.write("sitemapFile=testSitemap.xml\nsourceDir=testSourceDir\ntargetURL=testTargetURL\npassword=testPassword");
 writer.close();
 appFrame.loadButton.doClick();
 // Test if the properties are loaded correctly
 assertEquals("testSitemap.xml", appFrame.sitemapFile.getValue());
 assertEquals("testSourceDir", appFrame.sourceDir.getValue());
 assertEquals("testTargetURL", appFrame.targetURL.getValue());
 assertNull(appFrame.password);
 // Test if the input components are enabled after loading properties
 assertTrue(appFrame.loadButton.isEnabled());
 assertTrue(appFrame.saveButton.isEnabled());
 assertTrue(appFrame.buildButton.isEnabled());
 assertTrue(appFrame.forceButton.isEnabled());
 // Test if the status label is initialized
 assertEquals("Ready", appFrame.statusLabel.getText());
 // Clean up
 appFrame.saveButton.doClick();
 appFrame.exitButton.doClick();
 FileUtils.deleteQuietly(propFile);
 }
 catch (IOException e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testSaveProperties() {
 // Test if properties can be saved
 appFrame.loadProperties(new File("testProperties.txt"));
 final File newPropFile = new File("newProperties.txt");
 appFrame.saveButton.doClick();
 try {
 FileReader reader = new FileReader(newPropFile);
 String propText = new String(reader.readAllBytes());
 reader.close();
 // Test if the properties are saved correctly
 assertEquals("Lagoon properties\nsitemapFile=testSitemap.xml\nsourceDir=testSourceDir\ntargetURL=testTargetURL\npassword=null", propText);
 // Clean up
 FileUtils.deleteQuietly(newPropFile);
 }
 catch (IOException e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testBuildWithoutSitemapFile() {
 // Test if build fails without a sitemap file
 appFrame.sitemapFile.setValue("");
 appFrame.sourceDir.setValue("testSourceDir");
 appFrame.targetURL.setValue("testTargetURL");
 appFrame.buildButton.doClick();
 // Test if the status label shows an error message
 assertEquals("Error building: Sitemap file not specified", appFrame.statusLabel.getText());
 }

 @Test
 public void testBuildWithInvalidXMLSitemap() {
 // Set up a test XML file with an invalid format
 final File xmlFile = new File("testSitemap.xml");
 try {
 FileWriter writer = new FileWriter(xmlFile);
 writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<sitemap>\n<loc>\n<url>\n<loc>\n<url>\n<loc>\n<url>\n<loc>\n<url>\n");
 writer.close();
 // Set up the test properties
 appFrame.sitemapFile.setValue(xmlFile.getAbsolutePath());
 appFrame.sourceDir.setValue("testSourceDir");
 appFrame.targetURL.setValue("testTargetURL");
 appFrame.password = "testPassword";
 // Test if build fails with an invalid XML sitemap file
 appFrame.buildButton.doClick();
 // Test if the status label shows an error message
 assertEquals("Error building: Invalid XML sitemap file", appFrame.statusLabel.getText());
 // Clean up
 appFrame.exitButton.doClick();
 FileUtils.deleteQuietly(xmlFile);
 }
 catch (IOException e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testBuildWithPassword() {
 // Test if build succeeds with a valid password
 appFrame.sitemapFile.setValue("testSitemap.xml");
 appFrame.sourceDir.setValue("testSourceDir");
 appFrame.targetURL.setValue("testTargetURL");
 appFrame.password = "testPassword";
 appFrame.buildButton.doClick();
 // Test if the status label shows a success message
 assertEquals("Building...", appFrame.statusLabel.getText());
 // Test if the progress area shows build output
 assertFalse(appFrame.progressArea.getText().isEmpty());
 // Test if the status label shows "Ready" after the build is finished
 assertEquals("Ready", appFrame.statusLabel.getText());
 }

 @Test(expected = IOException.class)
 public void testBuildWithMissingSourceDir() {
 // Test if build fails with a missing source directory
 appFrame.sitemapFile.setValue("testSitemap.xml");
 appFrame.targetURL.setValue("testTargetURL");
 appFrame.password = "testPassword";
 appFrame.sourceDir.setValue("");
 appFrame.buildButton.doClick();
 // Test if an IOException is thrown
 }
}