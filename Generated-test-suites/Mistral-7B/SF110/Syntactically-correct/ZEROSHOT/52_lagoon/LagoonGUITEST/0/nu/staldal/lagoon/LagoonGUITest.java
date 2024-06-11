package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.*;
import nu.staldal.xtree.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class LagoonGUITest {

 private LagoonGUI appFrame;

 @Before
 public void setUp() {
 appFrame = new LagoonGUI();
 appFrame.show(); // Need to show the frame for some tests to work
 }

 @Test
 public void testMain() throws Exception {
 LagoonGUI.main(new String[]{"test.properties"});
 assertNotNull(appFrame.processor);
 }

 @Test
 public void testLagoonGUIConstructor() {
 // Test default constructor
 LagoonGUI lagoonGUI = new LagoonGUI();
 assertNotNull(lagoonGui);

 // Test constructor with arguments
 LagoonGUI lagoonGUIArg = new LagoonGUI("Test Lagoon GUI");
 assertEquals("Test Lagoon GUI", lagoonGUIArg.getTitle());
 }

 @Test
 public void testLoadProperties() {
 File propertyFile = new File("test.properties");
 appFrame.loadProperties(propertyFile);
 String expectedSitemapFile = "sitemap.xml";
 String expectedSourceDir = "src";
 String expectedTargetURL = "http://localhost:8080";
 String expectedPassword = "password";
 assertEquals(expectedSitemapFile, appFrame.sitemapFile.getValue());
 assertEquals(expectedSourceDir, appFrame.sourceDir.getValue());
 assertEquals(expectedTargetURL, appFrame.targetURL.getValue());
 assertEquals(expectedPassword, appFrame.password);
 }

 @Test(expected = LagoonException.class)
 public void testLoadPropertiesWithMissingProperty() throws IOException {
 File propertyFile = new File("testMissingProperty.properties");
 appFrame.loadProperties(propertyFile);
 fail("Expected LagoonException to be thrown");
 }

 @Test
 public void testSaveProperties() throws IOException {
 File propertyFile = new File("testSaveProperty.properties");
 appFrame.saveProperties();
 assertTrue(propertyFile.exists());
 }

 @Test
 public void testBuild() throws Exception {
 // Test build without arguments
 appFrame.build(false);
 // Test build with force argument
 appFrame.build(true);
 }

 @Test(expected = LagoonException.class)
 public void testBuildWithInvalidSitemapFile() throws IOException, SAXException {
 File sitemapFile = new File("invalidSitemap.xml");
 appFrame.sitemapFile.setValue(sitemapFile.getPath());
 appFrame.build(false);
 fail("Expected LagoonException to be thrown");
 }

 @Test(expected = FileNotFoundException.class)
 public void testInitWithMissingSitemapFile() throws IOException {
 appFrame.sitemapFile.setValue("");
 appFrame.init();
 fail("Expected FileNotFoundException to be thrown");
 }

 @Test
 public void testInitWithValidSitemapFile() throws IOException, SAXException {
 File sitemapFile = new File("testSitemap.xml");
 appFrame.sitemapFile.setValue(sitemapFile.getPath());
 appFrame.init();
 assertNotNull(appFrame.processor);
 }

 @Test(expected = AuthenticationMissingException.class)
 public void testInitWithMissingPassword() throws IOException, SAXException {
 appFrame.init();
 fail("Expected AuthenticationMissingException to be thrown");
 }

 @Test(expected = AuthenticationException.class)
 public void testInitWithInvalidPassword() throws IOException, SAXException {
 appFrame.password = "invalidPassword";
 appFrame.init();
 fail("Expected AuthenticationException to be thrown");
 }

 @Test
 public void testExit() {
 appFrame.exit();
 assertFalse(appFrame.isDisplayable());
 }
}