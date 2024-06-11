package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.LagoonProcessor;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.xtree.Element;

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
 public void testMain() throws Exception {
 LagoonGUI.main(new String[]{"test.properties"});
 assertNotNull(appFrame.processor);
 }

 @Test
 public void testConstructor() {
 assertEquals("Lagoon", appFrame.getTitle());
 assertNotNull(appFrame.inputPanel);
 assertNotNull(appFrame.centerPanel);
 assertNotNull(appFrame.buttonPanel);
 assertNull(appFrame.processor);
 assertEquals(0L, appFrame.sitemapLastModified);
 }

 @Test
 public void testWindowListener() {
 appFrame.windowClosing(new WindowEvent(appFrame, 0));
 verify(appFrame).exit();
 }

 @Test
 public void testLoadButtonActionPerformed() {
 appFrame.loadButton.doClick();
 assertNotNull(appFrame.processor);
 }

 @Test
 public void testSaveButtonActionPerformed() {
 final File tempFile = File.createTempFile("lagoon", null);
 FileDialog propertyFileDialog = mock(FileDialog.class);
 when(propertyFileDialog.show()).thenReturn(true);
 appFrame.saveButton.doClick();
 appFrame.savePropertyFile();
 verify(appFrame).savePropertyFile();
 appFrame.assertPropertiesSaved(tempFile);
 }

 @Test(expected = LagoonException.class)
 public void testSaveButtonActionPerformedWithNoFileChosen() {
 appFrame.saveButton.doClick();
 appFrame.savePropertyFile();
 }

 @Test
 public void testBuildButtonActionPerformed() {
 appFrame.buildButton.doClick();
 assertNotNull(appFrame.processor);
 }

 @Test
 public void testBuildButtonActionPerformedWithNullProcessor() {
 appFrame.processor = null;
 appFrame.buildButton.doClick();
 assertNull(appFrame.processor);
 }

 @Test
 public void testBuildButtonActionPerformedWithForceTrue() {
 appFrame.processor = mock(LagoonProcessor.class);
 doAnswer(invocation -> {
 throw new IOException();
 }).when(appFrame.processor).build(true);
 appFrame.buildButton.doClick();
 appFrame.assertMessageDialogShown("Building error", "File not found: IOException");
 }

 @Test
 public void testBuildButtonActionPerformedWithForceFalse() {
 appFrame.processor = mock(LagoonProcessor.class);
 doAnswer(invocation -> {
 // Successful build
 return true;
 }).when(appFrame.processor).build(false);
 appFrame.buildButton.doClick();
 appFrame.assertMessageDialogShown("Build finished successfully", "");
 }

 @Test
 public void testInitialize() {
 File sitemapFile = new File("test.xml");
 when(appFrame.sitemapFile.getValue()).thenReturn(sitemapFile.getAbsolutePath());
 appFrame.init();
 assertNotNull(appFrame.processor);
 }

 @Test(expected = AuthenticationMissingException.class)
 public void testInitializeWithMissingPassword() {
 appFrame.init();
 }

 @Test(expected = AuthenticationException.class)
 public void testInitializeWithInvalidPassword() {
 appFrame.password = "wrongPassword";
 appFrame.init();
 }

 @Test
 public void testInitializeWithFileNotFound() {
 when(appFrame.sitemapFile.getValue()).thenReturn("fileNot Found.xml");
 appFrame.init();
 assertNull(appFrame.processor);
 }

 @Test(expected = IOException.class)
 public void testInitializeWithIOException() {
 when(appFrame.sitemapFile.getValue()).thenReturn("test.xml");
 doAnswer(invocation -> {
 throw new FileNotFoundException();
 }).when(appFrame).newFileInputStream(argThat(isA(File.class)));
 appFrame.init();
 }

 @Test(expected = LagoonException.class)
 public void testInitializeWithLagoonException() {
 when(appFrame.sitemapFile.getValue()).thenReturn("test.xml");
 doAnswer(invocation -> {
 throw new SAXException(new IOException("SAX Exception"));
 }).when(appFrame).newInputSource(argThat(isA(File.class)));
 appFrame.init();
 }

 @Test
 public void testExit() {
 appFrame.exit();
 verifyZeroInteractions(appFrame);
 System.exit(0);
 }

 @Test
 public void testLoadProperties() {
 final Properties properties = new Properties();
 properties.setProperty("sitemapFile", "test.xml");
 properties.setProperty("sourceDir", "testDir");
 properties.setProperty("targetURL", "testUrl");
 appFrame.loadProperties(new File("test.properties"));
 assertEquals("test.xml", appFrame.sitemapFile.getValue());
 assertEquals("testDir", appFrame.sourceDir.getValue());
 assertEquals("testUrl", appFrame.targetURL.getValue());
 }

 @Test
 public void testLoadPropertiesWithNoFileChosen() {
 appFrame.loadProperties(null);
 assertNull(appFrame.processor);
 }

 @Test
 public void testLoadPropertyFile() {
 final File tempFile = File.createTempFile("lagoon", null);
 FileDialog propertyFileDialog = mock(FileDialog.class);
 when(propertyFileDialog.show()).thenReturn(true);
 appFrame.loadPropertyFile();
 appFrame.loadProperties(tempFile);
 verify(appFrame).loadProperties(tempFile);
 }

 private void assertMessageDialogShown(String title, String message) {
 MessageDialog dialog = appFrame.getDialog("MessageDialog");
 assertEquals(title, dialog.getTitle());
 assertEquals(message, dialog.getMessage());
 verify(dialog).show();
 }

 private void assertPropertiesSaved(File file) {
 Properties properties = new Properties();
 try {
 FileInputStream fis = new FileInputStream(file);
 properties.load(fis);
 fis.close();
 assertEquals("test.xml", properties.getProperty("sitemapFile"));
 assertEquals("testDir", properties.getProperty("sourceDir"));
 assertEquals("testUrl", properties.getProperty("targetURL"));
 }
 catch (IOException e) {
 fail("Failed to read saved properties file: " + e.getMessage());
 }
}
}