package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Properties;

public class LagoonGUITest {

 private LagoonGUI appFrame;

 @Before
 public void setUp() {
 appFrame = new LagoonGUI();
 appFrame.show();
 }

 @Test
 public void testConstructor() {
 assertNotNull(appFrame);
 assertEquals("Lagoon", appFrame.getTitle());
 assertNull(appFrame.getProcessor());
 assertEquals(0L, appFrame.getSitemapLastModified());
 }

 @Test
 public void testLoadProperties() {
 File propertyFile = new File("testProperties.txt");
 Properties prop = new Properties();
 prop.setProperty("sitemapFile", "testSitemap.xml");
 prop.setProperty("sourceDir", "testSourceDir");
 prop.setProperty("targetURL", "testTargetURL.com");
 prop.setProperty("password", "testPassword");

 appFrame.loadProperties(propertyFile);
 assertEquals("testSitemap.xml", appFrame.getSitemapFile().getValue());
 assertEquals("testSourceDir", appFrame.getSourceDir().getValue());
 assertEquals("testTargetURL.com", appFrame.getTargetURL().getValue());
 assertEquals("testPassword", appFrame.getPassword());
 }

 @Test
 public void testSaveProperties() {
 File propertyFile = new File("testProperties.txt");
 appFrame.savePropertyFile();
 assertTrue(propertyFile.exists());
 Properties prop = new Properties();
 try {
 FileInputStream fis = new FileInputStream(propertyFile);
 prop.load(fis);
 fis.close();
 } catch (IOException e) {
 fail("Error reading property file");
 }
 assertEquals("testSitemap.xml", prop.getProperty("sitemapFile"));
 assertEquals("testSourceDir", prop.getProperty("sourceDir"));
 assertEquals("testTargetURL.com", prop.getProperty("targetURL"));
 assertEquals("testPassword", prop.getProperty("password"));
 }

 @Test
 public void testBuild() throws Exception {
 File sitemapFile = new File("testSitemap.xml");
 File sourceDir = new File("testSourceDir");
 File targetURLFile = new File("testTargetURL.com");
 appFrame.setSitemapFile(sitemapFile);
 appFrame.setSourceDir(sourceDir);
 appFrame.setTargetURL(targetURLFile);
 appFrame.build(false);
 assertNotNull(appFrame.getProcessor());
 }

 @Test(expected = LagoonException.class)
 public void testBuildWithError() throws Exception {
 File sitemapFile = new File("testSitemapError.xml");
 File sourceDir = new File("testSourceDir");
 File targetURLFile = new File("testTargetURL.com");
 appFrame.setSitemapFile(sitemapFile);
 appFrame.setSourceDir(sourceDir);
 appFrame.setTargetURL(targetURLFile);
 appFrame.build(false);
 fail("Build should have failed with error");
 }

 @Test
 public void testExit() {
 appFrame.exit();
 assertTrue(appFrame.isDisplayable());
 fail("Exit should have closed the application");
 }

 @Test
 public void testWindowEvents() {
 WindowEvent windowEvent = new WindowEvent(appFrame, WindowEvent.WINDOW_CLOSING);
 appFrame.windowClosing(windowEvent);
 assertTrue(appFrame.isDisposed());
 }
}