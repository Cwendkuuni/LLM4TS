package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;

import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.LagoonProcessor;
import nu.staldal.xtree.TreeBuilder;
import nu.staldal.xtree.XMLInputFactory;

public class LagoonGUITest {

 private LagoonGUI lagoonGUI;

 @Before
 public void setUp() {
 lagoonGUI = new LagoonGUI();
 }

 @Test
 public void testInitialize() throws Exception {
 // Test initialization
 lagoonGUI.init();
 assertNotNull(lagoonGUI.processor);
 }

 @Test(expected = LagoonException.class)
 public void testInitializeWithInvalidSitemapFile() throws Exception {
 // Test initialization with invalid sitemap file
 lagoonGUI.sitemapFile.setValue("nonexistentfile.xml");
 lagoonGUI.init();
 fail("Initialization should have thrown an exception");
 }

 @Test
 public void testBuildWithoutForce() throws Exception {
 // Test build without force
 lagoonGUI.buildButton.doClick();
 // Add assertions here to check the state of the GUI after build
 }

 @Test
 public void testBuildWithForce() throws Exception {
 // Test build with force
 lagoonGUI.forceButton.doClick();
 // Add assertions here to check the state of the GUI after build
 }

 @Test
 public void testExit() {
 // Test exit
 lagoonGUI.exitButton.doClick();
 // Add assertions here to check the application has exited
 }

 @Test
 public void testLoadProperties() throws Exception {
 // Test loading properties from a file
 File propertyFile = new File("path/to/propertyfile.properties");
 lagoonGUI.loadProperties(propertyFile);
 assertEquals("expectedValue", lagoonGUI.sitemapFile.getValue());
 assertEquals("expectedValue", lagoonGUI.sourceDir.getValue());
 assertEquals("expectedValue", lagoonGUI.targetURL.getValue());
 }

 @Test
 public void testSaveProperties() throws Exception {
 // Test saving properties to a file
 File propertyFile = new File("path/to/propertyfile.properties");
 lagoonGUI.savePropertyFile();
 File newPropertyFile = new File("path/to/newpropertyfile.properties");
 assertTrue(propertyFile.exists());
 assertFalse(newPropertyFile.exists());
 }

 @Test
 public void testLoadAndSavePropertiesWithPassword() throws Exception {
 // Test loading and saving properties with a password
 lagoonGUI.password = "testpassword";
 lagoonGUI.savePropertyFile();
 File propertyFile = new File("path/to/propertyfile.properties");
 lagoonGUI.loadProperties(propertyFile);
 assertEquals("testpassword", lagoonGUI.password);
 }

 @Test
 public void testBuildWithInvalidPassword() throws Exception {
 // Test build with invalid password
 lagoonGUI.password = "testpassword";
 lagoonGUI.init();
 lagoonGUI.password = "wrongpassword";
 lagoonGUI.buildButton.doClick();
 fail("Build should have failed with an exception");
 }

 @Test
 public void testBuildWithValidPassword() throws Exception {
 // Test build with valid password
 lagoonGUI.password = "testpassword";
 lagoonGUI.init();
 lagoonGUI.password = "testpassword";
 lagoonGUI.buildButton.doClick();
 // Add assertions here to check the state of the GUI after build
 }
}