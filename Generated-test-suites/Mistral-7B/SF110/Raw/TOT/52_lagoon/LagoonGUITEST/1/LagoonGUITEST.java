package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonException;

import java.awt.event.ActionEvent;
import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class LagoonGUITest {

 private LagoonGUI gui;

 @Before
 public void setup() {
 gui = new LagoonGUI();
 gui.loadProperties(new File("test.properties"));
 gui.addWindowListener(gui);
 gui.show();
 }

 @Test
 public void testLoadProperties() {
 final String expectedSitemapFile = "testSitemap.xml";
 final String expectedSourceDir = "testSource";
 final String expectedTargetURL = "http://test.com";
 final String expectedPassword = "testPassword";

 gui.sitemapFile.setValue(expectedSitemapFile);
 gui.sourceDir.setValue(expectedSourceDir);
 gui.targetURL.setValue(expectedTargetURL);
 gui.password = expectedPassword;

 gui.loadProperties(new File("test.properties"));

 assertEquals(expectedSitemapFile, gui.sitemapFile.getValue());
 assertEquals(expectedSourceDir, gui.sourceDir.getValue());
 assertEquals(expectedTargetURL, gui.targetURL.getValue());
 assertEquals(expectedPassword, gui.password);
 }

 @Test(expected = LagoonException.class)
 public void testLoadPropertiesWithMissingProperty() {
 gui.loadProperties(new File("test.properties"));

 gui.sitemapFile.setValue("testSitemap.xml");
 gui.sourceDir.setValue("testSource");
 gui.targetURL.setValue("http://test.com");

 gui.loadProperties(new File("test.properties"));

 assertEquals("Property password not specified", gui.getLastMessage());
 }

 @Test
 public void testSaveProperties() {
 final String expectedSitemapFile = "testSitemap2.xml";
 final String expectedSourceDir = "testSource2";
 final String expectedTargetURL = "http://test.com2";
 final String expectedPassword = "testPassword2";

 gui.sitemapFile.setValue(expectedSitemapFile);
 gui.sourceDir.setValue(expectedSourceDir);
 gui.targetURL.setValue(expectedTargetURL);
 gui.password = expectedPassword;

 gui.savePropertyFile();

 assertEquals(expectedSitemapFile, gui.sitemapFile.getValue());
 assertEquals(expectedSourceDir, gui.sourceDir.getValue());
 assertEquals(expectedTargetURL, gui.targetURL.getValue());
 assertEquals(expectedPassword, gui.password);

 gui.statusLabel.setText("Properties saved");
 assertTrue(gui.getFile("test.properties").exists());
 }

 @Test
 public void testBuildWithSuccess() {
 gui.buildButton.doClick();

 assertEquals("Building...", gui.statusLabel.getText());
 final String expectedBuildMessage = "--- Build finished successfully " + showTime(12345) + " ---";

 gui.progressArea.setText("");
 gui.statusLabel.setText("Ready");
 assertEquals(expectedBuildMessage, gui.progressArea.getText());
 }

 @Test(expected = FileNotFoundException.class)
 public void testBuildWithMissingSitemapFile() {
 gui.sitemapFile.setValue("nonexistent.xml");
 gui.sourceDir.setValue("testSource");
 gui.targetURL.setValue("http://test.com");
 gui.password = "testPassword";

 gui.buildButton.doClick();
 }

 @Test
 public void testInitWithMissingSitemapFile() {
 gui.sitemapFile.setValue("nonexistent.xml");
 gui.sourceDir.setValue("testSource");
 gui.targetURL.setValue("http://test.com");
 gui.password = "testPassword";

 gui.init();

 assertEquals("Property file not found: nonexistent.xml", gui.getLastMessage());
 }

 @Test
 public void testInitWithInvalidPassword() {
 gui.sitemapFile.setValue("testSitemap.xml");
 gui.sourceDir.setValue("testSource");
 gui.targetURL.setValue("http://test.com");
 gui.password = "testPasswordInvalid";
 gui.init();

 assertEquals("Invalid password", gui.getLastMessage());
 }

 @Test
 public void testInitWithValidPassword() {
 gui.sitemapFile.setValue("testSitemap.xml");
 gui.sourceDir.setValue("testSource");
 gui.targetURL.setValue("http://test.com");
 gui.password = "testPasswordValid";

 gui.init();

 assertEquals("Initializing...", gui.statusLabel.getText());
 assertNotNull(gui.processor);
 }

 @Test
 public void testExit() {
 gui.exitButton.doClick();

 assertEquals("Not initialized", gui.statusLabel.getText());
 assertTrue(gui.isDisplayable());
 gui.dispose();
 assertFalse(gui.isDisplayable());
 }

 @Test
 public void testLoadPropertiesWithPassword() {
 final String expectedPassword = "testPassword";

 gui.loadProperties(new File("test.propertiesWithPassword.properties"));

 assertEquals(expectedPassword, gui.password);
 }

 private String showTime(final long ms) {
 if (ms < 10000L) {
 return "in " + ms + " ms";
 }
 return "in " + ms / 1000L + " s";
 }
}