package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.*;
import org.junit.Before;
import org.junit.Test;

public class LagoonGUITest {

 private LagoonGUI lagoonGUI;

 @Before
 public void setUp() {
 lagoonGUI = new LagoonGUI();
 }

 @Test
 public void testInitializeComponents() {
 // Test if components are initialized correctly
 assertNotNull(lagoonGUI.getButtonPanel());
 assertNotNull(lagoonGUI.getInputPanel());
 assertNotNull(lagoonGUI.getCenterPanel());
 assertNotNull(lagoonGUI.getLoadButton());
 assertNotNull(lagoonGUI.getSaveButton());
 assertNotNull(lagoonGUI.getBuildButton());
 assertNotNull(lagoonGUI.getForceButton());
 assertNotNull(lagoonGUI.getExitButton());
 assertNotNull(lagoonGUI.getSitemapFile());
 assertNotNull(lagoonGUI.getSourceDir());
 assertNotNull(lagoonGUI.getTargetURL());
 assertNotNull(lagoonGUI.getStatusLabel());
 assertNotNull(lagoonGUI.getProgressArea());
 }

 @Test
 public void testLoadPropertiesFile() {
 // Test loading properties file
 lagoonGUI.loadPropertyFile();
 assertNotNull(lagoonGUI.getSitemapFile().getValue());
 assertNotNull(lagoonGUI.getSourceDir().getValue());
 assertNotNull(lagoonGUI.getTargetURL().getValue());
 }

 @Test
 public void testSavePropertiesFile() {
 // Test saving properties file
 lagoonGUI.savePropertyFile();
 assertNotNull(lagoonGUI.getSitemapFile().getValue());
 assertNotNull(lagoonGUI.getSourceDir().getValue());
 assertNotNull(lagoonGUI.getTargetURL().getValue());
 // TODO: Add test for saving password in property file
 }

 @Test(expected = LagoonException.class)
 public void testLoadPropertyFileWithInvalidFile() {
 // Test loading property file with invalid file
 lagoonGUI.loadPropertyFile(new File("invalidFile.prop"));
 fail("Load property file should have thrown LagoonException");
 }

 @Test
 public void testBuildWithValidProperties() {
 // Test building with valid properties
 lagoonGUI.sitemapFile.setValue("validSitemap.xml");
 lagoonGUI.sourceDir.setValue("validSourceDir");
 lagoonGUI.targetURL.setValue("validTargetURL");
 lagoonGUI.build(false);
 // TODO: Add test for checking if build was successful
 }

 @Test
 public void testBuildWithInvalidProperties() {
 // Test building with invalid properties
 lagoonGUI.sitemapFile.setValue("invalidSitemap.xml");
 lagoonGUI.sourceDir.setValue("invalidSourceDir");
 lagoonGUI.targetURL.setValue("invalidTargetURL");
 lagoonGUI.build(false);
 // TODO: Add test for checking if build failed with error message
 }

 @Test
 public void testForceBuildWithValidProperties() {
 // Test force build with valid properties
 lagoonGUI.sitemapFile.setValue("validSitemap.xml");
 lagoonGUI.sourceDir.setValue("validSourceDir");
 lagoonGUI.targetURL.setValue("validTargetURL");
 lagoonGUI.build(true);
 // TODO: Add test for checking if force build was successful
 }

 @Test
 public void testForceBuildWithInvalidProperties() {
 // Test force build with invalid properties
 lagoonGUI.sitemapFile.setValue("invalidSitemap.xml");
 lagoonGUI.sourceDir.setValue("invalidSourceDir");
 lagoonGUI.targetURL.setValue("invalidTargetURL");
 lagoonGUI.build(true);
 // TODO: Add test for checking if force build failed with error message
 }

 @Test
 public void testExit() {
 // Test exiting the application
 lagoonGUI.exit();
 // TODO: Add test for checking if application closed properly
 }
}