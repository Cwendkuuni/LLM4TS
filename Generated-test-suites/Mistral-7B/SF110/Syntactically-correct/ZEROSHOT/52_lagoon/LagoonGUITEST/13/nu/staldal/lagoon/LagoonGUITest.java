package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.LagoonProcessor;
import nu.staldal.xtree.SAXException;
import nu.staldal.xtree.XMLStreamReader;

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
 public void testWindowListener() {
 lagoonGUI.windowOpened(new WindowEvent(lagoonGUI, WindowEvent.WINDOW_OPENED));
 }

 @Test
 public void testLoadProperties() {
 final File propertyFile = new File("test.properties");
 lagoonGUI.loadProperties(propertyFile);
 assertEquals("expectedValue", lagoonGUI.getSitemapFile().getValue());
 assertEquals("expectedValue", lagoonGUI.getSourceDir().getValue());
 assertEquals("expectedValue", lagoonGUI.getTargetURL().getValue());
 }

 @Test
 public void testSaveProperties() throws IOException {
 final File propertyFile = new File("test.properties");
 lagoonGUI.saveProperties();
 assertTrue(propertyFile.exists());
 }

 @Test
 public void testBuild() throws Exception {
 final File sitemapFile = new File("sitemap.xml");
 sitemapFile.createNewFile();
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.loadProperties(new File("test.properties"));
 LagoonProcessor processor = mock(LagoonProcessor.class);
 when(processor.build(eq(false))).thenReturn(true);
 lagoonGUI.processor = processor;

 lagoonGUI.build(false);
 assertTrue(lagoonGUI.getStatusLabel().getText().contains("Building..."));
 verify(processor).build(eq(false));
 }

 @Test(expected = LagoonException.class)
 public void testInitWithInvalidSitemap() throws Exception {
 final File sitemapFile = new File("invalidSitemap.xml");
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.loadProperties(new File("test.properties"));
 lagoonGUI.init();
 fail("Initialization should have thrown LagoonException");
 }

 @Test(expected = FileNotFoundException.class)
 public void testInitWithMissingSitemapFile() throws Exception {
 lagoonGUI.sitemapFile.setValue("nonexistentFile.xml");
 lagoonGUI.loadProperties(new File("test.properties"));
 lagoonGUI.init();
 fail("Initialization should have thrown FileNotFoundException");
 }

 @Test(expected = IOException.class)
 public void testInitWithIOException() throws Exception {
 final File sitemapFile = new File("sitemap.xml");
 sitemapFile.createNewFile();
 XMLStreamReader xmlStreamReader = mock(XMLStreamReader.class);
 when(xmlStreamReader.getEventType()).thenThrow(new IOException());
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 when(xmlStreamReader.parse()).thenReturn(xmlStreamReader);
 lagoonGUI.loadProperties(new File("test.properties"));
 lagoonGUI.init();
 fail("Initialization should have thrown IOException");
 }

 @Test(expected = AuthenticationMissingException.class)
 public void testInitWithMissingPassword() throws Exception {
 final File sitemapFile = new File("sitemap.xml");
 sitemapFile.createNewFile();
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.loadProperties(new File("test.properties"));
 lagoonGUI.init();
 fail("Initialization should have thrown AuthenticationMissingException");
 }

 @Test(expected = AuthenticationException.class)
 public void testInitWithInvalidPassword() throws Exception {
 final File sitemapFile = new File("sitemap.xml");
 sitemapFile.createNewFile();
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.loadProperties(new File("test.properties"));
 final PasswordDialog passwordDialog = mock(PasswordDialog.class);
 when(passwordDialog.getPassword()).thenReturn("invalidPassword".toCharArray());
 when(passwordDialog.show()).thenReturn(true);
 when(lagoonGUI.showDialog(eq(PasswordDialog.class))).thenReturn(passwordDialog);
 lagoonGUI.init();
 fail("Initialization should have thrown AuthenticationException");
 }

 @Test
 public void testExit() {
 lagoonGUI.exit();
 assertFalse(lagoonGUI.isDisplayable());
 }

}