package nu.staldal.lagoon.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import nu.staldal.lagoon.*;
import static nu.staldal.lagoon.LagoonGUI.*;

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
 public void testMain() {
 String[] args = new String[1];
 args[0] = "some-properties-file.xml";
 try {
 LagoonGUI.main(args);
 } catch (Exception e) {
 fail("Main method should not throw an exception");
 }
 }

 @Test
 public void testLoadProperties() {
 InputComponent sitemapFile = appFrame.sitemapFile;
 InputComponent sourceDir = appFrame.sourceDir;
 InputComponent targetURL = appFrame.targetURL;

 sitemapFile.setValue("src/test/resources/sitemap.xml");
 sourceDir.setValue("src/test/resources/input");
 targetURL.setValue("http://example.com");

 appFrame.loadProperties(new File("some-properties-file.xml"));

 assertEquals("sitemap.xml", sitemapFile.getValue());
 assertEquals("src/test/resources/input", sourceDir.getValue());
 assertEquals("http://example.com", targetURL.getValue());
 }

 @Test
 public void testSaveProperties() {
 FileDialog propertyFileDialog = mock(FileDialog.class);
 when(propertyFileDialog.getFile()).thenReturn("output.properties");
 when(propertyFileDialog.getDirectory()).thenReturn(new File(".").getAbsoluteFile());

 appFrame.savePropertyFile();

 File outputFile = new File("output.properties");
 assertTrue(outputFile.exists());
 try {
 FileInputStream fis = new FileInputStream(outputFile);
 Properties prop = new Properties();
 prop.load(fis);
 fis.close();

 assertEquals("sitemap.xml", prop.getProperty("sitemapFile"));
 assertEquals("src/test/resources/input", prop.getProperty("sourceDir"));
 equal("http://example.com", prop.getProperty("targetURL"));
 assertNull(prop.getProperty("password"));

 outputFile.delete();
 } catch (IOException e) {
 fail("Error reading saved properties file");
 }
 }

 @Test(expected = LagoonException.class)
 public void testSavePropertiesWithPassword() {
 FileDialog propertyFileDialog = mock(FileDialog.class);
 when(propertyFileDialog.getFile()).thenReturn("output.properties");
 when(propertyFileDialog.getDirectory()).thenReturn(new File(".").getAbsoluteFile());

 PasswordDialog passwordDialog = mock(PasswordDialog.class);
 when(passwordDialog.getPassword()).thenReturn("password123".toCharArray());

 appFrame.password = "password123";
 when(appFrame.showDialog(eq(PasswordDialog.class), anyString(), anyString(), anyString(), anyString())).thenReturn(passwordDialog);

 appFrame.savePropertyFile();
 }

 @Test
 public void testBuild() {
 assertEquals(false, appFrame.buildButton.isEnabled());
 appFrame.sitemapFile.setValue("src/test/resources/sitemap.xml");
 appFrame.sourceDir.setValue("src/test/resources/input");
 appFrame.targetURL.setValue("http://example.com");

 appFrame.init();
 assertEquals(true, appFrame.buildButton.isEnabled());

 appFrame.build(false);
 assertEquals("Building...", appFrame.statusLabel.getText());
 }

 @Test
 public void testBuildWithForce() {
 appFrame.sitemapFile.setValue("src/test/resources/sitemap.xml");
 appFrame.sourceDir.setValue("src/test/resources/input");
 appFrame.targetURL.setValue("http://example.com");
 appFrame.init();
 appFrame.build(true);
 assertEquals("Force build", appFrame.forceButton.getText());
 }

 @Test
 public void testExit() {
 appFrame.exit();
 assertFalse(appFrame.isDisplayable());
 }
}