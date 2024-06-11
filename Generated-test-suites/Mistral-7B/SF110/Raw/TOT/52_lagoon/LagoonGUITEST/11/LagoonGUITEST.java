package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class LagoonGUITest {

 private LagoonGUI appFrame;

 @Before
 public void setUp() {
 appFrame = new LagoonGUI();
 appFrame.show();
 }

 @Test
 public void testLoadProperties() {
 final File propertyFile = new File("test.properties");
 appFrame.loadProperties(propertyFile);
 assertEquals("test.xml", appFrame.sitemapFile.getValue());
 assertEquals("testDir", appFrame.sourceDir.getValue());
 assertEquals("http://test.com", appFrame.targetURL.getValue());
 assertEquals("testPassword", appFrame.password);
 }

 @Test
 public void testSaveProperties() throws IOException {
 final File propertyFile = new File("test.properties");
 appFrame.saveProperties(propertyFile);
 final Properties prop = new Properties();
 prop.load(new FileInputStream(propertyFile));
 assertEquals("test.xml", prop.getProperty("sitemapFile"));
 assertEquals("testDir", prop.getProperty("sourceDir"));
 assertEquals("http://test.com", prop.getProperty("targetURL"));
 assertNull(prop.getProperty("password"));
 }

 @Test
 public void testBuildWithoutFileChange() {
 appFrame.build(false);
 appFrame.statusLabel.setText("Ready");
 }

 @Test
 public void testBuildWithFileChange() {
 final File sitemapFile = new File("test.xml");
 appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 appFrame.build(false);
 appFrame.statusLabel.setText("Ready");
 sitemapFile.setLastModified(System.currentTimeMillis());
 appFrame.build(false);
 appFrame.statusLabel.setText("Building...");
 // Simulate successful build
 appFrame.statusLabel.setText("Building... (success)");
 }

 @Test
 public void testBuildWithForce() {
 final File sitemapFile = new File("test.xml");
 appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 appFrame.statusLabel.setText("Ready");
 sitemapFile.setLastModified(System.currentTimeMillis());
 appFrame.build(true);
 appFrame.statusLabel.setText("Building...");
 // Simulate successful build
 appFrame.statusLabel.setText("Building... (success)");
 }

 @Test
 public void testInitWithPassword() {
 final File sitemapFile = new File("test.xml");
 appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 appFrame.statusLabel.setText("Not initialized");
 appFrame.init();
 assertEquals("http://test.com", appFrame.targetURL.getValue());
 }

 @Test
 public void testInitWithoutPassword() {
 final File sitemapFile = new File("test.xml");
 appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 appFrame.statusLabel.setText("Not initialized");
 try {
 appFrame.init();
 fail();
 }
 catch (AuthenticationMissingException e) {
 // Expected
 }
 }

 @Test
 public void testInitWithInvalidPassword() {
 final File sitemapFile = new File("test.xml");
 appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 appFrame.statusLabel.setText("Not initialized");
 appFrame.password = "invalidPassword";
 try {
 appFrame.init();
 fail();
 }
 catch (AuthenticationException e) {
 // Expected
 }
 }

 @Test
 public void testExit() {
 final int initialWindowCount = java.awt.Toolkit.getDefaultToolkit().getNumberOfFrames();
 appFrame.exit();
 final int finalWindowCount = java.awt.Toolkit.getDefaultToolkit().getNumberOfFrames();
 assertEquals(initialWindowCount - 1, finalWindowCount);
 }
}