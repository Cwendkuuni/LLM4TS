package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonException;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class LagoonGUITest {

 private LagoonGUI appFrame;

 @Before
 public void setup() {
 appFrame = new LagoonGUI();
 appFrame.addWindowListener(new MockWindowListener());
 }

 @Test
 public void testLoadProperties() {
 final Properties prop = new Properties();
 prop.setProperty("sitemapFile", "test.xml");
 prop.setProperty("sourceDir", "src");
 prop.setProperty("targetURL", "http://localhost:8080");
 appFrame.loadProperties(new File("test.properties"));
 assertEquals("test.xml", appFrame.sitemapFile.getValue());
 assertEquals("src", appFrame.sourceDir.getValue());
 assertEquals("http://localhost:8080", appFrame.targetURL.getValue());
 }

 @Test(expected = LagoonException.class)
 public void testLoadPropertiesMissingSitemapFile() {
 final Properties prop = new Properties();
 prop.setProperty("sourceDir", "src");
 prop.setProperty("targetURL", "http://localhost:8080");
 appFrame.loadProperties(new File("test.properties"));
 appFrame.init();
 fail("Initialization should have thrown exception");
 }

 @Test(expected = LagoonException.class)
 public void testLoadPropertiesMissingSourceDir() {
 final Properties prop = new Properties();
 prop.setProperty("sitemapFile", "test.xml");
 prop.setProperty("targetURL", "http://localhost:8080");
 appFrame.loadProperties(new File("test.properties"));
 appFrame.init();
 fail("Initialization should have thrown exception");
 }

 @Test(expected = LagoonException.class)
 public void testLoadPropertiesMissingTargetURL() {
 final Properties prop = new Properties();
 prop.setProperty("sitemapFile", "test.xml");
 prop.setProperty("sourceDir", "src");
 appFrame.loadProperties(new File("test.properties"));
 appFrame.init();
 fail("Initialization should have thrown exception");
 }

 @Test
 public void testBuild() {
 appFrame.sitemapFile.setValue("test.xml");
 appFrame.sourceDir.setValue("src");
 appFrame.targetURL.setValue("http://localhost:8080");
 appFrame.build(false);
 // Check statusLabel and progressArea output
 // ...
 }

 @Test
 public void testBuildForce() {
 appFrame.sitemapFile.setValue("test.xml");
 appFrame.sourceDir.setValue("src");
 appFrame.targetURL.setValue("http://localhost:8080");
 appFrame.build(true);
 // Check statusLabel and progressArea output
 // ...
 }

 @Test(expected = FileNotFoundException.class)
 public void testBuildMissingSitemapFile() {
 appFrame.sitemapFile.setValue("nonexistent.xml");
 appFrame.sourceDir.setValue("src");
 appFrame.targetURL.setValue("http://localhost:8080");
 appFrame.build(false);
 fail("Building should have thrown exception");
 }

 @Test(expected = IOException.class)
 public void testBuildIOException() {
 appFrame.sitemapFile.setValue("test.xml");
 appFrame.sourceDir.setValue("src");
 appFrame.targetURL.setValue("http://localhost:8080");
 appFrame.build(false);
 // Simulate an IOException during build process
 // ...
 fail("Building should have thrown exception");
 }

 @Test
 public void testExit() {
 appFrame.exit();
 // Check if the window is closed
 // ...
 }
}

class MockWindowListener extends WindowAdapter {
 @Override
 public void windowClosing(WindowEvent e) {
 // Do nothing
 }
}