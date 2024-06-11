package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;

import java.io.File;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LagoonGUITest {

 private LagoonGUI appFrame;

 @Before
 public void setup() {
 appFrame = new LagoonGUI();
 }

 @After
 public void teardown() {
 appFrame.exit();
 }

 @Test
 public void testInit() {
 appFrame.init();
 assertNotNull(appFrame.processor);
 }

 @Test
 public void testBuildWithValidInput() {
 appFrame.build(false);
 assertTrue(appFrame.statusLabel.getText().contains("Building..."));
 // Add assertions here to verify the build result
 }

 @Test
 public void testBuildWithInvalidInput() {
 appFrame.sitemapFile.setValue("invalidFile.xml");
 appFrame.build(false);
 assertFalse(appFrame.statusLabel.getText().contains("Building..."));
 // Add assertions here to verify the error message
 }

 @Test
 public void testLoadProperties() {
 final Properties prop = new Properties();
 final File propertyFile = new File("testPropertyFile.properties");
 try {
 prop.load(new FileInputStream(propertyFile));
 appFrame.loadProperties(propertyFile);
 }
 catch (IOException e) {
 e.printStackTrace();
 fail("Failed to load property file");
 }
 assertEquals(propertyFile.getAbsolutePath(), appFrame.sitemapFile.getValue());
 assertEquals("testSourceDir", appFrame.sourceDir.getValue());
 assertEquals("testTargetURL", appFrame.targetURL.getValue());
 }

 @Test
 public void testSaveProperties() {
 final File tempFile = File.createTempFile("test", null);
 appFrame.savePropertyFile();
 assertTrue(tempFile.exists());
 appFrame.exit();
 assertFalse(tempFile.exists());
 }
}