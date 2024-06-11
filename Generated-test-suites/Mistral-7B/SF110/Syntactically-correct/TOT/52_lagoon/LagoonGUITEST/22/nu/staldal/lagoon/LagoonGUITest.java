package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.xtree.TreeBuilder;

public class LagoonGUITest {

 private LagoonGUI appFrame;

 @Before
 public void setUp() {
 appFrame = new LagoonGUI();
 appFrame.show();
 }

 @After
 public void tearDown() {
 appFrame.exit();
 }

 @Test
 public void testInit() throws IOException, LagoonException {
 // Test initialization with valid sitemap file and proper directories
 File sitemapFile = new File("src/test/resources/validSitemap.xml");
 File sourceDir = new File("src/test/resources/validSourceDir");
 File targetURL = new File("http://example.com");
 appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 appFrame.sourceDir.setValue(sourceDir.getAbsolutePath());
 appFrame.targetURL.setValue(targetURL.toString());
 appFrame.init();
 // Test initialization with missing sitemap file
 appFrame.sitemapFile.setValue("nonexistentFile.xml");
 appFrame.init();
 // Test initialization with invalid source directory
 File invalidSourceDir = new File("src/test/resources/invalidSourceDir");
 appFrame.sourceDir.setValue(invalidSourceDir.getAbsolutePath());
 appFrame.init();
 // Test initialization with invalid target URL
 File invalidTargetURL = new File("src/test/resources/invalidTargetURL");
 appFrame.targetURL.setValue(invalidTargetURL.toString());
 appFrame.init();
 }

 @Test
 public void testBuild() throws IOException, LagoonException {
 // Test build with valid data
 File sitemapFile = new File("src/test/resources/validSitemap.xml");
 File sourceDir = new File("src/test/resources/validSourceDir");
 File targetURL = new File("http://example.com");
 appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 appFrame.sourceDir.setValue(sourceDir.getAbsolutePath());
 appFrame.targetURL.setValue(targetURL.toString());
 appFrame.build(false);
 // Test build with force flag
 appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 appFrame.sourceDir.setValue(sourceDir.getAbsolutePath());
 appFrame.targetURL.setValue(targetURL.toString());
 appFrame.build(true);
 // Test build with missing sitemap file
 appFrame.sitemapFile.setValue("nonexistentFile.xml");
 appFrame.sourceDir.setValue(sourceDir.getAbsolutePath());
 appFrame.targetURL.setValue(targetURL.toString());
 appFrame.build(false);
 // Test build with invalid source directory
 appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 appFrame.sourceDir.setValue(invalidSourceDir.getAbsolutePath());
 appFrame.targetURL.setValue(targetURL.toString());
 appFrame.build(false);
 // Test build with invalid target URL
 appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 appFrame.sourceDir.setValue(sourceDir.getAbsolutePath());
 appFrame.targetURL.setValue(invalidTargetURL.toString());
 appFrame.build(false);
 }

 @Test(expected = LagoonException.class)
 public void testInitWithInvalidXML() throws IOException, LagoonException {
 // Test initialization with invalid XML sitemap file
 File invalidXML = new File("src/test/resources/invalidXML.xml");
 appFrame.sitemapFile.setValue(invalidXML.getAbsolutePath());
 appFrame.sourceDir.setValue(sourceDir.getAbsolutePath());
 appFrame.targetURL.setValue(targetURL.toString());
 appFrame.init();
 }

 @Test(expected = AuthenticationMissingException.class)
 public void testInitWithMissingPassword() throws IOException, LagoonException {
 // Test initialization with missing password
 File sitemapFile = new File("src/test/resources/validSitemap.xml");
 File sourceDir = new File("src/test/resources/validSourceDir");
 File targetURL = new File("http://example.com");
 appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 appFrame.sourceDir.setValue(sourceDir.getAbsolutePath());
 appFrame.targetURL.setValue(targetURL.toString());
 appFrame.init();
 }

 @Test(expected = AuthenticationException.class)
 public void testInitWithInvalidPassword() throws IOException, LagoonException {
 // Test initialization with invalid password
 File sitemapFile = new File("src/test/resources/validSitemap.xml");
 File sourceDir = new File("src/test/resources/validSourceDir");
 File targetURL = new File("http://example.com");
 appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 appFrame.sourceDir.setValue(sourceDir.getAbsolutePath());
 appFrame.targetURL.setValue(targetURL.toString());
 appFrame.password = "incorrectPassword";
 appFrame.init();
 }

 @Test(expected = FileNotFoundException.class)
 public void testBuildWithMissingFile() throws IOException, LagoonException {
 // Test build with missing file
 File sitemapFile = new File("src/test/resources/validSitemap.xml");
 File sourceDir = new File("src/test/resources/validSourceDir");
 File targetURL = new File("http://example.com");
 appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 appFrame.sourceDir.setValue(sourceDir.getAbsolutePath());
 appFrame.targetURL.setValue(targetURL.toString());
 appFrame.build(false);
 File missingFile = new File("src/test/resources/missingFile.txt");
 appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 appFrame.sourceDir.setValue(sourceDir.getAbsolutePath());
 appFrame.targetURL.setValue(targetURL.toString());
 appFrame.build(false);
 }

 @Test(expected = IOException.class)
 public void testBuildWithIOException() throws IOException, LagoonException {
 // Test build with IOException
 File sitemapFile = new File("src/test/resources/validSitemap.xml");
 File sourceDir = new File("src/test/resources/validSourceDir");
 File targetURL = new File("http://example.com");
 appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 appFrame.sourceDir.setValue(sourceDir.getAbsolutePath());
 appFrame.targetURL.setValue(targetURL.toString());
 appFrame.build(false);
 File inputFile = new File("src/test/resources/inputFile.txt");
 appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 appFrame.sourceDir.setValue(sourceDir.getAbsolutePath());
 appFrame.targetURL.setValue(targetURL.toString());
 appFrame.build(false);
 FileOutputStream outputStream = new FileOutputStream(new File("src/test/resources/outputFile.txt"));
 appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 appFrame.sourceDir.setValue(sourceDir.getAbsolutePath());
 appFrame.targetURL.setValue(targetURL.toString());
 appFrame.build(false);
 outputStream.close();
 appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 appFrame.sourceDir.setValue(sourceDir.getAbsolutePath());
 appFrame.targetURL.setValue(targetURL.toString());
 appFrame.build(false);
 }

 @Test
 public void testLoadProperties() {
 // Test loading properties from file
 File propertyFile = new File("src/test/resources/validProperties.txt");
 appFrame.loadPropertyFile();
 assertEquals("validSitemap.xml", appFrame.sitemapFile.getValue());
 assertEquals("src/test/resources/validSourceDir", appFrame.sourceDir.getValue());
 assertEquals("http://example.com", appFrame.targetURL.getValue());
 assertEquals("password", appFrame.password);
 // Test loading properties with missing password
 propertyFile = new File("src/test/resources/validPropertiesWithoutPassword.txt");
 appFrame.loadPropertyFile();
 assertNull(appFrame.password);
 }

 @Test
 public void testSaveProperties() throws IOException {
 // Test saving properties to file
 File propertyFile = new File("src/test/resources/emptyProperties.txt");
 appFrame.savePropertyFile();
 assertEquals("Properties saved", appFrame.statusLabel.getText());
 propertyFile = new File("src/test/resources/existingProperties.txt");
 appFrame.sitemapFile.setValue("newSitemapFile.xml");
 appFrame.sourceDir.setValue("newSourceDir");
 appFrame.targetURL.setValue("newTargetURL");
 appFrame.password = "newPassword";
 appFrame.savePropertyFile();
 assertEquals("Properties saved", appFrame.statusLabel.getText());
 FileReader fileReader = new FileReader(propertyFile);
 Properties properties = new Properties();
 properties.load(fileReader);
 assertEquals("newSitemapFile.xml", properties.getProperty("sitemapFile"));
 assertEquals("newSourceDir", properties.getProperty("sourceDir"));
 assertEquals("newTargetURL", properties.getProperty("targetURL"));
 assertEquals("newPassword", properties.getProperty("password"));
 fileReader.close();
 // Test saving properties with YesNoQueryDialog
 appFrame.savePropertyFile();
 YesNoQueryDialog yesNoQueryDialog = new YesNoQueryDialog(appFrame, "Save property file", "Save password in property file?", "Yes", "No");
 yesNoQueryDialog.show();
 appFrame.password = "newPassword";
 appFrame.savePropertyFile();
 assertEquals("Properties saved", appFrame.statusLabel.getText());
 propertyFile = new File("src/test/resources/newProperties.txt");
 FileReader fileReader2 = new FileReader(propertyFile);
 Properties properties2 = new Properties();
 properties2.load(fileReader2);
 assertEquals("newSitemapFile.xml", properties2.getProperty("sitemapFile"));
 assertEquals("newSourceDir", properties2.getProperty("sourceDir"));
 assertEquals("newTargetURL", properties2.getProperty("targetURL"));
 assertEquals("newPassword", properties2.getProperty("password"));
 fileReader2.close();
 }
}