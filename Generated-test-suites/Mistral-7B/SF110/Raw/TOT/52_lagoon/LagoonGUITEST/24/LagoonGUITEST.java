package nu.staldal.lagoon;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LagoonGUITest {

 private LagoonGUI appFrame;

 @Before
 public void setUp() {
 appFrame = new LagoonGUI();
 }

 @Test
 public void testLoadProperties() {
 // Set up test properties and values
 String expectedSitemapFile = "testSitemap.xml";
 String expectedSourceDir = "testSourceDir/";
 String expectedTargetURL = "http://test.com";

 // Call method being tested
 appFrame.loadPropertyFile();

 // Validate result
 assertEquals(expectedSitemapFile, appFrame.sitemapFile.getValue());
 assertEquals(expectedSourceDir, appFrame.sourceDir.getValue());
 assertEquals(expectedTargetURL, appFrame.targetURL.getValue());
 }

 @Test
 public void testSavePropertiesWithPassword() {
 // Set up test properties and values
 String expectedSitemapFile = "testSitemap.xml";
 String expectedSourceDir = "testSourceDir/";
 String expectedTargetURL = "http://test.com";
 String expectedPassword = "testPassword";

 // Call method being tested
 appFrame.savePropertyFile();
 YesNoQueryDialog ynDialog = new YesNoQueryDialog(appFrame, "Save property file", "Save password in property file?", "Yes", "No");
 ynDialog.setResult(true);
 ynDialog.show();

 // Set password and call method being tested again
 appFrame.password = expectedPassword;
 appFrame.savePropertyFile();

 // Validate result
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
 fail("Failed to read property file: " + e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildWithNullProcessor() {
 appFrame.build(false);
 }

 @Test
 public void testBuildWithValidParameters() {
 // Set up test properties and values
 String expectedSitemapFile = "testSitemap.xml";
 String expectedSourceDir = "testSourceDir/";
 String expectedTargetURL = "http://test.com";

 // Call method being tested
 appFrame.sitemapFile.setValue(expectedSitemapFile);
 appFrame.sourceDir.setValue(expectedSourceDir);
 appFrame.targetURL.setValue(expectedTargetURL);
 appFrame.init();
 appFrame.build(false);

 // Validate result
 assertTrue(appFrame.statusLabel.getText().contains("Building..."));
 // Assuming that the build method prints progress to the progressArea, it's not visible in this test
 // For a more comprehensive test, you could use a mock TextAreaWriter to check the output of the build method
 }

 @Test(expected = LagoonException.class)
 public void testBuildWithInvalidSitemapFile() {
 // Set up test properties and values
 String expectedSitemapFile = "nonExistentFile.xml";
 String expectedSourceDir = "testSourceDir/";
 String expectedTargetURL = "http://test.com";

 // Call method being tested
 appFrame.sitemapFile.setValue(expectedSitemapFile);
 appFrame.sourceDir.setValue(expectedSourceDir);
 appFrame.targetURL.setValue(expectedTargetURL);
 appFrame.init();
 appFrame.build(false);

 // Validate error message
 assertNotNull(appFrame.progressArea.getText().trim());
 assertTrue(appFrame.progressArea.getText().contains("File not found"));
 }
}