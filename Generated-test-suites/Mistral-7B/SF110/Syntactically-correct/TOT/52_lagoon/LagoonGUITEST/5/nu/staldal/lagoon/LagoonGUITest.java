package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.xtree.Element;

public class LagoonGUITest {

 private LagoonGUI gui;

 @Before
 public void setUp() {
 try {
 gui = new LagoonGUI();
 gui.show();
 }
 catch (Exception e) {
 e.printStackTrace();
 fail("Failed to initialize GUI: " + e.getMessage());
 }
 }

 @After
 public void tearDown() {
 if (gui != null) {
 gui.exit();
 }
 }

 @Test
 public void testLoadProperties() {
 File propertyFile = new File("test.properties");
 Properties prop = new Properties();
 try {
 prop.load(new FileInputStream(propertyFile));
 }
 catch (IOException e) {
 e.printStackTrace();
 fail("Failed to load test properties file: " + e.getMessage());
 }
 String expectedSitemapFile = "testSitemap.xml";
 String expectedSourceDir = "testSrcDir";
 String expectedTargetURL = "http://test.com";
 String expectedPassword = "testPassword";
 assertEquals(expectedSitemapFile, gui.sitemapFile.getValue());
 assertEquals(expectedSourceDir, gui.sourceDir.getValue());
 assertEquals(expectedTargetURL, gui.targetURL.getValue());
 assertNull(gui.password);
 if (prop.getProperty("password") != null) {
 gui.password = prop.getProperty("password");
 try {
 gui.init();
 assertEquals(expectedPassword, gui.password);
 }
 catch (Exception e) {
 e.printStackTrace();
 fail("Failed to initialize GUI with test password: " + e.getMessage());
 }
 finally {
 gui.password = null;
 }
 }
}

 @Test
 public void testBuild() {
 File sitemapFile = new File("testSitemap.xml");
 File sourceDir = new File("testSrcDir");
 String targetURL = "http://test.com";
 String password = "testPassword";
 Element expectedTree = new XmlTreeBuilder().buildTreeFromString("<sitemap><loc><url>http://test.com/test1</url></loc></sitemap>");
 try {
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 gui.sourceDir.setValue(sourceDir.getAbsolutePath());
 gui.targetURL.setValue(targetURL);
 gui.password = password;
 gui.init();
 gui.build(false);
 assertEquals("--- Build finished successfully 2 ms ---", gui.progressArea.getText());
 }
 catch (Exception e) {
 e.printStackTrace();
 fail("Failed to build successfully: " + e.getMessage());
 }

 try {
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 gui.sourceDir.setValue(sourceDir.getAbsolutePath());
 gui.targetURL.setValue(targetURL);
 gui.password = password;
 gui.init();
 File outputFile = new File("testOutput.html");
 gui.build(true);
 assertEquals(expectedTree.toString(), new String(new FileReader(outputFile)));
 }
 catch (Exception e) {
 e.printStackTrace();
 fail("Failed to build with force: " + e.getMessage());
 }
 finally {
 sitemapFile.delete();
 sourceDir.delete();
 outputFile.delete();
 }
}

 @Test
 public void testWindowEvents() {
 gui.addWindowListener(new WindowAdapter() {
 @Override
 public void windowClosing(WindowEvent e) {
 assertEquals(WindowEvent.WINDOW_CLOSING, e.getID());
 fail("GUI should not be closing");
 }
 });
 gui.exitButton.doClick();
 gui.dispatchEvent(new ActionEvent(gui.exitButton, ActionEvent.ACTION_PERFORMED, ""));
 gui.dispatchEvent(new WindowEvent(gui, WindowEvent.WINDOW_CLOSING));
}

 @Test(expected = LagoonException.class)
 public void testInitError() {
 File sitemapFile = new File("testSitemapError.xml");
 try {
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 gui.init();
 fail("Expected init error");
 }
 catch (LagoonException e) {
 assertEquals("Error(s) occured while building", e.getMessage());
 }
 finally {
 sitemapFile.delete();
 }
}

 @Test(expected = FileNotFoundException.class)
 public void testLoadError() {
 try { gui.loadButton.doClick(); fail("Expected load error"); }
 catch (FileNotFoundException e) { assertEquals("File not found: test.properties", e.getMessage()); }
}

 @Test(expected = IOException.class)
 public void testBuildError() throws IOException {
 try {
 gui.sitemapFile.setValue("testSitemap.xml");
 gui.sourceDir.setValue("testSrcDir");
 gui.targetURL.setValue("http://test.com");
 gui.init();
 gui.build(false);
 fail("Expected build error");
 }
 catch (IOException e) { assertEquals("Fatal Building error: File not found: test.html", e.getMessage()); }
}
}