package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonException;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class LagoonGUITest {

 private LagoonGUI appFrame;

 @Before
 public void setUp() {
 appFrame = new LagoonGUI();
 appFrame.show();
 }

 @Test
 public void testLoadProperties() throws Exception {
 // Test loading properties from file
 final File propertyFile = new File("test.properties");
 final Properties prop = new Properties();
 prop.setProperty("sitemapFile", "testSitemap.xml");
 prop.setProperty("sourceDir", "testSourceDir");
 prop.setProperty("targetURL", "testTargetURL");
 prop.setProperty("password", "testPassword");
 final FileOutputStream fos = new FileOutputStream(propertyFile);
 prop.store(fos, "Lagoon properties");

 appFrame.loadPropertyFile();

 assertEquals("testSitemap.xml", appFrame.sitemapFile.getValue());
 assertEquals("testSourceDir", appFrame.sourceDir.getValue());
 assertEquals("testTargetURL", appFrame.targetURL.getValue());
 assertEquals("testPassword", appFrame.password);

 appFrame.savePropertyFile();

 // Test loading properties from null file
 appFrame.loadPropertyFile();
 assertNull(appFrame.sitemapFile.getValue());
 assertNull(appFrame.sourceDir.getValue());
 assertNull(appFrame.targetURL.getValue());
 assertNull(appFrame.password);

 // Test loading properties with missing file
 final File missingFile = new File("missingFile.properties");
 appFrame.loadPropertyFile(missingFile);
 assertNull(appFrame.sitemapFile.getValue());
 assertNull(appFrame.sourceDir.getValue());
 assertNull(appFrame.targetURL.getValue());
 assertNull(appFrame.password);
 }

 @Test
 public void testBuild() throws Exception {
 // Test successful build
 appFrame.sitemapFile.setValue("testSitemap.xml");
 appFrame.sourceDir.setValue("testSourceDir");
 appFrame.targetURL.setValue("testTargetURL");
 appFrame.buildButton.doClick();
 assertTrue(appFrame.statusLabel.getText().contains("Building..."));
 assertTrue(appFrame.progressArea.getText().contains("--- Build finished successfully"));

 // Test build with force flag
 appFrame.forceButton.doClick();
 assertTrue(appFrame.statusLabel.getText().contains("Building..."));
 assertTrue(appFrame.progressArea.getText().contains("--- Build finished successfully"));

 // Test build with invalid sitemap file
 appFrame.sitemapFile.setValue("invalidSitemap.xml");
 appFrame.sourceDir.setValue("testSourceDir");
 appFrame.targetURL.setValue("testTargetURL");
 appFrame.buildButton.doClick();
 assertTrue(appFrame.progressArea.getText().contains("--- Build finished with error(s)"));

 // Test build with missing source directory
 appFrame.sitemapFile.setValue("testSitemap.xml");
 appFrame.sourceDir.setValue(null);
 appFrame.targetURL.setValue("testTargetURL");
 appFrame.buildButton.doClick();
 assertTrue(appFrame.progressArea.getText().contains("--- Build finished with error(s)"));

 // Test build with missing target URL
 appFrame.sitemapFile.setValue("testSitemap.xml");
 appFrame.sourceDir.setValue("testSourceDir");
 appFrame.targetURL.setValue(null);
 appFrame.buildButton.doClick();
 assertTrue(appFrame.progressArea.getText().contains("--- Build finished with error(s)"));
}

 @Test
 public void testExit() throws Exception {
 appFrame.exitButton.doClick();
 assertEquals(0, System.exitCode());
 }
}