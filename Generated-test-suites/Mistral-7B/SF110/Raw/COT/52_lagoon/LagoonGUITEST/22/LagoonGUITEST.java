package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.MessageDialog;
import nu.staldal.lagoon.core.YesNoQueryDialog;
import nu.staldal.xtree.TreeBuilder;
import nu.staldal.xtree.XMLInputFactory;

public class LagoonGUITest {

 private LagoonGUI lagoonGUI;

 @Before
 public void setUp() {
 lagoonGUI = new LagoonGUI();
 }

 @Test
 public void testConstructor() {
 LagoonGUI appFrame = new LagoonGUI();
 assertNotNull(appFrame);
 }

 @Test
 public void testLoadProperties() {
 File propertyFile = new File("testProperties.prop");
 lagoonGUI.loadProperties(propertyFile);
 assertEquals("testSitemapFile.xml", lagoonGUI.sitemapFile.getValue());
 assertEquals("testSourceDir", lagoonGUI.sourceDir.getValue());
 assertEquals("testTargetURL.com", lagoonGUI.targetURL.getValue());
 assertEquals("testPassword", lagoonGUI.password);
 }

 @Test
 public void testSaveProperties() {
 File propertyFile = new File("testProperties.prop");
 lagoonGUI.savePropertyFile();
 assertNotNull(propertyFile.length());
 }

 @Test
 public void testBuildNormal() {
 lagoonGUI.sitemapFile.setValue("testSitemap.xml");
 lagoonGUI.sourceDir.setValue("testSourceDir");
 lagoonGUI.targetURL.setValue("testTargetURL.com");
 lagoonGUI.buildButton.doClick();
 // Check status label
 assertEquals("Building...", lagoonGUI.statusLabel.getText());
 // Check progress area
 String expectedProgressArea = "--- Start build ---\nBuilding...\n--- Build finished successfully 5s ---";
 assertEquals(expectedProgressArea, lagoonGUI.progressArea.getText());
 // Check buildButton is disabled
 assertFalse(lagoonGUI.buildButton.isEnabled());
 }

 @Test(expected = LagoonException.class)
 public void testBuildSitemapNotFound() {
 lagoonGUI.sitemapFile.setValue("testSitemapNotFound.xml");
 lagoonGUI.sourceDir.setValue("testSourceDir");
 lagoonGUI.targetURL.setValue("testTargetURL.com");
 lagoonGUI.buildButton.doClick();
 }

 @Test(expected = IOException.class)
 public void testBuildIOException() {
 lagoonGUI.sitemapFile.setValue("testSitemap.xml");
 lagoonGUI.sourceDir.setValue("testSourceDir");
 lagoonGUI.targetURL.setValue("testTargetURL.com");
 File inputFile = new File("testInputFile.xml");
 try {
 lagoonGUI.processor = new LagoonProcessor(lagoonGUI.targetURL.getValue(), TreeBuilder.parseXML(new FileInputStream(lagoonGUI.sitemapFile.getValue()), false), inputFile, lagoonGUI.password, null, null);
 }
 catch (Exception e) {
 // swallow exception
 }
 lagoonGUI.buildButton.doClick();
 }

 @Test
 public void testBuildForce() {
 lagoonGUI.sitemapFile.setValue("testSitemap.xml");
 lagoonGUI.sourceDir.setValue("testSourceDir");
 lagoonGUI.targetURL.setValue("testTargetURL.com");
 lagoonGUI.forceButton.doClick();
 // Check status label
 assertEquals("Building...", lagoonGUI.statusLabel.getText());
 // Check progress area
 String expectedProgressArea = "--- Start build ---\nBuilding...\n--- Build finished successfully 5s ---";
 assertEquals(expectedProgressArea, lagoonGUI.progressArea.getText());
 // Check buildButton is disabled
 assertFalse(lagoonGUI.buildButton.isEnabled());
 }

 @Test
 public void testInitPasswordMissing() {
 lagoonGUI.sitemapFile.setValue("testSitemap.xml");
 lagoonGUI.sourceDir.setValue("testSourceDir");
 lagoonGUI.targetURL.setValue("testTargetURL.com");
 lagoonGUI.init();
 assertTrue(lagoonGUI.password == null);
 }

 @Test
 public void testInitPasswordInvalid() {
 lagoonGUI.sitemapFile.setValue("testSitemap.xml");
 lagoonGUI.sourceDir.setValue("testSourceDir");
 lagoonGUI.targetURL.setValue("testTargetURL.com");
 lagoonGUI.password = "testInvalidPassword";
 lagoonGUI.init();
 assertFalse(lagoonGUI.processor != null);
 }

 @Test
 public void testInitPasswordMissingException() {
 lagoonGUI.sitemapFile.setValue("testSitemap.xml");
 lagoonGUI.sourceDir.setValue("testSourceDir");
 lagoonGUI.targetURL.setValue("testTargetURL.com");
 LagoonException exception = assertThrows(LagoonException.class, () -> lagoonGUI.init());
 assertEquals("AuthenticationMissingException: Password needed to access target", exception.getMessage());
 }

 @Test
 public void testInitPasswordInvalidException() {
 lagoonGUI.sitemapFile.setValue("testSitemap.xml");
 lagoonGUI.sourceDir.setValue("testSourceDir");
 lagoonGUI.targetURL.setValue("testTargetURL.com");
 lagoonGUI.password = "testInvalidPassword";
 LagoonException exception = assertThrows(LagoonException.class, () -> lagoonGUI.init());
 assertEquals("AuthenticationException: Invalid password, try again", exception.getMessage());
 }

 @Test
 public void testExit() {
 lagoonGUI.exitButton.doClick();
 assertEquals(0, System.exitCode());
 }
}