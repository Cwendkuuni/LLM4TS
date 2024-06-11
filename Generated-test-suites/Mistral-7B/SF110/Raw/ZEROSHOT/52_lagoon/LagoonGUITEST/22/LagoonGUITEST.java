package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.io.*;
import java.util.*;
import org.xml.sax.*;
import nu.staldal.lagoon.*;
import nu.staldal.xtree.*;
import org.junit.Test;

public class LagoonGUITest {

 private LagoonGUI gui;

 @Test
 public void testLagoonGUIConstruction() {
 LagoonGUI lagoonGUI = new LagoonGUI();
 assertNotNull(lagoonGUI);
 }

 @Test
 public void testInputComponents() {
 LagoonGUI lagoonGUI = new LagoonGUI();
 assertNotNull(lagoonGUI.sitemapFile);
 assertNotNull(lagoonGUI.sourceDir);
 assertNotNull(lagoonGUI.targetURL);
 }

 @Test
 public void testLoadButtonActionPerformed() throws Exception {
 LagoonGUI lagoonGUI = new LagoonGUI();
 MockFile file = new MockFile("test.properties");
 lagoonGUI.loadButton.doClick();
 lagoonGUI.loadProperties(file);
 assertEquals("test.properties", lagoonGUI.sitemapFile.getValue());
 }

 @Test
 public void testSaveButtonActionPerformed() throws Exception {
 LagoonGUI lagoonGUI = new LagoonGUI();
 File file = new File("test.properties");
 lagoonGUI.savePropertyFile();
 assertEquals("test.properties", lagoonGUI.sitemapFile.getValue());
 try (FileOutputStream fos = new FileOutputStream(file)) {
 Properties prop = lagoonGUI.getProperties();
 prop.store(fos, "Lagoon properties");
 }
 }

 @Test
 public void testBuildButtonActionPerformed() throws Exception {
 LagoonGUI lagoonGUI = new LagoonGUI();
 File sitemapFile = new File("test.xml");
 File sourceDir = new File("test");
 File targetURL = new File("http://test.com");
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.sourceDir.setValue(sourceDir.getAbsolutePath());
 lagoonGUI.targetURL.setValue(targetURL.getAbsolutePath());
 lagoonGUI.build(false);
 assertTrue(lagoonGUI.statusLabel.getText().contains("Building..."));
 }

 @Test(expected = LagoonException.class)
 public void testBuildButtonActionPerformedWithInvalidSitemapFile() throws Exception {
 LagoonGUI lagoonGUI = new LagoonGUI();
 File sitemapFile = new File("doesnotexist.xml");
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.sourceDir.setValue(".");
 lagoonGUI.targetURL.setValue(".");
 lagoonGUI.build(false);
 fail("Expected LagoonException to be thrown");
 }

 @Test(expected = IOException.class)
 public void testBuildButtonActionPerformedWithIOException() throws Exception {
 LagoonGUI lagoonGUI = new LagoonGUI();
 File sitemapFile = new File("test.xml");
 File sourceDir = new File("test");
 File targetURL = new File("http://test.com");
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.sourceDir.setValue(sourceDir.getAbsolutePath());
 lagoonGUI.targetURL.setValue(targetURL.getAbsolutePath());
 when(saxParserFactory.newSAXParser()).thenThrow(new SAXException());
 lagoonGUI.build(false);
 fail("Expected IOException to be thrown");
 }

 @Test
 public void testBuildButtonActionPerformedWithSuccess() throws Exception {
 LagoonGUI lagoonGUI = new LagoonGUI();
 File sitemapFile = new File("test.xml");
 File sourceDir = new File("test");
 File targetURL = new File("http://test.com");
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.sourceDir.setValue(sourceDir.getAbsolutePath());
 lagoonGUI.targetURL.setValue(targetURL.getAbsolutePath());
 when(saxParserFactory.newSAXParser()).thenReturn(mockParser);
 when(mockParser.parse(any(InputSource.class))).thenReturn(mockElement);
 lagoonGUI.build(false);
 assertTrue(lagoonGUI.statusLabel.getText().contains("Building..."));
 verify(mockProgressWriter).write("--- Start build ---\n");
 verify(mockProgressWriter).write("--- Build finished successfully " + anyString() + " ---");
 }

 @Test
 public void testExitButtonActionPerformed() throws Exception {
 LagoonGUI lagoonGUI = new LagoonGUI();
 lagoonGUI.exitButton.doClick();
 assertEquals(0, System.exit(0));
 }

 @Test
 public void testInit() throws Exception {
 LagoonGUI lagoonGUI = new LagoonGUI();
 File sitemapFile = new File("test.xml");
 File sourceDir = new File("test");
 File targetURL = new File("http://test.com");
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.sourceDir.setValue(sourceDir.getAbsolutePath());
 lagoonGUI.targetURL.setValue(targetURL.getAbsolutePath());
 doCallRealMethod().when(lagoonGUI);
 FileInputStream fis = mock(FileInputStream.class);
 InputSource is = mock(InputSource.class);
 when(fis.close()).thenReturn(true);
 when(is.getByteStream()).thenReturn(fis);
 when(TreeBuilder.parseXML(is, false)).thenReturn(mockElement);
 when(saxParserFactory.newSAXParser()).thenReturn(mockParser);
 when(mockParser.parse(any(InputSource.class))).thenReturn(mockElement);
 lagoonGUI.init();
 verify(lagoonGUI).destroy();
 verify(lagoonGUI).processor = new LagoonProcessor(targetURL.getAbsolutePath(), mockElement, sourceDir, null, mockProgressWriter, mockProgressWriter);
 verifyZeroInteractions(lagoonGUI.loadButton);
 verifyZeroInteractions(lagoonGUI.saveButton);
 verifyZeroInteractions(lagoonGUI.buildButton);
 verifyZeroInteractions(lagoonGUI.forceButton);
 verifyZeroInteractions(lagoonGUI.exitButton);
 }

 @Test
 public void testInitWithAuthenticationMissingException() throws Exception {
 LagoonGUI lagoonGUI = new LagoonGUI();
 File sitemapFile = new File("test.xml");
 File sourceDir = new File("test");
 File targetURL = new File("http://test.com");
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.sourceDir.setValue(sourceDir.getAbsolutePath());
 lagoonGUI.targetURL.setValue(targetURL.getAbsolutePath());
 doCallRealMethod().when(lagoonGUI);
 when(mockPasswordDialog.show()).thenReturn(true);
 when(mockPasswordDialog.getPassword()).thenReturn("password");
 when(saxParserFactory.newSAXParser()).thenReturn(mockParser);
 when(mockParser.parse(any(InputSource.class))).thenReturn(mockElement);
 lagoonGUI.init();
 verify(lagoonGUI).destroy();
 verify(lagoonGUI).processor = new LagoonProcessor(targetURL.getAbsolutePath(), mockElement, sourceDir, "password", mockProgressWriter, mockProgressWriter);
 verify(mockPasswordDialog).show();
 }

 @Test
 public void testInitWithAuthenticationException() throws Exception {
 LagoonGUI lagoonGUI = new LagoonGUI();
 File sitemapFile = new File("test.xml");
 File sourceDir = new File("test");
 File targetURL = new File("http://test.com");
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.sourceDir.setValue(sourceDir.getAbsolutePath());
 lagoonGUI.targetURL.setValue(targetURL.getAbsolutePath());
 doCallRealMethod().when(lagoonGUI);
 when(mockPasswordDialog.show()).thenReturn(true);
 when(mockPasswordDialog.getPassword()).thenReturn("wrongpassword");
 when(saxParserFactory.newSAXParser()).thenReturn(mockParser);
 when(mockParser.parse(any(InputSource.class))).thenReturn(mockElement);
 lagoonGUI.init();
 verify(lagoonGUI).destroy();
 verify(lagoonGUI).init();
 }

 @Test
 public void testLoadProperties() throws Exception {
 LagoonGUI lagoonGUI = new LagoonGUI();
 Properties properties = new Properties();
 properties.setProperty("sitemapFile", "test.xml");
 properties.setProperty("sourceDir", "test");
 properties.setProperty("targetURL", "http://test.com");
 properties.setProperty("password", "password");
 lagoonGUI.loadProperties(new File("test.properties"));
 assertEquals("test.xml", lagoonGUI.sitemapFile.getValue());
 assertEquals("test", lagoonGUI.sourceDir.getValue());
 assertEquals("http://test.com", lagoonGUI.targetURL.getValue());
 assertEquals("password", lagoonGUI.password);
 }

 @Test
 public void testSaveProperties() throws Exception {
 LagoonGUI lagoonGUI = new LagoonGUI();
 File file = new File("test.properties");
 Properties properties = new Properties();
 properties.setProperty("sitemapFile", "test.xml");
 properties.setProperty("sourceDir", "test");
 properties.setProperty("targetURL", "http://test.com");
 lagoonGUI.savePropertyFile();
 try (FileOutputStream fos = new FileOutputStream(file)) {
 Properties prop = lagoonGUI.getProperties();
 prop.store(fos, "Lagoon properties");
 }
 assertEquals("test.xml", lagoonGUI.sitemapFile.getValue());
 assertEquals("test", lagoonGUI.sourceDir.getValue());
 assertEquals("http://test.com", lagoonGUI.targetURL.getValue());
 assertEquals("password", lagoonGUI.password);
 }

 private static MockFile newMockFile(String fileName, String content) {
 MockFile file = new MockFile();
 file.name = fileName;
 file.content = content;
 return file;
 }

 private static MockProgressWriter newMockProgressWriter() {
 MockProgressWriter writer = new MockProgressWriter();
 return writer;
 }

 private static MockFile mockFile() {
 return new MockFile();
 }

 private static MockParser mockParser() {
 MockParser parser = new MockParser();
 return parser;
 }

 private static SAXParserFactory saxParserFactory() {
 SAXParserFactory factory = mock(SAXParserFactory.class);
 return factory;
 }

 private static Properties getProperties(LagoonGUI lagoonGUI) {
 Properties properties = new Properties();
 properties.setProperty("sitemapFile", lagoonGUI.sitemapFile.getValue());
 properties.setProperty("sourceDir", lagoonGUI.sourceDir.getValue());
 properties.setProperty("targetURL", lagoonGUI.targetURL.getValue());
 properties.setProperty("password", lagoonGUI.password);
 return properties;
 }

 private void doCallRealMethod() throws Exception {
 LagoonGUI lagoonGUI = this.gui;
 Class<?> clazz = LagoonGUI.class;
 Method method = clazz.getDeclaredMethod("init");
 method.setAccessible(true);
 method.invoke(lagoonGUI);
 }

 private static class MockFile {
 String name;
 String content;
 }

 private static class MockPasswordDialog extends Dialog {
 String message;
 String title;
 String password;
 boolean result;

 public MockPasswordDialog(Frame owner, String title, String message, String password) {
 super(owner, title, ModalityType.MODELESS);
 this.title = title;
 this.message = message;
 this.password = password;
 }

 public String getPassword() {
 return this.password;
 }

 public boolean show() {
 this.result = true;
 return this.result;
 }
 }

 private static class MockProgressWriter implements ProgressWriter {
 private List<String> writeCalls = new ArrayList<>();

 @Override
 public void write(String message) {
 this.writeCalls.add(message);
 }

 List<String> getWriteCalls() {
 return this.writeCalls;
 }
 }

 private static class MockParser implements SAXParser {
 private Element element;

 @Override
 public void parse(InputSource inputSource) throws SAXException, IOException {
 this.element = inputSource.getXML();
 }

 Element getElement() {
 return this.element;
 }
 }
}