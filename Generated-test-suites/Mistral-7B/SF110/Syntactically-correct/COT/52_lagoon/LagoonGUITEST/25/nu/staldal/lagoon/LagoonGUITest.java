package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.LagoonGUI;

public class LagoonGUITest {

 private LagoonGUI lagoonGUI;

 @Before
 public void setUp() {
 lagoonGUI = new LagoonGUI();
 }

 @Test
 public void testConstructor() {
 LagoonGUI lagoonGUI = new LagoonGUI();
 assertNotNull(lagoonGUI);
 }

 @Test
 public void testLoadButtonActionPerformed() {
 ActionEvent actionEvent = new ActionEvent(new Object(), 0, "ActionEvent");
 lagoonGUI.loadButton.addActionListener(e -> lagoonGUI.loadPropertyFile());
 lagoonGUI.loadButton.actionPerformed(actionEvent);
 lagoonGUI.assertLoadProperties();
 }

 @Test(expected = NullPointerException.class)
 public void testLoadButtonActionPerformedNullFileDialog() {
 ActionEvent actionEvent = new ActionEvent(new Object(), 0, "ActionEvent");
 lagoonGUI.loadButton.addActionListener(e -> lagoonGUI.loadPropertyFile(null));
 lagoonGUI.loadButton.actionPerformed(actionEvent);
 }

 @Test
 public void testSaveButtonActionPerformed() {
 ActionEvent actionEvent = new ActionEvent(new Object(), 0, "ActionEvent");
 lagoonGUI.saveButton.addActionListener(e -> lagoonGUI.savePropertyFile());
 lagoonGUI.saveButton.actionPerformed(actionEvent);
 lagoonGUI.assertPropertiesSaved();
 }

 @Test(expected = NullPointerException.class)
 public void testSaveButtonActionPerformedNullFileDialog() {
 ActionEvent actionEvent = new ActionEvent(new Object(), 0, "ActionEvent");
 lagoonGUI.saveButton.addActionListener(e -> lagoonGUI.savePropertyFile(null));
 lagoonGUI.saveButton.actionPerformed(actionEvent);
 }

 @Test
 public void testBuildButtonActionPerformed() {
 ActionEvent actionEvent = new ActionEvent(new Object(), 0, "ActionEvent");
 lagoonGUI.buildButton.addActionListener(e -> lagoonGUI.build(false));
 lagoonGUI.buildButton.actionPerformed(actionEvent);
 lagoonGUI.assertBuildSuccess();
 }

 @Test
 public void testBuildButtonActionPerformedWithForce() {
 ActionEvent actionEvent = new ActionEvent(new Object(), 0, "ActionEvent");
 lagoonGUI.buildButton.addActionListener(e -> lagoonGUI.build(true));
 lagoonGUI.buildButton.actionPerformed(actionEvent);
 lagoonGUI.assertBuildForceSuccess();
 }

 @Test(expected = NullPointerException.class)
 public void testBuildButtonActionPerformedNullFile() {
 ActionEvent actionEvent = new ActionEvent(new Object(), 0, "ActionEvent");
 lagoonGUI.buildButton.addActionListener(e -> lagoonGUI.build(null));
 lagoonGUI.buildButton.actionPerformed(actionEvent);
 }

 @Test
 public void testExitButtonActionPerformed() {
 ActionEvent actionEvent = new ActionEvent(new Object(), 0, "ActionEvent");
 lagoonGUI.exitButton.addActionListener(e -> lagoonGUI.exit());
 lagoonGUI.exitButton.actionPerformed(actionEvent);
 assertTrue(lagoonGUI.isExit());
 }

 @Test
 public void testInit() {
 lagoonGUI.init();
 lagoonGUI.assertInitialized();
 }

 @Test(expected = IOException.class)
 public void testInitWithInvalidSitemapFile() {
 final File sitemapFile = new File("doesnotexist.xml");
 lagoonGUI.sitemapFile = sitemapFile;
 lagoonGUI.init();
 fail("Initialization should have thrown an IOException");
 }

 @Test(expected = SAXException.class)
 public void testInitWithInvalidXMLSitemapFile() {
 final File sitemapFile = new File("invalidXML.xml");
 lagoonGUI.sitemapFile = sitemapFile;
 lagoonGUI.init();
 fail("Initialization should have thrown a SAXException");
 }

 @Test(expected = LagoonException.class)
 public void testInitWithMissingPassword() {
 final File sitemapFile = new File("validXML.xml");
 lagoonGUI.sitemapFile = sitemapFile;
 lagoonGUI.password = null;
 lagoonGUI.init();
 fail("Initialization should have thrown a LagoonException");
 }

 @Test(expected = AuthenticationException.class)
 public void testInitWithInvalidPassword() {
 final File sitemapFile = new File("validXML.xml");
 lagoonGUI.sitemapFile = sitemapFile;
 lagoonGUI.password = "incorrectpassword";
 lagoonGUI.init();
 fail("Initialization should have thrown an AuthenticationException");
 }

 @Test(expected = FileNotFoundException.class)
 public void testInitWithMissingSourceDir() {
 final File sitemapFile = new File("validXML.xml");
 final File sourceDir = new File("doesnotexist");
 lagoonGUI.sitemapFile = sitemapFile;
 lagoonGUI.sourceDir = sourceDir;
 lagoonGUI.init();
 fail("Initialization should have thrown a FileNotFoundException");
 }

 @Test(expected = IOException.class)
 public void testInitWithMissingTargetURL() {
 final File sitemapFile = new File("validXML.xml");
 final File targetURL = new File("doesnotexist");
 lagoonGUI.sitemapFile = sitemapFile;
 lagoonGUI.targetURL = targetURL;
 lagoonGUI.init();
 fail("Initialization should have thrown an IOException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullProperties() {
 final File sitemapFile = new File("validXML.xml");
 lagoonGUI.sitemapFile = sitemapFile;
 lagoonGUI.password = "validpassword";
 lagoonGUI.properties = null;
 lagoonGUI.init();
 fail("Initialization should have thrown an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithEmptyProperties() {
 final File sitemapFile = new File("validXML.xml");
 lagoonGUI.sitemapFile = sitemapFile;
 lagoonGUI.password = "validpassword";
 lagoonGUI.properties = new Properties();
 lagoonGUI.init();
 fail("Initialization should have thrown an IllegalArgumentException");
 }

 @Test
 public void testLoadProperties() {
 Properties properties = new Properties();
 properties.setProperty("sitemapFile", "validXML.xml");
 properties.setProperty("sourceDir", ".");
 properties.setProperty("targetURL", "http://localhost:8080/");
 lagoonGUI.loadProperties(properties);
 lagoonGUI.assertPropertiesLoaded();
 }

 @Test(expected = LagoonException.class)
 public void testLoadPropertiesWithMissingProperty() {
 Properties properties = new Properties();
 properties.setProperty("sitemapFile", "validXML.xml");
 lagoonGUI.loadProperties(properties);
 fail("Loading properties should have thrown a LagoonException");
 }

 @Test(expected = IOException.class)
 public void testLoadPropertiesWithInvalidXMLPropertyFile() {
 Properties properties = new Properties();
 properties.setProperty("sitemapFile", "invalidXML.xml");
 lagoonGUI.loadProperties(properties);
 fail("Loading properties should have thrown an IOException");
 }

 @Test
 public void testSaveProperties() {
 Properties properties = new Properties();
 properties.setProperty("sitemapFile", "validXML.xml");
 properties.setProperty("sourceDir", ".");
 properties.setProperty("targetURL", "http://localhost:8080/");
 lagoonGUI.saveProperties(properties);
 lagoonGUI.assertPropertiesSaved();
 }

 @Test(expected = NullPointerException.class)
 public void testSavePropertiesWithNullFile() {
 Properties properties = new Properties();
 properties.setProperty("sitemapFile", "validXML.xml");
 properties.setProperty("sourceDir", ".");
 properties.setProperty("targetURL", "http://localhost:8080/");
 lagoonGUI.saveProperties(null);
 fail("Saving properties should have thrown a NullPointerException");
 }

}