package nu.staldal.lagoon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class LagoonGUITest {

 private LagoonGUI appFrame;

 @Before
 public void setUp() {
 appFrame = new LagoonGUI();
 appFrame.show(); // Need to show frame for some tests
 }

 @After
 public void tearDown() {
 appFrame.exit();
 }

 @Test
 public void testLoadProperties() {
 File propertyFile = new File("testPropertyFile.properties");
 appFrame.loadProperties(propertyFile);
 String expectedSitemapFile = "testSitemapFile.xml";
 String expectedSourceDir = "testSourceDir";
 String expectedTargetURL = "http://test.com";
 String expectedPassword = "testPassword";
 assertEquals(expectedSitemapFile, appFrame.sitemapFile.getValue());
 assertEquals(expectedSourceDir, appFrame.sourceDir.getValue());
 assertEquals(expectedTargetURL, appFrame.targetURL.getValue());
 assertNull(appFrame.password);
 }

 @Test
 public void testLoadPropertiesWithPassword() {
 File propertyFile = new File("testPropertyFileWithPassword.properties");
 appFrame.loadProperties(propertyFile);
 String expectedSitemapFile = "testSitemapFile.xml";
 String expectedSourceDir = "testSourceDir";
 String expectedTargetURL = "http://test.com";
 String expectedPassword = "testPassword";
 assertEquals(expectedSitemapFile, appFrame.sitemapFile.getValue());
 assertEquals(expectedSourceDir, appFrame.sourceDir.getValue());
 assertEquals(expectedTargetURL, appFrame.targetURL.getValue());
 assertNotNull(appFrame.password);
 assertEquals(expectedPassword, appFrame.password);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadPropertiesWithInvalidFile() {
 appFrame.loadProperties(null);
 }

 @Test
 public void testBuild_NoChanges() throws IOException {
 File sitemapFile = new File("testSitemapFile.xml");
 File sourceDir = new File("testSourceDir");
 File targetURL = new File("http://test.com");
 appFrame.init();
 appFrame.build(false);
 }

 @Test
 public void testBuild_WithSitemapChange() throws IOException, InterruptedException {
 File sitemapFile = new File("testSitemapFile1.xml");
 File sourceDir = new File("testSourceDir");
 File targetURL = new File("http://test.com");
 appFrame.init();
 File newSitemapFile = new File("testSitemapFile2.xml");
 newSitemapFile.createNewFile(); // Create new sitemap file
 appFrame.sitemapFile.setValue(newSitemapFile.getAbsolutePath());
 appFrame.build(false); // Build should be triggered because sitemap file changed
 }

 @Test
 public void testBuild_WithSourceDirChange() throws IOException, InterruptedException {
 File sitemapFile = new File("testSitemapFile.xml");
 File sourceDir = new File("testSourceDir1");
 File targetURL = new File("http://test.com");
 appFrame.init();
 File newSourceDir = new File("testSourceDir2");
 newSourceDir.mkdirs(); // Create new source dir
 appFrame.sourceDir.setValue(newSourceDir.getAbsolutePath());
 appFrame.build(false); // Build should be triggered because source dir changed
 }

 @Test
 public void testBuild_WithTargetURLChange() throws IOException, InterruptedException {
 File sitemapFile = new File("testSitemapFile.xml");
 File sourceDir = new File("testSourceDir");
 File targetURL = new File("http://test.com1");
 appFrame.init();
 File newTargetURL = new File("http://test.com2");
 appFrame.targetURL.setValue(newTargetURL.getAbsolutePath());
 appFrame.build(false); // Build should be triggered because target URL changed
 }

 @Test(expected = NullPointerException.class)
 public void testBuild_NullProcessor() {
 appFrame.processor = null;
 appFrame.build(false);
 }

 @Test(expected = IOException.class)
 public void testDestroy_IOException() throws IOException {
 appFrame.processor = new LagoonProcessor(targetURL, null, null, null, null, null);
 appFrame.processor.destroy();
 }

 @Test
 public void testExit() {
 appFrame.exit();
 // Frame should be closed
 assertTrue(appFrame.isDisposed());
 }
}