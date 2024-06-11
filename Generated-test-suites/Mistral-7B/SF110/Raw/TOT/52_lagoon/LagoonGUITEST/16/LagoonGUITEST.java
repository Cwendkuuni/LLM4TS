package nu.staldal.lagoon.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.LagoonProcessor;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.xtree.Element;

public class LagoonGUITest {

 private LagoonGUI appFrame;

 @Before
 public void setUp() {
 appFrame = new LagoonGUI();
 }

 @After
 public void tearDown() {
 appFrame.exit();
 }

 @Test
 public void testInitializeGUI() {
 appFrame.show();
 // Test if the GUI is initialized properly by checking the components
 assertSame(appFrame.getTitle(), "Lagoon");
 assertFalse(appFrame.isFocusable());
 assertTrue(appFrame.isVisible());
 assertNotNull(appFrame.getInputPanel());
 assertNotNull(appFrame.getButtonPanel());
 assertNotNull(appFrame.getCenterPanel());
 assertNotNull(appFrame.getProgressArea());
 assertNotNull(appFrame.getStatusLabel());
 assertNull(appFrame.getProcessor());
 }

 @Test
 public void testLoadProperties() throws Exception {
 File propFile = new File("test.properties");
 appFrame.loadPropertyFile();
 appFrame.loadProperties(propFile);
 // Test if the properties are loaded correctly
 assertEquals("testSitemapFile", appFrame.getSitemapFile().getValue());
 assertEquals("testSourceDir", appFrame.getSourceDir().getValue());
 assertEquals("testTargetURL", appFrame.getTargetURL().getValue());
 assertNull(appFrame.getPassword());
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadPropertiesNotFound() throws Exception {
 appFrame.loadPropertyFile();
 appFrame.loadProperties(new File("nonexistentFile.properties"));
 }

 @Test
 public void testSaveProperties() {
 appFrame.savePropertyFile();
 File propFile = new File("test.properties");
 assertTrue(propFile.exists());
 }

 @Test
 public void testBuild() throws Exception {
 appFrame.buildButton.doClick();
 // Test if the build is initiated
 assertFalse(appFrame.isEnabled());
 // Test if the build is successful
 appFrame.build(false);
 assertTrue(appFrame.isEnabled());
 assertSame(appFrame.getStatusLabel().getText(), "Ready");
 }

 @Test(expected = IOException.class)
 public void testBuildIOException() throws Exception {
 appFrame.buildButton.doClick();
 appFrame.build(true);
 }

 @Test
 public void testInitWithInvalidSitemapFile() {
 appFrame.sitemapFile.setValue("test.xml");
 appFrame.init();
 // Test if the initialization fails with an invalid sitemap file
 assumeFalse(appFrame.getProcessor() != null);
 }

 @Test
 public void testInitWithValidSitemapFile() throws Exception {
 appFrame.sitemapFile.setValue("validSitemap.xml");
 appFrame.sourceDir.setValue("testSrcDir");
 appFrame.targetURL.setValue("testTargetURL");
 appFrame.init();
 // Test if the initialization is successful
 assertTrue(appFrame.getProcessor() != null);
 }

 @Test
 public void testExit() {
 appFrame.exitButton.doClick();
 // Test if the application exits properly
 assertEquals(0, System.exitCode());
 }
}