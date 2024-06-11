package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.xtree.TreeBuilder;

public class LagoonGUITest {

 private LagoonGUI lagoonGUI;

 @Before
 public void setUp() {
 lagoonGUI = new LagoonGUI();
 }

 @Test
 public void testConstructor() {
 LagoonGUI lagoonGUI = new LagoonGUI();
 assertNotNull(lagoonGUI);
 }

 @Test
 public void testMain() throws Exception {
 LagoonGUI.main(new String[0]);
 assertTrue(lagoonGUI.isShowing());
 }

 @Test
 public void testLoadProperties() {
 File propertyFile = new File("src/test/resources/testProperties.properties");
 lagoonGUI.loadProperties(propertyFile);
 assertEquals("testSitemapFile", lagoonGUI.sitemapFile.getValue());
 assertEquals("testSourceDir", lagoonGUI.sourceDir.getValue());
 assertEquals("testTargetURL", lagoonGUI.targetURL.getValue());
 }

 @Test
 public void testSaveProperties() {
 File propertyFile = new File("src/test/resources/testProperties.properties");
 lagoonGUI.savePropertyFile();
 Properties prop = new Properties();
 FileInputStream fis = new FileInputStream(propertyFile);
 prop.load(fis);
 fis.close();
 assertEquals("testSitemapFile", prop.getProperty("sitemapFile"));
 assertEquals("testSourceDir", prop.getProperty("sourceDir"));
 assertEquals("testTargetURL", prop.getProperty("targetURL"));
 }

 @Test
 public void testBuild() throws Exception {
 File sitemapFile = new File("src/test/resources/testSitemap.xml");
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.sourceDir.setValue("src/test/resources/testSource");
 lagoonGUI.targetURL.setValue("http://localhost:8080");
 lagoonGUI.build(false);
 assertTrue(lagoonGUI.statusLabel.getText().contains("Building..."));
 }

 @Test(expected = LagoonException.class)
 public void testBuildWithInvalidSitemap() throws Exception {
 File sitemapFile = new File("src/test/resources/nonexistentFile.xml");
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.sourceDir.setValue("src/test/resources/testSource");
 lagoonGUI.targetURL.setValue("http://localhost:8080");
 lagoonGUI.build(false);
 fail("Build should have failed with LagoonException");
 }

 @Test(expected = FileNotFoundException.class)
 public void testBuildWithNonExistentSourceDirectory() throws Exception {
 File sitemapFile = new File("src/test/resources/testSitemap.xml");
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.sourceDir.setValue("nonexistentDirectory");
 lagoonGUI.targetURL.setValue("http://localhost:8080");
 lagoonGUI.build(false);
 fail("Build should have failed with FileNotFoundException");
 }

 @Test(expected = IOException.class)
 public void testBuildWithIOException() throws Exception {
 File sitemapFile = new File("src/test/resources/testSitemap.xml");
 File sourceDir = new File("src/test/resources/testSource");
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.sourceDir.setValue(sourceDir.getAbsolutePath());
 lagoonGUI.targetURL.setValue("http://localhost:8080");
 File inputFile = new File("src/test/resources/testInputFile.xml");
 File outputFile = new File("src/test/resources/testOutputFile.xml");
 LagoonProcessor processor = mock(LagoonProcessor.class);
 when(processor.build(false)).thenThrow(new IOException());
 lagoonGUI.processor = processor;
 lagoonGUI.build(false);
 fail("Build should have failed with IOException");
 }

 @Test
 public void testExit() throws Exception {
 lagoonGUI.exit();
 assertFalse(lagoonGUI.isShowing());
 }

 @Test
 public void testInit() throws Exception {
 File sitemapFile = new File("src/test/resources/testSitemap.xml");
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.sourceDir.setValue("src/test/resources/testSource");
 lagoonGUI.targetURL.setValue("http://localhost:8080");
 lagoonGUI.init();
 assertNotNull(lagoonGUI.processor);
 }

 @Test(expected = AuthenticationMissingException.class)
 public void testInitWithMissingPassword() throws Exception {
 File sitemapFile = new File("src/test/resources/testSitemap.xml");
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.sourceDir.setValue("src/test/resources/testSource");
 lagoonGUI.targetURL.setValue("http://localhost:8080");
 lagoonGUI.init();
 fail("Init should have thrown AuthenticationMissingException");
 }

 @Test(expected = AuthenticationException.class)
 public void testInitWithInvalidPassword() throws Exception {
 File sitemapFile = new File("src/test/resources/testSitemap.xml");
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.sourceDir.setValue("src/test/resources/testSource");
 lagoonGUI.targetURL.setValue("http://localhost:8080");
 lagoonGUI.password = "invalidPassword";
 lagoonGUI.init();
 fail("Init should have thrown AuthenticationException");
 }

 @Test(expected = FileNotFoundException.class)
 public void testInitWithNonExistentSitemapFile() throws Exception {
 File sitemapFile = new File("src/test/resources/nonexistentFile.xml");
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.sourceDir.setValue("src/test/resources/testSource");
 lagoonGUI.targetURL.setValue("http://localhost:8080");
 lagoonGUI.init();
 fail("Init should have failed with FileNotFoundException");
 }

 @Test(expected = IOException.class)
 public void testInitWithIOException() throws Exception {
 File sitemapFile = new File("src/test/resources/testSitemap.xml");
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.sourceDir.setValue("src/test/resources/testSource");
 lagoonGUI.targetURL.setValue("http://localhost:8080");
 File inputFile = new File("src/test/resources/testInputFile.xml");
 File outputFile = new File("src/test/resources/testOutputFile.xml");
 LagoonProcessor processor = mock(LagoonProcessor.class);
 when(processor.build(false)).thenThrow(new IOException());
 lagoonGUI.processor = processor;
 lagoonGUI.init();
 fail("Init should have failed with IOException");
 }

 @Test(expected = LagoonException.class)
 public void testInitWithLagoonException() throws Exception {
 File sitemapFile = new File("src/test/resources/testSitemap.xml");
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.sourceDir.setValue("src/test/resources/testSource");
 lagoonGUI.targetURL.setValue("http://localhost:8080");
 LagoonProcessor processor = mock(LagoonProcessor.class);
 when(processor.build(false)).thenThrow(new LagoonException("Test exception"));
 lagoonGUI.processor = processor;
 lagoonGUI.init();
 fail("Init should have failed with LagoonException");
 }
}