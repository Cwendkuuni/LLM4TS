package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

import nu.staldal.lagoon.*;
import nu.staldal.xtree.*;

public class LagoonGUITest {

 private LagoonGUI lagoonGUI;

 @Test
 public void testLagoonGUIConstruction() {
 lagoonGUI = new LagoonGUI();
 assertNotNull(lagoonGUI);
 }

 @Test
 public void testLoadPropertiesButton() {
 lagoonGUI.loadButton.doClick();
 assertTrue(lagoonGUI.sitemapFile.isEnabled());
 assertTrue(lagoonGUI.sourceDir.isEnabled());
 assertTrue(lagoonGUI.targetURL.isEnabled());
 }

 @Test
 public void testSavePropertiesButton() {
 lagoonGUI.saveButton.doClick();
 // This test case assumes there is a file dialog implementation for saving properties
 // You need to mock it or use a real file system for this test to pass
 //assertFalse(lagoonGUI.statusLabel.getText().contains("Properties saved"));
 }

 @Test
 public void testBuildButton() throws IOException {
 lagoonGUI.sitemapFile.setValue("test.xml");
 lagoonGUI.sourceDir.setValue("src");
 lagoonGUI.targetURL.setValue("http://localhost:8080/");
 lagoonGUI.password = "testpassword".toCharArray();

 // Mock XML input and SAX parser to return a valid XML tree
 Element tree = new Element("sitemap");
 lagoonGUI.init();
 lagoonGUI.buildButton.doClick();
 // Add assertions here to check the behavior of the application after the build button is clicked
 }

 @Test
 public void testExitButton() {
 lagoonGUI.exitButton.doClick();
 // Add assertions here to check the behavior of the application after the exit button is clicked
 }

 @Test
 public void testInitializeWithInvalidPassword() throws IOException, AuthenticationException {
 lagoonGUI.sitemapFile.setValue("test.xml");
 lagoonGUI.sourceDir.setValue("src");
 lagoonGUI.targetURL.setValue("http://localhost:8080/");
 lagoonGUI.password = "wrongpassword".toCharArray();
 lagoonGUI.init();
 // Add assertions here to check the behavior of the application when an invalid password is provided
 }

 @Test
 public void testInitializeWithValidPassword() throws IOException, AuthenticationException {
 lagoonGUI.sitemapFile.setValue("test.xml");
 lagoonGUI.sourceDir.setValue("src");
 lagoonGUI.targetURL.setValue("http://localhost:8080/");
 lagoonGUI.password = "testpassword".toCharArray();
 lagoonGUI.init();
 // Add assertions here to check the behavior of the application when a valid password is provided
 }

 @Test
 public void testLoadPropertiesFile() throws IOException, LagoonException {
 Properties prop = new Properties();
 prop.setProperty("sitemapFile", "test.xml");
 prop.setProperty("sourceDir", "src");
 prop.setProperty("targetURL", "http://localhost:8080/");
 prop.setProperty("password", "testpassword");
 lagoonGUI = new LagoonGUI();
 lagoonGUI.loadProperties(new File("test.properties"));
 assertEquals(prop.getProperty("sitemapFile"), lagoonGUI.sitemapFile.getValue());
 assertEquals(prop.getProperty("sourceDir"), lagoonGUI.sourceDir.getValue());
 assertEquals(prop.getProperty("targetURL"), lagoonGUI.targetURL.getValue());
 assertEquals(prop.getProperty("password"), lagoonGUI.password.toString());
 }
}