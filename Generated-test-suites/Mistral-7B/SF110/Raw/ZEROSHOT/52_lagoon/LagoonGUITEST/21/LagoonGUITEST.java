package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import nu.staldal.lagoon.*;
import nu.staldal.lagoon.core.*;
import nu.staldal.xtree.*;

public class LagoonGUITest {

 private Frame frame;
 private LagoonGUI lagoonGUI;

 @Before
 public void setup() {
 lagoonGUI = new LagoonGUI();
 frame = lagoonGUI;
 }

 @After
 public void tearDown() {
 lagoonGUI.exit();
 }

 @Test
 public void testInitialization() {
 // Test if the GUI is initialized correctly
 assertEquals("Lagoon", frame.getTitle());
 assertNotNull(lagoonGUI.getCenterPanel());
 assertNotNull(lagoonGUI.getButtonPanel());
 assertNotNull(lagoonGUI.getInputPanel());
 assertNull(lagoonGUI.getProcessor());
 assertNull(lagoonGUI.getStatusLabel().getText());
 }

 @Test
 public void testLoadProperties() throws IOException, FileNotFoundException, SAXException, LagoonException {
 // Test loading properties from a file
 File propertyFile = new File("testProperties.properties");
 lagoonGUI.loadProperties(propertyFile);
 assertEquals("testProperties.properties", lagoonGUI.sitemapFile.getValue());
 assertEquals("testSourceDir", lagoonGUI.sourceDir.getValue());
 assertEquals("testTargetURL", lagoonGUI.targetURL.getValue());
 }

 @Test
 public void testSaveProperties() throws IOException {
 // Test saving properties to a file
 File file = new File("testProperties.properties");
 lagoonGUI.savePropertyFile();
 assertTrue(file.exists());
 // Test the contents of the saved file
 FileInputStream fis = new FileInputStream(file);
 Properties prop = new Properties();
 prop.load(fis);
 assertEquals("testProperties.properties", prop.getProperty("sitemapFile"));
 assertEquals("testSourceDir", prop.getProperty("sourceDir"));
 assertEquals("testTargetURL", prop.getProperty("targetURL"));
 fis.close();
 }

 @Test
 public void testBuild() throws IOException, InterruptedException, LagoonException, AuthenticationMissingException, AuthenticationException {
 // Test building with no errors
 lagoonGUI.buildButton.doClick();
 assertTrue(lagoonGUI.statusLabel.getText().equals("Building..."));
 // Test the progress area text
 assertEquals("", lagoonGUI.progressArea.getText());
 Thread.sleep(2000); // Sleep for a short time to allow the build process to finish
 assertEquals("--- Build finished successfully 2 s ---", lagoonGUI.progressArea.getText());
 // Test if the status label shows "Ready"
 assertEquals("Ready", lagoonGUI.statusLabel.getText());
 }

 @Test(expected = LagoonException.class)
 public void testBuildWithError() throws IOException, InterruptedException, LagoonException, AuthenticationMissingException, AuthenticationException {
 // Test building with an error
 lagoonGUI.buildButton.doClick();
 Thread.sleep(1000); // Sleep for a short time to allow the build process to start
 lagoonGUI.loadButton.doClick(); // Simulate loading a new sitemap file
 lagoonGUI.buildButton.doClick();
 }

 @Test
 public void testExit() {
 // Test exiting the application
 lagoonGUI.exitButton.doClick();
 assertTrue(java.awt.SystemDialog.mapExists(Dialog.Type.QUIT));
 }
}