package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Properties;

import org.junit.Test;

import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.test.TestHelper;
import nu.staldal.xtree.TreeBuilder;
import nu.staldal.xtree.XMLInputStream;

public class LagoonGUITest {

 private LagoonGUI lagoonGUI;

 @Test
 public void testConstructor() {
 lagoonGUI = new LagoonGUI();
 assertNotNull(lagoonGUI);
 }

 @Test
 public void testMainMethod() {
 final String[] args = new String[0];
 try {
 LagoonGUI.main(args);
 } catch (Exception e) {
 fail("Main method should not throw an exception");
 }
 }

 @Test
 public void testLoadButtonActionPerformed() {
 lagoonGUI = spy(new LagoonGUI());
 doCallRealMethod().when(lagoonGUI).loadPropertyFile();
 lagoonGUI.loadButton.addActionListener(mock(ActionListener.class));
 lagoonGUI.loadButton.actionPerformed(mock(ActionEvent.class));
 verify(lagoonGUI).loadPropertyFile();
 }

 @Test
 public void testSaveButtonActionPerformed() {
 lagoonGUI = spy(new LagoonGUI());
 doCallRealMethod().when(lagoonGUI).savePropertyFile();
 lagoonGUI.saveButton.addActionListener(mock(ActionListener.class));
 lagoonGUI.saveButton.actionPerformed(mock(ActionEvent.class));
 verify(lagoonGUI).savePropertyFile();
 }

 @Test
 public void testBuildButtonActionPerformed() throws Exception {
 lagoonGUI = new LagoonGUI();
 lagoonGUI.sitemapFile.setValue("testSitemap.xml");
 lagoonGUI.sourceDir.setValue("testSourceDir");
 lagoonGUI.targetURL.setValue("testTargetURL");
 final File testSitemapFile = new File("testSitemap.xml");
 final Properties properties = new Properties();
 final XMLInputStream xmlInput = new XMLInputStream(new FileInputStream(testSitemapFile));
 final Element sitemapTree = TreeBuilder.parseXML(xmlInput, false).getDocumentElement();
 lagoonGUI.init();
 lagoonGUI.buildButton.addActionListener(new ActionListener() {
 @Override
 public void actionPerformed(final ActionEvent e) {
 lagoonGUI.build(false);
 }
 });
 lagoonGUI.buildButton.actionPerformed(new ActionEvent(lagoonGUI, ActionEvent.ACTION_PERFORMED, "BuildButton"));
 // Verify the build method was called with the correct arguments
 verify(lagoonGUI).build(eq(false));
 // Verify that the processor was initialized and the build method was called
 assertEquals(true, lagoonGUI.processor != null);
 // Verify that the statusLabel and progressArea were updated
 assertEquals("Building...", lagoonGUI.statusLabel.getText());
 assertFalse(lagoonGUI.progressArea.getText().isEmpty());
 }

 @Test(expected = LagoonException.class)
 public void testInitWithInvalidSitemapFile() throws Exception {
 lagoonGUI = new LagoonGUI();
 lagoonGUI.sitemapFile.setValue("testSitemap.txt");
 lagoonGUI.init();
 fail("Init method should throw an exception with an invalid sitemap file");
 }

 @Test(expected = FileNotFoundException.class)
 public void testInitWithMissingSitemapFile() throws Exception {
 lagoonGUI = new LagoonGUI();
 lagoonGUI.sitemapFile.setValue("testSitemap.nonexistent");
 lagoonGUI.init();
 fail("Init method should throw a FileNotFoundException with a missing sitemap file");
 }

 @Test(expected = IOException.class)
 public void testInitWithIOException() throws Exception {
 lagoonGUI = new LagoonGUI();
 final File testSitemapFile = new File("testSitemap.xml");
 final FileInputStream fis = mock(FileInputStream.class);
 when(fis.close()).thenReturn(true);
 doThrow(new IOException("IOException")).when(fis).read();
 lagoonGUI.sitemapFile.setValue("testSitemap.xml");
 lagoonGUI.init();
 fail("Init method should throw an IOException with a read error");
 }

 @Test(expected = SAXException.class)
 public void testInitWithSAXException() throws Exception {
 lagoonGUI = new LagoonGUI();
 final File testSitemapFile = new File("testSitemap.xml");
 final FileInputStream fis = mock(FileInputStream.class);
 when(fis.close()).thenReturn(true);
 doThrow(new SAXException("SAXException")).when(fis).read();
 lagoonGUI.sitemapFile.setValue("testSitemap.xml");
 lagoonGUI.init();
 fail("Init method should throw a SAXException with a parse error");
 }

 @Test(expected = AuthenticationMissingException.class)
 public void testInitWithMissingAuthentication() throws Exception {
 lagoonGUI = new LagoonGUI();
 lagoonGUI.sitemapFile.setValue("testSitemap.xml");
 final YesNoQueryDialog yesNoDialog = mock(YesNoQueryDialog.class);
 doReturn(yesNoDialog).when(lagoonGUI).new YesNoQueryDialog(any(Component.class), anyString(), anyString(), anyString(), anyString());
 when(yesNoDialog.getResult()).thenReturn(false);
 lagoonGUI.init();
 fail("Init method should throw an AuthenticationMissingException if the user cancels the password dialog");
 }

 @Test(expected = AuthenticationException.class)
 public void testInitWithInvalidAuthentication() throws Exception {
 lagoonGUI = new LagoonGUI();
 lagoonGUI.sitemapFile.setValue("testSitemap.xml");
 final YesNoQueryDialog yesNoDialog = mock(YesNoQueryDialog.class);
 doReturn(yesNoDialog).when(lagoonGUI).new YesNoQueryDialog(any(Component.class), anyString(), anyString(), anyString(), anyString());
 when(yesNoDialog.getPassword()).thenReturn("wrongPassword");
 lagoonGUI.init();
 fail("Init method should throw an AuthenticationException if the user enters an incorrect password");
 }

 @Test
 public void testLoadPropertiesMethod() throws Exception {
 lagoonGUI = new LagoonGUI();
 final File testPropertyFile = new File("testPropertyFile.properties");
 final Properties testProperties = new Properties();
 testProperties.setProperty("sitemapFile", "testSitemap.xml");
 testProperties.setProperty("sourceDir", "testSourceDir");
 testProperties.setProperty("targetURL", "testTargetURL");
 final FileInputStream fis = mock(FileInputStream.class);
 when(fis.close()).thenReturn(true);
 when(fis.readAllBytes()).thenReturn(TestHelper.getPropertiesFileBytes("testPropertyFile.properties"));
 lagoonGUI.loadProperties(testPropertyFile);
 assertEquals("testSitemap.xml", lagoonGUI.sitemapFile.getValue());
 assertEquals("testSourceDir", lagoonGUI.sourceDir.getValue());
 assertEquals("testTargetURL", lagoonGUI.targetURL.getValue());
 verify(lagoonGUI).loadProperties(testPropertyFile);
 }

 @Test
 public void testSavePropertiesMethod() throws Exception {
 lagoonGUI = new LagoonGUI();
 lagoonGUI.sitemapFile.setValue("testSitemap.xml");
 lagoonGUI.sourceDir.setValue("testSourceDir");
 lagoonGUI.targetURL.setValue("testTargetURL");
 final Properties testProperties = new Properties();
 testProperties.setProperty("sitemapFile", "testSitemap.xml");
 testProperties.setProperty("sourceDir", "testSourceDir");
 testProperties.setProperty("targetURL", "testTargetURL");
 final FileOutputStream fos = mock(FileOutputStream.class);
 when(fos.close()).thenReturn(true);
 when(fos.write(anyByteArray())).thenAnswer(invocation -> invocation.getArgument(0).length);
 lagoonGUI.savePropertyFile();
 verify(lagoonGUI).savePropertyFile();
 }

 @Test
 public void testSavePropertiesFileWithPassword() throws Exception {
 lagoonGUI = new LagoonGUI();
 lagoonGUI.sitemapFile.setValue("testSitemap.xml");
 lagoonGUI.sourceDir.setValue("testSourceDir");
 lagoonGUI.targetURL.setValue("testTargetURL");
 final Properties testProperties = new Properties();
 testProperties.setProperty("sitemapFile", "testSitemap.xml");
 testProperties.setProperty("sourceDir", "testSourceDir");
 testProperties.setProperty("targetURL", "testTargetURL");
 final YesNoQueryDialog yesNoDialog = mock(YesNoQueryDialog.class);
 doReturn(yesNoDialog).when(lagoonGUI).new YesNoQueryDialog(any(Component.class), anyString(), anyString(), anyString(), anyString());
 when(yesNoDialog.getResult()).thenReturn(true);
 when(yesNoDialog.getPassword()).thenReturn("testPassword".toCharArray());
 lagoonGUI.savePropertyFile();
 final File testPropertyFile = new File("testPropertyFile.properties");
 final FileInputStream fis = new FileInputStream(testPropertyFile);
 final byte[] propertiesFileBytes = TestHelper.getPropertiesFileBytes("testPropertyFileWithPassword.properties");
 final int length = fis.read(propertiesFileBytes);
 assertEquals("testPassword".getBytes().length, length);
 }

}