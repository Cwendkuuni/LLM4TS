package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.*;
import nu.staldal.xtree.*;
import java.awt.*;
import java.io.*;

public class LagoonGUITest {

 private LagoonGUI appFrame;

 @Before
 public void setUp() {
 appFrame = new LagoonGUI();
 appFrame.show(); // Need to show the frame for some tests
 }

 @Test
 public void testConstructor() {
 LagoonGUI lg = new LagoonGUI();
 assertNotNull(lg);
 }

 @Test
 public void testLoadProperties() {
 appFrame.loadProperties(new File("testProperties.txt"));
 assertEquals("testSitemapFile", appFrame.sitemapFile.getValue());
 assertEquals("testSourceDir", appFrame.sourceDir.getValue());
 assertEquals("testTargetURL", appFrame.targetURL.getValue());
 }

 @Test(expected = LagoonException.class)
 public void testLoadPropertiesWithInvalidFile() {
 appFrame.loadProperties(new File("nonexistentfile.txt"));
 fail("Exception expected but not thrown");
 }

 @Test
 public void testSaveProperties() throws IOException {
 File propertyFile = new File("testProperties.txt");
 appFrame.savePropertyFile();
 assertTrue(propertyFile.exists());
 }

 @Test
 public void testBuild() throws Exception {
 appFrame.build(false);
 assertTrue(appFrame.statusLabel.getText().contains("Building..."));
 }

 @Test(expected = IOException.class)
 public void testBuildWithIOException() throws Exception {
 appFrame.build(false);
 appFrame.progressArea.setText("IOException test");
 appFrame.build(false);
 fail("IOException expected but not thrown");
 }

 @Test(expected = FileNotFoundException.class)
 public void testBuildWithFileNotFoundException() throws Exception {
 appFrame.build(false);
 appFrame.sitemapFile.setValue("nonexistentfile.xml");
 appFrame.build(false);
 fail("FileNotFoundException expected but not thrown");
 }

 @Test
 public void testExit() throws Exception {
 appFrame.exit();
 assertFalse(appFrame.isDisplayable());
 }

 @Test
 public void testInit() throws Exception {
 appFrame.init();
 assertEquals("Not initialized", appFrame.statusLabel.getText());
 }

 @Test
 public void testInitWithSitemapChange() throws Exception {
 appFrame.init();
 File sm = new File(appFrame.sitemapFile.getValue());
 sm.setLastModified(System.currentTimeMillis() + 1000);
 appFrame.init();
 assertNotEquals("Not initialized", appFrame.statusLabel.getText());
 }

 @Test
 public void testInitWithSourceDirChange() throws Exception {
 appFrame.init();
 File sourceDir = new File(appFrame.sourceDir.getValue());
 sourceDir.setLastModified(System.currentTimeMillis() + 1000);
 appFrame.init();
 assertNotEquals("Not initialized", appFrame.statusLabel.getText());
 }

 @Test
 public void testInitWithTargetURLChange() throws Exception {
 appFrame.init();
 File targetURL = new File(appFrame.targetURL.getValue());
 targetURL.setLastModified(System.currentTimeMillis() + 1000);
 appFrame.init();
 assertNotEquals("Not initialized", appFrame.statusLabel.getText());
 }
}