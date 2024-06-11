package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;

import org.junit.Test;

import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.xtree.TreeBuilder;

public class LagoonGUITest {

 private LagoonGUI gui;

 @Test
 public void testConstructor() {
 gui = new LagoonGUI();
 assertNotNull(gui);
 }

 @Test
 public void testMain() throws Exception {
 LagoonGUI.main(new String[]{"test.properties"});
 assertNotNull(gui);
 }

 @Test
 public void testLagooonGUI_windowOpened() {
 gui = new LagoonGUI();
 gui.show();
 assertTrue(gui.isDisplayable());
 }

 @Test
 public void testLoadButton_actionPerformed() {
 gui = new LagoonGUI();
 gui.show();
 MockActionEvent loadEvent = new MockActionEvent("LOAD");
 gui.loadButton.addActionListener(e -> gui.loadPropertyFile());
 gui.loadButton.fireActionEvent(loadEvent);
 assertTrue(gui.isPropertyFileLoaded());
 }

 @Test
 public void testSaveButton_actionPerformed() {
 gui = new LagoonGUI();
 gui.show();
 MockActionEvent saveEvent = new MockActionEvent("SAVE");
 gui.saveButton.addActionListener(e -> gui.savePropertyFile());
 gui.saveButton.fireActionEvent(saveEvent);
 assertTrue(gui.isPropertyFileSaved());
 }

 @Test
 public void testBuildButton_actionPerformed() throws LagoonException, IOException {
 gui = new LagoonGUI();
 gui.show();
 MockActionEvent buildEvent = new MockActionEvent("BUILD");
 gui.buildButton.addActionListener(e -> gui.build(false));
 gui.buildButton.fireActionEvent(buildEvent);
 assertTrue(gui.isBuildSuccess());
 }

 @Test
 public void testForceButton_actionPerformed() throws LagoonException, IOException {
 gui = new LagoonGUI();
 gui.show();
 MockActionEvent forceEvent = new MockActionEvent("FORCE_BUILD");
 gui.forceButton.addActionListener(e -> gui.build(true));
 gui.forceButton.fireActionEvent(forceEvent);
 assertTrue(gui.isBuildSuccess());
 }

 @Test
 public void testExitButton_actionPerformed() {
 gui = new LagoonGUI();
 gui.show();
 MockActionEvent exitEvent = new MockActionEvent("EXIT");
 gui.exitButton.addActionListener(e -> gui.exit());
 gui.exitButton.fireActionEvent(exitEvent);
 assertTrue(gui.isClosed());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuild_forceWithNullProcessor() {
 gui = new LagoonGUI();
 gui.processor = null;
 gui.build(true);
 }

 @Test(expected = FileNotFoundException.class)
 public void testBuild_fileNotFound() throws LagoonException, IOException {
 File sm = File.createTempFile("testSitemap", "xml");
 gui = new LagoonGUI();
 gui.show();
 MockActionEvent buildEvent = new MockActionEvent("BUILD");
 gui.buildButton.addActionListener(e -> gui.build(false));
 gui.sitemapFile.setValue(sm.getAbsolutePath());
 gui.buildButton.fireActionEvent(buildEvent);
 }

 @Test(expected = IOException.class)
 public void testInit_ioException() throws LagoonException, IOException {
 gui = new LagoonGUI();
 gui.show();
 MockActionEvent initEvent = new MockActionEvent("INIT");
 gui.buttonPanel.add(new Button("INIT"));
 gui.buttonPanel.getComponent(0).addActionListener(e -> gui.init());
 gui.buttonPanel.getComponent(0).fireActionEvent(initEvent);
 }

 @Test(expected = AuthenticationMissingException.class)
 public void testInit_authenticationMissingException() throws LagoonException, IOException {
 gui = new LagoonGUI();
 gui.show();
 MockActionEvent initEvent = new MockActionEvent("INIT");
 gui.buttonPanel.add(new Button("INIT"));
 gui.buttonPanel.getComponent(0).addActionListener(e -> gui.init());
 gui.buttonPanel.getComponent(0).fireActionEvent(initEvent);
 assertTrue(gui.showPasswordDialog());
 }

 @Test(expected = AuthenticationException.class)
 public void testInit_authenticationException() throws LagoonException, IOException {
 gui = new LagoonGUI();
 gui.show();
 MockActionEvent initEvent = new MockActionEvent("INIT");
 gui.buttonPanel.add(new Button("INIT"));
 gui.buttonPanel.getComponent(0).addActionListener(e -> gui.init());
 gui.buttonPanel.getComponent(0).fireActionEvent(initEvent);
 assertFalse(gui.showPasswordDialog());
 assertTrue(gui.showPasswordDialog());
 }

 @Test
 public void testGetProperty() throws LagoonException {
 Properties prop = new Properties();
 prop.setProperty("sitemapFile", "test.xml");
 prop.setProperty("sourceDir", "src");
 prop.setProperty("targetURL", "http://localhost:8080");
 assertEquals("test.xml", LagoonGUI.getProperty(prop, "sitemapFile"));
 assertEquals("src", LagoonGUI.getProperty(prop, "sourceDir"));
 assertEquals("http://localhost:8080", LagoonGUI.getProperty(prop, "targetURL"));
 }

 @Test
 public void testInit_saxException() throws LagoonException, IOException, SAXException {
 File sm = new File("test.xml");
 gui = new LagoonGUI();
 gui.show();
 MockActionEvent initEvent = new MockActionEvent("INIT");
 gui.buttonPanel.add(new Button("INIT"));
 gui.buttonPanel.getComponent(0).addActionEvent(new ActionEvent(gui.buttonPanel.getComponent(0), ActionEvent.ACTION_PERFORMED, "INIT"));
 gui.sitemapFile.setValue(sm.getAbsolutePath());
 InputSource is = new InputSource(new FileInputStream(sm));
 try {
 TreeBuilder.parseXML(is, false);
 }
 catch (SAXException e) {
 throw e;
 }
 finally {
 is.getByteStream().close();
 }
 }

 @Test
 public void testInit_authenticationException_showDialog() {
 gui = new LagoonGUI();
 MockActionEvent initEvent = new MockActionEvent("INIT");
 assertTrue(gui.showPasswordDialog());
 }

 @Test
 public void testInit_authenticationException_doNotShowDialog() {
 gui = new LagoonGUI();
 MockActionEvent initEvent = new MockActionEvent("INIT");
 assertFalse(gui.showPasswordDialog());
 }

 @Test
 public void testInit_passwordDialogResult() {
 gui = new LagoonGUI();
 MockActionEvent initEvent = new MockActionEvent("INIT");
 PasswordDialog passwordDialog = new PasswordDialog(gui, "Password", "Password needed to access target");
 passwordDialog.setResult(JOptionPane.YES_OPTION);
 gui.showPasswordDialog = () -> passwordDialog.getResult();
 MockActionEvent passwordDialogEvent = new MockActionEvent("PASSWORD_DIALOG_RESULT");
 gui.buttonPanel.add(new Button("INIT"));
 gui.buttonPanel.getComponent(0).addActionListener(e -> gui.init());
 gui.buttonPanel.getComponent(0).fireActionEvent(initEvent);
 assertTrue(gui.isInitialized());
 }

 @Test
 public void testLoadPropertyFile_fileNotFound() {
 File propertyFile = File.createTempFile("test", "properties");
 gui = new LagoonGUI();
 MockActionEvent loadEvent = new MockActionEvent("LOAD");
 gui.loadButton.addActionListener(e -> gui.loadPropertyFile());
 gui.loadButton.fireActionEvent(loadEvent);
 assertFalse(gui.isPropertyFileLoaded());
 }

 @Test
 public void testLoadPropertyFile_propertiesLoaded() throws IOException {
 Properties prop = new Properties();
 prop.setProperty("sitemapFile", "test.xml");
 prop.setProperty("sourceDir", "src");
 prop.setProperty("targetURL", "http://localhost:8080");
 prop.setProperty("password", "testPassword");
 File propertyFile = File.createTempFile("test", "properties");
 try (FileOutputStream fos = new FileOutputStream(propertyFile)) {
 prop.store(fos, "Lagoon properties");
 }
 gui = new LagoonGUI();
 MockActionEvent loadEvent = new MockActionEvent("LOAD");
 gui.loadButton.addActionListener(e -> gui.loadPropertyFile());
 gui.loadButton.fireActionEvent(loadEvent);
 assertTrue(gui.isPropertyFileLoaded());
 assertEquals("test.xml", gui.sitemapFile.getValue());
 assertEquals("src", gui.sourceDir.getValue());
 assertEquals("http://localhost:8080", gui.targetURL.getValue());
 assertEquals("testPassword", gui.password);
 }

 @Test
 public void testSavePropertyFile() throws IOException {
 File propertyFile = File.createTempFile("test", "properties");
 Properties prop = new Properties();
 prop.setProperty("sitemapFile", "test.xml");
 prop.setProperty("sourceDir", "src");
 prop.setProperty("targetURL", "http://localhost:8080");
 prop.setProperty("password", "testPassword");
 try (FileOutputStream fos = new FileOutputStream(propertyFile)) {
 prop.store(fos, "Lagoon properties");
 }
 gui = new LagoonGUI();
 MockActionEvent saveEvent = new MockActionEvent("SAVE");
 gui.saveButton.addActionListener(e -> gui.savePropertyFile());
 gui.saveButton.fireActionEvent(saveEvent);
 assertTrue(gui.isPropertyFileSaved());
 }

 @Test
 public void testSavePropertyFile_passwordNotSaved() throws IOException {
 Properties prop = new Properties();
 prop.setProperty("sitemapFile", "test.xml");
 prop.setProperty("sourceDir", "src");
 prop.setProperty("targetURL", "http://localhost:8080");
 prop.setProperty("password", "testPassword");
 File propertyFile = File.createTempFile("test", "properties");
 try (FileOutputStream fos = new FileOutputStream(propertyFile)) {
 prop.store(fos, "Lagoon properties");
 }
 gui = new LagoonGUI();
 MockActionEvent saveEvent = new MockActionEvent("SAVE");
 gui.saveButton.addActionListener(e -> gui.savePropertyFile());
 gui.saveButton.fireActionEvent(saveEvent);
 assertFalse(gui.password.equals("testPassword"));
 }

 @Test
 public void testSavePropertyFile_saveWithDialog() {
 gui = new LagoonGUI();
 MockActionEvent saveEvent = new MockActionEvent("SAVE");
 MockFileDialog fileDialog = new MockFileDialog();
 gui.saveButton.addActionListener(e -> gui.savePropertyFile());
 gui.saveButton.fireActionEvent(saveEvent);
 assertTrue(fileDialog.isFileSaved());
 }

 @Test
 public void testSavePropertyFile_cancelSaveWithDialog() {
 gui = new LagoonGUI();
 MockActionEvent saveEvent = new MockActionEvent("SAVE");
 MockFileDialog fileDialog = new MockFileDialog();
 gui.saveButton.addActionListener(e -> gui.savePropertyFile());
 fileDialog.setCancelOption(true);
 gui.saveButton.fireActionEvent(saveEvent);
 assertFalse(fileDialog.isFileSaved());
 }
}