package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonException;

import java.io.File;
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
 }

 @After
 public void tearDown() {
 appFrame.exit();
 }

 @Test
 public void testInitialization() {
 assertNotNull(appFrame.getStatusLabel());
 assertEquals("Not initialized", appFrame.getStatusLabel().getText());
 }

 @Test
 public void testBuildWithoutSitemap() {
 appFrame.sitemapFile.setValue("");
 appFrame.sourceDir.setValue("src/test/resources/no_such_directory");
 appFrame.targetURL.setValue("http://localhost:8080");
 appFrame.buttonPanel.getComponent(5).doClick(); // Force build button

 assertNotNull(appFrame.getStatusLabel());
 assertEquals("Not initialized", appFrame.getStatusLabel().getText());
 }

 @Test
 public void testBuildWithInvalidSitemap() throws LagoonException {
 appFrame.sitemapFile.setValue("src/test/resources/invalid_sitemap.xml");
 appFrame.sourceDir.setValue("src/test/resources");
 appFrame.targetURL.setValue("http://localhost:8080");
 appFrame.buttonPanel.getComponent(4).doClick(); // Build button

 assertNotNull(appFrame.getStatusLabel());
 assertEquals("Building...", appFrame.getStatusLabel().getText());

 // Simulate successful build
 appFrame.progressArea.setText("--- Start build ---\nBuilding...");
 appFrame.statusLabel.setText("Building...");

 // Test for error message when build fails
 appFrame.buttonPanel.getComponent(5).doClick(); // Force build button

 assertNotNull(appFrame.getStatusLabel());
 assertEquals("Building error", appFrame.getStatusLabel().getText());
 }

 @Test
 public void testBuildWithValidSitemap() throws Exception {
 final File propertyFile = new File("src/test/resources/valid_properties.txt");
 Properties prop = new Properties();
 prop.load(new FileInputStream(propertyFile));

 appFrame.sitemapFile.setValue(getProperty(prop, "sitemapFile"));
 appFrame.sourceDir.setValue(getProperty(prop, "sourceDir"));
 appFrame.targetURL.setValue(getProperty(prop, "targetURL"));
 appFrame.password = prop.getProperty("password");

 appFrame.buttonPanel.getComponent(4).doClick(); // Build button

 assertNotNull(appFrame.getStatusLabel());
 assertEquals("Building...", appFrame.getStatusLabel().getText());

 // Simulate successful build
 appFrame.progressArea.setText("--- Start build ---\nBuilding...");
 appFrame.statusLabel.setText("Building...");

 // Test for success message when build is finished
 appFrame.buttonPanel.getComponent(5).doClick(); // Force build button

 assertNotNull(appFrame.getStatusLabel());
 assertEquals("Ready", appFrame.getStatusLabel().getText());
 }

 private String getProperty(Properties prop, String name) throws LagoonException {
 return LagoonGUI.getProperty(prop, name);
 }
}