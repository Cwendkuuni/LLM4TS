package nu.staldal.lagoon.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import nu.staldal.lagoon.*;
import nu.staldal.xtree.*;
import java.awt.*;
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
 public void testInitializeComponents() {
 // Test if components are initialized correctly
 assertEquals("Lagoon", lagoonGUI.getTitle());
 assertNotNull(lagoonGUI.buttonPanel);
 assertNotNull(lagoonGUI.inputPanel);
 assertNotNull(lagoonGUI.centerPanel);
 assertNotNull(lagoonGUI.loadButton);
 assertNotNull(lagoonGUI.saveButton);
 assertNotNull(lagoonGUI.buildButton);
 assertNotNull(lagoonGUI.forceButton);
 assertNotNull(lagoonGUI.exitButton);
 assertNotNull(lagoonGUI.sitemapFile);
 assertNotNull(lagoonGUI.sourceDir);
 assertNotNull(lagoonGUI.targetURL);
 assertNotNull(lagoonGUI.statusLabel);
 assertNotNull(lagoonGUI.progressArea);
 }

 @Test
 public void testLoadProperties() {
 // Test loading properties file
 File samplePropertyFile = new File("sample.properties");
 try {
 FileWriter writer = new FileWriter(samplePropertyFile);
 writer.write("sitemapFile=test.xml\nsourceDir=testDir\ntargetURL=http://localhost:8080\npassword=testPassword");
 writer.close();

 lagoonGUI.loadPropertyFile();

 assertEquals("test.xml", lagoonGUI.sitemapFile.getValue());
 assertEquals("testDir", lagoonGUI.sourceDir.getValue());
 assertEquals("http://localhost:8080", lagoonGUI.targetURL.getValue());
 assertNotNull(lagoonGUI.password);
 assertEquals("testPassword", lagoonGUI.password);

 lagoonGUI.savePropertyFile();

 FileReader reader = new FileReader(samplePropertyFile);
 Properties prop = new Properties();
 prop.load(reader);
 assertEquals("test.xml", prop.getProperty("sitemapFile"));
 assertEquals("testDir", prop.getProperty("sourceDir"));
 assertEquals("http://localhost:8080", prop.getProperty("targetURL"));
 assertEquals("testPassword", prop.getProperty("password"));
 reader.close();

 samplePropertyFile.delete();

 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException occurred during test execution");
 }
 }

 @Test
 public void testLoadPropertiesMissingFile() {
 // Test loading properties file that does not exist
 lagoonGUI.loadPropertyFile();
 assertNull(lagoonGUI.password);
 }

 @Test
 public void testLoadPropertiesInvalidFile() {
 // Test loading properties file with invalid format
 File samplePropertyFile = new File("sample.txt");
 try {
 FileWriter writer = new FileWriter(samplePropertyFile);
 writer.write("InvalidFormat");
 writer.close();

 lagoonGUI.loadPropertyFile();
 fail("Expected LagoonException to be thrown");

 } catch (IOException e) {
 // Expected IOException during file creation
 } catch (LagoonException ex) {
 // Test if LagoonException is thrown
 String message = ex.getMessage();
 assertTrue(message.contains("Invalid format"));

 samplePropertyFile.delete();
 }
 }

 @Test
 public void testBuild() {
 // Test building with default values
 lagoonGUI.build(false);
 // Check if event listeners are disabled during build process
 Assert.assertFalse(lagoonGUI.loadButton.isEnabled());
 Assert.assertFalse(lagoonGUI.saveButton.isEnabled());
 Assert.assertFalse(lagoonGUI.buildButton.isEnabled());
 Assert.assertFalse(lagoonGUI.forceButton.isEnabled());
 Assert.assertFalse(lagoonGUI.sitemapFile.isEnabled());
 Assert.assertFalse(lagoonGUI.sourceDir.isEnabled());
 Assert.assertFalse(lagoonGUI.targetURL.isEnabled());
 // Test if initialization is performed
 // ... (add assertions to check initialization results)
 // Check if event listeners are enabled after build process
 Assert.assertTrue(lagoonGUI.loadButton.isEnabled());
 Assert.assertTrue(lagoonGUI.saveButton.isEnabled());
 Assert.assertTrue(lagoonGUI.buildButton.isEnabled());
 Assert.assertTrue(lagoonGUI.forceButton.isEnabled());
 Assert.assertTrue(lagoonGUI.sitemapFile.isEnabled());
 Assert.assertTrue(lagoonGUI.sourceDir.isEnabled());
 Assert.assertTrue(lagoonGUI.targetURL.isEnabled());
 }

 @Test
 public void testBuildWithForce() {
 // Test building with force option
 lagoonGUI.build(true);
 // Check if event listeners are disabled during build process
 Assert.assertFalse(lagoonGUI.loadButton.isEnabled());
 Assert.assertFalse(lagoonGUI.saveButton.isEnabled());
 Assert.assertFalse(lagoonGUI.buildButton.isEnabled());
 Assert.assertFalse(lagoonGUI.forceButton.isEnabled());
 Assert.assertFalse(lagoonGUI.sitemapFile.isEnabled());
 Assert.assertFalse(lagoonGUI.sourceDir.isEnabled());
 Assert.assertFalse(lagoonGUI.targetURL.isEnabled());
 // Test if initialization is performed with force option
 // ... (add assertions to check initialization results with force option)
 // Check if event listeners are enabled after build process
 Assert.assertTrue(lagoonGUI.loadButton.isEnabled());
 Assert.assertTrue(lagoonGUI.saveButton.isEnabled());
 Assert.assertTrue(lagoonGUI.buildButton.isEnabled());
 Assert.assertTrue(lagoonGUI.forceButton.isEnabled());
 Assert.assertTrue(lagoonGUI.sitemapFile.isEnabled());
 Assert.assertTrue(lagoonGUI.sourceDir.isEnabled());
 Assert.assertTrue(lagoonGUI.targetURL.isEnabled());
 }

 @Test
 public void testExit() {
 // Test exit function
 lagoonGUI.exit();
 assertTrue(lagoonGUI.isDisplayable() == false);
 }

}