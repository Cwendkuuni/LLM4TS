package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;

import java.awt.event.ActionEvent;
import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class LagoonGUITest {

 private LagoonGUI frame;

 @Before
 public void setUp() {
 frame = new LagoonGUI();
 frame.show(); // need to show the frame to be able to interact with the buttons
 }

 @Test
 public void testLoadProperties() {
 final String expectedSitemapFile = "testSitemap.xml";
 final String expectedSourceDir = "testSourceDir";
 final String expectedTargetURL = "http://test.com";
 final String expectedPassword = "testPassword";

 frame.sitemapFile.setValue(expectedSitemapFile);
 frame.sourceDir.setValue(expectedSourceDir);
 frame.targetURL.setValue(expectedTargetURL);
 frame.loadButton.doClick(); // simulate clicking the load button

 assertEquals(expectedSitemapFile, frame.sitemapFile.getValue());
 assertEquals(expectedSourceDir, frame.sourceDir.getValue());
 assertEquals(expectedTargetURL, frame.targetURL.getValue());
 assertNull(frame.password);
 }

 @Test
 public void testSaveProperties() {
 final String expectedSitemapFile = "testSitemap.xml";
 final String expectedSourceDir = "testSourceDir";
 final String expectedTargetURL = "http://test.com";
 final String expectedPassword = "testPassword";

 frame.sitemapFile.setValue(expectedSitemapFile);
 frame.sourceDir.setValue(expectedSourceDir);
 frame.targetURL.setValue(expectedTargetURL);
 frame.password = expectedPassword;
 frame.saveButton.doClick(); // simulate clicking the save button

 File propertyFile = new File("testPropertyFile.properties");
 try {
 Properties prop = new Properties();
 FileInputStream fis = new FileInputStream(propertyFile);
 prop.load(fis);
 fis.close();

 assertEquals(expectedSitemapFile, prop.getProperty("sitemapFile"));
 assertEquals(expectedSourceDir, prop.getProperty("sourceDir"));
 assertEquals(expectedTargetURL, prop.getProperty("targetURL"));
 assertEquals(expectedPassword, prop.getProperty("password"));
 }
 catch (IOException e) {
 fail("Failed to read property file");
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildWithNullProcessor() {
 frame.build(false); // simulate clicking the build button without initializing the processor
 }

 @Test
 public void testBuildWithSuccess() {
 final String expectedProgressAreaText = "--- Start build ---\n--- Build finished successfully 2000 ms ---";

 frame.init(); // initialize the processor
 frame.build(false); // simulate clicking the build button

 assertEquals(expectedProgressAreaText, frame.progressArea.getText());
 }

 @Test(expected = FileNotFoundException.class)
 public void testBuildWithMissingSitemapFile() {
 final File sm = new File("nonExistentSitemapFile.xml");
 frame.sitemapFile.setValue(sm.getAbsolutePath());
 frame.init(); // initialize the processor with the non-existent sitemap file
 frame.build(false); // simulate clicking the build button
 }

 @Test(expected = IOException.class)
 public void testBuildWithIOError() {
 final String expectedProgressAreaText = "--- Start build ---\n--- Build finished with error(s) in IO error ---";
 final File tempFile = File.createTempFile("test", null);
 frame.sitemapFile.setValue(tempFile.getAbsolutePath());
 frame.init(); // initialize the processor
 try {
 frame.build(false); // simulate clicking the build button
 fail("Build should have failed with an IO error");
 }
 catch (IOException e) {
 // expected
 }
 finally {
 tempFile.delete();
 }
 assertEquals(expectedProgressAreaText, frame.progressArea.getText());
 }

 @Test(expected = LagoonException.class)
 public void testBuildWithInvalidSitemap() {
 final String expectedProgressAreaText = "--- Start build ---\n--- Build finished with error(s) in LagoonException: Property sitemapFile not specified ---";
 frame.sitemapFile.setValue("");
 frame.init(); // initialize the processor with an empty sitemap file path
 frame.build(false); // simulate clicking the build button
 }

 @Test(expected = AuthenticationMissingException.class)
 public void testBuildWithMissingPassword() {
 final String expectedProgressAreaText = "--- Start build ---\n--- Build finished with error(s) in AuthenticationMissingException: Password needed to access target ---";
 frame.password = null;
 frame.init(); // initialize the processor with a missing password
 frame.build(false); // simulate clicking the build button
 }

 @Test(expected = AuthenticationException.class)
 public void testBuildWithInvalidPassword() {
 final String expectedProgressAreaText = "--- Start build ---\n--- Build finished with error(s) in AuthenticationException: Invalid password, try again ---";
 frame.password = "invalidPassword";
 frame.init(); // initialize the processor with an invalid password
 frame.build(false); // simulate clicking the build button
 }
}