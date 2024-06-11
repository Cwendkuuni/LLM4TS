import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.xtree.Element;

public class LagoonGUITest {

 private LagoonGUI appFrame;

 @Before
 public void setUp() {
 appFrame = new LagoonGUI();
 appFrame.show(); // Need to show the frame to interact with the UI
 }

 @After
 public void tearDown() {
 appFrame.exit();
 }

 @Test
 public void testBuild_ValidInput_Success() {
 // Set up input values
 appFrame.sitemapFile.setValue("test/input/sitemap.xml");
 appFrame.sourceDir.setValue("test/input/sourceDir");
 appFrame.targetURL.setValue("http://test.com");

 // Call build method
 appFrame.buildButton.doClick();

 // Check status label for success message
 assertEquals("Build finished successfully", appFrame.statusLabel.getText());
 }

 @Test
 public void testBuild_InvalidSitemapFile_Error() {
 // Set up input values
 appFrame.sitemapFile.setValue("test/input/invalidFile.txt");
 appFrame.sourceDir.setValue("test/input/sourceDir");
 appFrame.targetURL.setValue("http://test.com");

 // Call build method
 appFrame.buildButton.doClick();

 // Check status label for error message
 assertTrue(appFrame.statusLabel.getText().contains("Building error"));
 }

 @Test
 public void testBuild_NullProcessor_Error() {
 // Set up input values
 appFrame.sitemapFile.setValue(null);
 appFrame.sourceDir.setValue("test/input/sourceDir");
 appFrame.targetURL.setValue("http://test.com");

 // Call build method
 appFrame.buildButton.doClick();

 // Check status label for error message
 assertTrue(appFrame.statusLabel.getText().contains("Not initialized"));
 }

 @Test
 public void testBuild_FileNotFoundException_Error() {
 // Set up input values
 appFrame.sitemapFile.setValue("test/input/nonexistentFile.xml");
 appFrame.sourceDir.setValue("test/input/sourceDir");
 appFrame.targetURL.setValue("http://test.com");

 // Call build method
 appFrame.buildButton.doClick();

 // Check status label for error message
 assertTrue(appFrame.statusLabel.getText().contains("File not found"));
 }

 @Test
 public void testInit_ValidInput_Success() {
 // Set up input values
 appFrame.sitemapFile.setValue("test/input/sitemap.xml");
 appFrame.sourceDir.setValue("test/input/sourceDir");
 appFrame.targetURL.setValue("http://test.com");

 // Call init method
 appFrame.buildButton.doClick();

 // Check status label for success message
 assertEquals("Initialized", appFrame.statusLabel.getText());
 }

 @Test
 public void testInit_InvalidSitemapFile_Error() {
 // Set up input values
 appFrame.sitemapFile.setValue("test/input/invalidFile.txt");
 appFrame.sourceDir.setValue("test/input/sourceDir");
 appFrame.targetURL.setValue("http://test.com");

 // Call init method
 appFrame.buildButton.doClick();

 // Check status label for error message
 assertTrue(appFrame.statusLabel.getText().contains("SaxParseException"));
 }

 @Test
 public void testInit_NullProcessor_Error() {
 // Set up input values
 appFrame.sitemapFile.setValue("test/input/sitemap.xml");
 appFrame.sourceDir.setValue("test/input/sourceDir");
 appFrame.targetURL.setValue("http://test.com");
 appFrame.processor = null;

 // Call init method
 appFrame.buildButton.doClick();

 // Check status label for error message
 assertTrue(appFrame.statusLabel.getText().contains("Not initialized"));
 }

 @Test
 public void testLoadPropertiesFile_ValidFile_Success() {
 // Set up input values
 appFrame.sitemapFile.setValue("test/input/sitemap.xml");
 appFrame.sourceDir.setValue("test/input/sourceDir");
 appFrame.targetURL.setValue("http://test.com");

 // Call loadPropertiesFile method
 appFrame.loadButton.doClick();

 // Check input values for correct property values
 assertEquals("test/input/sitemap.xml", appFrame.sitemapFile.getValue());
 assertEquals("test/input/sourceDir", appFrame.sourceDir.getValue());
 assertEquals("http://test.com", appFrame.targetURL.getValue());
 }

 @Test
 public void testLoadPropertiesFile_InvalidFile_Error() {
 // Set up input values
 appFrame.sitemapFile.setValue("test/input/invalidFile.txt");
 appFrame.sourceDir.setValue("test/input/sourceDir");
 appFrame.targetURL.setValue("http://test.com");

 // Call loadPropertiesFile method
 appFrame.loadButton.doClick();

 // Check status label for error message
 assertTrue(appFrame.statusLabel.getText().contains("Error reading property file"));
 }

 @Test
 public void testSavePropertiesFile_ValidFile_Success() {
 // Set up input values
 appFrame.sitemapFile.setValue("test/input/sitemap.xml");
 appFrame.sourceDir.setValue("test/input/sourceDir");
 appFrame.targetURL.setValue("http://test.com");

 // Call savePropertiesFile method
 appFrame.saveButton.doClick();

 // Check property file contents for correct values
 Element sitemapTree = TreeBuilder.parseXML(new File("test/output/propertyFile.xml").toURL().openStream(), false);
 assertEquals("test/input/sitemap.xml", sitemapTree.getElementsByTagName("sitemapFile").item(0).getTextContent());
 assertEquals("test/input/sourceDir", sitemapTree.getElementsByTagName("sourceDir").item(0).getTextContent());
 assertEquals("http://test.com", sitemapTree.getElementsByTagName("targetURL").item(0).getTextContent());
 }

 @Test
 public void testSavePropertiesFile_InvalidFile_Error() {
 // Set up input values
 appFrame.sitemapFile.setValue("test/input/sitemap.xml");
 appFrame.sourceDir.setValue("test/input/sourceDir");
 appFrame.targetURL.setValue("http://test.com");

 // Call savePropertiesFile method
 appFrame.saveButton.doClick();

 // Create a non-existent file to save to
 File propertyFile = new File("test/output/propertyFile.txt");

 // Check status label for error message
 assertTrue(appFrame.statusLabel.getText().contains("Could not open the file for writing"));
 assertFalse(propertyFile.exists());
 }
}