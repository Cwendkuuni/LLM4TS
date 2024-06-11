package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Properties;

import javax.swing.*;

import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.LagoonException;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.MessageDialog;
import nu.staldal.lagoon.core.PasswordDialog;
import nu.staldal.lagoon.core.YesNoQueryDialog;
import nu.staldal.xtree.TreeBuilder;
import nu.staldal.xtree.TextAreaWriter;

public class LagoonGUITest {

 private LagoonGUI gui;

 @Before
 public void setUp() {
 gui = new LagoonGUI();
 gui.setVisible(true);
 }

 @Test
 public void testConstructor() {
 assertEquals("Lagoon", gui.getTitle());
 assertNotNull(gui.getInputPanel());
 assertNotNull(gui.getCenterPanel());
 assertNotNull(gui.getButtonPanel());
 assertNull(gui.getProcessor());
 assertEQ(0L, gui.getSitemapLastModified());
 }

 @Test
 public void testLoadButtonActionPerformed() {
 gui.loadButton.doClick();
 assertNotNull(gui.getProcessor());
 }

 @Test
 public void testSaveButtonActionPerformed() {
 gui.saveButton.doClick();
 assertNotNull(gui.getProcessor());
 // To be completed: add a verification for the saved property file
 }

 @Test
 public void testBuildButtonActionPerformed() {
 gui.buildButton.doClick();
 // To be completed: add a verification for the build process
 }

 @Test
 public void testForceButtonActionPerformed() {
 gui.forceButton.doClick();
 // To be completed: add a verification for the force build process
 }

 @Test(expected = LagoonException.class)
 public void testInitWithNullProcessor() {
 gui.init();
 fail("Init should throw an exception when processor is null");
 }

 @Test
 public void testInitWithValidSitemapFile() {
 final File sitemapFile = new File("validSitemap.xml");
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 gui.init();
 assertNotNull(gui.getProcessor());
 }

 @Test(expected = FileNotFoundException.class)
 public void testInitWithMissingSitemapFile() {
 gui.init();
 fail("Init should throw a FileNotFoundException when sitemap file is missing");
 }

 @Test(expected = LagoonException.class)
 public void testInitWithInvalidSitemapFile() {
 final File sitemapFile = new File("invalidSitemap.xml");
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 gui.init();
 fail("Init should throw a LagoonException when sitemap file is invalid");
 }

 @Test
 public void testLoadProperties() {
 final File propertyFile = new File("testProperties.xml");
 final Properties prop = new Properties();
 prop.setProperty("sitemapFile", "validSitemap.xml");
 prop.setProperty("sourceDir", "testSourceDir");
 prop.setProperty("targetURL", "testTargetURL.com");
 prop.setProperty("password", "testPassword");
 gui.loadProperties(propertyFile);
 assertEquals("testSourceDir", gui.sourceDir.getValue());
 assertEquals("testTargetURL.com", gui.targetURL.getValue());
 assertNotNull(gui.password);
 }

 @Test(expected = LagoonException.class)
 public void testLoadPropertiesWithMissingProperty() {
 final File propertyFile = new File("testPropertiesMissing.xml");
 gui.loadProperties(propertyFile);
 fail("LoadProperties should throw a LagoonException when a required property is missing");
 }

 @Test
 public void testSaveProperties() {
 final File savedPropertyFile = new File("testSavedProperties.xml");
 gui.savePropertyFile();
 assertTrue(savedPropertyFile.exists());
 // To be completed: add a verification for the saved property file content
 }

 @Test
 public void testSavePropertiesWithCancel() {
 final FileDialog propertyFileDialog = new FileDialog(gui, "Save property file", 1);
 propertyFileDialog.show();
 assertFalse(savedPropertyFile.exists());
 }

 @Test
 public void testSavePropertiesWithPasswordSaveDialogShown() {
 final FileDialog propertyFileDialog = new FileDialog(gui, "Save property file", 1);
 propertyFileDialog.show();
 final YesNoQueryDialog ynDialog = new YesNoQueryDialog(gui, "Save property file", "Save password in property file?", "Yes", "No");
 ynDialog.show();
 assertFalse(savedPropertyFile.exists());
 }

 @Test
 public void testSavePropertiesWithPasswordSaved() {
 final FileDialog propertyFileDialog = new FileDialog(gui, "Save property file", 1);
 propertyFileDialog.show();
 final YesNoQueryDialog ynDialog = new YesNoQueryDialog(gui, "Save property file", "Save password in property file?", "Yes", "No");
 ynDialog.setResult(true);
 final PasswordDialog pwdDialog = new PasswordDialog(gui, "Password", "Password needed to access target");
 pwdDialog.show();
 final String password = pwdDialog.getPassword();
 assertTrue(savedPropertyFile.exists());
 // To be completed: add a verification for the saved property file content
 }
}