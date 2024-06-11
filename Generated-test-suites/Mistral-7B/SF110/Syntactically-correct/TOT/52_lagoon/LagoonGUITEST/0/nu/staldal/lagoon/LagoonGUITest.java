import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;

public class LagoonGUITest {

 private LagoonGUI appFrame;

 @Before
 public void setUp() {
 appFrame = new LagoonGUI();
 }

 @Test
 public void testLoadProperties() {
 // Given a valid property file
 String propertyFile = "test.properties";
 Properties prop = new Properties();
 prop.setProperty("sitemapFile", "test.xml");
 prop.setProperty("sourceDir", "testSrc");
 prop.setProperty("targetURL", "http://test.com");
 try (FileOutputStream fos = new FileOutputStream(propertyFile)) {
 prop.store(fos, "Lagoon properties");
 }

 // When loading the properties
 appFrame.loadPropertyFile();

 // Then the values should be loaded correctly
 assertEquals("test.xml", appFrame.sitemapFile.getValue());
 assertEquals("testSrc", appFrame.sourceDir.getValue());
 assertEquals("http://test.com", appFrame.targetURL.getValue());

 // And the password should be null if not specified
 assertNull(appFrame.password);

 // Clean up
 File file = new File(propertyFile);
 file.delete();
 }

 @Test
 public void testSaveProperties() {
 // Given a valid property file
 appFrame.sitemapFile.setValue("test.xml");
 appFrame.sourceDir.setValue("testSrc");
 appFrame.targetURL.setValue("http://test.com");
 appFrame.password = "testPassword";

 // When saving the properties
 appFrame.savePropertyFile();

 // Then the property file should be saved correctly
 File propertyFile = new File("test.properties");
 assertTrue(propertyFile.exists());
 String content = "";
 try {
 content = new String(Files.readAllBytes(propertyFile.toPath()));
 }
 catch (IOException e) {
 e.printStackTrace();
 fail("Failed to read file.");
 }
 assertTrue(content.contains("sitemapFile=test.xml"));
 assertTrue(content.contains("sourceDir=testSrc"));
 assertTrue(content.contains("targetURL=http://test.com"));
 assertTrue(content.contains("password=" + appFrame.password));

 // Clean up
 propertyFile.delete();
 }

 @Test(expected = LagoonException.class)
 public void testLoadInvalidPropertyFile() {
 // Given an invalid property file
 appFrame.loadPropertyFile();

 // When loading the properties
 // Then an exception should be thrown
 }

 @Test(expected = IOException.class)
 public void testSavePropertyFileFailure() {
 // Given a failure to save the property file
 FileOutputStream fos = new FileOutputStream(new File("test.properties")) {
 @Override
 public void write(int b) throws IOException {
 throw new IOException();
 }
 };
 appFrame.savePropertyFile();

 // Then an exception should be thrown
 }
}