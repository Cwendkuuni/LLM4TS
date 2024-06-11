package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.MessageDialog;
import nu.staldal.lagoon.core.YesNoQueryDialog;
import nu.staldal.lagoon.gui.InputComponent;
import nu.staldal.lagoon.gui.LagoonGUI;
import nu.staldal.lagoon.gui.PasswordDialog;
import nu.staldal.lagoon.gui.TextAreaWriter;
import nu.staldal.xtree.SAXException;
import nu.staldal.xtree.TreeBuilder;

public class LagoonGUITest {

 private LagoonGUI lagoonGUI;

 @Before
 public void setUp() {
 lagoonGUI = new LagoonGUI();
 }

 @Test
 public void testMain() throws Exception {
 LagoonGUI.main(new String[0]);
 assertNotNull(lagoonGUI.getInputPanel());
 }

 @Test
 public void testWindowEvents() {
 lagoonGUI.addWindowListener(e -> {});
 lagoonGUI.windowOpened(new WindowEvent(lagoonGUI, WindowEvent.WINDOW_OPENED));
 lagoonGUI.windowClosing(new WindowEvent(lagoonGUI, WindowEvent.WINDOW_CLOSING));
 }

 @Test
 public void testBuildButtonActionPerformed() {
 lagoonGUI.buildButton.setEnabled(true);
 lagoonGUI.buildButton.doClick();
 assertFalse(lagoonGUI.loadButton.isEnabled());
 assertFalse(lagoonGUI.saveButton.isEnabled());
 assertFalse(lagoonGUI.buildButton.isEnabled());
 assertFalse(lagoonGUI.forceButton.isEnabled());
 assertFalse(lagoonGUI.sitemapFile.isEnabled());
 assertFalse(lagoonGUI.sourceDir.isEnabled());
 assertFalse(lagoonGUI.targetURL.isEnabled());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidSitemapFile() {
 lagoonGUI.sitemapFile.setValue("invalidFile.txt");
 lagoonGUI.build(false);
 }

 @Test
 public void testBuildWithSuccess() {
 lagoonGUI.sitemapFile.setValue("validSitemap.xml");
 lagoonGUI.sourceDir.setValue("sourceDirectory");
 lagoonGUI.targetURL.setValue("http://target.com");
 LagoonProcessor processor = mock(LagoonProcessor.class);
 when(processor.build(eq(false))).thenReturn(true);
 lagoonGUI.processor = processor;
 lagoonGUI.build(false);
 assertEquals("Build finished successfully", lagoonGUI.progressArea.getText().split("\n")[0]);
 }

 @Test(expected = FileNotFoundException.class)
 public void testBuildWithFileNotFoundException() {
 lagoonGUI.sitemapFile.setValue("invalidFile.xml");
 lagoonGUI.sourceDir.setValue("sourceDirectory");
 lagoonGUI.targetURL.setValue("http://target.com");
 LagoonProcessor processor = mock(LagoonProcessor.class);
 when(processor.build(eq(false))).thenThrow(new FileNotFoundException("File not found"));
 lagoonGUI.processor = processor;
 lagoonGUI.build(false);
 }

 @Test(expected = IOException.class)
 public void testBuildWithIOException() {
 lagoonGUI.sitemapFile.setValue("validSitemap.xml");
 lagoonGUI.sourceDir.setValue("sourceDirectory");
 lagoonGUI.targetURL.setValue("http://target.com");
 LagoonProcessor processor = mock(LagoonProcessor.class);
 when(processor.build(eq(false))).thenThrow(new IOException("IO Exception"));
 lagoonGUI.processor = processor;
 lagoonGUI.build(false);
 }

 @Test
 public void testLoadProperties() {
 Properties properties = new Properties();
 properties.setProperty("sitemapFile", "sitemap.xml");
 properties.setProperty("sourceDir", "sourceDirectory");
 properties.setProperty("targetURL", "http://target.com");
 lagoonGUI.loadProperties(new File("testProperties.txt"));
 lagoonGUI.loadProperties(new File("."));
 assertEquals("sitemap.xml", lagoonGUI.sitemapFile.getValue());
 assertEquals("sourceDirectory", lagoonGUI.sourceDir.getValue());
 assertEquals("http://target.com", lagoonGUI.targetURL.getValue());
 }

 @Test
 public void testSaveProperties() {
 lagoonGUI.sitemapFile.setValue("sitemap.xml");
 lagoonGUI.sourceDir.setValue("sourceDirectory");
 lagoonGUI.targetURL.setValue("http://target.com");
 lagoonGUI.password = "password";
 FileDialog propertyFileDialog = mock(FileDialog.class);
 when(propertyFileDialog.getFile()).thenReturn("testProperties.txt");
 when(propertyFileDialog.getDirectory()).thenReturn(".");
 File propertyFile = mock(File.class);
 when(propertyFile.getAbsolutePath()).thenReturn("testProperties.txt");
 lagoonGUI.savePropertyFile();
 assertTrue(lagoonGUI.statusLabel.getText().contains("Properties saved"));
 }

}