package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LagoonGUITest {

 private LagoonGUI appFrame;

 @Before
 public void setUp() {
 appFrame = new LagoonGUI();
 appFrame.show();
 appFrame.addNotify(); // Workaround for JUnit not properly simulating a window being shown
 }

 @After
 public void tearDown() {
 appFrame.dispose();
 appFrame = null;
 }

 @Test
 public void testLoadProperties() {
 Properties properties = new Properties();
 File propertyFile = new File("test_properties.txt");
 try {
 Files.write(propertyFile.toPath(), "sitemapFile=test_sitemap.xml\nsourceDir=test_src\ntargetURL=http://test.com\npassword=test_password".getBytes());
 appFrame.loadProperties(propertyFile);
 assertEquals("test_sitemap.xml", appFrame.sitemapFile.getValue());
 assertEquals("test_src", appFrame.sourceDir.getValue());
 assertEquals("http://test.com", appFrame.targetURL.getValue());
 assertEquals("test_password", appFrame.password);
 } catch (LagoonException e) {
 fail("Unexpected exception: " + e.getMessage());
 } finally {
 Files.delete(propertyFile.toPath());
 }
 }

 @Test
 public void testSaveProperties() {
 Properties properties = new Properties();
 properties.setProperty("sitemapFile", "test_sitemap.xml");
 properties.setProperty("sourceDir", "test_src");
 properties.setProperty("targetURL", "http://test.com");
 appFrame.loadProperties(new File("test_properties.txt"));
 appFrame.savePropertyFile();
 File propertyFile = new File("test_properties.txt");
 try {
 String contents = new String(Files.readAllBytes(propertyFile.toPath()));
 assertEquals("sitemapFile=test_sitemap.xml\nsourceDir=test_src\ntargetURL=http://test.com\npassword=\n", contents);
 } catch (IOException e) {
 fail("Unexpected exception: " + e.getMessage());
 } finally {
 Files.delete(propertyFile.toPath());
 }
 }

 @Test
 public void testBuild_Success() {
 appFrame.sitemapFile.setValue("test_sitemap.xml");
 appFrame.sourceDir.setValue("test_src");
 appFrame.targetURL.setValue("http://test.com");
 appFrame.password = "test_password";
 appFrame.build(false);
 String expectedOutput = "--- Start build--- \n" +
 "Building...\n" +
 "--- Build finished successfully 225 ms ---";
 assertEquals(expectedOutput, appFrame.progressArea.getText());
 }

 @Test(expected = LagoonException.class)
 public void testBuild_Failed_XMLParseError() {
 appFrame.sitemapFile.setValue("test_sitemap_invalid.xml");
 appFrame.sourceDir.setValue("test_src");
 appFrame.targetURL.setValue("http://test.com");
 appFrame.password = "test_password";
 appFrame.build(false);
 }

 @Test(expected = LagoonException.class)
 public void testBuild_Failed_FileNotFound() {
 appFrame.sitemapFile.setValue("test_sitemap_not_found.xml");
 appFrame.sourceDir.setValue("test_src");
 appFrame.targetURL.setValue("http://test.com");
 appFrame.password = "test_password";
 appFrame.build(false);
 }

 @Test(expected = LagoonException.class)
 public void testBuild_Failed_AccessDenied() {
 appFrame.sitemapFile.setValue("test_sitemap.xml");
 appFrame.sourceDir.setValue("test_src");
 appFrame.targetURL.setValue("http://test.com");
 appFrame.password = null;
 appFrame.build(false);
 }

 @Test(expected = LagoonException.class)
 public void testBuild_Failed_AuthenticationError() {
 appFrame.sitemapFile.setValue("test_sitemap.xml");
 appFrame.sourceDir.setValue("test_src");
 appFrame.targetURL.setValue("http://test.com");
 appFrame.password = "test_wrong_password";
 appFrame.build(false);
 }

 @Test(expected = LagoonException.class)
 public void testBuild_Failed_IOException() {
 appFrame.sitemapFile.setValue("test_sitemap.xml");
 appFrame.sourceDir.setValue("test_src");
 appFrame.targetURL.setValue("http://test.com");
 appFrame.password = "test_password";
 appFrame.build(false);
 }

}